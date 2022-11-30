INSERT INTO ROLE (name) VALUES ('ROLE_ADMIN');
INSERT INTO ROLE (name) VALUES ('ROLE_REGULAR_USER');
INSERT INTO ROLE (name) VALUES ('ROLE_DRIVER');
--sifra123@
insert into admins (id, email, password, name, surname, phone_number, city, profile_picture, role_id, verified, online) values
    (nextval('users_id_gen'), 'pera@gmail.com', '$2a$10$8TWonhaYGbjZ1C69pQwB0uWBOANl1FCwz0wxH9z2LsKXIhTM1hUay', 'Pera', 'Peric', '012345678', 'Novi Sad', 'default-user.png', 1, true, false);

insert into regular_users (id, email, password, name, surname, phone_number, city, profile_picture, role_id, blocked, verified, online) values
    (nextval('users_id_gen'), 'ana@gmail.com', '$2a$10$8TWonhaYGbjZ1C69pQwB0uWBOANl1FCwz0wxH9z2LsKXIhTM1hUay', 'Ana', 'Ancic', '012345678', 'Novi Sad', 'default-user.png',2, false, true, false);

insert into regular_users (id, email, password, name, surname, phone_number, city, profile_picture, role_id, blocked, verified, online) values
    (nextval('users_id_gen'), 'miki@gmail.com', '$2a$10$8TWonhaYGbjZ1C69pQwB0uWBOANl1FCwz0wxH9z2LsKXIhTM1hUay', 'Miki', 'Mikic', '0651234567', 'Novi Sad', 'default-user.png',2, false, true, false);

insert into regular_users (id, email, password, name, surname, phone_number, city, profile_picture, role_id, blocked, verified, online) values
    (nextval('users_id_gen'), 'zoka200015@gmail.com', '$2a$10$8TWonhaYGbjZ1C69pQwB0uWBOANl1FCwz0wxH9z2LsKXIhTM1hUay', 'Zorica', 'Vukovic', '0651234567', 'Novi Sad', 'default-user.png',2, false, true, false);

insert into vehicle_type_infos (num_of_seats, start_price, vehicle_type) values
    (9, 200, 0);
insert into vehicle_type_infos (num_of_seats, start_price, vehicle_type) values
    (5, 100, 1);
insert into vehicle_type_infos (num_of_seats, start_price, vehicle_type) values
    (7, 150, 2);

-- insert into routes (kilometres, location_id, )
insert into locations (city, lat, lon, street, number, zip_code) values
    ('Novi Sad', 45.258300,19.833950, 'Bulevar Oslobodjenja', '55', '21000'),
    ('Novi Sad', 45.246710, 19.848760, 'Bulevar Cara Lazara', '5', '21000'),
    ('Novi Sad', 45.256420, 19.811140, 'Bulevar Evrope', '10', '21000'),
    ('Novi Sad', 45.247460, 19.839030, 'Lasla Gala', '2', '21000'),
    ('Novi Sad', 45.245720,19.837379, 'Lasla Gala', '21', '21000'),
    ('Novi Sad', 45.245540,19.836470, 'Gogoljeva', '2', '21000'),
    ('Novi Sad', 45.245736,19.835760, 'Gogoljeva', '10', '21000'),
    ('Novi Sad', 45.246382,19.834196, 'Gogoljeva', '22', '21000'),
    ('Novi Sad', 45.246611,19.833666, 'Gogoljeva', '28', '21000'),
    ('Novi Sad', 45.246956,19.833697, 'Mise Dimitrijevica', '3c', '21000'),
    ('Novi Sad', 45.247748,19.834440, 'Mise Dimitrijevica', '1a', '21000'),
    ('Novi Sad', 45.248048,19.834964, 'Brace Ribnikara', '29', '21000'),
    ('Novi Sad', 45.247868,19.835635, 'Brace Ribnikara', '25b', '21000'),
    ('Novi Sad', 45.247785,19.836022, 'Brace Ribnikara', '25a', '21000');

insert into routes (distance, time) values
                             (3, 5),
                             (5, 2),
                             (6, 6);

insert into vehicles (baby_seat, pet_friendly, rate, vehicle_type_id, location_index, active_route_id, in_drive) values
    (true, false, 0, 1, 0, 1, true),
    (true, true, 0, 2, 0, 2, true),
    (false, true, 0, 3, 1, 3, false);

insert into driving_locations(location_id, index, route_id) values
      (5,1,1),
      (6,2,1),
      (7,3,1),
      (8,4,1),
      (9,5,1),
      (10,6,1),
      (11,7,1),
      (12,8,1),
      (13,9,1),
      (14,10,1),
      (2,1,2),
      (1,2,2),
      (4,1,3),
      (1,2,3);


insert into drivers (id, email, password, name, surname, phone_number, city, profile_picture, role_id, active, blocked, start_shift, end_shift, last_active, rate, working_minutes, location_id, vehicle_id, verified, online, drive) values
    (nextval('users_id_gen'), 'mile@gmail.com', '$2a$10$8TWonhaYGbjZ1C69pQwB0uWBOANl1FCwz0wxH9z2LsKXIhTM1hUay', 'Mile', 'Milic', '012345678', 'Novi Sad', 'default-user.png', 3, true, false, null, null, null, 1.3, 0, 1, 1, true, true, false);

