import { useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'
import PageLayout from '../components/PageLayout'
import Alert from '../components/Alert'

import {
  listarReferencias,
  atualizarReferencia,
  listarParametrosCompletos,
  criarReferencia
} from '../api/api'

export default function ReferenciasPage() {

  const { tipo } = useParams()

  const [lista, setLista] = useState([])
  const [erro, setErro] = useState('')
  const [salvandoId, setSalvandoId] = useState(null)

  const [descricao, setDescricao] = useState('')
  const [loading, setLoading] = useState(false)

  async function carregar() {
    try {
      let dados = []

      if (tipo === 'parametro') {

        const res = await listarParametrosCompletos()
        dados = res.data?.data || []

        dados = dados.map(item => {
          const valorDecimal = Number(item.extras?.valor ?? 0)

          return {
            ...item,
            extras: {
              ...item.extras,
              valor: (valorDecimal * 100).toFixed(2)
            }
          }
        })

      } else {

        const res = await listarReferencias(tipo)
        dados =
          res.data?.data?.content ||
          res.data?.data ||
          []

      }

      setLista(Array.isArray(dados) ? dados : [])

    } catch {
      setErro('Erro ao carregar referências')
    }
  }

  useEffect(() => {
    carregar()
  }, [tipo])

  function formatar(valor) {
    if (valor === '') return ''

    let v = valor.replace(',', '.')
    v = parseFloat(v)

    if (isNaN(v)) return ''

    if (v < 0) v = 0
    if (v > 100) v = 100

    return v.toFixed(2)
  }

  function atualizarValor(id, novoValor) {
    setLista(prev =>
      prev.map(item =>
        item.id === id
          ? {
              ...item,
              extras: {
                ...item.extras,
                valor: novoValor
              }
            }
          : item
      )
    )
  }

  async function salvarParametro(item) {

    const valorPercentual = Number(item.extras?.valor || 0)

    if (valorPercentual < 0 || valorPercentual > 100) {
      setErro('Valor deve estar entre 0 e 100')
      return
    }

    const valorDecimal = valorPercentual / 100

    try {
      setSalvandoId(item.id)

      await atualizarReferencia(tipo, item.id, {
        descricao: item.descricao,
        chave: item.extras?.chave,
        valor: valorDecimal
      })

    } catch {
      setErro('Erro ao atualizar parâmetro')
    } finally {
      setSalvandoId(null)
    }
  }

  async function criar() {
    if (!descricao) {
      setErro('Informe a descrição')
      return
    }

    try {
      setLoading(true)
      await criarReferencia(tipo, { descricao })
      setDescricao('')
      await carregar()
    } catch {
      setErro('Erro ao criar')
    } finally {
      setLoading(false)
    }
  }

  return (
    <PageLayout
      title={tipo === 'parametro' ? 'Encargos da Folha' : `Referências - ${tipo}`}
      showBack
      backTo="/referencias"
    >

      {erro && <Alert type="error" message={erro} />}

      {/* FORM */}
      {tipo !== 'parametro' && (
        <div className="form-container">
          <input
            className="form-input"
            placeholder="Descrição"
            value={descricao}
            onChange={(e) => setDescricao(e.target.value)}
          />
          <button className="add-btn" onClick={criar} disabled={loading}>
            {loading ? 'Salvando...' : 'Adicionar'}
          </button>
        </div>
      )}

      <div className="card">
        <table
          className="table-insumos compact"
          style={{ tableLayout: 'fixed', width: '100%' }}
        >
          <thead>
            <tr>
              <th style={{ width: 60 }}>ID</th>
              <th>Descrição</th>

              {tipo === 'parametro' && <th style={{ width: 220 }}>Chave</th>}
              {tipo === 'parametro' && <th style={{ width: 160 }}>Valor (%)</th>}
              {tipo === 'parametro' && <th style={{ width: 120 }}></th>}
            </tr>
          </thead>

          <tbody>
            {lista.map(item => (
              <tr key={item.id}>
                <td>{item.id}</td>
                <td>{item.descricao}</td>

                {tipo === 'parametro' && (
                  <>
                    <td style={{ whiteSpace: 'nowrap' }}>
                      {item.extras?.chave}
                    </td>

                    <td>
                      <div
                        style={{
                          display: 'flex',
                          alignItems: 'center',
                          gap: 6,
                          whiteSpace: 'nowrap'
                        }}
                      >
                        <input
                          type="text"
                          value={item.extras?.valor || ''}
                          onBlur={(e) =>
                            atualizarValor(item.id, formatar(e.target.value))
                          }
                          onChange={(e) =>
                            atualizarValor(item.id, e.target.value)
                          }
                          style={{
                            width: 70,
                            textAlign: 'right',
                            padding: '4px 6px'
                          }}
                        />
                        <span>%</span>
                      </div>
                    </td>

                    <td>
                      <button
                        className="add-btn"
                        onClick={() => salvarParametro(item)}
                        disabled={salvandoId === item.id}
                        style={{ whiteSpace: 'nowrap' }}
                      >
                        {salvandoId === item.id ? 'Salvando...' : 'Salvar'}
                      </button>
                    </td>
                  </>
                )}
              </tr>
            ))}
          </tbody>
        </table>
      </div>

    </PageLayout>
  )
}