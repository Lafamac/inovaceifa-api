import { getToken } from './auth'

const API_URL = import.meta.env.VITE_API_URL

export const apiFetch = async (url, options = {}) => {
    const token = getToken()

    const hasBody = options.body !== undefined

    const headers = {
        ...(hasBody ? { 'Content-Type': 'application/json' } : {}),
        ...(options.headers || {}),
        ...(token ? { Authorization: `Bearer ${token}` } : {})
    }

    try {
        const response = await fetch(`${API_URL}${url}`, {
            ...options,
            headers
        })

        // 🔥 NOVO: detectar PDF
        const contentType = response.headers.get('content-type')

        if (contentType && contentType.includes('application/pdf')) {
            const blob = await response.blob()
            return { ok: response.ok, data: blob }
        }

        // 🔽 continua igual
        const text = await response.text()

        let data = null
        try {
            data = text ? JSON.parse(text) : null
        } catch (e) {
            data = null
        }

        return { ok: response.ok, data }

    } catch (e) {
        return {
            ok: false,
            data: { success: false, message: 'Erro conexão' }
        }
    }
}

/* =========================================================
   AUTH
========================================================= */

export const login = (email, senha) =>
    apiFetch('/auth/login', {
        method: 'POST',
        body: JSON.stringify({ email, senha })
    })

export const getMe = () =>
    apiFetch('/auth/me')

/* =========================================================
   FAZENDAS
========================================================= */

export const listarFazendas = () =>
    apiFetch('/fazendas')

export const listarFazendasInativas = () =>
    apiFetch('/fazendas/inativas')

export const ativarFazenda = (id) =>
    apiFetch(`/fazendas/${id}/ativa`, {
        method: 'PUT'
    })

export const criarFazenda = (data) =>
    apiFetch('/fazendas', {
        method: 'POST',
        body: JSON.stringify(data)
    })

export const atualizarFazenda = (id, data) =>
    apiFetch(`/fazendas/${id}`, {
        method: 'PUT',
        body: JSON.stringify(data)
    })

export const buscarFazenda = (id) =>
    apiFetch(`/fazendas/${id}`)

export const excluirFazenda = (id) =>
    apiFetch(`/fazendas/${id}`, {
        method: 'DELETE'
    })

export const reativarFazenda = (id) =>
    apiFetch(`/fazendas/${id}/reativar`, {
        method: 'PUT'
    })

/* =========================================================
   SAFRAS
========================================================= */

export const listarSafras = () =>
    apiFetch('/safras')

export const ativarSafra = (id) =>
    apiFetch(`/contexto/safra-ativa/${id}`, {
        method: 'POST'
    })

/* =========================================================
   PROPRIETÁRIOS
========================================================= */

export const listarProprietarios = () =>
    apiFetch('/proprietarios')

export const buscarProprietario = (id) =>
    apiFetch(`/proprietarios/${id}`)

export const criarProprietario = (data) =>
    apiFetch('/proprietarios', {
        method: 'POST',
        body: JSON.stringify(data)
    })

export const atualizarProprietario = (id, data) =>
    apiFetch(`/proprietarios/${id}`, {
        method: 'PUT',
        body: JSON.stringify(data)
    })

export const ativarProprietario = (id) =>
    apiFetch(`/admin/proprietarios/${id}/ativar`, {
        method: 'PUT'
    })

export const desativarProprietario = (id) =>
    apiFetch(`/admin/proprietarios/${id}/desativar`, {
        method: 'PUT'
    })

export const ativarProprietarioContexto = (id) =>
    apiFetch(`/contexto/proprietario-ativo/${id}`, {
        method: 'POST'
    })

/* =========================================================
   VALIDAÇÕES
========================================================= */

export const verificarCnpj = (cnpj) =>
    apiFetch(`/validation/verificar-cnpj/${cnpj}`)

export const verificarCpf = (cpf) =>
    apiFetch(`/validation/verificar-cpf/${cpf}`)

