import { useState, useEffect } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { Trash2, Pencil } from 'lucide-react'

import PageLayout from '../components/PageLayout'
import FormInput from '../components/FormInput'
import Alert from '../components/Alert'

import {
  adicionarInsumoPlanejamento,
  listarProdutos,
  listarInsumosPlanejamento,
  removerInsumoPlanejamento,
  obterResumoPlanejamento
} from '../api/api'

export default function PlanejamentoInsumos() {

  const { id } = useParams()
  const navigate = useNavigate()

  const [produtoId, setProdutoId] = useState('')
  const [dosePorHa, setDosePorHa] = useState('')
  const [valorUnitarioPrevisto, setValorUnitarioPrevisto] = useState('')

  const [produtos, setProdutos] = useState([])
  const [insumos, setInsumos] = useState([])
  const [resumo, setResumo] = useState(null)

  const [erro, setErro] = useState('')
  const [sucesso, setSucesso] = useState('')

  useEffect(() => {
    if (!id) {
      setErro('ID do planejamento não encontrado')
      return
    }

    carregarTudo()
  }, [id])

  async function carregarTudo() {
    await Promise.all([
      carregarProdutos(),
      carregarInsumos(),
      carregarResumo()
    ])
  }

  async function carregarProdutos() {
    const res = await listarProdutos()
    setProdutos(res.data?.data?.content || res.data?.data || [])
  }

  async function carregarInsumos() {
    if (!id) return
    const res = await listarInsumosPlanejamento(id)
    setInsumos(res.data?.data || [])
  }

  async function carregarResumo() {
    if (!id) return
    const res = await obterResumoPlanejamento(id)
    setResumo(res.data?.data)
  }

  function editarInsumo(item) {
    setProdutoId(String(item.produtoId || item.produto?.id))
    setDosePorHa(item.dosePorHa)
    setValorUnitarioPrevisto(item.valorUnitarioPrevisto)
  }

  async function handleSubmit(e) {
    e.preventDefault()
    setErro('')
    setSucesso('')

    if (!id) {
      setErro('ID do planejamento inválido')
      return
    }

    if (resumo?.possuiOs) {
      setErro('Planejamento já possui OS gerada')
      return
    }

    if (!produtoId) {
      setErro('Selecione o produto')
      return
    }

    if (!dosePorHa || !valorUnitarioPrevisto) {
      setErro('Preencha todos os campos')
      return
    }

    const jaExiste = insumos.some(i =>
      String(i.produtoId || i.produto?.id) === produtoId
    )

    if (jaExiste) {
      setErro('Produto já adicionado')
      return
    }

    try {
      const res = await adicionarInsumoPlanejamento(id, {
        produtoId: Number(produtoId),
        dosePorHa: Number(dosePorHa),
        valorUnitarioPrevisto: Number(valorUnitarioPrevisto)
      })

      if (!res.ok || res.data?.success === false) {
        setErro(res.data?.message || 'Erro ao adicionar insumo')
        return
      }

      setSucesso('Insumo adicionado')

      setProdutoId('')
      setDosePorHa('')
      setValorUnitarioPrevisto('')

      await carregarInsumos()
      await carregarResumo()

    } catch (err) {
      console.error(err)
      setErro('Erro ao adicionar insumo')
    }
  }

  async function remover(itemId) {
    if (!id) return

    if (resumo?.possuiOs) {
      setErro('Planejamento já possui OS gerada')
      return
    }

    try {
      await removerInsumoPlanejamento(id, itemId)
      await carregarInsumos()
      await carregarResumo()
    } catch (err) {
      console.error(err)
      setErro('Erro ao remover')
    }
  }

  // 🔥 TOTAL CUSTO
  const totalInsumos = insumos.reduce((total, i) => {
    const custo =
      resumo?.areaTotal
        ? (i.dosePorHa * i.valorUnitarioPrevisto * resumo.areaTotal)
        : 0
    return total + custo
  }, 0)

  // 🔥 TOTAL DOSE
  const totalDose = insumos.reduce((total, i) => {
    return total + Number(i.dosePorHa || 0)
  }, 0)

  // 🔥 TOTAL VALOR UNITÁRIO
  const totalValorUnitario = insumos.reduce((total, i) => {
    return total + Number(i.valorUnitarioPrevisto || 0)
  }, 0)

  return (
    <PageLayout
      title="Insumos do Planejamento"
      showBack
      backTo="/safra-talhoes"
    >

      {erro && <Alert type="error" message={erro} />}
      {sucesso && <Alert type="success" message={sucesso} />}

      {resumo?.possuiOs && (
        <Alert
          type="warning"
          message="Planejamento já possui OS gerada. Edição bloqueada."
        />
      )}

      <form
        className="form-container"
        onSubmit={handleSubmit}
        style={{ opacity: resumo?.possuiOs ? 0.5 : 1 }}
      >

        <div>
          <label>Produto</label>
          <select
            value={produtoId}
            onChange={e => {
              const idSelecionado = e.target.value
              setProdutoId(idSelecionado)

              const produto = produtos.find(p => String(p.id) === idSelecionado)

              if (produto?.vlrUnitario !== undefined) {
                setValorUnitarioPrevisto(produto.vlrUnitario)
              }
            }}
            disabled={resumo?.possuiOs}
          >
            <option value="">Selecione</option>
            {produtos.map(p => (
              <option key={p.id} value={String(p.id)}>
                {p.nome}
              </option>
            ))}
          </select>
        </div>

        <FormInput
          label="Dose por Hectare"
          type="number"
          value={dosePorHa}
          onChange={e => setDosePorHa(e.target.value)}
          disabled={resumo?.possuiOs}
        />

        <FormInput
          label="Valor Unitário"
          type="number"
          value={valorUnitarioPrevisto}
          onChange={e => setValorUnitarioPrevisto(e.target.value)}
          disabled={resumo?.possuiOs}
        />

        <div className="form-actions">
          <button
            type="submit"
            className="add-btn"
            disabled={resumo?.possuiOs}
          >
            Adicionar
          </button>
        </div>

      </form>

      {insumos.length > 0 && (
        <div style={{ marginTop: 20 }}>

          <table className="table-insumos compact">
            <thead>
              <tr>
                <th>Produto</th>
                <th>Dose/Ha</th>
                <th>Valor Unit.</th>
                <th>Custo</th>
                <th style={{ width: 100 }}>Ações</th>
              </tr>
            </thead>

            <tbody>
              {insumos.map(i => {

                const custo =
                  resumo?.areaTotal
                    ? (i.dosePorHa * i.valorUnitarioPrevisto * resumo.areaTotal)
                    : 0

                return (
                  <tr key={i.id}>

                    <td className="col-produto">
                      {i.produtoNome || `Produto ${i.produtoId}`}
                    </td>

                    <td>{i.dosePorHa}</td>

                    <td>
                      R$ {Number(i.valorUnitarioPrevisto).toFixed(2)}
                    </td>

                    <td className="col-custo">
                      R$ {custo.toFixed(2)}
                    </td>

                    <td className="acoes">
                      <button
                        className="icon-button edit"
                        onClick={() => editarInsumo(i)}
                        title="Editar"
                      >
                        <Pencil size={16} />
                      </button>

                      <button
                        className="icon-button danger"
                        onClick={() => remover(i.id)}
                        title="Remover"
                      >
                        <Trash2 size={16} />
                      </button>
                    </td>

                  </tr>
                )
              })}
            </tbody>

            <tfoot>
              <tr>
                <td style={{ fontWeight: 600 }}>Totais:</td>

                <td style={{ fontWeight: 600 }}>
                  {totalDose.toFixed(2)}
                </td>

                <td style={{ fontWeight: 600 }}>
                  R$ {totalValorUnitario.toFixed(2)}
                </td>

                <td className="col-custo">
                  R$ {totalInsumos.toFixed(2)}
                </td>

                <td></td>
              </tr>
            </tfoot>

          </table>

        </div>
      )}

      <div style={{ marginTop: 20 }}>
        <button
          className="add-btn"
          onClick={() => navigate(`/planejamento/${id}/maquinas`)}
        >
          Avançar para Máquinas →
        </button>
      </div>

    </PageLayout>
  )
}