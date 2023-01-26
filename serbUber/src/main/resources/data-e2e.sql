INSERT INTO ROLE (name) VALUES ('ROLE_ADMIN');
INSERT INTO ROLE (name) VALUES ('ROLE_REGULAR_USER');
INSERT INTO ROLE (name) VALUES ('ROLE_DRIVER');

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

insert into vehicle_type_infos (num_of_seats, start_price, vehicle_type) values
    (9, 3, 0);
insert into vehicle_type_infos (num_of_seats, start_price, vehicle_type) values
    (5, 2, 1);
insert into vehicle_type_infos (num_of_seats, start_price, vehicle_type) values
    (7, 1, 2);

insert into routes (distance, time) values
                                        (3, 5),
                                        (5, 2),
                                        (6, 6),
                                        (3200, 3);

insert into vehicles (baby_seat, pet_friendly, rate, vehicle_type_id, active_route_id, current_location_index, current_stop_location_id, crossed_waypoints) values
                                                                                                                                                                (true, false, 0, 1, 2, 0, 2, 0),
                                                                                                                                                                (false, true, 0, 1, null, 0, 12, 0),
                                                                                                                                                                (false, true, 0, 2, null, 0, 5, 0),
                                                                                                                                                                (true, true, 0, 2, null, 0, 5, 0),
                                                                                                                                                                (false, true, 0, 1, 1, 0, 5, 0),
                                                                                                                                                                (false, true, 0, 3, null, 0, 7, 0);

--sifra123@
insert into admins (id, email, password, name, surname, phone_number, city, profile_picture, role_id, verified, online) values
                                                                                                                            (nextval('users_id_gen'), 'serbubernwtkts@gmail.com', '$2a$10$8TWonhaYGbjZ1C69pQwB0uWBOANl1FCwz0wxH9z2LsKXIhTM1hUay', 'Pera', 'Peric', '012345678', 'Novi Sad', 'default-user.png', 1, true, false),
                                                                                                                            (nextval('users_id_gen'), 'admin@gmail.com', '$2a$10$8TWonhaYGbjZ1C69pQwB0uWBOANl1FCwz0wxH9z2LsKXIhTM1hUay', 'Admin', 'Admin', '012345578', 'Novi Sad', 'default-user.png', 1, true, false);

insert into regular_users (id, email, password, name, surname, phone_number, city, profile_picture, role_id, blocked, verified, online) values
                                                                                                                                            (nextval('users_id_gen'), 'ana@gmail.com', '$2a$10$8TWonhaYGbjZ1C69pQwB0uWBOANl1FCwz0wxH9z2LsKXIhTM1hUay', 'Ana', 'Ancic', '012345678', 'Novi Sad', 'default-user.png', 2, false, true, false),
                                                                                                                                            (nextval('users_id_gen'), 'miki@gmail.com', '$2a$10$8TWonhaYGbjZ1C69pQwB0uWBOANl1FCwz0wxH9z2LsKXIhTM1hUay', 'Miki', 'Mikic', '0651234567', 'Novi Sad', 'default-user.png',2, false, true, false),
                                                                                                                                            (nextval('users_id_gen'), 'zoka200015@gmail.com', '$2a$10$8TWonhaYGbjZ1C69pQwB0uWBOANl1FCwz0wxH9z2LsKXIhTM1hUay', 'Zorica', 'Vukovic', '0651234567', 'Novi Sad', 'default-user.png',2, false, true, false),
                                                                                                                                            (nextval('users_id_gen'), 'anastasija@gmail.com', '$2a$10$8TWonhaYGbjZ1C69pQwB0uWBOANl1FCwz0wxH9z2LsKXIhTM1hUay', 'Anastasija', 'Sam', '012345678', 'Novi Sad', 'default-user.png', 2, false, true, false),
                                                                                                                                            (nextval('users_id_gen'), 'srki@gmail.com', '$2a$10$8TWonhaYGbjZ1C69pQwB0uWBOANl1FCwz0wxH9z2LsKXIhTM1hUay', 'Srdjan', 'Djuric', '012345678', 'Novi Sad', 'default-user.png', 2, false, true, false),
                                                                                                                                            (nextval('users_id_gen'), 'jovan@gmail.com', '$2a$10$8TWonhaYGbjZ1C69pQwB0uWBOANl1FCwz0wxH9z2LsKXIhTM1hUay', 'Jovan', 'Jovic', '012345678', 'Novi Sad', 'default-user.png', 2, false, true, false),
                                                                                                                                            (nextval('users_id_gen'), 'milos@gmail.com', '$2a$10$8TWonhaYGbjZ1C69pQwB0uWBOANl1FCwz0wxH9z2LsKXIhTM1hUay', 'Milos', 'Milosevic', '012345678', 'Novi Sad', 'default-user.png', 2, false, true, false),
                                                                                                                                            (nextval('users_id_gen'), 'zokili@gmail.com', '$2a$10$8TWonhaYGbjZ1C69pQwB0uWBOANl1FCwz0wxH9z2LsKXIhTM1hUay', 'Zorica', 'Zoric', '012345678', 'Novi Sad', 'default-user.png', 2, false, true, false),
                                                                                                                                            (nextval('users_id_gen'), 'jana@gmail.com', '$2a$10$8TWonhaYGbjZ1C69pQwB0uWBOANl1FCwz0wxH9z2LsKXIhTM1hUay', 'Jana', 'Janic', '012345678', 'Novi Sad', 'default-user.png', 2, false, true, false),
                                                                                                                                            (nextval('users_id_gen'), 'anastasijas12@gmail.com', '$2a$10$8TWonhaYGbjZ1C69pQwB0uWBOANl1FCwz0wxH9z2LsKXIhTM1hUay', 'Ana', 'Ancic', '012345678', 'Novi Sad', 'default-user.png', 2, false, true, false),
                                                                                                                                            (nextval('users_id_gen'), 'serbuber2@gmail.com', '$2a$10$8TWonhaYGbjZ1C69pQwB0uWBOANl1FCwz0wxH9z2LsKXIhTM1hUay', 'Ana', 'Ancic', '012345678', 'Novi Sad', 'default-user.png', 2, false, true, false);

