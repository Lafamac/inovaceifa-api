import { useState, useEffect } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { useAuth } from '../auth/AuthContext'
import '../styles/pages.css'
import '../styles/form.css'
import PageLayout from '../components/PageLayout'
import Alert from '../components/Alert'
import FormInput from '../components/FormInput'
import {
    criarGastoMaquina,
    listarTiposGastoMaquina
} from '../api/api'

export default function GastoMaquinaForm() {
    const navigate = useNavigate()
    const { id } = useParams()
    const { user, safraAtiva } = useAuth()

    const fazendaId = user?.fazendaAtiva?.id
    const safraId = safraAtiva?.id

    const [tipoGastoId, setTipoGastoId] = useState('')
    const [data, setData] = useState('')
    const [descricao, setDescricao] = useState('')
    const [valor, setValor] = useState('')
    const [tipos, setTipos] = useState([])
    const [erro, setErro] = useState('')
    const [sucesso, setSucesso] = useState('')

    useEffect(() => {
        async function carregarTipos() {
            const response = await listarTiposGastoMaquina()
            if (response.ok && response.data?.success) {
                setTipos(response.data.data)
            }
        }
        carregarTipos()
    }, [])

    async function handleSubmit(e) {
        e.preventDefault()
        setErro('')
        setSucesso('')

        if (!fazendaId || !safraId) {
            setErro('Safra não definida.')
            return
        }

        if (!tipoGastoId) {
            setErro('Selecione o tipo de gasto')
            return
        }

        if (!valor || Number(valor) <= 0) {
            setErro('Informe um valor válido')
            return
        }

        const payload = {
            maquinaId: Number(id),
            fazendaId,
            safraId,
            tipoGastoId: Number(tipoGastoId),
            data,
            descricao,
            valor: Number(valor)
        }

        const response = await criarGastoMaquina(payload)

        if (!response.ok || !response.data?.success) {
            setErro(response.data?.message || 'Erro ao salvar gasto')
            return
        }

        setSucesso('Gasto lançado com sucesso')

        setTimeout(() => {
            navigate(`/maquinas/${id}`)
        }, 1200)
    }

    return (
        <PageLayout
            title="Novo Gasto da Máquina"
            showBack
            backTo={`/maquinas/${id}`}
        >
            {erro && <Alert type="error" message={erro} />}
            {sucesso && <Alert type="success" message={sucesso} />}

            <form className="form-container" onSubmit={handleSubmit}>

                <div className="form-grid-4">
                    <div style={{ gridColumn: 'span 2' }}>
                        <label className="form-label">Tipo de Gasto</label>
                        <select
                            className="form-input"
                            value={tipoGastoId}
                            onChange={e => setTipoGastoId(e.target.value)}
                            required
                        >
                            <option value="">Selecione...</option>
                            {tipos.map(t => (
                                <option key={t.id} value={t.id}>
                                    {t.descricao}
                                </option>
                            ))}
                        </select>
                    </div>

                    <div style={{ gridColumn: 'span 2' }}>
                        <FormInput
                            label="Data"
                            type="date"
                            value={data}
                            onChange={e => setData(e.target.value)}
                            required
                        />
                    </div>
                </div>

                <FormInput
                    label="Descrição"
                    value={descricao}
                    onChange={e => setDescricao(e.target.value)}
                    uppercase
                />

                <div className="form-grid-4">
                    <div style={{ gridColumn: 'span 2' }}>
                        <FormInput
                            label="Valor (R$)"
                            type="number"
                            value={valor}
                            onChange={e => setValor(e.target.value)}
                            required
                        />
                    </div>
                </div>

                <div className="form-actions">
                    <button type="submit" className="add-btn">
                        Salvar Gasto
                    </button>
                </div>

            </form>
        </PageLayout>
    )
}