-- ----------------------------------------------------------------------------
-- Put here INSERT statements for inserting data required by the application
-- in the "pojo" database.
-- -----------------------------------------------------------------------------

-- MySQL – Disable Foreign Key Checks or Constraints
SET foreign_key_checks = 0;


SET @today=CURDATE();
SET @sub1day=DATE_SUB(@today,INTERVAL 1 DAY);
SET @sub2day=DATE_SUB(@today,INTERVAL 2 DAY);
SET @sub3day=DATE_SUB(@today,INTERVAL 3 DAY);
SET @sub4day=DATE_SUB(@today,INTERVAL 4 DAY);
SET @sub5day=DATE_SUB(@today,INTERVAL 5 DAY);
SET @sub6day=DATE_SUB(@today,INTERVAL 6 DAY);
SET @sub7day=DATE_SUB(@today,INTERVAL 7 DAY);
SET @sub8day=DATE_SUB(@today,INTERVAL 8 DAY);
SET @sub9day=DATE_SUB(@today,INTERVAL 9 DAY);
SET @sub10day=DATE_SUB(@today,INTERVAL 10 DAY);
SET @sub11day=DATE_SUB(@today,INTERVAL 11 DAY);
SET @sub12day=DATE_SUB(@today,INTERVAL 12 DAY);
SET @sub13day=DATE_SUB(@today,INTERVAL 13 DAY);
SET @sub14day=DATE_SUB(@today,INTERVAL 14 DAY);
SET @sub15day=DATE_SUB(@today,INTERVAL 15 DAY);
SET @sub16day=DATE_SUB(@today,INTERVAL 16 DAY);
SET @sub17day=DATE_SUB(@today,INTERVAL 17 DAY);
SET @sub18day=DATE_SUB(@today,INTERVAL 18 DAY);
SET @sub19day=DATE_SUB(@today,INTERVAL 19 DAY);
SET @sub20day=DATE_SUB(@today,INTERVAL 20 DAY);
SET @sub21day=DATE_SUB(@today,INTERVAL 21 DAY);
SET @sub22day=DATE_SUB(@today,INTERVAL 22 DAY);
SET @sub23day=DATE_SUB(@today,INTERVAL 23 DAY);
SET @sub24day=DATE_SUB(@today,INTERVAL 24 DAY);
SET @sub25day=DATE_SUB(@today,INTERVAL 25 DAY);
SET @sub26day=DATE_SUB(@today,INTERVAL 26 DAY);
SET @sub27day=DATE_SUB(@today,INTERVAL 27 DAY);
SET @sub28day=DATE_SUB(@today,INTERVAL 28 DAY);
SET @sub29day=DATE_SUB(@today,INTERVAL 29 DAY);
SET @sub30day=DATE_SUB(@today,INTERVAL 30 DAY);
SET @sub31day=DATE_SUB(@today,INTERVAL 31 DAY);
SET @sub1month=DATE_SUB(@today,INTERVAL 1 MONTH);
SET @sub2month=DATE_SUB(@today,INTERVAL 2 MONTH);
SET @sub3month=DATE_SUB(@today,INTERVAL 3 MONTH);
SET @sub4month=DATE_SUB(@today,INTERVAL 4 MONTH);
SET @sub5month=DATE_SUB(@today,INTERVAL 5 MONTH);
SET @sub6month=DATE_SUB(@today,INTERVAL 6 MONTH);
SET @sub7month=DATE_SUB(@today,INTERVAL 7 MONTH);
SET @sub8month=DATE_SUB(@today,INTERVAL 8 MONTH);
SET @sub9month=DATE_SUB(@today,INTERVAL 9 MONTH);
SET @sub10month=DATE_SUB(@today,INTERVAL 10 MONTH);
SET @sub11month=DATE_SUB(@today,INTERVAL 11 MONTH);

