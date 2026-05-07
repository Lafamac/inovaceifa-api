import { useEffect, useState } from 'react'
import { useToast } from '../context/ToastContext'
import { useQuery } from '@tanstack/react-query'

export default function useAtivoInativoCrud({
    listarAtivos,
    listarInativos,
    inativar,
    reativar,
    entityName = 'Registro',
    initialPageSize = 10
}) {
    const { showToast } = useToast()

    const [mostrarInativosState, setMostrarInativosState] = useState(false)

    const [page, setPage] = useState(0)
    const [size] = useState(initialPageSize)

    const setMostrarInativos = (val) => {
        setMostrarInativosState(val)
        setPage(0)
    }

    const [dialog, setDialog] = useState({ open: false, tipo: null, id: null })

    const {
        data: response,
        isLoading: loading,
        refetch
    } = useQuery({
        queryKey: [
            entityName,
            mostrarInativosState,
            page,
            size
        ],
        queryFn: () =>
            mostrarInativosState
                ? listarInativos(page, size)
                : listarAtivos(page, size)
    })

    // 🔥 CORREÇÃO AQUI: erro derivado diretamente para evitar renderizações em cascata
    const erro = response && (!response.ok || !response.data)
        ? (response?.data?.message || 'Erro ao carregar dados')
        : ''

    useEffect(() => {
        if (erro) {
            showToast(erro, 'error')
        }
    }, [erro, showToast])

    const dadosApi = response?.data?.data
    let dados = []
    let totalPages = 0
    if (response?.ok && dadosApi) {
        if (Array.isArray(dadosApi.content)) {
            dados = dadosApi.content
            totalPages = dadosApi.totalPages || 0
        } else if (Array.isArray(dadosApi)) {
            dados = dadosApi
        }
    }

    function abrirDialog(tipo, id) {
        setDialog({ open: true, tipo, id })
    }

    function fecharDialog() {
        setDialog({ open: false, tipo: null, id: null })
    }

    async function confirmarDialog() {
        try {
            const response =
                dialog.tipo === 'inativar'
                    ? await inativar(dialog.id)
                    : await reativar(dialog.id)

            if (!response?.ok) {
                showToast(
                    response?.data?.message || 'Erro na operação',
                    'error'
                )
                return
            }

            showToast(
                dialog.tipo === 'inativar'
                    ? `${entityName} inativado com sucesso`
                    : `${entityName} reativado com sucesso`,
                'success'
            )

            fecharDialog()
            refetch()

        } catch (err) {
            console.error(err)
            showToast('Erro ao executar operação', 'error')
        }
    }

    return {
        dados,
        loading,
        erro,
        mostrarInativos: mostrarInativosState,
        setMostrarInativos,
        page,
        totalPages,
        setPage,
        abrirDialog,
        dialog,
        fecharDialog,
        confirmarDialog
    }
}