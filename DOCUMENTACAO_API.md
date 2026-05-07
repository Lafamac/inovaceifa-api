# Inova Ceifa - Documentação da API Backend

> **Documentação gerada automaticamente a partir do Swagger.**
> URL Base Padrão: `http://localhost:8080` (em desenvolvimento) ou URL de Produção.

##  Turmas terceirizadas

### `[GET] /turmas-terceirizadas/{id}`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

---

### `[PUT] /turmas-terceirizadas/{id}`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

**Corpo da Requisição (JSON):** `Modelo TurmaUpdateDTO`

| Campo | Tipo | Descrição |
|---|---|---|
| `nome` | `string` | - |
| `responsavel` | `string` | - |
| `tipoPagamentoId` | `integer` | - |
| `operacaoId` | `integer` | - |
| `valorDiaria` | `number` | - |
| `valorPorSaca` | `number` | - |
| `quantidadePessoas` | `integer` | - |
| `dataInicio` | `string` | - |
| `dataFim` | `string` | - |

---

### `[DELETE] /turmas-terceirizadas/{id}`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

---

### `[PUT] /turmas-terceirizadas/{id}/reativar`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

---

### `[GET] /turmas-terceirizadas`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `page` | `integer` | query | Não |
| `size` | `integer` | query | Não |
| `sort` | `array` | query | Não |

---

### `[POST] /turmas-terceirizadas`
**Corpo da Requisição (JSON):** `Modelo TurmaCreateDTO`

| Campo | Tipo | Descrição |
|---|---|---|
| `nome` | `string` | - |
| `responsavel` | `string` | - |
| `tipoPagamentoId` | `integer` | - |
| `operacaoId` | `integer` | - |
| `valorDiaria` | `number` | - |
| `valorPorSaca` | `number` | - |
| `quantidadePessoas` | `integer` | - |
| `dataInicio` | `string` | - |
| `dataFim` | `string` | - |

---

### `[GET] /turmas-terceirizadas/inativas`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `page` | `integer` | query | Não |
| `size` | `integer` | query | Não |
| `sort` | `array` | query | Não |

---

## Adubação

### `[GET] /planejamento/adubacao`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `safraId` | `integer` | query | Sim |

---

## Apontamento Turma

### `[POST] /apontamentos-turma`
**Corpo da Requisição (JSON):** `Modelo ApontamentoTurmaCreateDTO`

| Campo | Tipo | Descrição |
|---|---|---|
| `turmaId` | `integer` | - |
| `dataInicio` | `string` | - |
| `dataFim` | `string` | - |
| `diasTrabalhados` | `integer` | - |
| `quantidadeColhida` | `number` | - |
| `observacao` | `string` | - |
| `ordemServicoId` | `integer` | - |

---

### `[GET] /apontamentos-turma/por-fazenda-safra`
---

### `[GET] /apontamentos-turma/ordem-servico/{id}`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

---

## Cadastro de Operações

### `[GET] /cadastro-operacoes/{id}`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

---

### `[PUT] /cadastro-operacoes/{id}`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

**Corpo da Requisição (JSON):** `Modelo CadastroOperacaoUpdateDTO`

| Campo | Tipo | Descrição |
|---|---|---|
| `codOper` | `integer` | - |
| `cultura` | `string` | - |
| `operacao` | `string` | - |
| `modalidade` | `string` | - |
| `deslocamento` | `string` | - |
| `atividade` | `string` | - |
| `faixaNominal` | `number` | - |
| `velocidadeOp` | `number` | - |
| `eficienciaCampo` | `number` | - |
| `gastoDiesel` | `number` | - |

---

### `[DELETE] /cadastro-operacoes/{id}`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

---

### `[PUT] /cadastro-operacoes/{id}/reativar`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

---

### `[GET] /cadastro-operacoes`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `page` | `integer` | query | Não |
| `size` | `integer` | query | Não |
| `sort` | `array` | query | Não |

---

### `[POST] /cadastro-operacoes`
**Corpo da Requisição (JSON):** `Modelo CadastroOperacaoCreateDTO`

| Campo | Tipo | Descrição |
|---|---|---|
| `codOper` | `integer` | - |
| `cultura` | `string` | - |
| `operacao` | `string` | - |
| `modalidade` | `string` | - |
| `deslocamento` | `string` | - |
| `atividade` | `string` | - |
| `faixaNominal` | `number` | - |
| `velocidadeOp` | `number` | - |
| `eficienciaCampo` | `number` | - |
| `gastoDiesel` | `number` | - |

---

### `[GET] /cadastro-operacoes/inativos`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `page` | `integer` | query | Não |
| `size` | `integer` | query | Não |
| `sort` | `array` | query | Não |

---

### `[GET] /cadastro-operacoes/ativos`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `page` | `integer` | query | Não |
| `size` | `integer` | query | Não |
| `sort` | `array` | query | Não |

---

## Conta Gerencial

### `[GET] /conta-gerencial/{id}`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

---

### `[PUT] /conta-gerencial/{id}`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

**Corpo da Requisição (JSON):** `Modelo ReferenciaUpdateDTO`

| Campo | Tipo | Descrição |
|---|---|---|
| `descricao` | `string` | - |

---

### `[DELETE] /conta-gerencial/{id}`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

---

### `[PUT] /conta-gerencial/{id}/reativar`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

---

### `[GET] /conta-gerencial`
---

### `[POST] /conta-gerencial`
**Corpo da Requisição (JSON):** `Modelo ReferenciaCreateDTO`

| Campo | Tipo | Descrição |
|---|---|---|
| `descricao` | `string` | - |

---

### `[GET] /conta-gerencial/inativos`
---

## Fazenda

### `[GET] /fazendas/{id}`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

---

### `[PUT] /fazendas/{id}`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

**Corpo da Requisição (JSON):** `Modelo FazendaUpdateDTO`

| Campo | Tipo | Descrição |
|---|---|---|
| `nome` | `string` | - |
| `endereco` | `string` | - |
| `cidade` | `string` | - |
| `estado` | `string` | - |
| `safraAtivaId` | `integer` | ID da safra ativa da fazenda |

---

