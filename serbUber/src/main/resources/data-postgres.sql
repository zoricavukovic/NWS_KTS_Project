INSERT INTO ROLE (name) VALUES ('ROLE_ADMIN');
INSERT INTO ROLE (name) VALUES ('ROLE_REGULAR_USER');
INSERT INTO ROLE (name) VALUES ('ROLE_DRIVER');

insert into admins (id, email, password, name, surname, phone_number, city, profile_picture, role_id) values
(nextval('users_id_gen'), 'pera@gmail.com', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', 'Pera', 'Peric', '012345678', 'Novi Sad', '/person.png', 1);
---123 lozinka svuda
-- insert into regular_users (id, email, password, name, surname, phone_number, city, profile_picture) values
-- (next_val('user_seq'), 'ana@gmail.com', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', 'Ana', 'Ancic', '012345678', 'Novi Sad', '/person.png');
-- insert into drivers (id, email, password, name, surname, phone_number, city, profile_picture) values
-- (next_val('driver_seq'), 'miki@gmail.com', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', 'Miki', 'Mikic', '012345678', 'Novi Sad', '/person.png');