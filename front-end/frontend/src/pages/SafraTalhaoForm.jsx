import { useState, useEffect } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { useAuth } from '../auth/AuthContext'
import { useQueryClient } from '@tanstack/react-query'
import '../styles/pages.css'
import '../styles/form.css'

import Alert from '../components/Alert'
import PageLayout from '../components/PageLayout'
import FormInput from '../components/FormInput'

import {
  criarSafraTalhao,
  buscarSafraTalhao,
  atualizarSafraTalhao,
  listarTalhoes,
  listarCulturas,
  listarResFerrugem,
  listarStCultivo,
  buscarTalhao,

  // 🔥 ALTERADO
  listarReferenciasOperacaoTalhao

} from '../api/api'

export default function SafraTalhaoForm() {

  const navigate = useNavigate()
  const { id } = useParams()
  const { safraAtiva, user } = useAuth()
  const queryClient = useQueryClient()

  const modoEdicao = !!id
  const fazendaId = user?.fazendaAtiva?.id

  const [talhaoId, setTalhaoId] = useState('')
  const [culturaId, setCulturaId] = useState('')
  const [areaUtilizada, setAreaUtilizada] = useState('')
  const [espRua, setEspRua] = useState('')
  const [espPlanta, setEspPlanta] = useState('')
  const [material, setMaterial] = useState('')

  const [resFerrugemId, setResFerrugemId] = useState('')
  const [stCultivoId, setStCultivoId] = useState('')
  const [stTerra, setStTerra] = useState('')
  const [vencContrato, setVencContrato] = useState('')

  const [irrigacao, setIrrigacao] = useState(false)
  const [estLitroPlanta, setEstLitroPlanta] = useState('')
  const [estSacaHectare, setEstSacaHectare] = useState('')
  const [estSaca, setEstSaca] = useState('')

  const [producaoReal, setProducaoReal] = useState('')
  const [precoSaca, setPrecoSaca] = useState('')

  const [operacaoTalhaoId, setOperacaoTalhaoId] = useState('')
  const [operacoesTalhao, setOperacoesTalhao] = useState([])

  const [talhoes, setTalhoes] = useState([])
  const [culturas, setCulturas] = useState([])
  const [resFerrugem, setResFerrugem] = useState([])
  const [stCultivo, setStCultivo] = useState([])

  const [erro, setErro] = useState('')
  const [sucesso, setSucesso] = useState('')

  async function carregarSelects() {

    try {

      const t = await listarTalhoes()
      const c = await listarCulturas()
      const r = await listarResFerrugem()
      const s = await listarStCultivo()

      // 🔥 ALTERADO
      const o = await listarReferenciasOperacaoTalhao()

      setTalhoes(t.data?.data?.content || [])
      setCulturas(c.data?.data || [])
      setResFerrugem(r.data?.data || [])
      setStCultivo(s.data?.data || [])

      setOperacoesTalhao(
        o.data?.data?.content ||
        o.data?.data ||
        []
      )

    } catch (err) {
      console.error(err)
      setErro('Erro ao carregar listas')
    }
  }

  async function carregar() {

    try {

      const response = await buscarSafraTalhao(id)
      const d = response.data.data

      setTalhaoId(String(d.talhao?.id || d.talhaoId || ''))
      setCulturaId(String(d.cultura?.id || d.culturaId || ''))

      setAreaUtilizada(d.areaUtilizada || '')
      setEspRua(d.espRua || '')
      setEspPlanta(d.espPlanta || '')
      setMaterial(d.material || '')

      setResFerrugemId(
        String(d.resFerrugem?.id || d.resFerrugemId || '')
      )

      setStCultivoId(
        String(d.stCultivo?.id || d.stCultivoId || '')
      )

      setStTerra(d.stTerra || '')
      setVencContrato(d.vencContrato || '')

      setIrrigacao(d.irrigacao || false)
      setEstLitroPlanta(d.estLitroPlanta || '')

      setEstSacaHectare(d.estimativaSacaHectare || '')
      setEstSaca(d.estimativaSaca || '')

      setProducaoReal(d.producaoReal || '')
      setPrecoSaca(d.precoSaca || '')

      setOperacaoTalhaoId(
        String(
          d.operacaoTalhao?.id ||
          d.operacaoTalhaoId ||
          ''
        )
      )

    } catch (err) {
      console.error(err)
      setErro('Erro ao carregar')
    }
  }

  async function carregarTalhaoSelecionado(id) {

    try {

      const res = await buscarTalhao(id)
      const t = res.data?.data

      if (!t) return

      setMaterial(t.material || '')
      setAreaUtilizada(t.area || '')
      setEspRua(t.espacamentoRua || '')
      setEspPlanta(t.espacamentoPlanta || '')

      setResFerrugemId(String(t.resistenciaFerrugemId || ''))
      setStCultivoId(String(t.sistemaCultivoId || ''))

    } catch (err) {
      console.error(err)
    }
  }

  useEffect(() => {

    async function init() {

      await carregarSelects()

      if (modoEdicao) {
        await carregar()
      }
    }

    init()

  }, [id, modoEdicao])

  async function handleSubmit(e) {

    e.preventDefault()

    setErro('')
    setSucesso('')

    if (!talhaoId || !culturaId) {
      setErro('Selecione talhão e cultura')
      return
    }

    if (!safraAtiva?.id || !fazendaId) {
      setErro('Safra ou fazenda não definida')
      return
    }

    const payload = {

      safraId: safraAtiva.id,
      fazendaId: fazendaId,

      talhaoId: Number(talhaoId),
      culturaId: Number(culturaId),

      areaUtilizada: areaUtilizada ? Number(areaUtilizada) : 0,
      espRua: espRua ? Number(espRua) : 0,
      espPlanta: espPlanta ? Number(espPlanta) : 0,

      material,

      resFerrugemId: resFerrugemId ? Number(resFerrugemId) : null,
      stCultivoId: stCultivoId ? Number(stCultivoId) : null,

      stTerra,
      vencContrato,
      irrigacao,

      estLitroPlanta: estLitroPlanta ? Number(estLitroPlanta) : 0,
      estimativaSacaHectare: estSacaHectare ? Number(estSacaHectare) : 0,
      estimativaSaca: estSaca ? Number(estSaca) : 0,

      producaoReal: producaoReal ? Number(producaoReal) : 0,
      precoSaca: precoSaca ? Number(precoSaca) : 0,

      operacaoTalhaoId: operacaoTalhaoId
        ? Number(operacaoTalhaoId)
        : null
    }

    try {

      const response = modoEdicao
        ? await atualizarSafraTalhao(id, payload)
        : await criarSafraTalhao(payload)

      if (!response.data?.success) {
        setErro('Já existe este talhão vinculado à safra')
        return
      }

      setSucesso('Salvo com sucesso')

      setTimeout(() => {

        queryClient.invalidateQueries(['safra-talhoes'])

        navigate('/safra-talhoes')

      }, 500)

    } catch {
      setErro('Já existe este talhão vinculado à safra')
    }
  }

  return (
    <PageLayout
      title={modoEdicao ? 'Editar Safra Talhão' : 'Novo Safra Talhão'}
      showBack
      backTo="/safra-talhoes"
    >

      {erro && <Alert type="error" message={erro} />}
      {sucesso && <Alert type="success" message={sucesso} />}

      <form className="form-container" onSubmit={handleSubmit}>

        <div className="form-grid-3">

          <div>
            <label>Talhão</label>

            <select
              value={talhaoId}
              onChange={async (e) => {

                const id = e.target.value

                setTalhaoId(id)

                if (id) {
                  await carregarTalhaoSelecionado(id)
                }
              }}
            >
              <option value="">Selecione</option>

              {talhoes.map(t => (
                <option key={t.id} value={String(t.id)}>
                  {t.nome}
                </option>
              ))}
            </select>
          </div>

          <FormInput
            label="Material"
            value={material}
            onChange={e => setMaterial(e.target.value)}
          />

          <FormInput
            label="Área Utilizada"
            type="number"
            value={areaUtilizada}
            onChange={e => setAreaUtilizada(e.target.value)}
          />

        </div>

        <div className="form-grid-3">

          <FormInput
            label="Espaçamento Rua"
            type="number"
            value={espRua}
            onChange={e => setEspRua(e.target.value)}
          />

          <FormInput
            label="Espaçamento Planta"
            type="number"
            value={espPlanta}
            onChange={e => setEspPlanta(e.target.value)}
          />

          <FormInput
            label="Situação Terra"
            value={stTerra}
            onChange={e => setStTerra(e.target.value)}
          />

        </div>

        <div className="form-grid-3">

          <div>
            <label>Cultura</label>

            <select
              value={culturaId}
              onChange={e => setCulturaId(e.target.value)}
            >
              <option value="">Selecione</option>

              {culturas.map(c => (
                <option key={c.id} value={String(c.id)}>
                  {c.descricao}
                </option>
              ))}
            </select>
          </div>

          <div>
            <label>Resistência Ferrugem</label>

            <select
              value={resFerrugemId}
              onChange={e => setResFerrugemId(e.target.value)}
            >
              <option value="">Selecione</option>

              {resFerrugem.map(r => (
                <option key={r.id} value={String(r.id)}>
                  {r.descricao}
                </option>
              ))}
            </select>
          </div>

          <div>
            <label>Situação Cultivo</label>

            <select
              value={stCultivoId}
              onChange={e => setStCultivoId(e.target.value)}
            >
              <option value="">Selecione</option>

              {stCultivo.map(s => (
                <option key={s.id} value={String(s.id)}>
                  {s.descricao}
                </option>
              ))}
            </select>
          </div>

        </div>

        <div>
          <label>Operação Talhão</label>

          <select
            value={operacaoTalhaoId}
            onChange={e => setOperacaoTalhaoId(e.target.value)}
          >
            <option value="">Selecione</option>

            {operacoesTalhao.map(op => (
              <option key={op.id} value={String(op.id)}>
                {op.descricao}
              </option>
            ))}
          </select>
        </div>

        <div className="form-grid-2">

          <FormInput
            label="Produção Real (sacas)"
            type="number"
            value={producaoReal}
            onChange={e => setProducaoReal(e.target.value)}
          />

          <FormInput
            label="Preço da Saca (R$)"
            type="number"
            value={precoSaca}
            onChange={e => setPrecoSaca(e.target.value)}
          />

        </div>

        <div className="form-actions">
          <button type="submit" className="add-btn">
            {modoEdicao ? 'Salvar alterações' : 'Salvar'}
          </button>
        </div>

      </form>

    </PageLayout>
  )
}