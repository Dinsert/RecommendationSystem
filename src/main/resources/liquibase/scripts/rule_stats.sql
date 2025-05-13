-- liquibase formatted sql

-- changeset mkorolkov:5
CREATE TABLE rule_stats (
    rule_id UUID PRIMARY KEY,
    count BIGINT NOT NULL DEFAULT 0
);