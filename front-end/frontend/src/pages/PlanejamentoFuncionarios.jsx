import { useState, useEffect } from 'react'
import { useParams } from 'react-router-dom'

import PageLayout from '../components/PageLayout'
import FormInput from '../components/FormInput'
import Alert from '../components/Alert'

import {
  listarFuncionarios,
  listarTerceirizados,
  listarTurmas,
  adicionarFuncionarioPlanejamento
} from '../api/api'

export default function PlanejamentoFuncionarios() {

  const { id } = useParams()

  const [tipo, setTipo] = useState('FUNCIONARIO')

  const [funcionarios, setFuncionarios] = useState([])
  const [terceirizados, setTerceirizados] = useState([])
  const [turmas, setTurmas] = useState([])

  const [funcionarioId, setFuncionarioId] = useState('')
  const [terceirizadoId, setTerceirizadoId] = useState('')
  const [turmaId, setTurmaId] = useState('')
  const [quantidadePessoas, setQuantidadePessoas] = useState('')

  const [lista, setLista] = useState([])
  const [totalCusto, setTotalCusto] = useState(0)

  const [erro, setErro] = useState('')
  const [sucesso, setSucesso] = useState('')

  useEffect(() => {
    carregarDados()
  }, [])

  async function carregarDados() {
    try {
      const [resFunc, resTerc, resTurma] = await Promise.all([
        listarFuncionarios(),
        listarTerceirizados(),
        listarTurmas()
      ])

      // 🔥 GARANTE ARRAY SEMPRE
      setFuncionarios(
        resFunc.data?.data?.content ||
        resFunc.data?.data ||
        []
      )

      setTerceirizados(
        resTerc.data?.data?.content ||
        resTerc.data?.data ||
        []
      )

      setTurmas(
        resTurma.data?.data?.content ||
        resTurma.data?.data ||
        []
      )

    } catch (err) {
      console.error(err)
      setErro('Erro ao carregar dados')
    }
  }

  function calcularCusto(payload) {

    // FUNCIONÁRIO (placeholder)
    if (payload.tipo === 'FUNCIONARIO') {
      return 0
    }

    // TERCEIRIZADO (exemplo simples)
    if (payload.tipo === 'TERCEIRIZADO') {
      return (payload.quantidadePessoas || 0) * 100
    }

    // TURMA
    if (payload.tipo === 'TURMA') {
      const turma = turmas.find(t => t.id == payload.turmaId)
      if (!turma) return 0

      return (turma.valorDiaria || 0) * (turma.quantidadePessoas || 0)
    }

    return 0
  }

  async function handleSubmit(e) {
    e.preventDefault()
    setErro('')
    setSucesso('')

    if (tipo === 'FUNCIONARIO' && !funcionarioId) {
      setErro('Selecione o funcionário')
      return
    }

    if (tipo === 'TERCEIRIZADO' && !terceirizadoId) {
      setErro('Selecione o terceirizado')
      return
    }

    if (tipo === 'TURMA' && !turmaId) {
      setErro('Selecione a turma')
      return
    }

    const jaExiste = lista.some(i =>
      (tipo === 'FUNCIONARIO' && i.funcionarioId == funcionarioId) ||
      (tipo === 'TERCEIRIZADO' && i.terceirizadoId == terceirizadoId) ||
      (tipo === 'TURMA' && i.turmaId == turmaId)
    )

    if (jaExiste) {
      setErro('Item já adicionado')
      return
    }

    try {

      const payload = {
        tipo,
        funcionarioId: tipo === 'FUNCIONARIO' ? Number(funcionarioId) : null,
        terceirizadoId: tipo === 'TERCEIRIZADO' ? Number(terceirizadoId) : null,
        turmaId: tipo === 'TURMA' ? Number(turmaId) : null,
        quantidadePessoas: tipo !== 'TURMA'
          ? Number(quantidadePessoas || 0)
          : null
      }

      const res = await adicionarFuncionarioPlanejamento(id, payload)

      if (!res.data?.success) {
        setErro('Erro ao adicionar')
        return
      }

      const novoItem = {
        ...payload,
        funcionarioNome: funcionarios.find(f => f.id == funcionarioId)?.nome,
        terceirizadoNome: terceirizados.find(t => t.id == terceirizadoId)?.nome,
        turmaNome: turmas.find(t => t.id == turmaId)?.nome,
        custo: calcularCusto(payload)
      }

      const novaLista = [...lista, novoItem]

      const total = novaLista.reduce((t, i) => t + (i.custo || 0), 0)

      setLista(novaLista)
      setTotalCusto(total)

      setSucesso('Adicionado com sucesso')

      setFuncionarioId('')
      setTerceirizadoId('')
      setTurmaId('')
      setQuantidadePessoas('')

    } catch (err) {
      console.error(err)
      setErro('Erro ao salvar')
    }
  }

  return (
    <PageLayout
      title="Funcionários do Planejamento"
      showBack
      backTo="/safra-talhoes"
    >

      {erro && <Alert type="error" message={erro} />}
      {sucesso && <Alert type="success" message={sucesso} />}

      <form className="form-container" onSubmit={handleSubmit}>

        <div>
          <label>Tipo</label>
          <select value={tipo} onChange={e => setTipo(e.target.value)}>
            <option value="FUNCIONARIO">Funcionário</option>
            <option value="TERCEIRIZADO">Terceirizado</option>
            <option value="TURMA">Turma</option>
          </select>
        </div>

        {tipo === 'FUNCIONARIO' && (
          <div>
            <label>Funcionário</label>
            <select value={funcionarioId} onChange={e => setFuncionarioId(e.target.value)}>
              <option value="">Selecione</option>
              {funcionarios.map(f => (
                <option key={f.id} value={f.id}>{f.nome}</option>
              ))}
            </select>
          </div>
        )}

        {tipo === 'TERCEIRIZADO' && (
          <div>
            <label>Terceirizado</label>
            <select value={terceirizadoId} onChange={e => setTerceirizadoId(e.target.value)}>
              <option value="">Selecione</option>
              {terceirizados.map(t => (
                <option key={t.id} value={t.id}>{t.nome}</option>
              ))}
            </select>
          </div>
        )}

        {tipo === 'TURMA' && (
          <div>
            <label>Turma</label>
            <select value={turmaId} onChange={e => setTurmaId(e.target.value)}>
              <option value="">Selecione</option>
              {turmas.map(t => (
                <option key={t.id} value={t.id}>{t.nome}</option>
              ))}
            </select>
          </div>
        )}

        {tipo !== 'TURMA' && (
          <FormInput
            label="Quantidade de Pessoas"
            type="number"
            value={quantidadePessoas}
            onChange={e => setQuantidadePessoas(e.target.value)}
          />
        )}

        <div className="form-actions">
          <button type="submit" className="add-btn">
            Adicionar
          </button>
        </div>

      </form>

      {lista.length > 0 && (
        <div className="card-list">
          {lista.map((i, index) => (
            <div key={index} className="card">
              {i.funcionarioNome ||
               i.terceirizadoNome ||
               i.turmaNome}
              {' '}| Qtde: {i.quantidadePessoas || '-'}
              {' '}| Custo: R$ {(i.custo || 0).toFixed(2)}
            </div>
          ))}
        </div>
      )}

      {lista.length > 0 && (
        <div style={{ marginTop: 20, fontWeight: 'bold' }}>
          Total Estimado: R$ {totalCusto.toFixed(2)}
        </div>
      )}

    </PageLayout>
  )
}