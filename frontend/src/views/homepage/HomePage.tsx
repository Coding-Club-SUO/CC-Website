import './HomePage.css'
import Link from 'next/link'
import Image from 'next/image'
import logo from '../../public/codingclub_ok_logo.jpeg'
import InstagramEmbed from '../../components/InstagramEmbed'

const FEATURES = [
  {
    title: 'Resource Repository',
    text: 'A high-performance repository for CS, Math, Physics, and Stats resources.',
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
const SOCIALS = [
  { label: 'Linktree', 
    url: 'https://linktr.ee/codingclub.suo',
    icon: '🔗'
  },
  { label: 'Instagram',
    url: 'https://www.instagram.com/codingclub.ok/',
    icon: '📸'
  },
  {label: 'Campus Portal',
    url: 'https://campus.hellorubric.com/?s=7801',
    icon: '🎓'
  },
  { label: 'Sign-Up Form', 
    url: null,
    icon: '📝'
  },
  { label: 'Discord Server',
    url: 'https://discord.gg/HWXnhqsxe',
    icon: '💬'
  }
]
export default function HomePage() {
  return (
    <div className="home">
      <section className="hero">
        <Image src={logo} className="hero-logo" alt="Coding Club logo" priority />
        <h1 className="hero-title">Coding Club Resource Hub</h1>
        <p className="hero-code">// learn. share. build. together.</p>
        <p className="hero-sub">
          A central place for COSC, MATH, PHYS, and STAT students to share and find
          course resources.
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
        <h2 className="about-heading">About the Coding Club</h2>
        <p className="about-text">
          The Coding Club at the University of British Columbia Okanagan Campus is a
          student-led organization that fosters a supportive and collaborative community
          for anyone passionate about coding — whether you're a beginner or an experienced
          developer. We host workshops, coding challenges, hackathons, and networking
          events to help members build technical skills, solve real-world problems, and
          explore different areas of technology.
        </p>
        <p className="about-text">
          We welcome students from all disciplines. Join us to collaborate, learn, and grow
          your coding expertise while connecting with like-minded peers.
        </p>
      </section>
            <section className="highlight">
        <h2 className="highlight-heading">From Our Community</h2>
        <InstagramEmbed />
      </section>
      
            <section className="socials">
        <h2 className="socials-heading">Connect With Us</h2>
        <div className="socials-grid">
          {SOCIALS.map(social =>
            social.url ? (
              <a
                key={social.label}
                href={social.url}
                target="_blank"
                rel="noopener noreferrer"
                className="social-link"
              >
                <span className="social-icon">{social.icon}</span>
                {social.label}
              </a>
            ) : (
              <span key={social.label} className="social-link disabled">
                <span className="social-icon">{social.icon}</span>
                {social.label} · soon
              </span>
            )
          )}
        </div>
      </section>
    </div>
  )
}