-- Script de migração para adicionar campo unit_price à tabela stock_items
-- Execute este script se a tabela já existe sem o campo unit_price

-- Verificar se a coluna unit_price já existe, se não existir, adicionar
SET @sql = (SELECT IF(
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
   WHERE TABLE_SCHEMA = DATABASE() 
   AND TABLE_NAME = 'stock_items' 
   AND COLUMN_NAME = 'unit_price') = 0,
  'ALTER TABLE stock_items ADD COLUMN unit_price DECIMAL(10, 2) DEFAULT 0.00 AFTER min_quantity',
  'SELECT "A coluna unit_price já existe" AS message'
));

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Atualizar registros existentes com preços de exemplo
UPDATE stock_items SET unit_price = 3.50 WHERE name = 'Farinha de Trigo';
UPDATE stock_items SET unit_price = 28.90 WHERE name = 'Mussarela';
UPDATE stock_items SET unit_price = 8.50 WHERE name = 'Molho de Tomate';
UPDATE stock_items SET unit_price = 22.80 WHERE name = 'Calabresa';
UPDATE stock_items SET unit_price = 35.90 WHERE name = 'Presunto';
UPDATE stock_items SET unit_price = 1.50 WHERE name = 'Alface';