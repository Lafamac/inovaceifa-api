import { useEffect } from 'react'
import { useAuth } from '../auth/AuthContext'

import PageLayout from '../components/PageLayout'
import Alert from '../components/Alert'

import { useQuery } from '@tanstack/react-query'
import { obterDashboardSafra } from '../api/api'
import AdubacaoChart from '../components/AdubacaoChart'

export default function DashboardSafra() {

  const { safraAtiva } = useAuth()

  const safraId = safraAtiva?.id

  const { data: response, isLoading, error } = useQuery({
    queryKey: ['dashboard-safra', safraId],
    queryFn: () => obterDashboardSafra(safraId),
    enabled: !!safraId
  })

  const data = response?.data?.data || null

  function formatar(valor) {
    return Number(valor || 0).toLocaleString('pt-BR')
  }

  if (!safraId) {
    return (
      <PageLayout title="Dashboard da Safra">
        <Alert type="warning" message="Selecione uma safra ativa" />
      </PageLayout>
    )
  }

  if (isLoading) {
    return <PageLayout title="Dashboard da Safra">Carregando...</PageLayout>
  }

  if (!data) {
    return (
      <PageLayout title="Dashboard da Safra">
        <Alert type="error" message="Nenhum dado encontrado" />
      </PageLayout>
    )
  }

  return (
    <PageLayout
      title="Dashboard da Safra"
      showBack
      backTo="/menu"
    >

      {/* 🔥 CARDS PRINCIPAIS */}
      <div style={{
        display: 'grid',
        gridTemplateColumns: 'repeat(auto-fit, minmax(220px, 1fr))',
        gap: 20,
        marginBottom: 25
      }}>

        <div className="card">
          <h4>Custo Total</h4>
          <strong>R$ {formatar(data.custoTotal)}</strong>
        </div>

        <div className="card">
          <h4>Receita</h4>
          <strong>R$ {formatar(data.receitaTotal)}</strong>
        </div>

        <div className="card">
          <h4>Lucro</h4>
          <strong style={{ color: data.lucroTotal >= 0 ? '#16a34a' : '#dc2626' }}>
            R$ {formatar(data.lucroTotal)}
          </strong>
        </div>

      </div>

      {/* 🔥 INDICADORES */}
      <div style={{
        display: 'grid',
        gridTemplateColumns: 'repeat(auto-fit, minmax(220px, 1fr))',
        gap: 20,
        marginBottom: 25
      }}>

        <div className="card">
          <h4>Custo por Hectare</h4>
          <strong>R$ {formatar(data.custoPorHectare)}</strong>
        </div>

        <div className="card">
          <h4>Custo por Saca</h4>
          <strong>R$ {formatar(data.custoPorSaca)}</strong>
        </div>

        <div className="card">
          <h4>Área Total</h4>
          <strong>{formatar(data.areaTotal)} ha</strong>
        </div>

        <div className="card">
          <h4>Produção Total</h4>
          <strong>{formatar(data.producaoTotal)}</strong>
        </div>

      </div>

      {/* 🔥 DISTRIBUIÇÃO DE CUSTOS */}
      <div className="card">
        <h3>Distribuição de Custos</h3>

        <AdubacaoChart
          data={[
            { nome: 'Insumos', valor: data.custoInsumos },
            { nome: 'Máquinas', valor: data.custoMaquinas },
            { nome: 'Compras', valor: data.custoCompras },
            { nome: 'Combustível', valor: data.custoCombustivel },
            { nome: 'Mão de Obra', valor: data.custoMaoObra },
            { nome: 'Terceiros', valor: data.custoTerceiros },
            { nome: 'Administrativo', valor: data.custoAdministrativo }
          ]}
        />
      </div>

    </PageLayout>
  )
}