/* =========================================================
   TALHÕES
========================================================= */

export const listarTalhoes = () =>
    apiFetch('/talhoes')

export const listarTalhoesInativos = () =>
    apiFetch('/talhoes/inativos')

export const buscarTalhao = (id) =>
    apiFetch(`/talhoes/${id}`)

export const criarTalhao = (data) =>
    apiFetch('/talhoes', {
        method: 'POST',
        body: JSON.stringify(data)
    })

export const atualizarTalhao = (id, data) =>
    apiFetch(`/talhoes/${id}`, {
        method: 'PUT',
        body: JSON.stringify(data)
    })

export const excluirTalhao = (id) =>
    apiFetch(`/talhoes/${id}`, {
        method: 'DELETE'
    })

export const reativarTalhao = (id) =>
    apiFetch(`/talhoes/${id}/reativar`, {
        method: 'PUT'
    })

/* =========================================================
   REFERÊNCIAS TALHÃO
========================================================= */

export const listarCulturas = () =>
    apiFetch('/referencias/culturas')

export const listarResFerrugem = () =>
    apiFetch('/referencias/res-ferrugem')

export const listarSistemasCultivo = () =>
    apiFetch('/referencias/st-cultivo')

export const listarStCultivo = listarSistemasCultivo

export const listarFamilias = () =>
    apiFetch('/referencias/familia')

export const listarGrupos = () =>
    apiFetch('/referencias/grupo')

/* =========================================================
   MOVIMENTAÇÃO DE PRODUTOS
========================================================= */

export const listarMovProdutos = () =>
    apiFetch('/mov-produtos')

export const criarMovProduto = (data) =>
    apiFetch('/mov-produtos', {
        method: 'POST',
        body: JSON.stringify(data)
    })

export const atualizarMovProduto = (id, data) =>
    apiFetch(`/mov-produtos/${id}`, {
        method: 'PUT',
        body: JSON.stringify(data)
    })

export const excluirMovProduto = (id) =>
    apiFetch(`/mov-produtos/${id}`, {
        method: 'DELETE'
    })

/* =========================================================
   SAFRA TALHÕES
========================================================= */

export const listarSafraTalhoes = () =>
    apiFetch('/safra-talhoes')

export const listarSafraTalhoesInativos = () =>
    apiFetch('/safra-talhoes/inativos')

export const buscarSafraTalhao = (id) =>
    apiFetch(`/safra-talhoes/${id}`)

export const criarSafraTalhao = (data) =>
    apiFetch('/safra-talhoes', {
        method: 'POST',
        body: JSON.stringify(data)
    })

export const atualizarSafraTalhao = (id, data) =>
    apiFetch(`/safra-talhoes/${id}`, {
        method: 'PUT',
        body: JSON.stringify(data)
    })

export const excluirSafraTalhao = (id) =>
    apiFetch(`/safra-talhoes/${id}`, {
        method: 'DELETE'
    })

export const reativarSafraTalhao = (id) =>
    apiFetch(`/safra-talhoes/${id}/reativar`, {
        method: 'PUT'
    })

/* =========================================================
   PLANEJAMENTO OPERAÇÃO
========================================================= */

export const listarPlanejamentos = () =>
    apiFetch('/planejamento-operacao')

export const buscarPlanejamento = (id) =>
    apiFetch(`/planejamento-operacao/${id}`)

export const criarPlanejamento = (data) =>
    apiFetch('/planejamento-operacao', {
        method: 'POST',
        body: JSON.stringify(data)
    })

export const excluirPlanejamento = (id) =>
    apiFetch(`/planejamento-operacao/${id}`, {
        method: 'DELETE'
    })

export const obterResumoPlanejamento = (id) =>
    apiFetch(`/planejamento-operacao/${id}/resumo`)

export const obterComparativoPlanejamento = (id) =>
    apiFetch(`/planejamento-operacao/${id}/comparativo`)

