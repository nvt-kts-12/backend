insert into authority (id, deleted, name) values (1, false, "ROLE_REGISTERED");
insert into authority (id, deleted, name) values (2, false, "ROLE_ADMIN");

insert into location_scheme (id, address, name, deleted) values (1, "Bul. Mihajla Pupina 3 Novi Sad", "Arena cineplex", 0 );

insert into sector (id,deleted, bottom_rightx, bottom_righty, top_leftx, top_lefty, capacity, col_num, row_num, type, location_scheme_id)
values (2, 0, "50", "50", "0", "0", 5, 5, 1, 1, 1 ),
(3, 0, "50", "50", "0", "0", 4, 0, 0, 0, 1 ),
(4, 0, "50", "50", "0", "0", 6, 2, 3, 1, 1 );

-- Janko, testing the report system. Feel free to comment out
insert into users (id, first_name, last_name, password, user_role, username, email, last_password_reset_date) values
            (1, "Janko", "Ljubic", "sifra1234Abc", 0, "janko_lj", "", "2019-01-01"),
            (2, "Marko", "Ljubic", "sifra1234Abc", 0, "marko_lj", "", "2019-01-01"),
            (3, "Petar", "Ljubic", "sifra1234Abc", 0, "petar_lj", "", "2019-01-01");
insert into location (id, deleted, scheme_id) values
            (1, 0, 1);
insert into location_sector (id, deleted, capacity, price, vip, location_id, sector_id) values
            (1, 0, 100, 1421, 0, 1, 3),
            (2, 0, 0, 50, 0, 1, 3);
insert into event (id, deleted, category, description, name) values
            (1, 0, 0, "opis dogadjaja", "naziv dogadjaja");
insert into event_day(id, deleted, date, reservation_expiration_date, state, event_id, location_id) values
            (1, 0, "2019-02-02", "2019-01-30", 0, 1, 1),
            (2, 0, "2019-02-04", "2019-01-30", 0, 1, 1);
insert into ticket(id, deleted, price, seat_col, seat_row, sector_id, sold, vip, event_day_id, user_id) values
            (1, 0, 1421, 0, 0, 1, 1, 0, 1, 1),
            (2, 0, 1421, 0, 0, 1, 1, 0, 1, 2),
            (3, 0, 50, 1, 1, 2, 1, 0, 1, 1),
            (4, 0, 50, 1, 1, 2, 1, 0, 2, 1),
            (5, 0, 50, 1, 1, 2, 0, 0, 2, 3);

-- simanic

