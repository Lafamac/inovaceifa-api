-- ============================================
-- TABELAS DE REFERÊNCIA (INT)
-- ============================================

CREATE TABLE tab_ref_usuario (
    id INT PRIMARY KEY,
    descricao VARCHAR(50) NOT NULL
);

INSERT INTO tab_ref_usuario (id, descricao) VALUES
(1, 'ADMIN'),
(2, 'SUPER_USUARIO'),
(3, 'USUARIO');

CREATE TABLE ref_cultura (
    id INT AUTO_INCREMENT PRIMARY KEY,
    codigo VARCHAR(50) NOT NULL UNIQUE,
    descricao VARCHAR(100)
);

INSERT INTO ref_cultura (codigo, descricao) VALUES
('CAFE', 'Café'),
('EQUINO', 'Equino'),
('FEIJAO', 'Feijão'),
('MILHO', 'Milho'),
('SOJA', 'Soja'),
('TODAS', 'Todas');

CREATE TABLE ref_res_ferrugem (
    id INT AUTO_INCREMENT PRIMARY KEY,
    codigo VARCHAR(50) NOT NULL UNIQUE,
    descricao VARCHAR(100)
);

INSERT INTO ref_res_ferrugem (codigo, descricao) VALUES
('RESISTENTE', 'Resistente'),
('SUSCETIVEL', 'Suscetível');

CREATE TABLE ref_st_cultivo (
    id INT AUTO_INCREMENT PRIMARY KEY,
    codigo VARCHAR(50) NOT NULL UNIQUE,
    descricao VARCHAR(100)
);

INSERT INTO ref_st_cultivo (codigo, descricao) VALUES
('ESQUELETADO', 'Esqueletado'),
('PRODUCAO', 'Produção'),
('RECEM_PLANTADO', 'Recém Plantado'),
('RECEPA', 'Recepa');

CREATE TABLE ref_tipo_maquina (
    id INT AUTO_INCREMENT PRIMARY KEY,
    codigo VARCHAR(50) NOT NULL UNIQUE,
    descricao VARCHAR(100)
);

INSERT INTO ref_tipo_maquina (codigo, descricao) VALUES
('IMPLEMENTO', 'Implemento'),
('TRATOR', 'Trator');

CREATE TABLE ref_tipo_gasto_maquina (
    id INT AUTO_INCREMENT PRIMARY KEY,
    codigo VARCHAR(50) NOT NULL UNIQUE,
    descricao VARCHAR(100)
);

INSERT INTO ref_tipo_gasto_maquina (codigo, descricao) VALUES
('ABASTECIMENTO', 'Abastecimento'),
('MANUTENCAO', 'Manutenção');

CREATE TABLE ref_role_acesso (
    id INT AUTO_INCREMENT PRIMARY KEY,
    codigo VARCHAR(50) NOT NULL UNIQUE,
    descricao VARCHAR(100)
);

INSERT INTO ref_role_acesso (codigo, descricao) VALUES
('LEITURA', 'Leitura'),
('GERENTE', 'Gerente'),
('ADMIN', 'Administrador');

-- ============================================
-- TABELAS PRINCIPAIS (BIGINT)
-- ============================================

CREATE TABLE usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    perfil_id INT NOT NULL,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (perfil_id) REFERENCES tab_ref_usuario(id)
);

CREATE TABLE proprietarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL UNIQUE,
    nome VARCHAR(255) NOT NULL,
    cpf CHAR(11) NOT NULL UNIQUE,
    celular VARCHAR(50),
    endereco VARCHAR(255),
    bairro VARCHAR(200),
    cidade VARCHAR(200),
    estado CHAR(2),
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

CREATE TABLE fazendas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    proprietario_id BIGINT NOT NULL,
    nome VARCHAR(255) NOT NULL,
    cnpj CHAR(14) NOT NULL UNIQUE,
    endereco VARCHAR(255),
    cidade VARCHAR(200),
    estado CHAR(2),
    FOREIGN KEY (proprietario_id) REFERENCES proprietarios(id) ON DELETE CASCADE
);

CREATE TABLE safras (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    fazenda_id BIGINT NOT NULL,
    nome VARCHAR(20) NOT NULL,
    dt_inicial DATE NOT NULL,
    dt_final DATE NOT NULL,
    FOREIGN KEY (fazenda_id) REFERENCES fazendas(id) ON DELETE CASCADE
);

