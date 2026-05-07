import { useState, useEffect } from 'react'
import { useNavigate, useParams } from 'react-router-dom'

import PageLayout from '../components/PageLayout'
import FormInput from '../components/FormInput'
import Alert from '../components/Alert'

import {
  criarFolha,
  buscarFolha,
  atualizarFolha,
  listarFuncionarios
} from '../api/api'

import { obterParametrosEncargos } from '../api/api'

export default function FolhaPagamentoForm() {

  const navigate = useNavigate()
  const { id } = useParams()

  const [funcionarioId, setFuncionarioId] = useState('')
  const [salarioBase, setSalarioBase] = useState('')
  const [mesAno, setMesAno] = useState('')

  const [funcionarios, setFuncionarios] = useState([])

  const [percentual, setPercentual] = useState(0)

  const [erro, setErro] = useState('')
  const [sucesso, setSucesso] = useState('')

  useEffect(() => {
    async function carregarFuncionarios() {
      try {
        const res = await listarFuncionarios()
        setFuncionarios(
          res.data?.data?.content ||
          res.data?.data ||
          []
        )
      } catch (err) {
        console.error(err)
        setErro('Erro ao carregar funcionários')
      }
    }

    async function carregarPercentual() {
      try {
        const res = await api.get('/parametros/encargos')
        setPercentual(res.data?.data?.percentual || 0)
      } catch (err) {
        console.error(err)
      }
    }

    async function carregar() {
      try {
        const res = await buscarFolha(id)
        const d = res.data?.data
        setFuncionarioId(d.funcionarioId)
        setSalarioBase(d.salarioBase)
        setMesAno(d.mesAno)
      } catch (err) {
        console.error(err)
        setErro('Erro ao carregar')
      }
    }

    carregarFuncionarios()
    carregarPercentual()
    if (id) carregar()
  }, [id])

  async function handleSubmit(e) {
    e.preventDefault()

    const payload = {
      funcionarioId: Number(funcionarioId),
      salarioBase: Number(salarioBase),
      mesAno
    }

    try {

      if (id) {
        await atualizarFolha(id, payload)
      } else {
        await criarFolha(payload)
      }

      setSucesso('Salvo com sucesso')
      setTimeout(() => navigate('/folha'), 600)

    } catch (err) {
      console.error(err)
      setErro('Erro ao salvar')
    }
  }

  // 🔥 PREVIEW
  const salario = Number(salarioBase || 0)
  const encargos = salario * percentual
  const total = salario + encargos

  return (
    <PageLayout title="Folha de Pagamento" showBack backTo="/folha">

      {erro && <Alert type="error" message={erro} />}
      {sucesso && <Alert type="success" message={sucesso} />}

      <form onSubmit={handleSubmit} className="form-container">

        {/* FUNCIONÁRIO */}
        <div>
          <label>Funcionário</label>
          <select
            value={funcionarioId}
            onChange={e => {
              const id = e.target.value
              setFuncionarioId(id)

              const f = funcionarios.find(x => x.id == id)

              if (f) {
                setSalarioBase(f.salario || 0)
              }
            }}
          >
            <option value="">Selecione</option>

            {Array.isArray(funcionarios) && funcionarios.map(f => (
              <option key={f.id} value={f.id}>
                {f.nome}
              </option>
            ))}
          </select>
        </div>

        {/* MÊS/ANO */}
        <FormInput
          label="Mês/Ano"
          type="month"
          value={mesAno}
          onChange={e => setMesAno(e.target.value)}
        />

        {/* SALÁRIO BASE */}
        <FormInput
          label="Salário Base (R$)"
          type="number"
          value={salarioBase}
          onChange={e => setSalarioBase(e.target.value)}
        />

        {/* 🔥 PREVIEW ENCARGOS */}
        <div className="card" style={{ marginTop: 15 }}>
          <strong>Encargos ({(percentual * 100).toFixed(0)}%)</strong>
          <div>R$ {encargos.toFixed(2)}</div>
        </div>

        {/* 🔥 PREVIEW TOTAL */}
        <div className="card">
          <strong>Total</strong>
          <div>R$ {total.toFixed(2)}</div>
        </div>

        <button type="submit" className="add-btn">
          Salvar
        </button>

      </form>

    </PageLayout>
  )
}