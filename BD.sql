-- Crear base de datos
CREATE DATABASE IF NOT EXISTS biblioteca_donbosco;
USE biblioteca_donbosco;

-- Tabla de usuarios
CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100),
    username VARCHAR(50) UNIQUE,
    password VARCHAR(100),
    rol ENUM('administrador', 'profesor', 'alumno') NOT NULL
);

-- Tabla padre: documentos
CREATE TABLE documentos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(200),
    autor VARCHAR(100),
    anio_publicacion INT,
    tipo ENUM('libro', 'revista', 'cd', 'dvd', 'pdf', 'tesis') NOT NULL
);

-- Tabla hija: libros
CREATE TABLE libros (
    id INT PRIMARY KEY,
    editorial VARCHAR(100),
    numero_paginas INT,
    FOREIGN KEY (id) REFERENCES documentos(id) ON DELETE CASCADE
);

-- Tabla hija: revistas
CREATE TABLE revistas (
    id INT PRIMARY KEY,
    numero INT,
    mes VARCHAR(20),
    FOREIGN KEY (id) REFERENCES documentos(id) ON DELETE CASCADE
);

-- Tabla hija: cds
CREATE TABLE cds (
    id INT PRIMARY KEY,
    genero VARCHAR(50),
    duracion_min INT,
    FOREIGN KEY (id) REFERENCES documentos(id) ON DELETE CASCADE
);

-- Tabla hija: dvds
CREATE TABLE dvds (
    id INT PRIMARY KEY,
    director VARCHAR(100),
    duracion_min INT,
    FOREIGN KEY (id) REFERENCES documentos(id) ON DELETE CASCADE
);

-- Tabla hija: pdfs
CREATE TABLE pdfs (
    id INT PRIMARY KEY,
    tema VARCHAR(100),
    numero_paginas INT,
    FOREIGN KEY (id) REFERENCES documentos(id) ON DELETE CASCADE
);

-- Tabla hija: tesis
CREATE TABLE tesis (
    id INT PRIMARY KEY,
    universidad VARCHAR(100),
    autor_estudiante VARCHAR(100),
    FOREIGN KEY (id) REFERENCES documentos(id) ON DELETE CASCADE
);

-- Tabla de préstamos
CREATE TABLE prestamos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT,
    documento_id INT,
    fecha_prestamo DATE,
    fecha_devolucion DATE,
    devuelto BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    FOREIGN KEY (documento_id) REFERENCES documentos(id)
);