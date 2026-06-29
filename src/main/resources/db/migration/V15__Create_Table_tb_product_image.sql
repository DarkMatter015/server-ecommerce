CREATE TABLE tb_product_image (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL,
    original_name VARCHAR(255) NOT NULL,
    content_type VARCHAR(150) NOT NULL,
    size_bytes BIGINT NOT NULL,
    object_key VARCHAR(512) NOT NULL,
    position INTEGER NOT NULL,
    created_at TIMESTAMP(6) NOT NULL,
    deleted_at TIMESTAMP(6),
    CONSTRAINT fk_product_image_product FOREIGN KEY (product_id) REFERENCES tb_product(id)
);

CREATE INDEX idx_product_image_product ON tb_product_image (product_id);
