CREATE DATABASE IF NOT EXISTS `weplan` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;

USE `weplan`;

DROP TABLE IF EXISTS `attendance`;
CREATE TABLE `attendance` (
  `user_id` int(11) NOT NULL,
  `event_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `events`;
CREATE TABLE `events` (
  `id` int(11) NOT NULL,
  `title` varchar(255) NOT NULL,
  `date` datetime NOT NULL,
  `description` longtext NOT NULL,
  `creator_id` int(11) NOT NULL,
  `image` varchar(255) NOT NULL,
  `cost` int(11) NOT NULL,
  `capacity` int(11) NOT NULL,
  `geom` geometry DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `fases`;
CREATE TABLE `fases` (
  `evento` int(11) NOT NULL,
  `numfase` int(11) NOT NULL,
  `coste` decimal(12,2) NOT NULL,
  `duracion` decimal(12,2) NOT NULL,
  `ubicacion` varchar(255) NOT NULL,
  `descripcion` longtext NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `follows`;
CREATE TABLE `follows` (
  `follower` int(11) NOT NULL,
  `followed` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(130) NOT NULL,
  `image` varchar(255) NOT NULL DEFAULT '',
  `nivelorganizador` decimal(12,2) NOT NULL DEFAULT '0.00',
  `nivelparticipante` decimal(12,2) NOT NULL DEFAULT '0.00',
  `session_id` varchar(100) DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;

ALTER TABLE `attendance`
  ADD PRIMARY KEY (`user_id`,`event_id`);

ALTER TABLE `events`
  ADD PRIMARY KEY (`id`);

ALTER TABLE `fases`
  ADD PRIMARY KEY (`evento`,`numfase`);

ALTER TABLE `follows`
  ADD PRIMARY KEY (`follower`,`followed`);

ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

ALTER TABLE `events`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1;

ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1;
