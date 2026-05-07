import { useEffect, useState } from 'react'

import PageLayout from '../components/PageLayout'
import Alert from '../components/Alert'

import { listarGestaoVista } from '../api/api'

export default function GestaoVistaPage() {

  const [data, setData] = useState(null)
  const [erro, setErro] = useState('')
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    carregar()
  }, [])

  async function carregar() {
    try {
      const res = await listarGestaoVista()

      if (!res.ok || !res.data?.data) {
        setErro('Erro ao carregar relatório')
        return
      }

      setData(res.data.data)

    } catch (e) {
      setErro('Erro ao carregar dados')
    } finally {
      setLoading(false)
    }
  }

  function fMoeda(v) {
    return Number(v || 0).toLocaleString('pt-BR', {
      style: 'currency',
      currency: 'BRL'
    })
  }

  function fNum(v) {
    return Number(v || 0).toLocaleString('pt-BR')
  }

  if (loading) return <PageLayout title="Gestão à Vista">Carregando...</PageLayout>

  if (erro) {
    return (
      <PageLayout title="Gestão à Vista">
        <Alert type="error" message={erro} />
      </PageLayout>
    )
  }

  return (
    <PageLayout title="Gestão à Vista" showBack backTo="/menu">

      {/* CARDS */}
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit,minmax(200px,1fr))', gap: 16 }}>
        <div className="card"><strong>Custo/ha</strong><p>{fMoeda(data.custoPorHectare)}</p></div>
        <div className="card"><strong>Custo/sc</strong><p>{fMoeda(data.custoPorSaca)}</p></div>
        <div className="card"><strong>Produtividade</strong><p>{fNum(data.produtividadeMedia)} sc/ha</p></div>
      </div>

      {/* RANKING */}
      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 16, marginTop: 20 }}>
        <div className="card"><strong>🏆 Melhor</strong><p>{data.melhorTalhao?.talhaoNome}</p></div>
        <div className="card"><strong>⚠️ Pior</strong><p>{data.piorTalhao?.talhaoNome}</p></div>
      </div>

      {/* TABELA */}
      <div style={{ display: 'flex', justifyContent: 'center', marginTop: 20 }}>
        <div className="card" style={{ width: '1400px', maxWidth: '95vw' }}>

          <h3>Gestão à Vista</h3>

          <table style={{ width: '100%' }}>
            <thead>
              <tr>
                <th>Talhão</th>
                <th>Área</th>
                <th>Produção</th>
                <th>Vendido</th>
                <th>Estoque</th>
                <th>Preço Médio</th>
                <th>sc/ha</th>
                <th>Custo</th>
                <th>Custo/ha</th>
                <th>Custo/sc</th>
                <th>Receita</th>
                <th>Lucro</th>
              </tr>
            </thead>

            <tbody>
              {data.itens.map((i, idx) => (
                <tr key={idx}>
                  <td>{i.talhaoNome}</td>
                  <td>{fNum(i.area)}</td>
                  <td>{fNum(i.producao)}</td>
                  <td>{fNum(i.vendido)}</td>
                  <td>{fNum(i.estoque)}</td>
                  <td>{fMoeda(i.precoMedio)}</td>
                  <td>{fNum(i.produtividade)}</td>
                  <td>{fMoeda(i.custoTotal)}</td>
                  <td>{fMoeda(i.custoPorHectare)}</td>
                  <td>{fMoeda(i.custoPorSaca)}</td>
                  <td>{fMoeda(i.receita)}</td>
                  <td style={{
                    color: i.lucro < 0 ? '#dc2626' : '#16a34a',
                    fontWeight: 600
                  }}>
                    {fMoeda(i.lucro)}
                  </td>
                </tr>
              ))}
            </tbody>

            <tfoot>
              <tr style={{ fontWeight: 'bold' }}>
                <td>TOTAL</td>
                <td>{fNum(data.totalArea)}</td>
                <td>{fNum(data.totalProducao)}</td>
                <td>{fNum(data.totalVendido)}</td>
                <td>{fNum(data.totalEstoque)}</td>
                <td>-</td>
                <td>-</td>
                <td>{fMoeda(data.totalCusto)}</td>
                <td>-</td>
                <td>-</td>
                <td>{fMoeda(data.totalReceita)}</td>
                <td style={{
                  color: data.totalLucro < 0 ? '#dc2626' : '#16a34a'
                }}>
                  {fMoeda(data.totalLucro)}
                </td>
              </tr>
            </tfoot>

          </table>
        </div>
      </div>

    </PageLayout>
  )
}