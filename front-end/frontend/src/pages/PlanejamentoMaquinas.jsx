import { useState, useEffect } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { useAuth } from '../auth/AuthContext'
import { Trash2, Pencil } from 'lucide-react'

import PageLayout from '../components/PageLayout'
import FormInput from '../components/FormInput'
import Alert from '../components/Alert'

import {
  adicionarMaquinaPlanejamento,
  listarMaquinas,
  listarMaquinasPlanejamento,
  removerMaquinaPlanejamento,
  obterResumoPlanejamento
} from '../api/api'

export default function PlanejamentoMaquinas() {

  const { id } = useParams()
  const navigate = useNavigate()
  const { user } = useAuth()

  const podeEditar = user?.perfilId === 2

  const [maquinaId, setMaquinaId] = useState('')
  const [horasPrevistas, setHorasPrevistas] = useState('')
  const [custoHora, setCustoHora] = useState('')

  const [maquinas, setMaquinas] = useState([])
  const [itens, setItens] = useState([])
  const [resumo, setResumo] = useState(null)

  const [erro, setErro] = useState('')
  const [sucesso, setSucesso] = useState('')



  async function carregarTudo() {
    await Promise.all([
      carregarMaquinas(),
      carregarItens(),
      carregarResumo()
    ])
  }

  async function carregarMaquinas() {
    const res = await listarMaquinas()
    setMaquinas(res.data?.data?.content || res.data?.data || [])
  }

  async function carregarItens() {
    const res = await listarMaquinasPlanejamento(id)
    setItens(res.data?.data || [])
  }

  async function carregarResumo() {
    const res = await obterResumoPlanejamento(id)
    setResumo(res.data?.data)
  }

  useEffect(() => {
    carregarTudo()
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [id])

  function editar(item) {
    setMaquinaId(String(item.maquinaId || item.maquina?.id))
    setHorasPrevistas(item.horasPrevistas)
    setCustoHora(item.custoHora)
  }

  async function handleSubmit(e) {
    e.preventDefault()
    setErro('')
    setSucesso('')

    if (!podeEditar) {
      setErro('Sem permissão')
      return
    }

    if (!maquinaId || !horasPrevistas) {
      setErro('Preencha os campos')
      return
    }

    try {
      await adicionarMaquinaPlanejamento(id, {
        maquinaId: Number(maquinaId),
        horasPrevistas: Number(horasPrevistas),
        custoHora: custoHora ? Number(custoHora) : null // 🔥 backend resolve fallback
      })

      setSucesso('Máquina adicionada')

      setMaquinaId('')
      setHorasPrevistas('')
      setCustoHora('')

      await carregarItens()
      await carregarResumo()

    } catch (err) {
      console.error(err)
      setErro('Erro ao salvar')
    }
  }

  async function remover(itemId) {
    if (!podeEditar) return

    try {
      await removerMaquinaPlanejamento(id, itemId)
      await carregarItens()
      await carregarResumo()
    } catch (err) {
      console.error(err)
      setErro('Erro ao remover')
    }
  }

  // 🔥 TOTAL HORAS
  const totalHoras = itens.reduce((t, i) => t + Number(i.horasPrevistas || 0), 0)

  // 🔥 TOTAL CUSTO
  const totalCusto = itens.reduce((t, i) => {
    const custo = i.horasPrevistas * i.custoHora
    return t + custo
  }, 0)

  return (
    <PageLayout
      title="Máquinas do Planejamento"
      showBack
      backTo={`/planejamento/${id}/insumos`}
    >

      {erro && <Alert type="error" message={erro} />}
      {sucesso && <Alert type="success" message={sucesso} />}

      <form className="form-container" onSubmit={handleSubmit}>

        <div>
          <label>Máquina</label>
          <select
            value={maquinaId}
            onChange={e => {
              const idSelecionado = e.target.value
              setMaquinaId(idSelecionado)

              const maq = maquinas.find(m => String(m.id) === idSelecionado)

              if (maq?.custoHora) {
                setCustoHora(maq.custoHora) // 🔥 AUTO
              }
            }}
            disabled={!podeEditar}
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
          disabled={!podeEditar}
        />

        <FormInput
          label="Custo por Hora (opcional)"
          type="number"
          value={custoHora}
          onChange={e => setCustoHora(e.target.value)}
          disabled={!podeEditar}
        />

        {podeEditar && (
          <div className="form-actions">
            <button type="submit" className="add-btn">
              Adicionar
            </button>
          </div>
        )}

      </form>

      {itens.length > 0 && (
        <div style={{ marginTop: 20 }}>

          <table className="table-insumos compact">
            <thead>
              <tr>
                <th>Máquina</th>
                <th>Horas</th>
                <th>Custo/Hora</th>
                <th>Custo</th>
                <th style={{ width: 100 }}>Ações</th>
              </tr>
            </thead>

            <tbody>
              {itens.map(i => {

                const custo = i.horasPrevistas * i.custoHora

                return (
                  <tr key={i.id}>

                    <td>{i.maquinaNome || i.maquinaId}</td>

                    <td>{i.horasPrevistas}</td>

                    <td>R$ {Number(i.custoHora).toFixed(2)}</td>

                    <td className="col-custo">
                      R$ {custo.toFixed(2)}
                    </td>

                    <td className="acoes">

                      <button
                        className="icon-button edit"
                        onClick={() => editar(i)}
                        title="Editar"
                      >
                        <Pencil size={16} />
                      </button>

                      <button
                        className="icon-button danger"
                        onClick={() => remover(i.id)}
                        title="Remover"
                      >
                        <Trash2 size={16} />
                      </button>

                    </td>

                  </tr>
                )
              })}
            </tbody>

            <tfoot>
              <tr>
                <td style={{ fontWeight: 600 }}>Totais:</td>

                <td style={{ fontWeight: 600 }}>
                  {totalHoras.toFixed(2)}
                </td>

                <td></td>

                <td className="col-custo">
                  R$ {totalCusto.toFixed(2)}
                </td>

                <td></td>
              </tr>
            </tfoot>

          </table>

        </div>
      )}

      <div style={{ marginTop: 20 }}>
        <button
          className="add-btn"
          onClick={() => navigate(`/planejamento/${id}/funcionarios`)}
        >
          Avançar para Mão de Obra →
        </button>
      </div>

    </PageLayout>
  )
}