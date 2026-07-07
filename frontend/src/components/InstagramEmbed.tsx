import { useEffect } from 'react'

const POST_URL = 'https://www.instagram.com/p/DVZVf-SEhnE/'

export default function InstagramEmbed() {
    useEffect(() => {
        const scriptId = 'instagram-embed-script'
        // Check if the script is already present
        if (!document.getElementById(scriptId)) {
            const script = document.createElement('script')
            script.id = scriptId
            script.src = 'https://www.instagram.com/embed.js'
            document.body.appendChild(script)
        }
        else {
            // If the script is already present, reinitialize the Instagram embeds
            ;(window as any).instgrm?.Embeds?.process()
        }
    }, [])

    return (
        <blockquote
            className="instagram-media"
            data-instgrm-permalink={POST_URL}
            data-instgrm-version="14"
            style={{ margin: '0 auto', maxWidth: '540px', width: '100%'}}
            >
                <a href={POST_URL}>View this post on Instagram</a>
            </blockquote>
    )
}
   