insert into drivers (id, email, password, name, surname, phone_number, city, profile_picture, role_id, active, blocked, start_shift, end_shift, rate, working_minutes, verified, online, drive, vehicle_id) values
                                                                                                                                                                                                                (nextval('users_id_gen'), 'mile@gmail.com', '$2a$10$8TWonhaYGbjZ1C69pQwB0uWBOANl1FCwz0wxH9z2LsKXIhTM1hUay', 'Mile', 'Milic', '012345678', 'Novi Sad', 'default-user.png', 3, true, false, to_timestamp('17.01.2023. 14:20', 'DD.MM.YYYY HH24:MI'), to_timestamp('18.01.2023. 14:20', 'DD.MM.YYYY HH24:MI'), 0, 20, true, true, false, 1),
                                                                                                                                                                                                                (nextval('users_id_gen'), 'milan@gmail.com', '$2a$10$8TWonhaYGbjZ1C69pQwB0uWBOANl1FCwz0wxH9z2LsKXIhTM1hUay', 'Milan', 'Milanovic', '012345678', 'Novi Sad', 'default-user.png', 3, true, false, to_timestamp('17.01.2023. 14:20', 'DD.MM.YYYY HH24:MI'), to_timestamp('18.01.2023. 14:20', 'DD.MM.YYYY HH24:MI'), 0, 1, true, true, false, 2),
                                                                                                                                                                                                                (nextval('users_id_gen'), 'eki@gmail.com', '$2a$10$8TWonhaYGbjZ1C69pQwB0uWBOANl1FCwz0wxH9z2LsKXIhTM1hUay', 'Esad', 'Esadic', '012345678', 'Novi Sad', 'default-user.png', 3, false, false, to_timestamp('17.01.2023. 14:20', 'DD.MM.YYYY HH24:MI'), to_timestamp('18.01.2023. 14:20', 'DD.MM.YYYY HH24:MI'), 0, 0, true, false, false, 3),
                                                                                                                                                                                                                (nextval('users_id_gen'), 'bole@gmail.com', '$2a$10$8TWonhaYGbjZ1C69pQwB0uWBOANl1FCwz0wxH9z2LsKXIhTM1hUay', 'Bole', 'Sam', '012345678', 'Novi Sad', 'default-user.png', 3, true, false, to_timestamp('17.01.2023. 14:20', 'DD.MM.YYYY HH24:MI'), to_timestamp('18.01.2023. 14:20', 'DD.MM.YYYY HH24:MI'), 0, 0, true, true, false, 4),
                                                                                                                                                                                                                (nextval('users_id_gen'), 'uros@gmail.com', '$2a$10$8TWonhaYGbjZ1C69pQwB0uWBOANl1FCwz0wxH9z2LsKXIhTM1hUay', 'Uros', 'Pejicc', '012345678', 'Novi Sad', 'default-user.png', 3, true, false, to_timestamp('17.01.2023. 14:20', 'DD.MM.YYYY HH24:MI'), to_timestamp('18.01.2023. 14:20', 'DD.MM.YYYY HH24:MI'), 0, 0, true, true, true, 5),
                                                                                                                                                                                                                (nextval('users_id_gen'), 'elo@gmail.com', '$2a$10$8TWonhaYGbjZ1C69pQwB0uWBOANl1FCwz0wxH9z2LsKXIhTM1hUay', 'Elo', 'Elic', '012345678', 'Novi Sad', 'default-user.png', 3, true, false, to_timestamp('17.01.2023. 14:20', 'DD.MM.YYYY HH24:MI'), to_timestamp('18.01.2023. 14:20', 'DD.MM.YYYY HH24:MI'), 0, 0, true, false, false, 6);

