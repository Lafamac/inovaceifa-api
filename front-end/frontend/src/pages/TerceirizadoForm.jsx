import { useState, useEffect } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { useQueryClient } from '@tanstack/react-query' // 🔥 ADICIONADO
import '../styles/pages.css'
import '../styles/form.css'
import Alert from '../components/Alert'
import PageLayout from '../components/PageLayout'
import FormInput from '../components/FormInput'
import {
    verificarCpf,
    criarTerceirizado,
    buscarTerceirizadoPorId,
    atualizarTerceirizado
} from '../api/api'

export default function TerceirizadoForm() {
    const navigate = useNavigate()
    const { id } = useParams()
    const modoEdicao = !!id
    const queryClient = useQueryClient() // 🔥 ADICIONADO

    const [cpf, setCpf] = useState('')
    const [nome, setNome] = useState('')
    const [email, setEmail] = useState('')
    const [celular, setCelular] = useState('')
    const [endereco, setEndereco] = useState('')
    const [bairro, setBairro] = useState('')
    const [cidade, setCidade] = useState('')
    const [estado, setEstado] = useState('')
    const [cargo, setCargo] = useState('')
    const [salario, setSalario] = useState('')

    const [cpfVerificado, setCpfVerificado] = useState(modoEdicao)
    const [erro, setErro] = useState('')
    const [sucesso, setSucesso] = useState('')

    function formatarCpf(valor) {
        valor = valor.replace(/\D/g, '')
        valor = valor.replace(/^(\d{3})(\d)/, '$1.$2')
        valor = valor.replace(/^(\d{3})\.(\d{3})(\d)/, '$1.$2.$3')
        valor = valor.replace(/\.(\d{3})(\d)/, '.$1-$2')
        return valor.slice(0, 14)
    }

    useEffect(() => {
        async function carregar() {
            try {
                const response = await buscarTerceirizadoPorId(id)
                if (!response.ok || !response.data?.data) {
                    setErro('Erro ao carregar terceirizado')
                    return
                }

                const t = response.data.data

                setCpf(formatarCpf(t.cpf || ''))
                setNome(t.nome || '')
                setEmail(t.email || '')
                setCelular(t.celular || '')
                setEndereco(t.endereco || '')
                setBairro(t.bairro || '')
                setCidade(t.cidade || '')
                setEstado(t.estado || '')
                setCargo(t.cargo || '')
                setSalario(t.salario ?? '')
            } catch (err) {
                console.error(err)
                setErro('Erro ao carregar terceirizado')
            }
        }

        if (modoEdicao) carregar()
    }, [id, modoEdicao])

    function limparCpf(valor) {
        return valor.replace(/\D/g, '')
    }

    function handleCpfChange(e) {
        const valor = formatarCpf(e.target.value)
        setCpf(valor)
        setCpfVerificado(false)
        setErro('')
        setSucesso('')
    }

    async function validarCpfNoBackend(valorCpf) {
        setErro('')
        const cpfLimpo = limparCpf(valorCpf)

        if (cpfLimpo.length !== 11) {
            setErro('Informe um CPF válido')
            setCpfVerificado(false)
            return false
        }

        const response = await verificarCpf(cpfLimpo)

        if (!response.data?.success) {
            setErro(response.data?.message || 'Erro ao verificar CPF')
            setCpfVerificado(false)
            return false
        }

        const disponivel = response.data.data

        if (disponivel === false && !modoEdicao) {
            setErro('CPF já cadastrado')
            setCpfVerificado(false)
            return false
        }

        setCpfVerificado(true)
        return true
    }

    async function handleSubmit(e) {
        e.preventDefault()

        const cpfLimpo = limparCpf(cpf)

        if (!modoEdicao) {
            const valido = await validarCpfNoBackend(cpf)
            if (!valido) return
        }

        if (!nome) {
            setErro('Informe o nome do terceirizado')
            return
        }

        const payload = {
            cpf: cpfLimpo,
            nome,
            email,
            celular,
            endereco,
            bairro,
            cidade,
            estado,
            cargo,
            salario: salario ? Number(salario) : null
        }

        const response = modoEdicao
            ? await atualizarTerceirizado(id, payload)
            : await criarTerceirizado(payload)

        if (!response.ok || !response.data?.success) {
            setErro(response.data?.message || 'Erro ao salvar terceirizado')
            return
        }

        setSucesso(
            modoEdicao
                ? 'Terceirizado atualizado com sucesso'
                : 'Terceirizado cadastrado com sucesso'
        )

        setTimeout(() => {
            queryClient.invalidateQueries(['terceirizados']) // 🔥 ADICIONADO
            navigate('/terceirizados')
        }, 1200)
    }

    return (
        <PageLayout
            title={modoEdicao ? 'Editar Terceirizado' : 'Novo Terceirizado'}
            showBack
            backTo="/terceirizados"
        >
            {erro && <Alert type="error" message={erro} />}
            {sucesso && <Alert type="success" message={sucesso} />}

            <form className="form-container" onSubmit={handleSubmit}>

                {!modoEdicao && (
                    <FormInput
                        label="CPF"
                        value={cpf}
                        onChange={handleCpfChange}
                        onBlur={() => validarCpfNoBackend(cpf)}
                        onKeyDown={async (e) => {
                            if (e.key === 'Enter') {
                                e.preventDefault()
                                await validarCpfNoBackend(cpf)
                            }
                        }}
                        placeholder="000.000.000-00"
                        style={{ maxWidth: '220px' }}
                    />
                )}

                {(cpfVerificado || modoEdicao) && (
                    <>
                        <FormInput
                            label="Nome"
                            value={nome}
                            onChange={e => setNome(e.target.value)}
                            uppercase
                            required
                        />

                        <div className="form-grid-4">
                            <div style={{ gridColumn: 'span 3' }}>
                                <FormInput
                                    label="E-mail"
                                    type="email"
                                    value={email}
                                    onChange={e => setEmail(e.target.value)}
                                />
                            </div>
                            <div style={{ gridColumn: 'span 1' }}>
                                <FormInput
                                    label="Celular"
                                    value={celular}
                                    onChange={e => setCelular(e.target.value)}
                                />
                            </div>
                        </div>

                        <FormInput
                            label="Endereço"
                            value={endereco}
                            onChange={e => setEndereco(e.target.value)}
                            uppercase
                        />

                        <div className="form-grid-5">
                            <div style={{ gridColumn: 'span 2' }}>
                                <FormInput
                                    label="Bairro"
                                    value={bairro}
                                    onChange={e => setBairro(e.target.value)}
                                    uppercase
                                />
                            </div>
                            <div style={{ gridColumn: 'span 2' }}>
                                <FormInput
                                    label="Cidade"
                                    value={cidade}
                                    onChange={e => setCidade(e.target.value)}
                                    uppercase
                                />
                            </div>
                            <div style={{ gridColumn: 'span 1' }}>
                                <FormInput
                                    label="Estado"
                                    value={estado}
                                    onChange={e => setEstado(e.target.value)}
                                    maxLength={2}
                                    placeholder="UF"
                                    uppercase
                                />
                            </div>
                        </div>

                        <div className="form-grid-4">
                            <div style={{ gridColumn: 'span 2' }}>
                                <FormInput
                                    label="Cargo"
                                    value={cargo}
                                    onChange={e => setCargo(e.target.value)}
                                    uppercase
                                />
                            </div>
                            <div style={{ gridColumn: 'span 1' }}>
                                <FormInput
                                    label="Salário"
                                    type="number"
                                    value={salario}
                                    onChange={e => setSalario(e.target.value)}
                                />
                            </div>
                        </div>

                        <div className="form-actions">
                            <button type="submit" className="add-btn">
                                {modoEdicao ? 'Salvar alterações' : 'Salvar terceirizado'}
                            </button>
                        </div>
                    </>
                )}
            </form>
        </PageLayout>
    )
}