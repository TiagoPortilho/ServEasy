-- Criação do banco de dados ServEasy
CREATE DATABASE IF NOT EXISTS serveasy_db;
USE serveasy_db;

-- Usuários do sistema
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('ADMIN', 'COZINHEIRO', 'CLIENTE_ATENDENTE') NOT NULL,
    full_name VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Itens do cardápio
CREATE TABLE IF NOT EXISTS menu_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    category ENUM('ENTRADAS', 'PRATOS_PRINCIPAIS', 'BEBIDAS', 'SOBREMESAS', 'LANCHES', 'PIZZAS') NOT NULL,
    image_url VARCHAR(255),
    is_available BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Mesas do restaurante
CREATE TABLE IF NOT EXISTS tables (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    table_number INT UNIQUE NOT NULL,
    capacity INT NOT NULL,
    status ENUM('DISPONIVEL', 'OCUPADA', 'RESERVADA', 'MANUTENCAO') DEFAULT 'DISPONIVEL',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Pedidos
CREATE TABLE IF NOT EXISTS orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    table_number INT NOT NULL,
    customer_name VARCHAR(100),
    total DECIMAL(10,2) NOT NULL,
    status ENUM('NOVO', 'EM_ANDAMENTO', 'PRONTO', 'ENTREGUE', 'CANCELADO') DEFAULT 'NOVO',
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Itens dos pedidos
CREATE TABLE IF NOT EXISTS order_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    menu_item_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    notes TEXT,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (menu_item_id) REFERENCES menu_items(id)
);

-- Estoque
CREATE TABLE IF NOT EXISTS stock_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    quantity INT NOT NULL DEFAULT 0,
    min_quantity INT NOT NULL DEFAULT 0,
    unit_price DECIMAL(10, 2) DEFAULT 0.00,
    unit VARCHAR(20),
    supplier VARCHAR(100),
    entry_date DATE,
    expiry_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Ingredientes dos pratos (relaciona pratos com itens do estoque)
CREATE TABLE IF NOT EXISTS menu_item_ingredients (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    menu_item_id BIGINT NOT NULL,
    stock_item_id BIGINT NOT NULL,
    quantity DECIMAL(10, 3) NOT NULL,
    unit VARCHAR(20),
    show_unit BOOLEAN NOT NULL DEFAULT TRUE,
    FOREIGN KEY (menu_item_id) REFERENCES menu_items(id) ON DELETE CASCADE,
    FOREIGN KEY (stock_item_id) REFERENCES stock_items(id) ON DELETE CASCADE,
    INDEX idx_menu_item (menu_item_id),
    INDEX idx_stock_item (stock_item_id)
);

-- Feedbacks dos clientes
CREATE TABLE IF NOT EXISTS feedbacks (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_name VARCHAR(100) NOT NULL,
    rating INT NOT NULL CHECK (rating >= 1 AND rating <= 5),
    comment TEXT NOT NULL,
    order_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE SET NULL
);

-- Base de dados criada e pronta para uso
-- Todos os dados devem ser inseridos manualmente através da interface do sistema

COMMIT;