import { useState, useEffect } from 'react'
import { useParams } from 'react-router-dom'

import PageLayout from '../components/PageLayout'
import Alert from '../components/Alert'

import {
  listarTurmas,
  registrarApontamentoTurma,
  listarApontamentosTurmaPorOs
} from '../api/api'

export default function OrdemServicoTurmas() {

  const { id } = useParams()

  const [turmas, setTurmas] = useState([])
  const [turmaId, setTurmaId] = useState('')

  const [diasTrabalhados, setDiasTrabalhados] = useState('')
  const [quantidadeColhida, setQuantidadeColhida] = useState('')
  const [observacao, setObservacao] = useState('')

  const [apontamentos, setApontamentos] = useState([])

  const [erro, setErro] = useState('')
  const [sucesso, setSucesso] = useState('')

  useEffect(() => {
    carregarTurmas()
    carregarApontamentos()
  }, [])

  async function carregarTurmas() {
    try {
      const res = await listarTurmas()
      setTurmas(res.data?.data || [])
    } catch (err) {
      setErro('Erro ao carregar turmas')
    }
  }

  async function carregarApontamentos() {
    try {
      const res = await listarApontamentosTurmaPorOs(id)
      setApontamentos(res.data?.data || [])
    } catch (err) {
      setErro('Erro ao carregar apontamentos')
    }
  }

  async function handleSubmit(e) {
    e.preventDefault()
    setErro('')
    setSucesso('')

    if (!turmaId) {
      setErro('Selecione a turma')
      return
    }

    try {
      await registrarApontamentoTurma({
        ordemServicoId: Number(id), // 🔥 NOVO
        turmaId: Number(turmaId),
        diasTrabalhados: diasTrabalhados ? Number(diasTrabalhados) : null,
        quantidadeColhida: quantidadeColhida ? Number(quantidadeColhida) : null,
        dataInicio: new Date().toISOString().split('T')[0],
        dataFim: new Date().toISOString().split('T')[0],
        observacao
      })

      setSucesso('Apontamento registrado com sucesso')

      setTurmaId('')
      setDiasTrabalhados('')
      setQuantidadeColhida('')
      setObservacao('')

      await carregarApontamentos()

    } catch (err) {
      console.error(err)
      setErro('Erro ao registrar apontamento')
    }
  }

  // 🔥 TOTAL POR TURMA
  const totalPorTurma = apontamentos.reduce((acc, a) => {
    const key = a.turmaNome || 'Sem nome'

    if (!acc[key]) acc[key] = 0

    acc[key] += Number(a.valorTotal || 0)

    return acc
  }, {})

  // 🔥 TOTAL GERAL
  const totalGeral = apontamentos.reduce(
    (t, a) => t + Number(a.valorTotal || 0),
    0
  )

  return (
    <PageLayout
      title="Turmas da OS"
      showBack
      backTo={`/ordens-servico/${id}`}
    >

      {erro && <Alert type="error" message={erro} />}
      {sucesso && <Alert type="success" message={sucesso} />}

      <form className="form-container" onSubmit={handleSubmit}>

        <div>
          <label>Turma</label>
          <select
            value={turmaId}
            onChange={e => setTurmaId(e.target.value)}
          >
            <option value="">Selecione</option>
            {turmas.map(t => (
              <option key={t.id} value={String(t.id)}>
                {t.nome}
              </option>
            ))}
          </select>
        </div>

        <div className="form-grid-2">
          <div>
            <label>Dias Trabalhados</label>
            <input
              type="number"
              value={diasTrabalhados}
              onChange={e => setDiasTrabalhados(e.target.value)}
            />
          </div>

          <div>
            <label>Quantidade Colhida</label>
            <input
              type="number"
              value={quantidadeColhida}
              onChange={e => setQuantidadeColhida(e.target.value)}
            />
          </div>
        </div>

        <div>
          <label>Observação</label>
          <input
            value={observacao}
            onChange={e => setObservacao(e.target.value)}
          />
        </div>

        <div className="form-actions">
          <button type="submit" className="add-btn">
            Registrar Apontamento
          </button>
        </div>

      </form>

      {/* 🔥 LISTA */}
      {apontamentos.length > 0 && (
        <div style={{ marginTop: 20 }}>

          <table className="table-insumos compact">

            <thead>
              <tr>
                <th>Turma</th>
                <th>Período</th>
                <th>Dias</th>
                <th>Qtd Colhida</th>
                <th>Valor</th>
              </tr>
            </thead>

            <tbody>
              {apontamentos.map(a => (
                <tr key={a.id}>

                  <td>{a.turmaNome}</td>

                  <td>
                    {a.dataInicio} - {a.dataFim}
                  </td>

                  <td>{a.diasTrabalhados || '-'}</td>

                  <td>{a.quantidadeColhida || '-'}</td>

                  <td className="col-custo">
                    R$ {Number(a.valorTotal).toFixed(2)}
                  </td>

                </tr>
              ))}
            </tbody>

          </table>

        </div>
      )}

      {/* 🔥 RESUMO */}
      {apontamentos.length > 0 && (
        <div style={{ marginTop: 20 }}>

          <h4>Total por Turma</h4>

          <table className="table-insumos compact">
            <thead>
              <tr>
                <th>Turma</th>
                <th>Total</th>
              </tr>
            </thead>

            <tbody>
              {Object.entries(totalPorTurma).map(([nome, total]) => (
                <tr key={nome}>
                  <td>{nome}</td>
                  <td className="col-custo">
                    R$ {total.toFixed(2)}
                  </td>
                </tr>
              ))}
            </tbody>

            <tfoot>
              <tr>
                <td><strong>Total Geral</strong></td>
                <td className="col-custo">
                  <strong>R$ {totalGeral.toFixed(2)}</strong>
                </td>
              </tr>
            </tfoot>

          </table>

        </div>
      )}

    </PageLayout>
  )
}