CREATE TABLE talhoes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    fazenda_id BIGINT NOT NULL,
    safra_id BIGINT NOT NULL,
    dt_criacao DATETIME NOT NULL,
    id_cultura INT NOT NULL,
    area DECIMAL(10,2),
    esp_rua DECIMAL(10,2),
    esp_planta DECIMAL(10,2),
    material VARCHAR(100),
    id_res_ferrugem INT NOT NULL,
    id_st_cultivo INT NOT NULL,
    st_terra VARCHAR(100),
    venc_contrato DATE,
    irrigacao BOOLEAN,
    est_litro_planta DECIMAL(10,2),
    est_saca_hectare DECIMAL(10,2),
    est_saca DECIMAL(10,2),

    FOREIGN KEY (fazenda_id) REFERENCES fazendas(id) ON DELETE CASCADE,
    FOREIGN KEY (safra_id) REFERENCES safras(id) ON DELETE CASCADE,
    FOREIGN KEY (id_cultura) REFERENCES ref_cultura(id),
    FOREIGN KEY (id_res_ferrugem) REFERENCES ref_res_ferrugem(id),
    FOREIGN KEY (id_st_cultivo) REFERENCES ref_st_cultivo(id)
);

CREATE TABLE maquinas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    fazenda_id BIGINT NOT NULL,
    nome VARCHAR(255) NOT NULL,
    marca VARCHAR(100),
    modelo VARCHAR(100),
    descricao VARCHAR(255),
    ano_fabricacao INT,
    imagem VARCHAR(255),
    horimetro FLOAT,
    id_tipo_maquina INT NOT NULL,

    FOREIGN KEY (fazenda_id) REFERENCES fazendas(id) ON DELETE CASCADE,
    FOREIGN KEY (id_tipo_maquina) REFERENCES ref_tipo_maquina(id)
);

CREATE TABLE funcionarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT UNIQUE NULL,
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

    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE SET NULL,
    FOREIGN KEY (proprietario_id) REFERENCES proprietarios(id) ON DELETE CASCADE,
    FOREIGN KEY (fazenda_id) REFERENCES fazendas(id) ON DELETE CASCADE
);

CREATE TABLE horas_maquinas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_maquina BIGINT NOT NULL,
    safra_id BIGINT NOT NULL,
    fazenda_id BIGINT NOT NULL,
    funcionario_id  BIGINT NULL,

    FOREIGN KEY (id_maquina) REFERENCES maquinas(id) ON DELETE CASCADE,
    FOREIGN KEY (safra_id) REFERENCES safras(id) ON DELETE CASCADE,
    FOREIGN KEY (fazenda_id) REFERENCES fazendas(id) ON DELETE CASCADE,
    FOREIGN KEY (funcionario_id ) REFERENCES funcionarios(id) ON DELETE SET NULL
);

CREATE TABLE gastos_maquina (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    data DATE NOT NULL,
    id_tipo_gasto INT NOT NULL,
    id_maquina BIGINT NOT NULL,
    fazenda_id BIGINT NOT NULL,
    safra_id BIGINT NOT NULL,
    funcionario_id  BIGINT NULL,
    descricao VARCHAR(255),
    vlr_usado DECIMAL(10,2) NOT NULL,

    FOREIGN KEY (id_tipo_gasto) REFERENCES ref_tipo_gasto_maquina(id),
    FOREIGN KEY (id_maquina) REFERENCES maquinas(id) ON DELETE CASCADE,
    FOREIGN KEY (fazenda_id) REFERENCES fazendas(id) ON DELETE CASCADE,
    FOREIGN KEY (safra_id) REFERENCES safras(id) ON DELETE CASCADE,
    FOREIGN KEY (funcionario_id ) REFERENCES funcionarios(id) ON DELETE SET NULL
);

CREATE TABLE hora_salario_funcionario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    funcionario_id  BIGINT NOT NULL,
    safra_id BIGINT NOT NULL,
    mes_ano VARCHAR(7) NOT NULL,
    he_planejada DECIMAL(15,2),
    total_sal_planejado DECIMAL(15,2),
    hr_planejadas DECIMAL(15,2),
    hr_mensais_contratadas DECIMAL(15,2),
    hr_mensais_efetivas DECIMAL(15,2),
    vlr_hora_planejado DECIMAL(15,2),
    vlr_hora_realizado DECIMAL(15,2),
    salario_pago DECIMAL(15,2),
    encargos_pagos DECIMAL(15,2),
    hr_extra_paga DECIMAL(15,2),
    total_salario_pg  DECIMAL(15,2),
    hr_mensais_exec   DECIMAL(15,2),	


    CONSTRAINT fk_hora_funcionarios_funcionarios
        FOREIGN KEY (funcionario_id )
        REFERENCES funcionarios(id)
        ON DELETE CASCADE
);


