CREATE TABLE `planes` (
	`id` INTEGER PRIMARY KEY AUTO_INCREMENT,
	`nombre` VARCHAR(255) NOT NULL,
	`fecha` DATETIME NOT NULL,
	`descripcion` LONGTEXT NOT NULL,
	`creador` INTEGER NOT NULL,
	`imagen` VARCHAR(255) NOT NULL
);

CREATE TABLE `users` (
	`id` INTEGER PRIMARY KEY AUTO_INCREMENT,
	`username` VARCHAR(50) NOT NULL,
	`email` VARCHAR(100) NOT NULL,
	`password` VARCHAR(130) NOT NULL,
	`imagen` VARCHAR(255) NOT NULL,
	`nivelorganizador` DECIMAL(12, 2) NOT NULL,
	`nivelparticipante` DECIMAL(12, 2) NOT NULL,
	`session_id` INT(11) NOT NULL
);

CREATE TABLE `fases` (
	`evento` INTEGER NOT NULL,
	`numfase` INTEGER NOT NULL,
	`coste` DECIMAL(12, 2) NOT NULL,
	`duracion` DECIMAL(12, 2) NOT NULL,
	`ubicacion` VARCHAR(255) NOT NULL,
	`descripcion` LONGTEXT NOT NULL,
	PRIMARY KEY (`evento`, `numfase`)
);

CREATE TABLE `follows` (
	`follower` INTEGER NOT NULL,
	`followed` INTEGER NOT NULL,
	PRIMARY KEY (`follower`, `followed`)
);

CREATE TABLE `asistencias` (
	`usuario` INTEGER NOT NULL,
	`evento` INTEGER NOT NULL,
	PRIMARY KEY (`usuario`, `evento`)
);
