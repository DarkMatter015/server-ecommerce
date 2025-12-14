-- Roles
INSERT INTO tb_role (id, name) VALUES (1, 'ADMIN'), (2, 'USER');

-- Users (senha para ambos é '123456')
INSERT INTO tb_user (id, display_name, email, password, cpf) VALUES (1, 'Admin Test User', 'admin@teste.com', '$2a$10$LOqePml/koRGsk2YAIOFI.1YNKZg7EsQ5BAIuYP1nWOyYRl21dlne', '00000000000');
INSERT INTO tb_user (id, display_name, email, password, cpf) VALUES (2, 'Common Test User', 'user@teste.com', '$2a$10$LOqePml/koRGsk2YAIOFI.1YNKZg7EsQ5BAIuYP1nWOyYRl21dlne', '11111111111');

-- User_Roles
INSERT INTO tb_user_roles (user_id, role_id) VALUES (1, 1); -- Admin User tem Role ADMIN
INSERT INTO tb_user_roles (user_id, role_id) VALUES (2, 2); -- Common User tem Role USER

-- Outros dados essenciais
INSERT INTO tb_category (id, name) VALUES (1, 'Guitarras');
INSERT INTO tb_payment (id, name) VALUES (1, 'PIX');
INSERT INTO tb_product (id, name, price, quantity_available_in_stock, category_id) VALUES (1, 'Fender Stratocaster', 8990.00, 10, 1);
INSERT INTO tb_address (id, user_id, street, number, city, state, neighborhood, cep) VALUES (1, 2, 'Rua dos Testes', '123', 'Test City', 'TS', 'Test Neighborhood', '85501000');
INSERT INTO tb_order_status (id, name) VALUES (1, 'PENDENTE');

-- ==================================================================================
-- REINICIA AS SEQUENCES DO H2 PARA EVITAR CONFLITOS DE CHAVE PRIMÁRIA
-- Altera o próximo valor a ser usado pelo auto-incremento para um número seguro (ex: 100)
-- ==================================================================================
ALTER TABLE tb_role ALTER COLUMN id RESTART WITH 100;
ALTER TABLE tb_user ALTER COLUMN id RESTART WITH 100;
ALTER TABLE tb_category ALTER COLUMN id RESTART WITH 100;
ALTER TABLE tb_payment ALTER COLUMN id RESTART WITH 100;
ALTER TABLE tb_product ALTER COLUMN id RESTART WITH 100;
ALTER TABLE tb_address ALTER COLUMN id RESTART WITH 100;
ALTER TABLE tb_order_status ALTER COLUMN id RESTART WITH 100;
ALTER TABLE tb_order ALTER COLUMN id RESTART WITH 100;
ALTER TABLE tb_order_item ALTER COLUMN id RESTART WITH 100;
