import { useEffect, useState } from 'react'
import { useParams, useNavigate } from 'react-router-dom'

import PageLayout from '../components/PageLayout'
import Alert from '../components/Alert'
import SkeletonCard from '../components/SkeletonCard'

import { obterComparativoPlanejamento } from '../api/api'

export default function PlanejamentoComparativo() {

  const { id } = useParams()
  const navigate = useNavigate()

  const [dados, setDados] = useState(null)
  const [loading, setLoading] = useState(true)
  const [erro, setErro] = useState('')

  useEffect(() => {
    carregar()
  }, [])

  async function carregar() {
    try {
      const res = await obterComparativoPlanejamento(id)

      if (!res.data?.success) {
        setErro('Erro ao carregar comparativo')
        return
      }

      setDados(res.data.data)

    } catch (err) {
      console.error(err)
      setErro('Erro ao carregar comparativo')
    } finally {
      setLoading(false)
    }
  }

  function diferenca(previsto, realizado) {
    return (realizado || 0) - (previsto || 0)
  }

  return (
    <PageLayout
      title="Comparativo do Planejamento"
      showBack
      backTo={`/planejamento/${id}/resumo`}
    >

      {erro && <Alert type="error" message={erro} />}

      {loading && (
        <>
          <SkeletonCard />
          <SkeletonCard />
        </>
      )}

      {!loading && dados && (

        <div className="card-list">

          {/* 🔹 INSUMOS */}
          <div className="card">
            <strong>Insumos</strong>
            <p>Previsto: R$ {dados.insumosPrevisto}</p>
            <p>Realizado: R$ {dados.insumosRealizado}</p>
            <p>Diferença: R$ {diferenca(dados.insumosPrevisto, dados.insumosRealizado)}</p>
          </div>

          {/* 🔹 MÁQUINAS */}
          <div className="card">
            <strong>Máquinas</strong>
            <p>Previsto: R$ {dados.maquinasPrevisto}</p>
            <p>Realizado: R$ {dados.maquinasRealizado}</p>
            <p>Diferença: R$ {diferenca(dados.maquinasPrevisto, dados.maquinasRealizado)}</p>
          </div>

          {/* 🔹 MÃO DE OBRA */}
          <div className="card">
            <strong>Mão de Obra</strong>
            <p>Previsto: R$ {dados.maoObraPrevisto}</p>
            <p>Realizado: R$ {dados.maoObraRealizado}</p>
            <p>Diferença: R$ {diferenca(dados.maoObraPrevisto, dados.maoObraRealizado)}</p>
          </div>

          {/* 🔹 TOTAL */}
          <div className="card">
            <strong>Total Geral</strong>
            <p>Previsto: R$ {dados.totalPrevisto}</p>
            <p>Realizado: R$ {dados.totalRealizado}</p>
            <p>Diferença: R$ {diferenca(dados.totalPrevisto, dados.totalRealizado)}</p>
          </div>

        </div>
      )}

      {!loading && dados && (
        <div style={{ marginTop: 20 }}>
          <button
            className="add-btn"
            onClick={() => navigate(`/planejamento/${id}/comparativo-detalhado`)}
          >
            Ver Detalhado →
          </button>
        </div>
      )}

    </PageLayout>
  )
}