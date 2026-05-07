import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'

import { listarVendasPorTalhao } from '../api/api'

import PageLayout from '../components/PageLayout'
import CrudToolbar from '../components/CrudToolbar'
import CrudCard from '../components/CrudCard'
import CrudCardList from '../components/CrudCardList'
import Alert from '../components/Alert'
import { Pencil } from 'lucide-react'

export default function VendasPage() {

  const navigate = useNavigate()
  const [dados, setDados] = useState([])
  const [erro, setErro] = useState('')
  const [busca, setBusca] = useState('')

  useEffect(() => {
    carregar()
  }, [])

  async function carregar() {
    try {
      // 🔥 aqui depois você pode filtrar por talhão
      const res = await listarVendasPorTalhao(1)
      setDados(res.data?.data || [])
    } catch (e) {
      setErro('Erro ao carregar vendas')
    }
  }

  const lista = dados.filter(v =>
    String(v.id).includes(busca)
  )

  return (
    <PageLayout title="Vendas">

      <CrudToolbar
        busca={busca}
        setBusca={setBusca}
        onNovo={() => navigate('/vendas/novo')}
        labelNovo="Nova Venda"
      />

      <Alert type="error" message={erro} />

      <CrudCardList>
        {lista.map(v => (
          <CrudCard
            key={v.id}
            title={`Venda #${v.id}`}
            subtitle={`Qtd: ${v.quantidade} | R$ ${v.precoUnitario}`}
            onClick={() => navigate(`/vendas/${v.id}/editar`)}
            actions={
              <button onClick={() => navigate(`/vendas/${v.id}/editar`)}>
                <Pencil size={16} />
              </button>
            }
          />
        ))}
      </CrudCardList>

    </PageLayout>
  )
}