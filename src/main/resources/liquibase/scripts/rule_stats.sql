-- liquibase formatted sql

-- changeset mkorolkov:5
CREATE TABLE rule_stats (
    rule_id UUID NOT NULL,
    count BIGINT NOT NULL DEFAULT 0,
    FOREIGN KEY (rule_id) REFERENCES dynamic_rules(id)
);