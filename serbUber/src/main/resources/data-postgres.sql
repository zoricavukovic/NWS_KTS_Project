INSERT INTO ROLE (name) VALUES ('ROLE_ADMIN');
INSERT INTO ROLE (name) VALUES ('ROLE_REGULAR_USER');
INSERT INTO ROLE (name) VALUES ('ROLE_DRIVER');

insert into admins (id, email, password, name, surname, phone_number, city, profile_picture, role_id) values
    (nextval('users_id_gen'), 'pera@gmail.com', '123', 'Pera', 'Peric', '012345678', 'Novi Sad', '/person.png', 1);

insert into regular_users (id, email, password, name, surname, phone_number, city, profile_picture, role_id, blocked, verified) values
    (nextval('users_id_gen'), 'ana@gmail.com', '123', 'Ana', 'Ancic', '012345678', 'Novi Sad', '/person.png',2, false, true);

insert into regular_users (id, email, password, name, surname, phone_number, city, profile_picture, role_id, blocked, verified) values
    (nextval('users_id_gen'), 'miki@gmail.com', '123', 'Miki', 'Mikic', '0651234567', 'Novi Sad', '/person.png',2, false, true);

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

insert into route_destinations (route_id, location_id) values
    (1, 2);
insert into route_destinations (route_id, location_id) values
    (2, 3);
insert into route_destinations (route_id, location_id) values
    (2, 4);


insert into drivers (id, email, password, name, surname, phone_number, city, profile_picture, role_id, active, blocked, verified, start_shift, end_shift, last_active, rate, working_minutes, location_id, vehicle_id) values
    (nextval('users_id_gen'), 'mile@gmail.com', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', 'Mile', 'Milic', '012345678', 'Novi Sad', '/person.png', 3, false, false, true, null, null, null, 0, 0, 1, 1);

insert into drivers (id, email, password, name, surname, phone_number, city, profile_picture, role_id, active, blocked, verified, start_shift, end_shift, last_active, rate, working_minutes, location_id, vehicle_id) values
    (nextval('users_id_gen'), 'milan@gmail.com', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', 'Milan', 'Milanovic', '012345678', 'Novi Sad', '/person.png', 3, false, false, true, null, null, null, 0, 0, 1, 2);

insert into drivings (active, driver_email, driving_status, duration, paying_limit, price, started, route_id) values
    (false, 'mile@gmail.com', 1, 10, null, 350, null, 1);

insert into drivings_users(driving_id, user_id) values
    (1, 2);


insert into login_user_info (email, password, role_id) values
    ('pera@gmail.com', '123', 1);
insert into login_user_info (email, password, role_id) values
    ('ana@gmail.com', '123', 2);
insert into login_user_info (email, password, role_id) values
    ('miki@gmail.com', '123',2);
insert into login_user_info (email, password, role_id) values
    ('mile@gmail.com', '123',3);
insert into login_user_info (email, password, role_id) values
    ('milan@gmail.com', '123',3);