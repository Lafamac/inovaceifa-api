import { useEffect, useState } from 'react'

import {
    LineChart,
    Line,
    XAxis,
    YAxis,
    Tooltip,
    ResponsiveContainer,
    CartesianGrid,
    Legend
} from 'recharts'

import PageLayout from '../components/PageLayout'
import Alert from '../components/Alert'

import {
    obterComparativoSafras
} from '../api/api'

export default function ComparativoSafrasPage() {

    const [data, setData] = useState(null)

    const [erro, setErro] = useState('')

    const [loading, setLoading] = useState(true)

    useEffect(() => {
        carregar()
    }, [])

    async function carregar() {

        try {

            const res =
                await obterComparativoSafras()

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
            <PageLayout title="Comparativo Safras">
                Carregando...
            </PageLayout>
        )
    }

    if (erro) {

        return (
            <PageLayout title="Comparativo Safras">
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
            title="Comparativo Safras"
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
                        🏆 Melhor Safra
                    </strong>

                    <p>
                        {
                            data.melhorSafra?.safraNome
                        }
                    </p>

                </div>

                <div className="card">

                    <strong>
                        💰 Maior Lucro
                    </strong>

                    <p>
                        {
                            data.maiorLucro?.safraNome
                        }
                    </p>

                </div>

                <div className="card">

                    <strong>
                        🌱 Maior Produtividade
                    </strong>

                    <p>
                        {
                            data.maiorProdutividade?.safraNome
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
                    Lucro por Safra
                </h3>

                <div
                    style={{
                        width: '100%',
                        height: 320
                    }}
                >

                    <ResponsiveContainer>

                        <LineChart
                            data={data.itens}
                        >

                            <CartesianGrid
                                strokeDasharray="3 3"
                            />

                            <XAxis
                                dataKey="safraNome"
                            />

                            <YAxis />

                            <Tooltip />

                            <Legend />

                            <Line
                                type="monotone"
                                dataKey="lucro"
                            />

                        </LineChart>

                    </ResponsiveContainer>

                </div>

            </div>

            {/* 🔥 PRODUTIVIDADE */}
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

                        <LineChart
                            data={data.itens}
                        >

                            <CartesianGrid
                                strokeDasharray="3 3"
                            />

                            <XAxis
                                dataKey="safraNome"
                            />

                            <YAxis />

                            <Tooltip />

                            <Legend />

                            <Line
                                type="monotone"
                                dataKey="produtividade"
                            />

                        </LineChart>

                    </ResponsiveContainer>

                </div>

            </div>

            {/* 🔥 MARGEM */}
            <div
                className="card"
                style={{
                    marginBottom: 20
                }}
            >

                <h3>
                    Margem
                </h3>

                <div
                    style={{
                        width: '100%',
                        height: 320
                    }}
                >

                    <ResponsiveContainer>

                        <LineChart
                            data={data.itens}
                        >

                            <CartesianGrid
                                strokeDasharray="3 3"
                            />

                            <XAxis
                                dataKey="safraNome"
                            />

                            <YAxis />

                            <Tooltip />

                            <Legend />

                            <Line
                                type="monotone"
                                dataKey="margem"
                            />

                        </LineChart>

                    </ResponsiveContainer>

                </div>

            </div>

            {/* 🔥 CUSTO/SACA */}
            <div
                className="card"
                style={{
                    marginBottom: 20
                }}
            >

                <h3>
                    Custo por Saca
                </h3>

                <div
                    style={{
                        width: '100%',
                        height: 320
                    }}
                >

                    <ResponsiveContainer>

                        <LineChart
                            data={data.itens}
                        >

                            <CartesianGrid
                                strokeDasharray="3 3"
                            />

                            <XAxis
                                dataKey="safraNome"
                            />

                            <YAxis />

                            <Tooltip />

                            <Legend />

                            <Line
                                type="monotone"
                                dataKey="custoPorSaca"
                            />

                        </LineChart>

                    </ResponsiveContainer>

                </div>

            </div>

            {/* 🔥 TABELA */}
            <div className="card">

                <h3>
                    Evolução das Safras
                </h3>

                <table
                    style={{
                        width: '100%',
                        marginTop: 10
                    }}
                >

                    <thead>

                        <tr>

                            <th>Safra</th>
                            <th>Receita</th>
                            <th>Custo</th>
                            <th>Lucro</th>
                            <th>Margem</th>
                            <th>Produtividade</th>

                        </tr>

                    </thead>

                    <tbody>

                        {data.itens.map(
                            (item, idx) => (

                                <tr key={idx}>

                                    <td>
                                        {item.safraNome}
                                    </td>

                                    <td>
                                        {
                                            moeda(
                                                item.receita
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

                                    <td>
                                        {
                                            numero(
                                                item.produtividade
                                            )
                                        }
                                    </td>

                                </tr>

                            )
                        )}

                    </tbody>

                </table>

                {/* 🔥 TOTAIS */}
                <div
                    style={{
                        display: 'flex',
                        justifyContent: 'flex-end',
                        marginTop: 20,
                        paddingRight: 30
                    }}
                >

                    <div
                        style={{
                            textAlign: 'right',
                            minWidth: 260
                        }}
                    >

                        <div style={{ marginBottom: 8 }}>

                            <strong>
                                Receita Total:
                            </strong>

                            {' '}
                            {moeda(data.totalReceita)}

                        </div>

                        <div style={{ marginBottom: 8 }}>

                            <strong>
                                Custo Total:
                            </strong>

                            {' '}
                            {moeda(data.totalCusto)}

                        </div>

                        <div
                            style={{
                                fontWeight: 700,
                                color:
                                    data.totalLucro < 0
                                        ? '#dc2626'
                                        : '#16a34a'
                            }}
                        >

                            <strong>
                                Lucro Total:
                            </strong>

                            {' '}
                            {moeda(data.totalLucro)}

                        </div>

                    </div>

                </div>

            </div>

        </PageLayout>
    )
}