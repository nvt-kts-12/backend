insert into authority (id, deleted, name) values (1, false, "ROLE_REGISTERED");
insert into authority (id, deleted, name) values (2, false, "ROLE_ADMIN");

insert into admins (id, first_name, last_name, password, username) values
(1, "Admin", "Admin", "$2y$12$Cg76kI/v91EAqOjRWSrr/utljtY604voeDEJ1ldYq46iWTo7Mo1wO", "admin" );
-- password Admin021!

insert into admin_authority(authority_id, admin_id) values(2,1);

insert into users (id, first_name, last_name, password, username, email) values
(0, "UserFirstName", "UserLastName", "$2b$10$MbpAOLT5iU2OTOFMOBO4C.lCLUogU0VY3K.myRwMVuI.Cgi3prINO", "user", "user@gmail.com" );
-- password User123!

insert into user_authority(authority_id, user_id) values (1, 0);

INSERT INTO `location_scheme` (`id`, `deleted`, `address`, `name`) VALUES
(1,0,'Petrovaradin, Novi Sad','Petrovaradinska trvrdjava'),
(2,0,'Sutjeska 2, Novi Sad','Spens'),
(3,0,'Pozorisni trg 1, Novi Sad','Srpsko narodno pozoriste'),
(4,0,'Bul. Mihajla Pupina 3, Novi Sad','Arena Cineplex');

INSERT INTO `sector` (`id`, `deleted`, `bottom_rightx`, `bottom_righty`, `capacity`, `col_num`, `row_num`, `top_leftx`, `top_lefty`, `type`, `location_scheme_id`) VALUES
 (1,0,489,109,200,0,0,12,9,'PARTER',1),
 (2,0,485,221,50,5,10,15,116,'GRANDSTAND',1),
 (3,0,479,291,30,3,10,18,232,'GRANDSTAND',1),
 (4,0,413,83,40,4,10,94,18,'GRANDSTAND',2),
 (5,0,416,286,40,4,10,99,219,'GRANDSTAND',2),
 (6,0,86,292,40,4,10,4,19,'GRANDSTAND',2),
 (7,0,489,287,40,4,10,423,21,'GRANDSTAND',2),
 (8,0,414,218,100,0,0,98,87,'PARTER',2),
 (9,0,495,207,50,5,10,13,8,'GRANDSTAND',3),
 (10,0,490,292,20,10,2,19,215,'GRANDSTAND',3),
 (11,0,368,191,16,4,4,140,25,'GRANDSTAND',4),
 (12,0,485,293,30,10,3,18,199,'GRANDSTAND',4);

INSERT INTO `location` (`id`, `deleted`, `scheme_id`) VALUES (1,0,1),(2,0,1),(3,0,1),(4,0,2),(5,0,3),(6,0,4);

INSERT INTO `location_sector` (`id`, `deleted`, `capacity`, `price`, `vip`, `location_id`, `sector_id`) VALUES
(1,0,200,2000, 1,1,1),
(2,0,50,2500, 1,1,2),
(3,0,30,10000, 1,1,3),
(4,0,200,2000,0,2,1),
(5,0,50,3000,0,2,2),
(6,0,30,10000,1,2,3),
(7,0,200,3000,0,3,1),
(8,0,50,4000, 0,3,2),
(9,0,30,15000, 1,3,3),
(10,0,40,500, 0,4,4),
(11,0,40,500, 0,4,5),
(12,0,50,600, 0,5,9),
(13,0,16,400, 0,6,11),
(14,0,30,600, 1,6,12);

INSERT INTO `event` (`id`, `deleted`, `category`, `description`, `name`) VALUES
(1,0,'ENTERTAINMENT','Exit 20th birthday!','EXIT 2.0'),
(2,0,'SPORT','Dodjite da navijamo','Odbojka Vojvodina - Proleter Zrenjanin'),
(3,0,'CULTURAL','Nova izvedba cuvenog baleta','Balet Labudovo jezero'),
(4,0,'ENTERTAINMENT','Milos Bikovic u glavnoj ulozi','Juzni vetar 2');

