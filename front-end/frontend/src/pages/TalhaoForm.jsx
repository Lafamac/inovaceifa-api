import { useState, useEffect } from 'react'
import { useNavigate, useParams } from 'react-router-dom'

import '../styles/pages.css'
import '../styles/form.css'

import Alert from '../components/Alert'
import PageLayout from '../components/PageLayout'
import FormInput from '../components/FormInput'

import {
    listarResFerrugem,
    listarSistemasCultivo,
    criarTalhao,
    buscarTalhao,
    atualizarTalhao
} from '../api/api'

export default function TalhaoForm() {

    const navigate = useNavigate()

    const { id } = useParams()

    const modoEdicao = !!id

    const [nome, setNome] = useState('')

    const [resistenciaId, setResistenciaId] =
        useState('')

    const [sistemaId, setSistemaId] =
        useState('')

    const [area, setArea] = useState('')

    const [espRua, setEspRua] = useState('')

    const [espPlanta, setEspPlanta] =
        useState('')

    const [material, setMaterial] =
        useState('')

    const [resFerrugem, setResFerrugem] =
        useState([])

    const [sistemas, setSistemas] =
        useState([])

    const [erro, setErro] = useState('')

    const [sucesso, setSucesso] =
        useState('')

    useEffect(() => {

        async function carregarReferencias() {

            try {

                const ferrugemRes =
                    await listarResFerrugem()

                const sistemasRes =
                    await listarSistemasCultivo()

                if (ferrugemRes.ok) {

                    setResFerrugem(
                        ferrugemRes.data?.data || []
                    )
                }

                if (sistemasRes.ok) {

                    setSistemas(
                        sistemasRes.data?.data || []
                    )
                }

            } catch (err) {

                console.error(err)

                setErro(
                    'Erro ao carregar tabelas de referência'
                )
            }
        }

        async function carregarTalhao() {

            try {

                const response =
                    await buscarTalhao(id)

                if (
                    !response.ok ||
                    !response.data?.data
                ) {

                    setErro(
                        'Erro ao carregar talhão'
                    )

                    return
                }

                const t = response.data.data

                setNome(t.nome || '')

                setResistenciaId(
                    t.resistenciaFerrugemId || ''
                )

                setSistemaId(
                    t.sistemaCultivoId || ''
                )

                setArea(t.area ?? '')

                setEspRua(
                    t.espacamentoRua ?? ''
                )

                setEspPlanta(
                    t.espacamentoPlanta ?? ''
                )

                setMaterial(t.material || '')

            } catch (err) {

                console.error(err)

                setErro(
                    'Erro ao carregar talhão'
                )
            }
        }

        carregarReferencias()

        if (modoEdicao) {
            carregarTalhao()
        }

    }, [id, modoEdicao])

    async function handleSubmit(e) {

        e.preventDefault()

        setErro('')

        setSucesso('')

        // 🔥 NOME
        if (!nome) {

            setErro(
                'Informe o nome do talhão'
            )

            return
        }

        // 🔥 CAMPOS OBRIGATÓRIOS
        if (
            area === '' ||
            espRua === '' ||
            espPlanta === ''
        ) {

            setErro(
                'Informe Área, Esp. Rua e Esp. Planta'
            )

            return
        }

        // 🔥 SELECTS OBRIGATÓRIOS
        if (
            !resistenciaId ||
            !sistemaId
        ) {

            setErro(
                'Preencha os campos obrigatórios'
            )

            return
        }

        // 🔥 PAYLOAD FINAL
        const payload = {

            nome: nome?.trim(),

            area:
                area && area !== ''
                    ? Number(area)
                    : 0,

            espacamentoRua:
                espRua && espRua !== ''
                    ? Number(espRua)
                    : 0,

            espacamentoPlanta:
                espPlanta && espPlanta !== ''
                    ? Number(espPlanta)
                    : 0,

            material:
                material?.trim() || '',

            resistenciaFerrugemId:
                resistenciaId
                    ? Number(resistenciaId)
                    : null,

            sistemaCultivoId:
                sistemaId
                    ? Number(sistemaId)
                    : null
        }

        console.log(
            'PAYLOAD TALHÃO:',
            JSON.stringify(
                payload,
                null,
                2
            )
        )

        try {

            const response = modoEdicao

                ? await atualizarTalhao(
                    id,
                    payload
                )

                : await criarTalhao(
                    payload
                )

            if (
                !response.ok ||
                !response.data?.success
            ) {

                setErro(

                    response.data?.message ||

                    JSON.stringify(
                        response.data,
                        null,
                        2
                    ) ||

                    'Erro ao salvar talhão'
                )

                return
            }

            setSucesso(

                modoEdicao

                    ? 'Talhão atualizado com sucesso'

                    : 'Talhão cadastrado com sucesso'
            )

            setTimeout(() => {

                navigate('/talhoes')

            }, 1200)

        } catch (err) {

            console.error(err)

            console.log(
                'ERRO BACKEND:',
                err?.response?.data
            )

            setErro(

                JSON.stringify(
                    err?.response?.data,
                    null,
                    2
                ) ||

                'Erro ao salvar talhão'
            )
        }
    }

    return (

        <PageLayout
            title={
                modoEdicao
                    ? 'Editar Talhão'
                    : 'Novo Talhão'
            }
            showBack
            backTo="/talhoes"
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

                <FormInput
                    label="Nome"
                    value={nome}
                    onChange={e =>
                        setNome(
                            e.target.value
                        )
                    }
                    required
                />

                <div className="form-grid-3">

                    <div>

                        <label>
                            Sistema Cultivo *
                        </label>

                        <select
                            value={sistemaId}
                            onChange={e =>
                                setSistemaId(
                                    e.target.value
                                )
                            }
                        >

                            <option value="">
                                Selecione
                            </option>

                            {sistemas.map(s => (

                                <option
                                    key={s.id}
                                    value={s.id}
                                >
                                    {s.descricao}
                                </option>

                            ))}

                        </select>

                    </div>

                    <div>

                        <label>
                            Resistência Ferrugem *
                        </label>

                        <select
                            value={resistenciaId}
                            onChange={e =>
                                setResistenciaId(
                                    e.target.value
                                )
                            }
                        >

                            <option value="">
                                Selecione
                            </option>

                            {resFerrugem.map(r => (

                                <option
                                    key={r.id}
                                    value={r.id}
                                >
                                    {r.descricao}
                                </option>

                            ))}

                        </select>

                    </div>

                </div>

                <FormInput
                    label="Material"
                    value={material}
                    onChange={e =>
                        setMaterial(
                            e.target.value
                        )
                    }
                />

                <div className="form-grid-3">

                    <FormInput
                        label="Área (ha)"
                        type="number"
                        value={area}
                        onChange={e =>
                            setArea(
                                e.target.value
                            )
                        }
                        required
                    />

                    <FormInput
                        label="Esp. Rua"
                        type="number"
                        value={espRua}
                        onChange={e =>
                            setEspRua(
                                e.target.value
                            )
                        }
                        required
                    />

                    <FormInput
                        label="Esp. Planta"
                        type="number"
                        value={espPlanta}
                        onChange={e =>
                            setEspPlanta(
                                e.target.value
                            )
                        }
                        required
                    />

                </div>

                <div className="form-actions">

                    <button
                        type="submit"
                        className="add-btn"
                    >

                        {modoEdicao

                            ? 'Salvar alterações'

                            : 'Salvar talhão'}

                    </button>

                </div>

            </form>

        </PageLayout>
    )
}