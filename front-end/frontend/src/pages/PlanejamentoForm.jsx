import { useState, useEffect } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { useAuth } from '../auth/AuthContext'

import PageLayout from '../components/PageLayout'
import FormInput from '../components/FormInput'
import Alert from '../components/Alert'

import {
  adicionarInsumoPlanejamento,
  listarProdutos
} from '../api/api'

export default function PlanejamentoInsumos() {

  const { id } = useParams()
  const navigate = useNavigate()
  const { user } = useAuth()

  const [produtoId, setProdutoId] = useState('')
  const [dosePorHa, setDosePorHa] = useState('')
  const [valorUnitarioPrevisto, setValorUnitarioPrevisto] = useState('')

  const [produtos, setProdutos] = useState([])
  const [insumosAdicionados, setInsumosAdicionados] = useState([])

  const [erro, setErro] = useState('')
  const [sucesso, setSucesso] = useState('')

  const podeEditar = user?.perfilId === 2

  useEffect(() => {
    carregarProdutos()
  }, [])

  async function carregarProdutos() {
    try {
      const res = await listarProdutos()
      setProdutos(res.data?.data?.content || res.data?.data || [])
    } catch (err) {
      console.error(err)
      setErro('Erro ao carregar produtos')
    }
  }

  async function handleSubmit(e) {
    e.preventDefault()
    setErro('')
    setSucesso('')

    if (!podeEditar) {
      setErro('Você não tem permissão para adicionar insumos')
      return
    }

    if (!produtoId) {
      setErro('Selecione o produto')
      return
    }

    try {
      const res = await adicionarInsumoPlanejamento(id, {
        produtoId: Number(produtoId),
        dosePorHa: Number(dosePorHa),
        valorUnitarioPrevisto: Number(valorUnitarioPrevisto)
      })

      if (!res.data?.success) {
        setErro('Erro ao adicionar insumo')
        return
      }

      setSucesso('Insumo adicionado com sucesso')

      setInsumosAdicionados(prev => [
        ...prev,
        {
          produtoId,
          dosePorHa,
          valorUnitarioPrevisto
        }
      ])

      setProdutoId('')
      setDosePorHa('')
      setValorUnitarioPrevisto('')

    } catch (err) {
      console.error(err)
      setErro('Erro ao salvar')
    }
  }

  return (
    <PageLayout
      title="Insumos do Planejamento"
      showBack
      backTo="/safra-talhoes"
    >

      {erro && <Alert type="error" message={erro} />}
      {sucesso && <Alert type="success" message={sucesso} />}

      <form className="form-container" onSubmit={handleSubmit}>

        <div>
          <label>Produto</label>
          <select
            value={produtoId}
            onChange={e => setProdutoId(e.target.value)}
            disabled={!podeEditar}
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
          disabled={!podeEditar}
        />

        <FormInput
          label="Valor Unitário"
          type="number"
          value={valorUnitarioPrevisto}
          onChange={e => setValorUnitarioPrevisto(e.target.value)}
          disabled={!podeEditar}
        />

        {podeEditar && (
          <div className="form-actions">
            <button type="submit" className="add-btn">
              Adicionar Insumo
            </button>
          </div>
        )}

      </form>

      {/* 🔥 LISTA LOCAL */}
      {insumosAdicionados.length > 0 && (
        <div className="card-list">
          {insumosAdicionados.map((i, index) => (
            <div key={index} className="card">
              Produto: {i.produtoId} | Dose: {i.dosePorHa} | Valor: {i.valorUnitarioPrevisto}
            </div>
          ))}
        </div>
      )}

      {/* 🔥 FLUXO → MÁQUINAS */}
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