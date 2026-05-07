import { useEffect, useState } from 'react'

import Alert from '../components/Alert'

import {
  resumoLancamentos
} from '../api/api'

import { DollarSign, CheckCircle, AlertCircle, List } from 'lucide-react'

export default function ResumoFinanceiro() {

  const [dados, setDados] = useState(null)
  const [erro, setErro] = useState('')

  useEffect(() => {
    carregar()
  }, [])

  async function carregar() {
    try {
      const res = await resumoLancamentos()
      const d = res.data?.data || {}
      setDados(d)
    } catch (e) {
      console.error(e)
      setErro('Erro ao carregar resumo financeiro')
    }
  }

  function formatarMoeda(valor) {
    return Number(valor || 0).toLocaleString('pt-BR', {
      style: 'currency',
      currency: 'BRL'
    })
  }

  function formatarNumero(valor) {
    return Number(valor || 0).toLocaleString('pt-BR')
  }

  if (erro) {
    return <Alert type="error" message={erro} />
  }

  if (!dados) return null

  return (
    <div className="grid-container">

      {/* TOTAL */}
      <div className="module-card">
        <div className="module-content">
          <h3><DollarSign size={18} /> Total Despesas</h3>
          <p>{formatarMoeda(dados.totalDespesas)}</p>
        </div>
      </div>

      {/* PAGO */}
      <div className="module-card">
        <div className="module-content">
          <h3 style={{ color: '#16a34a' }}>
            <CheckCircle size={18} /> Total Pago
          </h3>
          <p style={{ color: '#16a34a' }}>
            {formatarMoeda(dados.totalPago)}
          </p>
        </div>
      </div>

      {/* PENDENTE */}
      <div className="module-card">
        <div className="module-content">
          <h3 style={{ color: '#dc2626' }}>
            <AlertCircle size={18} /> Pendente
          </h3>
          <p style={{ color: '#dc2626' }}>
            {formatarMoeda(dados.totalPendente)}
          </p>
        </div>
      </div>

      {/* QUANTIDADE */}
      <div className="module-card">
        <div className="module-content">
          <h3>
            <List size={18} /> Qtd Lançamentos
          </h3>
          <p>{formatarNumero(dados.quantidadeLancamentos)}</p>
        </div>
      </div>

    </div>
  )
}