ALTER TABLE item_linha_credito
ADD COLUMN codigo VARCHAR(100);

UPDATE item_linha_credito
SET codigo = 'CUSTEIO_BOVINO_CORTE'
WHERE titulo = 'Bovinocultura de corte';

UPDATE item_linha_credito
SET codigo = 'CUSTEIO_BOVINO_LEITE'
WHERE titulo = 'Bovinocultura de leite';

UPDATE item_linha_credito
SET codigo = 'CUSTEIO_SOJA'
WHERE titulo = 'Soja';

UPDATE item_linha_credito
SET codigo = 'CUSTEIO_ARROZ'
WHERE titulo = 'Arroz';

UPDATE item_linha_credito
SET codigo = 'CUSTEIO_TRIGO'
WHERE titulo = 'Trigo';

UPDATE item_linha_credito
SET codigo = 'CUSTEIO_PECUARIA_RECRIA_ENGORDA'
WHERE titulo = 'Pecuária (Recria e Engorda)';

UPDATE item_linha_credito
SET codigo = 'INVEST_MAQUINAS_IMPLEMENTOS'
WHERE titulo = 'Maquinas e Implementos';

UPDATE item_linha_credito
SET codigo = 'INVEST_MATRIZES_REPRODUTORES_CORTE'
WHERE titulo = 'Matrizes e Reprodutores Pecuária de Corte';

UPDATE item_linha_credito
SET codigo = 'INVEST_MORADIAS_RURAIS'
WHERE titulo = 'Construção e Reforma de Moradias Rurais';

UPDATE item_linha_credito
SET codigo = 'INVEST_CAMINHONETES_MOTOS'
WHERE titulo = 'Caminhonetes e motos';

UPDATE item_linha_credito
SET codigo = 'INVEST_MELHORAMENTO_GENETICO_LEITE'
WHERE titulo = 'Melhoramento Genético e Pecuária de Leite';

UPDATE item_linha_credito
SET codigo = 'INVEST_ATIVIDADES_INTENSIVAS'
WHERE titulo = 'Atividades Intensivas';

UPDATE item_linha_credito
SET codigo = 'INVEST_DEMAIS_PRODUTOS'
WHERE titulo = 'Demais Produtos de Investimento';

ALTER TABLE item_linha_credito
ALTER COLUMN codigo SET NOT NULL;

ALTER TABLE item_linha_credito
ADD CONSTRAINT uk_item_linha_credito_codigo UNIQUE (codigo);