### `[DELETE] /fazendas/{id}`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

---

### `[PUT] /fazendas/{id}/reativar`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

---

### `[PUT] /fazendas/{id}/ativa`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

---

### `[GET] /fazendas`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `page` | `integer` | query | Não |
| `size` | `integer` | query | Não |
| `sort` | `array` | query | Não |

---

### `[POST] /fazendas`
**Corpo da Requisição (JSON):** `Modelo FazendaCreateDTO`

| Campo | Tipo | Descrição |
|---|---|---|
| `proprietarioId` | `integer` | ID do proprietário (obrigatório apenas para super usuário) |
| `nome` | `string` | - |
| `cnpj` | `string` | - |
| `safraAtivaId` | `integer` | ID da safra ativa (opcional) |
| `endereco` | `string` | - |
| `cidade` | `string` | - |
| `estado` | `string` | - |
| `nomeSafraInicial` | `string` | - |

---

### `[GET] /fazendas/inativas`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `page` | `integer` | query | Não |
| `size` | `integer` | query | Não |
| `sort` | `array` | query | Não |

---

## Funcionários

### `[GET] /funcionarios/{id}`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

---

### `[PUT] /funcionarios/{id}`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

**Corpo da Requisição (JSON):** `Modelo FuncionarioUpdateDTO`

| Campo | Tipo | Descrição |
|---|---|---|
| `nome` | `string` | - |
| `endereco` | `string` | - |
| `bairro` | `string` | - |
| `cidade` | `string` | - |
| `estado` | `string` | - |
| `email` | `string` | - |
| `celular` | `string` | - |
| `cargo` | `string` | - |
| `salario` | `number` | - |
| `dtAdmissao` | `string` | - |

---

### `[DELETE] /funcionarios/{id}`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

---

### `[PUT] /funcionarios/{id}/reativar`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

---

### `[GET] /funcionarios`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `page` | `integer` | query | Não |
| `size` | `integer` | query | Não |
| `sort` | `array` | query | Não |

---

### `[POST] /funcionarios`
**Corpo da Requisição (JSON):** `Modelo FuncionarioCreateDTO`

| Campo | Tipo | Descrição |
|---|---|---|
| `nome` | `string` | - |
| `cpf` | `string` | - |
| `endereco` | `string` | - |
| `bairro` | `string` | - |
| `cidade` | `string` | - |
| `estado` | `string` | - |
| `email` | `string` | - |
| `celular` | `string` | - |
| `cargo` | `string` | - |
| `salario` | `number` | - |
| `dtAdmissao` | `string` | - |

---

### `[POST] /funcionarios/{id}/criar-usuario`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

**Corpo da Requisição (JSON):** `Modelo FuncionarioCriarUsuarioDTO`

| Campo | Tipo | Descrição |
|---|---|---|
| `email` | `string` | - |
| `senha` | `string` | - |

---

### `[GET] /funcionarios/inativos`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `page` | `integer` | query | Não |
| `size` | `integer` | query | Não |
| `sort` | `array` | query | Não |

---

## Lançamentos de Despesas

### `[PUT] /financeiro/lancamentos/{id}/reativar`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

---

### `[PUT] /financeiro/lancamentos/{id}/pagar`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

---

### `[PUT] /financeiro/lancamentos/{id}/cancelar`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

---

### `[GET] /financeiro/lancamentos`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `status` | `string` | query | Não |
| `dataInicio` | `string` | query | Não |
| `dataFim` | `string` | query | Não |

---

### `[POST] /financeiro/lancamentos`
**Corpo da Requisição (JSON):** `Modelo LancamentoCreateDTO`

| Campo | Tipo | Descrição |
|---|---|---|
| `refDespesaId` | `integer` | - |
| `centroCustoId` | `integer` | - |
| `valor` | `number` | - |
| `data` | `string` | - |
| `observacao` | `string` | - |

---

### `[GET] /financeiro/lancamentos/resumo`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `dataInicio` | `string` | query | Não |
| `dataFim` | `string` | query | Não |

---

### `[GET] /financeiro/lancamentos/projecao`
---

### `[GET] /financeiro/lancamentos/orcado-vs-realizado`
---

### `[GET] /financeiro/lancamentos/dashboard`
---

### `[GET] /financeiro/lancamentos/dashboard-mensal`
---

### `[GET] /financeiro/lancamentos/custo-hectare`
---

## Operacção Produto

### `[GET] /operacao-produtos`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `page` | `integer` | query | Não |
| `size` | `integer` | query | Não |
| `sort` | `array` | query | Não |

---

### `[POST] /operacao-produtos`
**Corpo da Requisição (JSON):** `Modelo OperacaoProdutoCreateDTO`

| Campo | Tipo | Descrição |
|---|---|---|
| `operacaoTalhaoId` | `integer` | - |
| `produtoId` | `integer` | - |
| `quantidade` | `number` | - |
| `vlrUnitario` | `number` | - |

---

## Operação Funcionários

### `[GET] /operacao-funcionarios`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `page` | `integer` | query | Não |
| `size` | `integer` | query | Não |
| `sort` | `array` | query | Não |

---

### `[POST] /operacao-funcionarios`
**Corpo da Requisição (JSON):** `Modelo OperacaoFuncionarioCreateDTO`

| Campo | Tipo | Descrição |
|---|---|---|
| `operacaoTalhaoId` | `integer` | - |
| `funcionarioId` | `integer` | - |
| `valorUnitario` | `number` | - |
| `horasTrabalhadas` | `number` | - |

---

## Operação de Combustível

### `[GET] /operacao-combustivel`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `page` | `integer` | query | Não |
| `size` | `integer` | query | Não |
| `sort` | `array` | query | Não |

---

### `[POST] /operacao-combustivel`
**Corpo da Requisição (JSON):** `Modelo OperacaoCombustivelCreateDTO`

| Campo | Tipo | Descrição |
|---|---|---|
| `operacaoTalhaoId` | `integer` | - |
| `maquinaId` | `integer` | - |
| `litros` | `number` | - |
| `valorUnitario` | `number` | - |

---

## Produtos

### `[GET] /produtos/{id}`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

---

