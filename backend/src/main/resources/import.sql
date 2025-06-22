-- Roles possíveis
INSERT INTO role (authority) VALUES ('ROLE_ADMIN');
INSERT INTO role (authority) VALUES ('ROLE_CLIENTE');

-- Usuários
INSERT INTO usuario (nome, email, senha, login, telefone) VALUES ('Gustavo', 'gustavohp.vilela@gmail.com', '$2a$10$ZhadxzdYzVeN23JXcwr4yuyMa3d1UgFd3eDrjRZj4Aq8M3u1fAsKK', 'gustavo', '37987654321');
INSERT INTO usuario (nome, email, senha, login, telefone) VALUES ('Marcos',  'marcos@gmail.com', '$2a$10$ZhadxzdYzVeN23JXcwr4yuyMa3d1UgFd3eDrjRZj4Aq8M3u1fAsKK', 'marcos',  '37123456789');
INSERT INTO usuario (nome, email, senha, login, telefone) VALUES ('Nanda',   'nanda@gmail.com',  '$2a$10$ZhadxzdYzVeN23JXcwr4yuyMa3d1UgFd3eDrjRZj4Aq8M3u1fAsKK', 'nanda',   '37543216789');
INSERT INTO usuario (nome, email, senha, login, telefone) VALUES ('jj',      'jj@jj.com',       '$2a$10$2BzEm2Kl.u.2c3IfnRLNxOBRdFGT75juw.wN921PraGf0iKsyK/O.',    'jj',      '99999999999');
INSERT INTO usuario (nome, email, senha, login, telefone) VALUES ('Alice',   'alice@example.com',   '$2a$10$ZhadxzdYzVeN23JXcwr4yuyMa3d1UgFd3eDrjRZj4Aq8M3u1fAsKK', 'alice',   '31988887777');
INSERT INTO usuario (nome, email, senha, login, telefone) VALUES ('Bruno',   'bruno@example.com',   '$2a$10$ZhadxzdYzVeN23JXcwr4yuyMa3d1UgFd3eDrjRZj4Aq8M3u1fAsKK', 'bruno',   '31977776666');
INSERT INTO usuario (nome, email, senha, login, telefone) VALUES ('Carla',   'carla@example.com',   '$2a$10$ZhadxzdYzVeN23JXcwr4yuyMa3d1UgFd3eDrjRZj4Aq8M3u1fAsKK', 'carla',   '31966665555');
INSERT INTO usuario (nome, email, senha, login, telefone) VALUES ('Daniel',  'daniel@example.com',  '$2a$10$ZhadxzdYzVeN23JXcwr4yuyMa3d1UgFd3eDrjRZj4Aq8M3u1fAsKK', 'daniel',  '31955554444');
INSERT INTO usuario (nome, email, senha, login, telefone) VALUES ('Eduardo', 'eduardo@example.com', '$2a$10$ZhadxzdYzVeN23JXcwr4yuyMa3d1UgFd3eDrjRZj4Aq8M3u1fAsKK', 'eduardo', '31944443333');
INSERT INTO usuario (nome, email, senha, login, telefone) VALUES ('Fernanda','fernanda@example.com','$2a$10$ZhadxzdYzVeN23JXcwr4yuyMa3d1UgFd3eDrjRZj4Aq8M3u1fAsKK', 'fernanda','31933332222');
INSERT INTO usuario (nome, email, senha, login, telefone) VALUES ('Gisele',  'gisele@example.com',  '$2a$10$ZhadxzdYzVeN23JXcwr4yuyMa3d1UgFd3eDrjRZj4Aq8M3u1fAsKK', 'gisele',  '31922221111');
INSERT INTO usuario (nome, email, senha, login, telefone) VALUES ('Henrique','henrique@example.com','$2a$10$ZhadxzdYzVeN23JXcwr4yuyMa3d1UgFd3eDrjRZj4Aq8M3u1fAsKK', 'henrique','31911110000');
INSERT INTO usuario (nome, email, senha, login, telefone) VALUES ('Isabela', 'isabela@example.com', '$2a$10$ZhadxzdYzVeN23JXcwr4yuyMa3d1UgFd3eDrjRZj4Aq8M3u1fAsKK', 'isabela', '31900009999');
INSERT INTO usuario (nome, email, senha, login, telefone) VALUES ('João',    'joao@example.com',    '$2a$10$ZhadxzdYzVeN23JXcwr4yuyMa3d1UgFd3eDrjRZj4Aq8M3u1fAsKK', 'joao',    '31912344321');
INSERT INTO usuario (nome, email, senha, login, telefone) VALUES ('Karen',   'karen@example.com',   '$2a$10$ZhadxzdYzVeN23JXcwr4yuyMa3d1UgFd3eDrjRZj4Aq8M3u1fAsKK', 'karen',   '31943211234');

