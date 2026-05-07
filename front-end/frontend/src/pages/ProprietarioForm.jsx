import { useState, useEffect } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import '../styles/pages.css'
import '../styles/form.css'
import Alert from '../components/Alert'
import PageLayout from '../components/PageLayout'
import FormInput from '../components/FormInput'
import {
    verificarCpf,
    criarProprietario,
    buscarProprietario,
    atualizarProprietario
} from '../api/api'

function formatarCpf(valor) {
    valor = valor.replace(/\D/g, '')
    valor = valor.replace(/^(\d{3})(\d)/, '$1.$2')
    valor = valor.replace(/^(\d{3})\.(\d{3})(\d)/, '$1.$2.$3')
    valor = valor.replace(/\.(\d{3})(\d)/, '.$1-$2')
    return valor.slice(0, 14)
}

function limparCpf(valor) {
    return valor.replace(/\D/g, '')
}

export default function ProprietarioForm() {
    const navigate = useNavigate()
    const { id } = useParams()
    const modoEdicao = !!id

    const [cpf, setCpf] = useState('')
    const [nome, setNome] = useState('')
    const [email, setEmail] = useState('')
    const [celular, setCelular] = useState('')
    const [endereco, setEndereco] = useState('')
    const [bairro, setBairro] = useState('')
    const [cidade, setCidade] = useState('')
    const [estado, setEstado] = useState('')

    const [cpfVerificado, setCpfVerificado] = useState(modoEdicao)
    const [erro, setErro] = useState('')
    const [sucesso, setSucesso] = useState('')

    useEffect(() => {
        async function carregarProprietario() {
            try {
                const response = await buscarProprietario(id)
                if (!response.ok || !response.data?.data) {
                    setErro('Erro ao carregar proprietário')
                    return
                }

                const p = response.data.data
                setCpf(formatarCpf(p.cpf || ''))
                setNome(p.nome || '')
                setEmail(p.email || '')
                setCelular(p.celular || '')
                setEndereco(p.endereco || '')
                setBairro(p.bairro || '')
                setCidade(p.cidade || '')
                setEstado(p.estado || '')
            } catch (err) {
                console.error(err)
                setErro('Erro ao carregar proprietário')
            }
        }

        if (modoEdicao) carregarProprietario()
    }, [id, modoEdicao])
    function handleCpfChange(e) {
        const valor = formatarCpf(e.target.value)
        setCpf(valor)
        setCpfVerificado(false)
        setErro('')
        setSucesso('')
    }

    async function validarCpfNoBackend(valorCpf) {
        setErro('')
        setSucesso('')
        const cpfLimpo = limparCpf(valorCpf)

        if (cpfLimpo.length !== 11) {
            setErro('Informe um CPF válido')
            setCpfVerificado(false)
            return false
        }

        try {
            const response = await verificarCpf(cpfLimpo)

            if (!response.data?.success) {
                setErro(response.data?.message || 'Erro ao verificar CPF')
                setCpfVerificado(false)
                return false
            }

            if (response.data.data === false) {
                setErro('CPF já cadastrado')
                setCpfVerificado(false)
                return false
            }

            setCpfVerificado(true)
            return true
        } catch (error) {
            console.error(error)
            setErro('Erro ao verificar CPF')
            setCpfVerificado(false)
            return false
        }
    }

    async function handleSubmit(e) {
        e.preventDefault()
        setErro('')
        setSucesso('')

        const cpfLimpo = limparCpf(cpf)

        if (!modoEdicao) {
            const valido = await validarCpfNoBackend(cpf)
            if (!valido) return
        }

        if (!nome) {
            setErro('Informe o nome do proprietário')
            return
        }

        if (!email) {
            setErro('Informe o e-mail')
            return
        }

        try {
            let response

            if (modoEdicao) {
                response = await atualizarProprietario(id, {
                    cpf: cpfLimpo,
                    nome,
                    email,
                    celular,
                    endereco,
                    bairro,
                    cidade,
                    estado
                })
            } else {
                response = await criarProprietario({
                    cpf: cpfLimpo,
                    nome,
                    email,
                    celular,
                    endereco,
                    bairro,
                    cidade,
                    estado
                })
            }

            if (!response.ok || !response.data?.success) {
                setErro(response.data?.message || 'Erro ao salvar proprietário')
                return
            }

            setSucesso(
                modoEdicao
                    ? 'Proprietário atualizado com sucesso'
                    : 'Proprietário cadastrado com sucesso'
            )

            setTimeout(() => navigate('/proprietarios'), 1200)
        } catch (error) {
            console.error(error)
            setErro('Erro ao salvar proprietário')
        }
    }

    return (
        <PageLayout
            title={modoEdicao ? 'Editar Proprietário' : 'Novo Proprietário'}
            showBack
            backTo="/proprietarios"
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
                        style={{ maxWidth: '200px' }}
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

                        {/* EMAIL 3 / CELULAR 1 */}
                        <div className="form-grid-4">
                            <div style={{ gridColumn: 'span 3' }}>
                                <FormInput
                                    label="E-mail"
                                    type="email"
                                    value={email}
                                    onChange={e => setEmail(e.target.value)}
                                    required
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

                        {/* BAIRRO 2 / CIDADE 2 / ESTADO 1 */}
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

                        <div className="form-actions">
                            <button type="submit" className="add-btn">
                                {modoEdicao ? 'Salvar alterações' : 'Salvar proprietário'}
                            </button>
                        </div>
                    </>
                )}
            </form>
        </PageLayout>
    )
}