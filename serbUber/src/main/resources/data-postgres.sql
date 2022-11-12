INSERT INTO ROLE (name) VALUES ('ROLE_ADMIN');
INSERT INTO ROLE (name) VALUES ('ROLE_REGULAR_USER');
INSERT INTO ROLE (name) VALUES ('ROLE_DRIVER');
--sifra123@
insert into admins (id, email, password, name, surname, phone_number, city, profile_picture, role_id) values
    (nextval('users_id_gen'), 'pera@gmail.com', '$2a$10$8TWonhaYGbjZ1C69pQwB0uWBOANl1FCwz0wxH9z2LsKXIhTM1hUay', 'Pera', 'Peric', '012345678', 'Novi Sad', 'default-user.png', 1);

insert into regular_users (id, email, password, name, surname, phone_number, city, profile_picture, role_id, blocked, verified) values
    (nextval('users_id_gen'), 'ana@gmail.com', '$2a$10$8TWonhaYGbjZ1C69pQwB0uWBOANl1FCwz0wxH9z2LsKXIhTM1hUay', 'Ana', 'Ancic', '012345678', 'Novi Sad', 'default-user.png',2, false, true);

insert into regular_users (id, email, password, name, surname, phone_number, city, profile_picture, role_id, blocked, verified) values
    (nextval('users_id_gen'), 'miki@gmail.com', '$2a$10$8TWonhaYGbjZ1C69pQwB0uWBOANl1FCwz0wxH9z2LsKXIhTM1hUay', 'Miki', 'Mikic', '0651234567', 'Novi Sad', 'default-user.png',2, false, true);

insert into regular_users (id, email, password, name, surname, phone_number, city, profile_picture, role_id, blocked, verified) values
    (nextval('users_id_gen'), 'zoka200015@gmail.com', '$2a$10$8TWonhaYGbjZ1C69pQwB0uWBOANl1FCwz0wxH9z2LsKXIhTM1hUay', 'Zorica', 'Vukovic', '0651234567', 'Novi Sad', 'default-user.png',2, false, true);

insert into vehicle_type_infos (num_of_seats, start_price, vehicle_type) values
    (9, 200, 0);
insert into vehicle_type_infos (num_of_seats, start_price, vehicle_type) values
    (5, 100, 1);
insert into vehicle_type_infos (num_of_seats, start_price, vehicle_type) values
    (7, 150, 2);

insert into vehicles (baby_seat, pet_friendly, rate, vehicle_type_id) values
    (true, false, 0, 1);
insert into vehicles (baby_seat, pet_friendly, rate, vehicle_type_id) values
    (true, true, 0, 2);
insert into vehicles (baby_seat, pet_friendly, rate, vehicle_type_id) values
    (false, true, 0, 3);

-- insert into routes (kilometres, location_id, )
insert into locations (city, lat, lon, street, number, zip_code) values
    ('Novi Sad', 0, 0, 'Bulevar Oslobodjenja', '55', '21000');
insert into locations (city, lat, lon, street, number, zip_code) values
    ('Novi Sad', 10, 10, 'Bulevar Cara Lazara', '5', '21000');
insert into locations (city, lat, lon, street, number, zip_code) values
    ('Novi Sad', 20, 20, 'Bulevar Evrope', '10', '21000');
insert into locations (city, lat, lon, street, number, zip_code) values
    ('Novi Sad', 30, 30, 'Lasla Gala', '2', '21000');

insert into routes (kilometers, location_id) values
    (3, 1);
insert into routes (kilometers, location_id) values
    (5, 1);
insert into routes (kilometers, location_id) values
    (6, 3);

insert into favourite_routes (user_id, route_id) values
    (2, 1);
insert into favourite_routes (user_id, route_id) values
    (3, 1);

insert into route_destinations (route_id, location_id) values
    (1, 2);
insert into route_destinations (route_id, location_id) values
    (2, 3);
insert into route_destinations (route_id, location_id) values
    (2, 1);
insert into route_destinations (route_id, location_id) values
    (3, 4);


insert into drivers (id, email, password, name, surname, phone_number, city, profile_picture, role_id, active, blocked, verified, start_shift, end_shift, last_active, rate, working_minutes, location_id, vehicle_id) values
    (nextval('users_id_gen'), 'mile@gmail.com', '$2a$10$8TWonhaYGbjZ1C69pQwB0uWBOANl1FCwz0wxH9z2LsKXIhTM1hUay', 'Mile', 'Milic', '012345678', 'Novi Sad', 'default-user.png', 3, false, false, true, null, null, null, 0, 0, 1, 1);

insert into drivers (id, email, password, name, surname, phone_number, city, profile_picture, role_id, active, blocked, verified, start_shift, end_shift, last_active, rate, working_minutes, location_id, vehicle_id) values
    (nextval('users_id_gen'), 'milan@gmail.com', '$2a$10$8TWonhaYGbjZ1C69pQwB0uWBOANl1FCwz0wxH9z2LsKXIhTM1hUay', 'Milan', 'Milanovic', '012345678', 'Novi Sad', 'default-user.png', 3, false, false, true, null, null, null, 0, 0, 1, 2);

insert into drivings (active, driver_email, driving_status, duration, paying_limit, price, started, route_id) values
    (false, 'mile@gmail.com', 1, 10, null, 400, to_timestamp('06.11.2022. 14:00', 'DD.MM.YYYY HH24:MI'), 1);
insert into drivings (active, driver_email, driving_status, duration, paying_limit, price, started, route_id) values
    (false, 'milan@gmail.com', 1, 10, null, 500,to_timestamp('24.08.2022. 14:00', 'DD.MM.YYYY HH24:MI'), 3);

insert into drivings_users(driving_id, user_id) values
    (1, 2);
insert into drivings_users(driving_id, user_id) values
    (2, 2);


insert into messages(id, message, time_stamp, sender_id, receiver_id, admin_response) values
    (nextval('notifications_id_gen'), 'Heej, imam problem', '11.11.2022. 11:00', 2, 1, false);
insert into messages(id, message, time_stamp, sender_id, receiver_id, admin_response) values
    (nextval('notifications_id_gen'), 'Sta te muci?', '11.11.2022. 11:20', 2, 1, true);
insert into messages(id, message, time_stamp, sender_id, receiver_id, admin_response) values
    (nextval('notifications_id_gen'), 'Mozes mi reci..', '11.11.2022. 11:29', 2, 1, true);
insert into messages(id, message, time_stamp, sender_id, receiver_id, admin_response) values
    (nextval('notifications_id_gen'), 'Ma zezam te..', '11.11.2022. 11:45', 2, 1, false);
insert into messages(id, message, time_stamp, sender_id, receiver_id, admin_response) values
    (nextval('notifications_id_gen'), 'Pomoc na putu..', '11.11.2022. 15:00', 5, 1, false);
insert into messages(id, message, time_stamp, sender_id, receiver_id, admin_response) values
    (nextval('notifications_id_gen'), 'Tu si?', '11.11.2022. 16:00', 5, 1, false);



