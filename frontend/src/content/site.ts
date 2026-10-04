// Placeholder copy. Shaped as plain data so it can later be swapped for
// content fetched from the CMS / admin dashboard without touching the views.

export const ORG_SHORT = 'CSCU'
export const ORG_NAME = 'Computer Science Course Union'

export const LOGO = { src: '/cscu_ok_logo.png', width: 299, height: 320 }

export const LINKS = {
  rubric: 'https://campus.hellorubric.com/?s=9287',
  discord: 'https://discord.com/invite/vFW5cSaBUm',
  instagram: 'https://www.instagram.com/cscu.ok',
  github: 'https://github.com/Coding-Club-SUO',
  linkedin: 'https://www.linkedin.com/company/cscu-ok/',
  projectMemberForm: 'https://forms.gle/opJrqzWkt9hFZjXF8',
}

export const EMAIL = 'cscu.okanagan@gmail.com'

export const TAGLINE =
  'A department-backed hub for Computer Science and Data Science students at UBC Okanagan, run by students.'

export const GET_INVOLVED = {
  title: 'Get Involved',
  intro: 'Membership is open to any UBC Okanagan student in Computer Science or Data Science.',
  options: [
    {
      kicker: 'Membership',
      title: 'Become a member',
      text: 'Join CSCU on Rubric to receive event updates and access member sales.',
      cta: { label: 'Join on Rubric', href: LINKS.rubric },
    },
    {
      kicker: 'Projects',
      title: 'Become a project member',
      text: 'CSCU regularly looks for students interested contributing to large-scale open-source projects that benefit the campus community. Projects may be led by project leads and supported by workshop coaches, so you can collaborate, learn, and build software together.',
      cta: { label: 'Apply here', href: LINKS.projectMemberForm },
    },
    {
      kicker: 'Coaches',
      title: 'Become a Workshop coach',
      text: 'This year, CSCU is hosting regular workshops across various areas of Computer Science and Data Science. We’re always looking for students interested in leading workshops, contributing to large-scale student-led open-source projects, or helping develop and maintain existing projects.',
      cta: { label: 'Apply here', href: LINKS.projectMemberForm },
    },
    {
      kicker: 'Executive team',
      title: 'Run for a role',
      text: 'CSCU holds executive elections each year. Watch the Discord and Instagram for nomination windows and election details.',
      cta: { label: 'Follow on Instagram', href: LINKS.instagram },
    },
  ],
  rolesTitle: 'Executive roles',
  rolesIntro: "CSCU's executive team currently includes:",
  roles: ['President', 'VP Finance', 'VP Events'],
  rolesNote:
    'Additional roles may be filled each term as needed. Detailed role descriptions are still being developed. See the Team page to meet the current executive members.',
  teamCta: { label: 'Meet the Team', href: '/about/team' },
}

export const TEAM = {
  title: 'Meet the Team',
  intro: `The students behind the ${ORG_SHORT} this year.`,
  // Placeholder section assignments; move members to their real sections.
  sections: [
    {
      title: 'Executive Officers',
      members: [
        { name: 'Soumil', role: 'President', bio: 'Placeholder bio text.', image: '/soumil.png' },
        { name: 'Noah', role: 'Vice President Events', bio: 'Placeholder bio text.', image: '/noah.png' },
        { name: 'Mohamed', role: 'Vice President Finance', bio: 'Placeholder bio text.', image: '/mohamed.png' },
      ],
    },
    {
      title: 'External',
      members: [
        { name: 'Kevin', role: 'External Relations Coordinator', bio: 'Placeholder bio text.', image: '/kevin.png' },
        {name: 'Moon', role: 'Head of Operations - External', bio: 'Placeholder bio text.', image: '/moon.png'}
      ],
    },
    {
      title: 'Internal',
      members: [
        { name: 'Mathijs', role: 'Head of Operations - Internal', bio: 'Placeholder bio text.', image: '/mathijs.png' },
        { name: 'Ethan', role: 'Internal Events Coordinator', bio: 'Placeholder bio text.', image: '/ethan.png' },
      ],
    },
    {
      title: 'Marketing & Student Relations',
      members: [
        { name: 'Gurkirin', role: 'Head of Communications & Marketing', bio: 'Placeholder bio text.', image: '/gurkirin.png' },
        { name: 'Saanjih', role: 'Social Media & Outreach Coordinator', bio: 'Placeholder bio text.', image: '/saanjih.png' },
        { name: 'Esme', role: 'Design Coordinator', bio: 'Placeholder bio text.', image: '/esme.png' },
      ],
    },
    {
      title: 'Workshop Coaches & Software Leads',
      members: [
        { name: 'Hakim', role: 'Web Development Workshop Coach & Project Lead', bio: 'Placeholder bio text.', image: '/hakim.JPG' },

      ],
    },
  ],
}

export const CONTRIBUTE = {
  title: 'Contribute',
  intro:
    'This website is open source. Anyone can fork the repository, open issues, and submit pull requests on GitHub.',
  perksTitle: 'Become a project member',
  perksIntro:
    'To be recognized on the website as a contributor, join as a project member. You don’t need to be a Computer Science student to make a difference. We welcome creative thinkers, designers, writers, testers, and organizers who want to help our projects grow. Whether you’re designing interfaces, reviewing code, managing projects, or writing documentation, there’s a place for you on our team. Project members get:',
  perks: [
    'Recognition as a contributor on this website',
    'A chance to win prizes and accolades for your contributions',
    'Recommendation letters',
    'LinkedIn endorsements',
  ],
  ctas: [
    { label: 'View on GitHub', href: LINKS.github },
    { label: 'Apply as a project member', href: LINKS.projectMemberForm },
  ],
  currentTitle: 'Current project members',
  pastTitle: 'Past project members',
  // Placeholder names; replace with real project members.
  current: ['Hakim Rashid'],
  past: ['Krish Khullar', 'Janhvi'],
}

export const ABOUT = {
  title: 'About CSCU',
  intro: `${ORG_SHORT} is the ${ORG_NAME} at UBC Okanagan: a department-backed hub for Computer Science and Data Science students, run by students.`,
  sections: [
    {
      title: 'History',
      paragraphs: [
        "For much of its history, CSCU has relied on year-to-year momentum. Each executive team's ability to keep the union active depended on students stepping up to lead and on how well each team delivered. That made CSCU resilient in strong years and quieter in others, with continuity never guaranteed.",
        "BC Hacks grew out of that same student energy. As CSCU's hackathon, it has been prominent in some years and dormant in others, depending on who was available to organize it.",
        "Coding Club developed alongside these efforts with a more continuous focus: building a coding community through regular events, and creating opportunities that can support students beyond any single executive team's term.",
      ],
    },
    {
      title: 'Where CSCU is headed',
      paragraphs: [
        'CSCU is being restructured as a department-backed initiative in partnership with the UBC Okanagan Department of Computer Science. The aim is a more sustainable, central hub for the program, rather than one that depends entirely on the momentum of individual executive teams. This work is still in progress.',
        'It brings together the shared goals behind CSCU, Coding Club, and related initiatives: one connected CS community, with more consistent opportunities for students. Depending on the year, that may include hackathons, coding events, workshops, collaborative projects, and other community activities.',
        'CSCU also aims to support the wider ecosystem of CS-related clubs, working alongside the department and other student groups rather than in isolation. See the homepage for more on how that fits together.',
      ],
    },
  ],
}
