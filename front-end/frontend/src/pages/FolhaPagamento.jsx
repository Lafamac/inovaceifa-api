import { useState, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'

import PageLayout from '../components/PageLayout'
import Alert from '../components/Alert'

import { listarFolha } from '../api/api'

export default function FolhaPagamento() {

  const navigate = useNavigate()

  const [dados, setDados] = useState([])
  const [erro, setErro] = useState('')
  const [loading, setLoading] = useState(false)

  useEffect(() => {
    carregar()
  }, [])

  async function carregar() {
    setLoading(true)
    try {
      const res = await listarFolha()

      const lista =
        res.data?.data?.content ||
        res.data?.data ||
        []

      console.log('FOLHA RESPONSE:', res.data)
      console.log('LISTA FINAL:', lista)

      setDados(Array.isArray(lista) ? lista : [])

    } catch (err) {
      console.error(err)
      setErro('Erro ao carregar folha')
    } finally {
      setLoading(false)
    }
  }

  return (
    <PageLayout title="Folha de Pagamento">

      {erro && <Alert type="error" message={erro} />}

      <button
        className="add-btn"
        onClick={() => navigate('/folha/novo')}
      >
        Nova Folha
      </button>

      {loading && <p>Carregando...</p>}

      {!loading && dados.length === 0 && (
        <p>Nenhum registro encontrado</p>
      )}

      {!loading && dados.length > 0 && (
        <table className="table-insumos">
          <thead>
            <tr>
              <th>Funcionário</th>
              <th>Mês</th>
              <th>Salário</th>
              <th>Encargos</th>
              <th>Total</th>
            </tr>
          </thead>
          <tbody>
            {dados.map(f => (
              <tr key={f.id}>
                <td>
                  {f.funcionarioNome ||
                   f.funcionario?.nome ||
                   '-'}
                </td>
                <td>{f.mesAno}</td>
                <td>R$ {Number(f.salarioBase || 0).toFixed(2)}</td>
                <td>R$ {Number(f.encargos || 0).toFixed(2)}</td>
                <td><strong>R$ {Number(f.total || 0).toFixed(2)}</strong></td>
              </tr>
            ))}
          </tbody>
        </table>
      )}

    </PageLayout>
  )
}