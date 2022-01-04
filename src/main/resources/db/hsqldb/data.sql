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

INSERT INTO players(id, first_name, last_name, email, username, round_id) VALUES (7, 'Jeff', 'Black', 'ejemplo@gmail.com', 'player7', null);
INSERT INTO players(id, first_name, last_name, email, username, round_id) VALUES (8, 'Maria', 'Escobito', 'ejemplo@gmail.com', 'player8', null);
INSERT INTO players(id, first_name, last_name, email, username, round_id) VALUES (9, 'David', 'Schroeder', 'ejemplo@gmail.com', 'player9', null);
INSERT INTO players(id, first_name, last_name, email, username, round_id) VALUES (10, 'Carlos', 'Estaban', 'ejemplo@gmail.com', 'player10', null);
INSERT INTO players(id, first_name, last_name, email, username, round_id) VALUES (12, 'Sonia', 'Laure', 'ejemplo@gmail.com', 'sonlaumot', null);
INSERT INTO players(id, first_name, last_name, email, username, round_id) VALUES (13, 'Celia', 'Hermoso', 'ejemplo@gmail.com', 'celhersot', null);
INSERT INTO players(id, first_name, last_name, email, username, round_id) VALUES (15, 'Manuel', 'Dominguez', 'ejemplo@gmail.com', 'mandommag', null);


INSERT INTO rounds(id,duration,rapids,whirlpools,num_players, match_start, match_end, turn_start, player_id, round_state) VALUES (1, 0, false, false, 3, '2013-01-04 08:00', '2013-01-04 08:00', '2013-01-04 08:00',13, 1);
INSERT INTO players(id, first_name, last_name, email, username, round_id) VALUES (11, 'Carlos', 'Delgado', 'ejemplo@gmail.com', 'cardelbec', 1);
INSERT INTO players(id, first_name, last_name, email, username, round_id) VALUES (14, 'Manuel', 'López', 'ejemplo@gmail.com','manlopalm', 1);

INSERT INTO rounds(id,duration,rapids,whirlpools,num_players, match_start, match_end, turn_start, player_id, round_state) VALUES (2, 0, false, false, 3, '2013-01-04 08:00', '2013-01-04 08:00', '2013-01-04 08:00',13, 1);
INSERT INTO rounds(id,duration,rapids,whirlpools,num_players, match_start, match_end, turn_start, player_id, round_state) VALUES (3, 0, false, false, 3, '2013-01-04 08:00', '2013-01-04 08:00', '2013-01-04 08:00',13, 2);

INSERT INTO acting_players(id, player, points, first_player,turn,round_id) VALUES (1,0,5,0,1,1);

INSERT INTO tiles(id, row_index, column_index, orientation, salmon_eggs, tile_type, round_id) VALUES (1, 1, 1, 1, 0, 8, 1);
INSERT INTO tiles(id, row_index, column_index, orientation, salmon_eggs, tile_type, round_id) VALUES (2, 1, 2, 0, 0, 8, 1);
INSERT INTO tiles(id, row_index, column_index, orientation, salmon_eggs, tile_type, round_id) VALUES (3, 1, 3, 3, 0, 8, 1);
INSERT INTO tiles(id, row_index, column_index, orientation, salmon_eggs, tile_type, round_id) VALUES (4, 2, 2, 2, 0, 8, 1);
INSERT INTO tiles(id, row_index, column_index, orientation, salmon_eggs, tile_type, round_id) VALUES (5, 2, 1, 0, 0, 1, 1);
INSERT INTO tiles(id, row_index, column_index, orientation, salmon_eggs, tile_type, round_id) VALUES (6, 2, 3, 0, 0, 0, 1);
INSERT INTO tiles(id, row_index, column_index, orientation, salmon_eggs, tile_type, round_id) VALUES (7, 3, 1, 0, 0, 4, 1);
INSERT INTO tiles(id, row_index, column_index, orientation, salmon_eggs, tile_type, round_id) VALUES (8, 3, 2, 0, 0, 1, 1);
INSERT INTO tiles(id, row_index, column_index, orientation, salmon_eggs, tile_type, round_id) VALUES (9, 3, 3, 0, 0, 0, 1);
INSERT INTO tiles(id, row_index, column_index, orientation, salmon_eggs, tile_type, round_id) VALUES (10, 4, 1, 0, 0, 4, 1);
INSERT INTO tiles(id, row_index, column_index, orientation, salmon_eggs, tile_type, round_id) VALUES (11, 4, 2, 0, 0, 1, 1);
INSERT INTO tiles(id, row_index, column_index, orientation, salmon_eggs, tile_type, round_id) VALUES (12, 4, 3, 9, 0, 0, 1);



