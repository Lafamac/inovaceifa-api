import { useEffect, useState } from 'react'
import { useParams, useNavigate } from 'react-router-dom'

import PageLayout from '../components/PageLayout'
import Alert from '../components/Alert'
import SkeletonCard from '../components/SkeletonCard'

import { obterComparativoDetalhado } from '../api/api'

export default function PlanejamentoComparativoDetalhado() {

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
      const res = await obterComparativoDetalhado(id)

      if (!res.data?.success) {
        setErro('Erro ao carregar comparativo detalhado')
        return
      }

      setDados(res.data.data)

    } catch (err) {
      console.error(err)
      setErro('Erro ao carregar comparativo detalhado')
    } finally {
      setLoading(false)
    }
  }

  function diff(prev, real) {
    return (real || 0) - (prev || 0)
  }

  function renderLista(lista, labelNome) {
    if (!lista || lista.length === 0) {
      return <p>Nenhum registro</p>
    }

    return lista.map((item, index) => (
      <div key={index} className="card">
        <strong>{item[labelNome]}</strong>

        <p>Previsto: {item.previsto}</p>
        <p>Realizado: {item.realizado}</p>
        <p>Diferença: {diff(item.previsto, item.realizado)}</p>
      </div>
    ))
  }

  return (
    <PageLayout
      title="Comparativo Detalhado"
      showBack
      backTo={`/planejamento/${id}/comparativo`}
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
          <div>
            <h3>Insumos</h3>
            {renderLista(dados.insumos, 'produto')}
          </div>

          {/* 🔹 MÁQUINAS */}
          <div>
            <h3>Máquinas</h3>
            {renderLista(dados.maquinas, 'maquina')}
          </div>

          {/* 🔹 FUNCIONÁRIOS */}
          <div>
            <h3>Funcionários</h3>
            {renderLista(dados.funcionarios, 'nome')}
          </div>

        </div>
      )}

      {/* 🔥 FINAL */}
      {!loading && dados && (
        <div style={{ marginTop: 20 }}>
          <button
            className="add-btn"
            onClick={() => navigate(`/ordens-servico/novo?planejamentoId=${id}`)}
          >
            Gerar Ordem de Serviço →
          </button>
        </div>
      )}

    </PageLayout>
  )
}