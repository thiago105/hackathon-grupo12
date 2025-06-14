CREATE DATABASE IF NOT EXISTS hackata;
USE hackata;

--TABELA CURSOS
CREATE TABLE cursos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL
);

--TABELA USUARIOS
CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    foto_url VARCHAR(255),
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    senha_hash VARCHAR(255),
    curso_id INT,
    FOREIGN KEY (curso_id) REFERENCES cursos(id)
);

--TABELA PALESTRANTE
CREATE TABLE palestrantes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    mini_curriculo TEXT,
    tema VARCHAR(150),
    foto_url VARCHAR(255)
);

--TABELA EVENTOS
CREATE TABLE eventos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    data_inicio DATE,
    data_fim DATE,
    hora TIME,
    endereco VARCHAR(150) NOT NULL,
    foto_url VARCHAR(255),
    curso_id INT,
    palestrante_id INT,
    FOREIGN KEY (curso_id) REFERENCES cursos(id),
    FOREIGN KEY (palestrante_id) REFERENCES palestrantes(id)
);

--TABELA INSCRICOES
CREATE TABLE inscricoes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT NOT NULL,
    evento_id INT NOT NULL,
    data_inscricao DATETIME DEFAULT CURRENT_TIMESTAMP,
    aprovado BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    FOREIGN KEY (evento_id) REFERENCES eventos(id),
    UNIQUE (usuario_id, evento_id)
);