INSERT INTO salmonboards(id,background,height,width,round) VALUES (1,'/resources/images/back_pattern.jpg',800,800,1);
INSERT INTO piece(id, num_salmon, stuck, player_id, round_id, tile_id,color) VALUES (1, 1, false, 11, 1, 2, 4);
INSERT INTO piece(id, num_salmon, stuck, player_id, round_id, tile_id,color) VALUES (3, 2, false, 14, 1, 2, 3);
INSERT INTO piece(id, num_salmon, stuck, player_id, round_id, tile_id,color) VALUES (4, 1, false, 11, 1, 2, 0);
INSERT INTO piece(id, num_salmon, stuck, player_id, round_id, tile_id,color) VALUES (5, 2, false, 14, 1, 2, 1);
INSERT INTO piece(id, num_salmon, stuck, player_id, round_id, tile_id,color) VALUES (6, 2, false, 14, 1, 2, 2);

INSERT INTO piece(id, num_salmon, stuck, player_id, round_id, tile_id,color) VALUES (7, 1, false, 11, 1, 1, 4);
INSERT INTO piece(id, num_salmon, stuck, player_id, round_id, tile_id,color) VALUES (8, 2, false, 14, 1, 1, 3);
INSERT INTO piece(id, num_salmon, stuck, player_id, round_id, tile_id,color) VALUES (9, 1, false, 11, 1, 1, 0);
INSERT INTO piece(id, num_salmon, stuck, player_id, round_id, tile_id,color) VALUES (10, 2, false, 14, 1, 1, 1);
INSERT INTO piece(id, num_salmon, stuck, player_id, round_id, tile_id,color) VALUES (11, 2, false, 14, 1, 1, 2);

INSERT INTO piece(id, num_salmon, stuck, player_id, round_id, tile_id,color) VALUES (12, 1, false, 11, 1, 3, 4);
INSERT INTO piece(id, num_salmon, stuck, player_id, round_id, tile_id,color) VALUES (13, 2, false, 14, 1, 3, 3);
INSERT INTO piece(id, num_salmon, stuck, player_id, round_id, tile_id,color) VALUES (14, 1, false, 11, 1, 3, 0);
INSERT INTO piece(id, num_salmon, stuck, player_id, round_id, tile_id,color) VALUES (15, 2, false, 14, 1, 3, 1);
INSERT INTO piece(id, num_salmon, stuck, player_id, round_id, tile_id,color) VALUES (16, 2, false, 14, 1, 3, 2);

INSERT INTO piece(id, num_salmon, stuck, player_id, round_id, tile_id,color) VALUES (17, 1, false, 11, 1, 4, 4);
INSERT INTO piece(id, num_salmon, stuck, player_id, round_id, tile_id,color) VALUES (18, 2, false, 14, 1, 4, 3);
INSERT INTO piece(id, num_salmon, stuck, player_id, round_id, tile_id,color) VALUES (19, 1, false, 11, 1, 4, 0);
INSERT INTO piece(id, num_salmon, stuck, player_id, round_id, tile_id,color) VALUES (20, 2, false, 14, 1, 4, 1);
INSERT INTO piece(id, num_salmon, stuck, player_id, round_id, tile_id,color) VALUES (21, 2, false, 14, 1, 4, 2);
--INSERT INTO piece(id, num_salmon, stuck, player_id, round_id, tile_id) VALUES (2, 2, false, 14, 1, 1);



--INSERT INTO piece(id,color,type,x_position,y_position,board_id) VALUES (1,'BLACK','HORSE',1,1,1);
--INSERT INTO piece(id,color,type,x_position,y_position,board_id) VALUES (2,'BLACK','KING',5,1,1);
--INSERT INTO piece(id,color,type,x_position,y_position,board_id) VALUES (3,'WHITE','KING',7,5,1);
--INSERT INTO piece(id,color,type,x_position,y_position,board_id) VALUES (4,'BLACK','HORSE',6,1,1);

INSERT INTO scores(id, value, player_id, round_id) VALUES (1, 0, 11, 1);
INSERT INTO scores(id, value, player_id, round_id) VALUES (2, 0, 14, 1);

