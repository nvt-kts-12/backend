insert into authority (id, deleted, name) values (1, false, "ROLE_REGISTERED");
insert into authority (id, deleted, name) values (2, false, "ROLE_ADMIN");

insert into location_scheme (id, address, name, deleted) values (1, "Bul. Mihajla Pupina 3 Novi Sad", "Arena cineplex", 0 );

insert into sector (id,deleted, bottom_rightx, bottom_righty, top_leftx, top_lefty, capacity, col_num, row_num, type, location_scheme_id)
values (2, 0, "50", "50", "0", "0", 50, 5, 10, 1, 1 ),
(3, 0, "50", "50", "0", "0", 100, 0, 0, 0, 1 ),
(4, 0, "50", "50", "0", "0", 20, 2, 10, 1, 1 );

