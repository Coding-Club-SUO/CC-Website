// the file holds functions that talk to the backend's
// resource endpoints.

export type Resource = {
    id: number
    title: string
    course: string
    uploader: string
    uploadedAt: string
    downloadUrl: string
}
export async function getResources(): Promise<Resource[]> {
// await is to make sure to pause until the response
    const response = await fetch('/api/resources')
// the fetch is actual call to the backend endpoint
    if (!response.ok) {
        throw new Error(`Failed to load resources (status ${response.status})`)
        //using backticks to drop a variable inside a string, 
        //anything else will be treated as a string,
    }//throwing error instead of letting the page crash

    return response.json()
}