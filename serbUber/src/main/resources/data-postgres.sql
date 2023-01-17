INSERT INTO ROLE (name) VALUES ('ROLE_ADMIN');
INSERT INTO ROLE (name) VALUES ('ROLE_REGULAR_USER');
INSERT INTO ROLE (name) VALUES ('ROLE_DRIVER');
--sifra123@
insert into admins (id, email, password, name, surname, phone_number, city, profile_picture, role_id, verified, online) values
    (nextval('users_id_gen'), 'pera@gmail.com', '$2a$10$8TWonhaYGbjZ1C69pQwB0uWBOANl1FCwz0wxH9z2LsKXIhTM1hUay', 'Pera', 'Peric', '012345678', 'Novi Sad', 'default-user.png', 1, true, false);

insert into regular_users (id, email, password, name, surname, phone_number, city, profile_picture, role_id, blocked, verified, online) values
    (nextval('users_id_gen'), 'ana@gmail.com', '$2a$10$8TWonhaYGbjZ1C69pQwB0uWBOANl1FCwz0wxH9z2LsKXIhTM1hUay', 'Ana', 'Ancic', '012345678', 'Novi Sad', 'default-user.png', 2, false, true, false);

insert into regular_users (id, email, password, name, surname, phone_number, city, profile_picture, role_id, blocked, verified, online) values
    (nextval('users_id_gen'), 'miki@gmail.com', '$2a$10$8TWonhaYGbjZ1C69pQwB0uWBOANl1FCwz0wxH9z2LsKXIhTM1hUay', 'Miki', 'Mikic', '0651234567', 'Novi Sad', 'default-user.png',2, false, true, false);

insert into regular_users (id, email, password, name, surname, phone_number, city, profile_picture, role_id, blocked, verified, online) values
    (nextval('users_id_gen'), 'zoka200015@gmail.com', '$2a$10$8TWonhaYGbjZ1C69pQwB0uWBOANl1FCwz0wxH9z2LsKXIhTM1hUay', 'Zorica', 'Vukovic', '0651234567', 'Novi Sad', 'default-user.png',2, false, true, false);

insert into vehicle_type_infos (num_of_seats, start_price, vehicle_type) values
    (9, 3, 0);
insert into vehicle_type_infos (num_of_seats, start_price, vehicle_type) values
    (5, 2, 1);
insert into vehicle_type_infos (num_of_seats, start_price, vehicle_type) values
    (7, 1, 2);

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
    ('Novi Sad', 45.247785,19.836022, 'Brace Ribnikara', '25a', '21000'),
    ('Novi Sad', 45.242610,19.789230, 'Futoski put', '103', '21000');


insert into routes (distance, time) values
                     (3, 5),
                     (5, 2),
                     (6, 6),
                     (3200, 3);

insert into vehicles (baby_seat, pet_friendly, rate, vehicle_type_id, active_route_id, current_location_index, current_stop_location_id) values
    (true, false, 2.9, 1, 2, 0, 5),
    (false, true, 5, 3, 4, 0, 5),
    (false, true, 0, 3, 1, 0, 2);

insert into driving_locations(location_id, index, route_id, route_index) values
      (5,1,1, 0),
      (6,2,1, 0),
      (7,3,1, 0),
      (14,10,1, -1),
      (2,1,2, 0),
      (1,2,2, -1),
      (4,1,3, 0),
      (1,2,3, -1),
      (5, 1,4, 0),
      (15, 2, 4, 0),
      (3, 3, 4, -1);


insert into drivers (id, email, password, name, surname, phone_number, city, profile_picture, role_id, active, blocked, start_shift, end_shift, rate, working_minutes, verified, online, drive, vehicle_id) values
    (nextval('users_id_gen'), 'mile@gmail.com', '$2a$10$8TWonhaYGbjZ1C69pQwB0uWBOANl1FCwz0wxH9z2LsKXIhTM1hUay', 'Mile', 'Milic', '012345678', 'Novi Sad', 'default-user.png', 3, true, false, null, null, 4.1, 0, true, false, false, 3);

insert into drivers (id, email, password, name, surname, phone_number, city, profile_picture, role_id, active, blocked, start_shift, end_shift, rate, working_minutes, verified, online, drive, vehicle_id) values
    (nextval('users_id_gen'), 'milan@gmail.com', '$2a$10$8TWonhaYGbjZ1C69pQwB0uWBOANl1FCwz0wxH9z2LsKXIhTM1hUay', 'Milan', 'Milanovic', '012345678', 'Novi Sad', 'default-user.png', 3, true, false, null, null, 4, 1, true, false,false, 2);

