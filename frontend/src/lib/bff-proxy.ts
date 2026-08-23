import { refreshAccessToken } from '@/api/auth/auth'
import { cookies } from 'next/headers'
import { NextResponse } from 'next/server'

const baseUrl = process.env.API_BASE_URL ?? "http://backend:8000/api/v1"

interface ProxyOptions {
    path: string
    method?: string
    body?: BodyInit
    tags?: string[]
    revalidateAfter?: string[]
}

export async function proxyToBackend(
    { path, method = 'GET', body, tags, revalidateAfter }: ProxyOptions,
    isPublic: boolean = false,
) {
    const cookieStore = await cookies()
    let accessToken = cookieStore.get('access_token')?.value

    if (!accessToken && !isPublic) {
        return NextResponse.json({ message: 'Not authenticated' }, { status: 401 })
    }

    const createHeaders = (token?: string): Record<string, string> => ({
        ...(body instanceof FormData ? {} : { 'Content-Type': 'application/json' }),
        ...(isPublic && token ? { Authorization: `Bearer ${token}` } : {}),
    })

    let backendResponse = await fetch(`${baseUrl}${path}`, {
        method, headers: createHeaders(accessToken),
        body, ...(tags ? { next: { tags } } : {}),
    })
    
    if (!backendResponse.ok && backendResponse.status === 401) {
        await refreshAccessToken()
        accessToken = cookieStore.get('access_token')?.value

        backendResponse = await fetch(`${baseUrl}${path}`, {
            method, headers: createHeaders(accessToken),
            body, ...(tags ? { next: { tags } } : {}),
        })
    }

    if (!backendResponse.ok) {
        const errorText = await backendResponse.text()
        return NextResponse.json({ message: errorText || 'Request failed' }, { status: backendResponse.status })
    }

    if (revalidateAfter?.length) {
        const { revalidateTag } = await import('next/cache')
        for (const tag of revalidateAfter) revalidateTag(tag, 'default')
    }

    const contentType = backendResponse.headers.get('content-type')
    const data = contentType?.includes('application/json') ? await backendResponse.json() : null

    return NextResponse.json(data)
}