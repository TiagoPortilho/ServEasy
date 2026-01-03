-- Migração para criar tabela de vendas/ganhos
CREATE TABLE IF NOT EXISTS sales (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    table_number INT NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    items_count INT NOT NULL DEFAULT 0,
    order_ids TEXT,
    payment_method VARCHAR(50) DEFAULT 'DINHEIRO',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    INDEX idx_table_number (table_number),
    INDEX idx_created_at (created_at),
    INDEX idx_total_amount (total_amount)
);