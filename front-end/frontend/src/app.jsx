import { Routes, Route, Navigate } from 'react-router-dom'
import Login from './auth/Login'
import PrivateRoute from './auth/PrivateRoute'
import HomeRedirect from './pages/HomeRedirect'
import SelecionarFazenda from './pages/SelecionarFazenda'
import Layout from './components/Layout'
import OperationLayout from './components/OperationLayout'
import Proprietarios from './pages/Proprietarios'
import AtivaSafra from './pages/AtivaSafra'
import FazendaForm from './pages/FazendaForm'
import MenuFazenda from './pages/MenuFazenda'
import TrocarSenha from './pages/TrocarSenha'
import ProprietarioForm from './pages/ProprietarioForm'
import Talhoes from './pages/Talhoes'
import TalhaoForm from './pages/TalhaoForm'
import Terceirizados from './pages/Terceirizados'
import TerceirizadoForm from './pages/TerceirizadoForm'
import Funcionarios from './pages/Funcionarios'
import FuncionarioForm from './pages/FuncionarioForm'
import TurmasTerceirizadas from './pages/TurmasTerceirizadas'
import TurmaTerceirizadaForm from './pages/TurmaTerceirizadaForm'

import Maquinas from './pages/Maquinas'
import MaquinaForm from './pages/MaquinaForm'
import MaquinaDetalhe from './pages/MaquinaDetalhe'
import HoraMaquinaForm from './pages/HoraMaquinaForm'
import GastoMaquinaForm from './pages/GastoMaquinaForm'

import Produtos from './pages/Produtos'
import ProdutoForm from './pages/ProdutoForm'
import MovProdutoForm from './pages/MovProdutoForm'
import ProdutoDetalhe from './pages/ProdutoDetalhe'

import SafraTalhoes from './pages/SafraTalhoes'
import SafraTalhaoForm from './pages/SafraTalhaoForm'

import PlanejamentoForm from './pages/PlanejamentoForm'
import PlanejamentoNovo from './pages/PlanejamentoNovo'
import PlanejamentoInsumos from './pages/PlanejamentoInsumos'
import PlanejamentoMaquinas from './pages/PlanejamentoMaquinas'
import PlanejamentoFuncionarios from './pages/PlanejamentoFuncionarios'
import PlanejamentoResumo from './pages/PlanejamentoResumo'
import PlanejamentoComparativo from './pages/PlanejamentoComparativo'
import PlanejamentoComparativoDetalhado from './pages/PlanejamentoComparativoDetalhado'
import AdubacaoPage from "./pages/AdubacaoPage"

import OrdemServicoList from './pages/OrdemServicoList'
import OrdemServicoForm from './pages/OrdemServicoForm'
import OrdemServicoDetalhe from './pages/OrdemServicoDetalhe'
import OrdemServicoAuditoria from './pages/OrdemServicoAuditoria'
import OrdemServicoAuditoriaDetalhe from './pages/OrdemServicoAuditoriaDetalhe'
import OrdemServicoTurmas from './pages/OrdemServicoTurmas'

import OrdemServicoExecucao from './pages/OrdemServicoExecucao'
import OrdemServicoInsumos from './pages/OrdemServicoInsumos'
import OrdemServicoMaquinas from './pages/OrdemServicoMaquinas'
import OrdemServicoFuncionarios from './pages/OrdemServicoFuncionarios'
import DashboardSafra from './pages/DashboardSafra'

import PedidosCompra from './pages/PedidosCompra'
import PedidoCompraForm from './pages/PedidoCompraForm'
import FolhaPagamento from './pages/FolhaPagamento'
import FolhaPagamentoForm from './pages/FolhaPagamentoForm'

import OperacaoDetalhe from './pages/OperacaoDetalhe'
import RelatorioCompleto from './pages/RelatorioCompleto'

import Safras from './pages/Safras'
import SafraForm from './pages/SafraForm'

import ReferenciasPage from './pages/ReferenciasPage'
import ReferenciasMenu from './pages/ReferenciasMenu'
import Financeiro from './pages/Financeiro'
import ContasPagar from './pages/ContasPagar'
import ContasPagarForm from './pages/ContasPagarForm'
import BaixarContaForm from './pages/BaixarContaForm'
import LancamentosPage from './pages/LancamentosPage'
import AdministrativoPage from './modules/administrativo/AdministrativoPage';
import AdministrativoForm from './modules/administrativo/AdministrativoForm';
import GestaoVistaPage from './pages/GestaoVistaPage'

import VendasPage from './pages/VendasPage'
import VendaForm from './pages/VendaForm'
import ComparativoTalhoesPage from './pages/ComparativoTalhoesPage'
import ComparativoSafrasPage from './pages/ComparativoSafrasPage'


