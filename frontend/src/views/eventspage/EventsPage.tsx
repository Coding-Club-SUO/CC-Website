"use client"

import { useState } from 'react'
import './EventsPage.css'
import type { ClubEvent } from '../../content/events'

const WEEKDAYS = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat']

const pad = (n: number) => String(n).padStart(2, '0')
const toKey = (y: number, m: number, d: number) => `${y}-${pad(m + 1)}-${pad(d)}`

// Parse YYYY-MM-DD as a local date (avoids UTC off-by-one)
function parseDate(key: string) {
  const [y, m, d] = key.split('-').map(Number)
  return new Date(y, m - 1, d)
}

function formatTime(t: string) {
  const [h, m] = t.split(':').map(Number)
  return `${h % 12 || 12}:${pad(m)} ${h < 12 ? 'AM' : 'PM'}`
}

function EventItem({ event, onSelect }: { event: ClubEvent; onSelect: (id: string) => void }) {
  const date = parseDate(event.date)
  return (
    <li>
      <button type="button" className="event-item" onClick={() => onSelect(event.id)}>
        <div className="event-date">
          <span className="event-date-month">{date.toLocaleString('en-US', { month: 'short' })}</span>
          <span className="event-date-day">{date.getDate()}</span>
        </div>
        <div className="event-info">
          <h3 className="event-title">{event.title}</h3>
          <p className="event-meta">
            {formatTime(event.startTime)} – {formatTime(event.endTime)} · {event.location}
          </p>
          <p className="event-desc">{event.description}</p>
        </div>
      </button>
    </li>
  )
}

function EventDetail({ event, onClose }: { event: ClubEvent; onClose: () => void }) {
  const date = parseDate(event.date)
  const showDesc = !event.description.startsWith('Placeholder')
  return (
    <div className="event-detail">
      <button type="button" className="event-detail-back" onClick={onClose}>← Back to list</button>
      <h2 className="event-detail-title">{event.title}</h2>
      <dl className="event-detail-facts">
        <dt>Date</dt>
        <dd>{date.toLocaleString('en-US', { weekday: 'long', month: 'long', day: 'numeric', year: 'numeric' })}</dd>
        <dt>Time</dt>
        <dd>{formatTime(event.startTime)} – {formatTime(event.endTime)}</dd>
        <dt>Location</dt>
        <dd>{event.location}</dd>
      </dl>
      {showDesc && <p className="event-detail-desc">{event.description}</p>}
    </div>
  )
}

type Season = 'winter' | 'summer' | 'fall'

// Calendar-based terms: Winter Jan–Apr, Summer May–Aug, Fall Sep–Dec.
const SEASONS: { id: Season; label: string; icon: string; range: string }[] = [
  { id: 'winter', label: 'Winter', icon: '❄️', range: 'Jan – Apr' },
  { id: 'summer', label: 'Summer', icon: '☀️', range: 'May – Aug' },
  { id: 'fall', label: 'Fall', icon: '🍂', range: 'Sep – Dec' },
]

const seasonOf = (key: string): Season => {
  const month = Number(key.split('-')[1])
  return month <= 4 ? 'winter' : month <= 8 ? 'summer' : 'fall'
}

function Archive({ events, onSelect }: { events: ClubEvent[]; onSelect: (id: string) => void }) {
  const years = [...new Set(events.map(e => e.date.slice(0, 4)))].sort().reverse()
  const [year, setYear] = useState(years[0] ?? '')
  const [season, setSeason] = useState<Season | null>(null)

  if (years.length === 0) return <p className="event-empty">No archived events yet.</p>

  const shown = events
    .filter(e => e.date.startsWith(year) && (!season || seasonOf(e.date) === season))
    .sort((a, b) => a.date.localeCompare(b.date) || a.startTime.localeCompare(b.startTime))

  const months = new Map<string, ClubEvent[]>()
  for (const e of shown) {
    const m = e.date.slice(0, 7)
    months.set(m, [...(months.get(m) ?? []), e])
  }

  const yearIndex = years.indexOf(year)

  return (
    <div className="archive">
      <div className="archive-year">
        <button type="button" className="calendar-nav" onClick={() => setYear(years[yearIndex + 1])} disabled={yearIndex >= years.length - 1} aria-label="Earlier year">‹</button>
        <span className="archive-year-label">{year}</span>
        <button type="button" className="calendar-nav" onClick={() => setYear(years[yearIndex - 1])} disabled={yearIndex <= 0} aria-label="Later year">›</button>
      </div>

      <div className="season-buttons" role="group" aria-label="Filter by season">
        {SEASONS.map(s => (
          <button
            key={s.id}
            type="button"
            aria-pressed={season === s.id}
            className={`season-btn${season === s.id ? ' active' : ''}`}
            onClick={() => setSeason(season === s.id ? null : s.id)}
          >
            <span aria-hidden="true">{s.icon}</span>
            <span className="season-name">{s.label}</span>
            <span className="season-range">{s.range}</span>
          </button>
        ))}
      </div>

      {shown.length === 0 ? (
        <p className="event-empty">No events archived for this selection.</p>
      ) : (
        <ol className="timeline">
          {[...months.entries()].map(([m, list]) => (
            <li key={m} className="timeline-month">
              <h3 className="timeline-month-label">
                {parseDate(`${m}-01`).toLocaleString('en-US', { month: 'long' })}
              </h3>
              <ul className="timeline-events">
                {list.map(e => (
                  <li key={e.id} className={`timeline-event season-${seasonOf(e.date)}`}>
                    <span className="timeline-dot" aria-hidden="true" />
                    <button type="button" className="timeline-card" onClick={() => onSelect(e.id)}>
                      <span className="timeline-date">
                        {parseDate(e.date).toLocaleString('en-US', { weekday: 'short', month: 'short', day: 'numeric' })}
                      </span>
                      <h4 className="event-title">{e.title}</h4>
                      <p className="event-meta">{formatTime(e.startTime)} – {formatTime(e.endTime)} · {e.location}</p>
                      {!e.description.startsWith('Placeholder') && <p className="event-desc">{e.description}</p>}
                    </button>
                  </li>
                ))}
              </ul>
            </li>
          ))}
        </ol>
      )}
    </div>
  )
}

