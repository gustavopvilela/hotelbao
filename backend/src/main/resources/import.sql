-- Roles possíveis
INSERT INTO role (authority) VALUES ('ROLE_ADMIN');
INSERT INTO role (authority) VALUES ('ROLE_CLIENTE');
INSERT INTO role (authority) VALUES ('ROLE_USER');

-- Três usuários, um para cada role
INSERT INTO usuario (nome, email, senha, login, telefone) VALUES ('Gustavo', 'gustavohp.vilela@gmail.com', '$2a$10$ZhadxzdYzVeN23JXcwr4yuyMa3d1UgFd3eDrjRZj4Aq8M3u1fAsKK', 'gustavo', '37987654321');
INSERT INTO usuario (nome, email, senha, login, telefone) VALUES ('Marcos', 'marcos@gmail.com', '$2a$10$ZhadxzdYzVeN23JXcwr4yuyMa3d1UgFd3eDrjRZj4Aq8M3u1fAsKK', 'marcos', '37123456789');
INSERT INTO usuario (nome, email, senha, login, telefone) VALUES ('Nanda', 'nanda@gmail.com', '$2a$10$ZhadxzdYzVeN23JXcwr4yuyMa3d1UgFd3eDrjRZj4Aq8M3u1fAsKK', 'nanda', '37543216789');
INSERT INTO usuario (nome, email, senha, login, telefone) VALUES ('jj', 'jj@jj.com', '$2a$10$2BzEm2Kl.u.2c3IfnRLNxOBRdFGT75juw.wN921PraGf0iKsyK/O.', 'jj', '99999999999');

INSERT INTO usuario_role (usuario_id, role_id) VALUES (1, 1);
INSERT INTO usuario_role (usuario_id, role_id) VALUES (2, 2);
INSERT INTO usuario_role (usuario_id, role_id) VALUES (3, 3);
INSERT INTO usuario_role (usuario_id, role_id) VALUES (4, 1);