import { useState } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { useAuth } from '../auth/AuthContext'

import PageLayout from '../components/PageLayout'
import FormInput from '../components/FormInput'
import Alert from '../components/Alert'

import {
  listarMaquinas,
  listarMaquinasOs,
  adicionarMaquinaOs,
  removerMaquinaOs,
  obterOrdemServico
} from '../api/api'

// ✅ React Query
import { useQuery, useMutation } from '@tanstack/react-query'

export default function OrdemServicoMaquinas() {

  const { id } = useParams()
  const navigate = useNavigate()
  const { user } = useAuth()

  const podeEditarPerfil = user?.perfilId !== 2

  const [maquinaId, setMaquinaId] = useState('')
  const [horasPrevistas, setHorasPrevistas] = useState('')
  const [custoHora, setCustoHora] = useState('')

  const [erro, setErro] = useState('')
  const [sucesso, setSucesso] = useState('')

  // 🔥 QUERY MÁQUINAS
  const { data: maquinasResponse } = useQuery({
    queryKey: ['maquinas'],
    queryFn: listarMaquinas
  })

  const maquinas =
    maquinasResponse?.data?.data?.content ||
    maquinasResponse?.data?.data ||
    []

  // 🔥 QUERY MÁQUINAS DA OS
  const {
    data: maquinasOsResponse,
    refetch: refetchMaquinasOs
  } = useQuery({
    queryKey: ['maquinas-os', id],
    queryFn: () => listarMaquinasOs(id)
  })

  const maquinasOs = maquinasOsResponse?.data?.data || []

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
    mutationFn: (payload) => adicionarMaquinaOs(id, payload),
    onSuccess: (res) => {
      if (!res.data?.success) {
        setErro('Erro ao adicionar máquina')
        return
      }

      setSucesso('Máquina adicionada')

      setMaquinaId('')
      setHorasPrevistas('')
      setCustoHora('')

      refetchMaquinasOs()
    },
    onError: () => {
      setErro('Erro ao salvar')
    }
  })

  // 🔥 MUTATION REMOVER
  const removerMutation = useMutation({
    mutationFn: (itemId) => removerMaquinaOs(id, itemId),
    onSuccess: () => {
      refetchMaquinasOs()
    },
    onError: () => {
      setErro('Erro ao remover máquina')
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

    if (!maquinaId) {
      setErro('Selecione a máquina')
      return
    }

    const jaExiste = maquinasOs.some(m => String(m.maquinaId) === maquinaId)

    if (jaExiste) {
      setErro('Máquina já adicionada')
      return
    }

    adicionarMutation.mutate({
      maquinaId: Number(maquinaId),
      horasPrevistas: Number(horasPrevistas),
      custoHora: Number(custoHora)
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
      title="Máquinas da Ordem de Serviço"
      showBack
      backTo={`/ordens-servico/${id}/insumos`}
    >

      {erro && <Alert type="error" message={erro} />}
      {sucesso && <Alert type="success" message={sucesso} />}

      <form className="form-container" onSubmit={handleSubmit}>

        <div>
          <label>Máquina</label>
          <select
            value={maquinaId}
            onChange={e => setMaquinaId(e.target.value)}
            disabled={!podeEditar()}
          >
            <option value="">Selecione</option>
            {maquinas.map(m => (
              <option key={m.id} value={String(m.id)}>
                {m.nome}
              </option>
            ))}
          </select>
        </div>

        <FormInput
          label="Horas Previstas"
          type="number"
          value={horasPrevistas}
          onChange={e => setHorasPrevistas(e.target.value)}
          disabled={!podeEditar()}
        />

        <FormInput
          label="Custo por Hora"
          type="number"
          value={custoHora}
          onChange={e => setCustoHora(e.target.value)}
          disabled={!podeEditar()}
        />

        {podeEditar() && (
          <div className="form-actions">
            <button type="submit" className="add-btn">
              Adicionar Máquina
            </button>
          </div>
        )}

      </form>

      {maquinasOs.length > 0 && (
        <div className="card-list">
          {maquinasOs.map(m => (
            <div key={m.id} className="card">
              Máquina: {m.maquinaNome || m.maquinaId}
              <br />
              Horas: {m.horasPrevistas}
              <br />
              Custo: {m.custoHora}

              {podeEditar() && (
                <button
                  className="icon-button danger"
                  onClick={() => remover(m.id)}
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
          onClick={() => navigate(`/ordens-servico/${id}/funcionarios`)}
        >
          Avançar para Funcionários →
        </button>
      </div>

    </PageLayout>
  )
}