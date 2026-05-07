import { useState, useEffect } from 'react'
import { useQuery, useMutation } from '@tanstack/react-query'

import FormInput from '../components/FormInput'
import Alert from '../components/Alert'

import {
  listarMaquinas,
  adicionarOperacaoCombustivel,
  listarOperacaoCombustivel,
  removerOperacaoCombustivel
} from '../api/api'

export default function OperacaoCombustivel({ operacaoId, setTotal, bloqueado }) {

  const [maquinaId, setMaquinaId] = useState('')
  const [litros, setLitros] = useState('')
  const [valorUnitario, setValorUnitario] = useState('')

  const [erro, setErro] = useState('')
  const [sucesso, setSucesso] = useState('')

  const { data: maquinasRes } = useQuery({
    queryKey: ['maquinas'],
    queryFn: listarMaquinas
  })

  const maquinas =
    maquinasRes?.data?.data?.content ||
    maquinasRes?.data?.data ||
    []

  const { data: listaRes, refetch } = useQuery({
    queryKey: ['operacao-combustivel', operacaoId],
    queryFn: () => listarOperacaoCombustivel(operacaoId)
  })

  const lista = Array.isArray(listaRes?.data?.data)
    ? listaRes.data.data
    : []

  const addMutation = useMutation({
    mutationFn: adicionarOperacaoCombustivel,
    onSuccess: () => {
      setSucesso('Combustível lançado')
      setMaquinaId('')
      setLitros('')
      setValorUnitario('')
      refetch()
    },
    onError: () => setErro('Erro ao salvar')
  })

  const removeMutation = useMutation({
    mutationFn: removerOperacaoCombustivel,
    onSuccess: () => refetch(),
    onError: () => setErro('Erro ao remover')
  })

  function handleSubmit(e) {
    e.preventDefault()

    if (!maquinaId) {
      setErro('Selecione a máquina')
      return
    }

    addMutation.mutate({
      operacaoTalhaoId: Number(operacaoId),
      maquinaId: Number(maquinaId),
      litros: Number(litros),
      valorUnitario: Number(valorUnitario)
    })
  }

  function remover(id) {
    if (bloqueado) return
    removeMutation.mutate(id)
  }

  const totalGeral = lista.reduce(
    (t, i) => t + (Number(i.litros) * Number(i.valorUnitario)),
    0
  )

  useEffect(() => {
    if (setTotal) setTotal(totalGeral)
  }, [totalGeral, setTotal])

  return (
    <div className="card" style={{ marginBottom: 20 }}>

      <h3>Combustível</h3>

      {erro && <Alert type="error" message={erro} />}
      {sucesso && <Alert type="success" message={sucesso} />}

      <form className="form-container" onSubmit={handleSubmit}>

        <div>
          <label>Máquina</label>
          <select
            value={maquinaId}
            onChange={e => setMaquinaId(e.target.value)}
            disabled={bloqueado}
          >
            <option value="">Selecione</option>
            {maquinas.map(m => (
              <option key={m.id} value={m.id}>
                {m.nome}
              </option>
            ))}
          </select>
        </div>

        <FormInput
          label="Litros"
          type="number"
          value={litros}
          onChange={e => setLitros(e.target.value)}
          disabled={bloqueado}
        />

        <FormInput
          label="Valor/Litro"
          type="number"
          value={valorUnitario}
          onChange={e => setValorUnitario(e.target.value)}
          disabled={bloqueado}
        />

        <div className="form-actions">
          <button className="add-btn" disabled={bloqueado}>
            Adicionar
          </button>
        </div>

      </form>

      {lista.length > 0 && (
        <div style={{ marginTop: 20 }}>

          <table className="table-insumos compact">

            <thead>
              <tr>
                <th>Máquina</th>
                <th>Litros</th>
                <th>Valor</th>
                <th>Total</th>
                <th></th>
              </tr>
            </thead>

            <tbody>
              {lista.map(i => (
                <tr key={i.id}>
                  <td>{i.maquinaNome}</td>
                  <td>{i.litros}</td>
                  <td>{i.valorUnitario}</td>
                  <td className="col-custo">
                    {(i.litros * i.valorUnitario).toFixed(2)}
                  </td>
                  <td>
                    <button
                      className="icon-button danger"
                      disabled={bloqueado}
                      onClick={() => remover(i.id)}
                    >
                      ❌
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>

            <tfoot>
              <tr>
                <td colSpan="3"><strong>Total</strong></td>
                <td className="col-custo">
                  <strong>{totalGeral.toFixed(2)}</strong>
                </td>
                <td></td>
              </tr>
            </tfoot>

          </table>

        </div>
      )}

    </div>
  )
}