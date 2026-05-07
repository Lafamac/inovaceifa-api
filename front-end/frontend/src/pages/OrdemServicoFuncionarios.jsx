import { useState } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { useAuth } from '../auth/AuthContext'

import PageLayout from '../components/PageLayout'
import FormInput from '../components/FormInput'
import Alert from '../components/Alert'

import {
  listarFuncionarios,
  listarTerceirizados,
  listarTurmasTerceirizadas,
  listarFuncionariosOs,
  adicionarFuncionarioOs,
  removerFuncionarioOs,
  obterOrdemServico
} from '../api/api'

// ✅ React Query
import { useQuery, useMutation } from '@tanstack/react-query'

export default function OrdemServicoFuncionarios() {

  const { id } = useParams()
  const navigate = useNavigate()
  const { user } = useAuth()

  const podeEditarPerfil = user?.perfilId !== 2

  const [tipo, setTipo] = useState('FUNCIONARIO')

  const [funcionarioId, setFuncionarioId] = useState('')
  const [terceirizadoId, setTerceirizadoId] = useState('')
  const [turmaId, setTurmaId] = useState('')

  const [quantidadePessoas, setQuantidadePessoas] = useState('1')
  const [horasPrevistas, setHorasPrevistas] = useState('')
  const [custoHoraPrevisto, setCustoHoraPrevisto] = useState('')
  const [observacao, setObservacao] = useState('')

  const [erro, setErro] = useState('')
  const [sucesso, setSucesso] = useState('')

  // 🔥 QUERIES
  const { data: funcionariosRes } = useQuery({
    queryKey: ['funcionarios'],
    queryFn: listarFuncionarios
  })

  const { data: terceirizadosRes } = useQuery({
    queryKey: ['terceirizados'],
    queryFn: listarTerceirizados
  })

  const { data: turmasRes } = useQuery({
    queryKey: ['turmas'],
    queryFn: listarTurmasTerceirizadas
  })

  const {
    data: listaRes,
    refetch
  } = useQuery({
    queryKey: ['funcionarios-os', id],
    queryFn: () => listarFuncionariosOs(id)
  })

  const { data: ordemRes } = useQuery({
    queryKey: ['ordem-servico', id],
    queryFn: () => obterOrdemServico(id)
  })

  const funcionarios = funcionariosRes?.data?.data || []
  const terceirizados = terceirizadosRes?.data?.data || []
  const turmas = turmasRes?.data?.data || []
  const lista = listaRes?.data?.data || []
  const ordem = ordemRes?.data?.data || null

  function podeEditar() {
    return podeEditarPerfil && ordem?.status !== 'FINALIZADA'
  }

  function limparCampos() {
    setFuncionarioId('')
    setTerceirizadoId('')
    setTurmaId('')
  }

  function getSelecionadoId() {
    if (tipo === 'FUNCIONARIO') return funcionarioId
    if (tipo === 'TERCEIRIZADO') return terceirizadoId
    if (tipo === 'TURMA') return turmaId
  }

  // 🔥 MUTATION ADD
  const addMutation = useMutation({
    mutationFn: (payload) => adicionarFuncionarioOs(id, payload),
    onSuccess: (res) => {
      if (!res.data?.success) {
        setErro('Erro ao adicionar')
        return
      }

      setSucesso('Adicionado com sucesso')

      limparCampos()
      setHorasPrevistas('')
      setCustoHoraPrevisto('')
      setObservacao('')

      refetch()
    },
    onError: () => setErro('Erro ao salvar')
  })

  // 🔥 MUTATION REMOVE
  const removeMutation = useMutation({
    mutationFn: (itemId) => removerFuncionarioOs(id, itemId),
    onSuccess: () => refetch(),
    onError: () => setErro('Erro ao remover')
  })

  async function handleSubmit(e) {
    e.preventDefault()
    setErro('')
    setSucesso('')

    if (!podeEditar()) {
      setErro('OS não pode ser alterada')
      return
    }

    const selecionadoId = getSelecionadoId()

    if (!selecionadoId) {
      setErro('Selecione o item')
      return
    }

    const jaExiste = lista.some(i =>
      i.tipoMaoObra === tipo &&
      String(i.funcionarioId || i.terceirizadoId || i.turmaId) === selecionadoId
    )

    if (jaExiste) {
      setErro('Já adicionado')
      return
    }

    const payload = {
      tipoMaoObra: tipo,
      funcionarioId: tipo === 'FUNCIONARIO' ? Number(funcionarioId) : null,
      terceirizadoId: tipo === 'TERCEIRIZADO' ? Number(terceirizadoId) : null,
      turmaId: tipo === 'TURMA' ? Number(turmaId) : null,
      quantidadePessoas: Number(quantidadePessoas),
      horasPrevistas: Number(horasPrevistas),
      custoHoraPrevisto: Number(custoHoraPrevisto),
      observacao
    }

    addMutation.mutate(payload)
  }

  function remover(itemId) {
    if (!podeEditar()) {
      setErro('OS não pode ser alterada')
      return
    }

    removeMutation.mutate(itemId)
  }

  function renderSelect() {

    const disabled = !podeEditar()

    if (tipo === 'FUNCIONARIO') {
      return (
        <select value={funcionarioId} onChange={e => setFuncionarioId(e.target.value)} disabled={disabled}>
          <option value="">Selecione funcionário</option>
          {funcionarios.map(f => (
            <option key={f.id} value={f.id}>{f.nome}</option>
          ))}
        </select>
      )
    }

    if (tipo === 'TERCEIRIZADO') {
      return (
        <select value={terceirizadoId} onChange={e => setTerceirizadoId(e.target.value)} disabled={disabled}>
          <option value="">Selecione terceirizado</option>
          {terceirizados.map(t => (
            <option key={t.id} value={t.id}>{t.nome}</option>
          ))}
        </select>
      )
    }

    return (
      <select value={turmaId} onChange={e => setTurmaId(e.target.value)} disabled={disabled}>
        <option value="">Selecione turma</option>
        {turmas.map(t => (
          <option key={t.id} value={t.id}>{t.nome}</option>
        ))}
      </select>
    )
  }

  return (
    <PageLayout
      title="Mão de Obra da OS"
      showBack
      backTo={`/ordens-servico/${id}/maquinas`}
    >

      {erro && <Alert type="error" message={erro} />}
      {sucesso && <Alert type="success" message={sucesso} />}

      <form className="form-container" onSubmit={handleSubmit}>

        <div>
          <label>Tipo</label>
          <select
            value={tipo}
            onChange={e => {
              setTipo(e.target.value)
              limparCampos()
            }}
            disabled={!podeEditar()}
          >
            <option value="FUNCIONARIO">Funcionário</option>
            <option value="TERCEIRIZADO">Terceirizado</option>
            <option value="TURMA">Turma</option>
          </select>
        </div>

        <div>
          <label>Seleção</label>
          {renderSelect()}
        </div>

        <FormInput
          label="Quantidade de Pessoas"
          type="number"
          value={quantidadePessoas}
          onChange={e => setQuantidadePessoas(e.target.value)}
          disabled={!podeEditar()}
        />

        <FormInput
          label="Horas Previstas"
          type="number"
          value={horasPrevistas}
          onChange={e => setHorasPrevistas(e.target.value)}
          disabled={!podeEditar()}
        />

        <FormInput
          label="Custo Hora"
          type="number"
          value={custoHoraPrevisto}
          onChange={e => setCustoHoraPrevisto(e.target.value)}
          disabled={!podeEditar()}
        />

        <FormInput
          label="Observação"
          value={observacao}
          onChange={e => setObservacao(e.target.value)}
          disabled={!podeEditar()}
        />

        {podeEditar() && (
          <div className="form-actions">
            <button type="submit" className="add-btn">
              Adicionar
            </button>
          </div>
        )}

      </form>

      {lista.length > 0 && (
        <div className="card-list">
          {lista.map(item => (
            <div key={item.id} className="card">
              Tipo: {item.tipoMaoObra}
              <br />
              Horas: {item.horasPrevistas}
              <br />
              Custo: {item.custoTotalPrevisto}

              {podeEditar() && (
                <button
                  className="icon-button danger"
                  onClick={() => remover(item.id)}
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
          onClick={() => navigate(`/ordens-servico/${id}`)}
        >
          Finalizar Execução →
        </button>
      </div>

    </PageLayout>
  )
}