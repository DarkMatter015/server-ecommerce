CREATE TABLE chat_sessions
(
    id         UUID PRIMARY KEY,
    user_id    BIGINT      NOT NULL,
    title      VARCHAR(255),
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE chat_messages
(
    id         BIGSERIAL PRIMARY KEY,
    session_id UUID        NOT NULL,
    role       VARCHAR(20) NOT NULL,
    content    TEXT        NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_chat_session
        FOREIGN KEY (session_id)
            REFERENCES chat_sessions (id)
            ON DELETE CASCADE
);

CREATE INDEX idx_chat_messages_session ON chat_messages (session_id);
CREATE INDEX idx_chat_sessions_user ON chat_sessions (user_id);