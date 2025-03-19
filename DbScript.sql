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
    
    
    
INSERT INTO funcionario (nome, cpf, idade, salario, tipo_funcionario) 
VALUES ('NomeDono', '12345678900', 45, 8000.00, 'Dono');

INSERT INTO funcionario (nome, cpf, idade, salario, tipo_funcionario) 
VALUES ('NomeCozinheiro', '98765432100', 35, 3000.00, 'Cozinheiro');

INSERT INTO funcionario (nome, cpf, idade, salario, tipo_funcionario) 
VALUES ('NomeAtendente', '45612378900', 28, 2000.00, 'Atendente');

INSERT INTO mesa (numero, ocupada, valor_gasto, pago) 
VALUES (101, false, 0.00, false);

INSERT INTO usuario (login, senha, id_funcionario) 
VALUES ('dono123', 'donosenha123', 1);

INSERT INTO usuario (login, senha, id_funcionario) 
VALUES ('cozinheiro123', 'cozinheirosenha123', 2);

INSERT INTO usuario (login, senha, id_funcionario) 
VALUES ('atendente123', 'atendentesenha123', 3);

INSERT INTO usuario (login, senha, id_mesa) 
VALUES ('mesa1', 'mesa1senha', 1);