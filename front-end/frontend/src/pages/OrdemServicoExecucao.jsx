import { useParams, useNavigate } from 'react-router-dom'
import PageLayout from '../components/PageLayout'

// ✅ React Query
import { useQuery } from '@tanstack/react-query'
import { obterOrdemServico, finalizarOrdemServico, criarOperacaoTalhao } from '../api/api'

export default function OrdemServicoExecucao() {

  const { id } = useParams()
  const navigate = useNavigate()

  const { data: ordemResponse, refetch } = useQuery({
    queryKey: ['ordem-servico', id],
    queryFn: () => obterOrdemServico(id)
  })

  const ordem = ordemResponse?.data?.data || null

  async function handleFinalizar() {
    try {
      const response = await finalizarOrdemServico(id)
      if (!response?.ok) return

      refetch()
    } catch (err) {
      console.error(err)
    }
  }

  // 🔥 NOVO — CRIAR OPERAÇÃO
  async function criarOperacao() {
    try {

      if (!ordem) return

      const res = await criarOperacaoTalhao({
        ordemServicoId: Number(id),
        safraTalhaoId: ordem?.talhoes?.[0]?.id, // 🔥 usa primeiro talhão
        areaTrabalhada: 0,
        dataExecucao: new Date().toISOString().split('T')[0]
      })

      const opId = res.data?.data?.id

      if (!opId) {
        console.error('Erro ao criar operação')
        return
      }

      navigate(`/operacoes/${opId}`)

    } catch (err) {
      console.error(err)
    }
  }

  return (
    <PageLayout
      title={`Execução da OS ${id}`}
      showBack
      backTo={`/ordens-servico/${id}`}
    >

      {/* 🔥 NOVO BOTÃO */}
      <div className="card" style={{ marginBottom: 20 }}>
        <button
          className="add-btn"
          onClick={criarOperacao}
        >
          Iniciar Operação →
        </button>
      </div>

      {/* 🔥 RESUMO DA OS */}
      {ordem && (
        <div className="card" style={{ marginBottom: 20 }}>
          <h3>Resumo da Ordem de Serviço</h3>

          <p><strong>Operação:</strong> {ordem.operacaoNome}</p>
          <p><strong>Status:</strong> {ordem.status}</p>
          <p><strong>Data início:</strong> {ordem.dataInicio || '-'}</p>
          <p><strong>Data fim:</strong> {ordem.dataFim || '-'}</p>

          {/* 🔥 CUSTO (se existir no backend) */}
          {ordem.custoTotal && (
            <p>
              <strong>Custo Total:</strong>{' '}
              R$ {Number(ordem.custoTotal).toLocaleString('pt-BR')}
            </p>
          )}
        </div>
      )}

      {/* INSUMOS */}
      <div className="card" style={{ marginBottom: 20 }}>
        <h3>Insumos</h3>

        <button
          className="add-btn"
          onClick={() => navigate(`/ordens-servico/${id}/insumos`)}
        >
          Lançar / Ajustar Insumos →
        </button>
      </div>

      {/* MÁQUINAS */}
      <div className="card" style={{ marginBottom: 20 }}>
        <h3>Máquinas</h3>

        <button
          className="add-btn"
          onClick={() => navigate(`/ordens-servico/${id}/maquinas`)}
        >
          Lançar / Ajustar Máquinas →
        </button>
      </div>

      {/* FUNCIONÁRIOS */}
      <div className="card" style={{ marginBottom: 20 }}>
        <h3>Mão de Obra</h3>

        <button
          className="add-btn"
          onClick={() => navigate(`/ordens-servico/${id}/funcionarios`)}
        >
          Lançar / Ajustar Funcionários →
        </button>
      </div>

      {/* 🔥 FINALIZAR OS */}
      {ordem?.status !== 'FINALIZADA' && (
        <div className="card">
          <button
            className="add-btn"
            style={{ background: '#16a34a' }}
            onClick={handleFinalizar}
          >
            Finalizar Ordem de Serviço ✔
          </button>
        </div>
      )}

    </PageLayout>
  )
}