export const obterComparativoDetalhadoPlanejamento = (id) =>
    apiFetch(`/planejamento-operacao/${id}/comparativo-detalhado`)

export const obterComparativoDetalhado = obterComparativoDetalhadoPlanejamento

/* =========================================================
   PLANEJAMENTO - INSUMOS
========================================================= */

export const listarInsumosPlanejamento = (id) =>
    apiFetch(`/planejamento-operacao/${id}/insumos`)

export const adicionarInsumoPlanejamento = (id, data) =>
    apiFetch(`/planejamento-operacao/${id}/insumos`, {
        method: 'POST',
        body: JSON.stringify(data)
    })

export const removerInsumoPlanejamento = (id, itemId) =>
    apiFetch(`/planejamento-operacao/${id}/insumos/${itemId}`, {
        method: 'DELETE'
    })

/* =========================================================
   PLANEJAMENTO - MÁQUINAS
========================================================= */

export const listarMaquinasPlanejamento = (id) =>
    apiFetch(`/planejamento-operacao/${id}/maquinas`)

export const adicionarMaquinaPlanejamento = (id, data) =>
    apiFetch(`/planejamento-operacao/${id}/maquinas`, {
        method: 'POST',
        body: JSON.stringify(data)
    })

export const removerMaquinaPlanejamento = (id, itemId) =>
    apiFetch(`/planejamento-operacao/${id}/maquinas/${itemId}`, {
        method: 'DELETE'
    })

/* =========================================================
   PLANEJAMENTO - FUNCIONÁRIOS
========================================================= */

export const listarFuncionariosPlanejamento = (id) =>
    apiFetch(`/planejamento-operacao/${id}/funcionarios`)

export const adicionarFuncionarioPlanejamento = (id, data) =>
    apiFetch(`/planejamento-operacao/${id}/funcionarios`, {
        method: 'POST',
        body: JSON.stringify(data)
    })

export const removerFuncionarioPlanejamento = (id, itemId) =>
    apiFetch(`/planejamento-operacao/${id}/funcionarios/${itemId}`, {
        method: 'DELETE'
    })

/* =========================================================
   TURMAS (MÃO DE OBRA)
========================================================= */

export const listarTurmas = () =>
    apiFetch('/turmas')

export const listarTurmasInativas = () =>
    apiFetch('/turmas/inativas')

export const buscarTurma = (id) =>
    apiFetch(`/turmas/${id}`)

export const criarTurma = (data) =>
    apiFetch('/turmas', {
        method: 'POST',
        body: JSON.stringify(data)
    })

export const atualizarTurma = (id, data) =>
    apiFetch(`/turmas/${id}`, {
        method: 'PUT',
        body: JSON.stringify(data)
    })

export const excluirTurma = (id) =>
    apiFetch(`/turmas/${id}`, {
        method: 'DELETE'
    })

export const reativarTurma = (id) =>
    apiFetch(`/turmas/${id}/reativar`, {
        method: 'PUT'
    })

/* =========================================================
   TERCEIRIZADOS
========================================================= */

export const listarTerceirizados = () =>
    apiFetch('/terceirizados')

export const listarTerceirizadosInativos = () =>
    apiFetch('/terceirizados/inativos')

export const buscarTerceirizadoPorId = (id) =>
    apiFetch(`/terceirizados/${id}`)

export const criarTerceirizado = (data) =>
    apiFetch('/terceirizados', {
        method: 'POST',
        body: JSON.stringify(data)
    })

export const atualizarTerceirizado = (id, data) =>
    apiFetch(`/terceirizados/${id}`, {
        method: 'PUT',
        body: JSON.stringify(data)
    })

export const excluirTerceirizado = (id) =>
    apiFetch(`/terceirizados/${id}`, {
        method: 'DELETE'
    })

