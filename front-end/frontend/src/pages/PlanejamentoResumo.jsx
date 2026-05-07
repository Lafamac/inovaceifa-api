import { useEffect, useState } from 'react'
import { useParams, useNavigate } from 'react-router-dom'

import PageLayout from '../components/PageLayout'
import Alert from '../components/Alert'
import SkeletonCard from '../components/SkeletonCard'

import { obterResumoPlanejamento } from '../api/api'

export default function PlanejamentoResumo() {

  const { id } = useParams()
  const navigate = useNavigate()

  const [resumo, setResumo] = useState(null)
  const [loading, setLoading] = useState(true)
  const [erro, setErro] = useState('')

  useEffect(() => {
    carregar()
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [id])

  async function carregar() {
    try {
      const res = await obterResumoPlanejamento(id)

      if (!res.data?.success) {
        setErro('Erro ao carregar resumo')
        return
      }

      setResumo(res.data.data)

    } catch (err) {
      console.error(err)
      setErro('Erro ao carregar resumo')
    } finally {
      setLoading(false)
    }
  }

  return (
    <PageLayout
      title="Resumo do Planejamento"
      showBack
      backTo={`/planejamento/${id}/funcionarios`}
    >

      {erro && <Alert type="error" message={erro} />}

      {loading && (
        <>
          <SkeletonCard />
          <SkeletonCard />
        </>
      )}

      {!loading && resumo && (

        <div className="card-list">

          {/* 🔹 CUSTOS */}
          <div className="card">
            <strong>Custos</strong>
            <p>Insumos: R$ {resumo.custoInsumos}</p>
            <p>Máquinas: R$ {resumo.custoMaquina}</p>
            <p>Mão de obra: R$ {resumo.custoMaoObra}</p>
            <p>Combustível: R$ {resumo.custoCombustivel}</p>
          </div>

          {/* 🔹 TOTAL */}
          <div className="card">
            <strong>Total</strong>
            <p>Custo total: R$ {resumo.custoTotal}</p>
          </div>

          {/* 🔹 INDICADORES */}
          <div className="card">
            <strong>Indicadores</strong>
            <p>Custo por hectare: R$ {resumo.custoPorHectare}</p>
            <p>Custo por saca: R$ {resumo.custoPorSaca}</p>
          </div>

        </div>
      )}

      {/* 🔥 AÇÃO FINAL (AINDA NÃO VAMOS PRA OS) */}
      {!loading && resumo && (
        <div style={{ marginTop: 20 }}>
          <button
            className="add-btn"
            onClick={() => navigate(`/planejamento/${id}/comparativo`)}
          >
            Próximo →
          </button>
        </div>
      )}

    </PageLayout>
  )
}