-- Correções para tb_product
ALTER TABLE tb_product ALTER COLUMN description DROP NOT NULL;
ALTER TABLE tb_product ALTER COLUMN category_id SET NOT NULL;

-- Correções para tb_role
ALTER TABLE tb_role ALTER COLUMN name SET NOT NULL;
ALTER TABLE tb_role ADD CONSTRAINT uk_role_name UNIQUE (name);

-- Correções para tb_user
ALTER TABLE tb_user ALTER COLUMN display_name SET NOT NULL;
ALTER TABLE tb_user ALTER COLUMN email SET NOT NULL;
ALTER TABLE tb_user ALTER COLUMN password SET NOT NULL;
ALTER TABLE tb_user ALTER COLUMN cpf SET NOT NULL;
ALTER TABLE tb_user ADD CONSTRAINT uk_user_cpf UNIQUE (cpf);

-- Correções para tb_address
ALTER TABLE tb_address ALTER COLUMN street SET NOT NULL;
ALTER TABLE tb_address ALTER COLUMN number SET NOT NULL;
ALTER TABLE tb_address ALTER COLUMN neighborhood SET NOT NULL;
ALTER TABLE tb_address ALTER COLUMN city SET NOT NULL;
ALTER TABLE tb_address ALTER COLUMN state SET NOT NULL;
ALTER TABLE tb_address ALTER COLUMN cep SET NOT NULL;
ALTER TABLE tb_address ALTER COLUMN user_id SET NOT NULL;

-- Correções para tb_order_status
ALTER TABLE tb_order_status ADD CONSTRAINT uk_order_status_name UNIQUE (name);

-- Correções para tb_order
ALTER TABLE tb_order ALTER COLUMN data SET NOT NULL;
ALTER TABLE tb_order ALTER COLUMN user_id SET NOT NULL;
ALTER TABLE tb_order ALTER COLUMN payment_id SET NOT NULL;
ALTER TABLE tb_order ALTER COLUMN shipment_id SET NOT NULL;
ALTER TABLE tb_order ALTER COLUMN shipment_name SET NOT NULL;
ALTER TABLE tb_order ALTER COLUMN shipment_price SET NOT NULL;
ALTER TABLE tb_order ALTER COLUMN shipment_delivery_time SET NOT NULL;
ALTER TABLE tb_order ALTER COLUMN address_neighborhood SET NOT NULL;

-- Correções para tb_order_item
ALTER TABLE tb_order_item ALTER COLUMN order_id SET NOT NULL;
ALTER TABLE tb_order_item ALTER COLUMN product_id SET NOT NULL;
