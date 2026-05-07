import { useAuth } from '../auth/AuthContext'
import { useNavigate } from 'react-router-dom'
import PageLayout from '../components/PageLayout'
import '../styles/pages.css'
import { useEffect, useState } from 'react'

import {
    ClipboardList,
    DollarSign,
    Users,
    Tractor,
    Package,
    ArrowLeftRight,
    Sprout,
    Table,
    Map,
    ShoppingCart,
    BarChart3,
    PieChart,
    UserCog,
    Database
} from 'lucide-react'

export default function MenuFazenda() {
    const { user, safraAtiva } = useAuth()
    const navigate = useNavigate()

    const [isDark, setIsDark] = useState(
        document.body.classList.contains('dark')
    )

    useEffect(() => {
        const observer = new MutationObserver(() => {
            setIsDark(document.body.classList.contains('dark'))
        })

        observer.observe(document.body, {
            attributes: true,
            attributeFilter: ['class']
        })

        return () => observer.disconnect()
    }, [])

    if (!user) {
        return <p>Carregando...</p>
    }

    const modulosOperacionais = [
        { nome: 'Safra Talhões', rota: '/safra-talhoes', icon: Map, descricao: 'Gestão da safra por talhão' },
        { nome: 'Ordens de Serviço', rota: '/ordens-servico', icon: ClipboardList, descricao: 'Execução e controle operacional' },
        { nome: 'Adubação', rota: '/planejamento/adubacao', icon: BarChart3, descricao: 'Visão geral de insumos da safra' }


    ]

    const modulosSuprimentos = [
        { nome: 'Movimentação de produtos', rota: '/produtos/movimentar', icon: ArrowLeftRight, descricao: 'Entradas e saídas' },
        { nome: 'Compras', rota: '/pedidos-compra', icon: ShoppingCart, descricao: 'Pedidos e estoque' }
    ]

    const modulosFinanceiro = [
        { nome: 'Dashboard Safra', rota: '/dashboard-safra', icon: BarChart3, descricao: 'Indicadores e custos da safra' },
        { nome: 'Comparativo Talhões', rota: '/bi/comparativo-talhoes', icon: BarChart3,  descricao: 'Indicadores comparativos e rankings'},
        { nome: 'Relatório', rota: '/relatorio', icon: PieChart, descricao: 'Resultado financeiro da safra' },
        { nome: 'Visão Gerencial', rota: '/relatorios/gestao-vista', icon: BarChart3, descricao: 'Resultado consolidado por talhão' },
        { nome: 'Financeiro', rota: '/financeiro', icon: DollarSign, descricao: 'Controle de custos e receitas' },
        { nome: 'Administrativo', rota: '/custos/administrativo', icon: BarChart3, descricao: 'Custos indiretos da safra' },
        { nome: 'Lançamentos', rota: '/financeiro/lancamentos', icon: DollarSign, descricao: 'Lançamentos financeiros' },
        { nome: 'Folha', rota: '/folha', icon: DollarSign, descricao: 'Custos de pessoal' },
        { nome: 'Vendas', rota: '/vendas', icon: DollarSign, descricao: 'Comercialização da produção' }
    ]

    const modulosCadastros = [
       { nome: 'Safra', rota: '/safra', icon: Sprout, descricao: 'Configurações da safra ativa' },
           { nome: 'Talhões', rota: '/talhoes', icon: Map, descricao: 'Áreas produtivas da fazenda' },
           { nome: 'Produtos', rota: '/produtos', icon: Package, descricao: 'Cadastro de insumos e itens' },
           { nome: 'Máquinas', rota: '/maquinas', icon: Tractor, descricao: 'Frota e manutenção' },
           { nome: 'Funcionários', rota: '/funcionarios', icon: Users, descricao: 'Equipe e contratos ativos' },
           { nome: 'Terceirizados', rota: '/terceirizados', icon: UserCog, descricao: 'Prestadores de serviços' },
           { nome: 'Turmas', rota: '/turmas-terceirizadas', icon: Users, descricao: 'Equipes terceirizadas' },
           { nome: 'Referências', rota: '/referencias', icon: Table, descricao: 'Dados auxiliares do sistema' }
    ]

    const renderCard = (modulo) => {
        const Icon = modulo.icon
        return (
            <div
                key={modulo.nome}
                className="module-card"
                onClick={() => navigate(modulo.rota)}
            >
                <div className="module-icon">
                    <Icon size={34} />
                </div>
                <div className="module-content">
                    <h3>{modulo.nome}</h3>
                    <p>{modulo.descricao}</p>
                </div>
                <div className="module-arrow">→</div>
            </div>
        )
    }

    // 🔥 STYLE DINÂMICO PARA TÍTULOS
    const sectionTitleStyle = {
        marginBottom: 15,
        color: isDark ? '#f9fafb' : '#1e293b',
        borderBottom: isDark
            ? '2px solid #374151'
            : '2px solid #e2e8f0',
        paddingBottom: 10
    }

    return (
        <PageLayout
            title={
                safraAtiva
                    ? `${user.fazendaAtiva?.nome} — ${safraAtiva.nome}`
                    : user.fazendaAtiva?.nome || 'Fazenda'
            }
        >
            <div className="grid-wrapper" style={{ flexDirection: 'column', alignItems: 'center' }}>

                {/* 🔥 GRUPO 1 — CADASTROS */}
                <div style={{ width: '100%', maxWidth: '1000px', marginBottom: '40px' }}>
                    <h2 style={sectionTitleStyle}>🗃️ Cadastros</h2>
                    <div className="grid-container">
                        {modulosCadastros.map(renderCard)}
                    </div>
                </div>

                {/* 🔥 GRUPO 2 — OPERACIONAL */}
                <div style={{ width: '100%', maxWidth: '1000px', marginBottom: '40px' }}>
                    <h2 style={sectionTitleStyle}>🚜 Operacional & Execução</h2>
                    <div className="grid-container">
                        {modulosOperacionais.map(renderCard)}
                    </div>
                </div>

                {/* 🔥 GRUPO 3 — SUPRIMENTOS */}
                <div style={{ width: '100%', maxWidth: '1000px', marginBottom: '40px' }}>
                    <h2 style={sectionTitleStyle}>📦 Suprimentos</h2>
                    <div className="grid-container">
                        {modulosSuprimentos.map(renderCard)}
                    </div>
                </div>

                {/* 🔥 GRUPO 4 — FINANCEIRO */}
                <div style={{ width: '100%', maxWidth: '1000px', marginBottom: '40px' }}>
                    <h2 style={sectionTitleStyle}>💰 Financeiro & RH</h2>
                    <div className="grid-container">
                        {modulosFinanceiro.map(renderCard)}
                    </div>
                </div>

            </div>
        </PageLayout>
    )
}