-- - - - - - - - - - - - - - - - - - - - - - - - 
-- Volcado de datos para la tabla `Category` - -
-- - - - - - - - - - - - - - - - - - - - - - - -
TRUNCATE TABLE `Category`;
INSERT INTO Category (`categoryId`, `categoryName`) VALUES
(1, 'Electronica'),
(2, 'Informatica'),
(3, 'Deportes'),
(4, 'Juegos'),
(5, 'Viajes'),
(6, 'Ropa');


-- - - - - - - - - - - - - - - - - - - - - - 
-- Volcado de datos para la tabla `Card` - -
-- - - - - - - - - - - - - - - - - - - - - -
TRUNCATE TABLE `Card`;
INSERT INTO `Card` (`numberId`, `expiryDate`) VALUES
(4417123456789113, DATE_ADD(@today,INTERVAL 2 YEAR)),
(1234567891234567, DATE_ADD(@today,INTERVAL 3 YEAR)),
(3759876543210011, DATE_ADD(@today,INTERVAL 1 YEAR)),
(4973000000000000, DATE_ADD(@today,INTERVAL 4 YEAR));


-- - - - - - - - - - - - - - - - - - - - - - - - - - 
-- Volcado de datos para la tabla `UserProfile`  - -
-- - - - - - - - - - - - - - - - - - - - - - - - - -
-- Todas las contrasenas son pass
TRUNCATE TABLE `UserProfile`;
INSERT INTO `UserProfile` (`usrId`, `loginName`, `enPassword`, `firstName`, `lastName`, `email`, `card`) VALUES
(1, 'fcedron' , 'NRYlpSlGPSxpg', 'Francisco Abel', 'Cedrón Santaeufemia', 'francisco.cedron@udc.es', 4417123456789113),
(2, 'nlrivas' , 'NRYlpSlGPSxpg', 'Nuria'         , 'López Rivas'        , 'nrlivas@udc.es'         , 3759876543210011),
(3, 'lucas38' , 'NRYlpSlGPSxpg', 'Lucas'         , 'Peréz González'     , 'lucas38@hotmail.com'    , NULL),
(4, 'lucerina', 'NRYlpSlGPSxpg', 'Lucía'         , 'Antón Arias'        , 'lucerina@gmail.com'     , 4417123456789113),
(5, 'kikeloco', 'NRYlpSlGPSxpg', 'Enrique'       , 'Cadena Piñeiros'    , 'kikeloco@yahoo.com'     , 4973000000000000),
(6, 'juanca'  , 'NRYlpSlGPSxpg', 'Juan Carlos'   , 'Fonseca Alarcon'    , 'theking@terra.es'       , 1234567891234567),
(7, 'espera'  , 'NRYlpSlGPSxpg', 'Esperanza'     , 'Diaz Casas'         , 'esperasentado@gmail.com', 1234567891234567),
(8, 'sandia'  , 'NRYlpSlGPSxpg', 'Sandra'        , 'Herazo Maya'        , 'sandia@yopmail.com'     , NULL),
(9, 'stella'  , 'NRYlpSlGPSxpg', 'Stella'        , 'Smith'              , 'seestrella@yopmail.com' , NULL);

