"use client"

import { useEffect, useRef, useState } from 'react'
import Link from 'next/link'
import Image from 'next/image'
import { usePathname } from 'next/navigation'
import ThemeToggle from '../ThemeToggle'
import { LOGO } from '../../content/site'
import './Navbar.css'

const ABOUT_LINKS = [
    { href: '/about/get-involved', label: 'Get Involved' },
    { href: '/about/team', label: 'Meet the Team' },
    { href: '/about/contribute', label: 'Contribute' },
    { href: '/about/vision', label: 'About CSCU' },
]

export default function Navbar() {
    const pathname = usePathname()
    const [aboutOpen, setAboutOpen] = useState(false)
    const dropdownRef = useRef<HTMLDivElement>(null)

    // Close on route change, outside click, or Escape
    useEffect(() => setAboutOpen(false), [pathname])
    useEffect(() => {
        const onClick = (e: MouseEvent) => {
            if (!dropdownRef.current?.contains(e.target as Node)) setAboutOpen(false)
        }
        const onKey = (e: KeyboardEvent) => {
            if (e.key === 'Escape') setAboutOpen(false)
        }
        document.addEventListener('mousedown', onClick)
        document.addEventListener('keydown', onKey)
        return () => {
            document.removeEventListener('mousedown', onClick)
            document.removeEventListener('keydown', onKey)
        }
    }, [])

    return (
        <nav className="navbar">
            <Link href="/" className="nav-brand">
                <Image src={LOGO.src} width={LOGO.width} height={LOGO.height} className="nav-logo" alt="" />
                <span>CSCU</span>
            </Link>
            <div className="nav-links">
                <Link href="/" className={`nav-link${pathname === '/' ? ' active' : ''}`}>Home</Link>

                <div className="nav-dropdown" ref={dropdownRef}>
                    <button
                        type="button"
                        className={`nav-link nav-dropdown-toggle${pathname.startsWith('/about') ? ' active' : ''}`}
                        aria-haspopup="true"
                        aria-expanded={aboutOpen}
                        onClick={() => setAboutOpen(open => !open)}
                    >
                        About <span aria-hidden="true">▾</span>
                    </button>
                    {aboutOpen && (
                        <div className="nav-dropdown-menu">
                            {ABOUT_LINKS.map(link => (
                                <Link
                                    key={link.href}
                                    href={link.href}
                                    className={`nav-dropdown-item${pathname === link.href ? ' active' : ''}`}
                                >
                                    {link.label}
                                </Link>
                            ))}
                        </div>
                    )}
                </div>

                <Link href="/events" className={`nav-link${pathname.startsWith('/events') ? ' active' : ''}`}>Events</Link>
                <Link href="/resourcehub" className={`nav-link${pathname.startsWith("/resourcehub") || pathname.startsWith("/resources") ? " active" : ""}`}>Resource Hub</Link>
                <ThemeToggle />
            </div>
        </nav>
    )
}