insert into drivings (active, driver_id, driving_status, duration, price, started, end_date, route_id, last_reminder, reservation) values
                                                                                                                                       (true, 18, 2, 2, 3, to_timestamp('25.01.2023. 20:48', 'DD.MM.YYYY HH24:MI'), null, 1, null, false),
                                                                                                                                       (true, 14, 2, 8, 3, to_timestamp('25.01.2023. 8:25', 'DD.MM.YYYY HH24:MI'), null, 2, null, false),
                                                                                                                                       (false, 18, 2, 10, 5, to_timestamp('25.01.2023 18:50', 'DD.MM.YYYY HH24:MI'), null, 3, null, false);


insert into driving_locations(location_id, index, route_id, route_index) values
                                                                             (5, 1, 1, 0),
                                                                             (10, 2, 1, 0),
                                                                             (7, 3, 1, 0),
                                                                             (14, 4, 1, 0),
                                                                             (2,1,2, 0),
                                                                             (3,2,2, 0),
                                                                             (4,1,3, 0),
                                                                             (1,2,3, 0),
                                                                             (5, 1,4, 0),
                                                                             (15, 2, 4, 0),
                                                                             (3, 3, 4, 0);

insert into drivings_users(driving_id, user_id) values
                                                    (1, 8),
                                                    (1, 9),
                                                    (2, 10),
                                                    (3, 7);

insert into chat_rooms(client_id, admin_id, resolved) values
    (3, 1, true);
insert into chat_rooms(client_id, admin_id, resolved) values
    (3, 1, false);
insert into chat_rooms(client_id, admin_id, resolved) values
    (4, 1, false);
insert into chat_rooms(client_id, admin_id, resolved) values
    (14, 2, false);


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

-- insert into reviews(vehicle_rate, driver_rate, message, sender_id, driving_id) values
--     (4, 4.3, 'Cisto vozilo, prijatna voznja..', 2, 1),
--     (2.1, 4, 'Klima nije radila...', 2, 2),
--     (2.6, 4, 'Klima ne radi, vozilo prljavo.', 3, 2),
--     (5, 4, 'Dobar utisak..', 3, 4);

insert into reports(id, admin_email, answered, sender_id, receiver_id, message, time_stamp) values
                                                                                                (nextval('notifications_id_gen'), null, false, 2, 5, 'Vozac je bezobrazan!','2022-11-20 14:00'),
                                                                                                (nextval('notifications_id_gen'), null, false, 3, 5, 'Vozac je skrenuo sa zadate putanje!', '2022-12-01 13:00'),
                                                                                                (nextval('notifications_id_gen'), null, false, 5, 2,'Ana je prosula sok po sedistu!', '2022-12-01 13:25');


insert into favourite_routes (user_id, route_id) values
    (3,1);

insert into paying_info(currency, token_price, max_num_of_tokens_per_transaction) values
    ('EUR', 1, 20);

insert into token_banks(user_id, num_of_tokens, total_token_amount_spent, total_money_amount_spent, paying_info_id) values
                                                                                                                        (3, 4, 0, 4, 1),
                                                                                                                        (4, 8, 0, 8, 1),
                                                                                                                        (5, 5, 1, 6, 1),
                                                                                                                        (6, 3, 0, 3, 1),
                                                                                                                        (7, 1, 0, 1, 1),
                                                                                                                        (8, 10, 0, 10, 1),
                                                                                                                        (9, 10, 0, 10, 1),
                                                                                                                        (10, 10, 0, 10, 1),
                                                                                                                        (11, 10, 0, 10, 1),
                                                                                                                        (12, 10, 0, 10, 1),
                                                                                                                        (13, 10, 0, 10, 1);


insert into token_transactions(time_stamp, num_of_bought_tokens, total_price, token_bank_id) values
                                                                                                 ('2022-12-01 14:03', 4, 4, 1),
                                                                                                 ('2022-12-03 14:00', 7, 7, 1),
                                                                                                 ('2022-12-03 15:00', 8, 8, 2);

insert into bell_notifications(message, redirect_id, seen, should_redirect, time_stamp, user_id) values
                                                                                                     ('Driver has declined driving.', null, false, false, '2022-01-01 14:03', 3),
                                                                                                     ('Driver accepted driving.', null, false, false, '2022-01-01 14:05', 3),
                                                                                                     ('Driver accepted driving.', null, false, false, '2022-12-01 14:03', 4);