-- - - - - - - - - - - - - - - - - - - - - - - - 
-- Volcado de datos para la tabla `Product`  - -
-- - - - - - - - - - - - - - - - - - - - - - - -
TRUNCATE TABLE `Product`;
INSERT INTO `Product` (`productId`, `productName`, `Category`, `owner`, `description`,  `initDate`, `endDate`, `initPrice`, `shipmentDescription`, `winBid`, `currentPrice`, version) VALUES
(1, 'Motor Nema 17', 1, 1, 'Motor Nema 17 4.8kg Nuevo!!!', @sub2month, TIMESTAMPADD(MONTH,5,CURRENT_TIMESTAMP()), 10.05, 'Se envía por correo certificado', NULL, NULL, 1),
(2, 'Mac Book Air', 2, 1, 'Mac Book Air 13". Modelo A1369', @sub1month, TIMESTAMPADD(MONTH,2,CURRENT_TIMESTAMP()), 300.00, 'Se envía por DHL', NULL, NULL, 1),
(3, 'Bicicleta Trek', 3, 2, 'Bicicleta Trek de carbono con Durace', @sub1day, TIMESTAMPADD(MONTH,2,CURRENT_TIMESTAMP()), 400.50, 'Se entrega en mano', 304, 500.5, 1),
(4, 'GTA V', 4, 2, 'GTA V para PC', @sub10day, TIMESTAMPADD(MONTH,4,CURRENT_TIMESTAMP()), 0.01, 'Se envia por correo estandar', NULL, NULL, 1),
(5, 'Troller', 5, 4, 'Maleta Troller de 15L', @sub2month, TIMESTAMPADD(MONTH,8,CURRENT_TIMESTAMP()), 9.50, 'Se envía por UPS', NULL, NULL, 1),
(6, 'Gafas Maximo Dutti', 6, 5, 'Gafas Maximo Dutti Negras', @sub20day, TIMESTAMPADD(DAY,2,CURRENT_TIMESTAMP()), 120.12, 'Se entrega en mano', NULL, NULL, 1),
(7, 'Cinturon hombre', 6, 6, 'Cinturon Kalvin klein', @sub25day, TIMESTAMPADD(MONTH,10,CURRENT_TIMESTAMP()), 1.00, 'Recogida en local', NULL, NULL, 1),
(8, 'Zapatilla Kawasaki', 6, 4, 'Calzado de color rosa. Talla 34.', @sub1month, TIMESTAMPADD(DAY,3,CURRENT_TIMESTAMP()), 8.00, 'Envío por correo certificado', NULL, NULL, 1),
(9, 'Cocina a gas', 5, 2, 'Cocina a gas campegio a estrenar', @sub12day, TIMESTAMPADD(DAY,16,CURRENT_TIMESTAMP()), 14.90, 'Entrega en local', NULL, NULL, 1),
(10, 'Adaptador de enchufe', 5, 1, 'Adaptador Universal USA-UK', @sub6day,TIMESTAMPADD(DAY,6,CURRENT_TIMESTAMP()), 1.30, 'Entrega por la compañía Saime', NULL, NULL, 1),
(11, 'Organizador de viaje', 5, 1, 'Organizador de viaje Swiss-Gear(R) - Con cierre de cremallera y gancho para colgar', @sub5day, TIMESTAMPADD(MONTH,4,CURRENT_TIMESTAMP()), 7.90, 'Se entrega en mano', NULL, NULL, 1),
(12, 'Neceser', 5, 1, 'Maletín neceser de talenti - NUEVO', @sub4day, TIMESTAMPADD(MINUTE,5,CURRENT_TIMESTAMP()), 15.00, 'Envió por UPS', NULL, NULL, 1),
(13, 'Candado', 5, 5, 'Candado para maleta con combinación de números', @sub6day, TIMESTAMPADD(MINUTE,14,CURRENT_TIMESTAMP()), 6.29, 'Se envía a traves de la compañia DHL', NULL, NULL, 1),
(14, 'Maleta', 5, 6, 'Maleta Saxoline Jumbotroller, XXL', @sub3month, TIMESTAMPADD(MINUTE,9,CURRENT_TIMESTAMP()), 140.00, 'Entrega en mano', NULL, NULL, 1),
(15, 'GTA IV', 3, 2, 'GTA IV para PC', @sub1month, TIMESTAMPADD(MINUTE,6,CURRENT_TIMESTAMP()), 30.00, 'Envío por UPS', NULL, NULL, 1),
(16, 'GTA IV', 3, 1, 'Juego gta 4 para PS3', @sub6day, TIMESTAMPADD(MINUTE,20,CURRENT_TIMESTAMP()), 29.90, 'Se realiza por DHL', NULL, NULL, 1),
(18, 'Batería P-BELL', 2, 4, 'Batería Packard DELL TJ63', @sub4day, @sub3day, 25.88, 'Mediante UPS', NULL, NULL, 1),
(19, 'HDD 40gb', 2, 1, 'Disco Duro 40GB IDE', @sub4day, @sub1day, 13.95, 'Entrega en mano', NULL, NULL, 1),
(20, 'Resistencias 1k', 1, 1, '1000 RESISTENCIAS DE 1KOHMIO', @sub24day, TIMESTAMPADD(DAY,10,CURRENT_TIMESTAMP()), 1.75, 'Envío por carta estandar', NULL, NULL, 1),
(21, 'DisplayPort Adapter', 2, 1, 'Adaptador MiniDisplayPort a HDMI', @sub26day, TIMESTAMPADD(DAY,10,CURRENT_TIMESTAMP()), 12.11, 'Envió por UPS', NULL, NULL, 1),
(22, 'Adaptador MiniDisplayPort', 2, 2, 'Adaptador de VGA a MiniDisplayPort', @sub23day, TIMESTAMPADD(MONTH,9,CURRENT_TIMESTAMP()), 12.11, 'Envió por UPS', NULL, NULL, 1),
(23, 'Monitor 15"', 2, 1, 'Monitor 15" TFT 1024x768pixels', @sub24day, TIMESTAMPADD(MONTH,7,CURRENT_TIMESTAMP()), 10.10, 'Envió por DHL', NULL, NULL, 1),
(24, 'Arduino UNO', 1, 4, 'Arduino UNO R3 con cable USB', @sub1month, TIMESTAMPADD(MONTH,7,CURRENT_TIMESTAMP()), 5.50, 'Envió por correo certificado', NULL, NULL, 1),
(25, 'Arduino MEGA', 1, 4, 'Arduino MEGA 2560', @sub1month, TIMESTAMPADD(MONTH,7,CURRENT_TIMESTAMP()), 10.00, 'Envió por UPS', NULL, NULL, 1),
(26, 'Arduino UNO', 1, 1, 'Fundino (clon de arduino uno)', @sub1month, TIMESTAMPADD(MONTH,7,CURRENT_TIMESTAMP()), 4.00, 'Envió por UPS', NULL, NULL, 1),
(27, 'Sensor de humedad', 1, 1, 'Humidity and Temperaty sensor DHT11', @sub2month, TIMESTAMPADD(MONTH,7,CURRENT_TIMESTAMP()), 2.10, 'Envió por UPS', NULL, NULL, 1),
(28, 'Protoboard', 1, 1, 'Placa de desarrollo (16,5 x 5,8 x 1,0 cm)', @sub4month, TIMESTAMPADD(MONTH,7,CURRENT_TIMESTAMP()), 8.20, 'Envió por UPS', NULL, NULL, 1),
(29, 'Chip ATMEL', 1, 1, 'ATMEL AVR-16PU ATEMEGA8 8bits', @sub3day, TIMESTAMPADD(MONTH,7,CURRENT_TIMESTAMP()), 0.70, 'Envió por UPS', NULL, NULL, 1),
(30, 'Disipador AEROCOOL', 2 , 2, 'Disipador AEROCOOL 0db para socket 2011, 1155, 1156', @sub13day, TIMESTAMPADD(MONTH,7,CURRENT_TIMESTAMP()), 22.65, 'Recogida en local', NULL, NULL, 1),
(31, 'Pala de padel', 3, 1, 'Pala de padel SLAZENGER', @sub9day, TIMESTAMPADD(MONTH,7,CURRENT_TIMESTAMP()), 6.50, 'Mediante UPS', NULL, NULL, 1),
(32, 'Banco de abdominales', 3, 2, 'Banco de abdominales AB Rocket twister', @sub9day, TIMESTAMPADD(MONTH,7,CURRENT_TIMESTAMP()), 29.95, 'Envió por correo urgente', NULL, NULL, 1),
(33, 'Perneras de ciclismo', 3, 1, 'Perneras de ciclismo Specialized', @sub9day, TIMESTAMPADD(MONTH,11,CURRENT_TIMESTAMP()), 13.00, 'Envió por UPS', NULL, NULL, 1),
(34, 'FIFA 14 (PS3)', 4, 1, 'FIFA 14 para PS3', @sub9day, TIMESTAMPADD(MONTH,14,CURRENT_TIMESTAMP()), 15.00, 'Envió por paquete azul', NULL, NULL, 1),
(35, 'FIFA 14 (xBOX)', 4, 2, 'FIFA 14 para XBOX', @sub9day, TIMESTAMPADD(DAY,24,CURRENT_TIMESTAMP()), 10.00, 'Envió por UPS', NULL, NULL, 1),
(36, 'The last of us (PS3)', 4, 1, 'Juego The last of us nuevo para PS3', @sub9day, TIMESTAMPADD(MONTH,3,CURRENT_TIMESTAMP()), 15.00, 'Envió por paquete azul', NULL, NULL, 1),
(37, 'FIFA 13 (PC)', 4, 1, 'FIFA 14 para PC', @sub13day, TIMESTAMPADD(MONTH,3,CURRENT_TIMESTAMP()), 5.00, 'Envió por DHL', NULL, NULL, 1),
(38, 'GT 6', 4, 1, 'Juego Gran Turismo para PS3', @sub13day, TIMESTAMPADD(DAY,7,CURRENT_TIMESTAMP()), 35.00, 'Envió por paquete azul', NULL, NULL, 1),
(39, 'Assasins Creed 3', 4, 1, 'Assasins Creed 3 para PS3 ¡¡Sin desprencintar!!!', @sub13day, TIMESTAMPADD(DAY,70,CURRENT_TIMESTAMP()), 55.00, 'Envió por paquete azul', NULL, NULL, 1),
(40, 'Cinta kapton', 1, 1, '100 metros de cinta kapton de 8cm de ancho', @sub13day, TIMESTAMPADD(MONTH,2,CURRENT_TIMESTAMP()), 20.00, 'Envió por correos', NULL, NULL, 1),
(41, 'Pack de juegos XBOX 360', 4, 2, 'Mass effect, Gears of war, Gears of war 2, Gears of war 3, Gears of war Judgment, Lost Odyssey', @sub13day, '2014-04-04 04:04:00', 10.00, 'Envió por correos', NULL, NULL, 1),
(42, 'Netbook ACER', 2, 1, 'Portátil netbook acer aspire one zg5', @sub13day, TIMESTAMPADD(MONTH,2,CURRENT_TIMESTAMP()), 200.00, 'Envió por correos', NULL, NULL, 1),
(43, '2GB de RAMM', 2, 2, '2GB de RAMM DDR3 1667HZ CL10', @sub13day, TIMESTAMPADD(MONTH,2,CURRENT_TIMESTAMP()), 20.00, 'Envió por correos', NULL, NULL, 1),
(44, 'Arduino Pro', 1, 2, 'Arduino PRO 2560', @sub26day, TIMESTAMPADD(MONTH,6,CURRENT_TIMESTAMP()), 7.50, 'Envió por paquete azul', NULL, NULL, 1),
(45, '10 LEDs', 1, 2, '10 LEDs de colores variados', @sub26day, TIMESTAMPADD(MONTH,9,CURRENT_TIMESTAMP()), 0.50, 'Envió por correo normal', NULL, NULL, 1),
(46, 'Servo', 1, 2, 'servo 180grados', @sub26day, TIMESTAMPADD(MONTH,2,CURRENT_TIMESTAMP()), 2.50, 'Envió por UPS', NULL, NULL, 1),
(47, 'Jumpers', 1, 2, '1000 jumpers para arduino', @sub21day, TIMESTAMPADD(DAY,9,CURRENT_TIMESTAMP()), 2.00, 'Envió por UPS', NULL, NULL, 1);


