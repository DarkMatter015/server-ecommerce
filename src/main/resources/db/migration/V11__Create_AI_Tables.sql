-- 1. Habilita a extensão Vector.
CREATE
EXTENSION IF NOT EXISTS vector;

-- 2. Tabela de Embeddings
CREATE TABLE product_embeddings
(
    id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    product_id BIGINT NOT NULL,
    embedding  vector(768),
    content    TEXT,
    metadata   JSONB,
    created_at TIMESTAMP        DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_product_embedding FOREIGN KEY (product_id) REFERENCES tb_product (id) ON DELETE CASCADE
);

-- 3. Índice HNSW para performance
CREATE INDEX product_embedding_hnsw_idx ON product_embeddings USING hnsw (embedding vector_cosine_ops);