import { NextResponse } from 'next/server'
import type { NextRequest } from 'next/server'
import { jwtDecode } from 'jwt-decode'

export async function proxy(request: NextRequest) {
    const accessToken = request.cookies.get('access_token')?.value
    const refreshToken = request.cookies.get('refresh_token')?.value

    if (!refreshToken) {
        return NextResponse.next()
    }

    if (accessToken && !isExpired(accessToken)) {
        return NextResponse.next()
    }

    const baseUrl = process.env.API_BASE_URL ?? "http://backend:8000/api/v1"
    const refreshResponse = await fetch(`${baseUrl}/auth/refresh`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ refreshToken }),
    })

    if (!refreshResponse.ok) {
        const response = NextResponse.next()
        response.cookies.delete('access_token')
        response.cookies.delete('refresh_token')
        return response
    }

    const { accessToken: newAccessToken, refreshToken: newRefreshToken, expiresIn } = await refreshResponse.json()

    const response = NextResponse.next()
    response.cookies.set('access_token', newAccessToken, {
        httpOnly: true,
        secure: process.env.NODE_ENV === 'production',
        sameSite: 'strict',
        path: '/',
        maxAge: Number(process.env.ACCESS_EXP) || 600,
    })
    response.cookies.set('refresh_token', newRefreshToken, {
        httpOnly: true,
        secure: process.env.NODE_ENV === 'production',
        sameSite: 'strict',
        path: '/',
        maxAge: expiresIn,
    })
    return response
}

function isExpired(token: string): boolean {
    try {
        const decoded = jwtDecode<{ exp: number }>(token)
        return Date.now() >= decoded.exp * 1000
    } catch {
        return true
    }
}

export const config = {
    matcher: [
        // run on all routes except static assets/api internals
        '/((?!_next/static|_next/image|favicon.ico).*)',
    ],
}