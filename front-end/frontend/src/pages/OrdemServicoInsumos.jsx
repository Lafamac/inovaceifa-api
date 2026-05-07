import { useState } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { useAuth } from '../auth/AuthContext'

import PageLayout from '../components/PageLayout'
import FormInput from '../components/FormInput'
import Alert from '../components/Alert'

import {
  listarProdutos,
  listarInsumosOs,
  adicionarInsumoOs,
  removerInsumoOs,
  obterOrdemServico
} from '../api/api'

// ✅ React Query
import { useQuery, useMutation } from '@tanstack/react-query'

export default function OrdemServicoInsumos() {

  const { id } = useParams()
  const navigate = useNavigate()
  const { user } = useAuth()

  const podeEditarPerfil = user?.perfilId !== 2

  const [produtoId, setProdutoId] = useState('')
  const [dosePorHa, setDosePorHa] = useState('')
  const [valorUnitarioPrevisto, setValorUnitarioPrevisto] = useState('')

  const [erro, setErro] = useState('')
  const [sucesso, setSucesso] = useState('')

  // 🔥 QUERY PRODUTOS
  const { data: produtosResponse } = useQuery({
    queryKey: ['produtos'],
    queryFn: listarProdutos
  })

  const produtos =
    produtosResponse?.data?.data?.content ||
    produtosResponse?.data?.data ||
    []

  // 🔥 QUERY INSUMOS
  const {
    data: insumosResponse,
    refetch: refetchInsumos
  } = useQuery({
    queryKey: ['insumos-os', id],
    queryFn: () => listarInsumosOs(id)
  })

  const insumos = insumosResponse?.data?.data || []

  // 🔥 QUERY ORDEM
  const { data: ordemResponse } = useQuery({
    queryKey: ['ordem-servico', id],
    queryFn: () => obterOrdemServico(id)
  })

  const ordem = ordemResponse?.data?.data || null

  function podeEditar() {
    return podeEditarPerfil && ordem?.status !== 'FINALIZADA'
  }

  // 🔥 MUTATION ADICIONAR
  const adicionarMutation = useMutation({
    mutationFn: (payload) => adicionarInsumoOs(id, payload),
    onSuccess: (res) => {
      if (!res.data?.success) {
        setErro('Erro ao adicionar insumo')
        return
      }

      setSucesso('Insumo adicionado')

      setProdutoId('')
      setDosePorHa('')
      setValorUnitarioPrevisto('')

      refetchInsumos()
    },
    onError: () => {
      setErro('Erro ao salvar')
    }
  })

  // 🔥 MUTATION REMOVER
  const removerMutation = useMutation({
    mutationFn: (itemId) => removerInsumoOs(id, itemId),
    onSuccess: () => {
      refetchInsumos()
    },
    onError: () => {
      setErro('Erro ao remover insumo')
    }
  })

  async function handleSubmit(e) {
    e.preventDefault()
    setErro('')
    setSucesso('')

    if (!podeEditar()) {
      setErro('OS não pode ser alterada')
      return
    }

    if (!produtoId) {
      setErro('Selecione o produto')
      return
    }

    const jaExiste = insumos.some(i => String(i.produtoId) === produtoId)

    if (jaExiste) {
      setErro('Produto já adicionado')
      return
    }

    adicionarMutation.mutate({
      produtoId: Number(produtoId),
      dosePorHa: Number(dosePorHa),
      valorUnitarioPrevisto: Number(valorUnitarioPrevisto)
    })
  }

  function remover(itemId) {

    if (!podeEditar()) {
      setErro('OS não pode ser alterada')
      return
    }

    removerMutation.mutate(itemId)
  }

  return (
    <PageLayout
      title="Insumos da Ordem de Serviço"
      showBack
      backTo={`/ordens-servico/${id}/execucao`}
    >

      {erro && <Alert type="error" message={erro} />}
      {sucesso && <Alert type="success" message={sucesso} />}

      <form className="form-container" onSubmit={handleSubmit}>

        <div>
          <label>Produto</label>
          <select
            value={produtoId}
            onChange={e => setProdutoId(e.target.value)}
            disabled={!podeEditar()}
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
          disabled={!podeEditar()}
        />

        <FormInput
          label="Valor Unitário"
          type="number"
          value={valorUnitarioPrevisto}
          onChange={e => setValorUnitarioPrevisto(e.target.value)}
          disabled={!podeEditar()}
        />

        {podeEditar() && (
          <div className="form-actions">
            <button type="submit" className="add-btn">
              Adicionar Insumo
            </button>
          </div>
        )}

      </form>

      {insumos.length > 0 && (
        <div className="card-list">
          {insumos.map(i => (
            <div key={i.id} className="card">
              Produto: {i.produtoNome || i.produtoId}
              <br />
              Dose: {i.dosePorHa}
              <br />
              Valor: {i.valorUnitarioPrevisto}

              {podeEditar() && (
                <button
                  className="icon-button danger"
                  onClick={() => remover(i.id)}
                  style={{ marginTop: 10 }}
                >
                  Remover
                </button>
              )}
            </div>
          ))}
        </div>
      )}

      <div style={{ marginTop: 20 }}>
        <button
          className="add-btn"
          onClick={() => navigate(`/ordens-servico/${id}/maquinas`)}
        >
          Avançar para Máquinas →
        </button>
      </div>

    </PageLayout>
  )
}