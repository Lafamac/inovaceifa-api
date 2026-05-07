import { useState, useEffect } from 'react'
import { useNavigate, useParams } from 'react-router-dom'

import '../styles/pages.css'
import '../styles/form.css'

import PageLayout from '../components/PageLayout'
import Alert from '../components/Alert'
import FormInput from '../components/FormInput'
import SelectReferencia from '../components/SelectReferencia'

import {
    criarMaquina,
    atualizarMaquina,
    buscarMaquina,
    listarTiposMaquina
} from '../api/api'

export default function MaquinaForm() {

    const navigate = useNavigate()

    const { id } = useParams()

    const modoEdicao = !!id

    const [nome, setNome] = useState('')
    const [marca, setMarca] = useState('')
    const [modelo, setModelo] = useState('')
    const [descricao, setDescricao] = useState('')

    const [anoFabricacao, setAnoFabricacao] =
        useState('')

    const [horimetro, setHorimetro] =
        useState('')

    const [tipoMaquinaId, setTipoMaquinaId] =
        useState('')

    const [tipoPosseId, setTipoPosseId] =
        useState('')

    const [valorDiaria, setValorDiaria] =
        useState('')

    const [inicioLocacao, setInicioLocacao] =
        useState('')

    const [fimLocacao, setFimLocacao] =
        useState('')

    const [diasContratados, setDiasContratados] =
        useState('')

    const [valorTotalLocacao, setValorTotalLocacao] =
        useState('')

    const [imagem, setImagem] =
        useState('')

    const [tipos, setTipos] = useState([])

    const [erro, setErro] = useState('')

    const [sucesso, setSucesso] =
        useState('')

    useEffect(() => {

        async function carregarTipos() {

            const response =
                await listarTiposMaquina()

            if (
                response.ok &&
                response.data?.success
            ) {

                setTipos(
                    response.data.data
                )

            } else {

                setErro(
                    'Erro ao carregar tipos de máquina'
                )
            }
        }

        async function carregarMaquina() {

            const response =
                await buscarMaquina(id)

            if (
                !response.ok ||
                !response.data?.data
            ) {

                setErro(
                    'Erro ao carregar máquina'
                )

                return
            }

            const m = response.data.data

            setNome(m.nome || '')
            setMarca(m.marca || '')
            setModelo(m.modelo || '')
            setDescricao(m.descricao || '')

            setAnoFabricacao(
                m.anoFabricacao || ''
            )

            setHorimetro(
                m.horimetro || 0
            )

            setTipoMaquinaId(
                m.tipoMaquina?.id || ''
            )

            setTipoPosseId(
                m.tipoPosseId || ''
            )

            setValorDiaria(
                m.valorDiaria || ''
            )

            setInicioLocacao(
                m.inicioLocacao || ''
            )

            setFimLocacao(
                m.fimLocacao || ''
            )

            setDiasContratados(
                m.diasContratados || ''
            )

            setValorTotalLocacao(
                m.valorTotalLocacao || ''
            )

            setImagem(
                m.imagem || ''
            )
        }

        carregarTipos()

        if (modoEdicao) {
            carregarMaquina()
        }

    }, [id, modoEdicao])

    async function handleSubmit(e) {

        e.preventDefault()

        setErro('')
        setSucesso('')

        if (!nome) {

            setErro(
                'Informe o nome da máquina'
            )

            return
        }

        if (!tipoMaquinaId) {

            setErro(
                'Selecione o tipo da máquina'
            )

            return
        }

        const payload = {

            nome: nome?.trim(),

            marca: marca?.trim() || '',

            modelo: modelo?.trim() || '',

            descricao:
                descricao?.trim() || '',

            anoFabricacao:
                anoFabricacao
                    ? Number(anoFabricacao)
                    : null,

            imagem:
                imagem?.trim() || '',

            horimetro:
                horimetro
                    ? Number(horimetro)
                    : 0,

            tipoMaquinaId:
                Number(tipoMaquinaId),

            tipoPosseId:
                tipoPosseId
                    ? Number(tipoPosseId)
                    : 1,

            valorDiaria:
                valorDiaria
                    ? Number(valorDiaria)
                    : 0,

            inicioLocacao:
                inicioLocacao || null,

            fimLocacao:
                fimLocacao || null,

            diasContratados:
                diasContratados
                    ? Number(diasContratados)
                    : 0,

            valorTotalLocacao:
                valorTotalLocacao
                    ? Number(valorTotalLocacao)
                    : 0,

            ativo: true
        }

        try {

            const response = modoEdicao

                ? await atualizarMaquina(
                    id,
                    payload
                )

                : await criarMaquina(
                    payload
                )

            if (
                !response.ok ||
                !response.data?.success
            ) {

                setErro(
                    response.data?.message ||
                    'Erro ao salvar máquina'
                )

                return
            }

            setSucesso(

                modoEdicao

                    ? 'Máquina atualizada com sucesso'

                    : 'Máquina cadastrada com sucesso'
            )

            setTimeout(() => {

                navigate('/maquinas')

            }, 1200)

        } catch (err) {

            console.error(err)

            setErro(
                err?.response?.data?.message ||
                'Erro ao salvar máquina'
            )
        }
    }

    return (

        <PageLayout
            title={
                modoEdicao
                    ? 'Editar Máquina'
                    : 'Nova Máquina'
            }
            showBack
            backTo="/maquinas"
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
                style={{
                    gap: 8
                }}
            >

                <FormInput
                    label="Nome"
                    value={nome}
                    onChange={e =>
                        setNome(e.target.value)
                    }
                    uppercase
                    required
                />

                <div
                    className="form-grid-4"
                    style={{ gap: 8 }}
                >

                    <div style={{ gridColumn: 'span 2' }}>

                        <label className="form-label">
                            Tipo de Máquina
                        </label>

                        <SelectReferencia
                            tipo="tipo-maquina"
                            value={tipoMaquinaId}
                            onChange={setTipoMaquinaId}
                        />

                    </div>

                    <div style={{ gridColumn: 'span 2' }}>

                        <FormInput
                            label="Ano Fabricação"
                            type="number"
                            value={anoFabricacao}
                            onChange={e =>
                                setAnoFabricacao(
                                    e.target.value
                                )
                            }
                        />

                    </div>

                </div>

                <div
                    className="form-grid-4"
                    style={{ gap: 8 }}
                >

                    <div style={{ gridColumn: 'span 2' }}>

                        <FormInput
                            label="Marca"
                            value={marca}
                            onChange={e =>
                                setMarca(
                                    e.target.value
                                )
                            }
                            uppercase
                        />

                    </div>

                    <div style={{ gridColumn: 'span 2' }}>

                        <FormInput
                            label="Modelo"
                            value={modelo}
                            onChange={e =>
                                setModelo(
                                    e.target.value
                                )
                            }
                            uppercase
                        />

                    </div>

                </div>

                <FormInput
                    label="Descrição"
                    value={descricao}
                    onChange={e =>
                        setDescricao(
                            e.target.value
                        )
                    }
                    uppercase
                />

                <div
                    className="form-grid-4"
                    style={{ gap: 8 }}
                >

                    <FormInput
                        label="Horímetro"
                        type="number"
                        value={horimetro}
                        onChange={e =>
                            setHorimetro(
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
                        label="Dias Contratados"
                        type="number"
                        value={diasContratados}
                        onChange={e =>
                            setDiasContratados(
                                e.target.value
                            )
                        }
                    />

                    <FormInput
                        label="Valor Total"
                        type="number"
                        value={valorTotalLocacao}
                        onChange={e =>
                            setValorTotalLocacao(
                                e.target.value
                            )
                        }
                    />

                </div>

                {/* 🔥 DATAS MESMA LINHA */}
                <div
                    style={{
                        display: 'grid',
                        gridTemplateColumns: '1fr 1fr',
                        gap: 8,
                        alignItems: 'end'
                    }}
                >

                    <FormInput
                        label="Início Locação"
                        type="date"
                        value={inicioLocacao}
                        onChange={e =>
                            setInicioLocacao(
                                e.target.value
                            )
                        }
                    />

                    <FormInput
                        label="Fim Locação"
                        type="date"
                        value={fimLocacao}
                        onChange={e =>
                            setFimLocacao(
                                e.target.value
                            )
                        }
                    />

                </div>

                <FormInput
                    label="Imagem URL"
                    value={imagem}
                    onChange={e =>
                        setImagem(
                            e.target.value
                        )
                    }
                />

                <div
                    className="form-actions"
                    style={{
                        marginTop: 4
                    }}
                >

                    <button
                        type="submit"
                        className="add-btn"
                    >

                        {modoEdicao

                            ? 'Salvar alterações'

                            : 'Salvar máquina'}

                    </button>

                </div>

            </form>

        </PageLayout>
    )
}