-- - - - - - - - - - - - - - - - - - - - - - 
-- Volcado de datos para la tabla `Bid`  - -
-- - - - - - - - - - - - - - - - - - - - - -
TRUNCATE TABLE `Bid`;
INSERT INTO `Bid` (`bidId`, `bidDate`, `maxPrice`, `product`, `owner`) VALUES
-- Apuesta para el producto uno que esta cerrada (gana el usuario 4 quedando el valor en 15.50)
(100, CONCAT_WS(' ', @sub30day, CURTIME()), 12.00, 1, 2),
(101, CONCAT_WS(' ', @sub29day, CURTIME()), 13.00, 1, 3),
(102, CONCAT_WS(' ', @sub28day, CURTIME()), 13.00, 1, 2),
(103, CONCAT_WS(' ', @sub27day, CURTIME()), 13.70, 1, 3),
(104, CONCAT_WS(' ', @sub26day, CURTIME()), 15.00, 1, 2),
(105, CONCAT_WS(' ', @sub25day, CURTIME()), 15.00, 1, 4),
(106, CONCAT_WS(' ', @sub24day, CURTIME()), 15.50, 1, 4);
UPDATE `Product`
SET    `winBid` = 106,
	   `currentPrice` = 15.50
WHERE  `productId` = 1;