### `[PUT] /produtos/{id}`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

**Corpo da Requisição (JSON):** `Modelo ProdutoUpdateDTO`

| Campo | Tipo | Descrição |
|---|---|---|
| `nome` | `string` | - |
| `codigo` | `string` | - |
| `unidade` | `string` | - |
| `ativoNutr` | `string` | - |
| `grupoId` | `integer` | - |
| `familiaId` | `integer` | - |
| `qtde` | `number` | - |
| `vlrUnitario` | `number` | - |
| `precoCusto` | `number` | - |

---

### `[DELETE] /produtos/{id}`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

---

### `[PUT] /produtos/{id}/reativar`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

---

### `[GET] /produtos`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `page` | `integer` | query | Não |
| `size` | `integer` | query | Não |
| `sort` | `array` | query | Não |

---

### `[POST] /produtos`
**Corpo da Requisição (JSON):** `Modelo ProdutoCreateDTO`

| Campo | Tipo | Descrição |
|---|---|---|
| `nome` | `string` | - |
| `codigo` | `string` | - |
| `unidade` | `string` | - |
| `ativoNutr` | `string` | - |
| `grupoId` | `integer` | - |
| `familiaId` | `integer` | - |
| `qtde` | `number` | - |
| `vlrUnitario` | `number` | - |
| `precoCusto` | `number` | - |

---

### `[GET] /produtos/inativos`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `page` | `integer` | query | Não |
| `size` | `integer` | query | Não |
| `sort` | `array` | query | Não |

---

### `[GET] /produtos/ativos`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `page` | `integer` | query | Não |
| `size` | `integer` | query | Não |
| `sort` | `array` | query | Não |

---

## Proprietários

### `[GET] /proprietarios/{id}`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

---

### `[PUT] /proprietarios/{id}`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

**Corpo da Requisição (JSON):** `Modelo ProprietarioUpdateDTO`

| Campo | Tipo | Descrição |
|---|---|---|
| `nome` | `string` | - |
| `cpf` | `string` | - |
| `email` | `string` | - |
| `celular` | `string` | - |
| `endereco` | `string` | - |
| `bairro` | `string` | - |
| `cidade` | `string` | - |
| `estado` | `string` | - |
| `ativo` | `boolean` | - |

---

### `[DELETE] /proprietarios/{id}`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

---

### `[PUT] /proprietarios/{id}/reativar`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

---

### `[GET] /proprietarios`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `page` | `integer` | query | Não |
| `size` | `integer` | query | Não |
| `sort` | `array` | query | Não |

---

### `[POST] /proprietarios`
**Corpo da Requisição (JSON):** `Modelo ProprietarioCreateDTO`

| Campo | Tipo | Descrição |
|---|---|---|
| `nome` | `string` | - |
| `cpf` | `string` | - |
| `email` | `string` | - |
| `celular` | `string` | - |
| `endereco` | `string` | - |
| `bairro` | `string` | - |
| `cidade` | `string` | - |
| `estado` | `string` | - |

---

### `[GET] /proprietarios/inativos`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `page` | `integer` | query | Não |
| `size` | `integer` | query | Não |
| `sort` | `array` | query | Não |

---

### `[GET] /proprietarios/ativos`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `page` | `integer` | query | Não |
| `size` | `integer` | query | Não |
| `sort` | `array` | query | Não |

---

## Terceirizados

### `[GET] /terceirizados/{id}`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

---

### `[PUT] /terceirizados/{id}`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

**Corpo da Requisição (JSON):** `Modelo TerceirizadoUpdateDTO`

| Campo | Tipo | Descrição |
|---|---|---|
| `nome` | `string` | - |
| `endereco` | `string` | - |
| `bairro` | `string` | - |
| `cidade` | `string` | - |
| `estado` | `string` | - |
| `email` | `string` | - |
| `celular` | `string` | - |
| `imagem` | `string` | - |
| `cargo` | `string` | - |
| `salario` | `number` | - |

---

### `[DELETE] /terceirizados/{id}`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

---

### `[PUT] /terceirizados/{id}/reativar`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

---

### `[GET] /terceirizados`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `page` | `integer` | query | Não |
| `size` | `integer` | query | Não |
| `sort` | `array` | query | Não |

---

### `[POST] /terceirizados`
**Corpo da Requisição (JSON):** `Modelo TerceirizadoCreateDTO`

| Campo | Tipo | Descrição |
|---|---|---|
| `nome` | `string` | - |
| `cpf` | `string` | - |
| `endereco` | `string` | - |
| `bairro` | `string` | - |
| `cidade` | `string` | - |
| `estado` | `string` | - |
| `email` | `string` | - |
| `celular` | `string` | - |
| `imagem` | `string` | - |
| `cargo` | `string` | - |
| `salario` | `number` | - |

---

### `[GET] /terceirizados/inativos`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `page` | `integer` | query | Não |
| `size` | `integer` | query | Não |
| `sort` | `array` | query | Não |

---

## Tipo Conta

### `[PUT] /tipo-conta/{id}`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

**Corpo da Requisição (JSON):** `Modelo TipoContaUpdateDTO`

| Campo | Tipo | Descrição |
|---|---|---|
| `arvore` | `string` | - |
| `indice` | `string` | - |
| `ativo` | `boolean` | - |

---

### `[DELETE] /tipo-conta/{id}`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

---

### `[GET] /tipo-conta`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `page` | `integer` | query | Não |
| `size` | `integer` | query | Não |
| `sort` | `array` | query | Não |

---

### `[POST] /tipo-conta`
**Corpo da Requisição (JSON):** `Modelo TipoContaCreateDTO`

| Campo | Tipo | Descrição |
|---|---|---|
| `arvore` | `string` | - |
| `indice` | `string` | - |

---

### `[GET] /tipo-conta/inativos`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `page` | `integer` | query | Não |
| `size` | `integer` | query | Não |
| `sort` | `array` | query | Não |

---

### `[GET] /tipo-conta/buscar-indice`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `indice` | `string` | query | Sim |
| `page` | `integer` | query | Não |
| `size` | `integer` | query | Não |
| `sort` | `array` | query | Não |

