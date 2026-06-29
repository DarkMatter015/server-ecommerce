CREATE TABLE tb_order_document (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL,
    original_name VARCHAR(255) NOT NULL,
    content_type VARCHAR(150) NOT NULL,
    size_bytes BIGINT NOT NULL,
    document_type VARCHAR(50) NOT NULL,
    object_key VARCHAR(512) NOT NULL,
    uploaded_by BIGINT,
    created_at TIMESTAMP(6) NOT NULL,
    deleted_at TIMESTAMP(6),
    CONSTRAINT fk_order_document_order FOREIGN KEY (order_id) REFERENCES tb_order(id),
    CONSTRAINT fk_order_document_user FOREIGN KEY (uploaded_by) REFERENCES tb_user(id)
);

CREATE INDEX idx_order_document_order ON tb_order_document (order_id);
CREATE INDEX idx_order_document_type ON tb_order_document (document_type);