INSERT INTO `Bid` (`bidId`, `bidDate`, `maxPrice`, `product`, `owner`) VALUES
-- Apuesta para el producto 2 (va ganando el usuario 2 con un valor de 380.5)
(200, CONCAT_WS(' ', @sub29day, '11:06:04'), 312.00, 2, 2),
(201, CONCAT_WS(' ', @sub29day, '12:00:00'), 305.00, 2, 3),
(202, CONCAT_WS(' ', @sub29day, '12:06:04'), 310.00, 2, 3),
(203, CONCAT_WS(' ', @sub25day, '17:00:04'), 320.70, 2, 4),
(204, CONCAT_WS(' ', @sub24day, '21:25:04'), 400.00, 2, 2),
(205, CONCAT_WS(' ', @sub24day, '22:32:04'), 350.00, 2, 4),
(206, CONCAT_WS(' ', @sub23day, '10:46:04'), 380.00, 2, 4);
UPDATE `Product`
SET    `winBid` = 204,
	   `currentPrice` = 380.50
WHERE  `productId` = 2;

INSERT INTO `Bid` (`bidId`, `bidDate`, `maxPrice`, `product`, `owner`) VALUES
-- Apuesta para el producto 3 (va ganando el usuario 1 con un valor de 500.5)
(300, CONCAT_WS(' ', @sub23day, '11:06:04'), 450.00, 3, 1),
(301, CONCAT_WS(' ', @sub19day, '12:00:00'), 405.00, 3, 4),
(302, CONCAT_WS(' ', @sub19day, '12:05:04'), 410.00, 3, 4),
(303, CONCAT_WS(' ', @sub19day, '12:08:14'), 460.70, 3, 4),
(304, CONCAT_WS(' ', @sub9day , '21:25:04'), 550.00, 3, 1),
(305, CONCAT_WS(' ', @sub4day , '22:32:04'), 480.00, 3, 5),
(306, CONCAT_WS(' ', @sub3day , '10:46:04'), 500.00, 3, 6);
UPDATE `Product`
SET    `winBid` = 304,
	   `currentPrice` = 500.50
