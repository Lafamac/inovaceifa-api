import { NavLink, useLocation } from 'react-router-dom'
import '../styles/tabs.css'

export default function ModuleTabs({ tabs }) {
    const location = useLocation()

    return (
        <div className="module-tabs-container">
            {tabs.map(tab => {
                const isActive = location.pathname.startsWith(tab.path)
                return (
                    <NavLink
                        key={tab.path}
                        to={tab.path}
                        className={`module-tab ${isActive ? 'active' : ''}`}
                    >
                        {tab.icon && <tab.icon size={16} className="tab-icon" />}
                        {tab.label}
                    </NavLink>
                )
            })}
        </div>
    )
}
