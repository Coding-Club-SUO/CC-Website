import { proxyToBackend } from "@/lib/bff-proxy";

export async function GET() {
    return proxyToBackend({ path: '/resources', tags: ['resources'] }, true)
}