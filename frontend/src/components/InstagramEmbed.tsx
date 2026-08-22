"use client"

import { useEffect } from 'react'

// Instagram's embed.js attaches this global object when it loads.
declare global {
  interface Window {
    instgrm?: {
      Embeds: {
        process: () => void
      }
    }
  }
}

const POST_URL = 'https://www.instagram.com/p/DVZVf-SEhnE/'

export default function InstagramEmbed() {
  useEffect(() => {
    const scriptId = 'instagram-embed-script'
    if (!document.getElementById(scriptId)) {
      const script = document.createElement('script')
      script.id = scriptId
      script.src = 'https://www.instagram.com/embed.js'
      script.async = true
      document.body.appendChild(script)
    } else {
      window.instgrm?.Embeds?.process()
    }
  }, [])

  return (
    <blockquote
      className="instagram-media"
      data-instgrm-permalink={POST_URL}
      data-instgrm-version="14"
      style={{ margin: '0 auto', maxWidth: '540px', width: '100%' }}
    >
      <a href={POST_URL}>View this post on Instagram</a>
    </blockquote>
  )
}