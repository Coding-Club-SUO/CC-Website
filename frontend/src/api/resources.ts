import { api } from "../context/AuthContext"

export type Resource = {
    id: number
    title: string
    course: string
    uploader: string
    uploadedAt: string
    downloadUrl: string
}

export async function getResources(): Promise<Resource[]> {
    const response = await api.get<Resource[]>('/api/v1/resources')
    
    return response.data
}