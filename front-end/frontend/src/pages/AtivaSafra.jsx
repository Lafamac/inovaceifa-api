import { useEffect, useState } from 'react'
import { useAuth } from '../auth/AuthContext'
import { useNavigate } from 'react-router-dom'
import { listarSafras, ativarSafra } from '../api/api'
import '../styles/pages.css'
import PageLayout from '../components/PageLayout'

export default function AtivaSafra() {
    const { user, setSafraAtiva } = useAuth()
    const navigate = useNavigate()

    const [safras, setSafras] = useState([])
    const [safraSelecionada, setSafraSelecionada] = useState('')
    const [loadingSafras, setLoadingSafras] = useState(true)

    useEffect(() => {
        if (user?.fazendaAtiva?.id) {
            carregarSafras()
        }
    }, [user?.fazendaAtiva?.id])

    async function carregarSafras() {
        setLoadingSafras(true)

        try {
            const response = await listarSafras()
            if (!response.ok) return

            const data = response.data?.data
            const lista = data?.content || []
            setSafras(lista)
        } catch (err) {
            console.error(err)
        } finally {
            setLoadingSafras(false)
        }
    }

    async function handleSelecionarSafra(e) {
        const id = e.target.value
        setSafraSelecionada(id)

        if (!id) return

        try {
            const response = await ativarSafra(id)
            if (!response.ok) {
                console.error('Erro ao ativar safra')
                return
            }

            const safra = safras.find(s => String(s.id) === String(id))
            if (safra) {
                setSafraAtiva(safra)
                localStorage.setItem('safraAtiva', JSON.stringify(safra))
            }

            navigate('/menu')
        } catch (err) {
            console.error(err)
        }
    }

    if (!user) {
        return <p>Carregando...</p>
    }

    const podeVoltar =
        user?.perfilId === 1 || user?.perfilId === 2

    return (
        <PageLayout
            title="Dados da Fazenda"
            showBack={podeVoltar}
            backTo="/selecionar-fazenda"
        >
            <div className="ativarSafra-grid">
                <div className="ativarSafra-card">
                    <h3>Fazenda ativa</h3>
                    <p>
                        {user.fazendaAtiva
                            ? user.fazendaAtiva.nome
                            : 'Nenhuma fazenda selecionada'}
                    </p>
                </div>

                <div className="ativarSafra-card">
                    <h3>Usuário</h3>
                    <p><strong>Nome:</strong> {user.nome}</p>
                    <p><strong>Tipo:</strong> {user.tipo}</p>
                </div>

                <div className="ativarSafra-card">
                    <h3>Safra ativa</h3>

                    {loadingSafras ? (
                        <p>Carregando safras...</p>
                    ) : (
                        <select
                            value={safraSelecionada}
                            onChange={handleSelecionarSafra}
                            style={{ padding: '6px', width: '50%' }}
                        >
                            <option value="">
                                Selecionar safra
                            </option>

                            {safras.map(s => (
                                <option key={s.id} value={s.id}>
                                    {s.nome || `Safra ${s.id}`}
                                </option>
                            ))}
                        </select>
                    )}
                </div>
            </div>
        </PageLayout>
    )
}
