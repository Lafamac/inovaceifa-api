import { useState, useEffect } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { useAuth } from '../auth/AuthContext'

import PageLayout from '../components/PageLayout'
import Alert from '../components/Alert'
import FormInput from '../components/FormInput'

import {
  criarSafra,
  atualizarSafra,
  buscarSafra
} from '../api/api'

export default function SafraForm() {

  const { id } = useParams()
  const navigate = useNavigate()
  const { user } = useAuth()

  const modoEdicao = !!id

  const [nome, setNome] = useState('')
  const [dataInicial, setDataInicial] = useState('')
  const [dataFinal, setDataFinal] = useState('')
  const [areaPlantada, setAreaPlantada] = useState('')
  const [orcamentoPrevisto, setOrcamentoPrevisto] = useState('')

  const [erro, setErro] = useState('')
  const [sucesso, setSucesso] = useState('')

  useEffect(() => {
    if (modoEdicao) carregar()
  }, [id])

  async function carregar() {
    try {
      const res = await buscarSafra(id)
      const s = res.data.data

      setNome(s.nome)
      setDataInicial(s.dataInicial)
      setDataFinal(s.dataFinal)
      setAreaPlantada(s.areaPlantada || '')
      setOrcamentoPrevisto(s.orcamentoPrevisto || '')

    } catch {
      setErro('Erro ao carregar')
    }
  }

  async function handleSubmit(e) {
    e.preventDefault()
    setErro('')
    setSucesso('')

    const payload = {
      nome,
      dataInicial,
      dataFinal,
      areaPlantada: areaPlantada ? Number(areaPlantada) : 0,
      orcamentoPrevisto: orcamentoPrevisto ? Number(orcamentoPrevisto) : 0,
      fazendaId: user?.fazendaAtiva?.id
    }

    try {
      if (modoEdicao) {
        await atualizarSafra(id, payload)
      } else {
        await criarSafra(payload)
      }

      setSucesso('Salvo com sucesso')

      setTimeout(() => {
        navigate('/safra')
      }, 500)

    } catch {
      setErro('Erro ao salvar')
    }
  }

  return (
    <PageLayout
      title={modoEdicao ? 'Editar Safra' : 'Nova Safra'}
      showBack
      backTo="/safra"
    >

      {erro && <Alert type="error" message={erro} />}
      {sucesso && <Alert type="success" message={sucesso} />}

      <form className="form-container" onSubmit={handleSubmit}>

        {/* 🔹 NOME */}
        <FormInput
          label="Nome"
          value={nome}
          onChange={e => setNome(e.target.value)}
        />

        {/* 🔥 LINHA 1 - DATAS */}
        <div style={{
          display: 'grid',
          gridTemplateColumns: '1fr 1fr',
          gap: 12
        }}>
          <FormInput
            label="Data Inicial"
            type="date"
            value={dataInicial}
            onChange={e => setDataInicial(e.target.value)}
          />

          <FormInput
            label="Data Final"
            type="date"
            value={dataFinal}
            onChange={e => setDataFinal(e.target.value)}
          />
        </div>

        {/* 🔥 LINHA 2 - ÁREA E ORÇAMENTO */}
        <div style={{
          display: 'grid',
          gridTemplateColumns: '1fr 1fr',
          gap: 12
        }}>
          <FormInput
            label="Área Plantada (ha)"
            type="number"
            value={areaPlantada}
            onChange={e => setAreaPlantada(e.target.value)}
          />

          <FormInput
            label="Orçamento Previsto (R$)"
            type="number"
            value={orcamentoPrevisto}
            onChange={e => setOrcamentoPrevisto(e.target.value)}
          />
        </div>

        <div className="form-actions">
          <button className="add-btn">
            Salvar
          </button>
        </div>

      </form>

    </PageLayout>
  )
}