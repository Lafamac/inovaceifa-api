import { useEffect, useState } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { useAuth } from '../auth/AuthContext'

import PageLayout from '../components/PageLayout'
import Alert from '../components/Alert'
import SkeletonCard from '../components/SkeletonCard'
import EmptyState from '../components/EmptyState'

import { listarAuditoriaOs } from '../api/api'

export default function OrdemServicoAuditoria() {

  const { id } = useParams()
  const navigate = useNavigate()
  const { user } = useAuth()

  const podeVer = user?.perfilId === 2

  const [lista, setLista] = useState([])
  const [loading, setLoading] = useState(true)
  const [erro, setErro] = useState('')

  useEffect(() => {

    if (!podeVer) {
      setErro('Você não tem permissão para acessar auditoria')
      setLoading(false)
      return
    }

    carregar()

  }, [])

  async function carregar() {
    try {
      const res = await listarAuditoriaOs(id)

      if (!res.data?.success) {
        setErro('Erro ao carregar auditoria')
        return
      }

      const dados = res.data?.data || []
      setLista(Array.isArray(dados) ? dados : [])

    } catch (err) {
      console.error(err)
      setErro('Erro ao carregar auditoria')
    } finally {
      setLoading(false)
    }
  }

  function formatarData(data) {
    if (!data) return '-'
    return new Date(data).toLocaleString()
  }

  if (loading) {
    return (
      <PageLayout title="Auditoria da OS">
        <SkeletonCard />
        <SkeletonCard />
      </PageLayout>
    )
  }

  return (
    <PageLayout
      title="Auditoria da OS"
      showBack
      backTo={`/ordens-servico/${id}`}
    >

      {erro && <Alert type="error" message={erro} />}

      {!loading && lista.length === 0 && (
        <EmptyState message="Nenhuma alteração registrada" />
      )}

      {!loading && lista.length > 0 && (
        <div className="card-list">
          {lista.map(item => (
            <div key={item.id} className="card">

              <strong style={{ color: '#2563eb' }}>
                {item.acao}
              </strong>

              <p><b>Usuário:</b> {item.usuarioId}</p>

              <p><b>Data:</b> {formatarData(item.dataEvento)}</p>

            </div>
          ))}
        </div>
      )}

      {/* 🔥 DETALHADO */}
      {podeVer && lista.length > 0 && (
        <div style={{ marginTop: 20 }}>
          <button
            className="add-btn"
            onClick={() => navigate(`/ordens-servico/${id}/auditoria/detalhe`)}
          >
            Ver detalhado →
          </button>
        </div>
      )}

    </PageLayout>
  )
}