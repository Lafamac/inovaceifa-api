import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../auth/AuthContext'

import {
  listarOrdensServico,
  excluirOrdemServico
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

// ✅ React Query (preparação)
import { useQueryClient } from '@tanstack/react-query'

export default function OrdemServicoList() {

  const navigate = useNavigate()
  const { user } = useAuth()
  const queryClient = useQueryClient()

  const [busca, setBusca] = useState('')

  const {
    dados,
    loading,
    erro,
    page,
    totalPages,
    setPage,
    abrirDialog,
    dialog,
    fecharDialog,
    confirmarDialog
  } = useAtivoInativoCrud({
    listarAtivos: listarOrdensServico,
    listarInativos: listarOrdensServico,
    inativar: excluirOrdemServico,
    reativar: null,
    entityName: 'Ordem de Serviço'
  })

  const lista = Array.isArray(dados?.content)
    ? dados.content
    : Array.isArray(dados)
      ? dados
      : []

  const filtrados = lista.filter(o =>
    String(o.nrOs || '')
      .toLowerCase()
      .includes(busca.toLowerCase())
  )

  const podeCriar = user?.perfilId === 2

  return (
    <PageLayout title="Ordens de Serviço">

      <CrudToolbar
        busca={busca}
        setBusca={setBusca}
        onNovo={() => navigate('/ordens-servico/novo')}
        labelNovo="Nova OS"
        mostrarInativos={false}
        setMostrarInativos={() => {}}
        esconderToggle
        esconderInativos
        esconderFiltro
        esconderInativo
        esconderSwitch
        esconderBusca={false}
        esconderNovo={!podeCriar}
      />

      <Alert type="error" message={erro} />

      {loading && (
        <>
          <SkeletonCard />
          <SkeletonCard />
        </>
      )}

      {!loading && filtrados.length === 0 && (
        <EmptyState message="Nenhuma ordem de serviço encontrada." />
      )}

      <CrudCardList>
        {filtrados.map(o => {

          const nomesTalhoes = Array.isArray(o.talhoes)
            ? o.talhoes.map(t => t.nome)
            : []

          return (
            <CrudCard
              key={o.id}
              title={`OS: ${o.nrOs}`}
              subtitle={`Operação: ${o.operacaoNome || '-'}`}

              description={
                <>
                  <div>Status: {o.status || '-'}</div>
                  <div>Início: {o.dataInicio || '-'}</div>
                  <div>Fim: {o.dataFim || '-'}</div>

                  {nomesTalhoes.length > 0 && (
                    <div style={{ marginTop: 5 }}>
                      <strong>Talhões:</strong>
                      <div style={{ marginTop: 4 }}>
                        {nomesTalhoes.map((nome, i) => (
                          <span
                            key={i}
                            style={{
                              display: 'inline-block',
                              padding: '2px 8px',
                              marginRight: 5,
                              marginBottom: 5,
                              background: '#eef2ff',
                              borderRadius: 6,
                              fontSize: 12
                            }}
                          >
                            {nome}
                          </span>
                        ))}
                      </div>
                    </div>
                  )}
                </>
              }

              onClick={() => navigate(`/ordens-servico/${o.id}`)}

              actions={
                <>
                  <button
                    className="icon-button edit"
                    onClick={(e) => {
                      e.stopPropagation()
                      navigate(`/ordens-servico/${o.id}`)
                    }}
                    title="Editar OS"
                  >
                    <Pencil size={18} />
                  </button>

                  {podeCriar && (
                    <button
                      className="icon-button danger"
                      onClick={(e) => {
                        e.stopPropagation()
                        abrirDialog('inativar', o.id)
                      }}
                      title="Excluir OS"
                    >
                      <Power size={18} />
                    </button>
                  )}
                </>
              }
            />
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
        message="Deseja realmente excluir esta ordem de serviço?"
        onConfirm={confirmarDialog}
        onCancel={fecharDialog}
      />

    </PageLayout>
  )
}