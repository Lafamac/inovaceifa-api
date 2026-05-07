-- ============================
-- CONTAS A PAGAR (Financeiro)
-- ============================
CREATE INDEX idx_conta_pagar_dash_fazenda_safra_data
    ON contas_pagar (fazenda_id, safra_id, dt_vencimento);

CREATE INDEX idx_conta_pagar_dash_fazenda_data
    ON contas_pagar (fazenda_id, dt_vencimento);

-- ============================
-- GASTOS DE MÁQUINA
-- ============================
CREATE INDEX idx_gastos_maquina_dash_fazenda_safra_data
    ON gastos_maquina (fazenda_id, safra_id, data);

CREATE INDEX idx_gastos_maquina_dash_maquina_data
    ON gastos_maquina (maquina_id, data);

-- ============================
-- HORAS DE MÁQUINA
-- ============================
CREATE INDEX idx_horas_maquinas_dash_fazenda_safra_data
    ON horas_maquinas (fazenda_id, safra_id, data_execucao);

CREATE INDEX idx_horas_maquinas_dash_maquina_data
    ON horas_maquinas (maquina_id, data_execucao);

-- ============================
-- MOVIMENTAÇÃO DE PRODUTOS
-- ============================
CREATE INDEX idx_mov_produtos_dash_fazenda_safra_data
    ON mov_produtos (fazenda_id, safra_id, dt_mov);

CREATE INDEX idx_mov_produtos_dash_produto_data
    ON mov_produtos (produto_id, dt_mov);

-- ============================
-- SAFRA ↔ TALHÃO (produtividade)
-- ============================
CREATE INDEX idx_safra_talhoes_dash_safra
    ON safra_talhoes (safra_id);

CREATE INDEX idx_safra_talhoes_dash_cultura
    ON safra_talhoes (cultura_id);

CREATE INDEX idx_contapagar_centrocusto
    ON contas_pagar (centro_custo_id);

CREATE INDEX idx_lancamento_centrocusto
    ON lancamento_despesa (centro_custo_id);
