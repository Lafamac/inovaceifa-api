import { useState, useEffect, useRef } from 'react'
import { Link, useLocation } from 'react-router-dom'
import { Menu, Tractor, Map, ClipboardList, BarChart3, Package, ArrowLeftRight, ShoppingCart, Users, UserCog, DollarSign } from 'lucide-react'
import { useAuth } from '../auth/AuthContext'
import Sidebar from './Sidebar'
import ModuleTabs from './ModuleTabs'
import '../styles/layout.css'

export default function OperationLayout({ children }) {
    const { user, logout } = useAuth()

    const [collapsed, setCollapsed] = useState(() => localStorage.getItem('sidebarCollapsed') === 'true')
    const [mobileOpen, setMobileOpen] = useState(false)
    const [darkMode, setDarkMode] = useState(() => localStorage.getItem('darkMode') === 'true')
    const [openMenu, setOpenMenu] = useState(false)
    const location = useLocation()
    const path = location.pathname

    let moduleTabs = null

    if (path.startsWith('/ordens-servico') || path.startsWith('/talhoes') || path.startsWith('/safra-talhoes') || path.startsWith('/maquinas') || path.startsWith('/planejamento')) {
        moduleTabs = [
            { path: '/ordens-servico', label: 'Ordens de Serviço', icon: ClipboardList },
            { path: '/talhoes', label: 'Talhões', icon: Map },
            { path: '/safra-talhoes', label: 'Safra Talhões', icon: Map },
            { path: '/maquinas', label: 'Máquinas', icon: Tractor },
            { path: '/planejamento/adubacao', label: 'Adubação', icon: BarChart3 },
        ]
    } else if (path.startsWith('/produtos') || path.startsWith('/pedidos-compra')) {
        moduleTabs = [
            { path: '/produtos', label: 'Produtos', icon: Package },
            { path: '/produtos/movimentar', label: 'Movimentação', icon: ArrowLeftRight },
            { path: '/pedidos-compra', label: 'Compras', icon: ShoppingCart },
        ]
    } else if (path.startsWith('/funcionarios') || path.startsWith('/terceirizados') || path.startsWith('/turmas') || path.startsWith('/folha')) {
        moduleTabs = [
            { path: '/funcionarios', label: 'Funcionários', icon: Users },
            { path: '/terceirizados', label: 'Terceirizados', icon: UserCog },
            { path: '/turmas-terceirizadas', label: 'Turmas', icon: Users },
            { path: '/folha', label: 'Folha Pagto.', icon: DollarSign },
        ]
    } else if (path.startsWith('/relatorio') || path.startsWith('/financeiro') || path.startsWith('/contas') || path.startsWith('/fluxo')) {
        moduleTabs = [
            { path: '/relatorio', label: 'Relatório', icon: BarChart3 },
            { path: '/contas-pagar', label: 'Contas a Pagar', icon: DollarSign },
            { path: '/contas-receber', label: 'Contas a Receber', icon: DollarSign },
            { path: '/fluxo-caixa', label: 'Fluxo de Caixa', icon: DollarSign },
        ]
    }

    const menuRef = useRef(null)

    useEffect(() => {
        localStorage.setItem('sidebarCollapsed', collapsed)
    }, [collapsed])



    useEffect(() => {
        localStorage.setItem('darkMode', darkMode)
        document.body.classList.toggle('dark', darkMode)
    }, [darkMode])

    /* Close user menu on outside click */
    useEffect(() => {
        function handleClickOutside(event) {
            if (menuRef.current && !menuRef.current.contains(event.target)) {
                setOpenMenu(false)
            }
        }
        document.addEventListener('mousedown', handleClickOutside)
        return () => document.removeEventListener('mousedown', handleClickOutside)
    }, [])

    return (
        <div className="app-container">

            {/* HEADER OPERACIONAL */}
            <header className="header">
                <div style={{ display: 'flex', alignItems: 'center', gap: '12px' }}>
                    <button
                        className="mobile-menu-btn"
                        onClick={() => setMobileOpen(true)}
                    >
                        <Menu size={20} />
                    </button>

                    <strong>CEIFA</strong>
                </div>

                <div className="header-right" ref={menuRef}>

                    {/* Dark Toggle */}
                    <button
                        className="theme-toggle"
                        onClick={() => setDarkMode(prev => !prev)}
                    >
                        {darkMode ? '☀️' : '🌙'}
                    </button>

                    {/* User */}
                    <span
                        className="user-name"
                        onClick={() => setOpenMenu(prev => !prev)}
                    >
                        {user?.email} ▾
                    </span>

                    {openMenu && (
                        <div className="user-menu">
                            <Link
                                to="/auth/trocar-senha"
                                className="user-menu-item"
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

            <div className="body-wrapper">
                <Sidebar
                    collapsed={collapsed}
                    toggleCollapse={() => setCollapsed(prev => !prev)}
                    mobileOpen={mobileOpen}
                    closeMobile={() => setMobileOpen(false)}
                />

                <main className="operation-content">
                    {moduleTabs && <ModuleTabs tabs={moduleTabs} />}
                    {children}
                </main>
            </div>

            <footer>
                <div>Versão 1.0.0</div>
                <div>Copyright © CEIFA</div>
                <div>{new Date().toLocaleDateString()}</div>
            </footer>
        </div>
    )
}