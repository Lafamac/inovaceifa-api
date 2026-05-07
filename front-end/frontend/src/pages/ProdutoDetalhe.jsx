import { useEffect, useState } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import PageLayout from '../components/PageLayout'
import Alert from '../components/Alert'
import {
    buscarProduto,
    listarMovProdutos
} from '../api/api'
import '../styles/pages.css'

export default function ProdutoDetalhe() {
    const { id } = useParams()
    const navigate = useNavigate()

    const [produto, setProduto] = useState(null)
    const [movimentacoes, setMovimentacoes] = useState([])

    const [loading, setLoading] = useState(true)
    const [erro, setErro] = useState('')

    useEffect(() => {
        carregar()
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [id])

    async function carregar() {
        try {
            setLoading(true)

            const prodRes = await buscarProduto(id)
            const movRes = await listarMovProdutos()

            if (prodRes.ok && prodRes.data?.data) {
                setProduto(prodRes.data.data)
            }

            if (movRes.ok && movRes.data?.data) {
                setMovimentacoes(movRes.data.data.content || [])
            }

        } catch {
            setErro('Erro ao carregar dados')
        } finally {
            setLoading(false)
        }
    }

    if (loading) return <p style={{ padding: 20 }}>Carregando...</p>
    if (!produto) return <p style={{ padding: 20 }}>Produto não encontrado</p>

    const entradas = movimentacoes.filter(m => m.tipo === 'ENTRADA')
    const saidas = movimentacoes.filter(m => m.tipo === 'SAIDA')

    const totalEntradas = entradas.reduce((acc, m) => acc + (m.quantidade || 0), 0)
    const totalSaidas = saidas.reduce((acc, m) => acc + (m.quantidade || 0), 0)

    const estoqueAtual = produto.qtde || 0
    const custoMedio = produto.vlrUnitario || 0

    return (
        <PageLayout
            title={`Produto: ${produto.nome}`}
            showBack
            backTo="/produtos"
        >
            {erro && <Alert type="error" message={erro} />}

            {/* 🔥 RESUMO PRINCIPAL */}
            <div className="form-grid-2">

                <div className="info-card">
                    <div style={{
                        display: 'flex',
                        justifyContent: 'space-between',
                        alignItems: 'center'
                    }}>
                        <span>Estoque Atual</span>
                        <strong style={{ fontSize: 22 }}>
                            {estoqueAtual}
                        </strong>
                    </div>
                </div>

                <div className="info-card">
                    <div style={{
                        display: 'flex',
                        justifyContent: 'space-between',
                        alignItems: 'center'
                    }}>
                        <span>Custo Médio</span>
                        <strong style={{ fontSize: 22 }}>
                            R$ {custoMedio}
                        </strong>
                    </div>
                </div>

            </div>

            {/* 🔥 MOVIMENTAÇÃO */}
            <div className="form-grid-2" style={{ marginTop: 15 }}>

                <div className="info-card">
                    <div style={{
                        display: 'flex',
                        justifyContent: 'space-between'
                    }}>
                        <span>Total Entrada</span>
                        <strong style={{ color: '#2e7d32' }}>
                            {totalEntradas}
                        </strong>
                    </div>
                </div>

                <div className="info-card">
                    <div style={{
                        display: 'flex',
                        justifyContent: 'space-between'
                    }}>
                        <span>Total Saída</span>
                        <strong style={{ color: '#c62828' }}>
                            {totalSaidas}
                        </strong>
                    </div>
                </div>

            </div>

            {/* 🔥 AÇÃO */}
            <div style={{
                marginTop: 20,
                marginBottom: 20
            }}>
                <button
                    className="add-btn"
                    onClick={() => navigate('/produtos/movimentar')}
                >
                    + Movimentar Produto
                </button>
            </div>

            {/* 🔥 HISTÓRICO */}
            <div className="crud-card">

                <h3 style={{ marginBottom: 15 }}>
                    Histórico de Movimentações
                </h3>

                {movimentacoes.length === 0 && (
                    <p>Nenhuma movimentação registrada</p>
                )}

                {movimentacoes.map(mov => (
                    <div
                        key={mov.id}
                        className="crud-item"
                        style={{
                            display: 'grid',
                            gridTemplateColumns: '2fr 1fr 1fr',
                            alignItems: 'center'
                        }}
                    >

                        <div>
                            <strong style={{
                                color: mov.tipo === 'ENTRADA'
                                    ? '#2e7d32'
                                    : '#c62828'
                            }}>
                                {mov.tipo === 'ENTRADA' ? 'Entrada' : 'Saída'}
                            </strong>
                            <p style={{ margin: 0 }}>
                                {mov.data}
                            </p>
                        </div>

                        <div>
                            <span>Qtd</span>
                            <strong>{mov.quantidade}</strong>
                        </div>

                        <div>
                            <span>Valor</span>
                            <strong>
                                {mov.valorUnitario
                                    ? `R$ ${mov.valorUnitario}`
                                    : '-'}
                            </strong>
                        </div>

                    </div>
                ))}

            </div>

        </PageLayout>
    )
}