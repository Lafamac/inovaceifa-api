import { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { useAuth } from '../auth/AuthContext'
import PageLayout from '../components/PageLayout'
import Alert from '../components/Alert'
import CrudCardList from '../components/CrudCardList'
import CrudCard from '../components/CrudCard'
import EmptyState from '../components/EmptyState'
import SkeletonCard from '../components/SkeletonCard'
import {
    buscarMaquina,
    listarHorasMaquina,
    listarGastosMaquina
} from '../api/api'
import { Plus } from 'lucide-react'

export default function MaquinaDetalhe() {
    const { id } = useParams()
    const navigate = useNavigate()
    const { user, safraAtiva } = useAuth()

    const fazendaId = user?.fazendaAtiva?.id
    const safraId = safraAtiva?.id

    const [maquina, setMaquina] = useState(null)
    const [horas, setHoras] = useState([])
    const [gastos, setGastos] = useState([])
    const [aba, setAba] = useState('horas')
    const [loading, setLoading] = useState(true)
    const [erro, setErro] = useState('')

    useEffect(() => {
        carregarDados()
    }, [id, safraId])

    async function carregarDados() {
        try {
            setLoading(true)

            const maquinaRes = await buscarMaquina(id)
            if (!maquinaRes.ok || !maquinaRes.data?.data) {
                setErro('Erro ao carregar máquina')
                return
            }

            setMaquina(maquinaRes.data.data)

            if (fazendaId && safraId) {
                const horasRes = await listarHorasMaquina({
                    maquinaId: Number(id),
                    fazendaId,
                    safraId
                })

                const gastosRes = await listarGastosMaquina({
                    maquinaId: Number(id),
                    fazendaId,
                    safraId
                })

                if (horasRes.ok) {
                    setHoras(horasRes.data?.data || [])
                }

                if (gastosRes.ok) {
                    setGastos(gastosRes.data?.data || [])
                }
            }

        } catch (error) {
            setErro('Erro ao carregar dados')
        } finally {
            setLoading(false)
        }
    }

    /* =============================
       CÁLCULOS (CORRIGIDO)
    ============================= */

    const listaHoras = Array.isArray(horas) ? horas : []
    const listaGastos = Array.isArray(gastos) ? gastos : []

    const totalHoras = listaHoras.reduce(
        (acc, h) => acc + Number(h.horasTrabalhadas || 0),
        0
    )

    // 🔥 NOVO: usa custoHora
    const custoTotalHoras = listaHoras.reduce(
        (acc, h) =>
            acc +
            Number(h.horasTrabalhadas || 0) *
            Number(h.custoHora || 0),
        0
    )

    const totalGastos = listaGastos.reduce(
        (acc, g) => acc + Number(g.valor || 0),
        0
    )

    const custoTotal = custoTotalHoras + totalGastos

    const custoPorHora =
        totalHoras > 0 ? (custoTotal / totalHoras).toFixed(2) : 0

    /* =============================
       RENDER
    ============================= */

    return (
        <PageLayout
            title="Detalhes da Máquina"
            showBack
            backTo="/maquinas"
        >
            {erro && <Alert type="error" message={erro} />}
            {loading && <SkeletonCard />}

            {!loading && maquina && (
                <>
                    {/* IDENTIFICAÇÃO */}
                    <CrudCard
                        title={maquina.nome}
                        subtitle={`${maquina.marca || '-'} / ${maquina.modelo || '-'}`}
                        ativo={maquina.ativo}
                    />

                    {/* KPI GRID */}
                    <div className="kpi-grid">

                        <div className="kpi-card kpi-blue">
                            <div className="kpi-value">
                                {totalHoras.toFixed(2)}h
                            </div>
                            <div className="kpi-label">
                                Total Horas (Safra)
                            </div>
                        </div>

                        <div className="kpi-card kpi-red">
                            <div className="kpi-value">
                                R$ {custoTotal.toFixed(2)}
                            </div>
                            <div className="kpi-label">
                                Custo Total
                            </div>
                        </div>

                        <div className="kpi-card kpi-purple">
                            <div className="kpi-value">
                                R$ {custoPorHora}
                            </div>
                            <div className="kpi-label">
                                Custo por Hora
                            </div>
                        </div>

                        <div className="kpi-card kpi-green">
                            <div className="kpi-value">
                                {maquina.horimetro || 0}h
                            </div>
                            <div className="kpi-label">
                                Horímetro Atual
                            </div>
                        </div>

                    </div>

                    {/* TABS + BOTÃO */}
                    <div
                        style={{
                            display: 'flex',
                            justifyContent: 'space-between',
                            alignItems: 'center',
                            marginTop: 32
                        }}
                    >
                        <div className="segmented-control">
                            <button
                                className={`segmented-button ${aba === 'horas' ? 'active' : ''}`}
                                onClick={() => setAba('horas')}
                            >
                                Horas
                            </button>

                            <button
                                className={`segmented-button ${aba === 'gastos' ? 'active' : ''}`}
                                onClick={() => setAba('gastos')}
                            >
                                Gastos
                            </button>
                        </div>

                        <button
                            className="add-btn"
                            onClick={() =>
                                navigate(
                                    aba === 'horas'
                                        ? `/maquinas/${id}/horas/nova`
                                        : `/maquinas/${id}/gastos/nova`
                                )
                            }
                        >
                            <Plus size={16} /> Novo
                        </button>
                    </div>

                    {/* LISTA */}
                    {aba === 'horas' && (
                        <>
                            {listaHoras.length === 0 && (
                                <EmptyState message="Nenhuma hora lançada nesta safra." />
                            )}

                            <CrudCardList>
                                {listaHoras.map(h => (
                                    <CrudCard
                                        key={h.id}
                                        title={`Data: ${h.dataExecucao}`}
                                        subtitle={`Horas: ${h.horasTrabalhadas} | R$ ${Number(h.custoHora || 0).toFixed(2)}/h`}
                                    />
                                ))}
                            </CrudCardList>
                        </>
                    )}

                    {aba === 'gastos' && (
                        <>
                            {listaGastos.length === 0 && (
                                <EmptyState message="Nenhum gasto lançado nesta safra." />
                            )}

                            <CrudCardList>
                                {listaGastos.map(g => (
                                    <CrudCard
                                        key={g.id}
                                        title={`Data: ${g.data}`}
                                        subtitle={`R$ ${Number(g.valor || 0).toFixed(2)}`}
                                    />
                                ))}
                            </CrudCardList>
                        </>
                    )}
                </>
            )}
        </PageLayout>
    )
}