WHERE  `productId` = 3;

INSERT INTO `Bid` (`bidId`, `bidDate`, `maxPrice`, `product`, `owner`) VALUES
-- Apuesta para el producto 4 (va ganando el usuario 6 con un valor de 560.5)
(400, CONCAT_WS(' ', @sub23day, '11:06:04'), 450.00, 4, 1),
(401, CONCAT_WS(' ', @sub19day, '12:00:00'), 405.00, 4, 4),
(402, CONCAT_WS(' ', @sub19day, '12:05:04'), 410.00, 4, 4),
(403, CONCAT_WS(' ', @sub19day, '12:08:14'), 460.70, 4, 4),
(404, CONCAT_WS(' ', @sub9day, '21:25:04'), 550.00, 4, 1),
(405, CONCAT_WS(' ', @sub4day, '22:32:04'), 480.00, 4, 5),
(406, CONCAT_WS(' ', @sub3day, '10:46:04'), 600.00, 4, 6),
(407, CONCAT_WS(' ', @sub2day, '21:25:04'), 560.00, 4, 1);
UPDATE `Product`
SET    `winBid` = 406,
	   `currentPrice` = 560.50
WHERE  `productId` = 4;

INSERT INTO `Bid` (`bidId`, `bidDate`, `maxPrice`, `product`, `owner`) VALUES
-- Apuesta para el producto 5 (va ganando el usuario 5 con un valor de 16.5)
(500, CONCAT_WS(' ', @sub23day, '11:06:04'), 10.00, 5, 1),
(501, CONCAT_WS(' ', @sub19day, '12:00:00'), 14.00, 5, 2),
(502, CONCAT_WS(' ', @sub19day, '12:05:04'), 12.00, 5, 3),
(503, CONCAT_WS(' ', @sub19day, '12:08:14'), 16.70, 5, 5),
(504, CONCAT_WS(' ', @sub9day, '21:25:04'), 15.00, 5, 6),
(505, CONCAT_WS(' ', @sub4day, '22:32:04'), 16.00, 5, 1);
UPDATE `Product`
SET    `winBid` = 503,
	   `currentPrice` = 16.50
