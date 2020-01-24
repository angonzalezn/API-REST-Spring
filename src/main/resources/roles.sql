INSERT INTO roles(name) VALUES('ROLE_USER');
INSERT INTO roles(name) VALUES('ROLE_OTHER');
INSERT INTO roles(name) VALUES('ROLE_ADMIN');

INSERT INTO users(username, password, enabled) VALUES('ssotom', '$2a$04$6wuqGbD75wRiuXwRQgFVuu0e5Us2r.HAkZ3.EiXT06VNz1UCkkQb2', true);

INSERT INTO user_roles(user_id, role_id) VALUES(1, 1);