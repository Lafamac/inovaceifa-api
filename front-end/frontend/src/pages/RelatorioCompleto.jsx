import { useQuery } from '@tanstack/react-query'

import PageLayout from '../components/PageLayout'
import Alert from '../components/Alert'

import { obterRelatorioCompleto } from '../api/api'

export default function RelatorioCompleto() {

  const { data, isLoading } = useQuery({
    queryKey: ['relatorio-completo'],
    queryFn: obterRelatorioCompleto
  })

  const r = data?.data?.data || null

  function formatar(valor) {
    return Number(valor || 0).toLocaleString('pt-BR')
  }

  if (isLoading) {
    return <PageLayout title="Relatório">Carregando...</PageLayout>
  }

  if (!r) {
    return (
      <PageLayout title="Relatório">
        <Alert type="error" message="Erro ao carregar relatório" />
      </PageLayout>
    )
  }

  return (
    <PageLayout title="Relatório Completo da Safra">

      {/* 🔥 RESULTADO PRINCIPAL */}
      <div style={{
        display: 'grid',
        gridTemplateColumns: 'repeat(auto-fit, minmax(220px, 1fr))',
        gap: 20,
        marginBottom: 30
      }}>

        <div className="card">
          <h4>Custo Total</h4>
          <strong>R$ {formatar(r.custoTotal)}</strong>
        </div>

        <div className="card">
          <h4>Receita Total</h4>
          <strong>R$ {formatar(r.receitaTotal)}</strong>
        </div>

        <div className="card">
          <h4>Lucro</h4>
          <strong style={{ color: '#16a34a' }}>
            R$ {formatar(r.lucroTotal)}
          </strong>
        </div>

      </div>

      {/* 🔥 INDICADORES AGRÍCOLAS */}
      <div style={{
        display: 'grid',
        gridTemplateColumns: 'repeat(auto-fit, minmax(220px, 1fr))',
        gap: 20,
        marginBottom: 30
      }}>

        <div className="card">
          <h4>Área Total</h4>
          <strong>{formatar(r.areaTotal)} ha</strong>
        </div>

        <div className="card">
          <h4>Produção Total</h4>
          <strong>{formatar(r.producaoTotal)}</strong>
        </div>

        <div className="card">
          <h4>Custo / Hectare</h4>
          <strong>R$ {formatar(r.custoPorHectare)}</strong>
        </div>

        <div className="card">
          <h4>Custo / Saca</h4>
          <strong>R$ {formatar(r.custoPorSaca)}</strong>
        </div>

      </div>

      {/* 🔥 RESUMO FINAL */}
      <div className="card">

        <h3>Resultado da Safra</h3>

        <p>
          <strong>Custo total:</strong> R$ {formatar(r.custoTotal)}
        </p>

        <p>
          <strong>Receita total:</strong> R$ {formatar(r.receitaTotal)}
        </p>

        <p>
          <strong>Lucro final:</strong>{' '}
          <span style={{ color: '#16a34a', fontWeight: 'bold' }}>
            R$ {formatar(r.lucroTotal)}
          </span>
        </p>

      </div>

    </PageLayout>
  )
}