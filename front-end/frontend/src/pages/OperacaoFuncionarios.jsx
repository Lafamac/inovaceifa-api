import { useState, useEffect } from 'react'
import { useQuery, useMutation } from '@tanstack/react-query'

import FormInput from '../components/FormInput'
import Alert from '../components/Alert'

import {
  listarFuncionarios,
  adicionarOperacaoFuncionario,
  listarOperacaoFuncionarios,
  removerOperacaoFuncionario
} from '../api/api'

export default function OperacaoFuncionarios({ operacaoId, setTotal, bloqueado }) {

  const [funcionarioId, setFuncionarioId] = useState('')
  const [horasTrabalhadas, setHorasTrabalhadas] = useState('')
  const [valorUnitario, setValorUnitario] = useState('')

  const [erro, setErro] = useState('')
  const [sucesso, setSucesso] = useState('')

  // 🔥 LISTAR FUNCIONÁRIOS
  const { data: funcionariosRes } = useQuery({
    queryKey: ['funcionarios'],
    queryFn: listarFuncionarios
  })

  const funcionarios =
    funcionariosRes?.data?.data?.content ||
    funcionariosRes?.data?.data ||
    []

  // 🔥 LISTA DA OPERAÇÃO
  const { data: listaRes, refetch } = useQuery({
    queryKey: ['operacao-funcionarios', operacaoId],
    queryFn: () => listarOperacaoFuncionarios(operacaoId)
  })

  const lista = Array.isArray(listaRes?.data?.data)
    ? listaRes.data.data
    : []

  // 🔥 ADD
  const addMutation = useMutation({
    mutationFn: adicionarOperacaoFuncionario,
    onSuccess: () => {
      setSucesso('Funcionário lançado')
      setFuncionarioId('')
      setHorasTrabalhadas('')
      setValorUnitario('')
      refetch()
    },
    onError: () => setErro('Erro ao adicionar')
  })

  // 🔥 REMOVE
  const removeMutation = useMutation({
    mutationFn: removerOperacaoFuncionario,
    onSuccess: () => refetch(),
    onError: () => setErro('Erro ao remover')
  })

  function handleSubmit(e) {
    e.preventDefault()
    setErro('')
    setSucesso('')

    if (!funcionarioId) {
      setErro('Selecione o funcionário')
      return
    }

    addMutation.mutate({
      operacaoTalhaoId: Number(operacaoId),
      funcionarioId: Number(funcionarioId),
      horasTrabalhadas: Number(horasTrabalhadas),
      valorUnitario: Number(valorUnitario)
    })
  }

  function remover(id) {
    if (bloqueado) return
    removeMutation.mutate(id)
  }

  // 🔥 TOTAL
  const totalGeral = lista.reduce(
    (t, i) =>
      t + (Number(i.horasTrabalhadas) * Number(i.valorUnitario)),
    0
  )

  // 🔥 ENVIA TOTAL PARA PAI
  useEffect(() => {
    if (setTotal) setTotal(totalGeral)
  }, [totalGeral])

  return (
    <div className="card" style={{ marginBottom: 20 }}>

      <h3>Funcionários</h3>

      {erro && <Alert type="error" message={erro} />}
      {sucesso && <Alert type="success" message={sucesso} />}

      <form className="form-container" onSubmit={handleSubmit}>

        <div>
          <label>Funcionário</label>
          <select
            value={funcionarioId}
            onChange={e => setFuncionarioId(e.target.value)}
            disabled={bloqueado}
          >
            <option value="">Selecione</option>
            {funcionarios.map(f => (
              <option key={f.id} value={f.id}>
                {f.nome}
              </option>
            ))}
          </select>
        </div>

        <FormInput
          label="Horas Trabalhadas"
          type="number"
          value={horasTrabalhadas}
          onChange={e => setHorasTrabalhadas(e.target.value)}
          disabled={bloqueado}
        />

        <FormInput
          label="Valor Hora"
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

      {/* 🔥 TABELA */}
      {lista.length > 0 && (
        <div style={{ marginTop: 20 }}>

          <table className="table-insumos compact">

            <thead>
              <tr>
                <th>Funcionário</th>
                <th>Horas</th>
                <th>Valor Hora</th>
                <th>Total</th>
                <th></th>
              </tr>
            </thead>

            <tbody>
              {lista.map(i => (
                <tr key={i.id}>
                  <td>
                    {i.funcionarioNome ||
                     i.funcionario?.nome ||
                     '-'}
                  </td>
                  <td>{i.horasTrabalhadas}</td>
                  <td>{i.valorUnitario}</td>
                  <td className="col-custo">
                    {(i.horasTrabalhadas * i.valorUnitario).toFixed(2)}
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