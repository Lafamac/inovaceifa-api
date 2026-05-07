import { useNavigate } from 'react-router-dom'
import PageLayout from '../components/PageLayout'
import '../styles/pages.css'

export default function ReferenciasMenu() {

  const navigate = useNavigate()

  const itens = [
    { nome: 'Centro de Custo', tipo: 'centro-custo' },
    { nome: 'Família', tipo: 'familia' },
    { nome: 'Grupo', tipo: 'grupo' },
    { nome: 'Conta Gerencial', tipo: 'conta-gerencial' },

    { nome: 'Encargos Folha', tipo: 'parametro' },

    { nome: 'Cultura', tipo: 'cultura' },
    { nome: 'Status Cultivo', tipo: 'st-cultivo' },
    { nome: 'Resistência Ferrugem', tipo: 'res-ferrugem' },

    { nome: 'Tipo Máquina', tipo: 'tipo-maquina' },
    { nome: 'Tipo Posse Máquina', tipo: 'tipo-posse-maquina' },
    { nome: 'Tipo Gasto Máquina', tipo: 'tipo-gasto-maquina' },

    { nome: 'Tipo Movimento Produto', tipo: 'tipo-mov-produto' },
    { nome: 'Tipo Pagamento', tipo: 'tipo-pagamento' },

    { nome: 'Status Pedido Compra', tipo: 'pedido-compra-status' },

    { nome: 'Despesas', tipo: 'despesa' },
    { nome: 'Operação Talhão', tipo: 'operacao-talhao' },
    { nome: 'Tipo Rateio', tipo: 'tipo-rateio' }
  ]

  return (
    <PageLayout
      title="Tabelas de Referência"
      showBack
      backTo="/menu"
    >
      <div className="grid-wrapper">
        <div className="grid-container">

          {itens.map(item => (
            <div
              key={item.tipo}
              className="module-card"
              onClick={() => navigate(`/referencias/${item.tipo}`)}
            >
              <div className="module-content">
                <h3>{item.nome}</h3>
              </div>

              <div className="module-arrow">→</div>
            </div>
          ))}

        </div>
      </div>
    </PageLayout>
  )
}