INSERT INTO rounds(id,duration,rapids,whirlpools,num_players, match_start, match_end, turn_start, player_id, round_state) VALUES (4, 0, false, false, 2, '2013-01-04 08:00', '2013-01-04 08:00', '2013-01-04 08:00',13, 1);
INSERT INTO players(id, first_name, last_name, email, username, round_id) VALUES (5, 'Peter', 'McTavish', 'ejemplo@gmail.com', 'player5', 4);
INSERT INTO players(id, first_name, last_name, email, username, round_id) VALUES (6, 'Jean', 'Coleman', 'ejemplo@gmail.com', 'player6', 4);
INSERT INTO acting_players(id, player, points, first_player,turn,round_id) VALUES (2,0,5,0,1,4);
INSERT INTO tiles(id, row_index, column_index, orientation, salmon_eggs, tile_type, round_id) VALUES (13, 1, 1, 1, 0, 8, 4);
INSERT INTO tiles(id, row_index, column_index, orientation, salmon_eggs, tile_type, round_id) VALUES (14, 1, 2, 1, 0, 8, 4);
INSERT INTO tiles(id, row_index, column_index, orientation, salmon_eggs, tile_type, round_id) VALUES (15, 1, 3, 1, 0, 8, 4);
INSERT INTO tiles(id, row_index, column_index, orientation, salmon_eggs, tile_type, round_id) VALUES (16, 2, 1, 1, 0, 8, 4);
INSERT INTO tiles(id, row_index, column_index, orientation, salmon_eggs, tile_type, round_id) VALUES (17, 2, 2, 1, 0, 1, 4);
INSERT INTO tiles(id, row_index, column_index, orientation, salmon_eggs, tile_type, round_id) VALUES (18, 2, 3, 1, 0, 0, 4);
INSERT INTO tiles(id, row_index, column_index, orientation, salmon_eggs, tile_type, round_id) VALUES (19, 3, 1, 3, 0, 3, 4);
INSERT INTO tiles(id, row_index, column_index, orientation, salmon_eggs, tile_type, round_id) VALUES (20, 3, 2, 1, 0, 1, 4);
INSERT INTO tiles(id, row_index, column_index, orientation, salmon_eggs, tile_type, round_id) VALUES (21, 3, 3, 1, 0, 0, 4);
INSERT INTO tiles(id, row_index, column_index, orientation, salmon_eggs, tile_type, round_id) VALUES (22, 4, 1, 1, 0, 4, 4);

INSERT INTO tiles(id, row_index, column_index, orientation, salmon_eggs, tile_type, round_id) VALUES (23, 4, 2, 1, 0, 4, 4);
INSERT INTO tiles(id, row_index, column_index, orientation, salmon_eggs, tile_type, round_id) VALUES (24, 4, 3, 1, 0, 4, 4);
INSERT INTO tiles(id, row_index, column_index, orientation, salmon_eggs, tile_type, round_id) VALUES (25, 5, 1, 1, 0, 3, 4);
INSERT INTO tiles(id, row_index, column_index, orientation, salmon_eggs, tile_type, round_id) VALUES (26, 5, 2, 1, 0, 4, 4);
INSERT INTO tiles(id, row_index, column_index, orientation, salmon_eggs, tile_type, round_id) VALUES (27, 5, 3, 1, 0, 4, 4);
INSERT INTO tiles(id, row_index, column_index, orientation, salmon_eggs, tile_type, round_id) VALUES (28, 6, 1, 1, 0, 4, 4);
INSERT INTO tiles(id, row_index, column_index, orientation, salmon_eggs, tile_type, round_id) VALUES (29, 6, 2, 1, 0, 6, 4);
INSERT INTO tiles(id, row_index, column_index, orientation, salmon_eggs, tile_type, round_id) VALUES (30, 6, 3, 1, 0, 4, 4);
INSERT INTO tiles(id, row_index, column_index, orientation, salmon_eggs, tile_type, round_id) VALUES (31, 7, 1, 3, 0, 0, 4);
INSERT INTO tiles(id, row_index, column_index, orientation, salmon_eggs, tile_type, round_id) VALUES (32, 7, 2, 1, 0, 4, 4);
INSERT INTO tiles(id, row_index, column_index, orientation, salmon_eggs, tile_type, round_id) VALUES (33, 7, 3, 1, 0, 4, 4);
INSERT INTO tiles(id, row_index, column_index, orientation, salmon_eggs, tile_type, round_id) VALUES (34, 8, 1, 1, 0, 4, 4);
INSERT INTO tiles(id, row_index, column_index, orientation, salmon_eggs, tile_type, round_id) VALUES (35, 8, 2, 1, 0, 4, 4);
INSERT INTO tiles(id, row_index, column_index, orientation, salmon_eggs, tile_type, round_id) VALUES (36, 8, 3, 1, 0, 3, 4);
INSERT INTO piece(id, num_salmon, stuck, player_id, round_id, tile_id,color) VALUES (22, 2, false, 5, 4, 14,0);
INSERT INTO piece(id, num_salmon, stuck, player_id, round_id, tile_id,color) VALUES (23, 2, false, 5, 4, 19,1);
INSERT INTO piece(id, num_salmon, stuck, player_id, round_id, tile_id,color) VALUES (24, 2, false, 5, 4, 18,2);
INSERT INTO piece(id, num_salmon, stuck, player_id, round_id, tile_id,color) VALUES (25, 2, false, 5, 4, 25,3);
INSERT INTO piece(id, num_salmon, stuck, player_id, round_id, tile_id,color) VALUES (26, 2, false, 5, 4, 28,2);
INSERT INTO piece(id, num_salmon, stuck, player_id, round_id, tile_id,color) VALUES (27, 2, false, 5, 4, 26,0);
INSERT INTO piece(id, num_salmon, stuck, player_id, round_id, tile_id,color) VALUES (28, 2, false, 5, 4, 35,1);
INSERT INTO piece(id, num_salmon, stuck, player_id, round_id, tile_id,color) VALUES (29, 2, false, 5, 4, 31,2);

