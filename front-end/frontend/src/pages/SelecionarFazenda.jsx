import { useState } from 'react'
import { useNavigate } from 'react-router-dom'

import {
    listarFazendas,
    listarFazendasInativas,
    ativarFazenda,
    excluirFazenda,
    reativarFazenda
} from '../api/api'

import { useAuth } from '../auth/AuthContext'
import useAtivoInativoCrud from '../hooks/useAtivoInativoCrud'

import CrudToolbar from '../components/CrudToolbar'
import CrudCard from '../components/CrudCard'
import CrudCardList from '../components/CrudCardList'
import ConfirmDialog from '../components/ConfirmDialog'
import Pagination from '../components/Pagination'
import EmptyState from '../components/EmptyState'
import Alert from '../components/Alert'
import PageLayout from '../components/PageLayout'

import { Pencil, Power } from 'lucide-react'

import SkeletonCard from '../components/SkeletonCard'

export default function SelecionarFazenda() {

    const navigate = useNavigate()

    // 🔥 ALTERADO
    const {
        user,
        setUser
    } = useAuth()

    const [busca, setBusca] = useState('')

    const {
        dados: fazendas,
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
        listarAtivos: listarFazendas,
        listarInativos: listarFazendasInativas,
        inativar: excluirFazenda,
        reativar: reativarFazenda,
        entityName: 'Fazenda'
    })

    // 🔥 ALTERADO
    async function selecionarFazenda(fazenda) {

        try {

            const response = await ativarFazenda(fazenda.id)

            if (!response.ok) return

            // 🔥 ATUALIZA CONTEXTO
            const novoUser = {
                ...user,
                fazendaAtiva: {
                    id: fazenda.id,
                    nome: fazenda.nome
                }
            }

            // 🔥 ATUALIZA AUTH CONTEXT
            setUser(novoUser)

            // 🔥 PERSISTE
            localStorage.setItem(
                'user',
                JSON.stringify(novoUser)
            )

            navigate('/ativarSafra', {
                replace: true
            })

        } catch (err) {

            console.error(err)
        }
    }

    const filtradas = (fazendas || []).filter(f =>
        f.nome?.toLowerCase().includes(busca.toLowerCase())
    )

    return (
        <PageLayout
            title="Selecionar Fazenda"
            showBack={user?.perfilId === 2}
            backTo="/proprietarios"
        >

            <CrudToolbar
                busca={busca}
                setBusca={setBusca}
                onNovo={() => navigate('/fazendas/nova')}
                labelNovo="Nova Fazenda"
                mostrarInativos={mostrarInativos}
                setMostrarInativos={setMostrarInativos}
            />

            <Alert type="error" message={erro} />

            {loading && (
                <>
                    <SkeletonCard />
                    <SkeletonCard />
                    <SkeletonCard />
                </>
            )}

            {!loading && filtradas.length === 0 && (
                <EmptyState
                    message={
                        mostrarInativos
                            ? 'Nenhuma fazenda inativa encontrada.'
                            : 'Nenhuma fazenda ativa encontrada.'
                    }
                />
            )}

            <CrudCardList>
                {filtradas.map(f => (
                    <CrudCard
                        key={f.id}
                        title={f.nome}
                        subtitle={f.endereco || 'Endereço não informado'}
                        ativo={!mostrarInativos}
                        disabled={mostrarInativos}
                        onClick={() =>
                            !mostrarInativos &&
                            selecionarFazenda(f)
                        }
                        actions={
                            <>
                                <button
                                    className="icon-button edit"
                                    onClick={() =>
                                        navigate(`/fazendas/${f.id}/editar`)
                                    }
                                    title="Editar"
                                >
                                    <Pencil size={18} />
                                </button>

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
                    dialog.tipo === 'inativar'
                        ? 'inativar'
                        : 'reativar'
                } esta fazenda?`}
                onConfirm={confirmarDialog}
                onCancel={fecharDialog}
            />

        </PageLayout>
    )
}