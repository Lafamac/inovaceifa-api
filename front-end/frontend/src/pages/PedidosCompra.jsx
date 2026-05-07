import { useState } from 'react'
import { useNavigate } from 'react-router-dom'

import {
  listarPedidosCompra,
  listarPedidosCompraInativos,
  excluirPedidoCompra,
  reativarPedidoCompra,
  aprovarPedidoCompra,
  receberPedidoCompra
} from '../api/api'

import useAtivoInativoCrud from '../hooks/useAtivoInativoCrud'
import PageLayout from '../components/PageLayout'
import CrudToolbar from '../components/CrudToolbar'
import CrudCard from '../components/CrudCard'
import CrudCardList from '../components/CrudCardList'
import Alert from '../components/Alert'
import { Check, Truck, Pencil } from 'lucide-react'

export default function PedidosCompra() {

  const navigate = useNavigate()
  const [busca, setBusca] = useState('')

  const {
    dados,
    erro,
    mostrarInativos,
    setMostrarInativos
  } = useAtivoInativoCrud({
    listarAtivos: listarPedidosCompra,
    listarInativos: listarPedidosCompraInativos,
    inativar: excluirPedidoCompra,
    reativar: reativarPedidoCompra,
    entityName: 'Pedido'
  })

  const lista = Array.isArray(dados) ? dados : []

  return (
    <PageLayout title="Pedidos de Compra">

      <CrudToolbar
        busca={busca}
        setBusca={setBusca}
        onNovo={() => navigate('/pedidos-compra/novo')}
        labelNovo="Novo Pedido"
        mostrarInativos={mostrarInativos}
        setMostrarInativos={setMostrarInativos}
      />

      <Alert type="error" message={erro} />

      <CrudCardList>
        {lista.map(p => (
          <CrudCard
            key={p.id}
            title={`Pedido #${p.id}`}
            subtitle={`Status: ${p.status} | Total: R$ ${p.valorTotal}`}
            onClick={() => navigate(`/pedidos-compra/${p.id}/editar`)}
            actions={
              <>
                <button onClick={() => aprovarPedidoCompra(p.id)}>
                  <Check size={16} />
                </button>

                <button onClick={() => receberPedidoCompra(p.id)}>
                  <Truck size={16} />
                </button>

                <button onClick={() => navigate(`/pedidos-compra/${p.id}/editar`)}>
                  <Pencil size={16} />
                </button>
              </>
            }
          />
        ))}
      </CrudCardList>

    </PageLayout>
  )
}