export default function EventsPage({ events }: { events: ClubEvent[] }) {
  const now = new Date()
  const todayKey = toKey(now.getFullYear(), now.getMonth(), now.getDate())

  const [view, setView] = useState({ year: now.getFullYear(), month: now.getMonth() })
  const [tab, setTab] = useState<'upcoming' | 'archive'>('upcoming')
  const [selectedId, setSelectedId] = useState<string | null>(null)
  const selected = events.find(e => e.id === selectedId) ?? null

  const sorted = [...events].sort((a, b) => a.date.localeCompare(b.date) || a.startTime.localeCompare(b.startTime))
  const upcoming = sorted.filter(e => e.date >= todayKey)
  const past = sorted.filter(e => e.date < todayKey)
  const nextEvent = upcoming[0]

  const byDate = new Map<string, ClubEvent[]>()
  for (const e of sorted) byDate.set(e.date, [...(byDate.get(e.date) ?? []), e])

  const firstWeekday = new Date(view.year, view.month, 1).getDay()
  const daysInMonth = new Date(view.year, view.month + 1, 0).getDate()
  const cells: (number | null)[] = [
    ...Array(firstWeekday).fill(null),
    ...Array.from({ length: daysInMonth }, (_, i) => i + 1),
  ]

  const shiftMonth = (delta: number) => {
    const d = new Date(view.year, view.month + delta, 1)
    setView({ year: d.getFullYear(), month: d.getMonth() })
  }

  const monthLabel = new Date(view.year, view.month, 1).toLocaleString('en-US', { month: 'long', year: 'numeric' })

  return (
    <div className="events-page">
      <div className="events-hero">
        <h1 className="events-title">Events</h1>
        {nextEvent && (
          <p className="events-next">
            <span className="events-next-label">Next up</span>
            {nextEvent.title} · {parseDate(nextEvent.date).toLocaleString('en-US', { month: 'short', day: 'numeric' })}
          </p>
        )}
      </div>
      <p className="events-intro">Workshops, socials, and study sessions from the Computer Science Course Union.</p>

      <div className="events-layout">
        <section className="calendar" aria-label="Event calendar">
          <div className="calendar-header">
            <button type="button" className="calendar-nav" onClick={() => shiftMonth(-1)} aria-label="Previous month">‹</button>
            <div className="calendar-title">
              <h2 className="calendar-month">{monthLabel}</h2>
              <button type="button" className="calendar-today" onClick={() => setView({ year: now.getFullYear(), month: now.getMonth() })}>Today</button>
            </div>
            <button type="button" className="calendar-nav" onClick={() => shiftMonth(1)} aria-label="Next month">›</button>
          </div>
          <div className="calendar-grid">
            {WEEKDAYS.map(d => <div key={d} className="calendar-weekday">{d}</div>)}
            {cells.map((day, i) => {
              if (day === null) return <div key={`blank-${i}`} className="calendar-cell blank" />
              const key = toKey(view.year, view.month, day)
              const dayEvents = byDate.get(key) ?? []
              return (
                <div key={key} className={`calendar-cell${key === todayKey ? ' today' : ''}`}>
                  <span className="calendar-day">{day}</span>
                  {dayEvents.map(e => (
                    <button
                      key={e.id}
                      type="button"
                      className={`calendar-event${key < todayKey ? ' past' : ''}${e.id === selectedId ? ' selected' : ''}`}
                      title={e.title}
                      onClick={() => setSelectedId(e.id)}
                    >
                      {e.title}
                    </button>
                  ))}
                </div>
              )
            })}
          </div>
        </section>

        <section className="event-list" aria-label="Event list">
          {!selected && (
            <div className="event-tabs" role="tablist">
              <button type="button" role="tab" aria-selected={tab === 'upcoming'} className={`event-tab${tab === 'upcoming' ? ' active' : ''}`} onClick={() => setTab('upcoming')}>
                Upcoming ({upcoming.length})
              </button>
              <button type="button" role="tab" aria-selected={tab === 'archive'} className={`event-tab${tab === 'archive' ? ' active' : ''}`} onClick={() => setTab('archive')}>
                Archive ({past.length})
              </button>
            </div>
          )}
          <div className="event-scroll">
          {selected ? (
            <EventDetail event={selected} onClose={() => setSelectedId(null)} />
          ) : tab === 'archive' ? (
            <Archive events={past} onSelect={setSelectedId} />
          ) : upcoming.length === 0 ? (
            <p className="event-empty">
              No upcoming events right now. New events are announced on our Discord and Instagram, so check back soon!
            </p>
          ) : (
            <ul className="event-items">
              {upcoming.map(e => <EventItem key={e.id} event={e} onSelect={setSelectedId} />)}
            </ul>
          )}
          </div>
        </section>
      </div>
    </div>
  )
}