insert into drivers (id, email, password, name, surname, phone_number, city, profile_picture, role_id, active, blocked, start_shift, end_shift, rate, working_minutes, verified, online, drive, vehicle_id) values
    (nextval('users_id_gen'), 'eki@gmail.com', '$2a$10$8TWonhaYGbjZ1C69pQwB0uWBOANl1FCwz0wxH9z2LsKXIhTM1hUay', 'Esad', 'Esadic', '012345678', 'Novi Sad', 'default-user.png', 3, true, false, null, null, 0, 0, true, false, false, 1);

insert into drivings (active, driver_id, driving_status, duration, paying_limit, price, started, end_date, route_id) values
   (true, 5, 2, 2, null, 3, to_timestamp('17.01.2023. 14:20', 'DD.MM.YYYY HH24:MI'), null, 1),
   (false, 5, 2, 2, null, 3, to_timestamp('17.01.2023. 23:20', 'DD.MM.YYYY HH24:MI'), null, 2),
   (false, 6, 3, 10, null, 5,to_timestamp('24.10.2022 14:00', 'DD.MM.YYYY HH24:MI'), to_timestamp('24.10.2022. 14:10', 'DD.MM.YYYY HH24:MI'),3),
   (false, 6, 3, 10, null, 5,to_timestamp('24.11.2022 14:00', 'DD.MM.YYYY HH24:MI'), to_timestamp('24.11.2022. 14:10', 'DD.MM.YYYY HH24:MI'),4);


insert into admins (id, email, password, name, surname, phone_number, city, profile_picture, role_id, verified, online) values
    (nextval('users_id_gen'), 'admin@gmail.com', '$2a$10$8TWonhaYGbjZ1C69pQwB0uWBOANl1FCwz0wxH9z2LsKXIhTM1hUay', 'Admin', 'Admin', '012345578', 'Novi Sad', 'default-user.png', 1, true, false);

insert into regular_users (id, email, password, name, surname, phone_number, city, profile_picture, role_id, blocked, verified, online) values
    (nextval('users_id_gen'), 'anastasija@gmail.com', '$2a$10$8TWonhaYGbjZ1C69pQwB0uWBOANl1FCwz0wxH9z2LsKXIhTM1hUay', 'Anastasija', 'Sam', '012345678', 'Novi Sad', 'default-user.png', 2, false, true, false);

insert into regular_users (id, email, password, name, surname, phone_number, city, profile_picture, role_id, blocked, verified, online) values
    (nextval('users_id_gen'), 'srki@gmail.com', '$2a$10$8TWonhaYGbjZ1C69pQwB0uWBOANl1FCwz0wxH9z2LsKXIhTM1hUay', 'Srdjan', 'Djuric', '012345678', 'Novi Sad', 'default-user.png', 2, false, true, false);

insert into regular_users (id, email, password, name, surname, phone_number, city, profile_picture, role_id, blocked, verified, online) values
    (nextval('users_id_gen'), 'jovan@gmail.com', '$2a$10$8TWonhaYGbjZ1C69pQwB0uWBOANl1FCwz0wxH9z2LsKXIhTM1hUay', 'Jovan', 'Jovic', '012345678', 'Novi Sad', 'default-user.png', 2, false, true, false);

insert into regular_users (id, email, password, name, surname, phone_number, city, profile_picture, role_id, blocked, verified, online) values
    (nextval('users_id_gen'), 'milos@gmail.com', '$2a$10$8TWonhaYGbjZ1C69pQwB0uWBOANl1FCwz0wxH9z2LsKXIhTM1hUay', 'Milos', 'Milosevic', '012345678', 'Novi Sad', 'default-user.png', 2, false, true, false);

insert into regular_users (id, email, password, name, surname, phone_number, city, profile_picture, role_id, blocked, verified, online) values
    (nextval('users_id_gen'), 'zokili@gmail.com', '$2a$10$8TWonhaYGbjZ1C69pQwB0uWBOANl1FCwz0wxH9z2LsKXIhTM1hUay', 'Zorica', 'Zoric', '012345678', 'Novi Sad', 'default-user.png', 2, false, true, false);

