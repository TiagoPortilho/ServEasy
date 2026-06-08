-- Migration: Alterar tabela orders para usar table_id FK em vez de table_number
-- Data: 2026-01-03
-- Descrição: Adiciona coluna table_id como FK para tabela tables e remove table_number

-- Primeiro, adicionar a nova coluna table_id
ALTER TABLE orders ADD COLUMN table_id BIGINT NULL;

-- Atualizar os dados existentes: mapear table_number para table_id
UPDATE orders 
SET table_id = (
    SELECT id 
    FROM tables 
    WHERE tables.table_number = orders.table_number 
    LIMIT 1
)
WHERE table_number IS NOT NULL;

-- Adicionar a constraint de foreign key
ALTER TABLE orders 
ADD CONSTRAINT FK_orders_table_id 
FOREIGN KEY (table_id) REFERENCES tables(id);

-- Tornar a coluna table_id NOT NULL (após os dados serem migrados)
ALTER TABLE orders MODIFY COLUMN table_id BIGINT NOT NULL;

-- Remover a coluna table_number antiga
ALTER TABLE orders DROP COLUMN table_number;

-- Adicionar índice para melhor performance
CREATE INDEX IDX_orders_table_id ON orders(table_id);