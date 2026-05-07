import { useState, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import { useQueryClient } from '@tanstack/react-query'
import {
  listarSafraTalhoes,
  listarSafraTalhoesInativos,
  excluirSafraTalhao,
  reativarSafraTalhao
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
import { Pencil, Power, ClipboardList } from 'lucide-react'
import SkeletonCard from '../components/SkeletonCard'

export default function SafraTalhoes() {

  const navigate = useNavigate()
  const queryClient = useQueryClient()
  const [busca, setBusca] = useState('')

  useEffect(() => {
    queryClient.invalidateQueries(['safra-talhoes'])
  }, [queryClient])

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
    listarAtivos: listarSafraTalhoes,
    listarInativos: listarSafraTalhoesInativos,
    inativar: excluirSafraTalhao,
    reativar: reativarSafraTalhao,
    entityName: 'Safra Talhão'
  })

  const lista = Array.isArray(dados) ? dados : []

  const filtrados = lista.filter(f =>
    String(f.talhao?.nome || f.talhao?.id || '')
      .toLowerCase()
      .includes(busca.toLowerCase())
  )

  return (
    <PageLayout title="Safra Talhões">

      <CrudToolbar
        busca={busca}
        setBusca={setBusca}
        onNovo={() => navigate('/safra-talhoes/novo')}
        labelNovo="Novo Safra Talhão"
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
        <EmptyState message="Nenhum registro encontrado." />
      )}

      <CrudCardList>
        {filtrados.map(f => (
          <CrudCard
            key={f.id}
            title={f.talhao?.nome || `Talhão ID: ${f.talhao?.id}`}
            subtitle={`
              Área: ${f.areaUtilizada || 0} ha |
              Produção: ${f.producaoReal || 0} sc |
              R$ ${(f.producaoReal || 0) * (f.precoSaca || 0)}
            `}
            ativo={!mostrarInativos}
            disabled={mostrarInativos}
            onClick={() =>
              !mostrarInativos &&
              navigate(`/safra-talhoes/${f.id}/editar`)
            }
            actions={
              <>
                <button
                  title="Editar"
                  className="icon-button edit"
                  onClick={(e) => {
                    e.stopPropagation()
                    navigate(`/safra-talhoes/${f.id}/editar`)
                  }}
                >
                  <Pencil size={18} />
                </button>

                <button
                  title={
                    f.possuiOs
                      ? 'Planejamento já possui OS'
                      : 'Planejar'
                  }
                  className="icon-button edit"
                  disabled={f.possuiOs}
                  onClick={(e) => {
                    e.stopPropagation()
                    if (!f.possuiOs) {
                      navigate(`/planejamento/novo?safraTalhaoId=${f.id}`)
                    }
                  }}
                >
                  <ClipboardList size={18} />
                </button>

                {!mostrarInativos ? (
                  <button
                    title="Inativar"
                    className="icon-button danger"
                    onClick={(e) => {
                      e.stopPropagation()
                      abrirDialog('inativar', f.id)
                    }}
                  >
                    <Power size={18} />
                  </button>
                ) : (
                  <button
                    title="Reativar"
                    className="icon-button success"
                    onClick={(e) => {
                      e.stopPropagation()
                      abrirDialog('reativar', f.id)
                    }}
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
        } este registro?`}
        onConfirm={confirmarDialog}
        onCancel={fecharDialog}
      />

    </PageLayout>
  )
}