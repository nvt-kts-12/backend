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

insert into location_scheme (id, address, name) values
(1, "Bul. Mihajla Pupina 3, Novi Sad", "Arena cineplex"),
(2, "Sutjeska 2, Novi Sad", "Spens");

insert into sector (id, bottom_rightx, bottom_righty, top_leftx, top_lefty, capacity, col_num, row_num, type, location_scheme_id) values
(1, "0", "0", "0", "0", 5, 5, 1, "GRANDSTAND", 1 ), -- arena
(2, "0", "0", "0", "0", 6, 2, 3, "GRANDSTAND", 1 ), -- arena
(3, "65", "-100", "25", "10", 5, 5, 1, "GRANDSTAND", 2 ), -- spens
(4, "275", "-100", "235", "10", 5, 5, 1, "GRANDSTAND", 2 ), -- spens
(5, "215", "-20", "85", "10", 5, 0, 0, "PARTER", 2 ); -- spens

insert into location(id, scheme_id) values
(1, 1), -- arena
(2, 2), -- spens eventDay 1
(3, 2); -- spens eventDay 2

insert into location_sector (id, capacity, price, vip, location_id, sector_id) values
(1, 5, 300, 0, 1, 1), -- arena Grandstane
(2, 2, 600, 0, 2, 3), -- spens Grandstand eventDay 1
(3, 3, 500, 0, 2, 4), -- spens Grandstand eventDay 1
(4, 3, 300, 0, 2, 5), -- spens Parter eventDay 1
(5, 2, 600, 0, 3, 3), -- spens Grandstand eventDay 2
(6, 3, 500, 0, 3, 4), -- spens Grandstand eventDay 2
(7, 3, 300, 0, 3, 5); -- spens Parter eventDay 2

insert into event (id, category, description, name) values
(1, "ENTERTAINMENT", "Opis", "Film 1"),
(2, "ENTERTAINMENT", "Opis", "Koncert Zdravka Colica");

insert into event_day(id, date, reservation_expiration_date, state, event_id, location_id) values
(1, "2020-02-15", "2020-02-12", "RESERVABLE_AND_BUYABLE", 1, 1), -- arena, Film 1
(2, "2020-02-18", "2020-02-16", "RESERVABLE_AND_BUYABLE", 2, 2), -- spens, Colic day 1
(3, "2020-02-19", "2020-02-17", "RESERVABLE_AND_BUYABLE", 2, 3); -- spens, Colic day 2

insert into ticket(id, price, seat_col, seat_row, sector_id, sold, vip, event_day_id) values
(1, 300, 1, 1, 1, 0, 0, 1), -- arena grandstand id 1
(2, 300, 2, 1, 1, 0, 0, 1), --
(3, 300, 3, 1, 1, 0, 0, 1), --
(4, 300, 4, 1, 1, 0, 0, 1), --
(5, 300, 5, 1, 1, 0, 0, 1), --
(6, 600, 1, 1, 3, 0, 0, 2), -- spens grandstand id 2  day 1
(7, 600, 2, 1, 3, 0, 0, 2), --
(8, 500, 1, 1, 4, 0, 0, 2), -- spens grandstand id 3
(9, 500, 2, 1, 4, 0, 0, 2), --
(10, 500, 3, 1, 4, 0, 0, 2), --
(11, 300, 0, 0, 5, 0, 0, 2), -- spens parter id 5
(12, 300, 0, 0, 5, 0, 0, 2), --
(13, 300, 0, 0, 5, 0, 0, 2), --
(14, 600, 1, 1, 3, 0, 0, 3), -- spens grandstand id 2 day 2
(15, 600, 2, 1, 3, 0, 0, 3), --
(16, 500, 1, 1, 4, 0, 0, 3), -- spens grandstand id 3
(17, 500, 2, 1, 4, 0, 0, 3), --
(18, 500, 3, 1, 4, 0, 0, 3), --
(19, 300, 1, 1, 5, 1, 0, 3), -- spens parter id 5
(20, 300, 2, 1, 5, 0, 0, 3), --
(21, 300, 3, 1, 5, 0, 0, 3); --