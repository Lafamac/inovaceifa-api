CREATE TABLE planejamento_operacao (
                                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                       proprietario_id BIGINT NOT NULL,
                                       fazenda_id BIGINT NOT NULL,
                                       safra_id BIGINT NOT NULL,
                                       safra_talhao_id BIGINT NOT NULL,
                                       operacao_id BIGINT NOT NULL,
                                       data_prevista DATE,
                                       area_planejada DECIMAL(10,2) NOT NULL,
                                       velocidade DECIMAL(10,2),
                                       eficiencia DECIMAL(10,2),
                                       horas_previstas DECIMAL(10,2),
                                       diesel_previsto DECIMAL(10,2),
                                       custo_insumos DECIMAL(15,2),
                                       custo_maquinas DECIMAL(15,2),
                                       custo_combustivel DECIMAL(15,2),
                                       custo_total DECIMAL(15,2),
                                       status VARCHAR(20) DEFAULT 'PLANEJADO',
                                       ativo TINYINT(1) DEFAULT 1,
                                       FOREIGN KEY (proprietario_id) REFERENCES proprietarios(id),
                                       FOREIGN KEY (fazenda_id) REFERENCES fazendas(id),
                                       FOREIGN KEY (safra_id) REFERENCES safras(id),
                                       FOREIGN KEY (safra_talhao_id) REFERENCES safra_talhoes(id),
                                       FOREIGN KEY (operacao_id) REFERENCES cadastro_operacao(id),
                                       CONSTRAINT unique_safra_talhao UNIQUE (safra_talhao_id)
);

CREATE TABLE planejamento_insumo (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     planejamento_operacao_id BIGINT NOT NULL,
                                     produto_id BIGINT NOT NULL,
                                     dose_por_ha DECIMAL(10,4) NOT NULL,
                                     quantidade_total DECIMAL(15,4),
                                     valor_unitario_previsto DECIMAL(15,4),
                                     valor_total_previsto DECIMAL(15,4),
                                     ativo TINYINT(1) DEFAULT 1,
                                     CONSTRAINT fk_pi_planejamento_operacao
                                         FOREIGN KEY (planejamento_operacao_id) REFERENCES planejamento_operacao(id) ON DELETE CASCADE,
                                     CONSTRAINT fk_pi_produto
                                         FOREIGN KEY (produto_id) REFERENCES produtos(id)
);

CREATE TABLE planejamento_maquina (
                                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                      planejamento_operacao_id BIGINT NOT NULL,
                                      maquina_id BIGINT NOT NULL,
                                      horas_previstas DECIMAL(10,2),
                                      custo_hora DECIMAL(15,2),
                                      custo_total DECIMAL(15,2),
                                      ativo TINYINT(1) DEFAULT 1,
                                      FOREIGN KEY (planejamento_operacao_id) REFERENCES planejamento_operacao(id),
                                      FOREIGN KEY (maquina_id) REFERENCES maquinas(id)
);

CREATE TABLE ordem_servico (
                               id BIGINT AUTO_INCREMENT PRIMARY KEY,
                               proprietario_id BIGINT NOT NULL,
                               fazenda_id BIGINT NOT NULL,
                               safra_id BIGINT NOT NULL,
                               nr_os VARCHAR(20),
                               operacao_id BIGINT NOT NULL,
                               planejamento_operacao_id BIGINT,
                               data_inicio DATE,
                               data_fim DATE,
                               status VARCHAR(20),
                               version BIGINT NOT NULL DEFAULT 0,
                               observacao VARCHAR(255),
                               custo_total DECIMAL(15,2),
                               FOREIGN KEY (proprietario_id) REFERENCES proprietarios(id),
                               FOREIGN KEY (fazenda_id) REFERENCES fazendas(id),
                               FOREIGN KEY (safra_id) REFERENCES safras(id),
                               FOREIGN KEY (operacao_id) REFERENCES cadastro_operacao(id),
                               CONSTRAINT fk_os_planejamento_operacao
                                   FOREIGN KEY (planejamento_operacao_id)
                                       REFERENCES planejamento_operacao(id)
);




