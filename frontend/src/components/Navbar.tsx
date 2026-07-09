import { NavLink } from 'react-router-dom'
import './Navbar.css'

export default function Navbar() {
    return (
        <nav className="navbar">
            <NavLink to="/" className="nav-brand">
            Coding Club <span className="nav-brand-accent">Resource Hub</span>
                </NavLink>
                <div className="nav-links">
                    <NavLink to="/" className="nav-link">Home</NavLink>
                    <NavLink to="/resources" className="nav-link">Resources</NavLink>
                 </div>
        </nav>
    )
}