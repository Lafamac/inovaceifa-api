import { useState, useEffect } from 'react'
import { useNavigate, useParams } from 'react-router-dom'

import PageLayout from '../components/PageLayout'
import FormInput from '../components/FormInput'
import Alert from '../components/Alert'

import {
  criarPedidoCompra,
  buscarPedidoCompra,
  atualizarPedidoCompra,
  listarProdutos
} from '../api/api'

export default function PedidoCompraForm() {

  const navigate = useNavigate()
  const { id } = useParams()

  const [itens, setItens] = useState([])
  const [produtos, setProdutos] = useState([])

  const [produtoId, setProdutoId] = useState('')
  const [quantidade, setQuantidade] = useState('')
  const [valorUnitario, setValorUnitario] = useState('')

  const [erro, setErro] = useState('')
  const [sucesso, setSucesso] = useState('')

  useEffect(() => {
    async function carregarProdutos() {
      const res = await listarProdutos()
      setProdutos(res.data?.data || [])
    }

    async function carregar() {
      const res = await buscarPedidoCompra(id)
      setItens(res.data?.data?.itens || [])
    }

    carregarProdutos()
    if (id) carregar()
  }, [id])

  function adicionarItem() {
    if (!produtoId) return

    const novo = {
      produtoId: Number(produtoId),
      quantidade: Number(quantidade),
      valorUnitario: Number(valorUnitario),
      valorTotal: Number(quantidade) * Number(valorUnitario)
    }

    setItens(prev => [...prev, novo])

    setProdutoId('')
    setQuantidade('')
    setValorUnitario('')
  }

  async function salvar() {

    const payload = {
      itens
    }

    if (id) {
      await atualizarPedidoCompra(id, payload)
    } else {
      await criarPedidoCompra(payload)
    }

    setSucesso('Salvo com sucesso')

    setTimeout(() => navigate('/pedidos-compra'), 800)
  }

  const total = itens.reduce((t, i) => t + i.valorTotal, 0)

  return (
    <PageLayout title="Pedido de Compra" showBack backTo="/pedidos-compra">

      {erro && <Alert type="error" message={erro} />}
      {sucesso && <Alert type="success" message={sucesso} />}

      <div className="form-grid-3">

        <select value={produtoId} onChange={e => setProdutoId(e.target.value)}>
          <option value="">Produto</option>
          {produtos.map(p => (
            <option key={p.id} value={p.id}>{p.nome}</option>
          ))}
        </select>

        <FormInput
          type="number"
          placeholder="Qtd"
          value={quantidade}
          onChange={e => setQuantidade(e.target.value)}
        />

        <FormInput
          type="number"
          placeholder="Valor"
          value={valorUnitario}
          onChange={e => setValorUnitario(e.target.value)}
        />

      </div>

      <button className="add-btn" onClick={adicionarItem}>
        Adicionar Item
      </button>

      <table className="table-insumos">
        <tbody>
          {itens.map((i, idx) => (
            <tr key={idx}>
              <td>{i.produtoId}</td>
              <td>{i.quantidade}</td>
              <td>{i.valorUnitario}</td>
              <td>{i.valorTotal}</td>
            </tr>
          ))}
        </tbody>
      </table>

      <div style={{ marginTop: 20 }}>
        Total: R$ {total.toFixed(2)}
      </div>

      <button className="add-btn" onClick={salvar}>
        Salvar Pedido
      </button>

    </PageLayout>
  )
}