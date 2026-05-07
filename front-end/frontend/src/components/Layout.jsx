import { useAuth } from '../auth/AuthContext'
import { useLocation, Link } from 'react-router-dom'
import { useState, useRef, useEffect } from 'react'
import { AnimatePresence, motion } from 'framer-motion'
import '../styles/layout.css'

export default function Layout({ children }) {
    const { user, logout } = useAuth()
    const location = useLocation()
    const [open, setOpen] = useState(false)
    const menuRef = useRef(null)

    useEffect(() => {
        function handleClickOutside(event) {
            if (menuRef.current && !menuRef.current.contains(event.target)) {
                setOpen(false)
            }
        }
        document.addEventListener('mousedown', handleClickOutside)
        return () => document.removeEventListener('mousedown', handleClickOutside)
    }, [])

    return (
        <div className="app-container">

            <header className="header">
                <strong>CEIFA</strong>

                <div className="header-right" ref={menuRef}>
                    <span
                        className="user-name"
                        onClick={() => setOpen(!open)}
                    >
                        {user?.email} ▾
                    </span>

                    {open && (
                        <div className="user-menu">
                            <Link
                                to="/auth/trocar-senha"
                                className="user-menu-item"
                                onClick={() => setOpen(false)}
                            >
                                Trocar senha
                            </Link>

                            <button
                                onClick={logout}
                                className="user-menu-item"
                            >
                                Sair
                            </button>
                        </div>
                    )}
                </div>
            </header>

            <AnimatePresence mode="wait">
                <motion.main
                    key={location.pathname}
                    className="page-content"
                    initial={{ opacity: 0, y: 12 }}
                    animate={{ opacity: 1, y: 0 }}
                    exit={{ opacity: 0, y: -12 }}
                    transition={{ duration: 0.25 }}
                >
                    {children}
                </motion.main>
            </AnimatePresence>

            <footer>
                <div>Versão 1.0.0</div>
                <div>Copyright © CEIFA</div>
                <div>{new Date().toLocaleDateString()}</div>
            </footer>
        </div>
    )
}