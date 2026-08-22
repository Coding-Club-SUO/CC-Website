import type { NextConfig } from 'next'
import { fileURLToPath } from 'node:url'

const nextConfig: NextConfig = {
  output: 'standalone',
  cacheHandler: fileURLToPath(new URL('./cache-handler.js', import.meta.url)),
  cacheMaxMemorySize: 0,
  cacheComponents: true,
}

export default nextConfig