-- Associação usuário-role
INSERT INTO usuario_role (usuario_id, role_id) VALUES (1, 1);  -- Gustavo -> ADMIN
INSERT INTO usuario_role (usuario_id, role_id) VALUES (2, 2);  -- Marcos  -> CLIENTE
INSERT INTO usuario_role (usuario_id, role_id) VALUES (3, 2);  -- Nanda   -> CLIENTE
INSERT INTO usuario_role (usuario_id, role_id) VALUES (4, 1);  -- jj      -> ADMIN
INSERT INTO usuario_role (usuario_id, role_id) VALUES (5, 2);  -- Alice   -> CLIENTE
INSERT INTO usuario_role (usuario_id, role_id) VALUES (6, 2);  -- Bruno   -> CLIENTE
INSERT INTO usuario_role (usuario_id, role_id) VALUES (7, 2);  -- Carla   -> CLIENTE
INSERT INTO usuario_role (usuario_id, role_id) VALUES (8, 1);  -- Daniel  -> ADMIN
INSERT INTO usuario_role (usuario_id, role_id) VALUES (9, 2);  -- Eduardo -> CLIENTE
INSERT INTO usuario_role (usuario_id, role_id) VALUES (10, 2); -- Fernanda-> CLIENTE
INSERT INTO usuario_role (usuario_id, role_id) VALUES (11, 2); -- Gisele  -> CLIENTE
INSERT INTO usuario_role (usuario_id, role_id) VALUES (12, 2); -- Henrique-> CLIENTE
INSERT INTO usuario_role (usuario_id, role_id) VALUES (13, 1); -- Isabela -> ADMIN
INSERT INTO usuario_role (usuario_id, role_id) VALUES (14, 2); -- João    -> CLIENTE
INSERT INTO usuario_role (usuario_id, role_id) VALUES (15, 2); -- Karen   -> CLIENTE

-- Quartos
INSERT INTO quarto (valor, descricao, imagem_url) VALUES (40, 'Quarto com vista para o mar', 'vista-mar.jpg');
INSERT INTO quarto (valor, descricao, imagem_url) VALUES (50, 'Suíte executiva', 'executiva.jpg');
INSERT INTO quarto (valor, descricao, imagem_url) VALUES (60, 'Quarto standard', 'standard.jpg');
INSERT INTO quarto (valor, descricao, imagem_url) VALUES (70, 'Suíte presidencial', 'presidencial.jpg');
INSERT INTO quarto (valor, descricao, imagem_url) VALUES (80, 'Quarto família', 'familia.jpg');
INSERT INTO quarto (valor, descricao, imagem_url) VALUES (90, 'Quarto econômico', 'economico.jpg');
INSERT INTO quarto (valor, descricao, imagem_url) VALUES (100, 'Suíte com banheira', 'banheira.jpg');
INSERT INTO quarto (valor, descricao, imagem_url) VALUES (110, 'Quarto luxo com varanda', 'luxo-varanda.jpg');
INSERT INTO quarto (valor, descricao, imagem_url) VALUES (120, 'Suíte temática', 'tematica.jpg');
INSERT INTO quarto (valor, descricao, imagem_url) VALUES (130, 'Quarto com cama king-size', 'king.jpg');
INSERT INTO quarto (valor, descricao, imagem_url) VALUES (140, 'Quarto duplo', 'duplo.jpg');
INSERT INTO quarto (valor, descricao, imagem_url) VALUES (150, 'Suíte romântica', 'romantica.jpg');
INSERT INTO quarto (valor, descricao, imagem_url) VALUES (160, 'Quarto adaptado para PCD', 'pcd.jpg');
INSERT INTO quarto (valor, descricao, imagem_url) VALUES (170, 'Quarto com escritório', 'escritorio.jpg');
INSERT INTO quarto (valor, descricao, imagem_url) VALUES (180, 'Quarto com cozinha compacta', 'cozinha.jpg');