export const reativarTerceirizado = (id) =>
    apiFetch(`/terceirizados/${id}/reativar`, {
        method: 'PUT'
    })

/* =========================================================
   FUNCIONÁRIOS
========================================================= */

export const listarFuncionarios = () =>
    apiFetch('/funcionarios')

export const listarFuncionariosInativos = () =>
    apiFetch('/funcionarios/inativos')

export const buscarFuncionario = (id) =>
    apiFetch(`/funcionarios/${id}`)

export const criarFuncionario = (data) =>
    apiFetch('/funcionarios', {
        method: 'POST',
        body: JSON.stringify(data)
    })

export const atualizarFuncionario = (id, data) =>
    apiFetch(`/funcionarios/${id}`, {
        method: 'PUT',
        body: JSON.stringify(data)
    })

export const excluirFuncionario = (id) =>
    apiFetch(`/funcionarios/${id}`, {
        method: 'DELETE'
    })

export const reativarFuncionario = (id) =>
    apiFetch(`/funcionarios/${id}/reativar`, {
        method: 'PUT'
    })

export const criarUsuarioFuncionario = (id) =>
    apiFetch(`/funcionarios/${id}/criar-usuario`, {
        method: 'POST'
    })

/* =========================================================
   MÁQUINAS
========================================================= */

export const listarMaquinas = () =>
    apiFetch('/maquinas')

export const listarMaquinasInativas = () =>
    apiFetch('/maquinas/inativas')

export const buscarMaquina = (id) =>
    apiFetch(`/maquinas/${id}`)

export const criarMaquina = (data) =>
    apiFetch('/maquinas', {
        method: 'POST',
        body: JSON.stringify(data)
    })

export const atualizarMaquina = (id, data) =>
    apiFetch(`/maquinas/${id}`, {
        method: 'PUT',
        body: JSON.stringify(data)
    })

export const excluirMaquina = (id) =>
    apiFetch(`/maquinas/${id}`, {
        method: 'DELETE'
    })

export const reativarMaquina = (id) =>
    apiFetch(`/maquinas/${id}/reativar`, {
        method: 'PUT'
    })

/* =========================================================
   GASTOS MÁQUINA
========================================================= */

export const listarGastosMaquina = () =>
    apiFetch('/gastos-maquina')

export const criarGastoMaquina = (data) =>
    apiFetch('/gastos-maquina', {
        method: 'POST',
        body: JSON.stringify(data)
    })

export const atualizarGastoMaquina = (id, data) =>
    apiFetch(`/gastos-maquina/${id}`, {
        method: 'PUT',
        body: JSON.stringify(data)
    })

export const excluirGastoMaquina = (id) =>
    apiFetch(`/gastos-maquina/${id}`, {
        method: 'DELETE'
    })

/* =========================================================
   HORAS MÁQUINA
========================================================= */

export const listarHorasMaquina = () =>
    apiFetch('/horas-maquina')

export const criarHoraMaquina = (data) =>
    apiFetch('/horas-maquina', {
        method: 'POST',
        body: JSON.stringify(data)
    })

export const atualizarHoraMaquina = (id, data) =>
    apiFetch(`/horas-maquina/${id}`, {
        method: 'PUT',
        body: JSON.stringify(data)
    })

export const excluirHoraMaquina = (id) =>
    apiFetch(`/horas-maquina/${id}`, {
        method: 'DELETE'
    })

/* =========================================================
   OPERAÇÕES TALHÃO
========================================================= */

export const listarOperacoesTalhao = () =>
    apiFetch('/operacoes-talhao')

export const buscarOperacaoTalhao = (id) =>
    apiFetch(`/operacoes-talhao/${id}`)

export const criarOperacaoTalhao = (data) =>
    apiFetch('/operacoes-talhao', {
        method: 'POST',
        body: JSON.stringify(data)
    })

export const atualizarOperacaoTalhao = (id, data) =>
    apiFetch(`/operacoes-talhao/${id}`, {
        method: 'PUT',
        body: JSON.stringify(data)
    })

