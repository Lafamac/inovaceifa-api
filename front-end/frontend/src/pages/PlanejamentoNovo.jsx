import { useEffect, useRef } from 'react'
import { useNavigate, useSearchParams } from 'react-router-dom'

import PageLayout from '../components/PageLayout'
import Alert from '../components/Alert'

import {
  criarPlanejamento,
  buscarPlanejamentoPorSafraTalhao
} from '../api/api'

export default function PlanejamentoNovo() {

  const navigate = useNavigate()
  const [params] = useSearchParams()

  const safraTalhaoId = params.get('safraTalhaoId')

  // 🔥 CONTROLE PARA NÃO DUPLICAR (ESSENCIAL)
  const jaExecutou = useRef(false)

  async function criar() {

    try {
      // 🔥 1. TENTAR BUSCAR EXISTENTE (AGORA CORRETO)
      const existente = await buscarPlanejamentoPorSafraTalhao(safraTalhaoId)

      if (existente.ok) {
        const planejamentoExistente =
          existente.data?.data || existente.data

        if (planejamentoExistente?.id) {
          console.log('Planejamento já existe, redirecionando...')
          navigate(`/planejamento/${planejamentoExistente.id}/insumos`)
          return
        }
      }

      // 🔥 2. SE NÃO EXISTE → CRIA
      const res = await criarPlanejamento({
        safraTalhaoId: Number(safraTalhaoId),
        operacaoId: 1,
        dataPrevista: new Date().toISOString().split('T')[0],
        areaPlanejada: 1
      })

      const id =
        res.data?.data?.id ||
        res.data?.data?.data?.id

      if (!id) {
        console.error('Erro ao criar planejamento', res)
        return
      }

      navigate(`/planejamento/${id}/insumos`)

    } catch (err) {
      console.error('Erro ao criar planejamento:', err)
    }
  }

  useEffect(() => {
    if (!safraTalhaoId) return

    if (jaExecutou.current) return
    jaExecutou.current = true

    criar()
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [safraTalhaoId])

  return (
    <PageLayout title="Criando Planejamento...">
      <Alert
        type={safraTalhaoId ? "info" : "warning"}
        message={
          safraTalhaoId
            ? "Criando planejamento..."
            : "Safra Talhão não informado."
        }
      />
    </PageLayout>
  )
}