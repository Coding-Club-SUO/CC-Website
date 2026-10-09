// Placeholder events. Replace with data from the backend / admin dashboard later.
// `date` is an ISO date (YYYY-MM-DD) in local campus time.

export type ClubEvent = {
  id: string
  title: string
  date: string
  startTime: string
  endTime: string
  location: string
  description: string
}

export const EVENTS: ClubEvent[] = [
  { id: '1', title: 'Welcome Back Social', date: '2026-09-10', startTime: '17:00', endTime: '19:00', location: 'Student Union Building', description: 'Meet the exec team and other students. Pizza provided.' },
  { id: '2', title: 'Intro to Git Workshop', date: '2026-09-24', startTime: '18:00', endTime: '19:30', location: 'SCI 200', description: 'Version control basics for beginners.' },
  { id: '3', title: 'Midterm Study Jam', date: '2026-10-08', startTime: '18:00', endTime: '21:00', location: 'Library Study Room', description: 'Group study session for COSC and MATH midterms.' },
  { id: '4', title: 'Resume & Co-op Night', date: '2026-10-15', startTime: '17:30', endTime: '19:00', location: 'ASC 130', description: 'Resume reviews and advice from upper-year students.' },
  { id: '5', title: 'Hackathon Kickoff', date: '2026-10-24', startTime: '09:00', endTime: '18:00', location: 'Innovation Precinct', description: 'A day of building with prizes. Teams of up to four.' },
  { id: '6', title: 'Alumni Panel', date: '2026-11-05', startTime: '18:00', endTime: '19:30', location: 'SCI 200', description: 'Graduates share their career paths.' },
  { id: '7', title: 'Game Night', date: '2026-11-19', startTime: '19:00', endTime: '22:00', location: 'Student Union Building', description: 'Board games, video games, and snacks.' },
  { id: '8', title: 'Final Exam Study Jam', date: '2026-12-03', startTime: '18:00', endTime: '22:00', location: 'Library Study Room', description: 'End-of-term study session.' },
]

// Placeholder archive entries (historical events).
export const ARCHIVE_EVENTS: ClubEvent[] = [
  { id: 'a1', title: 'Fall Kickoff Social', date: '2025-09-12', startTime: '17:00', endTime: '19:00', location: 'Student Union Building', description: 'Placeholder archive entry.' },
  { id: 'a2', title: 'Hackathon Weekend', date: '2025-10-25', startTime: '09:00', endTime: '18:00', location: 'Innovation Precinct', description: 'Placeholder archive entry.' },
  { id: 'a3', title: 'Winter Study Jam', date: '2025-12-04', startTime: '18:00', endTime: '22:00', location: 'Library Study Room', description: 'Placeholder archive entry.' },
  { id: 'a4', title: 'New Year Game Night', date: '2026-01-22', startTime: '19:00', endTime: '22:00', location: 'Student Union Building', description: 'Placeholder archive entry.' },
  { id: 'a5', title: 'Intro to Web Dev Workshop', date: '2026-02-19', startTime: '18:00', endTime: '19:30', location: 'SCI 200', description: 'Placeholder archive entry.' },
  { id: 'a6', title: 'Career Fair Prep', date: '2026-03-12', startTime: '17:30', endTime: '19:00', location: 'ASC 130', description: 'Placeholder archive entry.' },
  { id: 'a7', title: 'End of Year Social', date: '2026-05-21', startTime: '17:00', endTime: '20:00', location: 'Student Union Building', description: 'Placeholder archive entry.' },
  { id: 'a8', title: 'Summer Project Showcase', date: '2026-07-16', startTime: '18:00', endTime: '20:00', location: 'SCI 200', description: 'Placeholder archive entry.' },
]
