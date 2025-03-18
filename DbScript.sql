CREATE DATABASE serveasy;
USE serveasy;

CREATE TABLE funcionario (
    id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    nome VARCHAR(255) NOT NULL,
    cpf VARCHAR(14) NOT NULL UNIQUE,
    tipo_funcionario VARCHAR(100) NOT NULL,
    idade INT NOT NULL,
    salario DECIMAL(10,2)
);


CREATE TABLE mesa (
    id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    numero INT NOT NULL UNIQUE,
    ocupada BOOLEAN DEFAULT FALSE,
    valor_gasto FLOAT DEFAULT 0.0, 
    pago BOOLEAN DEFAULT FALSE
);



CREATE TABLE usuario (
    id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    login VARCHAR(100) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    id_funcionario INT NULL,
    id_mesa INT NULL,
    CONSTRAINT fk_usuario_funcionario FOREIGN KEY (id_funcionario) REFERENCES funcionario(id) ON DELETE SET NULL,
    CONSTRAINT fk_usuario_mesa FOREIGN KEY (id_mesa) REFERENCES mesa(id)
);

CREATE TABLE prato (
    id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,  
    nome VARCHAR(255) NOT NULL,                  
    preco FLOAT NOT NULL,
    descricao TEXT
);

CREATE TABLE pedido (
    id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    id_prato INT NOT NULL,   
    id_mesa INT NOT NULL, 
    confirmado BOOLEAN DEFAULT FALSE, 
    CONSTRAINT fk_pedido_prato FOREIGN KEY (id_prato) REFERENCES prato(id),  
    CONSTRAINT fk_pedido_mesa FOREIGN KEY (id_mesa) REFERENCES mesa(id)     
);

CREATE TABLE feedback (
    id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,       
    comentario TEXT NOT NULL);       