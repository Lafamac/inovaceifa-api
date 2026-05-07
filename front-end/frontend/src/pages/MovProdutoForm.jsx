import { useState, useEffect, useRef } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../auth/AuthContext'
import '../styles/pages.css'
import '../styles/form.css'
import PageLayout from '../components/PageLayout'
import Alert from '../components/Alert'
import FormInput from '../components/FormInput'
import {
    listarProdutos,
    criarMovProduto
} from '../api/api'

export default function MovProdutoForm() {
    const navigate = useNavigate()
    const { user, safraAtiva } = useAuth()

    const fazendaId = user?.fazendaAtiva?.id
    const safraId = safraAtiva?.id

    const [fornecedor, setFornecedor] = useState('')
    const [numeroNotaFiscal, setNumeroNotaFiscal] = useState('')
    const [dataEntrada, setDataEntrada] = useState('')
    const [dataVencimento, setDataVencimento] = useState('')

    const [produtos, setProdutos] = useState([])
    const [itens, setItens] = useState([
        { produtoId: '', quantidade: '', valorUnitario: '' }
    ])

    const refs = useRef([])
    const addButtonRef = useRef(null)

    const [erro, setErro] = useState('')
    const [sucesso, setSucesso] = useState('')

    const dadosNotaPreenchidos =
        fornecedor &&
        numeroNotaFiscal &&
        dataEntrada &&
        dataVencimento
    useEffect(() => {
        if (dadosNotaPreenchidos) {
            setTimeout(() => {
                refs.current[0]?.produto?.focus()
            }, 100)
        }
    }, [dadosNotaPreenchidos])
    
    useEffect(() => {
        async function carregarProdutos() {
            const response = await listarProdutos()
            if (response.ok && response.data?.data?.content) {
                setProdutos(response.data.data.content)
            }
        }
        carregarProdutos()
    }, [])

    function adicionarItem() {
        setItens(prev => [
            ...prev,
            { produtoId: '', quantidade: '', valorUnitario: '' }
        ])

        setTimeout(() => {
            refs.current[itens.length]?.produto?.focus()
        }, 100)
    }

    function removerItem(index) {
        const novos = [...itens]
        novos.splice(index, 1)
        setItens(novos)
    }

    function atualizarItem(index, campo, valor) {
        const novos = [...itens]
        novos[index][campo] = valor
        setItens(novos)
    }

    function handleEnter(e, index, campo) {
        if (e.key === 'Enter') {
            e.preventDefault()

            if (campo === 'produto') {
                refs.current[index]?.qtd?.focus()
            }

            if (campo === 'qtd') {
                refs.current[index]?.vlr?.focus()
            }

            if (campo === 'vlr') {
                addButtonRef.current?.focus()
            }
        }
    }

    function validarItens() {
        for (let i = 0; i < itens.length; i++) {
            const item = itens[i]

            if (!item.produtoId) {
                return `Item ${i + 1}: selecione o produto`
            }

            if (!item.quantidade || Number(item.quantidade) <= 0) {
                return `Item ${i + 1}: quantidade deve ser maior que zero`
            }

            if (!item.valorUnitario || Number(item.valorUnitario) <= 0) {
                return `Item ${i + 1}: valor unitário deve ser maior que zero`
            }
        }

        return null
    }

    const totalNota = itens.reduce((acc, item) => {
        const qtd = Number(item.quantidade) || 0
        const vlr = Number(item.valorUnitario) || 0
        return acc + (qtd * vlr)
    }, 0)

    async function handleSubmit(e) {
        e.preventDefault()
        setErro('')
        setSucesso('')

        if (!dadosNotaPreenchidos) {
            setErro('Preencha os dados da nota')
            return
        }

        const erroValidacao = validarItens()
        if (erroValidacao) {
            setErro(erroValidacao)
            return
        }

        const payload = {
            fornecedor,
            numeroNotaFiscal,
            dataEntrada,
            dataVencimento,
            itens: itens.map(i => ({
                produtoId: Number(i.produtoId),
                quantidade: Number(i.quantidade),
                valorUnitario: Number(i.valorUnitario)
            }))
        }

        const response = await criarMovProduto(payload)

        if (!response.ok || !response.data?.success) {
            setErro(response.data?.message || 'Erro ao salvar movimentação')
            return
        }

        setSucesso('Movimentação registrada com sucesso')

        setTimeout(() => {
            navigate('/produtos')
        }, 1200)
    }

    return (
        <PageLayout title="Movimentação de Produtos" showBack backTo="/produtos">

            {erro && <Alert type="error" message={erro} />}
            {sucesso && <Alert type="success" message={sucesso} />}

            <form className="form-container" onSubmit={handleSubmit}>

                <div className="form-grid-4">
                    <FormInput label="Fornecedor" value={fornecedor} onChange={e => setFornecedor(e.target.value)} uppercase />
                    <FormInput label="Nota Fiscal" value={numeroNotaFiscal} onChange={e => setNumeroNotaFiscal(e.target.value)} uppercase />
                    <FormInput label="Data Entrada" type="date" value={dataEntrada} onChange={e => setDataEntrada(e.target.value)} />
                    <FormInput label="Data Vencimento" type="date" value={dataVencimento} onChange={e => setDataVencimento(e.target.value)} />
                </div>

                {!dadosNotaPreenchidos && (
                    <p style={{ color: '#999', marginTop: 10 }}>
                        Preencha os dados da nota para liberar os itens
                    </p>
                )}

                <div style={{
                    marginTop: 10,
                    border: '1px solid #ddd',
                    borderRadius: 8,
                    opacity: dadosNotaPreenchidos ? 1 : 0.5,
                    pointerEvents: dadosNotaPreenchidos ? 'auto' : 'none'
                }}>

                    <div style={{
                        display: 'grid',
                        gridTemplateColumns: '3fr 1fr 1fr 40px',
                        background: '#f5f5f5',
                        padding: 10,
                        fontWeight: 600
                    }}>
                        <div>Produto</div>
                        <div>Qtd</div>
                        <div>Vlr Unit</div>
                        <div></div>
                    </div>

                    {itens.map((item, index) => {
                        if (!refs.current[index]) refs.current[index] = {}

                        return (
                            <div key={index} style={{
                                display: 'grid',
                                gridTemplateColumns: '3fr 1fr 1fr 40px',
                                padding: 8,
                                borderTop: '1px solid #eee',
                                alignItems: 'center'
                            }}>

                                <select
                                    ref={el => refs.current[index].produto = el}
                                    className="form-input"
                                    value={item.produtoId}
                                    onChange={e => atualizarItem(index, 'produtoId', e.target.value)}
                                    onKeyDown={(e) => handleEnter(e, index, 'produto')}
                                >
                                    <option value="">Selecione</option>
                                    {produtos.map(p => (
                                        <option key={p.id} value={p.id}>
                                            {p.nome}
                                        </option>
                                    ))}
                                </select>

                                <input
                                    ref={el => refs.current[index].qtd = el}
                                    className="form-input"
                                    type="number"
                                    value={item.quantidade}
                                    onChange={e => atualizarItem(index, 'quantidade', e.target.value)}
                                    onKeyDown={(e) => handleEnter(e, index, 'qtd')}
                                />

                                <input
                                    ref={el => refs.current[index].vlr = el}
                                    className="form-input"
                                    type="number"
                                    value={item.valorUnitario}
                                    onChange={e => atualizarItem(index, 'valorUnitario', e.target.value)}
                                    onKeyDown={(e) => handleEnter(e, index, 'vlr')}
                                />

                                <button
                                    type="button"
                                    onClick={() => removerItem(index)}
                                    style={{
                                        color: '#c62828',
                                        border: 'none',
                                        background: 'transparent',
                                        cursor: 'pointer',
                                        display: 'flex',
                                        alignItems: 'center',
                                        justifyContent: 'center'
                                    }}
                                >
                                    ✕
                                </button>

                            </div>
                        )
                    })}

                </div>

                <button
                    ref={addButtonRef}
                    type="button"
                    disabled={!dadosNotaPreenchidos}
                    onClick={adicionarItem}
                    onKeyDown={(e) => {
                        if (e.key === 'Enter') {
                            e.preventDefault()
                            adicionarItem()
                        }
                    }}
                >
                    + Adicionar
                </button>

                <div style={{ marginTop: 10, textAlign: 'right', fontWeight: 600 }}>
                    Total: R$ {totalNota.toFixed(2)}
                </div>

                <div className="form-actions">
                    <button type="submit" className="add-btn">
                        Salvar
                    </button>
                </div>

            </form>
        </PageLayout>
    )
}