import { useParams } from 'react-router-dom'
import { useQuery } from '@tanstack/react-query'

import PageLayout from '../components/PageLayout'
import Alert from '../components/Alert'

import OperacaoProdutos from './OperacaoProdutos'
import OperacaoCombustivel from './OperacaoCombustivel'
import OperacaoFuncionarios from './OperacaoFuncionarios'

import { obterOperacaoDetalhe } from '../api/api'

export default function OperacaoDetalhe() {

  const { id } = useParams()

  const { data, isLoading, error } = useQuery({
    queryKey: ['operacao-detalhe', id],
    queryFn: () => obterOperacaoDetalhe(id)
  })

  const op = data?.data?.data || null

  const bloqueado = op?.statusOs === 'FINALIZADA'

  if (isLoading) {
    return <PageLayout title="Operação">Carregando...</PageLayout>
  }

  if (!op) {
    return (
      <PageLayout title="Operação">
        <Alert type="error" message="Operação não encontrada" />
      </PageLayout>
    )
  }

  return (
    <PageLayout
      title={`Operação ${op.id} - Talhão ${op.talhao}`}
      showBack
      backTo="/ordens-servico"
    >

      {/* 🔥 RESUMO */}
      <div className="card" style={{ marginBottom: 20 }}>

        <h3>Resumo de Custos</h3>

        <p><strong>Área:</strong> {op.area} ha</p>

        <hr />

        <p>Insumos: R$ {op.custos.insumos}</p>
        <p>Combustível: R$ {op.custos.combustivel}</p>
        <p>Máquinas: R$ {op.custos.maquinas}</p>
        <p>Funcionários: R$ {op.custos.funcionarios}</p>

        <hr />

        <h2>Total: R$ {op.custos.total}</h2>

        {bloqueado && (
          <p style={{ color: 'red' }}>
            🔒 OS finalizada
          </p>
        )}

      </div>

      {/* 🔥 TABELA PRODUTOS */}
      {op.produtos?.length > 0 && (
        <div className="card" style={{ marginBottom: 20 }}>
          <h3>Produtos</h3>

          <table className="table-insumos compact">
            <thead>
              <tr>
                <th>Produto</th>
                <th>Qtd</th>
                <th>Total</th>
              </tr>
            </thead>
            <tbody>
              {op.produtos.map((p, i) => (
                <tr key={i}>
                  <td>{p.nome}</td>
                  <td>{p.quantidade}</td>
                  <td>{p.total}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      {/* 🔥 TABELA COMBUSTÍVEL */}
      {op.combustivel?.length > 0 && (
        <div className="card" style={{ marginBottom: 20 }}>
          <h3>Combustível</h3>

          <table className="table-insumos compact">
            <thead>
              <tr>
                <th>Máquina</th>
                <th>Litros</th>
                <th>Total</th>
              </tr>
            </thead>
            <tbody>
              {op.combustivel.map((c, i) => (
                <tr key={i}>
                  <td>{c.maquina}</td>
                  <td>{c.litros}</td>
                  <td>{c.total}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      {/* 🔥 TABELA MÁQUINAS */}
      {op.maquinas?.length > 0 && (
        <div className="card" style={{ marginBottom: 20 }}>
          <h3>Máquinas</h3>

          <table className="table-insumos compact">
            <thead>
              <tr>
                <th>Máquina</th>
                <th>Horas</th>
                <th>Total</th>
              </tr>
            </thead>
            <tbody>
              {op.maquinas.map((m, i) => (
                <tr key={i}>
                  <td>{m.maquina}</td>
                  <td>{m.horas}</td>
                  <td>{m.total}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      {/* 🔥 TABELA FUNCIONÁRIOS */}
      {op.funcionarios?.length > 0 && (
        <div className="card" style={{ marginBottom: 20 }}>
          <h3>Funcionários</h3>

          <table className="table-insumos compact">
            <thead>
              <tr>
                <th>Nome</th>
                <th>Horas</th>
              </tr>
            </thead>
            <tbody>
              {op.funcionarios.map((f, i) => (
                <tr key={i}>
                  <td>{f.nome}</td>
                  <td>{f.horas}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      {/* 🔥 FORMULÁRIOS (EDIÇÃO) */}
      {!bloqueado && (
        <>
          <OperacaoProdutos operacaoId={id} />
          <OperacaoCombustivel operacaoId={id} />
          <OperacaoFuncionarios operacaoId={id} />
        </>
      )}

    </PageLayout>
  )
}