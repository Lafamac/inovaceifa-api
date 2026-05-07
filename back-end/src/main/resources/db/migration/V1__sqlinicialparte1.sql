-- ============================================
-- TABELAS DE REFERÊNCIA (INT)
-- ============================================

CREATE TABLE tab_ref_usuario (
                                 id BIGINT PRIMARY KEY,
                                 descricao VARCHAR(50) NOT NULL,
                                 ativo TINYINT(1) DEFAULT 1
);


CREATE TABLE ref_centro_custo (
                                  id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                  descricao VARCHAR(150) NOT NULL,
                                  ativo TINYINT(1) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE ref_grupo (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           descricao VARCHAR(50) NOT NULL,
                           ativo TINYINT(1) DEFAULT 1
);

CREATE TABLE ref_familia (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             descricao VARCHAR(50) NOT NULL,
                             ativo TINYINT(1) DEFAULT 1
);

CREATE TABLE ref_tipo_posse_maquina (
                                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                        descricao VARCHAR(255) NOT NULL,
                                        ativo TINYINT(1) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE tab_ref_despesa (
                                 id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                 descricao VARCHAR(50) NOT NULL,
                                 ativo TINYINT(1) DEFAULT 1
);

CREATE TABLE ref_cultura (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             descricao VARCHAR(100),
                             ativo TINYINT(1) DEFAULT 1
);

CREATE TABLE ref_res_ferrugem (
                                  id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                  descricao VARCHAR(100),
                                  ativo TINYINT(1) DEFAULT 1
);


CREATE TABLE ref_st_cultivo (
                                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                descricao VARCHAR(100),
                                ativo TINYINT(1) DEFAULT 1
);


CREATE TABLE ref_tipo_maquina (
                                  id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                  descricao VARCHAR(100),
                                  ativo TINYINT(1) DEFAULT 1
);



CREATE TABLE ref_tipo_gasto_maquina (
                                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                        descricao VARCHAR(100),
                                        ativo TINYINT(1) DEFAULT 1
);


CREATE TABLE ref_role_acesso (
                                 id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                 descricao VARCHAR(100),
                                 ativo TINYINT(1) DEFAULT 1
);

CREATE TABLE ref_operacao_talhao (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     descricao VARCHAR(150) NOT NULL,
                                     ativo TINYINT(1) DEFAULT 1
) ENGINE=InnoDB;

-- Tipo movimentação produto
CREATE TABLE ref_tipo_mov_produto (
                                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                      descricao VARCHAR(100),
                                      ativo TINYINT(1) DEFAULT 1
);

CREATE TABLE ref_pedido_compra_status (
                                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                          descricao VARCHAR(50) NOT NULL,
                                          ativo TINYINT(1) DEFAULT 1
);



CREATE TABLE   tipo_conta  (
                               id BIGINT AUTO_INCREMENT PRIMARY KEY,
                               arvore  varchar(255)  DEFAULT NULL,
                               indice  varchar(150) DEFAULT NULL,
                               ativo TINYINT(1) DEFAULT 1

);

CREATE TABLE conta_gerencial (
                                 id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                 descricao varchar(50) DEFAULT NULL,
                                 ativo TINYINT(1) DEFAULT 1
);

CREATE TABLE ref_tipo_pagamento (
                                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                    descricao VARCHAR(100) NOT NULL,
                                    ativo TINYINT(1) DEFAULT 1
);

CREATE TABLE ref_tipo_rateio (
                                 id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                 codigo VARCHAR(20) NOT NULL UNIQUE,
                                 descricao VARCHAR(100) NOT NULL,
                                 ativo BOOLEAN DEFAULT TRUE
);

CREATE TABLE   cadastro_operacao  (
                                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                      cod_oper  BIGINT DEFAULT NULL,
                                      cultura  varchar(50) DEFAULT NULL,
                                      operacao  varchar(100) DEFAULT NULL,
                                      modalidade  varchar(100) DEFAULT NULL,
                                      deslocamento  varchar(100) DEFAULT NULL,
                                      atividade  varchar(100) DEFAULT NULL,
                                      faixa_nominal  decimal(20,6) DEFAULT NULL,
                                      velocidade_op  decimal(20,6) DEFAULT NULL,
                                      eficiencia_campo  decimal(20,6) DEFAULT NULL,
                                      gasto_diesel  decimal(20,6) DEFAULT NULL,
                                      custo_hora_maquina DECIMAL(15,2),
                                      ativo TINYINT(1) DEFAULT 1

);


CREATE TABLE usuarios (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          nome VARCHAR(100) NOT NULL,
                          email VARCHAR(100) NOT NULL UNIQUE,
                          senha VARCHAR(255) NOT NULL,
                          perfil_id BIGINT NOT NULL,
                          criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          CONSTRAINT fk_usuario_perfil
                              FOREIGN KEY (perfil_id) REFERENCES tab_ref_usuario(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE proprietarios (
                               id BIGINT AUTO_INCREMENT PRIMARY KEY,
                               usuario_id BIGINT NOT NULL UNIQUE,
                               nome VARCHAR(255) NOT NULL,
                               cpf CHAR(20) NOT NULL UNIQUE,
                               celular VARCHAR(50),
                               endereco VARCHAR(255),
                               bairro VARCHAR(200),
                               email VARCHAR(150),
                               cidade VARCHAR(200),
                               estado CHAR(2),
                               ativo TINYINT(1) DEFAULT 1,
                               CONSTRAINT fk_prop_usuario
                                   FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE fazendas (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          proprietario_id BIGINT NOT NULL,
                          nome VARCHAR(255) NOT NULL,
                          cnpj CHAR(20) NOT NULL UNIQUE,
                          endereco VARCHAR(255),
                          cidade VARCHAR(200),
                          estado CHAR(2),
                          ativo TINYINT(1) DEFAULT 1,
                          CONSTRAINT fk_fazenda_prop
                              FOREIGN KEY (proprietario_id) REFERENCES proprietarios(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE safras (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        fazenda_id BIGINT NOT NULL,
                        nome VARCHAR(20) NOT NULL,
                        dt_inicial DATE NOT NULL,
                        dt_final DATE NOT NULL,
                        area_plantada DECIMAL(15,2) NULL,
                        orcamento_previsto DECIMAL(15,2) NULL,

                        CONSTRAINT fk_safra_fazenda
                            FOREIGN KEY (fazenda_id) REFERENCES fazendas(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE funcionarios (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              usuario_id BIGINT UNIQUE,
                              proprietario_id BIGINT NOT NULL,
                              fazenda_id BIGINT NOT NULL,
                              nome VARCHAR(255) NOT NULL,
                              cpf CHAR(11) NOT NULL UNIQUE,
                              endereco VARCHAR(255),
                              bairro VARCHAR(200),
                              cidade VARCHAR(200),
                              estado CHAR(2),
                              email VARCHAR(200),
                              celular VARCHAR(50),
                              imagem VARCHAR(255),
                              cargo VARCHAR(50),
                              salario DECIMAL(15,2),
                              dt_admissao  DATE,
                              ativo TINYINT(1) DEFAULT 1,
                              CONSTRAINT fk_func_usuario
                                  FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE SET NULL,
                              CONSTRAINT fk_func_prop
                                  FOREIGN KEY (proprietario_id) REFERENCES proprietarios(id) ON DELETE CASCADE,
                              CONSTRAINT fk_func_fazenda
                                  FOREIGN KEY (fazenda_id) REFERENCES fazendas(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE lancamento_despesa (
                                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                    proprietario_id BIGINT NOT NULL,
                                    fazenda_id BIGINT NOT NULL,
                                    safra_id BIGINT NOT NULL,
                                    ref_despesa_id BIGINT NOT NULL,
                                    ordem_servico_id BIGINT NULL,
                                    valor DECIMAL(15,2) NOT NULL,
                                    data DATE NOT NULL,
                                    origem VARCHAR(30) NOT NULL, -- MANUAL ou ORDEM_SERVICO
                                    observacao VARCHAR(255),
                                    status_pagamento VARCHAR(20) DEFAULT 'PENDENTE',
                                    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                    centro_custo_id BIGINT,
                                    ativo TINYINT(1) DEFAULT 1,
                                    CONSTRAINT fk_ld_prop FOREIGN KEY (proprietario_id) REFERENCES proprietarios(id),
                                    CONSTRAINT fk_ld_faz FOREIGN KEY (fazenda_id) REFERENCES fazendas(id),
                                    CONSTRAINT fk_ld_safra FOREIGN KEY (safra_id) REFERENCES safras(id),
                                    CONSTRAINT fk_lancamento_centro_custo FOREIGN KEY (centro_custo_id)
                                            REFERENCES ref_centro_custo(id),
                                    CONSTRAINT fk_ld_ref FOREIGN KEY (ref_despesa_id) REFERENCES tab_ref_despesa(id)
);


CREATE TABLE terceirizados (
                               id BIGINT AUTO_INCREMENT PRIMARY KEY,
                               proprietario_id BIGINT NOT NULL,
                               fazenda_id BIGINT NOT NULL,
                               nome VARCHAR(255) NOT NULL,
                               cpf CHAR(11) NOT NULL UNIQUE,
                               endereco VARCHAR(255),
                               bairro VARCHAR(200),
                               cidade VARCHAR(200),
                               estado CHAR(2),
                               email VARCHAR(200),
                               celular VARCHAR(50),
                               imagem VARCHAR(255),
                               cargo VARCHAR(50),
                               salario DECIMAL(15,2),
                               ativo TINYINT(1) DEFAULT 1,
                               CONSTRAINT fk_terc_prop
                                   FOREIGN KEY (proprietario_id) REFERENCES proprietarios(id) ON DELETE CASCADE,
                               CONSTRAINT fk_terc_fazenda
                                   FOREIGN KEY (fazenda_id) REFERENCES fazendas(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE administrativo (
                                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                descricao VARCHAR(150) NOT NULL,
                                fazenda_id BIGINT NOT NULL,
                                safra_id BIGINT NOT NULL,
                                mes_ano VARCHAR(7),
                                un VARCHAR(5),
                                tipo_rateio_id BIGINT NOT NULL,
                                conta_gerencial_id BIGINT NOT NULL,
                                despesa_educampo_id BIGINT NOT NULL,
                                vlr_unit_planejado DECIMAL(15,2),
                                qtd_planejado BIGINT,
                                vlr_total_planejado DECIMAL(15,2),
                                vlr_ha_planejado DECIMAL(15,2),
                                vlr_unit_realizado DECIMAL(15,2),
                                qtd_realizada BIGINT,
                                vlr_total_realizado DECIMAL(15,2),
                                vlr_ha_realizado DECIMAL(15,2),
                                CONSTRAINT fk_adm_fazenda
                                    FOREIGN KEY (fazenda_id) REFERENCES fazendas(id),
                                CONSTRAINT fk_adm_safra
                                    FOREIGN KEY (safra_id) REFERENCES safras(id),
                                CONSTRAINT fk_adm_conta
                                    FOREIGN KEY (conta_gerencial_id) REFERENCES conta_gerencial(id),
                                CONSTRAINT fk_adm_despesa
                                    FOREIGN KEY (despesa_educampo_id) REFERENCES tab_ref_despesa(id),
                                CONSTRAINT fk_adm_tipo_rateio
                                    FOREIGN KEY (tipo_rateio_id)
                                        REFERENCES ref_tipo_rateio(id)
) ENGINE=InnoDB;


CREATE TABLE contas_pagar (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              favorecido VARCHAR(200) NOT NULL,

                              fazenda_id BIGINT NOT NULL,
                              safra_id BIGINT NOT NULL,
                              ref_despesa_id BIGINT NOT NULL,

                              n_nota_fiscal VARCHAR(30),
                              dt_vencimento DATE,
                              dt_pagamento DATE,

                              vlr_real DECIMAL(15,2) DEFAULT 0.00,
                              vlr_juros DECIMAL(15,2) DEFAULT 0.00,
                              vlr_pago DECIMAL(15,2),
                              baixada CHAR(1) DEFAULT 'N',
                              centro_custo_id BIGINT,

                              CONSTRAINT fk_cp_fazenda
                                  FOREIGN KEY (fazenda_id) REFERENCES fazendas(id),

                              CONSTRAINT fk_cp_safra
                                  FOREIGN KEY (safra_id) REFERENCES safras(id),

                              CONSTRAINT fk_contapagar_centro_custo
                                  FOREIGN KEY (centro_custo_id)
                                      REFERENCES ref_centro_custo(id),

                              CONSTRAINT fk_cp_ref_despesa
                                  FOREIGN KEY (ref_despesa_id) REFERENCES tab_ref_despesa(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE maquinas (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          fazenda_id BIGINT NOT NULL,
                          nome VARCHAR(255) NOT NULL,
                          marca VARCHAR(100),
                          modelo VARCHAR(100),
                          descricao VARCHAR(255),
                          ano_fabricacao BIGINT,
                          imagem VARCHAR(255),
                          horimetro DECIMAL(10,2),
                          tipo_maquina_id BIGINT NOT NULL,
                          tipo_posse_id BIGINT NOT NULL,
                          valor_diaria DECIMAL(15,2) NULL,
                          inicio_locacao DATE NULL,
                          fim_locacao DATE NULL,
                          dias_contratados BIGINT NULL,
                          valor_total_locacao DECIMAL(15,2) NULL,
                          ativo TINYINT(1) DEFAULT 1,
                          CONSTRAINT fk_maquina_fazenda
                              FOREIGN KEY (fazenda_id) REFERENCES fazendas(id),
                          CONSTRAINT fk_maquina_tipo
                              FOREIGN KEY (tipo_maquina_id) REFERENCES ref_tipo_maquina(id),
                          CONSTRAINT fk_maquina_tipo_posse
                              FOREIGN KEY (tipo_posse_id) REFERENCES ref_tipo_posse_maquina(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE produtos (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          nome VARCHAR(150) NOT NULL,
                          fazenda_id BIGINT NOT NULL,
                          codigo VARCHAR(13),
                          unidade VARCHAR(30),
                          grupo_id BIGINT NOT NULL,
                          familia_id BIGINT NOT NULL,
                          ativo_nutr VARCHAR(50),
                          preco_custo DECIMAL(15,4),
                          qtde DECIMAL(15,2),
                          vlr_unitario DECIMAL(15,2) DEFAULT 0.00,
                          vlr_total DECIMAL(15,2) DEFAULT 0.00,
                          ativo TINYINT(1) DEFAULT 1,
                          CONSTRAINT fk_fazenda_produto FOREIGN KEY (fazenda_id) REFERENCES fazendas(id),
                          CONSTRAINT fk_grupo_produto FOREIGN KEY (grupo_id) REFERENCES ref_grupo(id),
                          CONSTRAINT fk_familia_produto FOREIGN KEY (familia_id) REFERENCES ref_familia(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE mov_produtos (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,

                              produto_id BIGINT NOT NULL,
                              fazenda_id BIGINT NOT NULL,
                              safra_id BIGINT NOT NULL,
                              tipo_mov_id BIGINT NOT NULL,
                              dt_mov DATE NOT NULL,
                              qtde DECIMAL(15,2) NOT NULL,
                              vlr_unitario DECIMAL(15,2),
                              vlr_total DECIMAL(15,2),
                              nr_nota_fiscal VARCHAR(20),
                              nr_os VARCHAR(20),
                              dt_pagamento DATE,

                              CONSTRAINT fk_mp_produto FOREIGN KEY (produto_id) REFERENCES produtos(id),
                              CONSTRAINT fk_mp_tipo FOREIGN KEY (tipo_mov_id) REFERENCES ref_tipo_mov_produto(id),
                              CONSTRAINT fk_mp_fazenda FOREIGN KEY (fazenda_id) REFERENCES fazendas(id),
                              CONSTRAINT fk_mp_safra FOREIGN KEY (safra_id) REFERENCES safras(id)
) ENGINE=InnoDB;


CREATE TABLE proprietario_fazenda_ativa (
                                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                            proprietario_id BIGINT NOT NULL,
                                            fazenda_id BIGINT NOT NULL,
                                            atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                                            CONSTRAINT fk_proprietario FOREIGN KEY (proprietario_id) REFERENCES proprietarios(id),
                                            CONSTRAINT fk_fazenda FOREIGN KEY (fazenda_id) REFERENCES fazendas(id),
                                            CONSTRAINT uq_proprietario UNIQUE (proprietario_id)
);

CREATE TABLE talhoes (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         nome VARCHAR(150) NOT NULL,
                         fazenda_id BIGINT NOT NULL,
                         dt_criacao DATETIME NOT NULL,
                         area DECIMAL(10,2),
                         esp_rua DECIMAL(10,2),
                         esp_planta DECIMAL(10,2),
                         material VARCHAR(100),
                         id_res_ferrugem BIGINT,
                         id_st_cultivo BIGINT,
                         ativo TINYINT(1) DEFAULT 1,
                         CONSTRAINT fk_talhao_fazenda
                             FOREIGN KEY (fazenda_id) REFERENCES fazendas(id) ON DELETE CASCADE,
                         CONSTRAINT fk_talhao_res_ferrugem
                             FOREIGN KEY (id_res_ferrugem) REFERENCES ref_res_ferrugem(id),
                         CONSTRAINT fk_talhao_st_cultivo
                             FOREIGN KEY (id_st_cultivo) REFERENCES ref_st_cultivo(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE safra_talhoes (
                               id BIGINT AUTO_INCREMENT PRIMARY KEY,
                               proprietario_id BIGINT NOT NULL,
                               fazenda_id BIGINT NOT NULL,
                               safra_id BIGINT NOT NULL,
                               talhao_id BIGINT NOT NULL,
                               cultura_id BIGINT NOT NULL,
                               area_utilizada DECIMAL(10,2) NOT NULL,
                               esp_rua DECIMAL(10,2),
                               esp_planta DECIMAL(10,2),
                               material VARCHAR(100),
                               id_res_ferrugem BIGINT,
                               id_st_cultivo BIGINT,
                               st_terra VARCHAR(100),
                               venc_contrato DATE,
                               irrigacao TINYINT(1),
                               est_litro_planta DECIMAL(10,2),
                               est_saca_hectare DECIMAL(10,2),
                               est_saca DECIMAL(10,2),
                               producao_real DECIMAL(10,2),
                               preco_saca DECIMAL(10,2),
                               ativo TINYINT(1) DEFAULT 1,
                               CONSTRAINT fk_safra_talhao_proprietario
                                   FOREIGN KEY (proprietario_id) REFERENCES proprietarios(id),
                               CONSTRAINT fk_safra_talhao_fazenda
                                   FOREIGN KEY (fazenda_id) REFERENCES fazendas(id),
                               CONSTRAINT fk_safra_talhao_safra
                                   FOREIGN KEY (safra_id) REFERENCES safras(id) ON DELETE CASCADE,
                               CONSTRAINT fk_safra_talhao_talhao
                                   FOREIGN KEY (talhao_id) REFERENCES talhoes(id) ON DELETE CASCADE,
                               CONSTRAINT fk_safra_talhao_cultura
                                   FOREIGN KEY (cultura_id) REFERENCES ref_cultura(id),
                               CONSTRAINT fk_safra_talhao_res_ferrugem
                                   FOREIGN KEY (id_res_ferrugem) REFERENCES ref_res_ferrugem(id),
                               CONSTRAINT fk_safra_talhao_st_cultivo
                                   FOREIGN KEY (id_st_cultivo) REFERENCES ref_st_cultivo(id),
                               UNIQUE (safra_id, talhao_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

ALTER TABLE fazendas
    ADD COLUMN safra_ativa_id BIGINT;

ALTER TABLE fazendas
    ADD CONSTRAINT fk_fazenda_safra_ativa
        FOREIGN KEY (safra_ativa_id) REFERENCES safras(id);


CREATE TABLE ref_parametro (
                               id BIGINT AUTO_INCREMENT PRIMARY KEY,
                               chave VARCHAR(100) NOT NULL,
                               valor DECIMAL(10,4) NOT NULL,
                               descricao VARCHAR(255),
                               ativo TINYINT(1) DEFAULT 1
);


