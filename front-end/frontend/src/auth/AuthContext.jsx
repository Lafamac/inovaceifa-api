import { createContext, useContext, useEffect, useState } from 'react'
import { getMe } from '../api/api'

const AuthContext = createContext()

export function AuthProvider({ children }) {
    const [token, setToken] = useState(() => localStorage.getItem('token'))
    const [user, setUser] = useState(null)
    const [safraAtiva, setSafraAtiva] = useState(() => {
        const saved = localStorage.getItem('safraAtiva')
        return saved ? JSON.parse(saved) : null
    })
    const [loading, setLoading] = useState(true)

    useEffect(() => {
        async function loadUser() {
            if (!token) {
                setUser(null)
                setLoading(false)
                return
            }

            setLoading(true)

            try {
                const response = await getMe()

                if (response.ok && response.data?.data) {
                    const newUser = response.data.data
                    setUser(newUser)

                    const savedSafra = localStorage.getItem('safraAtiva')
                    if (savedSafra) {
                        const parsedSafra = JSON.parse(savedSafra)

                        if (
                            parsedSafra.fazendaId &&
                            parsedSafra.fazendaId !== newUser.fazendaAtiva?.id
                        ) {
                            updateSafraAtiva(null)
                        }
                    }
                } else {
                    localStorage.removeItem('token')
                    setToken(null)
                    setUser(null)
                }
            } catch (error) {
                console.error('Erro ao carregar usuário', error)
            } finally {
                setLoading(false)
            }
        }

        loadUser()
    }, [token]) // 🔧 agora depende do token

    function updateSafraAtiva(novaSafra) {
        setSafraAtiva(novaSafra)

        if (novaSafra) {
            localStorage.setItem('safraAtiva', JSON.stringify(novaSafra))
        } else {
            localStorage.removeItem('safraAtiva')
        }
    }

    function loginSuccess(newToken) {
        localStorage.setItem('token', newToken)
        setToken(newToken)
    }

    function logout() {
        localStorage.removeItem('token')
        localStorage.removeItem('safraAtiva')
        setToken(null)
        setUser(null)
        setSafraAtiva(null)
        window.location.href = '/login'
    }

    return (
        <AuthContext.Provider
            value={{
                user,
                setUser,
                safraAtiva,
                setSafraAtiva: updateSafraAtiva,
                isAuthenticated: !!user,
                loading,
                logout,
                loginSuccess // 🔧 novo método
            }}
        >
            {children}
        </AuthContext.Provider>
    )
}

// eslint-disable-next-line react-refresh/only-export-components
export function useAuth() {
    return useContext(AuthContext)
}

export default AuthContext