WHERE  `productId` = 5;

INSERT INTO `Bid` (`bidId`, `bidDate`, `maxPrice`, `product`, `owner`) VALUES
-- Apuesta para el producto 6 (va ganando el usuario 6 con un valor de 120.62)
(600, CONCAT_WS(' ', @sub23day, '11:06:04'), 150.00, 6, 1);
UPDATE `Product`
SET    `winBid` = 600,
	   `currentPrice` = 120.62
WHERE  `productId` = 6;

INSERT INTO `Bid` (`bidId`, `bidDate`, `maxPrice`, `product`, `owner`) VALUES
-- (7, 'Cinturon hombre', 6, 6, 'Cinturon Kalvin klein', '2014-01-20 05:06:04', '2014-10-12 05:07:38', 1.00, 'Recogida en local', NULL, NULL, 1),
(700, CONCAT_WS(' ', @sub23day, '21:06:04'), 1.50, 7, 1),
(701, CONCAT_WS(' ', @sub19day, '22:00:00'), 2.00, 7, 2),
(702, CONCAT_WS(' ', @sub19day, '22:05:04'), 5.50, 7, 1),
(703, CONCAT_WS(' ', @sub19day, '22:08:14'), 5.70, 7, 2),
(704, CONCAT_WS(' ', @sub8day, '21:25:04'), 15.00, 7, 1),
(705, CONCAT_WS(' ', @sub4day, '22:32:04'), 8.00, 7, 2);
UPDATE `Product`
SET    `winBid` = 704,
	   `currentPrice` = 8.50
WHERE  `productId` = 7;

INSERT INTO `Bid` (`bidId`, `bidDate`, `maxPrice`, `product`, `owner`) VALUES
-- (8, 'Zapatilla Kawasaki', 6, 4, 'Calzado de color rosa. Talla 34.', '2014-01-20 05:06:04', '2014-02-27 05:06:04', 8.00, 'Envío por correo certificado', NULL, NULL, 1),
(800, CONCAT_WS(' ', @sub23day, '21:06:04'), 10.50, 8, 1),
(801, CONCAT_WS(' ', @sub19day, '22:00:00'), 20.00, 8, 2),
(802, CONCAT_WS(' ', @sub19day, '22:05:04'), 50.50, 8, 1),
(803, CONCAT_WS(' ', @sub19day, '22:08:14'), 50.70, 8, 2),
(804, CONCAT_WS(' ', @sub8day, '21:25:04'), 65.00, 8, 1),
(805, CONCAT_WS(' ', @sub5day, '22:32:04'), 80.00, 8, 2);
UPDATE `Product`
SET    `winBid` = 805,
	   `currentPrice` = 65.50
