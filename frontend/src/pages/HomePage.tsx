import { Link } from 'react-router-dom'
import './HomePage.css'
import logo from '../assets/codingclub_ok_logo.jpeg'

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

export default function HomePage() {
  return (
    <div className="home">
      <section className="hero">
        <img src={logo} className="hero-logo" alt="Coding Club logo" />
        <h1 className="hero-title">Coding Club Resource Hub</h1>
        <p className="hero-code">// learn. share. build. together.</p>
        <p className="hero-sub">
          A central place for COSC, MATH, PHYS, and STAT students to share and find
          course resources.
        </p>
        <Link to="/resources" className="hero-btn">Browse Resources →</Link>
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
    </div>
  )
}