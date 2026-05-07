-- =========================
-- FAZENDAS
-- =========================
CREATE INDEX idx_fazenda_proprietario ON fazendas(proprietario_id);
CREATE INDEX idx_fazenda_safra_ativa ON fazendas(safra_ativa_id);

-- =========================
-- SAFRAS
-- =========================
CREATE INDEX idx_safra_fazenda ON safras(fazenda_id);

-- =========================
-- TALHÕES
-- =========================
CREATE INDEX idx_talhao_fazenda ON talhoes(fazenda_id);
-- =========================
-- SAFRA ↔ TALHÃO
-- =========================
CREATE INDEX idx_safra_talhao_safra_talhao
    ON safra_talhoes (safra_id, talhao_id);

-- =========================
-- FUNCIONÁRIOS
-- =========================
CREATE INDEX idx_funcionario_fazenda ON funcionarios(fazenda_id);
CREATE INDEX idx_funcionario_proprietario ON funcionarios(proprietario_id);

-- =========================
-- MÁQUINAS
-- =========================
CREATE INDEX idx_maquina_fazenda ON maquinas(fazenda_id);

-- =========================
-- HORAS DE MÁQUINA
-- =========================
CREATE INDEX idx_horas_maquinas_fazenda_data
    ON horas_maquinas (fazenda_id, data_execucao DESC);

CREATE INDEX idx_horas_maquinas_maquina_data
    ON horas_maquinas (maquina_id, data_execucao DESC);

CREATE INDEX idx_horas_maquinas_safra
    ON horas_maquinas (safra_id);

-- =========================
-- GASTOS DE MÁQUINA
-- =========================
CREATE INDEX idx_gastos_maquina_fazenda_data
    ON gastos_maquina (fazenda_id, data DESC);

CREATE INDEX idx_gastos_maquina_maquina_data
    ON gastos_maquina (maquina_id, data DESC);

-- =========================
-- MOVIMENTAÇÃO DE PRODUTOS
-- =========================
CREATE INDEX idx_mov_produto_fazenda_data
    ON mov_produtos (fazenda_id, dt_mov DESC);

CREATE INDEX idx_mov_produto_produto_data
    ON mov_produtos (produto_id, dt_mov DESC);

CREATE INDEX idx_mov_produto_safra
    ON mov_produtos (safra_id);

-- =========================
-- CONTAS A PAGAR
-- =========================
CREATE INDEX idx_conta_pagar_fazenda_vencimento
    ON contas_pagar (fazenda_id, dt_vencimento ASC);

CREATE INDEX idx_conta_pagar_safra
    ON contas_pagar (safra_id);

CREATE INDEX idx_po_safra ON planejamento_operacao(safra_id);
CREATE INDEX idx_po_fazenda ON planejamento_operacao(fazenda_id);
CREATE INDEX idx_po_talhao ON planejamento_operacao(safra_talhao_id);

CREATE INDEX idx_pi_planejamento ON planejamento_insumo(planejamento_operacao_id);

CREATE INDEX idx_pm_planejamento ON planejamento_maquina(planejamento_operacao_id);
