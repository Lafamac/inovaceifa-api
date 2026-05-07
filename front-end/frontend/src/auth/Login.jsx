import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../auth/AuthContext'
import { login } from '../api/api.js'
import Alert from '../components/Alert'
import '../styles/login.css'

function Login() {
    const [email, setEmail] = useState('')
    const [senha, setSenha] = useState('')
    const [erro, setErro] = useState('')
    const navigate = useNavigate()
    const { loginSuccess } = useAuth() // 🔧 novo

    async function handleLogin(e) {
        e.preventDefault()
        setErro('')

        try {
            const response = await login(email, senha)

            if (!response.ok) {
                setErro(
                    response.data?.message ||
                    'Erro ao realizar login'
                )
                return
            }

            if (!response.data?.success) {
                setErro(
                    response.data?.message ||
                    'Perfil bloqueado ou dados inválidos'
                )
                return
            }

            const token = response.data?.data?.token
            if (!token) {
                setErro('Erro ao realizar login')
                return
            }

            // 🔧 alteração aqui
            loginSuccess(token)

            navigate('/')
        } catch (err) {
            console.error(err)
            setErro('Erro ao conectar com o servidor')
        }
    }

    return (
        <div className="container">
            <div className="left-section">
                <img
                    src="/assets/background.png"
                    alt=""
                    className="bg-image"
                />
            </div>

            <div className="right-section">
                <div className="logo">
                    <img src="/assets/logo.png" alt="Logo do sistema" />
                </div>

                <Alert type="error" message={erro} />

                <form onSubmit={handleLogin} autoComplete="on">
                    <label>E-mail</label>
                    <input
                        type="email"
                        name="username"
                        placeholder="E-mail"
                        value={email}
                        onChange={e => setEmail(e.target.value)}
                        required
                        autoComplete="username"
                    />

                    <label>Senha</label>
                    <input
                        type="password"
                        name="password"
                        placeholder="Senha"
                        value={senha}
                        onChange={e => setSenha(e.target.value)}
                        required
                        autoComplete="current-password"
                    />

                    <div className="options">
                        <a href="#">Esqueci minha senha</a>
                    </div>

                    <button type="submit">
                        Entrar
                    </button>
                </form>
            </div>

            <footer className="login-footer">
                <div>Versão 1.0.0</div>
                <div>Copyright © CEIFA</div>
                <div>{new Date().toLocaleDateString()}</div>
            </footer>
        </div>
    )
}

export default Login
