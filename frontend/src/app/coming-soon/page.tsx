import Link from 'next/link'
import './coming-soon.css'

export const metadata = { title: 'Coming Soon' }

export default function ComingSoon() {
  return (
    <div className="coming-soon">
      <h1 className="coming-soon-title">Coming Soon</h1>
      <p className="coming-soon-text">
        This part of the site isn&apos;t available yet. Check back soon!
      </p>
      <Link href="/" className="coming-soon-link">Back to home</Link>
    </div>
  )
}
