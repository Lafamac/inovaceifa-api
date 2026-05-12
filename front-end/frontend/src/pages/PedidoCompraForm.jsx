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

  const [valorUnitario, setValorUnitario] =
    useState('')

  const [erro, setErro] = useState('')

  const [sucesso, setSucesso] = useState('')

  useEffect(() => {

    async function carregarProdutos() {

      try {

        const res = await listarProdutos()

        console.log(
          'PRODUTOS:',
          res.data
        )

        // 🔥 CORREÇÃO
        const lista =
          res.data?.data?.content ||
          res.data?.data ||
          []

        setProdutos(
          Array.isArray(lista)
            ? lista
            : []
        )

      } catch (err) {

        console.error(err)

        setProdutos([])

        setErro(
          'Erro ao carregar produtos'
        )
      }
    }

    async function carregar() {

      try {

        const res =
          await buscarPedidoCompra(id)

        setItens(
          res.data?.data?.itens || []
        )

      } catch (err) {

        console.error(err)

        setErro(
          'Erro ao carregar pedido'
        )
      }
    }

    carregarProdutos()

    if (id) {
      carregar()
    }

  }, [id])

  function adicionarItem() {

    setErro('')

    if (!produtoId) {

      setErro(
        'Selecione um produto'
      )

      return
    }

    if (!quantidade) {

      setErro(
        'Informe a quantidade'
      )

      return
    }

    if (!valorUnitario) {

      setErro(
        'Informe o valor unitário'
      )

      return
    }

    const produtoSelecionado =
      produtos.find(
        p => String(p.id) === String(produtoId)
      )

    const novo = {

      produtoId: Number(produtoId),

      produtoNome:
        produtoSelecionado?.nome || '',

      quantidade:
        Number(quantidade),

      valorUnitario:
        Number(valorUnitario),

      valorTotal:
        Number(quantidade) *
        Number(valorUnitario)
    }

    setItens(prev => [...prev, novo])

    setProdutoId('')

    setQuantidade('')

    setValorUnitario('')
  }

  async function salvar() {

    setErro('')

    setSucesso('')

    if (itens.length === 0) {

      setErro(
        'Adicione pelo menos um item'
      )

      return
    }

    const payload = {
      itens
    }

    try {

      if (id) {

        await atualizarPedidoCompra(
          id,
          payload
        )

      } else {

        await criarPedidoCompra(
          payload
        )
      }

      setSucesso(
        'Salvo com sucesso'
      )

      setTimeout(() => {

        navigate('/pedidos-compra')

      }, 800)

    } catch (err) {

      console.error(err)

      setErro(
        'Erro ao salvar pedido'
      )
    }
  }

  const total = itens.reduce(
    (t, i) => t + i.valorTotal,
    0
  )

  return (

    <PageLayout
      title="Pedido de Compra"
      showBack
      backTo="/pedidos-compra"
    >

      {erro && (
        <Alert
          type="error"
          message={erro}
        />
      )}

      {sucesso && (
        <Alert
          type="success"
          message={sucesso}
        />
      )}

      <div
        className="form-grid-3"
        style={{
          marginBottom: 16
        }}
      >

        <div>

          <label>Produto</label>

          <select
            value={produtoId}
            onChange={e =>
              setProdutoId(
                e.target.value
              )
            }
          >

            <option value="">
              Selecione
            </option>

            {produtos.map(p => (

              <option
                key={p.id}
                value={p.id}
              >
                {p.nome}
              </option>

            ))}

          </select>

        </div>

        <FormInput
          type="number"
          label="Quantidade"
          value={quantidade}
          onChange={e =>
            setQuantidade(
              e.target.value
            )
          }
        />

        <FormInput
          type="number"
          label="Valor Unitário"
          value={valorUnitario}
          onChange={e =>
            setValorUnitario(
              e.target.value
            )
          }
        />

      </div>

      <div
        className="form-actions"
        style={{
          marginBottom: 20
        }}
      >

        <button
          type="button"
          className="add-btn"
          onClick={adicionarItem}
        >
          Adicionar Item
        </button>

      </div>

      <div className="card">

        <table className="table-insumos">

          <thead>

            <tr>

              <th>Produto</th>
              <th>Qtd</th>
              <th>Valor Unitário</th>
              <th>Total</th>

            </tr>

          </thead>

          <tbody>

            {itens.map((i, idx) => (

              <tr key={idx}>

                <td>
                  {i.produtoNome ||
                    i.produtoId}
                </td>

                <td>
                  {i.quantidade}
                </td>

                <td>
                  R$ {Number(
                    i.valorUnitario
                  ).toFixed(2)}
                </td>

                <td>
                  R$ {Number(
                    i.valorTotal
                  ).toFixed(2)}
                </td>

              </tr>

            ))}

          </tbody>

        </table>

      </div>

      <div
        style={{
          marginTop: 20,
          fontSize: 18,
          fontWeight: 700
        }}
      >

        Total:
        {' '}
        R$ {total.toFixed(2)}

      </div>

      <div
        className="form-actions"
        style={{
          marginTop: 20
        }}
      >

        <button
          className="add-btn"
          onClick={salvar}
        >
          Salvar Pedido
        </button>

      </div>

    </PageLayout>
  )
}