insert into drivers (id, email, password, name, surname, phone_number, city, profile_picture, role_id, active, blocked, start_shift, end_shift, last_active, rate, working_minutes, location_id, vehicle_id, verified, online, drive) values
    (nextval('users_id_gen'), 'milan@gmail.com', '$2a$10$8TWonhaYGbjZ1C69pQwB0uWBOANl1FCwz0wxH9z2LsKXIhTM1hUay', 'Milan', 'Milanovic', '012345678', 'Novi Sad', 'default-user.png', 3, true, false, null, null, null, 0, 0, 1, 2, true, true, true);

insert into drivers (id, email, password, name, surname, phone_number, city, profile_picture, role_id, active, blocked, start_shift, end_shift, last_active, rate, working_minutes, location_id, vehicle_id, verified, online, drive) values
    (nextval('users_id_gen'), 'eki@gmail.com', '$2a$10$8TWonhaYGbjZ1C69pQwB0uWBOANl1FCwz0wxH9z2LsKXIhTM1hUay', 'Esad', 'Esadic', '012345678', 'Novi Sad', 'default-user.png', 3, true, false, null, null, null, 0, 0, 1, 3, true, true, false);



insert into drivings (active, driver_id, driving_status, duration, paying_limit, price, started, end_date, route_id, driving_id) values
                                                                       (false, 5, 1, 3, null, 400, to_timestamp('06.11.2022. 14:00', 'DD.MM.YYYY HH24:MI'), to_timestamp('06.11.2022. 14:03', 'DD.MM.YYYY HH24:MI'), 1, 5),
                                                                       (false, 5, 1, 2, null, 300, to_timestamp('16.11.2022. 14:00', 'DD.MM.YYYY HH24:MI'), to_timestamp('06.11.2022. 14:02', 'DD.MM.YYYY HH24:MI'),2, 5),
                                                                       (false, 5, 1, 5, null, 600, to_timestamp('16.11.2022. 18:00', 'DD.MM.YYYY HH24:MI'), to_timestamp('06.11.2022. 14:05', 'DD.MM.YYYY HH24:MI'),1, 5),
                                                                       (false, 5, 0, 4, null, 445, to_timestamp('14.12.2021. 14:00', 'DD.MM.YYYY HH24:MI'), to_timestamp('06.11.2022. 14:04', 'DD.MM.YYYY HH24:MI'),2, 5),
                                                                       (false, 5, 1, 4, null, 445, to_timestamp('16.11.2022. 14:00', 'DD.MM.YYYY HH24:MI'), to_timestamp('06.11.2022. 14:04', 'DD.MM.YYYY HH24:MI'),2, 5),
                                                                       (false, 5, 2, 4, null, 445, to_timestamp('16.11.2022. 18:00', 'DD.MM.YYYY HH24:MI'), to_timestamp('06.11.2022. 14:04', 'DD.MM.YYYY HH24:MI'),2, 5),
                                                                       (false, 6, 1, 10, null, 560,to_timestamp('24.08.2022 14:00', 'DD.MM.YYYY HH24:MI'), to_timestamp('06.11.2022. 14:10', 'DD.MM.YYYY HH24:MI'),3, 6),
                                                                       (false, 6, 1, 10, null, 500,to_timestamp('24.10.2022 14:00', 'DD.MM.YYYY HH24:MI'), to_timestamp('06.11.2022. 14:10', 'DD.MM.YYYY HH24:MI'),3, 6);

insert into admins (id, email, password, name, surname, phone_number, city, profile_picture, role_id, verified, online) values
    (nextval('users_id_gen'), 'admin@gmail.com', '$2a$10$8TWonhaYGbjZ1C69pQwB0uWBOANl1FCwz0wxH9z2LsKXIhTM1hUay', 'Admin', 'Admin', '012345578', 'Novi Sad', 'default-user.png', 1, true, false);


insert into drivings_users(driving_id, user_id) values
    (1, 2),
    (2, 2),
    (3, 2),
    (4, 4),
    (4, 2),
    (5, 2),
    (5, 4),
    (5, 3);


insert into chat_rooms(client_id, admin_id, resolved) values
    (2,1, true);
insert into chat_rooms(client_id, admin_id, resolved) values
    (2,1, false);
insert into chat_rooms(client_id, admin_id, resolved) values
    (3,1, false);
insert into chat_rooms(client_id, admin_id, resolved) values
    (6,1, false);


insert into messages(message, time_stamp, admin_response, chat_room_id, seen) values
    ('Heej, imam problem', '11.11.2022. 11:00', false, 2, true);
insert into messages(message, time_stamp, admin_response, chat_room_id, seen) values
    ('Sta te muci?', '11.11.2022. 11:20', true, 2, false);
insert into messages(message, time_stamp, admin_response, chat_room_id, seen) values
    ('Kako da se logout..', '11.11.2022. 11:29', false, 1, true);
insert into messages(message, time_stamp, admin_response, chat_room_id, seen) values
    ('Kliknes na ikonicu i logout..', '11.11.2022. 11:45', true, 1, true);
insert into messages(message, time_stamp, admin_response, chat_room_id, seen) values
    ('Pomoc na putu..', '11.11.2022. 15:00', false, 3, false);
insert into messages(message, time_stamp, admin_response, chat_room_id, seen) values
    ('Tu si?', '11.11.2022. 16:00', false, 3, false);
insert into messages(message, time_stamp, admin_response, chat_room_id, seen) values
    ('Cao, potrebna mi je pomoc..', '11.11.2022. 15:00', false, 4, false);



