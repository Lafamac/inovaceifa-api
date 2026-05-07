import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  Tooltip,
  ResponsiveContainer
} from 'recharts'

export default function AdubacaoChart({ data }) {

  const chartData = data.map(item => ({
    name: item.produtoNome || `Produto ${item.produtoId}`,
    quantidade: item.quantidadeTotal
  }))

  return (
    <div style={{ width: '100%', height: 300 }}>
      <ResponsiveContainer>
        <BarChart data={chartData}>
          <XAxis dataKey="name" />
          <YAxis />
          <Tooltip />
          <Bar dataKey="quantidade" />
        </BarChart>
      </ResponsiveContainer>
    </div>
  )
}