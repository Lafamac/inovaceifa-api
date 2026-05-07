import { useNavigate } from 'react-router-dom'
import PageLayout from '../components/PageLayout'

export default function Financeiro() {

  const navigate = useNavigate()

  const itens = [
    {
      nome: 'Contas a Pagar',
      rota: '/financeiro/contas-pagar',
      descricao: 'Controle de despesas e pagamentos'
    },
    {
      nome: 'Contas a Receber',
      rota: '/financeiro/contas-receber',
      descricao: 'Gestão de recebimentos'
    },
    {
      nome: 'Fluxo de Caixa',
      rota: '/financeiro/fluxo-caixa',
      descricao: 'Movimentação financeira'
    }
  ]

  return (
    <PageLayout title="Financeiro">

      <div className="grid-container">

        {itens.map(item => (
          <div
            key={item.rota}
            className="module-card"
            onClick={() => {
              if (item.rota === '/financeiro/contas-pagar') {
                navigate(item.rota)
              } else {
                alert('Módulo em desenvolvimento')
              }
            }}
          >
            <div className="module-content">
              <h3>{item.nome}</h3>
              <p>{item.descricao}</p>
            </div>

            <div className="module-arrow">→</div>
          </div>
        ))}

      </div>

    </PageLayout>
  )
}