import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import {
    listarProdutos,
    listarProdutosInativos,
    excluirProduto,
    reativarProduto
} from '../api/api'

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

import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'

export default function Produtos() {
    const navigate = useNavigate()
    const [busca, setBusca] = useState('')
    const [mostrarInativos, setMostrarInativos] = useState(false)
    const [dialog, setDialog] = useState({ open: false, tipo: '', id: null })

    const queryClient = useQueryClient()

    // 🔥 QUERY PRINCIPAL (CORRIGIDA)
    const { data, isLoading, error } = useQuery({
        queryKey: ['produtos', mostrarInativos],
        queryFn: async () => {
            const res = mostrarInativos
                ? await listarProdutosInativos()
                : await listarProdutos()

            return res.data?.data?.content || res.data?.data || []
        }
    })

    // 🔥 GARANTIA DE ARRAY (ANTI-BUG)
    const produtos = Array.isArray(data) ? data : []

    // 🔥 MUTATIONS
    const inativarMutation = useMutation({
        mutationFn: excluirProduto,
        onSuccess: () => {
            queryClient.invalidateQueries(['produtos'])
        }
    })

    const reativarMutation = useMutation({
        mutationFn: reativarProduto,
        onSuccess: () => {
            queryClient.invalidateQueries(['produtos'])
        }
    })

    function abrirDialog(tipo, id) {
        setDialog({ open: true, tipo, id })
    }

    function fecharDialog() {
        setDialog({ open: false, tipo: '', id: null })
    }

    function confirmarDialog() {
        if (dialog.tipo === 'inativar') {
            inativarMutation.mutate(dialog.id)
        } else {
            reativarMutation.mutate(dialog.id)
        }
        fecharDialog()
    }

    // 🔥 FILTRO (AGORA SEGURO)
    const filtrados = produtos.filter(p => {
        if (!busca) return true
        return (p.nome || '')
            .toLowerCase()
            .includes(busca.toLowerCase())
    })

    return (
        <PageLayout title="Produtos">

            <CrudToolbar
                busca={busca}
                setBusca={setBusca}
                onNovo={() => navigate('/produtos/novo')}
                labelNovo="Novo Produto"
                mostrarInativos={mostrarInativos}
                setMostrarInativos={setMostrarInativos}
            />

            <Alert type="error" message={error?.message} />

            {isLoading && (
                <>
                    <SkeletonCard />
                    <SkeletonCard />
                    <SkeletonCard />
                </>
            )}

            {!isLoading && filtrados.length === 0 && (
                <EmptyState
                    message={
                        mostrarInativos
                            ? 'Nenhum produto inativo encontrado.'
                            : 'Nenhum produto ativo encontrado.'
                    }
                />
            )}

            <CrudCardList>
                {filtrados.map(p => (
                    <CrudCard
                        key={p.id}
                        title={p.nome}
                        subtitle={`Código: ${p.codigo || '-'} | Unidade: ${p.unidade || '-'}`}
                        ativo={!mostrarInativos}
                        disabled={mostrarInativos}
                        onClick={() =>
                            !mostrarInativos &&
                            navigate(`/produtos/${p.id}`)
                        }
                        actions={
                            <>
                                <button
                                    className="icon-button"
                                    onClick={(e) => {
                                        e.stopPropagation()
                                        navigate(`/produtos/${p.id}`)
                                    }}
                                    title="Ver Detalhe"
                                >
                                    👁
                                </button>

                                {!mostrarInativos && (
                                    <button
                                        className="icon-button edit"
                                        onClick={(e) => {
                                            e.stopPropagation()
                                            navigate(`/produtos/${p.id}/editar`)
                                        }}
                                        title="Editar"
                                    >
                                        <Pencil size={18} />
                                    </button>
                                )}

                                {!mostrarInativos ? (
                                    <button
                                        className="icon-button danger"
                                        onClick={(e) => {
                                            e.stopPropagation()
                                            abrirDialog('inativar', p.id)
                                        }}
                                        title="Inativar"
                                    >
                                        <Power size={18} />
                                    </button>
                                ) : (
                                    <button
                                        className="icon-button success"
                                        onClick={(e) => {
                                            e.stopPropagation()
                                            abrirDialog('reativar', p.id)
                                        }}
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
                page={1}
                totalPages={1}
                onChange={() => {}}
            />

            <ConfirmDialog
                open={dialog.open}
                title="Confirmar operação"
                message={`Deseja realmente ${
                    dialog.tipo === 'inativar' ? 'inativar' : 'reativar'
                } este produto?`}
                onConfirm={confirmarDialog}
                onCancel={fecharDialog}
            />

        </PageLayout>
    )
}