CREATE TABLE   tipo_conta  (
   id INT AUTO_INCREMENT PRIMARY KEY,
   arvore  varchar(255)  DEFAULT NULL,
   indice  varchar(150) DEFAULT NULL,
   elemento  varchar(200)  DEFAULT NULL,
   unidade  varchar(50) DEFAULT NULL
);

CREATE TABLE conta_gerencial (
  id INT AUTO_INCREMENT PRIMARY KEY,
  descricao varchar(50) DEFAULT NULL
);

CREATE TABLE   cadastro_operacao  (
   id INT AUTO_INCREMENT PRIMARY KEY,
   cod_oper  int DEFAULT NULL,
   cultura  varchar(50) DEFAULT NULL,
   operacao  varchar(100) DEFAULT NULL,
   modalidade  varchar(100) DEFAULT NULL,
   deslocamento  varchar(100) DEFAULT NULL,
   atividade  varchar(100) DEFAULT NULL,
   faixa_nominal  decimal(20,6) DEFAULT NULL,
   velocidade_op  decimal(20,6) DEFAULT NULL,
   eficiencia_campo  decimal(20,6) DEFAULT NULL,
   gasto_diesel  decimal(20,6) DEFAULT NULL
);

CREATE TABLE talhoes_safra (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    fazenda_id BIGINT NOT NULL,
    safra_id BIGINT NOT NULL,

    CONSTRAINT fk_talhoes_safras
        FOREIGN KEY (safra_id)
        REFERENCES safras(id)
        ON DELETE CASCADE
);

CREATE TABLE administrativo ( 
   id INT AUTO_INCREMENT PRIMARY KEY,
   descricao VARCHAR(150) NOT NULL,
   fazenda_id BIGINT NOT NULL,
   safra_id BIGINT NOT NULL,
   mes_ano VARCHAR(7),
   destinacao VARCHAR(50),
   ativ_educampo VARCHAR(150),
   un VARCHAR(5),
   vlr_unit_planejado DECIMAL(15,2),
   qtd_planejado INT,
   vlr_total_planejado DECIMAL(15,2),
   vlr_ha_planejado DECIMAL(15,2),
   vlr_Unit_realizado DECIMAL(15,2),
   qtd_realizada INT,
   vlr_total_realizado DECIMAL(15,2),
   vlr_ha_realizado DECIMAL(15,2)
);

CREATE TABLE produtos ( 
   id_produto INT AUTO_INCREMENT PRIMARY KEY,
   nome VARCHAR(150) NOT NULL,
   fazenda_id BIGINT NOT NULL, 
   codigo VARCHAR(13),
   unidade VARCHAR(30),
   ativo_nutr VARCHAR(50),
   qtde FLOAT DEFAULT 0,
   vlr_unitario DECIMAL(15,2) DEFAULT 0.00,
   vlr_total DECIMAL(15,2) DEFAULT 0.00
);

CREATE TABLE mov_produtos ( 
   id_mov INT AUTO_INCREMENT PRIMARY KEY,
   id_produto INT NOT NULL,
   fazenda_id BIGINT NOT NULL,
   safra_id BIGINT NOT NULL, 
   tipo_mov ENUM('Entrada', 'Saida') NOT NULL,
   dt_mov DATE NOT NULL,
   qtde FLOAT NOT NULL,
   vlr_unitario DECIMAL(15,2),
   vlr_total DECIMAL(15,2),
   nr_nota_fiscal VARCHAR(20),
   dt_pagamento DATE,

   CONSTRAINT fk_produto_movimentacao 
      FOREIGN KEY (id_produto) 
      REFERENCES produtos(id_produto)
      ON DELETE CASCADE
);

CREATE TABLE contas_pagar ( 
   id INT AUTO_INCREMENT PRIMARY KEY,
   favorecido VARCHAR(200) NOT NULL,
   fazenda_id BIGINT NOT NULL, 
   safra_id BIGINT NOT NULL,
   n_nota_fiscal VARCHAR(30),
   dt_vencimento DATE,
   dt_pagamentto DATE,
   vlr_real DECIMAL(15,2) DEFAULT 0.00,
   vlr_juros DECIMAL(15,2) DEFAULT 0.00,
   vlr_pago DECIMAL (15,2),
   baixada CHAR(1) NULL DEFAULT 'N'
);
