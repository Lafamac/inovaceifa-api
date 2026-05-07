import { useState, useEffect } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { useAuth } from '../auth/AuthContext'
import '../styles/pages.css'
import '../styles/form.css'
import PageLayout from '../components/PageLayout'
import Alert from '../components/Alert'
import FormInput from '../components/FormInput'
import {
    criarHoraMaquina,
    buscarMaquina,
    atualizarMaquina,
    listarOperacoesTalhao
} from '../api/api'

export default function HoraMaquinaForm() {
    const navigate = useNavigate()
    const { id } = useParams()
    const { user, safraAtiva } = useAuth()

    const fazendaId = user?.fazendaAtiva?.id
    const safraId = safraAtiva?.id

    const [maquina, setMaquina] = useState(null)
    const [dataExecucao, setDataExecucao] = useState('')
    const [servicoExec, setServicoExec] = useState('')
    const [nroOs, setNroOs] = useState('')
    const [operacaoTalhaoId, setOperacaoTalhaoId] = useState('')
    const [operacoesTalhao, setOperacoesTalhao] = useState([])
    const [horimetroInicial, setHorimetroInicial] = useState('')
    const [horimetroFinal, setHorimetroFinal] = useState('')
    const [custoHora, setCustoHora] = useState('') // 🔥 NOVO

    const [erro, setErro] = useState('')
    const [sucesso, setSucesso] = useState('')

    useEffect(() => {
        async function carregarMaquina() {
            const response = await buscarMaquina(id)

            if (response.ok && response.data?.data) {
                const maq = response.data.data
                setMaquina(maq)
                setHorimetroInicial(maq.horimetro || 0)
            } else {
                setErro('Erro ao carregar máquina')
            }
        }

        async function carregarOperacoes() {
            try {
                const response = await listarOperacoesTalhao()

                if (response.ok && response.data?.data) {
                    const lista = response.data.data.content || []
                    setOperacoesTalhao(lista)
                }
            } catch {
                setOperacoesTalhao([])
            }
        }

        carregarMaquina()
        carregarOperacoes()
    }, [id])

    const horasCalculadas =
        horimetroFinal && horimetroInicial
            ? (Number(horimetroFinal) - Number(horimetroInicial)).toFixed(2)
            : ''

    async function handleSubmit(e) {
        e.preventDefault()
        setErro('')
        setSucesso('')

        if (!fazendaId || !safraId) {
            setErro('Safra não definida.')
            return
        }

        if (!dataExecucao) {
            setErro('Informe a data de execução')
            return
        }

        if (!horimetroFinal) {
            setErro('Informe o horímetro final')
            return
        }

        if (Number(horimetroFinal) <= Number(horimetroInicial)) {
            setErro('Horímetro final deve ser maior que o inicial')
            return
        }

        const payloadHora = {
            maquinaId: Number(id),
            fazendaId,
            safraId,
            servicoExec,
            nroOs,
            operacaoTalhaoId: operacaoTalhaoId ? Number(operacaoTalhaoId) : null,
            dataExecucao,
            horimetroInicial: Number(horimetroInicial),
            horimetroFinal: Number(horimetroFinal),
            horasTrabalhadas: Number(horasCalculadas),

            // 🔥 NOVO CAMPO
            custoHora: custoHora ? Number(custoHora) : 0
        }

        try {
            const responseHora = await criarHoraMaquina(payloadHora)

            if (!responseHora.ok || !responseHora.data?.success) {
                setErro(responseHora.data?.message || 'Erro ao salvar hora')
                return
            }

            await atualizarMaquina(id, {
                ...maquina,
                horimetro: Number(horimetroFinal)
            })

            setSucesso('Hora lançada e horímetro atualizado')

            setTimeout(() => {
                navigate(`/maquinas/${id}`)
            }, 1200)

        } catch {
            setErro('Erro ao salvar hora')
        }
    }

    return (
        <PageLayout
            title="Nova Hora da Máquina"
            showBack
            backTo={`/maquinas/${id}`}
        >
            {erro && <Alert type="error" message={erro} />}
            {sucesso && <Alert type="success" message={sucesso} />}

            <form className="form-container" onSubmit={handleSubmit}>

                <div className="form-grid-4">
                    <div style={{ gridColumn: 'span 2' }}>
                        <FormInput
                            label="Data de Execução"
                            type="date"
                            value={dataExecucao}
                            onChange={e => setDataExecucao(e.target.value)}
                            required
                        />
                    </div>

                    <div style={{ gridColumn: 'span 2' }}>
                        <FormInput
                            label="Nº OS (opcional)"
                            value={nroOs}
                            onChange={e => setNroOs(e.target.value)}
                        />
                    </div>
                </div>

                <FormInput
                    label="Serviço Executado"
                    value={servicoExec}
                    onChange={e => setServicoExec(e.target.value)}
                    uppercase
                />

                <div className="form-grid-4">
                    <div style={{ gridColumn: 'span 4' }}>
                        <label className="form-label">Operação do Talhão</label>
                        <select
                            className="form-input"
                            value={operacaoTalhaoId}
                            onChange={e => setOperacaoTalhaoId(e.target.value)}
                        >
                            <option value="">Selecione...</option>

                            {operacoesTalhao.map(op => (
                                <option key={op.id} value={op.id}>
                                    {op.descricao || `Operação ${op.id}`}
                                </option>
                            ))}
                        </select>
                    </div>
                </div>

                <div className="form-grid-4">
                    <div style={{ gridColumn: 'span 2' }}>
                        <FormInput
                            label="Horímetro Inicial"
                            type="number"
                            value={horimetroInicial}
                            disabled
                        />
                    </div>

                    <div style={{ gridColumn: 'span 2' }}>
                        <FormInput
                            label="Horímetro Final"
                            type="number"
                            value={horimetroFinal}
                            onChange={e => setHorimetroFinal(e.target.value)}
                            required
                        />
                    </div>
                </div>

                {horasCalculadas && (
                    <FormInput
                        label="Horas Trabalhadas"
                        value={horasCalculadas}
                        disabled
                    />
                )}

                {/* 🔥 NOVO CAMPO */}
                <FormInput
                    label="Custo por Hora (R$)"
                    type="number"
                    value={custoHora}
                    onChange={e => setCustoHora(e.target.value)}
                />

                <div className="form-actions">
                    <button type="submit" className="add-btn">
                        Salvar Hora
                    </button>
                </div>

            </form>
        </PageLayout>
    )
}