CREATE TABLE `Plan` (
  `id` INTEGER PRIMARY KEY AUTO_INCREMENT,
  `nombre` VARCHAR(255) NOT NULL,
  `fecha` DATETIME NOT NULL,
  `descripcion` LONGTEXT NOT NULL,
  `creador` INTEGER NOT NULL,
  `imagen` VARCHAR(255) NOT NULL
);

CREATE TABLE `Usuarios` (
  `id` INTEGER PRIMARY KEY AUTO_INCREMENT,
  `nombre` VARCHAR(255) NOT NULL,
  `email` VARCHAR(255) NOT NULL,
  `password` LONGTEXT NOT NULL,
  `imagen` VARCHAR(255) NOT NULL,
  `nivelorganizador` DECIMAL(12, 2) NOT NULL,
  `nivelparticipante` DECIMAL(12, 2) NOT NULL
);

CREATE TABLE `fase` (
  `evento` INTEGER NOT NULL,
  `numfase` INTEGER NOT NULL,
  `coste` DECIMAL(12, 2) NOT NULL,
  `duracion` DECIMAL(12, 2) NOT NULL,
  `ubicacion` VARCHAR(255) NOT NULL,
  `descripcion` LONGTEXT NOT NULL,
  PRIMARY KEY (`evento`, `numfase`)
);

CREATE TABLE `follow` (
  `follower` INTEGER NOT NULL,
  `followed` INTEGER NOT NULL,
  PRIMARY KEY (`follower`, `followed`)
);

CREATE TABLE `participaen` (
  `usuario` INTEGER NOT NULL,
  `evento` INTEGER NOT NULL,
  PRIMARY KEY (`usuario`, `evento`)
);
