import { useState } from 'react'
import { useNavigate } from 'react-router-dom'

import {
  listarContasPagar,
  deletarContaPagar
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

import { Pencil, CheckCircle } from 'lucide-react' // 🔥 ALTERADO

export default function ContasPagar() {

  const navigate = useNavigate()
  const [busca, setBusca] = useState('')
  const [mostrarBaixadas, setMostrarBaixadas] = useState(false)

  const {
    dados: contas,
    loading,
    erro,
    page,
    totalPages,
    setPage,
    dialog,
    abrirDialog,
    fecharDialog,
    confirmarDialog
  } = useAtivoInativoCrud({
    listarAtivos: listarContasPagar,
    listarInativos: listarContasPagar,
    inativar: deletarContaPagar,
    reativar: null,
    entityName: 'Conta'
  })

  const filtradas = contas
    .filter(c =>
      c.favorecido?.toLowerCase().includes(busca.toLowerCase())
    )
    .filter(c =>
      mostrarBaixadas ? c.baixada === 'S' : c.baixada === 'N'
    )

  function formatarMoeda(valor) {
    return Number(valor || 0).toLocaleString('pt-BR', {
      style: 'currency',
      currency: 'BRL'
    })
  }

  function isVencida(data) {
    if (!data) return false

    const hoje = new Date()
    const venc = new Date(data)

    hoje.setHours(0, 0, 0, 0)
    venc.setHours(0, 0, 0, 0)

    return venc < hoje
  }

  return (
    <PageLayout title="Contas a Pagar">

      <CrudToolbar
        busca={busca}
        setBusca={setBusca}
        onNovo={() => navigate('/financeiro/contas-pagar/novo')}
        labelNovo="Nova Conta"
        mostrarInativos={mostrarBaixadas}
        setMostrarInativos={setMostrarBaixadas}
        labelToggle="Mostrar baixadas"
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
        <EmptyState message="Nenhuma conta encontrada." />
      )}

      <CrudCardList>
        {filtradas.map(c => {

          const vencida =
            c.baixada === 'N' && isVencida(c.dataVencimento)

          return (
            <CrudCard
              key={c.id}
              title={c.favorecido}
              subtitle={`${c.descricaoDespesa || '-'} | ${c.dataVencimento} | ${formatarMoeda(c.vlrReal)}`}
              ativo={c.baixada === 'N'}
              disabled={c.baixada === 'S'}
              style={{
                borderLeft: vencida ? '4px solid #dc2626' : undefined
              }}
              actions={
                <>
                  {/* EDITAR */}
                  <button
                    className="icon-button edit"
                    onClick={() => navigate(`/financeiro/contas-pagar/${c.id}/editar`)}
                    title="Editar"
                  >
                    <Pencil size={18} />
                  </button>

                  {/* BAIXAR */}
                  {c.baixada === 'N' && (
                    <button
                      className="icon-button"
                      onClick={() => navigate(`/financeiro/contas-pagar/${c.id}/pagar`)}
                      title="Baixar"
                    >
                      <CheckCircle size={18} />
                    </button>
                  )}
                </>
              }
            >
              <div style={{ fontSize: 18, fontWeight: 'bold', color: '#16a34a' }}>
                {formatarMoeda(Number(c.vlrReal || 0))}
              </div>
              <div><strong>Status:</strong> {c.baixada === 'S' ? 'Pago' : 'Em aberto'}</div>
            </CrudCard>
          )
        })}
      </CrudCardList>

      <Pagination
        page={page}
        totalPages={totalPages}
        onChange={setPage}
      />

      <ConfirmDialog
        open={dialog.open}
        title="Confirmar exclusão"
        message="Deseja excluir esta conta?"
        onConfirm={confirmarDialog}
        onCancel={fecharDialog}
      />

    </PageLayout>
  )
}