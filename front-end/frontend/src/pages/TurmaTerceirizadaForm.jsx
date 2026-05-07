import { useState, useEffect } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { useQueryClient } from '@tanstack/react-query'

import PageLayout from '../components/PageLayout'
import FormInput from '../components/FormInput'
import Alert from '../components/Alert'

import {
    criarTurmaTerceirizada,
    buscarTurmaTerceirizada,
    atualizarTurmaTerceirizada,

    // 🔥 ALTERADO
    listarReferenciasOperacaoTalhao

} from '../api/api'

export default function TurmaTerceirizadaForm() {

    const navigate = useNavigate()

    const { id } = useParams()

    const modoEdicao = !!id

    const queryClient = useQueryClient()

    const [nome, setNome] = useState('')

    const [responsavel, setResponsavel] =
        useState('')

    const [quantidadePessoas,
        setQuantidadePessoas] = useState('')

    const [valorDiaria, setValorDiaria] =
        useState('')

    const [valorPorSaca, setValorPorSaca] =
        useState('')

    const [operacaoId, setOperacaoId] =
        useState('')

    const [operacoes, setOperacoes] =
        useState([])

    const [erro, setErro] = useState('')

    const [sucesso, setSucesso] =
        useState('')

    useEffect(() => {

        async function carregarOperacoes() {

            try {

                // 🔥 ALTERADO
                const res =
                    await listarReferenciasOperacaoTalhao()

                console.log(
                    'OPERACOES:',
                    res.data
                )

                if (
                    !res.ok ||
                    !res.data?.data
                ) {

                    setOperacoes([])

                    return
                }

                const data = res.data.data

                const lista =
                    data.content || data || []

                setOperacoes(
                    Array.isArray(lista)
                        ? lista
                        : []
                )

            } catch (err) {

                console.error(err)

                setOperacoes([])
            }
        }

        async function carregar() {

            try {

                const res =
                    await buscarTurmaTerceirizada(id)

                const t =
                    res.data?.data

                if (!t) return

                setNome(
                    t.nome || ''
                )

                setResponsavel(
                    t.responsavel || ''
                )

                setQuantidadePessoas(
                    t.quantidadePessoas || ''
                )

                setValorDiaria(
                    t.valorDiaria || ''
                )

                setValorPorSaca(
                    t.valorPorSaca || ''
                )

                setOperacaoId(
                    t.operacaoId || ''
                )

            } catch (err) {

                console.error(err)
            }
        }

        carregarOperacoes()

        if (modoEdicao) {
            carregar()
        }

    }, [id, modoEdicao])

    async function handleSubmit(e) {

        e.preventDefault()

        setErro('')

        setSucesso('')

        if (!nome) {

            setErro(
                'Informe o nome da turma'
            )

            return
        }

        const payload = {

            nome,

            responsavel,

            quantidadePessoas:
                quantidadePessoas
                    ? Number(quantidadePessoas)
                    : 0,

            valorDiaria:
                valorDiaria
                    ? Number(valorDiaria)
                    : null,

            valorPorSaca:
                valorPorSaca
                    ? Number(valorPorSaca)
                    : null,

            operacaoId:
                operacaoId
                    ? Number(operacaoId)
                    : null
        }

        try {

            const res = modoEdicao

                ? await atualizarTurmaTerceirizada(
                    id,
                    payload
                )

                : await criarTurmaTerceirizada(
                    payload
                )

            if (
                !res.ok ||
                !res.data?.success
            ) {

                setErro(
                    res.data?.message ||
                    'Erro ao salvar'
                )

                return
            }

            setSucesso(
                'Salvo com sucesso'
            )

            setTimeout(() => {

                queryClient.invalidateQueries({
                    queryKey: ['Turma']
                })

                navigate(
                    '/turmas-terceirizadas'
                )

            }, 800)

        } catch (err) {

            console.error(err)

            setErro(
                err?.response?.data?.message ||
                'Erro ao salvar'
            )
        }
    }

    return (

        <PageLayout
            title="Turma Terceirizada"
            showBack
            backTo="/turmas-terceirizadas"
        >

            {erro && (
                <Alert
                    type="error"
                    message={erro}
                />
            )}

            {sucesso && (
                <Alert
                    type="success"
                    message={sucesso}
                />
            )}

            <form
                className="form-container"
                onSubmit={handleSubmit}
            >

                <div className="form-grid-2">

                    <FormInput
                        label="Nome"
                        value={nome}
                        onChange={e =>
                            setNome(
                                e.target.value
                            )
                        }
                    />

                    <FormInput
                        label="Responsável"
                        value={responsavel}
                        onChange={e =>
                            setResponsavel(
                                e.target.value
                            )
                        }
                    />

                </div>

                <div>

                    <label>
                        Operação
                    </label>

                    <select
                        value={operacaoId}
                        onChange={e =>
                            setOperacaoId(
                                e.target.value
                            )
                        }
                        className="form-input"
                        disabled={
                            operacoes.length === 0
                        }
                    >

                        {operacoes.length === 0 ? (

                            <option value="">
                                Nenhuma operação cadastrada
                            </option>

                        ) : (

                            <>
                                <option value="">
                                    Selecione...
                                </option>

                                {operacoes.map(op => (

                                    <option
                                        key={op.id}
                                        value={op.id}
                                    >
                                        {op.descricao ||
                                            op.nome}
                                    </option>

                                ))}

                            </>

                        )}

                    </select>

                </div>

                <div className="form-grid-3">

                    <FormInput
                        label="Quantidade Pessoas"
                        type="number"
                        value={quantidadePessoas}
                        onChange={e =>
                            setQuantidadePessoas(
                                e.target.value
                            )
                        }
                    />

                    <FormInput
                        label="Valor Diária"
                        type="number"
                        value={valorDiaria}
                        onChange={e =>
                            setValorDiaria(
                                e.target.value
                            )
                        }
                    />

                    <FormInput
                        label="Valor por Saca"
                        type="number"
                        value={valorPorSaca}
                        onChange={e =>
                            setValorPorSaca(
                                e.target.value
                            )
                        }
                    />

                </div>

                <div className="form-actions">

                    <button className="add-btn">
                        Salvar
                    </button>

                </div>

            </form>

        </PageLayout>
    )
}