---

### `[GET] /tipo-conta/buscar-arvore`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `arvore` | `string` | query | Sim |
| `page` | `integer` | query | Não |
| `size` | `integer` | query | Não |
| `sort` | `array` | query | Não |

---

### `[GET] /tipo-conta/ativos`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `page` | `integer` | query | Não |
| `size` | `integer` | query | Não |
| `sort` | `array` | query | Não |

---

## admin-proprietario-controller

### `[PUT] /admin/proprietarios/{id}/desativar`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

---

### `[PUT] /admin/proprietarios/{id}/ativar`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

---

### `[GET] /admin/proprietarios`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `pageable` | `string` | query | Sim |

---

## referencia-admin-controller

### `[GET] /admin/referencias/{tipo}/{id}`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `tipo` | `string` | path | Sim |
| `id` | `integer` | path | Sim |

---

### `[PUT] /admin/referencias/{tipo}/{id}`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `tipo` | `string` | path | Sim |
| `id` | `integer` | path | Sim |

**Corpo da Requisição (JSON):** `Modelo ReferenciaUpdateDTO`

| Campo | Tipo | Descrição |
|---|---|---|
| `descricao` | `string` | - |

---

### `[DELETE] /admin/referencias/{tipo}/{id}`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `tipo` | `string` | path | Sim |
| `id` | `integer` | path | Sim |

---

### `[PUT] /admin/referencias/{tipo}/{id}/reativar`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `tipo` | `string` | path | Sim |
| `id` | `integer` | path | Sim |

---

### `[GET] /admin/referencias/{tipo}`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `tipo` | `string` | path | Sim |

---

### `[POST] /admin/referencias/{tipo}`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `tipo` | `string` | path | Sim |

**Corpo da Requisição (JSON):** `Modelo ReferenciaCreateDTO`

| Campo | Tipo | Descrição |
|---|---|---|
| `descricao` | `string` | - |

---

### `[GET] /admin/referencias`
---

### `[GET] /admin/referencias/{tipo}/inativos`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `tipo` | `string` | path | Sim |

---

## referencia-controller

### `[GET] /referencias/tipo-posse-maquina`
---

### `[GET] /referencias/tipo-pagamento`
---

### `[GET] /referencias/tipo-mov-produto`
---

### `[GET] /referencias/tipo-maquina`
---

### `[GET] /referencias/tipo-gasto-maquina`
---

### `[GET] /referencias/st-cultivo`
---

### `[GET] /referencias/res-ferrugem`
---

### `[GET] /referencias/pedido-compra-status`
---

### `[GET] /referencias/grupo`
---

### `[GET] /referencias/familia`
---

### `[GET] /referencias/despesa`
---

### `[GET] /referencias/culturas`
---

### `[GET] /referencias/conta-gerencial`
---

### `[GET] /referencias/centro-custo`
---

## segmentacao-funcionario-controller

### `[POST] /segmentacao-funcionario`
**Corpo da Requisição (JSON):** `Modelo SegmentacaoFuncionarioCreateDTO`

| Campo | Tipo | Descrição |
|---|---|---|
| `funcionarioId` | `integer` | - |
| `operacaoId` | `integer` | - |
| `percentual` | `number` | - |

---

### `[GET] /segmentacao-funcionario/{funcionarioId}`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `funcionarioId` | `integer` | path | Sim |

---

## validation-controller

### `[GET] /validation/verificar-cpf/{cpf}`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `cpf` | `string` | path | Sim |

---

### `[GET] /validation/verificar-cnpj/{cnpj}`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `cnpj` | `string` | path | Sim |

---

## 🌱 Operações de Talhão

### `[GET] /operacoes-talhao/{id}`
**Descrição:** Buscar operação de talhão por ID

**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

---

### `[PUT] /operacoes-talhao/{id}`
**Descrição:** Atualizar operação de talhão

**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

**Corpo da Requisição (JSON):** `Modelo OperacaoTalhaoUpdateDTO`

| Campo | Tipo | Descrição |
|---|---|---|
| `areaTrabalhada` | `number` | - |
| `dataExecucao` | `string` | - |

---

### `[DELETE] /operacoes-talhao/{id}`
**Descrição:** Excluir operação de talhão

**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

---

### `[GET] /operacoes-talhao`
**Descrição:** Listar operações de talhão

**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `page` | `integer` | query | Não |
| `size` | `integer` | query | Não |
| `sort` | `array` | query | Não |

---

### `[POST] /operacoes-talhao`
**Descrição:** Registrar operação em talhão

**Corpo da Requisição (JSON):** `Modelo OperacaoTalhaoCreateDTO`

| Campo | Tipo | Descrição |
|---|---|---|
| `ordemServicoId` | `integer` | - |
| `safraTalhaoId` | `integer` | - |
| `areaTrabalhada` | `number` | - |
| `dataExecucao` | `string` | - |

---

## 🌱 Planejamento Insumos

### `[GET] /planejamento-operacao/{id}/insumos`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

---

### `[POST] /planejamento-operacao/{id}/insumos`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

**Corpo da Requisição (JSON):** `Modelo PlanejamentoInsumoCreateDTO`

| Campo | Tipo | Descrição |
|---|---|---|
| `produtoId` | `integer` | - |
| `dosePorHa` | `number` | - |
| `valorUnitarioPrevisto` | `number` | - |

---

### `[DELETE] /planejamento-operacao/{id}/insumos/{itemId}`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |
| `itemId` | `integer` | path | Sim |

---

## 🌱 Safra Talhoes

### `[GET] /safra-talhoes/{id}`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

---

### `[PUT] /safra-talhoes/{id}`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

**Corpo da Requisição (JSON):** `Modelo SafraTalhaoUpdateDTO`

| Campo | Tipo | Descrição |
|---|---|---|
| `culturaId` | `integer` | - |
| `resFerrugemId` | `integer` | - |
| `stCultivoId` | `integer` | - |
| `areaUtilizada` | `number` | - |
| `espRua` | `number` | - |
| `espPlanta` | `number` | - |
| `material` | `string` | - |
| `stTerra` | `string` | - |
| `vencContrato` | `string` | - |
| `irrigacao` | `boolean` | - |
| `estLitroPlanta` | `number` | - |
| `estimativaSacaHectare` | `number` | - |
| `estimativaSaca` | `number` | - |
| `producaoReal` | `number` | - |
| `precoSaca` | `number` | - |
| `ativo` | `boolean` | - |