insert into regular_users (id, email, password, name, surname, phone_number, city, profile_picture, role_id, blocked, verified, online) values
    (nextval('users_id_gen'), 'jana@gmail.com', '$2a$10$8TWonhaYGbjZ1C69pQwB0uWBOANl1FCwz0wxH9z2LsKXIhTM1hUay', 'Jana', 'Janic', '012345678', 'Novi Sad', 'default-user.png', 2, false, true, false);


insert into drivings_users(driving_id, user_id) values
    (1, 2),
    (1, 3),
    (1, 4),
    (2, 9),
    (3, 2),
    (4, 4),
    (4, 2);
--     (5, 2),
--     (5, 4),
--     (5, 3);


insert into chat_rooms(client_id, admin_id, resolved) values
    (2,1, true);
insert into chat_rooms(client_id, admin_id, resolved) values
    (2,1, false);
insert into chat_rooms(client_id, admin_id, resolved) values
    (3,1, false);
insert into chat_rooms(client_id, admin_id, resolved) values
    (6,8, false);


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

insert into reviews(vehicle_rate, driver_rate, message, sender_id, driving_id) values
    (4, 4.3, 'Cisto vozilo, prijatna voznja..', 2, 1),
    (2.1, 4, 'Klima nije radila...', 2, 2),
    (2.6, 4, 'Klima ne radi, vozilo prljavo.', 3, 2),
    (5, 4, 'Dobar utisak..', 3, 4);

insert into reports(id, admin_email, answered, sender_id, receiver_id, message, time_stamp) values
    (nextval('notifications_id_gen'), null, false, 2, 5, 'Vozac je bezobrazan!','2022-11-20 14:00'),
    (nextval('notifications_id_gen'), null, false, 3, 5, 'Vozac je skrenuo sa zadate putanje!', '2022-12-01 13:00'),
    (nextval('notifications_id_gen'), null, false, 5, 2,'Ana je prosula sok po sedistu!', '2022-12-01 13:25');

-- insert into drivers (id, email, password, name, surname, phone_number, city, profile_picture, role_id, active, blocked, start_shift, end_shift, rate, working_minutes, verified, online, drive, vehicle_id) values
--     (nextval('users_id_gen'), 'bole@gmail.com', '$2a$10$8TWonhaYGbjZ1C69pQwB0uWBOANl1FCwz0wxH9z2LsKXIhTM1hUay', 'Bole', 'Sam', '012345678', 'Novi Sad', 'default-user.png', 3, true, false, null, null, 0, 0, true, false, false, 1);
--
-- insert into drivers (id, email, password, name, surname, phone_number, city, profile_picture, role_id, active, blocked, start_shift, end_shift, rate, working_minutes, verified, online, drive, vehicle_id) values
--     (nextval('users_id_gen'), 'uros@gmail.com', '$2a$10$8TWonhaYGbjZ1C69pQwB0uWBOANl1FCwz0wxH9z2LsKXIhTM1hUay', 'Uros', 'Pejicc', '012345678', 'Novi Sad', 'default-user.png', 3, true, false, null, null, 0, 0, true, false, false, 1);
--     (nextval('users_id_gen'), 'anastasija@gmail.com', '$2a$10$8TWonhaYGbjZ1C69pQwB0uWBOANl1FCwz0wxH9z2LsKXIhTM1hUay', 'Anastasija', 'Sam', '0651234567', 'Novi Sad', 'default-user.png',2, false, true, false);

insert into favourite_routes (user_id, route_id) values
    (2,1);

insert into paying_info(currency, token_price, max_num_of_tokens_per_transaction) values
    ('EUR', 1, 20);

insert into token_banks(user_id, num_of_tokens, total_token_amount_spent, total_money_amount_spent, paying_info_id) values
                                                                                                                        (2, 9, 2, 11, 1),
                                                                                                                        (3, 8, 0, 8, 1),
                                                                                                                        (4, 0, 0, 0, 1),
                                                                                                                        (9, 3, 0, 0, 1),
                                                                                                                        (10, 1, 0, 0, 1),
                                                                                                                        (11, 0, 0, 0, 1),
                                                                                                                        (12, 0, 0, 0, 1),
                                                                                                                        (13, 0, 0, 0, 1),
                                                                                                                        (14, 0, 0, 0, 1);

insert into token_transactions(time_stamp, num_of_bought_tokens, total_price, token_bank_id) values
                                                                                                 ('2022-12-01 14:03', 4, 4, 1),
                                                                                                 ('2022-12-03 14:00', 7, 7, 1),
                                                                                                 ('2022-12-03 15:00', 8, 8, 2);

