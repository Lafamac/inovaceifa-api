import { useState, useEffect } from 'react'
import { useQuery, useMutation } from '@tanstack/react-query'

import FormInput from '../components/FormInput'
import Alert from '../components/Alert'

import {
  listarProdutos,
  adicionarOperacaoProduto,
  listarOperacaoProdutos,
  removerOperacaoProduto
} from '../api/api'

export default function OperacaoProdutos({ operacaoId, setTotal, bloqueado }) {

  const [produtoId, setProdutoId] = useState('')
  const [quantidade, setQuantidade] = useState('')
  const [vlrUnitario, setVlrUnitario] = useState('')

  const [erro, setErro] = useState('')
  const [sucesso, setSucesso] = useState('')

  const { data: produtosRes } = useQuery({
    queryKey: ['produtos'],
    queryFn: listarProdutos
  })

  const produtos =
    produtosRes?.data?.data?.content ||
    produtosRes?.data?.data ||
    []

  const { data: listaRes, refetch } = useQuery({
    queryKey: ['operacao-produtos', operacaoId],
    queryFn: () => listarOperacaoProdutos(operacaoId)
  })

  const lista = Array.isArray(listaRes?.data?.data)
    ? listaRes.data.data
    : []

  const addMutation = useMutation({
    mutationFn: adicionarOperacaoProduto,
    onSuccess: () => {
      setSucesso('Produto adicionado')
      setProdutoId('')
      setQuantidade('')
      setVlrUnitario('')
      refetch()
    },
    onError: () => setErro('Erro ao adicionar')
  })

  const removeMutation = useMutation({
    mutationFn: removerOperacaoProduto,
    onSuccess: () => refetch(),
    onError: () => setErro('Erro ao remover')
  })

  function handleSubmit(e) {
    e.preventDefault()
    setErro('')
    setSucesso('')

    if (!produtoId) {
      setErro('Selecione o produto')
      return
    }

    addMutation.mutate({
      operacaoTalhaoId: Number(operacaoId),
      produtoId: Number(produtoId),
      quantidade: Number(quantidade),
      vlrUnitario: Number(vlrUnitario)
    })
  }

  function remover(id) {
    if (bloqueado) return
    removeMutation.mutate(id)
  }

  const totalGeral = lista.reduce(
    (t, i) => t + Number(i.vlrTotal || 0),
    0
  )

  useEffect(() => {
    if (setTotal) setTotal(totalGeral)
  }, [totalGeral])

  return (
    <div className="card" style={{ marginBottom: 20 }}>

      <h3>Produtos</h3>

      {erro && <Alert type="error" message={erro} />}
      {sucesso && <Alert type="success" message={sucesso} />}

      <form className="form-container" onSubmit={handleSubmit}>

        <div>
          <label>Produto</label>
          <select
            value={produtoId}
            onChange={e => setProdutoId(e.target.value)}
            disabled={bloqueado}
          >
            <option value="">Selecione</option>
            {produtos.map(p => (
              <option key={p.id} value={p.id}>
                {p.nome}
              </option>
            ))}
          </select>
        </div>

        <FormInput
          label="Quantidade"
          type="number"
          value={quantidade}
          onChange={e => setQuantidade(e.target.value)}
          disabled={bloqueado}
        />

        <FormInput
          label="Valor Unitário"
          type="number"
          value={vlrUnitario}
          onChange={e => setVlrUnitario(e.target.value)}
          disabled={bloqueado}
        />

        <div className="form-actions">
          <button className="add-btn" disabled={bloqueado}>
            Adicionar
          </button>
        </div>

      </form>

      {lista.length > 0 && (
        <div style={{ marginTop: 20 }}>

          <table className="table-insumos compact">

            <thead>
              <tr>
                <th>Produto</th>
                <th>Qtd</th>
                <th>Valor</th>
                <th>Total</th>
                <th></th>
              </tr>
            </thead>

            <tbody>
              {lista.map(i => (
                <tr key={i.id}>
                  <td>{i.produtoNome}</td>
                  <td>{i.quantidade}</td>
                  <td>{i.vlrUnitario}</td>
                  <td className="col-custo">{i.vlrTotal}</td>
                  <td>
                    <button
                      className="icon-button danger"
                      disabled={bloqueado}
                      onClick={() => remover(i.id)}
                    >
                      ❌
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>

            <tfoot>
              <tr>
                <td colSpan="3"><strong>Total</strong></td>
                <td className="col-custo">
                  <strong>{totalGeral.toFixed(2)}</strong>
                </td>
                <td></td>
              </tr>
            </tfoot>

          </table>

        </div>
      )}

    </div>
  )
}