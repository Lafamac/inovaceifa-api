import { useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'

import PageLayout from '../components/PageLayout'
import FormInput from '../components/FormInput'
import Alert from '../components/Alert'

import { pagarContaPagar } from '../api/api'

export default function BaixarContaForm() {

  const navigate = useNavigate()
  const { id } = useParams()

  const [dataPagamento, setDataPagamento] = useState('')
  const [valorPago, setValorPago] = useState('')
  const [juros, setJuros] = useState('')

  const [erro, setErro] = useState('')
  const [sucesso, setSucesso] = useState('')

  async function handleSubmit(e) {
    e.preventDefault()

    const payload = {
      dataPagamento,
      vlrPago: Number(valorPago),
      vlrJuros: Number(juros || 0)
    }

    try {
      await pagarContaPagar(id, payload)

      setSucesso('Conta baixada com sucesso')
      setTimeout(() => navigate('/financeiro/contas-pagar'), 600)

    } catch {
      setErro('Erro ao baixar conta')
    }
  }

  return (
    <PageLayout title="Baixar Conta" showBack backTo="/financeiro/contas-pagar">

      {erro && <Alert type="error" message={erro} />}
      {sucesso && <Alert type="success" message={sucesso} />}

      <form onSubmit={handleSubmit} className="form-container">

        <FormInput
          label="Data Pagamento"
          type="date"
          value={dataPagamento}
          onChange={e => setDataPagamento(e.target.value)}
        />

        <FormInput
          label="Valor Pago"
          type="number"
          value={valorPago}
          onChange={e => setValorPago(e.target.value)}
        />

        <FormInput
          label="Juros"
          type="number"
          value={juros}
          onChange={e => setJuros(e.target.value)}
        />

        <button type="submit" className="add-btn">
          Confirmar Baixa
        </button>

      </form>

    </PageLayout>
  )
}