"use client"

import './ResourcesPage.css'
import { useEffect, useState } from 'react'
import type { Resource } from '../../api/resources/resources'

const COURSES = ['All', 'COSC', 'MATH', 'PHYS', 'STAT']

export default function ResourcesPage({ resources }: { resources: Resource[] }) {
  const [selectedCourse, setSelectedCourse] = useState('All')
  const [searchQuery, setSearchQuery] = useState('')
  const [availableResources, setAvailableResources] = useState(resources)
  const [loadState, setLoadState] = useState<'checking' | 'ready' | 'error'>(
    resources.length === 0 ? 'checking' : 'ready'
  )

  useEffect(() => {
    if (resources.length > 0) return

    let cancelled = false

    async function verifyEmptyResources() {
      try {
        const response = await fetch('/api/resources')
        if (!response.ok) throw new Error('Resources request failed')

        const data: unknown = await response.json()
        if (!Array.isArray(data)) throw new Error('Invalid resources response')

        if (!cancelled) {
          setAvailableResources(data as Resource[])
          setLoadState('ready')
        }
      } catch {
        if (!cancelled) setLoadState('error')
      }
    }

    void verifyEmptyResources()

    return () => {
      cancelled = true
    }
  }, [resources])

  const normalizedQuery = searchQuery.trim().toLowerCase()
  const filtered = availableResources.filter(resource => {
    const matchesCourse = selectedCourse === 'All' || resource.course.startsWith(selectedCourse)
    const matchesSearch = !normalizedQuery ||
      resource.title.toLowerCase().includes(normalizedQuery) ||
      resource.course.toLowerCase().includes(normalizedQuery)

    return matchesCourse && matchesSearch
  })

  return (
    <div className="resources-page">
      <h1>Course Resources</h1>
      <p className="resources-subtitle">
        Past exams, worksheets, and study materials shared by students.
      </p>

      <div className="resources-controls">
        <label className="visually-hidden" htmlFor="resource-search">
          Search resources
        </label>
        <input
          id="resource-search"
          className="resource-search"
          type="search"
          placeholder="Search resources..."
          value={searchQuery}
          onChange={event => setSearchQuery(event.target.value)}
        />

        <div className="filter-bar">
          {COURSES.map(course => (
            <button
              key={course}
              className={selectedCourse === course ? 'filter-btn active' : 'filter-btn'}
              onClick={() => setSelectedCourse(course)}
            >
              {course}
            </button>
          ))}
        </div>
      </div>

      {loadState === 'checking' ? (
        <div className="state-msg" role="status">Loading resources...</div>
      ) : loadState === 'error' ? (
        <div className="empty-state" role="alert">
          <p className="empty-title">Unable to load resources</p>
          <p className="empty-sub">Please try again later.</p>
        </div>
      ) : availableResources.length === 0 ? (
        <div className="empty-state">
          <p className="empty-title">No resources uploaded yet</p>
          <p className="empty-sub">Be the first to share something for this course.</p>
        </div>
      ) : filtered.length === 0 && normalizedQuery ? (
        <div className="empty-state">
          <p className="empty-title">No results for &quot;{searchQuery.trim()}&quot;</p>
        </div>
      ) : filtered.length === 0 ? (
        <div className="empty-state">
          <p className="empty-title">No resources uploaded yet</p>
          <p className="empty-sub">Be the first to share something for this course.</p>
        </div>
      ) : (
        <div className="resource-list">
          {filtered.map(resource => (
            <div key={resource.id} className="resource-card">
              <div>
                <span className="course-tag">{resource.course}</span>
                <h3 className="resource-title">{resource.title}</h3>
                <p className="resource-meta">
                  Uploaded by {resource.uploader} · {new Date(resource.uploadedAt).toLocaleDateString()}
                </p>
              </div>
              {resource.downloadUrl && resource.downloadUrl !== '#' ? (
                <a
                  href={resource.downloadUrl}
                  className="download-btn"
                  target="_blank"
                  rel="noopener noreferrer"
                >
                  Download
                </a>
              ) : (
                <span className="download-btn disabled">Unavailable</span>
              )}
            </div>
          ))}
        </div>
      )}
    </div>
  )
}