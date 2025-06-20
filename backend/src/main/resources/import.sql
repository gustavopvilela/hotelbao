-- Roles possíveis
INSERT INTO role (authority) VALUES ('ROLE_ADMIN');
INSERT INTO role (authority) VALUES ('ROLE_CLIENTE');

-- Três usuários iniciais e um extra
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
