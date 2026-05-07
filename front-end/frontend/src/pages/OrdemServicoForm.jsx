import { useState, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'

import PageLayout from '../components/PageLayout'
import Alert from '../components/Alert'

import Select from 'react-select'

import {
  listarPlanejamentos,
  criarOsFromPlanejamento
} from '../api/api'

export default function OrdemServicoForm() {

  const navigate = useNavigate()

  const [planejamentoIds, setPlanejamentoIds] = useState([])
  const [planejamentos, setPlanejamentos] = useState([])

  const [erro, setErro] = useState('')
  const [sucesso, setSucesso] = useState('')
  const [loading, setLoading] = useState(false)

  useEffect(() => {
    carregar()
  }, [])

  async function carregar() {
    try {
      const res = await listarPlanejamentos()

      const lista = Array.isArray(res.data?.data?.content)
        ? res.data.data.content
        : Array.isArray(res.data?.data)
          ? res.data.data
          : []

      const formatado = lista.map(p => ({
        value: p.id,
        label: p.operacaoNome || p.operacao?.nome || `Planejamento ${p.id}`
      }))

      setPlanejamentos(formatado)

      if (lista.length === 0) {
        setErro('Não há planejamentos disponíveis. Cadastre uma safra/talhão para habilitar a criação de ordens de serviço.')
      }

    } catch (err) {
      console.error(err)
      setErro('Erro ao carregar planejamentos')
    }
  }

  async function handleSubmit(e) {
    e.preventDefault()
    setErro('')
    setSucesso('')
    setLoading(true)

    if (planejamentos.length === 0) {
      setErro('Não há planejamentos disponíveis.')
      setLoading(false)
      return
    }

    if (planejamentoIds.length === 0) {
      setErro('Selecione pelo menos um planejamento')
      setLoading(false)
      return
    }

    try {
      const res = await criarOsFromPlanejamento({
        planejamentoIds: planejamentoIds.map(p => Number(p.value))
      })

      if (!res.data?.success) {
        setErro('Erro ao criar OS')
        return
      }

      setSucesso('Ordem de serviço criada')

      navigate('/ordens-servico')

    } catch (err) {
      console.error(err)
      setErro('Erro ao salvar')
    } finally {
      setLoading(false)
    }
  }

  // 🔥 ESTILO PADRONIZADO
  const customStyles = {
    control: (base, state) => ({
      ...base,
      minHeight: 38,
      borderRadius: 6,
      borderColor: state.isFocused ? '#2563eb' : '#ccc',
      boxShadow: 'none',
      '&:hover': {
        borderColor: '#2563eb'
      },
      backgroundColor: '#fff'
    }),

    valueContainer: (base) => ({
      ...base,
      padding: '2px 8px'
    }),

    multiValue: (base) => ({
      ...base,
      backgroundColor: '#e0e7ff',
      borderRadius: 4
    }),

    multiValueLabel: (base) => ({
      ...base,
      color: '#1e3a8a',
      fontWeight: 500
    }),

    multiValueRemove: (base) => ({
      ...base,
      color: '#1e3a8a',
      ':hover': {
        backgroundColor: '#c7d2fe',
        color: '#1e3a8a'
      }
    }),

    menu: (base) => ({
      ...base,
      zIndex: 20
    }),

    option: (base, state) => ({
      ...base,
      backgroundColor: state.isFocused
        ? '#eef2ff'
        : state.isSelected
        ? '#6366f1'
        : '#fff',
      color: state.isSelected ? '#fff' : '#111',
      cursor: 'pointer'
    })
  }

  return (
    <PageLayout
      title="Nova Ordem de Serviço"
      showBack
      backTo="/ordens-servico"
    >

      {erro && <Alert type="error" message={erro} />}
      {sucesso && <Alert type="success" message={sucesso} />}

      <form className="form-container" onSubmit={handleSubmit}>

        <div style={{ marginBottom: 16 }}>
          <label style={{ marginBottom: 6, display: 'block', fontWeight: 500 }}>
            Planejamento
          </label>

          <Select
            isMulti
            options={planejamentos}
            value={planejamentoIds}
            onChange={(selected) => setPlanejamentoIds(selected || [])}
            placeholder="Digite para buscar planejamentos..."
            isDisabled={planejamentos.length === 0 || loading}
            styles={customStyles}
          />
        </div>

        <div className="form-actions">
          <button
            type="submit"
            className="add-btn"
            disabled={planejamentos.length === 0 || loading}
          >
            {loading ? 'Salvando...' : 'Criar Ordem de Serviço'}
          </button>
        </div>

      </form>

    </PageLayout>
  )
}