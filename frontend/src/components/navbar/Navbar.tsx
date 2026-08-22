"use client"

import Link from 'next/link'
import { usePathname } from 'next/navigation'
import './Navbar.css'

export default function Navbar() {
    const pathname = usePathname()

    return (
        <nav className="navbar">
            <Link href="/" className="nav-brand">
            Coding Club <span className="nav-brand-accent">Resource Hub</span>
                </Link>
                <div className="nav-links">
                    <Link href="/" className={`nav-link${pathname === '/' ? ' active' : ''}`}>Home</Link>
                    <Link href="/resources" className={`nav-link${pathname.startsWith('/resources') ? ' active' : ''}`}>Resources</Link>
                 </div>
        </nav>
    )
}