import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'

import PageLayout from '../components/PageLayout'
import Alert from '../components/Alert'

import {
  listarLancamentos
} from '../api/api'

import ResumoFinanceiro from './ResumoFinanceiro'

export default function LancamentosPage() {

  const navigate = useNavigate()

  const [lista, setLista] = useState([])
  const [erro, setErro] = useState('')
  const [loading, setLoading] = useState(false)

  const [filtroStatus, setFiltroStatus] = useState('')
  const [dataInicio, setDataInicio] = useState('')
  const [dataFim, setDataFim] = useState('')

  useEffect(() => {
    carregar()
  }, [])

  async function carregar() {
    try {
      setLoading(true)

      const res = await listarLancamentos(
        0,
        10,
        filtroStatus,
        dataInicio,
        dataFim
      )

      const dados =
        res.data?.data?.content ||
        res.data?.data ||
        []

      setLista(dados)

    } catch (err) {
      console.error(err)
      setErro('Erro ao carregar lançamentos')
    } finally {
      setLoading(false)
    }
  }

  function pesquisar() {
    carregar()
  }

  function formatarMoeda(valor) {
    return Number(valor || 0).toLocaleString('pt-BR', {
      style: 'currency',
      currency: 'BRL'
    })
  }

  return (
    <PageLayout title="Lançamentos Financeiros">

      {/* RESUMO */}
      <div
        style={{
          display: 'grid',
          gridTemplateColumns: 'repeat(4, 1fr)',
          gap: 16,
          marginBottom: 20
        }}
      >
        <ResumoFinanceiro />
      </div>

      {erro && <Alert type="error" message={erro} />}

      {/* FILTROS */}
      <div className="form-container" style={{ display: 'flex', gap: 12 }}>

        {/* STATUS */}
        <div style={{ display: 'flex', flexDirection: 'column' }}>
          <label>Status</label>
          <select
            value={filtroStatus}
            onChange={e => setFiltroStatus(e.target.value)}
          >
            <option value="">Todos</option>
            <option value="PENDENTE">Pendentes</option>
            <option value="PAGO">Pagos</option>
          </select>
        </div>

        {/* DATAS + BOTÃO */}
        <div style={{ display: 'flex', gap: 10, alignItems: 'flex-end' }}>

          <div style={{ display: 'flex', flexDirection: 'column' }}>
            <label>Data Início</label>
            <input
              type="date"
              value={dataInicio}
              onChange={e => setDataInicio(e.target.value)}
            />
          </div>

          <div style={{ display: 'flex', flexDirection: 'column' }}>
            <label>Data Fim</label>
            <input
              type="date"
              value={dataFim}
              onChange={e => setDataFim(e.target.value)}
            />
          </div>

          {/* BOTÃO MENOR E ALINHADO */}
          <button
            className="add-btn"
            onClick={pesquisar}
            style={{
              height: 34,
              padding: '0 12px',
              fontSize: 13
            }}
          >
            Buscar
          </button>

        </div>

      </div>

      {/* TABELA */}
      <div className="card">
        <table className="table-insumos">
          <thead>
            <tr>
              <th>Data</th>
              <th>Categoria</th>
              <th>Centro de Custo</th>
              <th>Valor</th>
              <th>Status</th>
              <th></th>
            </tr>
          </thead>

          <tbody>
            {lista.map(item => (
              <tr key={item.id}>

                <td>{item.data}</td>

                <td>{item.descricaoDespesa}</td>

                <td>{item.descricaoCentroCusto || '-'}</td>

                <td>{formatarMoeda(item.valor)}</td>

                <td>
                  {item.statusPagamento === 'PAGO' ? 'Pago' : 'Pendente'}
                </td>

                <td style={{ display: 'flex', gap: 8 }}>

                  {item.statusPagamento !== 'PAGO' && (
                    <button
                      className="add-btn"
                      onClick={() =>
                        navigate(`/financeiro/lancamentos/${item.id}/pagar`)
                      }
                    >
                      Pagar
                    </button>
                  )}

                </td>

              </tr>
            ))}
          </tbody>
        </table>
      </div>

    </PageLayout>
  )
}