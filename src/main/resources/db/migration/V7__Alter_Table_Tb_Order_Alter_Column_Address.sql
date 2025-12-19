ALTER TABLE tb_order ALTER COLUMN address_street DROP NOT NULL;
ALTER TABLE tb_order ALTER COLUMN address_number DROP NOT NULL;
ALTER TABLE tb_order ALTER COLUMN address_complement DROP NOT NULL;
ALTER TABLE tb_order ALTER COLUMN address_city DROP NOT NULL;
ALTER TABLE tb_order ALTER COLUMN address_state DROP NOT NULL;