import './AboutPage.css'
import Link from 'next/link'
import Image from 'next/image'
import { GET_INVOLVED, TEAM, ABOUT, CONTRIBUTE } from '../../content/site'

function ContributorIcon({ name }: { name: string }) {
  const initials = name
    .split(' ')
    .map(part => part[0])
    .join('')
    .slice(0, 2)
    .toUpperCase()
  return (
    <div className="team-member">
      <div className="team-avatar team-avatar-initials">{initials}</div>
      <h3 className="team-member-name">{name}</h3>
    </div>
  )
}

export function GetInvolvedPage() {
  return (
    <div className="about-page">
      <h1 className="about-page-title">{GET_INVOLVED.title}</h1>
      <p className="about-page-intro">{GET_INVOLVED.intro}</p>
      <div className="about-grid">
        {GET_INVOLVED.options.map(option => (
          <div key={option.title} className="about-card about-card-action">
            <p className="about-card-kicker">{option.kicker}</p>
            <h3 className="about-card-title">{option.title}</h3>
            <p className="about-card-text">{option.text}</p>
            <a
              href={option.cta.href}
              className="about-btn"
              target="_blank"
              rel="noopener noreferrer"
            >
              {option.cta.label}
            </a>
          </div>
        ))}
      </div>
      <div className="about-card about-roles">
        <h2 className="about-card-title">{GET_INVOLVED.rolesTitle}</h2>
        <p className="about-card-text">{GET_INVOLVED.rolesIntro}</p>
        <ul className="about-roles-list">
          {GET_INVOLVED.roles.map(role => (
            <li key={role}>{role}</li>
          ))}
        </ul>
        <p className="about-card-text">{GET_INVOLVED.rolesNote}</p>
        <Link href={GET_INVOLVED.teamCta.href} className="about-btn about-btn-ghost">
          {GET_INVOLVED.teamCta.label}
        </Link>
      </div>
    </div>
  )
}

export function TeamPage() {
  return (
    <div className="about-page">
      <h1 className="about-page-title">{TEAM.title}</h1>
      <p className="about-page-intro">{TEAM.intro}</p>
      {TEAM.sections.map(section => (
        <section key={section.title} className="team-section">
          <h2 className="team-section-title">{section.title}</h2>
          <div className="team-grid">
            {section.members.map(member => (
              <div key={member.name} className="team-member">
                <Image
                  src={member.image}
                  alt={member.name}
                  width={160}
                  height={160}
                  className="team-avatar"
                />
                <h3 className="team-member-name">{member.name}</h3>
                <p className="about-card-role">{member.role}</p>
                <p className="about-card-text">{member.bio}</p>
              </div>
            ))}
          </div>
        </section>
      ))}
    </div>
  )
}

export function VisionPage() {
  return (
    <div className="about-page">
      <h1 className="about-page-title">{ABOUT.title}</h1>
      <p className="about-page-intro">{ABOUT.intro}</p>
      <div className="about-stack">
        {ABOUT.sections.map(section => (
          <div key={section.title} className="about-card">
            <h2 className="about-card-title">{section.title}</h2>
            {section.paragraphs.map(text => (
              <p key={text} className="about-card-text about-card-para">{text}</p>
            ))}
          </div>
        ))}
      </div>
    </div>
  )
}

export function ContributePage() {
  return (
    <div className="about-page">
      <h1 className="about-page-title">{CONTRIBUTE.title}</h1>
      <p className="about-page-intro">{CONTRIBUTE.intro}</p>
      <div className="about-stack">
        <div className="about-card">
          <h2 className="about-card-title">{CONTRIBUTE.perksTitle}</h2>
          <p className="about-card-text">{CONTRIBUTE.perksIntro}</p>
          <ul className="about-roles-list">
            {CONTRIBUTE.perks.map(perk => (
              <li key={perk}>{perk}</li>
            ))}
          </ul>
          <div className="about-cta-row">
            {CONTRIBUTE.ctas.map(cta => (
              <a
                key={cta.label}
                href={cta.href}
                className="about-btn"
                target="_blank"
                rel="noopener noreferrer"
              >
                {cta.label}
              </a>
            ))}
          </div>
        </div>
        <section className="team-section">
          <h2 className="team-section-title">{CONTRIBUTE.currentTitle}</h2>
          <div className="team-grid">
            {CONTRIBUTE.current.map(name => (
              <ContributorIcon key={name} name={name} />
            ))}
          </div>
        </section>
        <section className="team-section">
          <h2 className="team-section-title">{CONTRIBUTE.pastTitle}</h2>
          <div className="team-grid">
            {CONTRIBUTE.past.map(name => (
              <ContributorIcon key={name} name={name} />
            ))}
          </div>
        </section>
      </div>
    </div>
  )
}
