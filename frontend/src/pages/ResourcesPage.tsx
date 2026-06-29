import { useState } from 'react'; 
// This lets your page remember things while youre using it; such as filters

type Resource = {
    id: number
    title: string
    course: string
    uploader: string
    uploadedAt: string
    downloadUrl: string
}
/* Typescript type, blueprint describing what a resource object
    must look like. Will throw an error if the wrong data type 
    is passed. */
    const MOCK_RESOURCES: Resource[] = [
  {
    id: 1,
    title: 'Midterm 1 Practice Problems',
    course: 'COSC 121',
    uploader: 'Jane Doe',
    uploadedAt: '2026-06-18T14:30:00Z',
    downloadUrl: '/api/resources/1/download',
  },
  {
    id: 2,
    title: 'Final Exam Review Sheet',
    course: 'MATH 151',
    uploader: 'John Smith',
    uploadedAt: '2026-06-20T09:00:00Z',
    downloadUrl: '/api/resources/2/download',
  },
  {
    id: 3,
    title: 'Lab 3 Solutions',
    course: 'PHYS 101',
    uploader: 'Alice Chen',
    uploadedAt: '2026-06-22T16:45:00Z',
    downloadUrl: '/api/resources/3/download',
  },
]
const COURSES = ['All', 'COSC', 'MATH', 'PHYS', 'STAT']

export default function ResourcesPage() {
  const [selectedCourse, setSelectedCourse] = useState('All')
  // selectedCourse is a variable that stores the currently selected course filter. 
  // setSelectedCourse is a function that updates the value of selectedCourse. 
  // useState('All') initializes selectedCourse to 'All', meaning no filter is applied initially.
  const filtered = selectedCourse === 'All'
  //if the selected courses is 'All', show everything
  // otherwise only show resources matching the seleccted filter
    ? MOCK_RESOURCES
    : MOCK_RESOURCES.filter(r => r.course.startsWith(selectedCourse))

    return (
    <div style={{ maxWidth: '800px', margin: '2rem auto', padding: '0 1.5rem' }}>
      <h1>Course Resources</h1>

      <div style={{ display: 'flex', gap: '0.5rem', margin: '1.5rem 0' }}>
        {COURSES.map(course => (
          // javascript method that loops over a list and turns each item
          // into something, in this case, HTML elements.
          // COURSES.map turns the buttons like 'COSC', 'ALL', MATH'
          // into HTML buttons to filter the resources.
          <button
            key={course}
            // React needs a unique key on each item in a list so it knows
            //which one changed when the page updates.
            onClick={() => setSelectedCourse(course)}
            /* when we click a button, it calls the setSelectedCourse with the 
            course name, which updates selectedCourse, which reruns the filter.
            */
          >
            {course}
          </button>
        ))}
      </div>

      <div>
        {filtered.map(resource => (
          <div key={resource.id}>
            <p>{resource.course}</p>
            <h3>{resource.title}</h3>
            <p>Uploaded by {resource.uploader}</p>
            <a href={resource.downloadUrl}>Download</a>
          </div>
        ))}
      </div>
    </div>
  )
}