-- liquibase formatted sql

-- changeset vkozhevatov:2
CREATE TABLE dynamic_rules (
    id UUID PRIMARY KEY,
    product_name VARCHAR(255) NOT NULL,
    product_id UUID NOT NULL,
    product_text TEXT NOT NULL
);