CREATE TABLE operacao_talhao (
                                 id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                 proprietario_id BIGINT NOT NULL,
                                 fazenda_id BIGINT NOT NULL,
                                 safra_id BIGINT NOT NULL,
                                 ordem_servico_id BIGINT NOT NULL,
                                 safra_talhao_id BIGINT NOT NULL,
                                 operacao_id BIGINT,
                                 operacao_talhao_tipo_id BIGINT,
                                 area_trabalhada DECIMAL(10,2),
                                 custo_insumos DECIMAL(15,2),
                                 custo_combustivel DECIMAL(15,2),
                                 custo_total DECIMAL(15,2),
                                 custo_mao_obra NUMERIC(15,2),
                                 custo_terceiros NUMERIC(15,2),
                                 custo_maquina NUMERIC(15,2),
                                 data_execucao DATE,
                                 CONSTRAINT fk_operacao_talhao_proprietario
                                     FOREIGN KEY (proprietario_id) REFERENCES proprietarios(id),
                                 CONSTRAINT fk_operacao_talhao_fazenda
                                     FOREIGN KEY (fazenda_id) REFERENCES fazendas(id),
                                 CONSTRAINT fk_operacao_talhao_safra
                                     FOREIGN KEY (safra_id) REFERENCES safras(id),
                                 CONSTRAINT fk_operacao_talhao_ordem
                                     FOREIGN KEY (ordem_servico_id) REFERENCES ordem_servico(id)
                                         ON DELETE CASCADE,
                                 CONSTRAINT fk_operacao_talhao_operacao
                                     FOREIGN KEY (operacao_id)
                                         REFERENCES cadastro_operacao(id),
                                 CONSTRAINT fk_operacao_talhao_tipo
                                     FOREIGN KEY (operacao_talhao_tipo_id)
                                         REFERENCES ref_operacao_talhao(id),
                                 CONSTRAINT fk_operacao_talhao_safra_talhao
                                     FOREIGN KEY (safra_talhao_id) REFERENCES safra_talhoes(id)
                                         ON DELETE CASCADE

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE operacao_produto (

                                  id BIGINT AUTO_INCREMENT PRIMARY KEY,

                                  proprietario_id BIGINT NOT NULL,
                                  fazenda_id BIGINT NOT NULL,
                                  safra_id BIGINT NOT NULL,

                                  operacao_talhao_id BIGINT NOT NULL,
                                  produto_id BIGINT NOT NULL,

                                  quantidade DECIMAL(15,2),
                                  vlr_unitario DECIMAL(15,2),
                                  vlr_total DECIMAL(15,2),

                                  FOREIGN KEY (proprietario_id) REFERENCES proprietarios(id),
                                  FOREIGN KEY (fazenda_id) REFERENCES fazendas(id),
                                  FOREIGN KEY (safra_id) REFERENCES safras(id),

                                  FOREIGN KEY (operacao_talhao_id) REFERENCES operacao_talhao(id),
                                  FOREIGN KEY (produto_id) REFERENCES produtos(id)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE operacao_funcionario (

                                      id BIGINT AUTO_INCREMENT PRIMARY KEY,

                                      proprietario_id BIGINT NOT NULL,
                                      fazenda_id BIGINT NOT NULL,
                                      safra_id BIGINT NOT NULL,

                                      operacao_talhao_id BIGINT NOT NULL,
                                      funcionario_id BIGINT NOT NULL,

                                      horas_trabalhadas DECIMAL(10,2),

                                      FOREIGN KEY (proprietario_id) REFERENCES proprietarios(id),
                                      FOREIGN KEY (fazenda_id) REFERENCES fazendas(id),
                                      FOREIGN KEY (safra_id) REFERENCES safras(id),
                                      FOREIGN KEY (operacao_talhao_id) REFERENCES operacao_talhao(id),
                                      FOREIGN KEY (funcionario_id) REFERENCES funcionarios(id)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE operacao_combustivel (

                                      id BIGINT AUTO_INCREMENT PRIMARY KEY,

                                      proprietario_id BIGINT NOT NULL,
                                      fazenda_id BIGINT NOT NULL,
                                      safra_id BIGINT NOT NULL,
                                      valor_unitario DECIMAL(10,2),
                                      operacao_talhao_id BIGINT NOT NULL,
                                      maquina_id BIGINT NOT NULL,
                                      litros DECIMAL(10,2),
                                      FOREIGN KEY (proprietario_id) REFERENCES proprietarios(id),
                                      FOREIGN KEY (fazenda_id) REFERENCES fazendas(id),
                                      FOREIGN KEY (safra_id) REFERENCES safras(id),
                                      FOREIGN KEY (operacao_talhao_id) REFERENCES operacao_talhao(id),
                                      FOREIGN KEY (maquina_id) REFERENCES maquinas(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE turmas_terceirizadas (
                                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                      nome VARCHAR(150) NOT NULL,
                                      responsavel VARCHAR(150),
                                      tipo_pagamento_id BIGINT NOT NULL,
                                      operacao_id BIGINT,
                                      operacao_talhao_id BIGINT,
                                      valor_diaria DECIMAL(15,2),
                                      valor_por_saca DECIMAL(15,2),
                                      quantidade_pessoas INT NOT NULL,
                                      data_inicio DATE,
                                      data_fim DATE,
                                      ativo TINYINT(1) DEFAULT 1,
                                      proprietario_id BIGINT NOT NULL,
                                      fazenda_id BIGINT NOT NULL,
                                      safra_id BIGINT NOT NULL,
                                      CONSTRAINT fk_turma_operacao
                                          FOREIGN KEY (operacao_id) REFERENCES cadastro_operacao(id),
                                      FOREIGN KEY (proprietario_id) REFERENCES proprietarios(id),
                                      FOREIGN KEY (fazenda_id) REFERENCES fazendas(id),
                                      FOREIGN KEY (tipo_pagamento_id) REFERENCES ref_tipo_pagamento(id),
                                      FOREIGN KEY (safra_id) REFERENCES safras(id),
                                      CONSTRAINT fk_terceirizada_operacao
                                          FOREIGN KEY (operacao_talhao_id)
                                              REFERENCES operacao_talhao(id)
);

CREATE TABLE horas_maquinas (
                                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                maquina_id BIGINT NOT NULL,
                                safra_id BIGINT NOT NULL,
                                fazenda_id BIGINT NOT NULL,
                                funcionario_id BIGINT,
                                data_execucao DATE NOT NULL,
                                servico_exec VARCHAR(200),
                                nro_os VARCHAR(50),
                                custo_hora DECIMAL(10,2),
                                operacao_talhao_id BIGINT,
                                horimetro_inicial DECIMAL(10,2) NOT NULL,
                                horimetro_final DECIMAL(10,2) NOT NULL,
                                horas_trabalhadas DECIMAL(10,2) NOT NULL,
                                FOREIGN KEY (maquina_id) REFERENCES maquinas(id),
                                FOREIGN KEY (safra_id) REFERENCES safras(id),
                                FOREIGN KEY (fazenda_id) REFERENCES fazendas(id),
                                FOREIGN KEY (operacao_talhao_id)  REFERENCES operacao_talhao(id),
                                FOREIGN KEY (funcionario_id) REFERENCES funcionarios(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE gastos_maquina (
                                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                data DATE NOT NULL,
                                tipo_gasto_id BIGINT NOT NULL,
                                maquina_id BIGINT NOT NULL,
                                fazenda_id BIGINT NOT NULL,
                                safra_id BIGINT NOT NULL,
                                funcionario_id BIGINT,
                                descricao VARCHAR(255),
                                vlr_usado DECIMAL(10,2) NOT NULL,
                                FOREIGN KEY (tipo_gasto_id) REFERENCES ref_tipo_gasto_maquina(id),
                                FOREIGN KEY (maquina_id) REFERENCES maquinas(id),
                                FOREIGN KEY (fazenda_id) REFERENCES fazendas(id),
                                FOREIGN KEY (safra_id) REFERENCES safras(id),
                                FOREIGN KEY (funcionario_id) REFERENCES funcionarios(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE auditoria_ordem_servico (
                                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                         ordem_servico_id BIGINT,
                                         usuario_id BIGINT,
                                         acao VARCHAR(20), -- CREATE, UPDATE, DELETE
                                         dados_antes TEXT,
                                         dados_depois TEXT,
                                         data_evento TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE planejamento_funcionario (
                                          id BIGINT AUTO_INCREMENT PRIMARY KEY,

                                          planejamento_operacao_id BIGINT NOT NULL,

                                          funcionario_id BIGINT NULL,
                                          terceirizado_id BIGINT NULL,
                                          turma_id BIGINT NULL,
                                          tipo_mao_obra VARCHAR(20) NOT NULL,
                                          quantidade_pessoas BIGINT DEFAULT 1,
                                          horas_previstas DECIMAL(10,2),
                                          custo_hora_previsto DECIMAL(15,2),
                                          custo_total_previsto DECIMAL(15,2),
                                          observacao VARCHAR(255),
                                          ativo TINYINT(1) DEFAULT 1,
                                          criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                          CONSTRAINT fk_pf_planejamento_operacao
                                              FOREIGN KEY (planejamento_operacao_id)
                                                  REFERENCES planejamento_operacao(id)
                                                  ON DELETE CASCADE,

                                          CONSTRAINT fk_pf_funcionario
                                              FOREIGN KEY (funcionario_id)
                                                  REFERENCES funcionarios(id),

                                          CONSTRAINT fk_pf_terceirizado
                                              FOREIGN KEY (terceirizado_id)
                                                  REFERENCES terceirizados(id),

                                          CONSTRAINT fk_pf_turma
                                              FOREIGN KEY (turma_id)
                                                  REFERENCES turmas_terceirizadas(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE pedido_compra (
                               id BIGINT AUTO_INCREMENT PRIMARY KEY,
                               proprietario_id BIGINT NOT NULL,
                               fazenda_id BIGINT NOT NULL,
                               safra_id BIGINT NOT NULL,
                               data DATE,
                               status_id BIGINT NOT NULL,
                               valor_total DECIMAL(15,2),
                               fornecedor_nome VARCHAR(255),
                               centro_custo_id BIGINT,
                               ativo TINYINT(1) DEFAULT 1,
                               CONSTRAINT fk_pedido_compra_fazenda
                                   FOREIGN KEY (fazenda_id) REFERENCES fazendas(id),
                               CONSTRAINT fk_pedido_compra_safra
                                   FOREIGN KEY (safra_id) REFERENCES safras(id),
                               CONSTRAINT fk_pedido_centro_custo
                                   FOREIGN KEY (centro_custo_id) REFERENCES ref_centro_custo(id),
                               CONSTRAINT fk_pedido_compra_proprietario
                                   FOREIGN KEY (proprietario_id) REFERENCES proprietarios(id),
                               CONSTRAINT fk_pedido_compra_status
                                   FOREIGN KEY (status_id) REFERENCES ref_pedido_compra_status(id)
);

CREATE TABLE pedido_compra_item (
                                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                    pedido_compra_id BIGINT NOT NULL,
                                    produto_id BIGINT NOT NULL,
                                    quantidade DECIMAL(15,4) NOT NULL,
                                    valor_unitario DECIMAL(15,4) NOT NULL,
                                    valor_total DECIMAL(15,4) NOT NULL,
                                     CONSTRAINT fk_pedido_item_pedido
                                        FOREIGN KEY (pedido_compra_id)
                                            REFERENCES pedido_compra(id)
                                            ON DELETE CASCADE,
                                    CONSTRAINT fk_pedido_item_produto
                                        FOREIGN KEY (produto_id)
                                            REFERENCES produtos(id)
);

CREATE TABLE folha_pagamento (
                                 id BIGINT AUTO_INCREMENT PRIMARY KEY,

                                 proprietario_id BIGINT NOT NULL,
                                 fazenda_id BIGINT NOT NULL,
                                 safra_id BIGINT NOT NULL,
                                 funcionario_id BIGINT NOT NULL,
                                 mes_ano VARCHAR(7) NOT NULL, -- 2025-01
                                 salario_base DECIMAL(15,2) NOT NULL,
                                 encargos DECIMAL(15,2),
                                 total DECIMAL(15,2) NOT NULL,
                                 ativo TINYINT(1) DEFAULT 1,
                                 CONSTRAINT fk_fp_proprietario FOREIGN KEY (proprietario_id) REFERENCES proprietarios(id),
                                 CONSTRAINT fk_fp_fazenda FOREIGN KEY (fazenda_id) REFERENCES fazendas(id),
                                 CONSTRAINT fk_fp_safra FOREIGN KEY (safra_id) REFERENCES safras(id),
                                 CONSTRAINT fk_fp_funcionario FOREIGN KEY (funcionario_id) REFERENCES funcionarios(id)
);

CREATE TABLE segmentacao_funcionario (
                                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                         funcionario_id BIGINT NOT NULL,
                                         operacao_id BIGINT NOT NULL,
                                         percentual DECIMAL(5,2) NOT NULL,
                                         proprietario_id BIGINT NOT NULL,
                                         fazenda_id BIGINT NOT NULL,
                                         safra_id BIGINT NOT NULL,
                                         ativo TINYINT(1) DEFAULT 1,
                                         CONSTRAINT fk_seg_funcionario
                                             FOREIGN KEY (funcionario_id) REFERENCES funcionarios(id),
                                         CONSTRAINT fk_seg_operacao
                                             FOREIGN KEY (operacao_id) REFERENCES cadastro_operacao(id),
                                         CONSTRAINT fk_seg_proprietario
                                             FOREIGN KEY (proprietario_id) REFERENCES proprietarios(id),
                                         CONSTRAINT fk_seg_fazenda
                                             FOREIGN KEY (fazenda_id) REFERENCES fazendas(id),
                                         CONSTRAINT fk_seg_safra
                                             FOREIGN KEY (safra_id) REFERENCES safras(id)
);

CREATE TABLE apontamento_turma (
                                   id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                   turma_id BIGINT NOT NULL,
                                   fazenda_id BIGINT NOT NULL,
                                   safra_id BIGINT NOT NULL,
                                   ordem_servico_id BIGINT,
                                   data_inicio DATE,
                                   data_fim DATE,
                                   dias_trabalhados INT,
                                   quantidade_colhida DECIMAL(10,2),
                                   valor_total DECIMAL(15,2),
                                   observacao VARCHAR(255),

                                   FOREIGN KEY (turma_id) REFERENCES turmas_terceirizadas(id),
                                   FOREIGN KEY (fazenda_id) REFERENCES fazendas(id),

                                   CONSTRAINT fk_apontamento_os
                                       FOREIGN KEY (ordem_servico_id) REFERENCES ordem_servico(id),

                                   FOREIGN KEY (safra_id) REFERENCES safras(id)
);

CREATE TABLE venda_producao (
                                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                proprietario_id BIGINT NOT NULL,
                                fazenda_id BIGINT NOT NULL,
                                safra_id BIGINT NOT NULL,
                                safra_talhao_id BIGINT NOT NULL,
                                quantidade DECIMAL(15,2) NOT NULL,
                                preco_unitario DECIMAL(15,2) NOT NULL,
                                valor_total DECIMAL(15,2),
                                data_venda DATE NOT NULL,
                                CONSTRAINT fk_venda_proprietario
                                    FOREIGN KEY (proprietario_id) REFERENCES proprietarios(id),
                                CONSTRAINT fk_venda_fazenda
                                    FOREIGN KEY (fazenda_id) REFERENCES fazendas(id),
                                CONSTRAINT fk_venda_safra
                                    FOREIGN KEY (safra_id) REFERENCES safras(id),
                                CONSTRAINT fk_venda_safra_talhao
                                    FOREIGN KEY (safra_talhao_id) REFERENCES safra_talhoes(id)

) ENGINE=InnoDB;


