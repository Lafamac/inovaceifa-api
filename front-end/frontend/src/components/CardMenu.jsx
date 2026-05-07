import { useState, useRef, useEffect } from 'react'
import '../styles/cardMenu.css'

export default function CardMenu({ onEdit, onDelete }) {
    const [open, setOpen] = useState(false)
    const menuRef = useRef()

    function toggleMenu(e) {
        e.stopPropagation()
        setOpen(!open)
    }

    useEffect(() => {
        function handleClickOutside(event) {
            if (menuRef.current && !menuRef.current.contains(event.target)) {
                setOpen(false)
            }
        }

        document.addEventListener('mousedown', handleClickOutside)
        return () => {
            document.removeEventListener('mousedown', handleClickOutside)
        }
    }, [])

    return (
        <div className="card-menu" ref={menuRef}>
            <button className="menu-button" onClick={toggleMenu}>
                ⋮
            </button>

            {open && (
                <div className="menu-dropdown">
                    <button onClick={onEdit}>Editar</button>
                    <button className="danger" onClick={onDelete}>
                        Excluir
                    </button>
                </div>
            )}
        </div>
    )
}