---

### `[DELETE] /safra-talhoes/{id}`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

---

### `[PUT] /safra-talhoes/{id}/reativar`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

---

### `[GET] /safra-talhoes`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `page` | `integer` | query | Não |
| `size` | `integer` | query | Não |
| `sort` | `array` | query | Não |

---

### `[POST] /safra-talhoes`
**Corpo da Requisição (JSON):** `Modelo SafraTalhaoCreateDTO`

| Campo | Tipo | Descrição |
|---|---|---|
| `talhaoId` | `integer` | - |
| `culturaId` | `integer` | - |
| `resFerrugemId` | `integer` | - |
| `stCultivoId` | `integer` | - |
| `areaUtilizada` | `number` | - |
| `espRua` | `number` | - |
| `espPlanta` | `number` | - |
| `material` | `string` | - |
| `stTerra` | `string` | - |
| `vencContrato` | `string` | - |
| `irrigacao` | `boolean` | - |
| `estLitroPlanta` | `number` | - |
| `estimativaSacaHectare` | `number` | - |
| `estimativaSaca` | `number` | - |
| `producaoReal` | `number` | - |
| `precoSaca` | `number` | - |

---

### `[GET] /safra-talhoes/inativos`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `page` | `integer` | query | Não |
| `size` | `integer` | query | Não |
| `sort` | `array` | query | Não |

---

## 🌱 Safras

### `[GET] /safras/{id}`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

---

### `[PUT] /safras/{id}`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

**Corpo da Requisição (JSON):** `Modelo SafraCreateDTO`

| Campo | Tipo | Descrição |
|---|---|---|
| `nome` | `string` | - |
| `dataInicial` | `string` | - |
| `dataFinal` | `string` | - |
| `areaPlantada` | `number` | - |
| `orcamentoPrevisto` | `number` | - |

---

### `[DELETE] /safras/{id}`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

---

### `[GET] /safras`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `page` | `integer` | query | Não |
| `size` | `integer` | query | Não |
| `sort` | `array` | query | Não |

---

### `[POST] /safras`
**Corpo da Requisição (JSON):** `Modelo SafraCreateDTO`

| Campo | Tipo | Descrição |
|---|---|---|
| `nome` | `string` | - |
| `dataInicial` | `string` | - |
| `dataFinal` | `string` | - |
| `areaPlantada` | `number` | - |
| `orcamentoPrevisto` | `number` | - |

---

## 🌾 Talhões

### `[GET] /talhoes/{id}`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

---

### `[PUT] /talhoes/{id}`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

**Corpo da Requisição (JSON):** `Modelo TalhaoUpdateDTO`

| Campo | Tipo | Descrição |
|---|---|---|
| `area` | `number` | - |
| `espacamentoRua` | `number` | - |
| `espacamentoPlanta` | `number` | - |
| `material` | `string` | - |
| `resistenciaFerrugemId` | `integer` | - |
| `sistemaCultivoId` | `integer` | - |

---

### `[DELETE] /talhoes/{id}`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

---

### `[PUT] /talhoes/{id}/reativar`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

---

### `[GET] /talhoes`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `page` | `integer` | query | Não |
| `size` | `integer` | query | Não |
| `sort` | `array` | query | Não |

---

### `[POST] /talhoes`
**Corpo da Requisição (JSON):** `Modelo TalhaoCreateDTO`

| Campo | Tipo | Descrição |
|---|---|---|
| `nome` | `string` | - |
| `area` | `number` | - |
| `espacamentoRua` | `number` | - |
| `espacamentoPlanta` | `number` | - |
| `material` | `string` | - |
| `resistenciaFerrugemId` | `integer` | - |
| `sistemaCultivoId` | `integer` | - |

---

### `[GET] /talhoes/inativos`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `page` | `integer` | query | Não |
| `size` | `integer` | query | Não |
| `sort` | `array` | query | Não |

---

## 🏡 Contexto da Fazenda

### `[POST] /contexto/safra-ativa/{safraId}`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `safraId` | `integer` | path | Sim |

---

### `[POST] /contexto/proprietario-ativo/{id}`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

---

### `[POST] /contexto/fazenda-ativa/{fazendaId}`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `fazendaId` | `integer` | path | Sim |

---

## 👥 Usuários

### `[GET] /usuarios/{id}`
**Descrição:** Buscar usuário

**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

---

### `[PUT] /usuarios/{id}`
**Descrição:** Atualizar usuário

**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

**Corpo da Requisição (JSON):** `Modelo UsuarioUpdateDTO`

| Campo | Tipo | Descrição |
|---|---|---|
| `nome` | `string` | - |
| `email` | `string` | - |
| `perfilId` | `integer` | - |

---

### `[DELETE] /usuarios/{id}`
**Descrição:** Excluir usuário

**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

---

### `[GET] /usuarios`
**Descrição:** Listar usuários

**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `page` | `integer` | query | Não |
| `size` | `integer` | query | Não |
| `sort` | `array` | query | Não |

---

### `[POST] /usuarios`
**Descrição:** Criar usuário

**Corpo da Requisição (JSON):** `Modelo UsuarioCreateDTO`

| Campo | Tipo | Descrição |
|---|---|---|
| `nome` | `string` | - |
| `email` | `string` | - |
| `senha` | `string` | - |
| `perfilId` | `integer` | - |

---

## 💰 Financeiro

### `[PUT] /contas-pagar/{id}`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

**Corpo da Requisição (JSON):** `Modelo ContaPagarUpdateDTO`

| Campo | Tipo | Descrição |
|---|---|---|
| `favorecido` | `string` | - |
| `refDespesaId` | `integer` | - |
| `centroCustoId` | `integer` | - |
| `numeroNotaFiscal` | `string` | - |
| `dataVencimento` | `string` | - |
| `vlrReal` | `number` | - |

---