INSERT INTO `event_day` (`id`, `deleted`, `date`, `reservation_expiration_date`, `state`, `event_id`, `location_id`) VALUES
(1,0,'2020-06-05','2020-05-25','RESERVABLE_AND_BUYABLE',1,1),
(2,0,'2020-06-06','2020-05-26','RESERVABLE_AND_BUYABLE',1,2),
(3,0,'2020-06-07','2020-05-27','RESERVABLE_AND_BUYABLE',1,3),
(4,0,'2020-03-18','2020-03-17','RESERVABLE_AND_BUYABLE',2,4),
(5,0,'2020-02-28','2020-02-27','RESERVABLE_AND_BUYABLE',3,5),
(6,0,'2020-02-14','2020-02-13','RESERVABLE_AND_BUYABLE',4,6);



INSERT INTO `ticket` (`id`, `deleted`, `price`, `seat_col`, `seat_row`, `sector_id`, `sector_type`, `sold`, `vip`, `event_day_id`, `user_id`) VALUES
(1,0,500,1,1,4,'GRANDSTAND',0,0,4,NULL),
(2,0,500,2,1,4,'GRANDSTAND',0,0,4,NULL),
(3,0,500,3,1,4,'GRANDSTAND',0,0,4,NULL),
(4,0,500,4,1,4,'GRANDSTAND',0,0,4,NULL),
(5,0,500,1,2,4,'GRANDSTAND',0,0,4,NULL),
(6,0,500,2,2,4,'GRANDSTAND',0,0,4,NULL),
(7,0,500,3,2,4,'GRANDSTAND',0,0,4,NULL),
(8,0,500,4,2,4,'GRANDSTAND',0,0,4,NULL),
(9,0,500,1,3,4,'GRANDSTAND',0,0,4,NULL),
(10,0,500,2,3,4,'GRANDSTAND',0,0,4,NULL),(11,0,500,3,3,4,'GRANDSTAND',0,0,4,NULL),(12,0,500,4,3,4,'GRANDSTAND',0,0,4,NULL),(13,0,500,1,4,4,'GRANDSTAND',0,0,4,NULL),(14,0,500,2,4,4,'GRANDSTAND',0,0,4,NULL),(15,0,500,3,4,4,'GRANDSTAND',0,0,4,NULL),(16,0,500,4,4,4,'GRANDSTAND',0,0,4,NULL),(17,0,500,1,5,4,'GRANDSTAND',0,0,4,NULL),(18,0,500,2,5,4,'GRANDSTAND',0,0,4,NULL),(19,0,500,3,5,4,'GRANDSTAND',0,0,4,NULL),(20,0,500,4,5,4,'GRANDSTAND',0,0,4,NULL),(21,0,500,1,6,4,'GRANDSTAND',0,0,4,NULL),(22,0,500,2,6,4,'GRANDSTAND',0,0,4,NULL),(23,0,500,3,6,4,'GRANDSTAND',0,0,4,NULL),(24,0,500,4,6,4,'GRANDSTAND',0,0,4,NULL),(25,0,500,1,7,4,'GRANDSTAND',0,0,4,NULL),(26,0,500,2,7,4,'GRANDSTAND',0,0,4,NULL),(27,0,500,3,7,4,'GRANDSTAND',0,0,4,NULL),(28,0,500,4,7,4,'GRANDSTAND',0,0,4,NULL),(29,0,500,1,8,4,'GRANDSTAND',0,0,4,NULL),(30,0,500,2,8,4,'GRANDSTAND',0,0,4,NULL),(31,0,500,3,8,4,'GRANDSTAND',0,0,4,NULL),(32,0,500,4,8,4,'GRANDSTAND',0,0,4,NULL),(33,0,500,1,9,4,'GRANDSTAND',0,0,4,NULL),(34,0,500,2,9,4,'GRANDSTAND',0,0,4,NULL),(35,0,500,3,9,4,'GRANDSTAND',0,0,4,NULL),(36,0,500,4,9,4,'GRANDSTAND',0,0,4,NULL),(37,0,500,1,10,4,'GRANDSTAND',0,0,4,NULL),(38,0,500,2,10,4,'GRANDSTAND',0,0,4,NULL),(39,0,500,3,10,4,'GRANDSTAND',0,0,4,NULL),(40,0,500,4,10,4,'GRANDSTAND',0,0,4,NULL),(41,0,500,1,1,5,'GRANDSTAND',0,0,4,NULL),(42,0,500,2,1,5,'GRANDSTAND',0,0,4,NULL),(43,0,500,3,1,5,'GRANDSTAND',0,0,4,NULL),(44,0,500,4,1,5,'GRANDSTAND',0,0,4,NULL),(45,0,500,1,2,5,'GRANDSTAND',0,0,4,NULL),(46,0,500,2,2,5,'GRANDSTAND',0,0,4,NULL),(47,0,500,3,2,5,'GRANDSTAND',0,0,4,NULL),(48,0,500,4,2,5,'GRANDSTAND',0,0,4,NULL),(49,0,500,1,3,5,'GRANDSTAND',0,0,4,NULL),(50,0,500,2,3,5,'GRANDSTAND',0,0,4,NULL),(51,0,500,3,3,5,'GRANDSTAND',0,0,4,NULL),(52,0,500,4,3,5,'GRANDSTAND',0,0,4,NULL),(53,0,500,1,4,5,'GRANDSTAND',0,0,4,NULL),(54,0,500,2,4,5,'GRANDSTAND',0,0,4,NULL),(55,0,500,3,4,5,'GRANDSTAND',0,0,4,NULL),(56,0,500,4,4,5,'GRANDSTAND',0,0,4,NULL),(57,0,500,1,5,5,'GRANDSTAND',0,0,4,NULL),(58,0,500,2,5,5,'GRANDSTAND',0,0,4,NULL),(59,0,500,3,5,5,'GRANDSTAND',0,0,4,NULL),(60,0,500,4,5,5,'GRANDSTAND',0,0,4,NULL),(61,0,500,1,6,5,'GRANDSTAND',0,0,4,NULL),(62,0,500,2,6,5,'GRANDSTAND',0,0,4,NULL),(63,0,500,3,6,5,'GRANDSTAND',0,0,4,NULL),(64,0,500,4,6,5,'GRANDSTAND',0,0,4,NULL),(65,0,500,1,7,5,'GRANDSTAND',0,0,4,NULL),(66,0,500,2,7,5,'GRANDSTAND',0,0,4,NULL),(67,0,500,3,7,5,'GRANDSTAND',0,0,4,NULL),(68,0,500,4,7,5,'GRANDSTAND',0,0,4,NULL),(69,0,500,1,8,5,'GRANDSTAND',0,0,4,NULL),(70,0,500,2,8,5,'GRANDSTAND',0,0,4,NULL),(71,0,500,3,8,5,'GRANDSTAND',0,0,4,NULL),(72,0,500,4,8,5,'GRANDSTAND',0,0,4,NULL),(73,0,500,1,9,5,'GRANDSTAND',0,0,4,NULL),(74,0,500,2,9,5,'GRANDSTAND',0,0,4,NULL),(75,0,500,3,9,5,'GRANDSTAND',0,0,4,NULL),(76,0,500,4,9,5,'GRANDSTAND',0,0,4,NULL),(77,0,500,1,10,5,'GRANDSTAND',0,0,4,NULL),(78,0,500,2,10,5,'GRANDSTAND',0,0,4,NULL),(79,0,500,3,10,5,'GRANDSTAND',0,0,4,NULL),(80,0,500,4,10,5,'GRANDSTAND',0,0,4,NULL),(81,0,600,1,1,9,'GRANDSTAND',0,0,5,NULL),(82,0,600,2,1,9,'GRANDSTAND',0,0,5,NULL),(83,0,600,3,1,9,'GRANDSTAND',0,0,5,NULL),(84,0,600,4,1,9,'GRANDSTAND',0,0,5,NULL),(85,0,600,5,1,9,'GRANDSTAND',0,0,5,NULL),(86,0,600,1,2,9,'GRANDSTAND',0,0,5,NULL),(87,0,600,2,2,9,'GRANDSTAND',0,0,5,NULL),(88,0,600,3,2,9,'GRANDSTAND',0,0,5,NULL),(89,0,600,4,2,9,'GRANDSTAND',0,0,5,NULL),(90,0,600,5,2,9,'GRANDSTAND',0,0,5,NULL),(91,0,600,1,3,9,'GRANDSTAND',0,0,5,NULL),(92,0,600,2,3,9,'GRANDSTAND',0,0,5,NULL),(93,0,600,3,3,9,'GRANDSTAND',0,0,5,NULL),(94,0,600,4,3,9,'GRANDSTAND',0,0,5,NULL),(95,0,600,5,3,9,'GRANDSTAND',0,0,5,NULL),(96,0,600,1,4,9,'GRANDSTAND',0,0,5,NULL),(97,0,600,2,4,9,'GRANDSTAND',0,0,5,NULL),(98,0,600,3,4,9,'GRANDSTAND',0,0,5,NULL),(99,0,600,4,4,9,'GRANDSTAND',0,0,5,NULL),(100,0,600,5,4,9,'GRANDSTAND',0,0,5,NULL),(101,0,600,1,5,9,'GRANDSTAND',0,0,5,NULL),(102,0,600,2,5,9,'GRANDSTAND',0,0,5,NULL),(103,0,600,3,5,9,'GRANDSTAND',0,0,5,NULL),(104,0,600,4,5,9,'GRANDSTAND',0,0,5,NULL),(105,0,600,5,5,9,'GRANDSTAND',0,0,5,NULL),(106,0,600,1,6,9,'GRANDSTAND',0,0,5,NULL),(107,0,600,2,6,9,'GRANDSTAND',0,0,5,NULL),(108,0,600,3,6,9,'GRANDSTAND',0,0,5,NULL),(109,0,600,4,6,9,'GRANDSTAND',0,0,5,NULL),(110,0,600,5,6,9,'GRANDSTAND',0,0,5,NULL),(111,0,600,1,7,9,'GRANDSTAND',0,0,5,NULL),(112,0,600,2,7,9,'GRANDSTAND',0,0,5,NULL),(113,0,600,3,7,9,'GRANDSTAND',0,0,5,NULL),(114,0,600,4,7,9,'GRANDSTAND',0,0,5,NULL),(115,0,600,5,7,9,'GRANDSTAND',0,0,5,NULL),(116,0,600,1,8,9,'GRANDSTAND',0,0,5,NULL),(117,0,600,2,8,9,'GRANDSTAND',0,0,5,NULL),(118,0,600,3,8,9,'GRANDSTAND',0,0,5,NULL),(119,0,600,4,8,9,'GRANDSTAND',0,0,5,NULL),(120,0,600,5,8,9,'GRANDSTAND',0,0,5,NULL),(121,0,600,1,9,9,'GRANDSTAND',0,0,5,NULL),(122,0,600,2,9,9,'GRANDSTAND',0,0,5,NULL),(123,0,600,3,9,9,'GRANDSTAND',0,0,5,NULL),(124,0,600,4,9,9,'GRANDSTAND',0,0,5,NULL),(125,0,600,5,9,9,'GRANDSTAND',0,0,5,NULL),(126,0,600,1,10,9,'GRANDSTAND',0,0,5,NULL),(127,0,600,2,10,9,'GRANDSTAND',0,0,5,NULL),(128,0,600,3,10,9,'GRANDSTAND',0,0,5,NULL),(129,0,600,4,10,9,'GRANDSTAND',0,0,5,NULL),(130,0,600,5,10,9,'GRANDSTAND',0,0,5,NULL),(131,0,400,1,1,11,'GRANDSTAND',0,0,6,NULL),(132,0,400,2,1,11,'GRANDSTAND',0,0,6,NULL),(133,0,400,3,1,11,'GRANDSTAND',0,0,6,NULL),(134,0,400,4,1,11,'GRANDSTAND',0,0,6,NULL),(135,0,400,1,2,11,'GRANDSTAND',0,0,6,NULL),(136,0,400,2,2,11,'GRANDSTAND',0,0,6,NULL),(137,0,400,3,2,11,'GRANDSTAND',0,0,6,NULL),(138,0,400,4,2,11,'GRANDSTAND',0,0,6,NULL),(139,0,400,1,3,11,'GRANDSTAND',0,0,6,NULL),(140,0,400,2,3,11,'GRANDSTAND',0,0,6,NULL),(141,0,400,3,3,11,'GRANDSTAND',0,0,6,NULL),(142,0,400,4,3,11,'GRANDSTAND',0,0,6,NULL),(143,0,400,1,4,11,'GRANDSTAND',0,0,6,NULL),(144,0,400,2,4,11,'GRANDSTAND',0,0,6,NULL),(145,0,400,3,4,11,'GRANDSTAND',0,0,6,NULL),(146,0,400,4,4,11,'GRANDSTAND',0,0,6,NULL),(147,0,600,1,1,12,'GRANDSTAND',0,1,6,NULL),(148,0,600,2,1,12,'GRANDSTAND',0,1,6,NULL),(149,0,600,3,1,12,'GRANDSTAND',0,1,6,NULL),(150,0,600,4,1,12,'GRANDSTAND',0,1,6,NULL),(151,0,600,5,1,12,'GRANDSTAND',0,1,6,NULL),(152,0,600,6,1,12,'GRANDSTAND',0,1,6,NULL),(153,0,600,7,1,12,'GRANDSTAND',0,1,6,NULL),(154,0,600,8,1,12,'GRANDSTAND',0,1,6,NULL),(155,0,600,9,1,12,'GRANDSTAND',0,1,6,NULL),(156,0,600,10,1,12,'GRANDSTAND',0,1,6,NULL),(157,0,600,1,2,12,'GRANDSTAND',0,1,6,NULL),(158,0,600,2,2,12,'GRANDSTAND',0,1,6,NULL),(159,0,600,3,2,12,'GRANDSTAND',0,1,6,NULL),(160,0,600,4,2,12,'GRANDSTAND',0,1,6,NULL),(161,0,600,5,2,12,'GRANDSTAND',0,1,6,NULL),(162,0,600,6,2,12,'GRANDSTAND',0,1,6,NULL),(163,0,600,7,2,12,'GRANDSTAND',0,1,6,NULL),(164,0,600,8,2,12,'GRANDSTAND',0,1,6,NULL),(165,0,600,9,2,12,'GRANDSTAND',0,1,6,NULL),(166,0,600,10,2,12,'GRANDSTAND',0,1,6,NULL),(167,0,600,1,3,12,'GRANDSTAND',0,1,6,NULL),(168,0,600,2,3,12,'GRANDSTAND',0,1,6,NULL),(169,0,600,3,3,12,'GRANDSTAND',0,1,6,NULL),(170,0,600,4,3,12,'GRANDSTAND',0,1,6,NULL),(171,0,600,5,3,12,'GRANDSTAND',0,1,6,NULL),(172,0,600,6,3,12,'GRANDSTAND',0,1,6,NULL),(173,0,600,7,3,12,'GRANDSTAND',0,1,6,NULL),(174,0,600,8,3,12,'GRANDSTAND',0,1,6,NULL),(175,0,600,9,3,12,'GRANDSTAND',0,1,6,NULL),(176,0,600,10,3,12,'GRANDSTAND',0,1,6,NULL);



