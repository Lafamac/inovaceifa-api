import { useState, useEffect } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { useQueryClient } from '@tanstack/react-query'
import '../styles/pages.css'
import '../styles/form.css'
import Alert from '../components/Alert'
import PageLayout from '../components/PageLayout'
import FormInput from '../components/FormInput'
import {
    criarProduto,
    buscarProduto,
    atualizarProduto,
    listarGrupos,
    listarFamilias
} from '../api/api'

export default function ProdutoForm() {
    const navigate = useNavigate()
    const { id } = useParams()
    const modoEdicao = !!id
    const queryClient = useQueryClient()

    const [nome, setNome] = useState('')
    const [codigo, setCodigo] = useState('')
    const [unidade, setUnidade] = useState('')
    const [grupoId, setGrupoId] = useState('')
    const [familiaId, setFamiliaId] = useState('')
    const [ativoNutr, setAtivoNutr] = useState('')
    const [qtde, setQtde] = useState('')
    const [vlrUnitario, setVlrUnitario] = useState('')
    const [precoCusto, setPrecoCusto] = useState('') // 🔥 NOVO

    const [grupos, setGrupos] = useState([])
    const [familias, setFamilias] = useState([])

    const [erro, setErro] = useState('')
    const [sucesso, setSucesso] = useState('')

    useEffect(() => {
        async function carregarReferencias() {
            const g = await listarGrupos()
            const f = await listarFamilias()

            if (g.ok) setGrupos(g.data?.data || [])
            if (f.ok) setFamilias(f.data?.data || [])
        }

        async function carregarProduto() {
            const response = await buscarProduto(id)

            if (!response.ok || !response.data?.data) {
                setErro('Erro ao carregar produto')
                return
            }

            const p = response.data.data

            setNome(p.nome || '')
            setCodigo(p.codigo || '')
            setUnidade(p.unidade || '')
            setGrupoId(p.grupo?.id || p.grupoId || '')
            setFamiliaId(p.familia?.id || p.familiaId || '')
            setAtivoNutr(p.ativoNutr || '')
            setQtde(p.qtde ?? '')
            setVlrUnitario(p.vlrUnitario ?? '')
            setPrecoCusto(p.precoCusto ?? '')
        }

        carregarReferencias()
        if (modoEdicao) carregarProduto()
    }, [id, modoEdicao])

    async function handleSubmit(e) {
        e.preventDefault()
        setErro('')
        setSucesso('')

        if (!nome) {
            setErro('Informe o nome do produto')
            return
        }

        if (!codigo) {
            setErro('Informe o código do produto')
            return
        }

        if (!unidade) {
            setErro('Informe a unidade')
            return
        }

        if (!grupoId || !familiaId) {
            setErro('Selecione grupo e família')
            return
        }

        const payload = {
            nome: nome.trim(),
            codigo: codigo.trim(),
            unidade: unidade.trim(),
            ativoNutr: ativoNutr && ativoNutr.trim() !== ''
                ? ativoNutr.toUpperCase()
                : 'N/A',
            grupoId: Number(grupoId),
            familiaId: Number(familiaId),
            qtde: qtde === '' ? 0 : Number(qtde),
            vlrUnitario: vlrUnitario === '' ? 0 : Number(vlrUnitario),
            precoCusto: precoCusto === '' ? 0 : Number(precoCusto) // 🔥 NOVO
        }

        const response = modoEdicao
            ? await atualizarProduto(id, payload)
            : await criarProduto(payload)

        if (!response.ok || !response.data?.success) {
            console.error(response)
            setErro(response.data?.message || 'Erro ao salvar produto')
            return
        }

        setSucesso(modoEdicao ? 'Atualizado com sucesso' : 'Criado com sucesso')

        setTimeout(() => {
            queryClient.invalidateQueries(['produtos'])
            navigate('/produtos')
        }, 500)
    }

    return (
        <PageLayout
            title={modoEdicao ? 'Editar Produto' : 'Novo Produto'}
            showBack
            backTo="/produtos"
        >
            {erro && <Alert type="error" message={erro} />}
            {sucesso && <Alert type="success" message={sucesso} />}

            <form className="form-container" onSubmit={handleSubmit}>

                <div className="form-grid-4">

                    <div>
                        <FormInput
                            label="Código"
                            value={codigo}
                            onChange={e => setCodigo(e.target.value)}
                            uppercase
                        />
                    </div>

                    <div style={{ gridColumn: 'span 2' }}>
                        <FormInput
                            label="Nome"
                            value={nome}
                            onChange={e => setNome(e.target.value)}
                            uppercase
                            required
                        />
                    </div>

                    <div>
                        <FormInput
                            label="Unidade"
                            value={unidade}
                            onChange={e => setUnidade(e.target.value)}
                            uppercase
                        />
                    </div>

                </div>

                <div className="form-grid-4">

                    <div>
                        <label className="form-label">Grupo *</label>
                        <select
                            className="form-input"
                            value={grupoId}
                            onChange={e => setGrupoId(e.target.value)}
                        >
                            <option value="">Selecione</option>
                            {grupos.map(g => (
                                <option key={g.id} value={g.id}>
                                    {g.descricao}
                                </option>
                            ))}
                        </select>
                    </div>

                    <div>
                        <label className="form-label">Família *</label>
                        <select
                            className="form-input"
                            value={familiaId}
                            onChange={e => setFamiliaId(e.target.value)}
                        >
                            <option value="">Selecione</option>
                            {familias.map(f => (
                                <option key={f.id} value={f.id}>
                                    {f.descricao}
                                </option>
                            ))}
                        </select>
                    </div>

                    <div style={{ gridColumn: 'span 2' }}>
                        <FormInput
                            label="Ativo Nutricional"
                            value={ativoNutr}
                            onChange={e => setAtivoNutr(e.target.value)}
                            uppercase
                        />
                    </div>

                </div>

                <div className="form-grid-4">

                    <div>
                        <FormInput
                            label="Quantidade"
                            type="number"
                            value={qtde}
                            onChange={e => setQtde(e.target.value)}
                        />
                    </div>

                    <div>
                        <FormInput
                            label="Valor Unitário"
                            type="number"
                            value={vlrUnitario}
                            onChange={e => setVlrUnitario(e.target.value)}
                        />
                    </div>

                    <div>
                        <FormInput
                            label="Preço de Custo"
                            type="number"
                            value={precoCusto}
                            onChange={e => setPrecoCusto(e.target.value)}
                        />
                    </div>

                </div>

                <div className="form-actions">
                    <button type="submit" className="add-btn">
                        {modoEdicao ? 'Salvar' : 'Criar'}
                    </button>
                </div>

            </form>
        </PageLayout>
    )
}