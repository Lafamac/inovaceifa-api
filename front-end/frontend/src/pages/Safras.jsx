import { useState, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import { listarSafras, excluirSafra } from '../api/api'

import PageLayout from '../components/PageLayout'
import CrudToolbar from '../components/CrudToolbar'
import CrudCard from '../components/CrudCard'
import CrudCardList from '../components/CrudCardList'
import ConfirmDialog from '../components/ConfirmDialog'
import Alert from '../components/Alert'

export default function Safras() {

  const navigate = useNavigate()

  const [lista, setLista] = useState([])
  const [busca, setBusca] = useState('')
  const [erro, setErro] = useState('')
  const [loading, setLoading] = useState(true)

  const [dialog, setDialog] = useState({ open: false, id: null })

  async function carregar() {
    try {
      const res = await listarSafras()

      // 🔥 CORREÇÃO AQUI
      const dados =
        res.data?.data?.content ||
        res.data?.data ||
        []

      setLista(Array.isArray(dados) ? dados : [])

    } catch {
      setErro('Erro ao carregar safras')
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    carregar()
  }, [])

  function abrirDialog(id) {
    setDialog({ open: true, id })
  }

  async function confirmarExcluir() {
    try {
      await excluirSafra(dialog.id)
      carregar()
    } catch {
      setErro('Erro ao excluir')
    } finally {
      setDialog({ open: false, id: null })
    }
  }

  const filtrados = lista.filter(s =>
    (s.nome || '').toLowerCase().includes(busca.toLowerCase())
  )

  return (
    <PageLayout title="Safras">

      <CrudToolbar
        busca={busca}
        setBusca={setBusca}
        onNovo={() => navigate('/safra/novo')}
        labelNovo="Nova Safra"
      />

      {erro && <Alert type="error" message={erro} />}

      <CrudCardList>
        {filtrados.map(s => (
          <CrudCard
            key={s.id}
            title={s.nome}
            subtitle={`
              ${s.dataInicial} → ${s.dataFinal}
              | Área: ${s.areaPlantada || 0} ha
              | Orçamento: R$ ${s.orcamentoPrevisto || 0}
            `}
            onClick={() => navigate(`/safra/${s.id}/editar`)}
            actions={
              <button
                className="icon-button danger"
                onClick={(e) => {
                  e.stopPropagation()
                  abrirDialog(s.id)
                }}
              >
                ❌
              </button>
            }
          />
        ))}
      </CrudCardList>

      <ConfirmDialog
        open={dialog.open}
        title="Excluir Safra"
        message="Deseja excluir esta safra?"
        onConfirm={confirmarExcluir}
        onCancel={() => setDialog({ open: false, id: null })}
      />

    </PageLayout>
  )
}