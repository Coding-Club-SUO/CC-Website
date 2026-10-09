import Image from 'next/image'
import Link from 'next/link'
import { LOGO, LINKS, EMAIL, ORG_NAME } from '../../content/site'
import './Footer.css'

const ICON_PROPS = {
    width: 18,
    height: 18,
    viewBox: '0 0 24 24',
    fill: 'none',
    stroke: 'currentColor',
    strokeWidth: 1.8,
    strokeLinecap: 'round' as const,
    strokeLinejoin: 'round' as const,
    'aria-hidden': true,
}

const ICONS = {
    Instagram: (
        <svg {...ICON_PROPS}>
            <rect x="3" y="3" width="18" height="18" rx="5" />
            <circle cx="12" cy="12" r="4" />
            <circle cx="17.5" cy="6.5" r="0.8" fill="currentColor" />
        </svg>
    ),
    Discord: (
        <svg {...ICON_PROPS}>
            <path d="M21 12a8 8 0 0 1-11.6 7.1L4 20l1.1-4.4A8 8 0 1 1 21 12Z" />
        </svg>
    ),
    Email: (
        <svg {...ICON_PROPS}>
            <rect x="3" y="5" width="18" height="14" rx="2" />
            <path d="m3.5 6.5 8.5 6.5 8.5-6.5" />
        </svg>
    ),
    Rubric: (
        <svg {...ICON_PROPS}>
            <path d="M3 9a2 2 0 0 0 0 6v2a1 1 0 0 0 1 1h16a1 1 0 0 0 1-1v-2a2 2 0 0 1 0-6V7a1 1 0 0 0-1-1H4a1 1 0 0 0-1 1Z" />
            <path d="M14 6v12" strokeDasharray="2 2" />
        </svg>
    ),
}

const SOCIALS = [
    { label: 'Instagram', href: LINKS.instagram },
    { label: 'Discord', href: LINKS.discord },
    { label: 'Email', href: EMAIL ? `mailto:${EMAIL}` : null },
    { label: 'Rubric', href: LINKS.rubric },
] as const

const SITE_LINKS = [
    { href: '/', label: 'Home' },
    { href: '/about/vision', label: 'About' },
    { href: '/about/get-involved', label: 'Get Involved' },
    { href: '/events', label: 'Events' },
    { href: '/resourcehub', label: 'Resource Hub' },
]

export default function Footer() {
    return (
        <footer className="footer">
            <div className="footer-inner">
                <div className="footer-brand">
                    <Link href="/" className="footer-logo-row">
                        <Image src={LOGO.src} width={LOGO.width} height={LOGO.height} className="footer-logo" alt="" />
                        <span className="footer-name">CSCU</span>
                    </Link>
                    <p className="footer-blurb">{ORG_NAME} at UBC Okanagan. Run by students.</p>
                </div>

                <nav className="footer-col" aria-label="Footer">
                    <h2 className="footer-heading">Explore</h2>
                    {SITE_LINKS.map(link => (
                        <Link key={link.href} href={link.href} className="footer-link">{link.label}</Link>
                    ))}
                </nav>

                <div className="footer-col">
                    <h2 className="footer-heading">Connect</h2>
                    <ul className="footer-socials">
                        {SOCIALS.map(social => (
                            <li key={social.label}>
                                {social.href ? (
                                    <a
                                        href={social.href}
                                        className="footer-social"
                                        {...(social.href.startsWith('http')
                                            ? { target: '_blank', rel: 'noopener noreferrer' }
                                            : {})}
                                    >
                                        {ICONS[social.label]}
                                        {social.label}
                                    </a>
                                ) : (
                                    <span className="footer-social disabled">
                                        {ICONS[social.label]}
                                        {social.label} · soon
                                    </span>
                                )}
                            </li>
                        ))}
                    </ul>
                </div>
            </div>
            <div className="footer-bottom">
                <span>© 2026 {ORG_NAME}, UBC Okanagan. Source on GitHub, licensed AGPL-3.0.</span>
            </div>
        </footer>
    )
}
