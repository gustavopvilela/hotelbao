-- Roles possíveis
INSERT INTO role (authority) VALUES ('ADMIN');
INSERT INTO role (authority) VALUES ('CLIENTE');
INSERT INTO role (authority) VALUES ('USER');

-- Três usuários, um para cada role
INSERT INTO usuario (nome, email, senha, login, telefone) VALUES ('Gustavo', 'gustavohp.vilela@gmail.com', '12345678', 'gustavo', '37987654321');
INSERT INTO usuario (nome, email, senha, login, telefone) VALUES ('Marcos', 'marcos@gmail.com', '12345678', 'marcos', '37123456789');
INSERT INTO usuario (nome, email, senha, login, telefone) VALUES ('Nanda', 'nanda@gmail.com', '12345678', 'nanda', '37543216789');

INSERT INTO usuario_role (usuario_id, role_id) VALUES (1, 1);
INSERT INTO usuario_role (usuario_id, role_id) VALUES (2, 2);
INSERT INTO usuario_role (usuario_id, role_id) VALUES (3, 3);