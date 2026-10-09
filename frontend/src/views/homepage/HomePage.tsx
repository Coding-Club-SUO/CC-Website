import './HomePage.css'
import Link from 'next/link'
import Image from 'next/image'
import Reveal from './components/Reveal'
import { LOGO, LINKS, TAGLINE, GET_INVOLVED } from '../../content/site'

const ACTIVITIES = [
  { title: 'Hackathons', text: "Including BC Hacks, CSCU's hackathon, when students are available to run it." },
  { title: 'Coding events', text: 'Regular events from Coding Club that build a coding community.' },
  { title: 'Workshops', text: 'Hands-on sessions, depending on the year and who is running them.' },
  { title: 'Projects', text: 'Collaborative and open-source projects led by students.' },
]

export default function HomePage() {
  return (
    <div className="club">
      <section className="club-hero">
        <div className="club-orb club-orb-a" aria-hidden="true" />
        <div className="club-orb club-orb-b" aria-hidden="true" />
        <div className="club-grid-bg" aria-hidden="true" />
        <Image
          src={LOGO.src}
          width={LOGO.width}
          height={LOGO.height}
          className="club-logo intro intro-1"
          alt="CSCU logo"
          priority
        />
        <p className="club-eyebrow intro intro-2">UBC Okanagan</p>
        <h1 className="club-title intro intro-3">
          Computer Science <span className="club-title-accent">Course Union</span>
        </h1>
        <p className="club-lead intro intro-4">{TAGLINE}</p>
        <div className="club-cta-row intro intro-5">
          <a href={LINKS.rubric} className="club-btn club-btn-primary" target="_blank" rel="noopener noreferrer">
            Join on Rubric
          </a>
          <Link href="/about/get-involved" className="club-btn">Get Involved</Link>
          <Link href="/events" className="club-btn">Events</Link>
          <Link href="/resourcehub" className="club-btn">Resource Hub</Link>
        </div>
      </section>

      <section className="club-section">
        <Reveal>
          <h2 className="club-heading">What we do</h2>
          <p className="club-sub">
            Depending on the year, CSCU brings students together through a mix of community activities.
          </p>
        </Reveal>
        <div className="club-cards">
          {ACTIVITIES.map((item, i) => (
            <Reveal key={item.title} delay={i * 80}>
              <div className="club-card">
                <h3 className="club-card-title">{item.title}</h3>
                <p className="club-card-text">{item.text}</p>
              </div>
            </Reveal>
          ))}
        </div>
      </section>

      <section className="club-section">
        <Reveal>
          <div className="club-panel">
            <h2 className="club-heading">Past and future</h2>
            <p className="club-text">
              CSCU has run on year-to-year momentum: resilient in strong years, quieter in others.
              BC Hacks and Coding Club grew out of that same student energy.
            </p>
            <p className="club-text">
              CSCU is now being restructured, in partnership with the UBC Okanagan Department of
              Computer Science, into a more sustainable hub for the program. That work is still in progress.
            </p>
            <Link href="/about/vision" className="club-link">Read the full story →</Link>
          </div>
        </Reveal>
      </section>

      <section className="club-section" id="get-involved">
        <Reveal>
          <h2 className="club-heading">Get involved</h2>
          <p className="club-sub">{GET_INVOLVED.intro}</p>
        </Reveal>
        <div className="club-cards club-cards-3">
          {GET_INVOLVED.options.map((option, i) => (
            <Reveal key={option.title} delay={i * 80}>
              <div className="club-card">
                <p className="club-kicker">{option.kicker}</p>
                <h3 className="club-card-title">{option.title}</h3>
                <a href={option.cta.href} className="club-link" target="_blank" rel="noopener noreferrer">
                  {option.cta.label} →
                </a>
              </div>
            </Reveal>
          ))}
        </div>
        <Reveal>
          <p className="club-note">
            <Link href="/about/get-involved" className="club-link">See details, including executive roles →</Link>
          </p>
        </Reveal>
      </section>

      <section className="club-section" id="community">
        <Reveal>
          <div className="club-panel">
            <h2 className="club-heading">The wider CS community</h2>
            <p className="club-text">
              CSCU aims to support the wider ecosystem of CS-related clubs, working alongside the
              department and other student groups rather than in isolation. Club details are still being
              compiled here; for now, the Resource Hub has info on clubs and events, and the Discord is
              the best place to ask.
            </p>
            <div className="club-cta-row club-cta-left">
              <Link href="/resourcehub" className="club-btn club-btn-primary">Visit the Resource Hub</Link>
              <a href={LINKS.discord} className="club-btn" target="_blank" rel="noopener noreferrer">
                Join the Discord
              </a>
            </div>
          </div>
        </Reveal>
      </section>
    </div>
  )
}