### `[DELETE] /contas-pagar/{id}`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

---

### `[GET] /contas-pagar`
**Descrição:** Listar contas a pagar

**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `page` | `integer` | query | Não |
| `size` | `integer` | query | Não |
| `sort` | `array` | query | Não |

---

### `[POST] /contas-pagar`
**Corpo da Requisição (JSON):** `Modelo ContaPagarCreateDTO`

| Campo | Tipo | Descrição |
|---|---|---|
| `favorecido` | `string` | - |
| `refDespesaId` | `integer` | - |
| `centroCustoId` | `integer` | - |
| `numeroNotaFiscal` | `string` | - |
| `dataVencimento` | `string` | - |
| `vlrReal` | `number` | - |

---

### `[POST] /contas-pagar/{id}/pagar`
**Descrição:** Pagar conta

**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

**Corpo da Requisição (JSON):** `Modelo ContaPagarPagamentoDTO`

| Campo | Tipo | Descrição |
|---|---|---|
| `dataPagamento` | `string` | - |
| `vlrJuros` | `number` | - |

---

### `[GET] /contas-pagar/fazenda`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `page` | `integer` | query | Não |
| `size` | `integer` | query | Não |
| `sort` | `array` | query | Não |

---

## 💰 Folha de Pagamento

### `[GET] /folha-pagamento/{id}`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

---

### `[PUT] /folha-pagamento/{id}`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

**Corpo da Requisição (JSON):** `Modelo FolhaPagamentoUpdateDTO`

| Campo | Tipo | Descrição |
|---|---|---|
| `salarioBase` | `number` | - |
| `encargos` | `number` | - |

---

### `[DELETE] /folha-pagamento/{id}`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

---

### `[PUT] /folha-pagamento/{id}/reativar`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

---

### `[GET] /folha-pagamento`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `page` | `integer` | query | Não |
| `size` | `integer` | query | Não |
| `sort` | `array` | query | Não |

---

### `[POST] /folha-pagamento`
**Corpo da Requisição (JSON):** `Modelo FolhaPagamentoCreateDTO`

| Campo | Tipo | Descrição |
|---|---|---|
| `funcionarioId` | `integer` | - |
| `mesAno` | `string` | - |
| `salarioBase` | `number` | - |
| `encargos` | `number` | - |

---

### `[GET] /folha-pagamento/inativos`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `page` | `integer` | query | Não |
| `size` | `integer` | query | Não |
| `sort` | `array` | query | Não |

---

## 💰 Gastos de Máquina

### `[PUT] /gastos-maquina/{id}`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

**Corpo da Requisição (JSON):** `Modelo GastoMaquinaCreateDTO`

| Campo | Tipo | Descrição |
|---|---|---|
| `tipoGastoId` | `integer` | - |
| `maquinaId` | `integer` | - |
| `funcionarioId` | `integer` | - |
| `data` | `string` | - |
| `descricao` | `string` | - |
| `valor` | `number` | - |

---

### `[DELETE] /gastos-maquina/{id}`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

---

### `[GET] /gastos-maquina`
**Descrição:** Listar gastos de máquina

**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `page` | `integer` | query | Não |
| `size` | `integer` | query | Não |
| `sort` | `array` | query | Não |

---

### `[POST] /gastos-maquina`
**Descrição:** Registrar gasto de máquina

**Corpo da Requisição (JSON):** `Modelo GastoMaquinaCreateDTO`

| Campo | Tipo | Descrição |
|---|---|---|
| `tipoGastoId` | `integer` | - |
| `maquinaId` | `integer` | - |
| `funcionarioId` | `integer` | - |
| `data` | `string` | - |
| `descricao` | `string` | - |
| `valor` | `number` | - |

---

## 📄 Ordem de Serviço

### `[GET] /ordens-servico/{id}`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

---

### `[PUT] /ordens-servico/{id}`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

**Corpo da Requisição (JSON):** `Modelo OrdemServicoUpdateDTO`

| Campo | Tipo | Descrição |
|---|---|---|
| `version` | `integer` | - |
| `planejamentoOperacaoId` | `integer` | - |
| `dataInicio` | `string` | - |
| `dataFim` | `string` | - |
| `status` | `string` | - |
| `observacao` | `string` | - |
| `custoTotal` | `number` | - |

---

### `[DELETE] /ordens-servico/{id}`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

---

### `[GET] /ordens-servico`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `page` | `integer` | query | Não |
| `size` | `integer` | query | Não |
| `sort` | `array` | query | Não |

---

### `[POST] /ordens-servico`
**Corpo da Requisição (JSON):** `Modelo OrdemServicoCreateDTO`

| Campo | Tipo | Descrição |
|---|---|---|
| `operacaoId` | `integer` | - |
| `dataInicio` | `string` | - |
| `dataFim` | `string` | - |
| `status` | `string` | - |
| `observacao` | `string` | - |
| `custoTotal` | `number` | - |

---

### `[POST] /ordens-servico/{id}/maquinas`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

**Corpo da Requisição (JSON):** `Modelo OrdemServicoMaquinasDTO`

| Campo | Tipo | Descrição |
|---|---|---|
| `maquinas` | `Array de MaquinaItem` | - |

---

### `[POST] /ordens-servico/{id}/funcionarios`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

**Corpo da Requisição (JSON):** `Modelo OrdemServicoFuncionariosDTO`

| Campo | Tipo | Descrição |
|---|---|---|
| `funcionarios` | `Array de FuncionarioItem` | - |

---

### `[POST] /ordens-servico/{id}/finalizar`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

---

### `[POST] /ordens-servico/from-planejamento`
**Descrição:** Gerar Ordem de Serviço a partir de planejamentos

**Corpo da Requisição (JSON):** `Modelo OrdemServicoFromPlanejamentoDTO`

| Campo | Tipo | Descrição |
|---|---|---|
| `planejamentoIds` | `Array de integer` | - |

---

## 📊 Administrativo

### `[PUT] /administrativo/{id}`
**Descrição:** Atualizar custo administrativo

**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

**Corpo da Requisição (JSON):** `Modelo AdministrativoUpdateDTO`

