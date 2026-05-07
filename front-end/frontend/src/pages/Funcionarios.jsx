import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import {
    listarFuncionarios,
    listarFuncionariosInativos,
    excluirFuncionario,
    reativarFuncionario,
    criarUsuarioFuncionario
} from '../api/api'
import useAtivoInativoCrud from '../hooks/useAtivoInativoCrud'
import CrudToolbar from '../components/CrudToolbar'
import CrudCard from '../components/CrudCard'
import CrudCardList from '../components/CrudCardList'
import ConfirmDialog from '../components/ConfirmDialog'
import Pagination from '../components/Pagination'
import EmptyState from '../components/EmptyState'
import Alert from '../components/Alert'
import PageLayout from '../components/PageLayout'
import { Pencil, Power, UserPlus } from 'lucide-react'
import SkeletonCard from '../components/SkeletonCard'
import { useAuth } from '../auth/AuthContext' // 🔥 NOVO

export default function Funcionarios() {
    const navigate = useNavigate()
    const { user } = useAuth() // 🔥 NOVO

    const [busca, setBusca] = useState('')
    const [loadingUsuario, setLoadingUsuario] = useState(null)
    const [erroUsuario, setErroUsuario] = useState('')
    const [sucessoUsuario, setSucessoUsuario] = useState('')

    const {
        dados: funcionarios,
        loading,
        erro,
        mostrarInativos,
        setMostrarInativos,
        page,
        totalPages,
        setPage,
        abrirDialog,
        dialog,
        fecharDialog,
        confirmarDialog
    } = useAtivoInativoCrud({
        listarAtivos: listarFuncionarios,
        listarInativos: listarFuncionariosInativos,
        inativar: excluirFuncionario,
        reativar: reativarFuncionario,
        entityName: 'Funcionário'
    })

    const filtrados = funcionarios.filter(f =>
        f.nome?.toLowerCase().includes(busca.toLowerCase())
    )

    async function handleCriarUsuario(id) {
        setErroUsuario('')
        setSucessoUsuario('')
        setLoadingUsuario(id)

        try {
            const response = await criarUsuarioFuncionario(id)

            if (!response.ok || !response.data?.success) {
                setErroUsuario(response.data?.message || 'Erro ao criar usuário')
                setLoadingUsuario(null)
                return
            }

            const credenciais = response.data?.data

            setSucessoUsuario(
                `Usuário criado com sucesso. Email: ${credenciais?.email} | Senha: ${credenciais?.senha}`
            )

        } catch (err) {
            console.error(err)
            setErroUsuario('Erro ao criar usuário')
        }

        setLoadingUsuario(null)
    }

    // 🔥 BLOQUEIO CORRETO
    if (user?.perfilId === 3) {
        return (
            <PageLayout title="Funcionários">
                <Alert type="error" message="Você não tem permissão para acessar esta tela." />
            </PageLayout>
        )
    }

    return (
        <PageLayout title="Funcionários">
            <CrudToolbar
                busca={busca}
                setBusca={setBusca}
                onNovo={() => navigate('/funcionarios/novo')}
                labelNovo="Novo Funcionário"
                mostrarInativos={mostrarInativos}
                setMostrarInativos={setMostrarInativos}
            />

            <Alert type="error" message={erro} />
            <Alert type="error" message={erroUsuario} />
            <Alert type="success" message={sucessoUsuario} />

            {loading && (
                <>
                    <SkeletonCard />
                    <SkeletonCard />
                    <SkeletonCard />
                </>
            )}

            {!loading && filtrados.length === 0 && (
                <EmptyState
                    message={
                        mostrarInativos
                            ? 'Nenhum funcionário inativo encontrado.'
                            : 'Nenhum funcionário ativo encontrado.'
                    }
                />
            )}

            <CrudCardList>
                {filtrados.map(f => (
                    <CrudCard
                        key={f.id}
                        title={f.nome}
                        subtitle={`CPF: ${f.cpf || '-'}`}
                        ativo={!mostrarInativos}
                        disabled={mostrarInativos}
                        onClick={() =>
                            !mostrarInativos &&
                            navigate(`/funcionarios/${f.id}/editar`)
                        }
                        actions={
                            <>
                                {!mostrarInativos && (
                                    <button
                                        className="icon-button edit"
                                        onClick={() =>
                                            navigate(`/funcionarios/${f.id}/editar`)
                                        }
                                        title="Editar"
                                    >
                                        <Pencil size={18} />
                                    </button>
                                )}

                                {!mostrarInativos && !f.possuiUsuario && f.ativo && (
                                    <button
                                        className="icon-button success"
                                        onClick={() => handleCriarUsuario(f.id)}
                                        title="Criar Usuário"
                                        disabled={loadingUsuario === f.id}
                                    >
                                        <UserPlus size={18} />
                                    </button>
                                )}

                                {!mostrarInativos ? (
                                    <button
                                        className="icon-button danger"
                                        onClick={() =>
                                            abrirDialog('inativar', f.id)
                                        }
                                        title="Inativar"
                                    >
                                        <Power size={18} />
                                    </button>
                                ) : (
                                    <button
                                        className="icon-button success"
                                        onClick={() =>
                                            abrirDialog('reativar', f.id)
                                        }
                                        title="Reativar"
                                    >
                                        <Power size={18} />
                                    </button>
                                )}
                            </>
                        }
                    />
                ))}
            </CrudCardList>

            <Pagination
                page={page}
                totalPages={totalPages}
                onChange={setPage}
            />

            <ConfirmDialog
                open={dialog.open}
                title="Confirmar operação"
                message={`Deseja realmente ${
                    dialog.tipo === 'inativar' ? 'inativar' : 'reativar'
                } este funcionário?`}
                onConfirm={confirmarDialog}
                onCancel={fecharDialog}
            />
        </PageLayout>
    )
}