export const excluirOperacaoTalhao = (id) =>
    apiFetch(`/operacoes-talhao/${id}`, {
        method: 'DELETE'
    })

/* =========================================================
   OPERAÇÃO - PRODUTOS
========================================================= */

export const listarOperacaoProdutos = (operacaoId) =>
    apiFetch(`/operacao-produtos?operacaoId=${operacaoId}`)

export const removerOperacaoProduto = (id) =>
    apiFetch(`/operacao-produtos/${id}`, {
        method: 'DELETE'
    })

/* =========================================================
   REFERÊNCIAS MÁQUINA
========================================================= */

export const listarTiposMaquina = () =>
    apiFetch('/referencias/tipo-maquina')

export const listarTiposGastoMaquina = () =>
    apiFetch('/referencias/tipo-gasto-maquina')

/* =========================================================
   PRODUTOS
========================================================= */

export const listarProdutos = () =>
    apiFetch('/produtos')

export const listarProdutosInativos = () =>
    apiFetch('/produtos/inativos')

export const buscarProduto = (id) =>
    apiFetch(`/produtos/${id}`)

export const criarProduto = (data) =>
    apiFetch('/produtos', {
        method: 'POST',
        body: JSON.stringify(data)
    })

export const atualizarProduto = (id, data) =>
    apiFetch(`/produtos/${id}`, {
        method: 'PUT',
        body: JSON.stringify(data)
    })

export const excluirProduto = (id) =>
    apiFetch(`/produtos/${id}`, {
        method: 'DELETE'
    })

export const reativarProduto = (id) =>
    apiFetch(`/produtos/${id}/reativar`, {
        method: 'PUT'
    })

/* =========================================================
   ADUBAÇÃO (VISÃO SAFRA)
========================================================= */

export const obterAdubacaoSafra = (safraId) =>
    apiFetch(`/planejamento/adubacao?safraId=${safraId}`)

/* =========================================================
   ORDEM DE SERVIÇO
========================================================= */

export const listarOrdensServico = () =>
    apiFetch('/ordens-servico')

export const buscarOrdemServico = (id) =>
    apiFetch(`/ordens-servico/${id}`)
export const obterOrdemServico = buscarOrdemServico

export const criarOrdemServico = (data) =>
    apiFetch('/ordens-servico', {
        method: 'POST',
        body: JSON.stringify(data)
    })

export const atualizarOrdemServico = (id, data) =>
    apiFetch(`/ordens-servico/${id}`, {
        method: 'PUT',
        body: JSON.stringify(data)
    })

export const excluirOrdemServico = (id) =>
    apiFetch(`/ordens-servico/${id}`, {
        method: 'DELETE'
    })

export const gerarOrdemServicoPorPlanejamento = (data) =>
    apiFetch('/ordens-servico/from-planejamento', {
        method: 'POST',
        body: JSON.stringify(data)
    })
export const criarOsFromPlanejamento = gerarOrdemServicoPorPlanejamento

export const finalizarOrdemServico = (id) =>
    apiFetch(`/ordens-servico/${id}/finalizar`, {
        method: 'POST'
    })

/* =========================================================
   AUDITORIA OS
========================================================= */

export const listarAuditoriaOS = (ordemId) =>
    apiFetch(`/ordens-servico/${ordemId}/auditoria`)

export const detalharAuditoriaOS = (ordemId) =>
    apiFetch(`/ordens-servico/${ordemId}/auditoria/detalhe`)

export const listarAuditoriaOs = listarAuditoriaOS
export const listarAuditoriaDetalheOs = detalharAuditoriaOS

/* =========================================================
   OS - FUNCIONÁRIOS
========================================================= */

export const listarFuncionariosOs = (id) =>
    apiFetch(`/ordens-servico/${id}/funcionarios`)

