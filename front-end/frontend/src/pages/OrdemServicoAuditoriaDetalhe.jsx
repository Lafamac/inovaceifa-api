import { useEffect, useState } from 'react'
import { useParams, useNavigate } from 'react-router-dom'

import PageLayout from '../components/PageLayout'
import Alert from '../components/Alert'
import SkeletonCard from '../components/SkeletonCard'

import { listarAuditoriaDetalheOs } from '../api/api'

export default function OrdemServicoAuditoriaDetalhe() {

  const { id } = useParams()
  const navigate = useNavigate()

  const [lista, setLista] = useState([])
  const [loading, setLoading] = useState(true)
  const [erro, setErro] = useState('')

  useEffect(() => {
    carregar()
  }, [])

  async function carregar() {
    try {
      const res = await listarAuditoriaDetalheOs(id)

      if (!res.data?.success) {
        setErro('Erro ao carregar histórico detalhado')
        return
      }

      setLista(res.data.data || [])

    } catch (err) {
      console.error(err)
      setErro('Erro ao carregar histórico detalhado')
    } finally {
      setLoading(false)
    }
  }

  return (
    <PageLayout
      title="Auditoria Detalhada"
      showBack
      backTo={`/ordens-servico/${id}/auditoria`}
    >

      {erro && <Alert type="error" message={erro} />}

      {loading && (
        <>
          <SkeletonCard />
          <SkeletonCard />
        </>
      )}

      {!loading && lista.length === 0 && (
        <p>Nenhuma alteração encontrada</p>
      )}

      {!loading && lista.length > 0 && (
        <div className="card-list">
          {lista.map((item, index) => (
            <div key={index} className="card">

              <strong>{item.campo}</strong>

              <p>
                <b>De:</b> {item.antes || '-'}
                {' → '}
                <b>Para:</b> {item.depois || '-'}
              </p>

              <p><b>Usuário:</b> {item.usuarioNome}</p>

              <p><b>Data:</b> {item.dataEvento}</p>

            </div>
          ))}
        </div>
      )}

    </PageLayout>
  )
}