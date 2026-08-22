import Redis from 'ioredis'

const redis = new Redis({
  host: process.env.REDIS_HOST || 'redis',
  port: Number(process.env.REDIS_PORT) || 6379,
  password: process.env.REDIS_PASSWORD || undefined,
  maxRetriesPerRequest: 3,
  enableOfflineQueue: false,
})

redis.on('error', (error) => {
  console.warn('[Redis cache] connection warning:', error.message)
})

export default class RedisCacheHandler {
  async get(key) {
    const data = await redis.get(key)
    return data ? JSON.parse(data) : null
  }

  async set(key, data, ctx) {
    await redis.set(key, JSON.stringify(data))

    if (typeof ctx?.revalidate === 'number') {
      await redis.expire(key, ctx.revalidate)
    }

    if (Array.isArray(ctx?.tags)) {
      for (const tag of ctx.tags) {
        await redis.sadd(`tag:${tag}`, key)
      }
    }
  }

  async revalidateTag(tag) {
    const keys = await redis.smembers(`tag:${tag}`)
    if (keys.length) {
      await redis.del(...keys)
      await redis.del(`tag:${tag}`)
    }
  }
}