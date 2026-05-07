import { useState } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { useAuth } from '../auth/AuthContext'

import PageLayout from '../components/PageLayout'
import Alert from '../components/Alert'

import {
  obterOrdemServico,
  finalizarOrdemServico,
  obterComparativoPlanejamento
} from '../api/api'

// ✅ React Query
import { useQuery } from '@tanstack/react-query'

export default function OrdemServicoDetalhe() {

  const { id } = useParams()
  const navigate = useNavigate()
  const { user } = useAuth()

  const [erro, setErro] = useState('')

  const podeExecutar = user?.perfilId !== 2
  const podeAuditoria = user?.perfilId === 2

  // ✅ QUERY 1 - OS
  const {
    data: ordemResponse,
    isLoading: loading,
    refetch
  } = useQuery({
    queryKey: ['ordem-servico', id],
    queryFn: () => obterOrdemServico(id)
  })

  const ordem = ordemResponse?.data?.data || null

  // ✅ QUERY 2 - COMPARATIVO (dependente)
  const {
    data: comparativoResponse
  } = useQuery({
    queryKey: ['comparativo-planejamento', ordem?.planejamentoOperacaoId],
    queryFn: () => obterComparativoPlanejamento(ordem.planejamentoOperacaoId),
    enabled: !!ordem?.planejamentoOperacaoId
  })

  const comparativo = comparativoResponse?.data?.data || null

  function diff(prev, real) {
    return (real || 0) - (prev || 0)
  }

  async function finalizar() {

    if (!podeExecutar) {
      setErro('Você não tem permissão para finalizar')
      return
    }

    try {
      const res = await finalizarOrdemServico(id)

      if (!res.data?.success) {
        setErro('Erro ao finalizar OS')
        return
      }

      alert('Ordem finalizada')

      refetch()

    } catch (err) {
      console.error(err)
      setErro('Erro ao finalizar OS')
    }
  }

  // 🔥 NOVA FUNÇÃO PDF
  const imprimir = () => {
    window.open(`http://localhost:8080/ordem-servico/${id}/pdf`, '_blank')
  }

  if (loading) {
    return <PageLayout title="Carregando..." />
  }

  if (!ordem) {
    return (
      <PageLayout title="Ordem de Serviço">
        <Alert type="error" message="OS não encontrada" />
      </PageLayout>
    )
  }

  const podeEditar = ordem.status !== 'FINALIZADA'

  return (
    <PageLayout
      title={`OS ${ordem.nrOs}`}
      showBack
      backTo="/ordens-servico"
    >

      {erro && <Alert type="error" message={erro} />}

      <div className="card">
        <p><strong>Status:</strong> {ordem.status}</p>
        <p><strong>Operação:</strong> {ordem.operacaoNome}</p>
        <p>
          <strong>Talhões:</strong>{' '}
          {ordem.talhoes?.length
            ? ordem.talhoes.map(t => t.nome).join(', ')
            : '-'}
        </p>
      </div>

      {comparativo && (
        <div className="card-list">

          <div className="card">
            <strong>Insumos</strong>
            <p>Previsto: {comparativo.insumosPrevisto}</p>
            <p>Realizado: {comparativo.insumosRealizado}</p>
            <p>Diferença: {diff(comparativo.insumosPrevisto, comparativo.insumosRealizado)}</p>
          </div>

          <div className="card">
            <strong>Máquinas</strong>
            <p>Previsto: {comparativo.maquinasPrevisto}</p>
            <p>Realizado: {comparativo.maquinasRealizado}</p>
            <p>Diferença: {diff(comparativo.maquinasPrevisto, comparativo.maquinasRealizado)}</p>
          </div>

          <div className="card">
            <strong>Mão de Obra</strong>
            <p>Previsto: {comparativo.maoObraPrevisto}</p>
            <p>Realizado: {comparativo.maoObraRealizado}</p>
            <p>Diferença: {diff(comparativo.maoObraPrevisto, comparativo.maoObraRealizado)}</p>
          </div>

          <div className="card">
            <strong>Total</strong>
            <p>Previsto: {comparativo.totalPrevisto}</p>
            <p>Realizado: {comparativo.totalRealizado}</p>
            <p>Diferença: {diff(comparativo.totalPrevisto, comparativo.totalRealizado)}</p>
          </div>

        </div>
      )}

      <div style={{ marginTop: 20, display: 'flex', gap: '10px', flexWrap: 'wrap' }}>

        {podeExecutar && podeEditar && (
          <button
            className="add-btn"
            onClick={() => navigate(`/ordens-servico/${id}/execucao`)}
          >
            Executar OS →
          </button>
        )}

        {podeExecutar && podeEditar && (
          <button
            className="add-btn"
            onClick={finalizar}
          >
            Finalizar OS
          </button>
        )}

        {podeAuditoria && (
          <button
            className="add-btn"
            onClick={() => navigate(`/ordens-servico/${id}/auditoria`)}
          >
            Auditoria →
          </button>
        )}

        <button
          className="add-btn"
          onClick={() => navigate(`/ordens-servico/${id}/turmas`)}
        >
          Turmas
        </button>

        {/* 🔥 ALTERAÇÃO AQUI */}
        <button
          className="add-btn"
          onClick={imprimir}
        >
          Imprimir OS 🧾
        </button>

      </div>

    </PageLayout>
  )
}