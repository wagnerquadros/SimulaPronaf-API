INSERT INTO linha_credito (nome, tipo, descricao, ativo, data_criacao)
VALUES
(
    'Pronaf Custeio',
    'CUSTEIO',
    'Financiamento para a manutenção da produção agrícola ou pecuária.',
    TRUE,
    CURRENT_TIMESTAMP
),
(
    'Pronaf Mais Alimentos',
    'INVESTIMENTO',
    'Linha de investimento para modernização, ampliação da produção e melhoria da infraestrutura produtiva.',
    TRUE,
    CURRENT_TIMESTAMP
);