--insert into location_scheme (id, address, name) values
--(1, "Bul. Mihajla Pupina 3, Novi Sad", "Arena cineplex"),
--(2, "Sutjeska 2, Novi Sad", "Spens");
--
--insert into sector (id, bottom_rightx, bottom_righty, top_leftx, top_lefty, capacity, col_num, row_num, type, location_scheme_id) values
--(1, "150", "200", "100", "50", 5, 5, 1, "GRANDSTAND", 1 ), -- arena
--(2, "400", "200", "350", "50", 6, 2, 3, "GRANDSTAND", 1 ), -- arena
--
--(3, "150", "200", "100", "50", 25, 5, 5, "GRANDSTAND", 2 ), -- spens
--
--(4, "400", "200", "350", "50", 5, 5, 1, "GRANDSTAND", 2 ), -- spens
--(5, "330", "180", "170", "150", 5, 0, 0, "PARTER", 2 ), -- spens
--
----janko :
--(6, "330", "180", "170", "150", 7, 0, 0, "PARTER", 1 ); -- test PARTER in arena
--
--insert into location(id, scheme_id) values
--(1, 1), -- arena
--(2, 2), -- spens eventDay 1
--(3, 2); -- spens eventDay 2
--
--insert into location_sector (id, capacity, price, vip, location_id, sector_id) values
--(1, 5, 3, 0, 1, 1), -- arena Grandstand
--(2, 2, 6, 0, 2, 3), -- spens Grandstand eventDay 1
--(3, 3, 5, 0, 2, 4), -- spens Grandstand eventDay 1
--(4, 3, 4, 0, 2, 5), -- spens Parter eventDay 1
--(5, 25, 6, 0, 3, 3), -- spens Grandstand eventDay 2
--(6, 3, 5, 0, 3, 4), -- spens Grandstand eventDay 2
--(7, 3, 3, 0, 3, 5), -- spens Parter eventDay 2
--
--(8, 7, 5, 1, 1, 6); -- arena PARTER test Janko
--
--insert into event (id, category, description, name) values
--(1, "ENTERTAINMENT", "Opis", "Film 1"),
--(2, "ENTERTAINMENT", "Opis", "Koncert Zdravka Colica");
--
--insert into event_day(id, date, reservation_expiration_date, state, event_id, location_id) values
--(1, "2020-02-15", "2020-02-12", "RESERVABLE_AND_BUYABLE", 1, 1), -- arena, Film 1
--(2, "2020-02-18", "2020-02-16", "RESERVABLE_AND_BUYABLE", 2, 2), -- spens, Colic day 1
--(3, "2020-02-19", "2020-02-17", "RESERVABLE_AND_BUYABLE", 2, 3); -- spens, Colic day 2
--
--insert into ticket(id, price, seat_col, seat_row, sector_id, sold, vip, event_day_id, sector_type) values
--(1, 3, 1, 1, 1, 0, 0, 1, "GRANDSTAND"), -- arena grandstand id 1
--(2, 3, 2, 1, 1, 0, 0, 1, "GRANDSTAND"), --
--(3, 3, 3, 1, 1, 0, 0, 1, "GRANDSTAND"), --
--(4, 3, 4, 1, 1, 0, 0, 1, "GRANDSTAND"), --
--(5, 3, 5, 1, 1, 0, 0, 1, "GRANDSTAND"), --
--(6, 6, 1, 1, 3, 0, 0, 2, "GRANDSTAND"), -- spens grandstand id 2  day 1
--(7, 6, 2, 1, 3, 0, 0, 2, "GRANDSTAND"), --
--(8, 5, 1, 1, 4, 0, 0, 2, "GRANDSTAND"), -- spens grandstand id 3
--(9, 5, 2, 1, 4, 0, 0, 2, "GRANDSTAND"), --
--(10, 5, 3, 1, 4, 0, 0, 2, "GRANDSTAND"), --
--(11, 3, 0, 0, 5, 0, 0, 2, "PARTER"), -- spens parter id 5
--(12, 3, 0, 0, 5, 0, 0, 2, "PARTER"), --
--(13, 3, 0, 0, 5, 0, 0, 2, "PARTER"), --
--(14, 6, 1, 1, 3, 0, 0, 3, "GRANDSTAND"), -- spens grandstand id 2 day 2
--(15, 6, 2, 1, 3, 0, 0, 3, "GRANDSTAND"), --
--(16, 5, 1, 1, 4, 0, 0, 3, "GRANDSTAND"), -- spens grandstand id 3
--(17, 5, 2, 1, 4, 0, 0, 3, "GRANDSTAND"), --
--(18, 5, 3, 1, 4, 0, 0, 3, "GRANDSTAND"), --
--(19, 3, 1, 1, 5, 0, 0, 3, "PARTER"), -- spens parter id 5
--(20, 3, 2, 1, 5, 0, 0, 3, "PARTER"), --
--(21, 3, 3, 1, 5, 0, 0, 3, "PARTER"), --
--
---- janko:
----(id, price, seat_col, seat_row, sector_id, sold, vip, event_day_id, sector_type)
--(22, 2, 5, 4, 3, 0, 0, 3, "GRANDSTAND"), --
--(23, 2, 1, 2, 3, 0, 0, 3, "GRANDSTAND"), --
--(24, 2, 1, 3, 3, 0, 0, 3, "GRANDSTAND"), --
--(25, 2, 1, 4, 3, 0, 0, 3, "GRANDSTAND"), --
--(26, 2, 1, 5, 3, 0, 0, 3, "GRANDSTAND"), --
--(27, 2, 5, 5, 3, 0, 0, 3, "GRANDSTAND"), --
--(28, 2, 2, 2, 3, 0, 0, 3, "GRANDSTAND"), --
--(29, 2, 2, 3, 3, 0, 0, 3, "GRANDSTAND"), --
--(30, 2, 2, 4, 3, 0, 0, 3, "GRANDSTAND"), --
--(31, 2, 2, 5, 3, 0, 0, 3, "GRANDSTAND"), --
--(32, 2, 3, 1, 3, 0, 0, 3, "GRANDSTAND"), --
--(33, 2, 3, 2, 3, 0, 0, 3, "GRANDSTAND"), --
--(34, 2, 3, 3, 3, 0, 0, 3, "GRANDSTAND"), --
--(35, 2, 3, 4, 3, 0, 0, 3, "GRANDSTAND"), --
--(36, 2, 3, 5, 3, 0, 0, 3, "GRANDSTAND"), --
--(37, 2, 4, 1, 3, 0, 0, 3, "GRANDSTAND"), --
--(38, 2, 4, 2, 3, 0, 0, 3, "GRANDSTAND"), --
--(39, 2, 4, 3, 3, 0, 0, 3, "GRANDSTAND"), --
--(40, 2, 4, 4, 3, 0, 0, 3, "GRANDSTAND"), --
--(41, 2, 4, 5, 3, 0, 0, 3, "GRANDSTAND"), --
--(42, 2, 5, 1, 3, 0, 0, 3, "GRANDSTAND"), --
--(43, 2, 5, 2, 3, 0, 0, 3, "GRANDSTAND"), --
--(44, 2, 5, 3, 3, 0, 0, 3, "GRANDSTAND"), --
--
----(id, price, seat_col, seat_row, sector_id, sold, vip, event_day_id, sector_type)
--
--(45, 5, 0, 0, 6, 0, 0, 1, "PARTER"), -- arena PARTER
--(46, 5, 0, 0, 6, 0, 0, 1, "PARTER"),
--(47, 5, 0, 0, 6, 0, 0, 1, "PARTER"),
--(48, 5, 0, 0, 6, 0, 0, 1, "PARTER"),
--(49, 5, 0, 0, 6, 0, 0, 1, "PARTER"),
--(50, 5, 0, 0, 6, 0, 0, 1, "PARTER"),
--(51, 5, 0, 0, 6, 0, 0, 1, "PARTER"); --
--
