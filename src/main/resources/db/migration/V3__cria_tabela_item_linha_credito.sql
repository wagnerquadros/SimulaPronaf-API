CREATE TABLE item_linha_credito (
    id BIGSERIAL PRIMARY KEY,
    linha_credito_id BIGINT NOT NULL,
    titulo VARCHAR(150) NOT NULL,
    descricao TEXT,
    publico TEXT,
    limite NUMERIC(15,2) NOT NULL,
    juros NUMERIC(5,2) NOT NULL,
    prazo_maximo INTEGER NOT NULL,
    carencia_maxima INTEGER NOT NULL,
    ordem_exibicao INTEGER NOT NULL,
    icone VARCHAR(100),
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    data_criacao TIMESTAMP NOT NULL,
    CONSTRAINT fk_item_linha_credito
        FOREIGN KEY (linha_credito_id)
        REFERENCES linha_credito(id)
);