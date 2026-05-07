import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import {
    listarTerceirizados,
    listarTerceirizadosInativos,
    excluirTerceirizado,
    reativarTerceirizado
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

export default function Terceirizados() {
    const navigate = useNavigate()
    const [busca, setBusca] = useState('')

    const {
        dados: terceirizados,
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
        listarAtivos: listarTerceirizados,
        listarInativos: listarTerceirizadosInativos,
        inativar: excluirTerceirizado,
        reativar: reativarTerceirizado,
        entityName: 'Terceirizado'
    })

    const filtrados = terceirizados.filter(t =>
        t.nome?.toLowerCase().includes(busca.toLowerCase())
    )

    return (
        <PageLayout title="Terceirizados">
            <CrudToolbar
                busca={busca}
                setBusca={setBusca}
                onNovo={() => navigate('/terceirizados/novo')}
                labelNovo="Novo Terceirizado"
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

            {!loading && filtrados.length === 0 && (
                <EmptyState
                    message={
                        mostrarInativos
                            ? 'Nenhum terceirizado inativo encontrado.'
                            : 'Nenhum terceirizado ativo encontrado.'
                    }
                />
            )}

            <CrudCardList>
                {filtrados.map(t => (
                    <CrudCard
                        key={t.id}
                        title={t.nome}
                        subtitle={`CPF: ${t.cpf || '-'}`}
                        ativo={!mostrarInativos}
                        disabled={mostrarInativos}
                        onClick={() =>
                            !mostrarInativos &&
                            navigate(`/terceirizados/${t.id}/editar`)
                        }
                        actions={
                            <>
                                {!mostrarInativos && (
                                    <button
                                        className="icon-button edit"
                                        onClick={() =>
                                            navigate(`/terceirizados/${t.id}/editar`)
                                        }
                                        title="Editar"
                                    >
                                        <Pencil size={18} />
                                    </button>
                                )}

                                {!mostrarInativos ? (
                                    <button
                                        className="icon-button danger"
                                        onClick={() =>
                                            abrirDialog('inativar', t.id)
                                        }
                                        title="Inativar"
                                    >
                                        <Power size={18} />
                                    </button>
                                ) : (
                                    <button
                                        className="icon-button success"
                                        onClick={() =>
                                            abrirDialog('reativar', t.id)
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
                } este terceirizado?`}
                onConfirm={confirmarDialog}
                onCancel={fecharDialog}
            />
        </PageLayout>
    )
}