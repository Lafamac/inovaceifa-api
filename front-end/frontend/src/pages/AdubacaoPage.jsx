import { useState, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'

import PageLayout from '../components/PageLayout'
import Alert from '../components/Alert'

import { useQuery } from '@tanstack/react-query'
import {
  obterAdubacaoSafra,
  criarPedidoCompra,
  listarProdutos
} from '../api/api'

import AdubacaoChart from '../components/AdubacaoChart'

export default function AdubacaoPage() {

  const navigate = useNavigate()

  const [safraId, setSafraId] = useState(1)
  const [erro, setErro] = useState('')
  const [data, setData] = useState(null)
  const [produtos, setProdutos] = useState([])

  const {
    data: response,
    isLoading
  } = useQuery({
    queryKey: ['adubacao', safraId],
    queryFn: () => obterAdubacaoSafra(safraId),
    onError: () => setErro('Erro ao carregar dados')
  })

  useEffect(() => {
    if (!response?.data?.data) {
      setData(null)
      return
    }

    setData(response.data.data)
  }, [response])

  // 🔥 CARREGAR PRODUTOS
  useEffect(() => {
    async function carregarProdutos() {
      try {
        const res = await listarProdutos()

        setProdutos(
          res.data?.data?.content ||
          res.data?.data ||
          []
        )
      } catch (err) {
        console.error(err)
      }
    }

    carregarProdutos()
  }, [])

  function formatar(valor) {
    return Number(valor || 0).toLocaleString('pt-BR')
  }

  // 🔥 NOVO
  async function gerarPedido() {
    try {

      const itens = data.totalGeral.map(p => {

        const produto = produtos.find(prod => prod.id === p.produtoId)

        return {
          produtoId: p.produtoId,
          quantidade: Number(p.quantidadeTotal),
          valorUnitario: produto?.precoCusto || 0
        }
      })

      const payload = { itens }

      const res = await criarPedidoCompra(payload)

      const id =
        res.data?.data?.id ||
        res.data?.data?.data?.id

      if (!id) {
        setErro('Erro ao gerar pedido')
        return
      }

      // 🔥 REDIRECIONA
      navigate(`/pedidos-compra/${id}/editar`)

    } catch (err) {
      console.error(err)
      setErro('Erro ao gerar pedido')
    }
  }

  if (isLoading) {
    return <PageLayout title="Adubação">Carregando...</PageLayout>
  }

  if (!data) {
    return (
      <PageLayout title="Adubação">
        <Alert type="error" message="Nenhum dado encontrado" />
      </PageLayout>
    )
  }

  return (
    <PageLayout title="Adubação">

      {erro && <Alert type="error" message={erro} />}

      <div style={{ marginBottom: 20 }}>
        <label>Safra: </label>
        <input
          type="number"
          value={safraId}
          onChange={(e) => setSafraId(e.target.value)}
        />
      </div>

      <div className="card" style={{ marginBottom: 20 }}>
        <h3>Total Geral</h3>

        {data.totalGeral
          ?.sort((a, b) => b.quantidadeTotal - a.quantidadeTotal)
          .map(item => (
            <div key={item.produtoId}>
              {item.produtoNome}:{' '}
              <strong>{formatar(item.quantidadeTotal)} kg</strong>
            </div>
          ))}

        <AdubacaoChart data={data.totalGeral} />

        <button
          className="add-btn"
          style={{ marginTop: 15 }}
          onClick={gerarPedido}
        >
          Gerar Pedido de Compra
        </button>

      </div>

      {data.talhoes?.map(t => (
        <div key={t.talhaoId} className="card">
          <h4>{t.talhaoNome}</h4>

          <table width="100%">
            <tbody>
              {t.produtos.map(p => (
                <tr key={p.produtoId}>
                  <td>{p.produtoNome}</td>
                  <td>{formatar(p.quantidadeTotal)} kg</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      ))}

    </PageLayout>
  )
}