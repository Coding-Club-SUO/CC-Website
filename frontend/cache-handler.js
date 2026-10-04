import Redis from 'ioredis'

const redis = new Redis({
  host: process.env.REDIS_HOST || 'redis',
  port: Number(process.env.REDIS_PORT) || 6379,
  password: process.env.REDIS_PASSWORD || undefined,
  maxRetriesPerRequest: 3,
  // Queue commands issued before the connection is ready (e.g. right after boot).
  enableOfflineQueue: true,
})

redis.on('error', (error) => {
  console.warn('[Redis cache] connection warning:', error.message)
})

// Cache entries hold Buffers and Maps (e.g. APP_PAGE rscData / segmentData),
// which plain JSON would flatten into unusable objects.
function serialize(value) {
  return JSON.stringify(value, function (key, val) {
    const raw = this[key]
    if (Buffer.isBuffer(raw)) return { __type: 'Buffer', data: raw.toString('base64') }
    if (raw instanceof Map) return { __type: 'Map', data: [...raw.entries()] }
    return val
  })
}

function deserialize(text) {
  return JSON.parse(text, (key, val) => {
    if (val?.__type === 'Buffer') return Buffer.from(val.data, 'base64')
    if (val?.__type === 'Map') return new Map(val.data)
    return val
  })
}

export default class RedisCacheHandler {
  // The cache is best-effort: a Redis failure must never fail a page render.
  async get(key) {
    try {
      const data = await redis.get(key)
      return data ? deserialize(data) : null
    } catch (error) {
      console.warn('[Redis cache] get failed:', error.message)
      return null
    }
  }

  async set(key, data, ctx) {
    try {
      // Next expects entries shaped { value, lastModified, tags } back from get().
      await redis.set(
        key,
        serialize({ value: data, lastModified: Date.now(), tags: ctx?.tags ?? [] }),
      )

      if (typeof ctx?.revalidate === 'number') {
        await redis.expire(key, ctx.revalidate)
      }

      if (Array.isArray(ctx?.tags)) {
        for (const tag of ctx.tags) {
          await redis.sadd(`tag:${tag}`, key)
        }
      }
    } catch (error) {
      console.warn('[Redis cache] set failed:', error.message)
    }
  }

  async revalidateTag(tags) {
    try {
      for (const tag of [tags].flat()) {
        const keys = await redis.smembers(`tag:${tag}`)
        if (keys.length) {
          await redis.del(...keys)
          await redis.del(`tag:${tag}`)
        }
      }
    } catch (error) {
      console.warn('[Redis cache] revalidateTag failed:', error.message)
    }
  }

  resetRequestCache() {}
}
