export type Resource = {
    id: number
    title: string
    course: string
    uploader: string
    uploadedAt: string
    downloadUrl: string
}

export async function getResources(): Promise<Resource[]> {
    const baseUrl = process.env.NEXT_PUBLIC_API_BASE_URL ?? "http://localhost:8000/api/v1"
    const response = await fetch(`${baseUrl}/resources`, {
        next: { tags: ['resources'] }
    })

    if (!response.ok) {
        throw new Error(`Resources request failed with status ${response.status}`)
    }
    return response.json() as Promise<Resource[]>
} 
