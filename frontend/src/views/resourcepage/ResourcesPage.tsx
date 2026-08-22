"use client"

import './ResourcesPage.css'
import { useState } from 'react'
import type { Resource } from '../../api/resources'

const COURSES = ['All', 'COSC', 'MATH', 'PHYS', 'STAT']

export default function ResourcesPage({ resources }: { resources: Resource[] }) {
  const [selectedCourse, setSelectedCourse] = useState('All')

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

      {filtered.length === 0 ? (
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