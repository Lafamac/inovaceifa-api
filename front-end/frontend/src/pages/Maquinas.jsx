import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import {
    listarMaquinas,
    listarMaquinasInativas,
    excluirMaquina,
    reativarMaquina
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
import SkeletonCard from '../components/SkeletonCard'
import { Pencil, Power, Eye } from 'lucide-react'

export default function Maquinas() {
    const navigate = useNavigate()
    const [busca, setBusca] = useState('')

    const {
        dados: maquinas,
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
        listarAtivos: listarMaquinas,
        listarInativos: listarMaquinasInativas,
        inativar: excluirMaquina,
        reativar: reativarMaquina,
        entityName: 'Máquina'
    })

    const filtradas = maquinas.filter(m =>
        m.nome?.toLowerCase().includes(busca.toLowerCase())
    )

    return (
        <PageLayout title="Máquinas">

            <CrudToolbar
                busca={busca}
                setBusca={setBusca}
                onNovo={() => navigate('/maquinas/novo')}
                labelNovo="Nova Máquina"
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
                            ? 'Nenhuma máquina inativa encontrada.'
                            : 'Nenhuma máquina ativa encontrada.'
                    }
                />
            )}

            <CrudCardList>
                {filtradas.map(m => (
                    <CrudCard
                        key={m.id}
                        title={m.nome}
                        subtitle={`${m.marca || '-'} / ${m.modelo || '-'}`}
                        ativo={!mostrarInativos}
                        disabled={mostrarInativos}
                        onClick={() =>
                            !mostrarInativos &&
                            navigate(`/maquinas/${m.id}`)
                        }
                        actions={
                            <>
                                {!mostrarInativos && (
                                    <>
                                        <button
                                            className="icon-button"
                                            onClick={() =>
                                                navigate(`/maquinas/${m.id}`)
                                            }
                                            title="Ver Detalhes"
                                        >
                                            <Eye size={18} />
                                        </button>

                                        <button
                                            className="icon-button edit"
                                            onClick={() =>
                                                navigate(`/maquinas/${m.id}/editar`)
                                            }
                                            title="Editar"
                                        >
                                            <Pencil size={18} />
                                        </button>
                                    </>
                                )}

                                {!mostrarInativos ? (
                                    <button
                                        className="icon-button danger"
                                        onClick={() =>
                                            abrirDialog('inativar', m.id)
                                        }
                                        title="Inativar"
                                    >
                                        <Power size={18} />
                                    </button>
                                ) : (
                                    <button
                                        className="icon-button success"
                                        onClick={() =>
                                            abrirDialog('reativar', m.id)
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
                } esta máquina?`}
                onConfirm={confirmarDialog}
                onCancel={fecharDialog}
            />

        </PageLayout>
    )
}