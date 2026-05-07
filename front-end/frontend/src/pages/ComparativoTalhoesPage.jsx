import { useEffect, useState } from 'react'

import {
    BarChart,
    Bar,
    XAxis,
    YAxis,
    Tooltip,
    ResponsiveContainer,
    CartesianGrid
} from 'recharts'

import PageLayout from '../components/PageLayout'
import Alert from '../components/Alert'

import {
    obterComparativoTalhoes
} from '../api/api'

export default function ComparativoTalhoesPage() {

    const [data, setData] = useState(null)

    const [erro, setErro] = useState('')

    const [loading, setLoading] = useState(true)

    useEffect(() => {
        carregar()
    }, [])

    async function carregar() {

        try {

            const res =
                await obterComparativoTalhoes()

            if (
                !res.ok ||
                !res.data?.data
            ) {

                setErro(
                    'Erro ao carregar comparativo'
                )

                return
            }

            setData(res.data.data)

        } catch (e) {

            console.error(e)

            setErro(
                'Erro ao carregar dados'
            )

        } finally {

            setLoading(false)
        }
    }

    function moeda(valor) {

        return Number(valor || 0)
            .toLocaleString(
                'pt-BR',
                {
                    style: 'currency',
                    currency: 'BRL'
                }
            )
    }

    function numero(valor) {

        return Number(valor || 0)
            .toLocaleString('pt-BR')
    }

    if (loading) {

        return (
            <PageLayout title="Comparativo Talhões">
                Carregando...
            </PageLayout>
        )
    }

    if (erro) {

        return (
            <PageLayout title="Comparativo Talhões">
                <Alert
                    type="error"
                    message={erro}
                />
            </PageLayout>
        )
    }

    if (!data) return null

    return (

        <PageLayout
            title="Comparativo Talhões"
            showBack
            backTo="/menu"
        >

            {/* 🔥 CARDS */}
            <div
                style={{
                    display: 'grid',
                    gridTemplateColumns:
                        'repeat(auto-fit, minmax(240px, 1fr))',
                    gap: 16,
                    marginBottom: 20
                }}
            >

                <div className="card">
                    <strong>
                        🏆 Melhor Lucro
                    </strong>

                    <p>
                        {
                            data.melhorLucro?.talhaoNome
                        }
                    </p>
                </div>

                <div className="card">
                    <strong>
                        🌱 Melhor Produtividade
                    </strong>

                    <p>
                        {
                            data.melhorProdutividade?.talhaoNome
                        }
                    </p>
                </div>

                <div className="card">
                    <strong>
                        ⚠️ Pior Margem
                    </strong>

                    <p>
                        {
                            data.piorMargem?.talhaoNome
                        }
                    </p>
                </div>

            </div>

            {/* 🔥 GRÁFICO LUCRO */}
            <div
                className="card"
                style={{
                    marginBottom: 20
                }}
            >

                <h3>
                    Lucro por Talhão
                </h3>

                <div
                    style={{
                        width: '100%',
                        height: 320
                    }}
                >

                    <ResponsiveContainer>

                        <BarChart
                            data={data.itens}
                        >

                            <CartesianGrid
                                strokeDasharray="3 3"
                            />

                            <XAxis
                                dataKey="talhaoNome"
                            />

                            <YAxis />

                            <Tooltip />

                            <Bar
                                dataKey="lucro"
                            />

                        </BarChart>

                    </ResponsiveContainer>

                </div>

            </div>

            {/* 🔥 GRÁFICO PRODUTIVIDADE */}
            <div
                className="card"
                style={{
                    marginBottom: 20
                }}
            >

                <h3>
                    Produtividade
                </h3>

                <div
                    style={{
                        width: '100%',
                        height: 320
                    }}
                >

                    <ResponsiveContainer>

                        <BarChart
                            data={data.itens}
                        >

                            <CartesianGrid
                                strokeDasharray="3 3"
                            />

                            <XAxis
                                dataKey="talhaoNome"
                            />

                            <YAxis />

                            <Tooltip />

                            <Bar
                                dataKey="produtividade"
                            />

                        </BarChart>

                    </ResponsiveContainer>

                </div>

            </div>

            {/* 🔥 GRÁFICO CUSTO */}
            <div
                className="card"
                style={{
                    marginBottom: 20
                }}
            >

                <h3>
                    Custo por Talhão
                </h3>

                <div
                    style={{
                        width: '100%',
                        height: 320
                    }}
                >

                    <ResponsiveContainer>

                        <BarChart
                            data={data.itens}
                        >

                            <CartesianGrid
                                strokeDasharray="3 3"
                            />

                            <XAxis
                                dataKey="talhaoNome"
                            />

                            <YAxis />

                            <Tooltip />

                            <Bar
                                dataKey="custo"
                            />

                        </BarChart>

                    </ResponsiveContainer>

                </div>

            </div>

            {/* 🔥 TABELA */}
            <div className="card">

                <h3>
                    Comparativo
                </h3>

                <table
                    style={{
                        width: '100%',
                        marginTop: 10
                    }}
                >

                    <thead>

                        <tr>

                            <th>Talhão</th>
                            <th>Produtividade</th>
                            <th>Custo</th>
                            <th>Receita</th>
                            <th>Lucro</th>
                            <th>Margem</th>

                        </tr>

                    </thead>

                    <tbody>

                        {data.itens.map(
                            (item, idx) => (

                                <tr key={idx}>

                                    <td>
                                        {item.talhaoNome}
                                    </td>

                                    <td>
                                        {
                                            numero(
                                                item.produtividade
                                            )
                                        }
                                    </td>

                                    <td>
                                        {
                                            moeda(
                                                item.custo
                                            )
                                        }
                                    </td>

                                    <td>
                                        {
                                            moeda(
                                                item.receita
                                            )
                                        }
                                    </td>

                                    <td
                                        style={{
                                            color:
                                                item.lucro < 0
                                                    ? '#dc2626'
                                                    : '#16a34a',
                                            fontWeight: 600
                                        }}
                                    >
                                        {
                                            moeda(
                                                item.lucro
                                            )
                                        }
                                    </td>

                                    <td>
                                        {
                                            numero(
                                                item.margem
                                            )
                                        }%
                                    </td>

                                </tr>

                            )
                        )}

                    </tbody>

                </table>

            </div>

        </PageLayout>
    )
}