-- Estadias
INSERT INTO estadia (data_entrada, data_saida, quarto_id, cliente_id) VALUES ('2025-06-01', '2025-06-02',  1,  2);
INSERT INTO estadia (data_entrada, data_saida, quarto_id, cliente_id) VALUES ('2025-06-03', '2025-06-04',  2,  2);
INSERT INTO estadia (data_entrada, data_saida, quarto_id, cliente_id) VALUES ('2025-06-05', '2025-06-06',  3,  3);
INSERT INTO estadia (data_entrada, data_saida, quarto_id, cliente_id) VALUES ('2025-06-07', '2025-06-08',  4,  3);
INSERT INTO estadia (data_entrada, data_saida, quarto_id, cliente_id) VALUES ('2025-06-09', '2025-06-10',  5,  5);
INSERT INTO estadia (data_entrada, data_saida, quarto_id, cliente_id) VALUES ('2025-06-11', '2025-06-12',  6,  5);
INSERT INTO estadia (data_entrada, data_saida, quarto_id, cliente_id) VALUES ('2025-06-13', '2025-06-14',  7,  6);
INSERT INTO estadia (data_entrada, data_saida, quarto_id, cliente_id) VALUES ('2025-06-15', '2025-06-16',  8,  6);
INSERT INTO estadia (data_entrada, data_saida, quarto_id, cliente_id) VALUES ('2025-06-17', '2025-06-18',  9,  7);
INSERT INTO estadia (data_entrada, data_saida, quarto_id, cliente_id) VALUES ('2025-06-19', '2025-06-20', 10,  7);
INSERT INTO estadia (data_entrada, data_saida, quarto_id, cliente_id) VALUES ('2025-06-21', '2025-06-22', 11,  9);
INSERT INTO estadia (data_entrada, data_saida, quarto_id, cliente_id) VALUES ('2025-06-23', '2025-06-24', 12,  9);
INSERT INTO estadia (data_entrada, data_saida, quarto_id, cliente_id) VALUES ('2025-06-25', '2025-06-26', 13, 10);
INSERT INTO estadia (data_entrada, data_saida, quarto_id, cliente_id) VALUES ('2025-06-27', '2025-06-28', 14, 10);
INSERT INTO estadia (data_entrada, data_saida, quarto_id, cliente_id) VALUES ('2025-06-29', '2025-06-30', 15, 11);
INSERT INTO estadia (data_entrada, data_saida, quarto_id, cliente_id) VALUES ('2025-07-01', '2025-07-02', 4, 11);
INSERT INTO estadia (data_entrada, data_saida, quarto_id, cliente_id) VALUES ('2025-07-03', '2025-07-04', 2, 12);
INSERT INTO estadia (data_entrada, data_saida, quarto_id, cliente_id) VALUES ('2025-07-05', '2025-07-06', 6, 12);
INSERT INTO estadia (data_entrada, data_saida, quarto_id, cliente_id) VALUES ('2025-07-07', '2025-07-08',  1, 14);
INSERT INTO estadia (data_entrada, data_saida, quarto_id, cliente_id) VALUES ('2025-07-09', '2025-07-10',  2, 14);
INSERT INTO estadia (data_entrada, data_saida, quarto_id, cliente_id) VALUES ('2025-07-11', '2025-07-12',  3, 15);
INSERT INTO estadia (data_entrada, data_saida, quarto_id, cliente_id) VALUES ('2025-07-13', '2025-07-14',  4, 15);
INSERT INTO estadia (data_entrada, data_saida, quarto_id, cliente_id) VALUES ('2025-07-15', '2025-07-16',  5,  2);
INSERT INTO estadia (data_entrada, data_saida, quarto_id, cliente_id) VALUES ('2025-07-17', '2025-07-18',  6,  2);
INSERT INTO estadia (data_entrada, data_saida, quarto_id, cliente_id) VALUES ('2025-07-19', '2025-07-20',  7,  3);
INSERT INTO estadia (data_entrada, data_saida, quarto_id, cliente_id) VALUES ('2025-07-21', '2025-07-22',  8,  3);
INSERT INTO estadia (data_entrada, data_saida, quarto_id, cliente_id) VALUES ('2025-07-23', '2025-07-24',  9,  5);
INSERT INTO estadia (data_entrada, data_saida, quarto_id, cliente_id) VALUES ('2025-07-25', '2025-07-26', 10,  5);
INSERT INTO estadia (data_entrada, data_saida, quarto_id, cliente_id) VALUES ('2025-07-27', '2025-07-28', 11,  6);
INSERT INTO estadia (data_entrada, data_saida, quarto_id, cliente_id) VALUES ('2025-07-29', '2025-07-30', 12,  6);
INSERT INTO estadia (data_entrada, data_saida, quarto_id, cliente_id) VALUES ('2025-07-31', '2025-08-01', 13,  7);
INSERT INTO estadia (data_entrada, data_saida, quarto_id, cliente_id) VALUES ('2025-08-02', '2025-08-03', 14,  7);
INSERT INTO estadia (data_entrada, data_saida, quarto_id, cliente_id) VALUES ('2025-08-04', '2025-08-05', 15,  9);
INSERT INTO estadia (data_entrada, data_saida, quarto_id, cliente_id) VALUES ('2025-08-06', '2025-08-07', 4,  9);
INSERT INTO estadia (data_entrada, data_saida, quarto_id, cliente_id) VALUES ('2025-08-08', '2025-08-09', 9, 10);
INSERT INTO estadia (data_entrada, data_saida, quarto_id, cliente_id) VALUES ('2025-08-10', '2025-08-11', 1, 10);
INSERT INTO estadia (data_entrada, data_saida, quarto_id, cliente_id) VALUES ('2025-08-12', '2025-08-13',  1, 11);
INSERT INTO estadia (data_entrada, data_saida, quarto_id, cliente_id) VALUES ('2025-08-14', '2025-08-15',  2, 11);
INSERT INTO estadia (data_entrada, data_saida, quarto_id, cliente_id) VALUES ('2025-08-16', '2025-08-17',  3, 12);
INSERT INTO estadia (data_entrada, data_saida, quarto_id, cliente_id) VALUES ('2025-08-18', '2025-08-19',  4, 12);
INSERT INTO estadia (data_entrada, data_saida, quarto_id, cliente_id) VALUES ('2025-08-20', '2025-08-21',  5, 14);
INSERT INTO estadia (data_entrada, data_saida, quarto_id, cliente_id) VALUES ('2025-08-22', '2025-08-23',  6, 14);
INSERT INTO estadia (data_entrada, data_saida, quarto_id, cliente_id) VALUES ('2025-08-24', '2025-08-25',  7, 15);
INSERT INTO estadia (data_entrada, data_saida, quarto_id, cliente_id) VALUES ('2025-08-26', '2025-08-27',  8, 15);
INSERT INTO estadia (data_entrada, data_saida, quarto_id, cliente_id) VALUES ('2025-08-28', '2025-08-29',  9,  2);
INSERT INTO estadia (data_entrada, data_saida, quarto_id, cliente_id) VALUES ('2025-08-30', '2025-08-31', 10,  3);