export const adicionarFuncionarioOs = (id, data) =>
    apiFetch(`/ordens-servico/${id}/funcionarios`, {
        method: 'POST',
        body: JSON.stringify(data)
    })

export const removerFuncionarioOs = (id, itemId) =>
    apiFetch(`/ordens-servico/${id}/funcionarios/${itemId}`, {
        method: 'DELETE'
    })

/* =========================================================
   OS - INSUMOS
========================================================= */

export const listarInsumosOs = (id) =>
    apiFetch(`/ordens-servico/${id}/insumos`)

export const adicionarInsumoOs = (id, data) =>
    apiFetch(`/ordens-servico/${id}/insumos`, {
        method: 'POST',
        body: JSON.stringify(data)
    })

export const removerInsumoOs = (id, itemId) =>
    apiFetch(`/ordens-servico/${id}/insumos/${itemId}`, {
        method: 'DELETE'
    })

/* =========================================================
   OS - MÁQUINAS
========================================================= */

export const listarMaquinasOs = (id) =>
    apiFetch(`/ordens-servico/${id}/maquinas`)

export const adicionarMaquinaOs = (id, data) =>
    apiFetch(`/ordens-servico/${id}/maquinas`, {
        method: 'POST',
        body: JSON.stringify(data)
    })

export const removerMaquinaOs = (id, itemId) =>
    apiFetch(`/ordens-servico/${id}/maquinas/${itemId}`, {
        method: 'DELETE'
    })



export const buscarPlanejamentoPorSafraTalhao = (id) =>
  apiFetch(`/planejamento-operacao/por-safra-talhao/${id}`)


// 🔥 TURMAS TERCEIRIZADAS

export const listarTurmasTerceirizadas = () =>
    apiFetch('/turmas-terceirizadas')

export const listarTurmasTerceirizadasInativas = () =>
    apiFetch('/turmas-terceirizadas/inativas')

export const criarTurmaTerceirizada = (data) =>
    apiFetch('/turmas-terceirizadas', {
        method: 'POST',
        body: JSON.stringify(data)
    })

export const buscarTurmaTerceirizada = (id) =>
    apiFetch(`/turmas-terceirizadas/${id}`)

export const atualizarTurmaTerceirizada = (id, data) =>
    apiFetch(`/turmas-terceirizadas/${id}`, {
        method: 'PUT',
        body: JSON.stringify(data)
    })

export const excluirTurmaTerceirizada = (id) =>
    apiFetch(`/turmas-terceirizadas/${id}`, {
        method: 'DELETE'
    })

export const reativarTurmaTerceirizada = (id) =>
    apiFetch(`/turmas-terceirizadas/${id}/reativar`, {
        method: 'PUT'
    })

export const registrarApontamentoTurma = (data) =>
    apiFetch('/apontamentos-turma', {
        method: 'POST',
        body: JSON.stringify(data)
    })

export const listarApontamentosTurmaPorOs = (id) =>
    apiFetch(`/apontamentos-turma/ordem-servico/${id}`)

export const listarApontamentosTurma = () =>
    apiFetch('/apontamentos-turma/por-fazenda-safra')

// 🔥 PEDIDOS DE COMPRA

export const listarPedidosCompra = () =>
  apiFetch('/pedidos-compra')

export const listarPedidosCompraInativos = () =>
  apiFetch('/pedidos-compra/inativos')

export const buscarPedidoCompra = (id) =>
  apiFetch(`/pedidos-compra/${id}`)

export const criarPedidoCompra = (data) =>
  apiFetch('/pedidos-compra', {
    method: 'POST',
    body: JSON.stringify(data)
  })

export const atualizarPedidoCompra = (id, data) =>
  apiFetch(`/pedidos-compra/${id}`, {
    method: 'PUT',
    body: JSON.stringify(data)
  })

export const excluirPedidoCompra = (id) =>
  apiFetch(`/pedidos-compra/${id}`, {
    method: 'DELETE'
  })

