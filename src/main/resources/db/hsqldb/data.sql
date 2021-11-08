-- One admin user, named admin1 with passwor 4dm1n and authority admin
INSERT INTO users(username,password,enabled) VALUES ('admin1','4dm1n',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (1,'admin1','admin');
-- One player user, named player1 with passwor 0wn3r
INSERT INTO users(username,password,enabled) VALUES ('player1','0wn3r',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (2,'player1','player');
-- One vet user, named vet1 with passwor v3t
INSERT INTO users(username,password,enabled) VALUES ('vet1','v3t',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (3,'vet1','veterinarian');

-- Meted aqui los usuarios antes de añadiros a la tabla de players
INSERT INTO users(username,password,enabled) VALUES ('cardelbec','cardelbec',TRUE);
INSERT INTO authorities(id, username, authority) VALUES (4, 'cardelbec', 'player');
INSERT INTO users(username,password,enabled) VALUES ('sonlaumot','sonlaumot',TRUE);
INSERT INTO authorities(id, username, authority) VALUES (5, 'sonlaumot', 'player');
INSERT INTO users(username,password,enabled) VALUES ('celhersot','c3lh3rs0t',TRUE);
INSERT INTO authorities(id, username, authority) VALUES (6, 'celhersot', 'player');
INSERT INTO users(username,password,enabled) VALUES ('manlopalm', 'manlopalm',TRUE);
INSERT INTO authorities(id, username, authority) VALUES (7, 'manlopalm', 'player');
INSERT INTO users(username,password,enabled) VALUES ('mandommag','mandommag',TRUE);
INSERT INTO authorities(id, username, authority) VALUES (8, 'mandommag', 'player');

INSERT INTO vets VALUES (1, 'James', 'Carter');
INSERT INTO vets VALUES (2, 'Helen', 'Leary');
INSERT INTO vets VALUES (3, 'Linda', 'Douglas');
INSERT INTO vets VALUES (4, 'Rafael', 'Ortega');
INSERT INTO vets VALUES (5, 'Henry', 'Stevens');
INSERT INTO vets VALUES (6, 'Sharon', 'Jenkins');

INSERT INTO specialties VALUES (1, 'radiology');
INSERT INTO specialties VALUES (2, 'surgery');
INSERT INTO specialties VALUES (3, 'dentistry');

INSERT INTO vet_specialties VALUES (2, 1);
INSERT INTO vet_specialties VALUES (3, 2);
INSERT INTO vet_specialties VALUES (3, 3);
INSERT INTO vet_specialties VALUES (4, 2);
INSERT INTO vet_specialties VALUES (5, 1);

INSERT INTO types VALUES (1, 'cat');
INSERT INTO types VALUES (2, 'dog');
INSERT INTO types VALUES (3, 'lizard');
INSERT INTO types VALUES (4, 'snake');
INSERT INTO types VALUES (5, 'bird');
INSERT INTO types VALUES (6, 'hamster');

INSERT INTO players VALUES (1, 'George', 'Franklin', 'ejemplo@gmail.com', 'player1');
INSERT INTO players VALUES (2, 'Betty', 'Davis', 'ejemplo@gmail.com', 'player1');
INSERT INTO players VALUES (3, 'Eduardo', 'Rodriquez', 'ejemplo@gmail.com', 'player1');
INSERT INTO players VALUES (4, 'Harold', 'Davis', 'ejemplo@gmail.com', 'player1');
INSERT INTO players VALUES (5, 'Peter', 'McTavish', 'ejemplo@gmail.com', 'player1');
INSERT INTO players VALUES (6, 'Jean', 'Coleman', 'ejemplo@gmail.com', 'player1');
INSERT INTO players VALUES (7, 'Jeff', 'Black', 'ejemplo@gmail.com', 'player1');
INSERT INTO players VALUES (8, 'Maria', 'Escobito', 'ejemplo@gmail.com', 'player1');
INSERT INTO players VALUES (9, 'David', 'Schroeder', 'ejemplo@gmail.com', 'player1');
INSERT INTO players VALUES (10, 'Carlos', 'Estaban', 'ejemplo@gmail.com', 'player1');
INSERT INTO players VALUES (11, 'Carlos', 'Delgado', 'ejemplo@gmail.com', 'cardelbec');
INSERT INTO players VALUES (12, 'Sonia', 'Laure', 'ejemplo@gmail.com', 'sonlaumot');
INSERT INTO players VALUES (13, 'Celia', 'Hermoso', 'ejemplo@gmail.com', 'celhersot');
INSERT INTO players VALUES (14, 'Manuel', 'López', 'ejemplo@gmail.com','manlopalm');
INSERT INTO players VALUES (15, 'Manuel', 'Dominguez', 'ejemplo@gmail.com', 'mandommag');

INSERT INTO pets(id,name,birth_date,type_id,player_id) VALUES (1, 'Leo', '2010-09-07', 1, 1);
INSERT INTO pets(id,name,birth_date,type_id,player_id) VALUES (2, 'Basil', '2012-08-06', 6, 2);
INSERT INTO pets(id,name,birth_date,type_id,player_id) VALUES (3, 'Rosy', '2011-04-17', 2, 3);
INSERT INTO pets(id,name,birth_date,type_id,player_id) VALUES (4, 'Jewel', '2010-03-07', 2, 3);
INSERT INTO pets(id,name,birth_date,type_id,player_id) VALUES (5, 'Iggy', '2010-11-30', 3, 4);
INSERT INTO pets(id,name,birth_date,type_id,player_id) VALUES (6, 'George', '2010-01-20', 4, 5);
INSERT INTO pets(id,name,birth_date,type_id,player_id) VALUES (7, 'Samantha', '2012-09-04', 1, 6);
INSERT INTO pets(id,name,birth_date,type_id,player_id) VALUES (8, 'Max', '2012-09-04', 1, 6);
INSERT INTO pets(id,name,birth_date,type_id,player_id) VALUES (9, 'Lucky', '2011-08-06', 5, 7);
INSERT INTO pets(id,name,birth_date,type_id,player_id) VALUES (10, 'Mulligan', '2007-02-24', 2, 8);
INSERT INTO pets(id,name,birth_date,type_id,player_id) VALUES (11, 'Freddy', '2010-03-09', 5, 9);
INSERT INTO pets(id,name,birth_date,type_id,player_id) VALUES (12, 'Lucky', '2010-06-24', 2, 10);
INSERT INTO pets(id,name,birth_date,type_id,player_id) VALUES (13, 'Sly', '2012-06-08', 1, 10);
INSERT INTO pets(id,name,birth_date,type_id,player_id) VALUES (14, 'Manolo', '2012-06-08', 1, 11);
INSERT INTO pets(id,name,birth_date,type_id,player_id) VALUES (15, 'Godolfreda', '2013-07-15', 4, 12);
INSERT INTO pets(id,name,birth_date,type_id,player_id) VALUES (16, 'Chicharrones', '2010-01-31', 3, 13);
INSERT INTO pets(id,name,birth_date,type_id,player_id) VALUES (17, 'Pepe', '2009-02-24', 6, 14);
INSERT INTO pets(id,name,birth_date,type_id,player_id) VALUES (18, 'Celia la Lagarta', '2010-01-30', 3, 15);

INSERT INTO visits(id,pet_id,visit_date,description) VALUES (1, 7, '2013-01-01', 'rabies shot');
INSERT INTO visits(id,pet_id,visit_date,description) VALUES (2, 8, '2013-01-02', 'rabies shot');
INSERT INTO visits(id,pet_id,visit_date,description) VALUES (3, 8, '2013-01-03', 'neutered');
INSERT INTO visits(id,pet_id,visit_date,description) VALUES (4, 7, '2013-01-04', 'spayed');

INSERT INTO rounds(id,duration,rapids,whirlpools,num_players, match_start, match_end, turn_start) VALUES (1, 0, false, false, 3, '2013-01-04 08:00', '2013-01-04 08:00', '2013-01-04 08:00');
