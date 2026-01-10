-- ============================================
-- TABELAS DE REFERÊNCIA
-- ============================================

CREATE TABLE tab_ref_usuario (
    id BIGINT PRIMARY KEY,
    descricao VARCHAR(50) NOT NULL
);

INSERT INTO tab_ref_usuario (id, descricao) VALUES
(1, 'ADMIN'),
(2, 'SUPER_USUARIO'),
(3, 'USUARIO');

-- ============================================
-- USUÁRIOS
-- ============================================

CREATE TABLE usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    perfil BIGINT NOT NULL,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_usuario_perfil
        FOREIGN KEY (perfil)
        REFERENCES tab_ref_usuario(id)
);

-- ============================================
-- PROPRIETÁRIOS
-- ============================================

CREATE TABLE proprietarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    cnpj CHAR(14) NOT NULL UNIQUE
);

-- ============================================
-- FAZENDAS
-- ============================================

CREATE TABLE fazendas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_proprietario BIGINT NOT NULL,
    nome VARCHAR(255) NOT NULL,
    endereco VARCHAR(255),
    cidade VARCHAR(200),
    estado CHAR(2),

    CONSTRAINT fk_fazendas_proprietarios
        FOREIGN KEY (id_proprietario)
        REFERENCES proprietarios(id)
        ON DELETE CASCADE
);

-- ============================================
-- SAFRAS
-- ============================================

CREATE TABLE safras (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_fazenda BIGINT NOT NULL,
    nome VARCHAR(20) NOT NULL,
    dt_inicial DATE NOT NULL,
    dt_final DATE NOT NULL,

    CONSTRAINT fk_safras_fazendas
        FOREIGN KEY (id_fazenda)
        REFERENCES fazendas(id)
        ON DELETE CASCADE
);