export const reativarPedidoCompra = (id) =>
  apiFetch(`/pedidos-compra/${id}/reativar`, {
    method: 'PUT'
  })

export const aprovarPedidoCompra = (id) =>
  apiFetch(`/pedidos-compra/${id}/aprovar`, {
    method: 'POST'
  })

export const receberPedidoCompra = (id) =>
  apiFetch(`/pedidos-compra/${id}/receber`, {
    method: 'POST'
  })

// 🔥 STATUS
export const listarStatusPedidoCompra = () =>
  apiFetch('/referencias/pedido-compra-status')

// 🔥 FOLHA PAGAMENTO

export const listarFolha = () =>
  apiFetch('/folha-pagamento')

export const buscarFolha = (id) =>
  apiFetch(`/folha-pagamento/${id}`)

export const criarFolha = (data) =>
  apiFetch('/folha-pagamento', {
    method: 'POST',
    body: JSON.stringify(data)
  })

export const atualizarFolha = (id, data) =>
  apiFetch(`/folha-pagamento/${id}`, {
    method: 'PUT',
    body: JSON.stringify(data)
  })

export const excluirFolha = (id) =>
  apiFetch(`/folha-pagamento/${id}`, {
    method: 'DELETE'
  })

// 🔥 OPERAÇÃO TALHÃO (adicionais)

export const adicionarOperacaoProduto = (data) =>
  apiFetch('/operacao-produtos', {
    method: 'POST',
    body: JSON.stringify(data)
  })

export const adicionarOperacaoCombustivel = (data) =>
  apiFetch('/operacao-combustivel', {
    method: 'POST',
    body: JSON.stringify(data)
  })

export const listarOperacaoCombustivel = (operacaoId) =>
  apiFetch(`/operacao-combustivel?operacaoId=${operacaoId}`)

export const removerOperacaoCombustivel = (id) =>
  apiFetch(`/operacao-combustivel/${id}`, {
    method: 'DELETE'
  })

// 🔥 OPERAÇÃO FUNCIONÁRIO

export const adicionarOperacaoFuncionario = (data) =>
  apiFetch('/operacao-funcionarios', {
    method: 'POST',
    body: JSON.stringify(data)
  })

export const listarOperacaoFuncionarios = (operacaoId) =>
  apiFetch(`/operacao-funcionarios?operacaoId=${operacaoId}`)

export const removerOperacaoFuncionario = (id) =>
  apiFetch(`/operacao-funcionarios/${id}`, {
    method: 'DELETE'
  })

export const obterOperacaoDetalhe = (id) =>
  apiFetch(`/operacoes-talhao/${id}/detalhe`)


//export const obterDashboard = () =>
//  apiFetch('/dashboard')

export const obterRelatorioCompleto = () =>
  apiFetch('/relatorios/completa')

export const obterDashboardSafra = (safraId) =>
  apiFetch(`/dashboard/safra/${safraId}`)

export const buscarSafra = (id) =>
  apiFetch(`/safras/${id}`)

export const criarSafra = (data) =>
  api.post('/safras', data)

export const atualizarSafra = (id, data) =>
  api.put(`/safras/${id}`, data)

export const excluirSafra = (id) =>
  api.delete(`/safras/${id}`)

export const listarReferencias = (tipo) =>
    apiFetch(`/referencias/${tipo}`)

export const listarReferenciasInativas = (tipo) =>
    apiFetch(`/referencias/${tipo}/inativos`)

export const buscarReferencia = (tipo, id) =>
    apiFetch(`/admin/referencias/${tipo}/${id}`)

export const criarReferencia = (tipo, data) =>
    apiFetch(`/admin/referencias/${tipo}`, {
        method: 'POST',
        body: JSON.stringify(data)
    })

export const atualizarReferencia = (tipo, id, data) =>
    apiFetch(`/admin/referencias/${tipo}/${id}`, {
        method: 'PUT',
        body: JSON.stringify(data)
    })

