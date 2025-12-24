CREATE TABLE tb_alert_product (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    user_id BIGINT,
    product_id BIGINT NOT NULL,
    request_date TIMESTAMP(6) NOT NULL,
    processed_at TIMESTAMP(6),
    status VARCHAR(255) NOT NULL,
    CONSTRAINT fk_alert_product_available_user FOREIGN KEY (user_id) REFERENCES tb_user(id),
    CONSTRAINT fk_alert_product_available_product FOREIGN KEY (product_id) REFERENCES tb_product(id)
);

CREATE INDEX idx_id_produto ON tb_alert_product (product_id);
CREATE INDEX idx_email ON tb_alert_product (email);
