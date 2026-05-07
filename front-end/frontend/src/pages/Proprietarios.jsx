import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import {
    listarProprietarios,
    ativarProprietario,
    desativarProprietario,
    ativarProprietarioContexto,
    getMe
} from '../api/api'
import { useAuth } from '../auth/AuthContext'
import Alert from '../components/Alert'
import PageLayout from '../components/PageLayout'
import ConfirmDialog from '../components/ConfirmDialog'
import '../styles/pages.css'
import { Pencil, Power } from 'lucide-react'
import SkeletonCard from '../components/SkeletonCard'

export default function Proprietarios() {
    const [proprietarios, setProprietarios] = useState([])
    const [loading, setLoading] = useState(true)
    const [erro, setErro] = useState('')
    const [busca, setBusca] = useState('')
    const [dialogOpen, setDialogOpen] = useState(false)
    const [proprietarioSelecionado, setProprietarioSelecionado] = useState(null)

    const navigate = useNavigate()
    const { setUser } = useAuth()

    useEffect(() => {
        carregarProprietarios()
    }, [])

    async function carregarProprietarios() {
        setLoading(true)
        setErro('')

        try {
            const response = await listarProprietarios()

            if (!response.ok) {
                setErro(response.data?.message || 'Erro ao carregar proprietários')
                return
            }

            const data = response.data?.data
            const lista = data?.content || []

            setProprietarios(lista)
        } catch (err) {
            console.error(err)
            setErro('Erro ao conectar com o servidor')
        } finally {
            setLoading(false)
        }
    }

    async function selecionarProprietario(proprietario) {
        try {
            const response = await ativarProprietarioContexto(proprietario.id)

            if (!response.ok) {
                setErro(response.data?.message || 'Erro ao definir proprietário')
                return
            }

            const me = await getMe()

            if (me.ok && me.data?.data) {
                setUser(me.data.data)

                localStorage.setItem('user', JSON.stringify(me.data.data))
            }

            navigate('/selecionar-fazenda')
        } catch (err) {
            console.error(err)
            setErro('Erro ao conectar com o servidor')
        }
    }

    function editarProprietario(id) {
        navigate(`/proprietarios/${id}/editar`)
    }

    function abrirDialog(proprietario) {
        setProprietarioSelecionado(proprietario)
        setDialogOpen(true)
    }

    function fecharDialog() {
        setDialogOpen(false)
        setProprietarioSelecionado(null)
    }

    async function confirmarAlteracao() {
        if (!proprietarioSelecionado) return

        try {
            const response = proprietarioSelecionado.ativo
                ? await desativarProprietario(proprietarioSelecionado.id)
                : await ativarProprietario(proprietarioSelecionado.id)

            if (!response.ok) {
                setErro(response.data?.message || 'Erro ao alterar status')
                return
            }

            fecharDialog()
            carregarProprietarios()
        } catch (err) {
            console.error(err)
            setErro('Erro ao conectar com o servidor')
        }
    }

    const proprietariosFiltrados = proprietarios.filter(p =>
        p.nome?.toLowerCase().includes(busca.toLowerCase())
    )

    return (
        <PageLayout title="Proprietários">
            <div className="actions">
                <input
                    type="text"
                    className="search-bar"
                    placeholder="🔍 Buscar..."
                    value={busca}
                    onChange={e => setBusca(e.target.value)}
                />

                <button
                    className="add-btn"
                    onClick={() => navigate('/proprietarios/novo')}
                >
                    + Novo Proprietário
                </button>
            </div>

            <Alert type="error" message={erro} />

            {loading && (
                <>
                    <SkeletonCard />
                    <SkeletonCard />
                    <SkeletonCard />
                </>
            )}

            {!loading && proprietariosFiltrados.length === 0 && (
                <p>Nenhum proprietário encontrado.</p>
            )}

            <div className="card-list">
                {proprietariosFiltrados.map(p => (
                    <div
                        key={p.id}
                        className="card"
                        onClick={() => selecionarProprietario(p)}
                    >
                        <div>
                            <div className="card-title">
                                {p.nome}
                            </div>

                            <div className="card-subtitle">
                                {p.email || 'Sem e-mail'}
                            </div>

                            {/* ÚNICA PÁGINA QUE MOSTRA STATUS */}
                            <div
                                className={`card-status ${
                                    p.ativo ? 'ativo' : 'inativo'
                                }`}
                            >
                                {p.ativo ? 'Ativo' : 'Inativo'}
                            </div>
                        </div>

                        <div
                            className="crud-card-actions"
                            onClick={e => e.stopPropagation()}
                        >
                            <button
                                className="icon-button edit"
                                onClick={() => editarProprietario(p.id)}
                                title="Editar proprietário"
                            >
                                <Pencil size={18} />
                            </button>

                            <button
                                className={`icon-button ${
                                    p.ativo ? 'danger' : 'success'
                                }`}
                                onClick={() => abrirDialog(p)}
                                title={
                                    p.ativo
                                        ? 'Desativar proprietário'
                                        : 'Ativar proprietário'
                                }
                            >
                                <Power size={18} />
                            </button>
                        </div>
                    </div>
                ))}
            </div>

            <ConfirmDialog
                open={dialogOpen}
                title={
                    proprietarioSelecionado?.ativo
                        ? 'Desativar proprietário'
                        : 'Ativar proprietário'
                }
                message={
                    proprietarioSelecionado?.ativo
                        ? 'Ao desativar, o proprietário não poderá acessar o sistema até ser reativado. Deseja continuar?'
                        : 'Deseja reativar este proprietário?'
                }
                onConfirm={confirmarAlteracao}
                onCancel={fecharDialog}
            />
        </PageLayout>
    )
}