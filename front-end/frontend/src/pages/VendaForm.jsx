import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'

import PageLayout from '../components/PageLayout'
import FormInput from '../components/FormInput'
import Alert from '../components/Alert'

import {
  criarVenda,
  listarPlanejamentos
} from '../api/api'

export default function VendaForm() {

  const navigate = useNavigate()

  const [safraTalhaoId, setSafraTalhaoId] = useState('')
  const [quantidade, setQuantidade] = useState('')
  const [precoUnitario, setPrecoUnitario] = useState('')
  const [dataVenda, setDataVenda] = useState('')

  const [planejamentos, setPlanejamentos] = useState([])

  const [erro, setErro] = useState('')
  const [sucesso, setSucesso] = useState('')

  // 🔥 cálculo automático
  const valorTotal =
    (Number(quantidade || 0) * Number(precoUnitario || 0))

  useEffect(() => {
    carregarPlanejamentos()
  }, [])

  async function carregarPlanejamentos() {
    try {
      const res = await listarPlanejamentos()

      const lista = Array.isArray(res.data?.data?.content)
        ? res.data.data.content
        : Array.isArray(res.data?.data)
          ? res.data.data
          : []

      setPlanejamentos(lista)

    } catch (e) {
      setErro('Erro ao carregar talhões')
    }
  }

  async function salvar() {

    setErro('')
    setSucesso('')

    if (!safraTalhaoId || !quantidade || !precoUnitario || !dataVenda) {
      setErro('Preencha todos os campos')
      return
    }

    try {

      await criarVenda({
        safraTalhaoId: Number(safraTalhaoId),
        quantidade: Number(quantidade),
        precoUnitario: Number(precoUnitario),
        dataVenda
      })

      setSucesso('Venda salva com sucesso')

      setTimeout(() => navigate('/vendas'), 800)

    } catch (e) {
      setErro('Erro ao salvar')
    }
  }

  return (
    <PageLayout title="Venda" showBack backTo="/vendas">

      {erro && <Alert type="error" message={erro} />}
      {sucesso && <Alert type="success" message={sucesso} />}

      <div className="form-grid-2" style={{ gap: 20 }}>

        {/* TALHÃO */}
        <div style={{ gridColumn: '1 / -1' }}>
          <label>Talhão</label>
          <select
            value={safraTalhaoId}
            onChange={e => setSafraTalhaoId(e.target.value)}
          >
            <option value="">Selecione</option>

            {planejamentos.map(p => (
              <option key={p.id} value={p.id}>
                {p.operacaoNome || p.operacao?.nome || `Planejamento ${p.id}`}
              </option>
            ))}
          </select>
        </div>

        {/* 🔥 LINHA COMPLETA COM 4 CAMPOS */}
        <div style={{
          gridColumn: '1 / -1',
          display: 'grid',
          gridTemplateColumns: 'repeat(4, 1fr)',
          gap: 16
        }}>

          <FormInput
            placeholder="Quantidade"
            value={quantidade}
            onChange={e => setQuantidade(e.target.value)}
          />

          <FormInput
            placeholder="Preço Unitário"
            value={precoUnitario}
            onChange={e => setPrecoUnitario(e.target.value)}
          />

          {/* 🔥 VALOR TOTAL (READONLY) */}
          <FormInput
            placeholder="Valor Total"
            value={valorTotal.toLocaleString('pt-BR', {
              style: 'currency',
              currency: 'BRL'
            })}
            readOnly
          />

          <FormInput
            type="date"
            value={dataVenda}
            onChange={e => setDataVenda(e.target.value)}
          />

        </div>

      </div>

      <div style={{ marginTop: 20 }}>
        <button className="add-btn" onClick={salvar}>
          Salvar Venda
        </button>
      </div>

    </PageLayout>
  )
}