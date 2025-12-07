CREATE TABLE tb_category (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE tb_payment (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE tb_product (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    price NUMERIC(38,2) NOT NULL,
    url_image VARCHAR(255),
    quantity_available_in_stock INTEGER NOT NULL,
    category_id BIGINT,
    CONSTRAINT fk_product_category FOREIGN KEY (category_id) REFERENCES tb_category(id)
);

CREATE TABLE tb_role (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255)
);

CREATE TABLE tb_user (
    id BIGSERIAL PRIMARY KEY,
    display_name VARCHAR(255),
    email VARCHAR(255) UNIQUE,
    password VARCHAR(255),
    cpf VARCHAR(255)
);

CREATE TABLE tb_user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id) REFERENCES tb_user(id),
    CONSTRAINT fk_user_roles_role FOREIGN KEY (role_id) REFERENCES tb_role(id)
);

CREATE TABLE tb_address (
    id BIGSERIAL PRIMARY KEY,
    street VARCHAR(255),
    number VARCHAR(255),
    complement VARCHAR(255),
    neighborhood VARCHAR(255),
    city VARCHAR(255),
    state VARCHAR(255),
    cep VARCHAR(255),
    user_id BIGINT,
    CONSTRAINT fk_address_user FOREIGN KEY (user_id) REFERENCES tb_user(id)
);

CREATE TABLE tb_order_status (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE tb_order (
    id BIGSERIAL PRIMARY KEY,
    data TIMESTAMP(6),
    user_id BIGINT,
    payment_id BIGINT,
    status_id BIGINT,
    shipment_id BIGINT,
    shipment_name VARCHAR(255),
    shipment_price NUMERIC(38,2),
    shipment_custom_price NUMERIC(38,2),
    shipment_discount NUMERIC(38,2),
    shipment_currency VARCHAR(255),
    shipment_delivery_time INTEGER,
    company_name TEXT,
    company_picture TEXT,
    address_street VARCHAR(255) NOT NULL,
    address_number VARCHAR(255) NOT NULL,
    address_complement VARCHAR(255),
    address_neighborhood VARCHAR(255),
    address_city VARCHAR(255) NOT NULL,
    address_state VARCHAR(255) NOT NULL,
    address_cep VARCHAR(8) NOT NULL,
    CONSTRAINT fk_order_user FOREIGN KEY (user_id) REFERENCES tb_user(id),
    CONSTRAINT fk_order_payment FOREIGN KEY (payment_id) REFERENCES tb_payment(id),
    CONSTRAINT fk_order_status FOREIGN KEY (status_id) REFERENCES tb_order_status(id)
);

CREATE TABLE tb_order_item (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT,
    product_id BIGINT,
    quantity INTEGER NOT NULL,
    total_price NUMERIC(38,2) NOT NULL,
    CONSTRAINT fk_order_item_order FOREIGN KEY (order_id) REFERENCES tb_order(id),
    CONSTRAINT fk_order_item_product FOREIGN KEY (product_id) REFERENCES tb_product(id)
);
