import { useState, useEffect } from 'react'
import { getResources, type Resource } from '../api/resources'
import './ResourcesPage.css'

const COURSES = ['All', 'COSC', 'MATH', 'PHYS', 'STAT']

// TEMPORARY sample data so we can build the UI while the backend is down.
// To preview the "no resources" message, change the .catch below to setResources([]).
const SAMPLE_RESOURCES: Resource[] = [
  { id: 1, title: 'Midterm 1 Practice Problems', course: 'COSC 121', uploader: 'Jane Doe', uploadedAt: '2026-06-18T14:30:00Z', downloadUrl: '#' },
  { id: 2, title: 'Final Exam Review Sheet', course: 'MATH 151', uploader: 'John Smith', uploadedAt: '2026-06-20T09:00:00Z', downloadUrl: '#' },
  { id: 3, title: 'Lab 3 Solutions', course: 'PHYS 101', uploader: 'Alice Chen', uploadedAt: '2026-06-22T16:45:00Z', downloadUrl: '#' },
  { id: 4, title: 'Regression Cheat Sheet', course: 'STAT 260', uploader: 'Sam Lee', uploadedAt: '2026-06-24T11:15:00Z', downloadUrl: '#' },
]

export default function ResourcesPage() {
  const [resources, setResources] = useState<Resource[]>([])
  const [loading, setLoading] = useState(true)
  const [selectedCourse, setSelectedCourse] = useState('All')

  useEffect(() => {
    getResources()
      .then(data => setResources(data))
      .catch(() => setResources(SAMPLE_RESOURCES)) // backend down → samples for now
      .finally(() => setLoading(false))
  }, [])

  const filtered = selectedCourse === 'All'
    ? resources
    : resources.filter(r => r.course.startsWith(selectedCourse))

  return (
    <div className="resources-page">
      <h1>Course Resources</h1>
      <p className="resources-subtitle">
        Past exams, worksheets, and study materials shared by students.
      </p>

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

      {loading ? (
        <p className="state-msg">Loading resources…</p>
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
              <a href={resource.downloadUrl} className="download-btn">Download</a>
            </div>
          ))}
        </div>
      )}
    </div>
  )
}