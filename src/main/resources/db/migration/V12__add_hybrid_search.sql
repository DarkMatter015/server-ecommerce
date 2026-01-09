-- 1. Adiciona a coluna de vetor de texto (tsvector)
ALTER TABLE product_embeddings
    ADD COLUMN search_vector tsvector;

-- 2. Atualiza os dados existentes
UPDATE product_embeddings
SET search_vector = to_tsvector('portuguese', content);

-- 3. Cria índice GIN (Generalized Inverted Index) para busca textual instantânea
CREATE INDEX product_embedding_search_idx ON product_embeddings USING GIN (search_vector);

-- 4. Criar Trigger para manter o vetor atualizado automaticamente
CREATE
OR REPLACE FUNCTION tsvector_update_trigger() RETURNS trigger AS $$
BEGIN
  NEW.search_vector
:= to_tsvector('portuguese', NEW.content);
RETURN NEW;
END
$$
LANGUAGE plpgsql;

CREATE TRIGGER tsvector_update
    BEFORE INSERT OR
UPDATE ON product_embeddings
    FOR EACH ROW EXECUTE FUNCTION tsvector_update_trigger();