-- One admin user, named admin1 with passwor 4dm1n and authority admin
INSERT INTO users(username,password,enabled) VALUES ('admin1','4dm1n',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (1,'admin1','admin');
-- One player user, named player1 with passwor 0wn3r
INSERT INTO users(username,password,enabled) VALUES ('player1','0wn3r',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (2,'player1','player');
INSERT INTO users(username,password,enabled) VALUES ('player2','0wn3r',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (3,'player2','player');
INSERT INTO users(username,password,enabled) VALUES ('player3','0wn3r',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (4,'player3','player');
INSERT INTO users(username,password,enabled) VALUES ('player4','0wn3r',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (5,'player4','player');
INSERT INTO users(username,password,enabled) VALUES ('player5','0wn3r',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (6,'player5','player');
INSERT INTO users(username,password,enabled) VALUES ('player6','0wn3r',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (7,'player6','player');
INSERT INTO users(username,password,enabled) VALUES ('player7','0wn3r',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (8,'player7','player');
INSERT INTO users(username,password,enabled) VALUES ('player8','0wn3r',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (9,'player8','player');
INSERT INTO users(username,password,enabled) VALUES ('player9','0wn3r',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (10,'player9','player');
INSERT INTO users(username,password,enabled) VALUES ('player10','0wn3r',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (11,'player10','player');
-- One vet user, named vet1 with passwor v3t
INSERT INTO users(username,password,enabled) VALUES ('vet1','v3t',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (99,'vet1','veterinarian');

-- Meted aqui los usuarios antes de añadiros a la tabla de players
INSERT INTO users(username,password,enabled) VALUES ('cardelbec','cardelbec',TRUE);
INSERT INTO authorities(id, username, authority) VALUES (12, 'cardelbec', 'player');
INSERT INTO users(username,password,enabled) VALUES ('sonlaumot','sonlaumot',TRUE);
INSERT INTO authorities(id, username, authority) VALUES (13, 'sonlaumot', 'player');
INSERT INTO users(username,password,enabled) VALUES ('celhersot','c3lh3rs0t',TRUE);
INSERT INTO authorities(id, username, authority) VALUES (14, 'celhersot', 'player');
INSERT INTO users(username,password,enabled) VALUES ('manlopalm', 'manlopalm',TRUE);
INSERT INTO authorities(id, username, authority) VALUES (15, 'manlopalm', 'player');
INSERT INTO users(username,password,enabled) VALUES ('mandommag','mandommag',TRUE);
INSERT INTO authorities(id, username, authority) VALUES (16, 'mandommag', 'player');

INSERT INTO players(id, first_name, last_name, email, username, round_id) VALUES (1, 'George', 'Franklin', 'ejemplo@gmail.com', 'player1', NULL);
INSERT INTO players(id, first_name, last_name, email, username, round_id) VALUES (2, 'Betty', 'Davis', 'ejemplo@gmail.com', 'player2', NULL);
INSERT INTO players(id, first_name, last_name, email, username, round_id) VALUES (3, 'Eduardo', 'Rodriquez', 'ejemplo@gmail.com', 'player3', null);
INSERT INTO players(id, first_name, last_name, email, username, round_id) VALUES (4, 'Harold', 'Davis', 'ejemplo@gmail.com', 'player4', null);
INSERT INTO players(id, first_name, last_name, email, username, round_id) VALUES (5, 'Peter', 'McTavish', 'ejemplo@gmail.com', 'player5', null);
INSERT INTO players(id, first_name, last_name, email, username, round_id) VALUES (6, 'Jean', 'Coleman', 'ejemplo@gmail.com', 'player6', null);
INSERT INTO players(id, first_name, last_name, email, username, round_id) VALUES (7, 'Jeff', 'Black', 'ejemplo@gmail.com', 'player7', null);
INSERT INTO players(id, first_name, last_name, email, username, round_id) VALUES (8, 'Maria', 'Escobito', 'ejemplo@gmail.com', 'player8', null);
INSERT INTO players(id, first_name, last_name, email, username, round_id) VALUES (9, 'David', 'Schroeder', 'ejemplo@gmail.com', 'player9', null);
INSERT INTO players(id, first_name, last_name, email, username, round_id) VALUES (10, 'Carlos', 'Estaban', 'ejemplo@gmail.com', 'player10', null);
INSERT INTO players(id, first_name, last_name, email, username, round_id) VALUES (12, 'Sonia', 'Laure', 'ejemplo@gmail.com', 'sonlaumot', null);
INSERT INTO players(id, first_name, last_name, email, username, round_id) VALUES (13, 'Celia', 'Hermoso', 'ejemplo@gmail.com', 'celhersot', null);
INSERT INTO players(id, first_name, last_name, email, username, round_id) VALUES (15, 'Manuel', 'Dominguez', 'ejemplo@gmail.com', 'mandommag', null);


INSERT INTO rounds(id,duration,rapids,whirlpools,num_players, match_start, match_end, turn_start, player_id, round_state) VALUES (1, 0, false, false, 3, '2013-01-04 08:00', '2013-01-04 08:00', '2013-01-04 08:00',13, 0);
INSERT INTO players(id, first_name, last_name, email, username, round_id) VALUES (11, 'Carlos', 'Delgado', 'ejemplo@gmail.com', 'cardelbec', 1);
INSERT INTO players(id, first_name, last_name, email, username, round_id) VALUES (14, 'Manuel', 'López', 'ejemplo@gmail.com','manlopalm', 1);


INSERT INTO acting_players(id, player, points, first_player,turn,round_id) VALUES (1,0,5,0,1,1);

INSERT INTO tiles(id, row_index, column_index, orientation, salmon_eggs, tile_type, round_id) VALUES (1, 1, 1, 1, 0, 8, 1);
INSERT INTO tiles(id, row_index, column_index, orientation, salmon_eggs, tile_type, round_id) VALUES (2, 1, 2, 1, 0, 8, 1);
INSERT INTO tiles(id, row_index, column_index, orientation, salmon_eggs, tile_type, round_id) VALUES (3, 1, 3, 1, 0, 8, 1);
INSERT INTO tiles(id, row_index, column_index, orientation, salmon_eggs, tile_type, round_id) VALUES (4, 2, 2, 1, 0, 8, 1);
INSERT INTO tiles(id, row_index, column_index, orientation, salmon_eggs, tile_type, round_id) VALUES (5, 2, 1, 1, 0, 1, 1);
INSERT INTO tiles(id, row_index, column_index, orientation, salmon_eggs, tile_type, round_id) VALUES (6, 2, 3, 1, 0, 0, 1);
INSERT INTO tiles(id, row_index, column_index, orientation, salmon_eggs, tile_type, round_id) VALUES (7, 3, 1, 1, 0, 4, 1);
INSERT INTO tiles(id, row_index, column_index, orientation, salmon_eggs, tile_type, round_id) VALUES (8, 3, 2, 1, 0, 1, 1);
INSERT INTO tiles(id, row_index, column_index, orientation, salmon_eggs, tile_type, round_id) VALUES (9, 3, 3, 1, 0, 0, 1);
INSERT INTO tiles(id, row_index, column_index, orientation, salmon_eggs, tile_type, round_id) VALUES (10, 4, 1, 1, 0, 4, 1);
INSERT INTO tiles(id, row_index, column_index, orientation, salmon_eggs, tile_type, round_id) VALUES (11, 4, 2, 1, 0, 1, 1);
INSERT INTO tiles(id, row_index, column_index, orientation, salmon_eggs, tile_type, round_id) VALUES (12, 4, 3, 1, 0, 0, 1);


INSERT INTO pieces(id, num_salmon, stuck, player_id, round_id, tile_id) VALUES (1, 2, false, 11, 1, 2);
INSERT INTO pieces(id, num_salmon, stuck, player_id, round_id, tile_id) VALUES (2, 2, false, 14, 1, 1);


INSERT INTO scores(id, value, player_id, round_id) VALUES (1, 0, 11, 1);
INSERT INTO scores(id, value, player_id, round_id) VALUES (2, 0, 14, 1);
