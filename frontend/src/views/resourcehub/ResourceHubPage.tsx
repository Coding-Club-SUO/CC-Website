import './ResourceHubPage.css'
import Link from 'next/link'
import Image from 'next/image'
import { LOGO } from '../../content/site'

const FEATURES = [
  {
    title: 'Resource Repository',
    text: 'A high-performance repository for course resources.',
  },
  {
    title: 'Forums & Discussion',
    text: 'Course-specific forums and discussions to encourage peer support.',
  },
  {
    title: 'Clubs, Events & Research',
    text: 'Info on clubs & events, campus opportunities & advice, and professor research.',
  },
]

export default function ResourceHubPage() {
  return (
    <div className="home">
      <section className="hero">
        <Image src={LOGO.src} width={LOGO.width} height={LOGO.height} className="hero-logo" alt="CSCU logo" priority />
        <h1 className="hero-title">CSCU Resource Hub</h1>
        <p className="hero-code">// learn. share. build. together.</p>
        <p className="hero-sub">
          A central place for students to share and find course resources.
        </p>
        <Link href="/resources" className="hero-btn">Browse Resources →</Link>
      </section>

      <section className="features">
        {FEATURES.map(feature => (
          <div key={feature.title} className="feature-card">
            <h3 className="feature-title">{feature.title}</h3>
            <p className="feature-text">{feature.text}</p>
          </div>
        ))}
      </section>

      <section className="about">
        <h2 className="about-heading">About the CSCU Resource Hub</h2>
        <p className="about-text">
          The CSCU Resource Hub is a department-backed hub for students, run by students. 
          Where students share and find course resources, access information about professor 
          research opportunities, and much more! This part of the website is currently still 
          under active development.
        </p>
        <p className="about-text">
          <Link href="/about/contribute" className="about-link">Intrested in contributing? →</Link>
        </p>
      </section>
    </div>
  )
}