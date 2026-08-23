'use server'
import { cookies } from "next/headers"
import { revalidateTag } from 'next/cache'
import { jwtDecode } from "jwt-decode"

import { logger } from "@/lib/logger"


const baseUrl = process.env.API_BASE_URL ?? "http://backend:8000/api/v1"
const ACCESS_COOKIE = 'access_token'
const REFRESH_COOKIE = 'refresh_token'

interface UserData {
  username: string;
  email: string;
  password: string;
  rememberUser: boolean;
}

function cookieOpts(maxAge: number, path = '/') {
    return {
        httpOnly: true,
        secure: process.env.NODE_ENV === 'production',
        sameSite: 'strict' as const,
        path,
        maxAge,
    }
}

async function setAuthCookies(
    accessToken: string, refreshToken: string, 
    rememberMe: boolean = false, expiresIn: number = 0) {
    const cookieStore = await cookies()
    const refreshExp = expiresIn > 0 ? expiresIn : (rememberMe ? 
        Number(process.env.MAX_REFRESH_EXP) | 2592000 : Number(process.env.REFRESH_EXP) | 86400)
    cookieStore.set(ACCESS_COOKIE, accessToken, cookieOpts(Number(process.env.ACCESS_EXP) | 600))
    cookieStore.set(REFRESH_COOKIE, refreshToken, cookieOpts(refreshExp, '/'))
}

async function clearAuthCookies() {
    const cookieStore = await cookies()
    cookieStore.delete(ACCESS_COOKIE)
    cookieStore.delete(REFRESH_COOKIE)
}

async function getAccessToken() {
    const cookieStore = await cookies()
    return cookieStore.get(ACCESS_COOKIE)?.value
}

async function getRefreshToken() {
    const cookieStore = await cookies()
    return cookieStore.get(REFRESH_COOKIE)?.value
}


export async function register(formData: UserData) {
    const response = await fetch(`${baseUrl}/auth/register`,{
        method: 'POST',
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(formData),
    })

    if (!response.ok) {
        logger.warn(`registration failed with status ${response.status}`)
        throw new Error(`Registration failed`)
    }

    const data = await response.json()
    const { accessToken, refreshToken, userData } = data

    await setAuthCookies(accessToken, refreshToken)
    revalidateTag(`user:${userData.id}`, 'default')
    logger.info(`registration successful for user ${userData.id}`)

    return { user: userData }
}


export async function login(identifier: string, password: string, rememberMe: boolean = false) {
    const response = await fetch(`${baseUrl}/auth/login`, {
        method: 'POST',
        headers: {
            "Content-Type": "application/x-www-form-urlencoded",
        },
        body: new URLSearchParams({
            identifier,
            password,
            rememberUser: rememberMe ? "true" : "false",
        }),
    })

    if (!response.ok) {
        logger.warn(`login failed with status ${response.status}`)
        throw new Error(`Login failed`)
    }

    const data = await response.json()
    const { accessToken, refreshToken, userData } = data

    await setAuthCookies(accessToken, refreshToken, rememberMe)
    revalidateTag(`user:${userData.id}`, 'default')

    logger.info(`login successful for user ${userData.id}`)

    return { user: userData }
}


export async function logout() {
    const accessToken = await getAccessToken()
    const refreshToken = await getRefreshToken()
    const userId = refreshToken ? jwtDecode<{ sub: string}>(refreshToken) : null

    if (accessToken && refreshToken) {
        const response = await fetch(`${baseUrl}/auth/logout`, {
            method: 'POST',
            body: JSON.stringify({ refreshToken }),
            headers: {
                Authorization: `Bearer ${accessToken}`,
            },
        })

        if (!response.ok) {
            if (userId) {
                logger.warn(`logout failed with status ${response.status} for user ${userId}`)
            } else {
                logger.warn(`logout failed with status ${response.status}`)
            }
        }
    }
    if (userId) {
        await revalidateTag(`user:${userId}`, 'default')
        logger.info(`logout successful for user ${userId}`) 
    } else {
        logger.info('logout successful')
    }
    await clearAuthCookies()
}


export async function refreshAccessToken() {
    const refreshToken = await getRefreshToken()
    const userId = refreshToken ? jwtDecode<{ sub: string}>(refreshToken) : null

    if (!refreshToken) {
        logger.info('no refresh token available')
        throw new Error('No refresh token available')
    }

    const response = await fetch(`${baseUrl}/auth/refresh`, {
        method: 'POST',
        body: JSON.stringify({ refreshToken }),
    })

    if (!response.ok) {
        await clearAuthCookies()
        if (userId) {
            logger.info(`refresh failed - session expired for user ${userId}`)
        } else {
            logger.info('refresh failed - session expired')
        }
        throw new Error('Refresh failed — session expired')
    }

    const data = await response.json()
    const { accessToken, refreshToken: newRefreshToken, expiresIn } = data
    if (userId) {
        await revalidateTag(`user:${userId}`, 'default')
        logger.info(`refresh successful for user ${userId}`)
    } else {
        logger.info('refresh successful')
    }
    await setAuthCookies(accessToken, newRefreshToken, false, expiresIn)
}