import { useState, useEffect } from 'react'
import { NavLink, useNavigate, useLocation } from 'react-router-dom'

import {
    Map,
    Tractor,
    Users,
    DollarSign,
    Package,
    ArrowLeftRight,
    Building2,
    Sprout,
    ChevronLeft,
    ChevronRight,
    ChevronDown,
    X,
    UserCog,
    Home,
    ClipboardList,
    ShoppingCart,
    BarChart3,
    PieChart,
    Table,
    Database
} from 'lucide-react'

import { useAuth } from '../auth/AuthContext'

import '../styles/layout.css'

export default function Sidebar({
    collapsed,
    toggleCollapse,
    mobileOpen,
    closeMobile
}) {

    const { user, safraAtiva } = useAuth()

    const navigate = useNavigate()

    const location = useLocation()

    const [openOperacional, setOpenOperacional] =
        useState(false)

    const [openSuprimentos, setOpenSuprimentos] =
        useState(false)

    const [openFinanceiro, setOpenFinanceiro] =
        useState(false)

    const [openCadastros, setOpenCadastros] =
        useState(false)

    /* 🔥 ABRIR SUBMENU AUTOMATICAMENTE */
    useEffect(() => {

        const path = location.pathname

        if (
            path.startsWith('/safra-talhoes') ||
            path.startsWith('/ordens-servico') ||
            path.startsWith('/planejamento/adubacao')
        ) {

            setOpenOperacional(true)
        }

        if (
            path.startsWith('/produtos/movimentar') ||
            path.startsWith('/pedidos-compra')
        ) {

            setOpenSuprimentos(true)
        }

        if (
            path.startsWith('/contas') ||
            path.startsWith('/fluxo') ||
            path.startsWith('/dashboard') ||
            path.startsWith('/relatorio') ||
            path.startsWith('/financeiro') ||
            path.startsWith('/folha') ||
            path.startsWith('/custos') ||

            // 🔥 NOVO
            path.startsWith('/bi/comparativo-talhoes')
        ) {

            setOpenFinanceiro(true)
        }

        if (
            path.startsWith('/safra') ||
            path.startsWith('/referencias') ||
            path.startsWith('/talhoes') ||
            path.startsWith('/maquinas') ||
            path.startsWith('/funcionarios') ||
            path.startsWith('/terceirizados') ||
            path.startsWith('/turmas') ||
            path === '/produtos'
        ) {

            setOpenCadastros(true)
        }

    }, [location.pathname])

    /* 🔥 RESET AO COLAPSAR */
    useEffect(() => {

        if (collapsed) {

            setOpenOperacional(false)

            setOpenSuprimentos(false)

            setOpenFinanceiro(false)

            setOpenCadastros(false)
        }

    }, [collapsed])

    return (
        <>
            <div
                className={`sidebar-overlay ${mobileOpen ? 'show' : ''}`}
                onClick={closeMobile}
            />

            <aside
                className={`sidebar
                ${collapsed ? 'collapsed' : ''}
                ${mobileOpen ? 'mobile-open' : ''}`}
            >

                <div className="sidebar-top">

                    <div className="sidebar-mobile-header">

                        <button
                            onClick={closeMobile}
                            className="mobile-close-btn"
                        >

                            <X size={18} />

                        </button>

                    </div>

                    <div
                        className="sidebar-toggle"
                        onClick={toggleCollapse}
                    >

                        {collapsed ? (

                            <ChevronRight size={18} />

                        ) : (

                            <ChevronLeft size={18} />

                        )}

                    </div>

                </div>

                {!collapsed && (

                    <div className="sidebar-header">

                        {user?.fazendaAtiva?.nome || ''}

                        {safraAtiva && (

                            <div className="sidebar-safra">

                                {safraAtiva.nome}

                            </div>
                        )}

                    </div>
                )}

                <div className="sidebar-context">

                    {user?.perfilId === 2 && (

                        <button
                            className="sidebar-context-button"
                            onClick={() =>
                                navigate('/proprietarios')
                            }
                        >

                            <Building2 size={16} />

                            {!collapsed && (
                                <span>
                                    Trocar Proprietário
                                </span>
                            )}

                        </button>
                    )}

                    {(user?.perfilId === 1 ||
                        user?.perfilId === 2) && (

                        <button
                            className="sidebar-context-button"
                            onClick={() =>
                                navigate('/selecionar-fazenda')
                            }
                        >

                            <Tractor size={16} />

                            {!collapsed && (
                                <span>
                                    Trocar Fazenda
                                </span>
                            )}

                        </button>
                    )}

                    <button
                        className="sidebar-context-button"
                        onClick={() =>
                            navigate('/ativarsafra')
                        }
                    >

                        <Sprout size={16} />

                        {!collapsed && (
                            <span>
                                Trocar Safra
                            </span>
                        )}

                    </button>

                </div>

                <hr className="sidebar-divider" />

                <nav className="sidebar-nav">

                    <NavLink
                        to="/menu"
                        className="sidebar-item"
                        data-label="Menu"
                    >

                        <Home size={18} />

                        {!collapsed && (
                            <span>
                                Menu Inicial
                            </span>
                        )}

                    </NavLink>

                    {/* ======================================= */}
                    {/* 🔥 CADASTROS */}
                    {/* ======================================= */}

                    <button
                        className={`sidebar-item ${openCadastros ? 'active' : ''}`}
                        onClick={() =>
                            setOpenCadastros(prev => !prev)
                        }
                        style={{
                            width: '100%',
                            background: 'none',
                            border: 'none'
                        }}
                        data-label="Cadastros"
                    >

                        <Database size={18} />

                        {!collapsed && (
                            <>
                                <span
                                    style={{
                                        flex: 1,
                                        textAlign: 'left'
                                    }}
                                >
                                    Cadastros
                                </span>

                                {openCadastros ? (
                                    <ChevronDown size={16} />
                                ) : (
                                    <ChevronRight size={16} />
                                )}
                            </>
                        )}

                    </button>

                    {openCadastros && !collapsed && (
                        <>

                            <NavLink
                                to="/safra"
                                className="sidebar-item"
                                style={{ paddingLeft: '36px' }}
                            >

                                <Sprout
                                    size={16}
                                    style={{ marginRight: 8 }}
                                />

                                <span>Safra</span>

                            </NavLink>

                            <NavLink
                                to="/talhoes"
                                className="sidebar-item"
                                style={{ paddingLeft: '36px' }}
                            >

                                <Map
                                    size={16}
                                    style={{ marginRight: 8 }}
                                />

                                <span>Talhões Base</span>

                            </NavLink>

                            <NavLink
                                to="/produtos"
                                className="sidebar-item"
                                style={{ paddingLeft: '36px' }}
                            >

                                <Package
                                    size={16}
                                    style={{ marginRight: 8 }}
                                />

                                <span>Produtos Base</span>

                            </NavLink>

                            <NavLink
                                to="/maquinas"
                                className="sidebar-item"
                                style={{ paddingLeft: '36px' }}
                            >

                                <Tractor
                                    size={16}
                                    style={{ marginRight: 8 }}
                                />

                                <span>Máquinas</span>

                            </NavLink>

                            <NavLink
                                to="/funcionarios"
                                className="sidebar-item"
                                style={{ paddingLeft: '36px' }}
                            >

                                <Users
                                    size={16}
                                    style={{ marginRight: 8 }}
                                />

                                <span>Funcionários</span>

                            </NavLink>

                            <NavLink
                                to="/terceirizados"
                                className="sidebar-item"
                                style={{ paddingLeft: '36px' }}
                            >

                                <UserCog
                                    size={16}
                                    style={{ marginRight: 8 }}
                                />

                                <span>Terceirizados</span>

                            </NavLink>

                            <NavLink
                                to="/turmas-terceirizadas"
                                className="sidebar-item"
                                style={{ paddingLeft: '36px' }}
                            >

                                <Users
                                    size={16}
                                    style={{ marginRight: 8 }}
                                />

                                <span>
                                    Turmas Terceirizadas
                                </span>

                            </NavLink>

                            <NavLink
                                to="/referencias"
                                className="sidebar-item"
                                style={{ paddingLeft: '36px' }}
                            >

                                <Table
                                    size={16}
                                    style={{ marginRight: 8 }}
                                />

                                <span>
                                    Tabelas de Ref.
                                </span>

                            </NavLink>

                        </>
                    )}

                    {/* ======================================= */}
                    {/* 🔥 OPERACIONAL */}
                    {/* ======================================= */}

                    <button
                        className={`sidebar-item ${openOperacional ? 'active' : ''}`}
                        onClick={() =>
                            setOpenOperacional(prev => !prev)
                        }
                        style={{
                            width: '100%',
                            background: 'none',
                            border: 'none'
                        }}
                    >

                        <Tractor size={18} />

                        {!collapsed && (
                            <>
                                <span
                                    style={{
                                        flex: 1,
                                        textAlign: 'left'
                                    }}
                                >
                                    Operacional
                                </span>

                                {openOperacional ? (
                                    <ChevronDown size={16} />
                                ) : (
                                    <ChevronRight size={16} />
                                )}
                            </>
                        )}

                    </button>

                    {openOperacional && !collapsed && (
                        <>
                            <NavLink
                                to="/safra-talhoes"
                                className="sidebar-item"
                                style={{ paddingLeft: '36px' }}
                            >

                                <Map
                                    size={16}
                                    style={{ marginRight: 8 }}
                                />

                                <span>
                                    Safra Talhões
                                </span>

                            </NavLink>

                            <NavLink
                                to="/ordens-servico"
                                className="sidebar-item"
                                style={{ paddingLeft: '36px' }}
                            >

                                <ClipboardList
                                    size={16}
                                    style={{ marginRight: 8 }}
                                />

                                <span>
                                    Ordens de Serviço
                                </span>

                            </NavLink>

                            <NavLink
                                to="/planejamento/adubacao"
                                className="sidebar-item"
                                style={{ paddingLeft: '36px' }}
                            >

                                <BarChart3
                                    size={16}
                                    style={{ marginRight: 8 }}
                                />

                                <span>
                                    Adubação
                                </span>

                            </NavLink>

                        </>
                    )}

                    {/* ======================================= */}
                    {/* 🔥 SUPRIMENTOS */}
                    {/* ======================================= */}

                    <button
                        className={`sidebar-item ${openSuprimentos ? 'active' : ''}`}
                        onClick={() =>
                            setOpenSuprimentos(prev => !prev)
                        }
                        style={{
                            width: '100%',
                            background: 'none',
                            border: 'none'
                        }}
                    >

                        <Package size={18} />

                        {!collapsed && (
                            <>
                                <span
                                    style={{
                                        flex: 1,
                                        textAlign: 'left'
                                    }}
                                >
                                    Suprimentos
                                </span>

                                {openSuprimentos ? (
                                    <ChevronDown size={16} />
                                ) : (
                                    <ChevronRight size={16} />
                                )}
                            </>
                        )}

                    </button>

                    {openSuprimentos && !collapsed && (
                        <>

                            <NavLink
                                to="/produtos/movimentar"
                                className="sidebar-item"
                                style={{ paddingLeft: '36px' }}
                            >

                                <ArrowLeftRight
                                    size={16}
                                    style={{ marginRight: 8 }}
                                />

                                <span>
                                    Movimentação
                                </span>

                            </NavLink>

                            <NavLink
                                to="/pedidos-compra"
                                className="sidebar-item"
                                style={{ paddingLeft: '36px' }}
                            >

                                <ShoppingCart
                                    size={16}
                                    style={{ marginRight: 8 }}
                                />

                                <span>
                                    Compras
                                </span>

                            </NavLink>

                        </>
                    )}

                    {/* ======================================= */}
                    {/* 🔥 FINANCEIRO */}
                    {/* ======================================= */}

                    <button
                        className={`sidebar-item ${openFinanceiro ? 'active' : ''}`}
                        onClick={() =>
                            setOpenFinanceiro(prev => !prev)
                        }
                        style={{
                            width: '100%',
                            background: 'none',
                            border: 'none'
                        }}
                    >

                        <DollarSign size={18} />

                        {!collapsed && (
                            <>
                                <span
                                    style={{
                                        flex: 1,
                                        textAlign: 'left'
                                    }}
                                >
                                    Financeiro & RH
                                </span>

                                {openFinanceiro ? (
                                    <ChevronDown size={16} />
                                ) : (
                                    <ChevronRight size={16} />
                                )}
                            </>
                        )}

                    </button>

                    {openFinanceiro && !collapsed && (
                        <>

                            <NavLink
                                to="/dashboard-safra"
                                className="sidebar-item"
                                style={{ paddingLeft: '36px' }}
                            >

                                <BarChart3
                                    size={16}
                                    style={{ marginRight: 8 }}
                                />

                                <span>
                                    Dashboard Safra
                                </span>

                            </NavLink>

                            <NavLink
                                to="/relatorio"
                                className="sidebar-item"
                                style={{ paddingLeft: '36px' }}
                            >

                                <PieChart
                                    size={16}
                                    style={{ marginRight: 8 }}
                                />

                                <span>
                                    Relatórios
                                </span>

                            </NavLink>

                            <NavLink
                                to="/relatorios/gestao-vista"
                                className="sidebar-item"
                                style={{ paddingLeft: '36px' }}
                            >

                                <BarChart3
                                    size={16}
                                    style={{ marginRight: 8 }}
                                />

                                <span>
                                    Visão Gerencial
                                </span>

                            </NavLink>

                            {/* 🔥 NOVO */}
                            <NavLink
                                to="/bi/comparativo-talhoes"
                                className="sidebar-item"
                                style={{ paddingLeft: '36px' }}
                            >

                                <BarChart3
                                    size={16}
                                    style={{ marginRight: 8 }}
                                />

                                <span>
                                    Comparativo Talhões
                                </span>

                            </NavLink>

                        </>
                    )}

                </nav>

            </aside>
        </>
    )
}