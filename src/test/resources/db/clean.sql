-- Desativa verificação de chave estrangeira para permitir TRUNCATE em qualquer ordem
SET REFERENTIAL_INTEGRITY FALSE;

TRUNCATE TABLE tb_order_item;
TRUNCATE TABLE tb_order;
TRUNCATE TABLE tb_product;
TRUNCATE TABLE tb_category;
TRUNCATE TABLE tb_address;
TRUNCATE TABLE tb_payment;
TRUNCATE TABLE tb_user_roles;
TRUNCATE TABLE tb_user;
TRUNCATE TABLE tb_role;
TRUNCATE TABLE tb_order_status;

-- Reativa a verificação
SET REFERENTIAL_INTEGRITY TRUE;
