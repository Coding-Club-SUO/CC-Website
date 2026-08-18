import { BrowserRouter, Routes, Route } from 'react-router-dom'
import Navbar from './components/Navbar'
import HomePage from './pages/HomePage'
import ResourcesPage from './pages/ResourcesPage'
import { AuthPage } from './pages/AuthPage'
import { AuthProvider } from './context/AuthContext'

function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <Navbar />
        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/resources" element={<ResourcesPage />} />
          <Route path="/login" element={<AuthPage />} />
        </Routes>
      </AuthProvider>
    </BrowserRouter>
  )
}

export default App