ALTER TABLE tb_order ALTER COLUMN shipment_name DROP NOT NULL;
ALTER TABLE tb_order ALTER COLUMN shipment_price DROP NOT NULL;
ALTER TABLE tb_order ALTER COLUMN shipment_delivery_time DROP NOT NULL;
ALTER TABLE tb_order ALTER COLUMN address_neighborhood DROP NOT NULL;