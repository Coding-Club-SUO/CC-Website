import { BrowserRouter, Routes, Route } from 'react-router-dom'
import ResourcesPage from './pages/ResourcesPage'

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/resources" element={<ResourcesPage />} />
      </Routes>
    </BrowserRouter>
  )
}

export default App