import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import {
    listarTurmasTerceirizadas,
    listarTurmasTerceirizadasInativas,
    excluirTurmaTerceirizada,
    reativarTurmaTerceirizada
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
import { Pencil, Power } from 'lucide-react'
import SkeletonCard from '../components/SkeletonCard'

export default function TurmasTerceirizadas() {

    const navigate = useNavigate()
    const [busca, setBusca] = useState('')

    const {
        dados,
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
        listarAtivos: listarTurmasTerceirizadas,
        listarInativos: listarTurmasTerceirizadasInativas,
        inativar: excluirTurmaTerceirizada,
        reativar: reativarTurmaTerceirizada,
        entityName: 'Turma'
    })

    const lista = Array.isArray(dados) ? dados : []

    const filtrados = lista.filter(t =>
        String(t.nome || '')
            .toLowerCase()
            .includes(busca.toLowerCase())
    )

    return (
        <PageLayout title="Turmas Terceirizadas">

            <CrudToolbar
                busca={busca}
                setBusca={setBusca}
                onNovo={() => navigate('/turmas-terceirizadas/novo')}
                labelNovo="Nova Turma"
                mostrarInativos={mostrarInativos}
                setMostrarInativos={setMostrarInativos}
            />

            <Alert type="error" message={erro} />

            {loading && (
                <>
                    <SkeletonCard />
                    <SkeletonCard />
                </>
            )}

            {!loading && filtrados.length === 0 && (
                <EmptyState message="Nenhuma turma encontrada." />
            )}

            <CrudCardList>
                {filtrados.map(t => (
                    <CrudCard
                        key={t.id}
                        title={t.nome}
                        subtitle={`Responsável: ${t.responsavel || '-'}`}
                        ativo={!mostrarInativos}
                        disabled={mostrarInativos}
                        onClick={() =>
                            !mostrarInativos &&
                            navigate(`/turmas-terceirizadas/${t.id}/editar`)
                        }
                        actions={
                            <>
                                {!mostrarInativos && (
                                    <button
                                        className="icon-button edit"
                                        onClick={() =>
                                            navigate(`/turmas-terceirizadas/${t.id}/editar`)
                                        }
                                    >
                                        <Pencil size={18} />
                                    </button>
                                )}

                                {!mostrarInativos ? (
                                    <button
                                        className="icon-button danger"
                                        onClick={() => abrirDialog('inativar', t.id)}
                                    >
                                        <Power size={18} />
                                    </button>
                                ) : (
                                    <button
                                        className="icon-button success"
                                        onClick={() => abrirDialog('reativar', t.id)}
                                    >
                                        <Power size={18} />
                                    </button>
                                )}
                            </>
                        }
                    />
                ))}
            </CrudCardList>

            <Pagination page={page} totalPages={totalPages} onChange={setPage} />

            <ConfirmDialog
                open={dialog.open}
                title="Confirmar operação"
                message="Deseja realmente alterar este registro?"
                onConfirm={confirmarDialog}
                onCancel={fecharDialog}
            />

        </PageLayout>
    )
}