| Campo | Tipo | Descrição |
|---|---|---|
| `descricao` | `string` | - |
| `mesAno` | `string` | - |
| `un` | `string` | - |
| `contaGerencialId` | `integer` | - |
| `despesaEducampoId` | `integer` | - |
| `valorUnitPlanejado` | `number` | - |
| `quantidadePlanejada` | `integer` | - |
| `valorTotalPlanejado` | `number` | - |
| `valorHaPlanejado` | `number` | - |
| `valorUnitRealizado` | `number` | - |
| `quantidadeRealizada` | `integer` | - |
| `valorTotalRealizado` | `number` | - |
| `valorHaRealizado` | `number` | - |

---

### `[GET] /administrativo`
**Descrição:** Listar custos administrativos

**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `page` | `integer` | query | Não |
| `size` | `integer` | query | Não |
| `sort` | `array` | query | Não |

---

### `[POST] /administrativo`
**Descrição:** Criar custo administrativo

**Corpo da Requisição (JSON):** `Modelo AdministrativoCreateDTO`

| Campo | Tipo | Descrição |
|---|---|---|
| `descricao` | `string` | - |
| `mesAno` | `string` | - |
| `un` | `string` | - |
| `contaGerencialId` | `integer` | - |
| `despesaEducampoId` | `integer` | - |
| `valorUnitPlanejado` | `number` | - |
| `quantidadePlanejada` | `integer` | - |
| `valorTotalPlanejado` | `number` | - |
| `valorHaPlanejado` | `number` | - |
| `valorUnitRealizado` | `number` | - |
| `quantidadeRealizada` | `integer` | - |
| `valorTotalRealizado` | `number` | - |
| `valorHaRealizado` | `number` | - |

---

## 📊 Comparação Completa

### `[GET] /relatorios/por-talhao`
---

### `[GET] /relatorios/completa`
---

## 📊 Dashboard

### `[GET] /dashboard/safra`
---

## 📊 Planejamento

### `[GET] /planejamento-operacao`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `page` | `integer` | query | Não |
| `size` | `integer` | query | Não |
| `sort` | `array` | query | Não |

---

### `[POST] /planejamento-operacao`
**Corpo da Requisição (JSON):** `Modelo PlanejamentoOperacaoCreateDTO`

| Campo | Tipo | Descrição |
|---|---|---|
| `safraTalhaoId` | `integer` | - |
| `operacaoId` | `integer` | - |
| `dataPrevista` | `string` | - |
| `areaPlanejada` | `number` | - |
| `velocidade` | `number` | - |
| `eficiencia` | `number` | - |
| `horasPrevistas` | `number` | - |
| `dieselPrevisto` | `number` | - |
| `custoInsumos` | `number` | - |
| `custoMaquinas` | `number` | - |
| `custoCombustivel` | `number` | - |
| `custoTotal` | `number` | - |

---

### `[GET] /planejamento-operacao/{id}`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

---

### `[DELETE] /planejamento-operacao/{id}`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

---

### `[GET] /planejamento-operacao/{id}/resumo`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

---

### `[GET] /planejamento-operacao/{id}/comparativo`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

---

### `[GET] /planejamento-operacao/{id}/comparativo-detalhado`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

---

### `[GET] /planejamento-operacao/por-safra-talhao/{id}`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

---

## 📋 Planejamento Funcionários

### `[GET] /planejamento-operacao/{id}/funcionarios`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

---

### `[POST] /planejamento-operacao/{id}/funcionarios`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

**Corpo da Requisição (JSON):** `Modelo PlanejamentoFuncionarioCreateDTO`

| Campo | Tipo | Descrição |
|---|---|---|
| `tipoMaoObra` | `string` | - |
| `funcionarioId` | `integer` | - |
| `terceirizadoId` | `integer` | - |
| `turmaId` | `integer` | - |
| `quantidadePessoas` | `integer` | - |
| `horasPrevistas` | `number` | - |
| `custoHoraPrevisto` | `number` | - |
| `observacao` | `string` | - |

---

### `[DELETE] /planejamento-operacao/{id}/funcionarios/{itemId}`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |
| `itemId` | `integer` | path | Sim |

---

## 📦 Movimentação de Produtos

### `[GET] /mov-produtos`
**Descrição:** Listar movimentações de produtos

**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `page` | `integer` | query | Não |
| `size` | `integer` | query | Não |
| `sort` | `array` | query | Não |

---

### `[POST] /mov-produtos`
**Descrição:** Criar movimentação de produto

**Corpo da Requisição (JSON):** `Modelo MovProdutoRequestDTO`

| Campo | Tipo | Descrição |
|---|---|---|
| `produtoId` | `integer` | - |
| `tipoMovimentoId` | `integer` | - |
| `dataMovimento` | `string` | - |
| `qtde` | `number` | - |
| `vlrUnitario` | `number` | - |
| `numeroNotaFiscal` | `string` | - |
| `numeroOrdemServico` | `string` | - |
| `dataPagamento` | `string` | - |

---

## 🔐 Autenticação

### `[PUT] /auth/trocar-senha`
**Descrição:** Trocar senha do usuário

**Corpo da Requisição (JSON):** `Modelo TrocarSenhaDTO`

| Campo | Tipo | Descrição |
|---|---|---|
| `senhaAtual` | `string` | - |
| `novaSenha` | `string` | - |

---

### `[POST] /auth/login`
**Descrição:** Login do usuário

**Corpo da Requisição (JSON):** `Modelo LoginRequest`

| Campo | Tipo | Descrição |
|---|---|---|
| `email` | `string` | Email do usuário |
| `senha` | `string` | Senha do usuário |

---

### `[GET] /auth/me`
**Descrição:** Dados do usuário autenticado

---

## 🚜 Máquinas

### `[GET] /maquinas/{id}`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

---

### `[PUT] /maquinas/{id}`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

**Corpo da Requisição (JSON):** `Modelo MaquinaUpdateDTO`

