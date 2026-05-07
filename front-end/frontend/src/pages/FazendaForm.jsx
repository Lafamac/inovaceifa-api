import { useState, useEffect } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import '../styles/pages.css'
import '../styles/form.css'
import Alert from '../components/Alert'
import PageLayout from '../components/PageLayout'
import FormInput from '../components/FormInput'

import {
    verificarCnpj,
    criarFazenda,
    buscarFazenda,
    atualizarFazenda
} from '../api/api'

import { useAuth } from '../auth/AuthContext'
import { useQueryClient } from '@tanstack/react-query'

function formatarCnpj(valor) {

    valor = valor.replace(/\D/g, '')

    valor = valor.replace(/^(\d{2})(\d)/, '$1.$2')

    valor = valor.replace(
        /^(\d{2})\.(\d{3})(\d)/,
        '$1.$2.$3'
    )

    valor = valor.replace(
        /\.(\d{3})(\d)/,
        '.$1/$2'
    )

    valor = valor.replace(
        /(\d{4})(\d)/,
        '$1-$2'
    )

    return valor.slice(0, 18)
}

function limparCnpj(valor) {
    return valor.replace(/\D/g, '')
}

export default function FazendaForm() {

    const navigate = useNavigate()

    const { id } = useParams()

    const modoEdicao = !!id

    const { user } = useAuth()

    const queryClient = useQueryClient()

    const backRoute = '/selecionar-fazenda'

    const [cnpj, setCnpj] = useState('')
    const [nome, setNome] = useState('')
    const [endereco, setEndereco] = useState('')
    const [cidade, setCidade] = useState('')
    const [estado, setEstado] = useState('')

    const [nomeSafra, setNomeSafra] = useState('')
    const [dataInicio, setDataInicio] = useState('')
    const [dataFim, setDataFim] = useState('')

    const [cnpjVerificado, setCnpjVerificado] =
        useState(modoEdicao)

    const [erro, setErro] = useState('')
    const [sucesso, setSucesso] = useState('')

    useEffect(() => {

        async function carregarFazenda() {

            try {

                const response = await buscarFazenda(id)

                if (!response.ok || !response.data?.data) {

                    setErro('Erro ao carregar fazenda')

                    return
                }

                const f = response.data.data

                setCnpj(formatarCnpj(f.cnpj || ''))

                setNome(f.nome || '')

                setEndereco(f.endereco || '')

                setCidade(f.cidade || '')

                setEstado(f.estado || '')

            } catch (err) {

                console.error(err)

                setErro('Erro ao carregar fazenda')
            }
        }

        if (modoEdicao) {
            carregarFazenda()
        }

    }, [id, modoEdicao])

    function handleCnpjChange(e) {

        const valor = formatarCnpj(e.target.value)

        setCnpj(valor)

        setCnpjVerificado(false)

        setErro('')

        setSucesso('')
    }

    async function validarCnpjNoBackend(valorCnpj) {

        setErro('')

        setSucesso('')

        const cnpjLimpo = limparCnpj(valorCnpj)

        if (cnpjLimpo.length !== 14) {

            setErro('Informe um CNPJ válido')

            setCnpjVerificado(false)

            return false
        }

        try {

            const response =
                await verificarCnpj(cnpjLimpo)

            if (!response.data?.success) {

                setErro(
                    response.data?.message ||
                    'Erro ao verificar CNPJ'
                )

                setCnpjVerificado(false)

                return false
            }

            if (response.data.data === false) {

                setErro(
                    'Fazenda já cadastrada para este CNPJ'
                )

                setCnpjVerificado(false)

                return false
            }

            setCnpjVerificado(true)

            return true

        } catch (error) {

            console.error(error)

            setErro('Erro ao verificar CNPJ')

            setCnpjVerificado(false)

            return false
        }
    }

    async function handleSubmit(e) {

        e.preventDefault()

        setErro('')

        setSucesso('')

        const cnpjLimpo = limparCnpj(cnpj)

        if (!modoEdicao) {

            const valido =
                await validarCnpjNoBackend(cnpj)

            if (!valido) return
        }

        if (!nome) {

            setErro('Informe o nome da fazenda')

            return
        }

        if (!modoEdicao) {

            if (!dataInicio || !dataFim) {

                setErro(
                    'Preencha os dados da safra inicial'
                )

                return
            }

            if (
                new Date(dataFim) <
                new Date(dataInicio)
            ) {

                setErro(
                    'A data de término não pode ser menor que a data de início'
                )

                return
            }
        }

        try {

            let response

            if (modoEdicao) {

                response = await atualizarFazenda(id, {
                    cnpj: cnpjLimpo,
                    nome,
                    endereco,
                    cidade,
                    estado
                })

            } else {

                const nomeSafraFinal =
                    nomeSafra?.trim() ||
                    `${dataInicio.slice(0, 4)}/${dataFim.slice(0, 4)}`

                const proprietarioId =
                    user?.proprietarioId ||
                    user?.id

                if (!proprietarioId) {

                    setErro(
                        'Proprietário não identificado'
                    )

                    return
                }

                response = await criarFazenda({

                    proprietarioId,

                    cnpj: cnpjLimpo,

                    nome,

                    endereco,

                    cidade,

                    estado,

                    nomeSafraInicial: nomeSafraFinal,

                    dataInicio,

                    dataFim
                })
            }

            if (!response.ok || !response.data?.success) {

                setErro(
                    response.data?.message ||
                    'Erro ao salvar fazenda'
                )

                return
            }

            await queryClient.invalidateQueries({
                queryKey: ['Fazenda']
            })

            navigate(backRoute, {
                replace: true
            })

        } catch (error) {

            console.error(error)

            setErro('Erro ao salvar fazenda')
        }
    }

    return (

        <PageLayout
            title={
                modoEdicao
                    ? 'Editar Fazenda'
                    : 'Nova Fazenda'
            }
            showBack
            backTo={backRoute}
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
                style={{ gap: 10 }}
            >

                {!modoEdicao && (

                    <FormInput
                        label="CNPJ"
                        value={cnpj}
                        onChange={handleCnpjChange}
                        onBlur={() =>
                            validarCnpjNoBackend(cnpj)
                        }
                        onKeyDown={async (e) => {

                            if (e.key === 'Enter') {

                                e.preventDefault()

                                await validarCnpjNoBackend(cnpj)
                            }
                        }}
                        placeholder="00.000.000/0000-00"
                        style={{
                            maxWidth: '220px'
                        }}
                    />
                )}

                {(cnpjVerificado || modoEdicao) && (
                    <>

                        <FormInput
                            label="Nome da fazenda"
                            value={nome}
                            onChange={e =>
                                setNome(e.target.value)
                            }
                            uppercase
                            required
                        />

                        <FormInput
                            label="Endereço"
                            value={endereco}
                            onChange={e =>
                                setEndereco(e.target.value)
                            }
                            uppercase
                        />

                        <div
                            className="form-row"
                            style={{ gap: 8 }}
                        >

                            <FormInput
                                label="Cidade"
                                value={cidade}
                                onChange={e =>
                                    setCidade(e.target.value)
                                }
                                className="cidade"
                                uppercase
                            />

                            <FormInput
                                label="Estado"
                                value={estado}
                                onChange={e =>
                                    setEstado(e.target.value)
                                }
                                maxLength={2}
                                placeholder="UF"
                                className="estado"
                                uppercase
                            />

                        </div>

                        {!modoEdicao && (

                            <div
                                style={{
                                    marginTop: 10,
                                    marginBottom: 10,
                                    padding: 10,
                                    border: '1px solid #ddd',
                                    borderRadius: 8
                                }}
                            >

                                <h4
                                    style={{
                                        marginBottom: 8
                                    }}
                                >
                                    🌱 Safra Inicial
                                </h4>

                                <FormInput
                                    label="Nome da Safra (Ex: 2024/2025)"
                                    value={nomeSafra}
                                    onChange={e =>
                                        setNomeSafra(
                                            e.target.value
                                        )
                                    }
                                />

                                <div
                                    className="form-row"
                                    style={{ gap: 8 }}
                                >

                                    <FormInput
                                        type="date"
                                        label="Data Início"
                                        value={dataInicio}
                                        onChange={e =>
                                            setDataInicio(
                                                e.target.value
                                            )
                                        }
                                    />

                                    <FormInput
                                        type="date"
                                        label="Data Fim"
                                        value={dataFim}
                                        onChange={e =>
                                            setDataFim(
                                                e.target.value
                                            )
                                        }
                                    />

                                </div>

                            </div>
                        )}

                        <div
                            className="form-actions"
                            style={{ marginTop: 6 }}
                        >

                            <button
                                type="submit"
                                className="add-btn"
                            >
                                {modoEdicao
                                    ? 'Salvar alterações'
                                    : 'Salvar fazenda'}
                            </button>

                        </div>

                    </>
                )}

            </form>

        </PageLayout>
    )
}