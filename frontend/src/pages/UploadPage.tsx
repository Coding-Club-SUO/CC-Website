import {useState } from 'react'
import {useNavigate} from 'react-router-dom'
import { uploadResource } from '../api/resources'
import './UploadPage.css'

const MAX_SIZE_MB = 10
const ALLOWED_EXTENSIONS = ['pdf', 'docx', 'pptx']

export default function UploadPage() {
  const navigate = useNavigate()
  const [title, setTitle] = useState('')
  const [course, setCourse] = useState('')
  const [file, setFile] = useState<File | null>(null)
  const [error, setError] = useState<string | null>(null)
  const [submitting, setSubmitting] = useState(false)

  function validate(): string | null {
    if (!title.trim()) return 'Please enter a title.'
    if (!course.trim()) return 'Please enter a course.'
    if (!file) return 'Please choose a file to upload.'
    const ext = file.name.split('.').pop()?.toLowerCase() ?? ''
   if (!ALLOWED_EXTENSIONS.includes(ext)) {
      return `That file type isn't allowed. Accepted: ${ALLOWED_EXTENSIONS.join(', ')}.`
    }
    if (file.size > MAX_SIZE_MB * 1024 * 1024) {
      return `File is too large (max ${MAX_SIZE_MB} MB).`
    }
    return null
  }

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault()
    const validationError = validate()
    if (validationError) {
      setError(validationError)
      return
    }
    setError(null)
    setSubmitting(true)
    try {
      await uploadResource({ title, course, file: file! })
      navigate('/resources')
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Upload failed. Please try again.')
    } finally {
      setSubmitting(false)
    }
  }

  return (
    <div className="upload-page">
      <h1>Upload a Resource</h1>
      <p className="upload-subtitle">
        Share a past exam, worksheet, or study material with your course.
      </p>

      <form className="upload-form" onSubmit={handleSubmit}>
        <label className="field">
          <span className="field-label">Title</span>
          <input
            type="text"
            value={title}
            onChange={e => setTitle(e.target.value)}
            placeholder="e.g. Midterm 1 Practice Problems"
          />
        </label>

        <label className="field">
          <span className="field-label">Course</span>
          <input
            type="text"
            value={course}
            onChange={e => setCourse(e.target.value)}
            placeholder="e.g. COSC 121"
          />
        </label>

        <label className="field">
          <span className="field-label">File</span>
          <input
            type="file"
            accept=".pdf,s.docx,.pptx"
            onChange={e => setFile(e.target.files?.[0] ?? null)}
          />
          <span className="field-hint">
            Accepted: PDF, DOCX, PPTX · max {MAX_SIZE_MB} MB
          </span>
        </label>

        {file && <p className="file-selected">Selected: {file.name}</p>}
        {error && <p className="form-error">{error}</p>}

        <button type="submit" className="submit-btn" disabled={submitting}>
          {submitting ? 'Uploading…' : 'Upload Resource'}
        </button>
      </form>
    </div>
  )
}

