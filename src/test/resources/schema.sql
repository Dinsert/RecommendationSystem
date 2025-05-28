CREATE TABLE users (
    id UUID PRIMARY KEY
);

CREATE TABLE products (
    id UUID PRIMARY KEY,
    type VARCHAR(255) NOT NULL
);

CREATE TABLE transactions (
    id UUID PRIMARY KEY,
    product_id UUID NOT NULL,
    user_id UUID NOT NULL,
    type VARCHAR(255) NOT NULL,
    amount INTEGER NOT NULL,
    FOREIGN KEY (product_id) REFERENCES products(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE dynamic_rules (
    id UUID PRIMARY KEY,
    product_id UUID NOT NULL,
    product_name VARCHAR(255) NOT NULL,
    product_text TEXT NOT NULL
);

CREATE TABLE rule_queries (
    id UUID PRIMARY KEY,
    rule_id UUID NOT NULL,
    query_type VARCHAR(255) NOT NULL,
    negate BOOLEAN NOT NULL,
    FOREIGN KEY (rule_id) REFERENCES dynamic_rules(id)
);

CREATE TABLE rule_query_arguments (
    query_id UUID NOT NULL,
    argument VARCHAR(255),
    FOREIGN KEY (query_id) REFERENCES rule_queries(id)
);

CREATE TABLE rule_stats (
    rule_id UUID NOT NULL,
    count BIGINT NOT NULL DEFAULT 0,
    FOREIGN KEY (rule_id) REFERENCES dynamic_rules(id)
);