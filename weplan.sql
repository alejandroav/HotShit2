-- phpMyAdmin SQL Dump
-- version 4.5.2
-- http://www.phpmyadmin.net
--
-- Servidor: localhost
-- Tiempo de generación: 17-12-2015 a las 15:09:51
-- Versión del servidor: 5.7.10
-- Versión de PHP: 5.6.13-0+deb8u1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `weplan`
--
CREATE DATABASE IF NOT EXISTS `weplan` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `weplan`;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `attendance`
--

DROP TABLE IF EXISTS `attendance`;
CREATE TABLE `attendance` (
  `user_id` int(11) NOT NULL,
  `event_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `events`
--

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

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `fases`
--

DROP TABLE IF EXISTS `fases`;
CREATE TABLE `fases` (
  `evento` int(11) NOT NULL,
  `numfase` int(11) NOT NULL,
  `coste` decimal(12,2) NOT NULL,
  `duracion` decimal(12,2) NOT NULL,
  `ubicacion` varchar(255) NOT NULL,
  `descripcion` longtext NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `follows`
--

DROP TABLE IF EXISTS `follows`;
CREATE TABLE `follows` (
  `follower` int(11) NOT NULL,
  `followed` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `users`
--

DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(130) NOT NULL,
  `imagen` varchar(255) NOT NULL,
  `nivelorganizador` decimal(12,2) NOT NULL,
  `nivelparticipante` decimal(12,2) NOT NULL,
  `session_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Indices de la tabla `attendance`
--
ALTER TABLE `attendance`
  ADD PRIMARY KEY (`user_id`,`event_id`);

--
-- Indices de la tabla `events`
--
ALTER TABLE `events`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `fases`
--
ALTER TABLE `fases`
  ADD PRIMARY KEY (`evento`,`numfase`);

--
-- Indices de la tabla `follows`
--
ALTER TABLE `follows`
  ADD PRIMARY KEY (`follower`,`followed`);

--
-- Indices de la tabla `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `events`
--
ALTER TABLE `events`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1;
--
-- AUTO_INCREMENT de la tabla `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
