-- Adiciona a coluna 'active' a todas as tabelas que representam entidades
-- O valor padrão 'true' garante que os registros existentes não sejam afetados

ALTER TABLE tb_category ADD COLUMN active BOOLEAN NOT NULL DEFAULT true;
ALTER TABLE tb_payment ADD COLUMN active BOOLEAN NOT NULL DEFAULT true;
ALTER TABLE tb_product ADD COLUMN active BOOLEAN NOT NULL DEFAULT true;
ALTER TABLE tb_role ADD COLUMN active BOOLEAN NOT NULL DEFAULT true;
ALTER TABLE tb_user ADD COLUMN active BOOLEAN NOT NULL DEFAULT true;
ALTER TABLE tb_address ADD COLUMN active BOOLEAN NOT NULL DEFAULT true;
ALTER TABLE tb_order_status ADD COLUMN active BOOLEAN NOT NULL DEFAULT true;
ALTER TABLE tb_order ADD COLUMN active BOOLEAN NOT NULL DEFAULT true;
ALTER TABLE tb_order_item ADD COLUMN active BOOLEAN NOT NULL DEFAULT true;