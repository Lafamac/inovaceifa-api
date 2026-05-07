import { Navigate, useLocation } from 'react-router-dom'
import { useAuth } from './AuthContext'
import '../styles/layout.css'

export default function PrivateRoute({ children }) {
    const { isAuthenticated, loading, user, safraAtiva } = useAuth()
    const location = useLocation()

    if (loading) {
        return (
            <div className="global-loader">
                <div className="loader-box">
                    <div className="loader-spinner" />
                    Carregando sistema...
                </div>
            </div>
        )
    }


    if (!isAuthenticated) {
        return <Navigate to="/login" replace />
    }

    const perfil = user?.perfilId
    const fazendaAtiva = user?.fazendaAtiva

    // 🔥 PERFIL 2 (SUPER USUÁRIO) NÃO USA FAZENDA/Safra
    if (perfil === 2) {
        return children
    }

    // Perfil 1 precisa escolher fazenda
    if (perfil === 1 && !fazendaAtiva) {
        if (location.pathname !== '/selecionar-fazenda') {
            return <Navigate to="/selecionar-fazenda" replace />
        }
    }

    // Perfil 3 precisa ter safra ativa
    if (perfil === 3 && fazendaAtiva && !safraAtiva) {
        if (location.pathname !== '/ativarSafra') {
            return <Navigate to="/ativarSafra" replace />
        }
    }

    return children
}