export const excluirReferencia = (tipo, id) =>
    apiFetch(`/admin/referencias/${tipo}/${id}`, {
        method: 'DELETE'
    })

export const reativarReferencia = (tipo, id) =>
    apiFetch(`/admin/referencias/${tipo}/${id}/reativar`, {
        method: 'PUT'
    })

export const obterParametrosEncargos = () =>
  apiFetch('/parametros/encargos')

export const listarParametrosCompletos = () =>
  apiFetch('/referencias/parametro-completo')

export const listarContasPagar = (page = 0, size = 10) =>
  apiFetch(`/contas-pagar/fazenda?page=${page}&size=${size}&sort=dataVencimento,ASC`)

export const pagarContaPagar = (id, data) =>
  apiFetch(`/contas-pagar/${id}/pagar`, {
    method: 'POST',
    body: JSON.stringify(data)
  })

// 🔥 LISTAR LANÇAMENTOS
export const listarLancamentos = (
  page = 0,
  size = 10,
  status = '',
  dataInicio = '',
  dataFim = ''
) => {

  let url = `/financeiro/lancamentos?page=${page}&size=${size}&sort=data,desc`

  if (status) url += `&status=${status}`
  if (dataInicio) url += `&dataInicio=${dataInicio}`
  if (dataFim) url += `&dataFim=${dataFim}`

  return apiFetch(url)
}

export const pagarLancamento = (id) =>
  apiFetch(`/financeiro/lancamentos/${id}/pagar`, {
    method: 'PUT'
  })


export const resumoLancamentos = () =>
  apiFetch(`/financeiro/lancamentos/resumo`)

export const deletarContaPagar = (id) =>
  apiFetch(`/contas-pagar/${id}`, {
    method: 'DELETE'
  })

export const criarContaPagar = (data) =>
  apiFetch('/contas-pagar', {
    method: 'POST',
    body: JSON.stringify(data)
  })

export const buscarContaPagar = (id) =>
  apiFetch(`/contas-pagar/${id}`)

export const atualizarContaPagar = (id, data) =>
  apiFetch(`/contas-pagar/${id}`, {
    method: 'PUT',
    body: JSON.stringify(data)
  })

// ==========================
// ADMINISTRATIVO
// ==========================

export const getAdministrativo = () => {
  return api.get('/administrativo');
};

export const getAdministrativoById = (id) => {
  return api.get(`/administrativo/${id}`);
};

export const createAdministrativo = (data) => {
  return api.post('/administrativo', data);
};

export const updateAdministrativo = (id, data) => {
  return api.put(`/administrativo/${id}`, data);
};

export const deleteAdministrativo = (id) => {
  return api.delete(`/administrativo/${id}`);
};

export const getTotalAdministrativo = () => {
  return api.get('/administrativo/total-realizado');
};

export const listarTipoRateio = () => {
  return api.get('/admin/referencias/tipo-rateio');
};

export const obterPdfOrdemServico = (id) => {
  return apiFetch(`/ordem-servico/${id}/pdf`, {
    method: 'GET'
  });
};

export const obterGestaoVista = () => {
  return apiFetch('/relatorios/gestao-vista')
}

export const listarGestaoVista = () =>
  apiFetch('/relatorios/gestao-vista')

export const criarVenda = (data) =>
  apiFetch('/vendas', {
    method: 'POST',
    body: JSON.stringify(data)
  })

export const listarVendasPorTalhao = (safraTalhaoId) =>
  apiFetch(`/vendas/talhao/${safraTalhaoId}`)

 export const listarReferenciasOperacaoTalhao = () =>
   apiFetch('/admin/referencias/operacao-talhao')

export const obterComparativoTalhoes = () =>
    apiFetch('/bi/comparativo-talhoes')

export const obterComparativoSafras = () =>
    apiFetch('/bi/comparativo-safras')