export default function App() {
    return (
        <Routes>

            <Route path="/login" element={<Login />} />

            <Route
                path="/"
                element={
                    <PrivateRoute>
                        <HomeRedirect />
                    </PrivateRoute>
                }
            />

            {/* ============================
               ADMINISTRATIVO
            ============================ */}

            <Route path="/selecionar-fazenda" element={<PrivateRoute><Layout><SelecionarFazenda /></Layout></PrivateRoute>} />
            <Route path="/proprietarios" element={<PrivateRoute><Layout><Proprietarios /></Layout></PrivateRoute>} />
            <Route path="/proprietarios/novo" element={<PrivateRoute><Layout><ProprietarioForm /></Layout></PrivateRoute>} />
            <Route path="/proprietarios/:id/editar" element={<PrivateRoute><Layout><ProprietarioForm /></Layout></PrivateRoute>} />
            <Route path="/fazendas/nova" element={<PrivateRoute><Layout><FazendaForm /></Layout></PrivateRoute>} />
            <Route path="/fazendas/:id/editar" element={<PrivateRoute><Layout><FazendaForm /></Layout></PrivateRoute>} />
            <Route path="/auth/trocar-senha" element={<PrivateRoute><Layout><TrocarSenha /></Layout></PrivateRoute>} />

            {/* ============================
               OPERACIONAL
            ============================ */}

            <Route path="/ativarSafra" element={<PrivateRoute><Layout><AtivaSafra /></Layout></PrivateRoute>} />
            <Route path="/menu" element={<PrivateRoute><OperationLayout><MenuFazenda /></OperationLayout></PrivateRoute>} />

            {/* TALHÕES */}
            <Route path="/talhoes" element={<PrivateRoute><OperationLayout><Talhoes /></OperationLayout></PrivateRoute>} />
            <Route path="/talhoes/novo" element={<PrivateRoute><OperationLayout><TalhaoForm /></OperationLayout></PrivateRoute>} />
            <Route path="/talhoes/:id/editar" element={<PrivateRoute><OperationLayout><TalhaoForm /></OperationLayout></PrivateRoute>} />

            {/* SAFRA TALHÕES */}
            <Route path="/safra-talhoes" element={<PrivateRoute><OperationLayout><SafraTalhoes /></OperationLayout></PrivateRoute>} />
            <Route path="/safra-talhoes/novo" element={<PrivateRoute><OperationLayout><SafraTalhaoForm /></OperationLayout></PrivateRoute>} />
            <Route path="/safra-talhoes/:id/editar" element={<PrivateRoute><OperationLayout><SafraTalhaoForm /></OperationLayout></PrivateRoute>} />

            {/* FUNCIONÁRIOS */}
            <Route path="/funcionarios" element={<PrivateRoute><OperationLayout><Funcionarios /></OperationLayout></PrivateRoute>} />
            <Route path="/funcionarios/novo" element={<PrivateRoute><OperationLayout><FuncionarioForm /></OperationLayout></PrivateRoute>} />
            <Route path="/funcionarios/:id/editar" element={<PrivateRoute><OperationLayout><FuncionarioForm /></OperationLayout></PrivateRoute>} />

            {/* TERCEIRIZADOS */}
            <Route path="/terceirizados" element={<PrivateRoute><OperationLayout><Terceirizados /></OperationLayout></PrivateRoute>} />
            <Route path="/terceirizados/novo" element={<PrivateRoute><OperationLayout><TerceirizadoForm /></OperationLayout></PrivateRoute>} />
            <Route path="/terceirizados/:id/editar" element={<PrivateRoute><OperationLayout><TerceirizadoForm /></OperationLayout></PrivateRoute>} />

            {/* TURMAS TERCEIRIZADAS */}
            <Route path="/turmas-terceirizadas" element={<PrivateRoute><OperationLayout><TurmasTerceirizadas /></OperationLayout></PrivateRoute>} />
            <Route path="/turmas-terceirizadas/novo" element={<PrivateRoute><OperationLayout><TurmaTerceirizadaForm /></OperationLayout></PrivateRoute>} />
            <Route path="/turmas-terceirizadas/:id/editar" element={<PrivateRoute><OperationLayout><TurmaTerceirizadaForm /></OperationLayout></PrivateRoute>} />

            {/* MÁQUINAS */}
            <Route path="/maquinas" element={<PrivateRoute><OperationLayout><Maquinas /></OperationLayout></PrivateRoute>} />
            <Route path="/maquinas/novo" element={<PrivateRoute><OperationLayout><MaquinaForm /></OperationLayout></PrivateRoute>} />
            <Route path="/maquinas/:id/editar" element={<PrivateRoute><OperationLayout><MaquinaForm /></OperationLayout></PrivateRoute>} />
            <Route path="/maquinas/:id/horas/nova" element={<PrivateRoute><OperationLayout><HoraMaquinaForm /></OperationLayout></PrivateRoute>} />
            <Route path="/maquinas/:id/gastos/nova" element={<PrivateRoute><OperationLayout><GastoMaquinaForm /></OperationLayout></PrivateRoute>} />
            <Route path="/maquinas/:id" element={<PrivateRoute><OperationLayout><MaquinaDetalhe /></OperationLayout></PrivateRoute>} />

            {/* PRODUTOS */}
            <Route path="/produtos" element={<PrivateRoute><OperationLayout><Produtos /></OperationLayout></PrivateRoute>} />
            <Route path="/produtos/novo" element={<PrivateRoute><OperationLayout><ProdutoForm /></OperationLayout></PrivateRoute>} />
            <Route path="/produtos/:id/editar" element={<PrivateRoute><OperationLayout><ProdutoForm /></OperationLayout></PrivateRoute>} />
            <Route path="/produtos/movimentar" element={<PrivateRoute><OperationLayout><MovProdutoForm /></OperationLayout></PrivateRoute>} />
            <Route path="/produtos/:id" element={<PrivateRoute><OperationLayout><ProdutoDetalhe /></OperationLayout></PrivateRoute>} />

            {/* PLANEJAMENTO */}
            <Route path="/planejamento/novo" element={<PrivateRoute><OperationLayout><PlanejamentoNovo /></OperationLayout></PrivateRoute>} />
            <Route path="/planejamento/:id/insumos" element={<PrivateRoute><OperationLayout><PlanejamentoInsumos /></OperationLayout></PrivateRoute>} />
            <Route path="/planejamento/:id/maquinas" element={<PrivateRoute><OperationLayout><PlanejamentoMaquinas /></OperationLayout></PrivateRoute>} />
            <Route path="/planejamento/:id/funcionarios" element={<PrivateRoute><OperationLayout><PlanejamentoFuncionarios /></OperationLayout></PrivateRoute>} />
            <Route path="/planejamento/:id/resumo" element={<PrivateRoute><OperationLayout><PlanejamentoResumo /></OperationLayout></PrivateRoute>} />
            <Route path="/planejamento/:id/comparativo" element={<PlanejamentoComparativo />} />
            <Route path="/planejamento/:id/comparativo-detalhado" element={<PlanejamentoComparativoDetalhado />} />
            <Route path="/planejamento/adubacao" element={<PrivateRoute><OperationLayout><AdubacaoPage /></OperationLayout></PrivateRoute>} />

            {/* ORDEM DE SERVIÇO */}
            <Route path="/ordens-servico" element={<PrivateRoute><OperationLayout><OrdemServicoList /></OperationLayout></PrivateRoute>} />
            <Route path="/ordens-servico/novo" element={<PrivateRoute><OperationLayout><OrdemServicoForm /></OperationLayout></PrivateRoute>} />
            <Route path="/ordens-servico/:id" element={<PrivateRoute><OperationLayout><OrdemServicoDetalhe /></OperationLayout></PrivateRoute>} />
            <Route path="/ordens-servico/:id/auditoria" element={<OrdemServicoAuditoria />} />
            <Route path="/ordens-servico/:id/auditoria/detalhe" element={<OrdemServicoAuditoriaDetalhe />} />
            <Route path="/ordens-servico/:id/turmas" element={<PrivateRoute><OperationLayout><OrdemServicoTurmas /></OperationLayout></PrivateRoute>} />
            <Route path="/operacoes/:id" element={<OperacaoDetalhe />} />

            {/* EXECUÇÃO */}
            <Route path="/ordens-servico/:id/execucao" element={<PrivateRoute><OperationLayout><OrdemServicoExecucao /></OperationLayout></PrivateRoute>} />
            <Route path="/ordens-servico/:id/insumos" element={<PrivateRoute><OperationLayout><OrdemServicoInsumos /></OperationLayout></PrivateRoute>} />
            <Route path="/ordens-servico/:id/maquinas" element={<PrivateRoute><OperationLayout><OrdemServicoMaquinas /></OperationLayout></PrivateRoute>} />
            <Route path="/ordens-servico/:id/funcionarios" element={<PrivateRoute><OperationLayout><OrdemServicoFuncionarios /></OperationLayout></PrivateRoute>} />

            <Route path="/pedidos-compra" element={<PrivateRoute><OperationLayout><PedidosCompra /></OperationLayout></PrivateRoute>} />
            <Route path="/pedidos-compra/novo" element={<PrivateRoute><OperationLayout><PedidoCompraForm /></OperationLayout></PrivateRoute>} />
            <Route path="/pedidos-compra/:id/editar" element={<PrivateRoute><OperationLayout><PedidoCompraForm /></OperationLayout></PrivateRoute>} />

            <Route path="/folha" element={<PrivateRoute><OperationLayout><FolhaPagamento /></OperationLayout></PrivateRoute>} />
            <Route path="/folha/novo" element={<PrivateRoute><OperationLayout><FolhaPagamentoForm /></OperationLayout></PrivateRoute>} />
            <Route path="/folha/:id/editar" element={<PrivateRoute><OperationLayout><FolhaPagamentoForm /></OperationLayout></PrivateRoute>} />
            <Route path="/financeiro" element={<PrivateRoute><OperationLayout><Financeiro /></OperationLayout></PrivateRoute>}/>
            <Route path="/financeiro/contas-pagar" element={<PrivateRoute><OperationLayout><ContasPagar /></OperationLayout></PrivateRoute>}/>
            <Route path="/financeiro/contas-pagar/novo" element={<PrivateRoute><OperationLayout><ContasPagarForm /></OperationLayout></PrivateRoute>}/>
            <Route path="/financeiro/contas-pagar/:id/pagar" element={<PrivateRoute><OperationLayout><BaixarContaForm /></OperationLayout></PrivateRoute>}/>
            <Route path="/financeiro/contas-pagar/:id" element={<PrivateRoute><OperationLayout><ContasPagarForm /></OperationLayout></PrivateRoute>}/>
            <Route path="/financeiro/contas-pagar/:id/editar" element={<PrivateRoute><OperationLayout><ContasPagarForm /></OperationLayout></PrivateRoute>}/>
            <Route path="/financeiro/lancamentos" element={<PrivateRoute><OperationLayout><LancamentosPage /></OperationLayout></PrivateRoute>}/>

            <Route path="/dashboard-safra" element={<PrivateRoute><OperationLayout><DashboardSafra /></OperationLayout></PrivateRoute>}/>
            <Route path="/relatorio" element={<PrivateRoute><OperationLayout><RelatorioCompleto /></OperationLayout></PrivateRoute>}/>

            <Route path="/safra" element={<PrivateRoute><OperationLayout><Safras /></OperationLayout></PrivateRoute>} />
            <Route path="/safra/novo" element={<PrivateRoute><OperationLayout><SafraForm /></OperationLayout></PrivateRoute>} />
            <Route path="/safra/:id/editar" element={<PrivateRoute><OperationLayout><SafraForm /></OperationLayout></PrivateRoute>} />

            <Route path="/referencias" element={<PrivateRoute><OperationLayout><ReferenciasMenu /></OperationLayout></PrivateRoute>}/>
            <Route path="/menu" element={<PrivateRoute><OperationLayout><MenuFazenda /></OperationLayout></PrivateRoute>} />
            <Route path="/referencias" element={<PrivateRoute><OperationLayout><ReferenciasMenu /></OperationLayout></PrivateRoute>} />
            <Route path="/referencias/:tipo" element={<PrivateRoute><OperationLayout><ReferenciasPage /></OperationLayout></PrivateRoute>} />

            <Route path="/custos/administrativo" element={<PrivateRoute><OperationLayout><AdministrativoPage /></OperationLayout></PrivateRoute>} />
            <Route path="/custos/administrativo/novo" element={<PrivateRoute><OperationLayout><AdministrativoForm /></OperationLayout></PrivateRoute>} />
            <Route path="/custos/administrativo/:id" element={<PrivateRoute><OperationLayout><AdministrativoForm /></OperationLayout></PrivateRoute>} />

            <Route path="/relatorios/gestao-vista" element={<PrivateRoute><OperationLayout><GestaoVistaPage /></OperationLayout></PrivateRoute>}/>

            <Route path="/vendas" element={<PrivateRoute><OperationLayout><VendasPage /></OperationLayout></PrivateRoute>}/>
            <Route path="/vendas/novo" element={<PrivateRoute><OperationLayout><VendaForm /></OperationLayout></PrivateRoute>}/>
            <Route path="/bi/comparativo-talhoes" element={<PrivateRoute><OperationLayout><ComparativoTalhoesPage /></OperationLayout></PrivateRoute>}/>
            <Route path="/bi/comparativo-safras"  element={<PrivateRoute><OperationLayout><ComparativoSafrasPage /></OperationLayout></PrivateRoute>}/>

            <Route path="*" element={<Navigate to="/" />} />

        </Routes>
    )
}