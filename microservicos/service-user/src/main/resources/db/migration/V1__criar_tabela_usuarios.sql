CREATE EXTENSION IF NOT EXISTS citext;


CREATE TABLE usuarios (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nome varchar(120) NOT NULL,
    email CITEXT NOT NULL UNIQUE,
    cpf varchar(14) NOT NULL UNIQUE,
    telefone varchar(20),
    senha_hash varchar(255) NOT null,
    perfil varchar(20) NOT NULL CHECK (perfil IN ('ADMINISTRADOR', 'AUDITOR', 'TECNICO')),
    online boolean DEFAULT FALSE NOT NULL ,
    ativo boolean DEFAULT TRUE NOT NULL,
    criado_em timestamptz DEFAULT CURRENT_TIMESTAMP NOT NULL,

    CONSTRAINT chk_online_apenas_tecnico 
        CHECK (online = FALSE OR (perfil = 'TECNICO' AND online = TRUE))
);

CREATE TABLE tokens_recuperacao_senha (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    usuario_id UUID NOT NULL,
    token_hash varchar(255) NOT NULL UNIQUE,
    expira_em timestamptz NOT NULL,
    usado_em timestamptz,

    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);

CREATE INDEX idx_tokens_usuario_id 
ON tokens_recuperacao_senha (usuario_id);