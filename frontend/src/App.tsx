import { BrowserRouter, Routes, Route } from 'react-router-dom'
import Navbar from './components/Navbar'
import HomePage from './pages/HomePage'
import ResourcesPage from './pages/ResourcesPage'
import UploadPage from './pages/UploadPage'

function App() {
  return (
    <BrowserRouter>
      <Navbar />
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/resources" element={<ResourcesPage />} />
        <Route path="/resources/new" element={<UploadPage />} />
      </Routes>
    </BrowserRouter>
  )
}

export default App