WHERE  `productId` = 8;

INSERT INTO `Bid` (`bidId`, `bidDate`, `maxPrice`, `product`, `owner`) VALUES
-- (9, 'Cocina a gas', 5, 2, 'Cocina a gas campegio a estrenar', '2014-02-10 05:06:04', '2014-03-10 05:06:04', 14.90, 'Entrega en local', NULL, NULL, 1),
(900, CONCAT_WS(' ', @sub23day, '21:06:04'), 15.00, 9, 1),
(901, CONCAT_WS(' ', @sub19day, '22:00:00'), 20.00, 9, 2),
(902, CONCAT_WS(' ', @sub19day, '22:05:04'), 50.50, 9, 1),
(903, CONCAT_WS(' ', @sub19day, ':08:14'), 50.70, 9, 2),
(904, CONCAT_WS(' ', @sub8day, '21:25:04'), 65.00, 9, 1),
(905, CONCAT_WS(' ', @sub5day, '22:32:04'), 80.00, 9, 2);
UPDATE `Product`
SET    `winBid` = 905,
	   `currentPrice` = 65.50
WHERE  `productId` = 9;

INSERT INTO `Bid` (`bidId`, `bidDate`, `maxPrice`, `product`, `owner`) VALUES
-- (10, 'Adaptador de enchufe', 5, 1, 'Adaptador Universal USA-UK', '2014-01-20 05:06:04', '2014-03-01 05:06:04', 1.30, 'Entrega por la compañía Saime', NULL, NULL, 1),
(1000, CONCAT_WS(' ', @sub23day, '21:06:04'), 5.00, 10, 1),
(1001, CONCAT_WS(' ', @sub19day, '22:00:00'), 2.00, 10, 2),
(1002, CONCAT_WS(' ', @sub19day, '22:05:04'), 5.50, 10, 2),
(1003, CONCAT_WS(' ', @sub19day, '22:08:14'), 7.00, 10, 1),
(1004, CONCAT_WS(' ', @sub8day, '21:25:04'), 7.50, 10, 3),
(1005, CONCAT_WS(' ', @sub5day, '22:32:04'), 8.00, 10, 2);
UPDATE `Product`
SET    `winBid` = 1005,
	   `currentPrice` = 8.00
WHERE  `productId` = 10;

INSERT INTO `Bid` (`bidId`, `bidDate`, `maxPrice`, `product`, `owner`) VALUES
-- (11, 'Organizador de viaje', 5, 1, 'Organizador de viaje Swiss-Gear(R) - Con cierre de cremallera y gancho para colgar', '2014-01-20 05:06:04', '2014-05-09 09:09:09', 7.90, 'Se entrega en mano', NULL, NULL, 1),
(110, CONCAT_WS(' ', @sub23day, '21:06:04'), 8.00, 11, 3),
(111, CONCAT_WS(' ', @sub19day, '22:00:00'), 9.00, 11, 2),
(112, CONCAT_WS(' ', @sub19day, '22:05:04'), 10.50, 11, 3),
(113, CONCAT_WS(' ', @sub19day, '22:08:14'), 11.00, 11, 4),
(114, CONCAT_WS(' ', @sub8day, '21:25:04'), 14.50, 11, 5),
(115, CONCAT_WS(' ', @sub5day, '22:32:04'), 12.00, 11, 6);
UPDATE `Product`
SET    `winBid` = 114,
	   `currentPrice` = 12.50
WHERE  `productId` = 11;

-- MySQL – Enable Foreign Key Checks or Constraints
SET foreign_key_checks = 1;