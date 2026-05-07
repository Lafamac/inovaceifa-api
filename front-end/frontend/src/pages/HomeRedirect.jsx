import { Navigate } from 'react-router-dom'
import { useAuth } from '../auth/AuthContext'

export default function HomeRedirect() {
    const { user, loading } = useAuth()

    if (loading) {
        return <p>Carregando...</p>
    }

    if (!user) {
        return <Navigate to="/login" replace />
    }

    const perfil = user.perfilId   // ← CORRETO

    if (perfil === 1) {
        return <Navigate to="/selecionar-fazenda" replace />
    }

    if (perfil === 2) {
        return <Navigate to="/proprietarios" replace />
    }

    if (perfil === 3) {
        return <Navigate to="/ativarSafra" replace />
    }

    return <Navigate to="/login" replace />
}
