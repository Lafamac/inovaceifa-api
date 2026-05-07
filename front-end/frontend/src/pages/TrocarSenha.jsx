import { useState } from 'react'
import PageLayout from '../components/PageLayout'
import FormInput from '../components/FormInput'
import Alert from '../components/Alert'
import '../styles/pages.css'
import '../styles/form.css'
import { apiFetch } from '../api/api'

export default function TrocarSenha() {
    const [senhaAtual, setSenhaAtual] = useState('')
    const [novaSenha, setNovaSenha] = useState('')
    const [confirmarSenha, setConfirmarSenha] = useState('')

    const [erro, setErro] = useState('')
    const [sucesso, setSucesso] = useState('')

    async function handleSubmit(e) {
        e.preventDefault()
        setErro('')
        setSucesso('')

        if (!senhaAtual || !novaSenha || !confirmarSenha) {
            setErro('Preencha todos os campos')
            return
        }

        // 🔧 validação de confirmação
        if (novaSenha !== confirmarSenha) {
            setErro('A confirmação da senha não confere')
            return
        }

        try {
            const response = await apiFetch('/auth/trocar-senha', {
                method: 'PUT',
                body: JSON.stringify({
                    senhaAtual,
                    novaSenha
                })
            })

            // 🔧 tratamento correto do retorno do backend
            if (!response.ok || !response.data?.success) {
                setErro(
                    response.data?.message ||
                    'Erro ao trocar senha'
                )
                return
            }

            setSucesso('Senha alterada. Faça login novamente.')

            setSenhaAtual('')
            setNovaSenha('')
            setConfirmarSenha('')

            setTimeout(() => {
                localStorage.removeItem('token')
                localStorage.removeItem('safraAtiva')
                window.location.href = '/login'
            }, 1500)

        } catch (error) {
            console.error(error)
            setErro('Erro ao conectar com o servidor')
        }
    }

    return (
        <PageLayout
            title="Trocar senha"
            showBack

        >
            {erro && <Alert type="error" message={erro} />}
            {sucesso && <Alert type="success" message={sucesso} />}

            <form className="form-container" onSubmit={handleSubmit}>
                <FormInput
                    label="Senha atual"
                    type="password"
                    value={senhaAtual}
                    onChange={e => setSenhaAtual(e.target.value)}
                    required
                />

                <FormInput
                    label="Nova senha"
                    type="password"
                    value={novaSenha}
                    onChange={e => setNovaSenha(e.target.value)}
                    required
                />

                <FormInput
                    label="Confirmar nova senha"
                    type="password"
                    value={confirmarSenha}
                    onChange={e => setConfirmarSenha(e.target.value)}
                    required
                />

                <div className="form-actions">
                    <button type="submit" className="add-btn">
                        Salvar nova senha
                    </button>
                </div>
            </form>
        </PageLayout>
    )
}