| Campo | Tipo | Descrição |
|---|---|---|
| `nome` | `string` | - |
| `marca` | `string` | - |
| `modelo` | `string` | - |
| `descricao` | `string` | - |
| `anoFabricacao` | `integer` | - |
| `imagem` | `string` | - |
| `horimetro` | `number` | - |
| `tipoMaquinaId` | `integer` | - |
| `tipoPosseId` | `integer` | - |
| `valorDiaria` | `number` | - |
| `inicioLocacao` | `string` | - |
| `fimLocacao` | `string` | - |
| `diasContratados` | `integer` | - |
| `valorTotalLocacao` | `number` | - |

---

### `[DELETE] /maquinas/{id}`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

---

### `[PUT] /maquinas/{id}/reativar`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

---

### `[PUT] /horas-maquina/{id}`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

**Corpo da Requisição (JSON):** `Modelo HoraMaquinaCreateDTO`

| Campo | Tipo | Descrição |
|---|---|---|
| `maquinaId` | `integer` | - |
| `funcionarioId` | `integer` | - |
| `operacaoTalhaoId` | `integer` | - |
| `servicoExec` | `string` | - |
| `nroOs` | `string` | - |
| `custoHora` | `number` | - |
| `dataExecucao` | `string` | - |
| `horimetroInicial` | `number` | - |
| `horimetroFinal` | `number` | - |

---

### `[DELETE] /horas-maquina/{id}`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

---

### `[GET] /maquinas`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `page` | `integer` | query | Não |
| `size` | `integer` | query | Não |
| `sort` | `array` | query | Não |

---

### `[POST] /maquinas`
**Corpo da Requisição (JSON):** `Modelo MaquinaCreateDTO`

| Campo | Tipo | Descrição |
|---|---|---|
| `nome` | `string` | - |
| `marca` | `string` | - |
| `modelo` | `string` | - |
| `descricao` | `string` | - |
| `anoFabricacao` | `integer` | - |
| `imagem` | `string` | - |
| `horimetro` | `number` | - |
| `tipoMaquinaId` | `integer` | - |
| `tipoPosseId` | `integer` | - |
| `valorDiaria` | `number` | - |
| `inicioLocacao` | `string` | - |
| `fimLocacao` | `string` | - |
| `diasContratados` | `integer` | - |
| `valorTotalLocacao` | `number` | - |
| `ativo` | `boolean` | - |

---

### `[GET] /horas-maquina`
**Descrição:** Listar horas de máquina

**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `page` | `integer` | query | Não |
| `size` | `integer` | query | Não |
| `sort` | `array` | query | Não |

---

### `[POST] /horas-maquina`
**Descrição:** Registrar horas de máquina

**Corpo da Requisição (JSON):** `Modelo HoraMaquinaCreateDTO`

| Campo | Tipo | Descrição |
|---|---|---|
| `maquinaId` | `integer` | - |
| `funcionarioId` | `integer` | - |
| `operacaoTalhaoId` | `integer` | - |
| `servicoExec` | `string` | - |
| `nroOs` | `string` | - |
| `custoHora` | `number` | - |
| `dataExecucao` | `string` | - |
| `horimetroInicial` | `number` | - |
| `horimetroFinal` | `number` | - |

---

### `[GET] /maquinas/inativas`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `page` | `integer` | query | Não |
| `size` | `integer` | query | Não |
| `sort` | `array` | query | Não |

---

## 🚜 Planejamento Máquinas

### `[GET] /planejamento-operacao/{id}/maquinas`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

---

### `[POST] /planejamento-operacao/{id}/maquinas`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

**Corpo da Requisição (JSON):** `Modelo PlanejamentoMaquinaCreateDTO`

| Campo | Tipo | Descrição |
|---|---|---|
| `maquinaId` | `integer` | - |

---

### `[DELETE] /planejamento-operacao/{id}/maquinas/{itemId}`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |
| `itemId` | `integer` | path | Sim |

---

## 🛒 Pedido de Compra

### `[GET] /pedidos-compra/{id}`
**Descrição:** Buscar pedido por ID

**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

---

### `[PUT] /pedidos-compra/{id}`
**Descrição:** Atualizar pedido

**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

**Corpo da Requisição (JSON):** `Modelo PedidoCompraUpdateDTO`

| Campo | Tipo | Descrição |
|---|---|---|
| `data` | `string` | - |
| `statusId` | `integer` | - |
| `fornecedorNome` | `string` | - |
| `centroCustoId` | `integer` | - |
| `itens` | `Array de PedidoCompraItemDTO` | - |

---

### `[DELETE] /pedidos-compra/{id}`
**Descrição:** Inativar pedido

**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

---

### `[PUT] /pedidos-compra/{id}/reativar`
**Descrição:** Reativar pedido

**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

---

### `[GET] /pedidos-compra`
**Descrição:** Listar pedidos ATIVOS

**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `page` | `integer` | query | Não |
| `size` | `integer` | query | Não |
| `sort` | `array` | query | Não |

---

### `[POST] /pedidos-compra`
**Descrição:** Criar pedido

**Corpo da Requisição (JSON):** `Modelo PedidoCompraCreateDTO`

| Campo | Tipo | Descrição |
|---|---|---|
| `data` | `string` | - |
| `statusId` | `integer` | - |
| `fornecedorNome` | `string` | - |
| `centroCustoId` | `integer` | - |
| `itens` | `Array de PedidoCompraItemDTO` | - |

---

### `[POST] /pedidos-compra/{id}/receber`
**Descrição:** Receber pedido (gera estoque + financeiro)

**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

---

### `[POST] /pedidos-compra/{id}/aprovar`
**Descrição:** Aprovar pedido

**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

---

### `[GET] /pedidos-compra/inativos`
**Descrição:** Listar pedidos INATIVOS

**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `page` | `integer` | query | Não |
| `size` | `integer` | query | Não |
| `sort` | `array` | query | Não |

---

## 🧾 Auditoria

### `[GET] /ordens-servico/{ordemId}/auditoria`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `ordemId` | `integer` | path | Sim |

---

## 🧾 Auditoria Avançada

### `[GET] /ordens-servico/{id}/auditoria/detalhe`
**Parâmetros na URL:**
| Nome | Tipo | Onde | Obrigatório |
|---|---|---|---|
| `id` | `integer` | path | Sim |

---

