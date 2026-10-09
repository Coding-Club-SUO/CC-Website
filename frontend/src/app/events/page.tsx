import { Suspense } from 'react'
import EventsPage from '../../views/eventspage/EventsPage'
import { EVENTS, ARCHIVE_EVENTS } from '../../content/events'

export default function Events() {
  // TODO: replace with data fetched from the backend
  // Suspense: the page reads the current date on the client, so it streams in.
  return (
    <Suspense fallback={null}>
      <EventsPage events={[...ARCHIVE_EVENTS, ...EVENTS]} />
    </Suspense>
  )
}
