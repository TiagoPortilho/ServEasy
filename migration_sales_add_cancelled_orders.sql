-- Adicionar coluna para contar pedidos cancelados na venda
ALTER TABLE sales ADD COLUMN cancelled_orders_count INT DEFAULT 0;