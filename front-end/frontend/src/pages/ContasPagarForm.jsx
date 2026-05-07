import { useState, useEffect } from 'react'
import { useNavigate, useParams } from 'react-router-dom'

import PageLayout from '../components/PageLayout'
import FormInput from '../components/FormInput'
import Alert from '../components/Alert'

import {
  criarContaPagar,
  atualizarContaPagar,
  buscarContaPagar,
  listarReferencias
} from '../api/api'

export default function ContasPagarForm() {

  const navigate = useNavigate()
  const { id } = useParams()

  const [favorecido, setFavorecido] = useState('')
  const [valor, setValor] = useState('')
  const [dataVencimento, setDataVencimento] = useState('')
  const [refDespesaId, setRefDespesaId] = useState('')

  const [despesas, setDespesas] = useState([])

  const [erro, setErro] = useState('')
  const [sucesso, setSucesso] = useState('')

  useEffect(() => {
    carregarDespesas()
    if (id) carregarConta()
  }, [id])

  async function carregarDespesas() {
    try {
      const res = await listarReferencias('despesa')
      const dados = res.data?.data || []
      setDespesas(dados)
    } catch {
      setErro('Erro ao carregar despesas')
    }
  }

  async function carregarConta() {
    try {
      const res = await buscarContaPagar(id)
      const d = res.data?.data

      setFavorecido(d.favorecido)
      setValor(d.vlrReal)
      setDataVencimento(d.dataVencimento)
      setRefDespesaId(d.refDespesaId) // 🔥 CORREÇÃO

    } catch {
      setErro('Erro ao carregar conta')
    }
  }

  async function handleSubmit(e) {
    e.preventDefault()

    const payload = {
      favorecido,
      vlrReal: Number(valor),
      dataVencimento,
      refDespesaId: Number(refDespesaId)
    }

    try {

      if (id) {
        await atualizarContaPagar(id, payload)
      } else {
        await criarContaPagar(payload)
      }

      setSucesso('Salvo com sucesso')
      setTimeout(() => navigate('/financeiro/contas-pagar'), 600)

    } catch {
      setErro('Erro ao salvar')
    }
  }

  return (
    <PageLayout
      title={id ? "Editar Conta" : "Nova Conta a Pagar"}
      showBack
      backTo="/financeiro/contas-pagar"
    >

      {erro && <Alert type="error" message={erro} />}
      {sucesso && <Alert type="success" message={sucesso} />}

      <form onSubmit={handleSubmit} className="form-container">

        <FormInput
          label="Favorecido"
          value={favorecido}
          onChange={e => setFavorecido(e.target.value)}
        />

        <div>
          <label>Despesa</label>
          <select
            value={refDespesaId}
            onChange={e => setRefDespesaId(e.target.value)}
          >
            <option value="">Selecione</option>
            {despesas.map(d => (
              <option key={d.id} value={d.id}>
                {d.descricao}
              </option>
            ))}
          </select>
        </div>

        <div style={{ display: 'flex', gap: 10 }}>
          <div style={{ flex: 1 }}>
            <FormInput
              label="Valor"
              type="number"
              value={valor}
              onChange={e => setValor(e.target.value)}
            />
          </div>

          <div style={{ flex: 1 }}>
            <FormInput
              label="Data Vencimento"
              type="date"
              value={dataVencimento}
              onChange={e => setDataVencimento(e.target.value)}
            />
          </div>
        </div>

        <button type="submit" className="add-btn">
          Salvar
        </button>

      </form>

    </PageLayout>
  )
}