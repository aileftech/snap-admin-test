INSERT INTO products (id, name, description, price) VALUES (1, 'iPhone 12', 'Apple iPhone 12 with 64GB Memory', 699.99);
INSERT INTO products (id, name, description, price) VALUES (2, 'Samsung Galaxy S21', 'Samsung Galaxy S21 with 128GB Memory', 799.99);
INSERT INTO products (id, name, description, price) VALUES (3, 'Google Pixel 5', 'Google Pixel 5 with 128GB Memory', 699.00);
INSERT INTO products (id, name, description, price) VALUES (4, 'OnePlus 8', 'OnePlus 8 with 128GB Memory', 599.99);
INSERT INTO products (id, name, description, price) VALUES (5, 'LG V60 ThinQ', 'LG V60 ThinQ with 128GB Memory', 799.99);
INSERT INTO products (id, name, description, price) VALUES (6, 'Motorola Edge', 'Motorola Edge with 128GB Memory', 699.99);
INSERT INTO products (id, name, description, price) VALUES (7, 'Sony Xperia 1 II', 'Sony Xperia 1 II with 256GB Memory', 1199.99);
INSERT INTO products (id, name, description, price) VALUES (8, 'Nokia 8.3', 'Nokia 8.3 with 128GB Memory', 699.00);
INSERT INTO products (id, name, description, price) VALUES (9, 'Huawei P40 Pro', 'Huawei P40 Pro with 256GB Memory', 899.99);
INSERT INTO products (id, name, description, price) VALUES (10, 'Xiaomi Mi 10', 'Xiaomi Mi 10 with 128GB Memory', 699.99);

INSERT INTO `user` (id, username, password) VALUES (1, 'user', '{bcrypt}$2a$10$DFI.ufOoWyTu1vO4kqMTV.cLB7O.rAOVQIv/Q3Rx13uivd9mtYlTG');
INSERT INTO `user` (id, username, password) VALUES (2, 'admin', '{bcrypt}$2a$10$DFI.ufOoWyTu1vO4kqMTV.cLB7O.rAOVQIv/Q3Rx13uivd9mtYlTG');
INSERT INTO `role` (id, name) VALUES (1, 'USER'), (2, 'ADMIN');
INSERT INTO `user_roles` (roles_id, user_id) VALUES (1, 1), (2, 2);