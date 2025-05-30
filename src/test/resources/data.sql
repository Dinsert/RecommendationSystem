INSERT INTO users (id) VALUES
--Первый пользователь
('9d2df4a9-0085-4838-b8af-d8b46659cb62'),
--Второй пользователь
('e809075f-1752-411a-8e0c-de3bae23e1b9');

INSERT INTO products (id, type) VALUES
--Продукты первого пользователя
('8e153f10-e667-4fb0-b08b-45c40de9637e', 'DEBIT'),
('fd8c7c64-12d0-44c3-916c-692dc8c1e624', 'SAVING'),
--Продукты второго пользователя
('08fe0bfa-8375-4789-98d9-e43e3a9dbecd', 'INVEST'),
('26dcd78c-a1ad-48da-b9d0-9f4937a0e9d3', 'SAVING'),
('f7e0c81d-17c7-47ca-8e26-f8c694052d94', 'CREDIT');

INSERT INTO transactions (id, product_id, user_id, type, amount) VALUES
--Транзакции первого пользователя
('64ee8a34-2f16-4154-aba9-ee23706dbda4', '8e153f10-e667-4fb0-b08b-45c40de9637e', '9d2df4a9-0085-4838-b8af-d8b46659cb62', 'DEPOSIT', 100002),
('78d7303b-0bda-4666-bbd5-9d65ae36c142', '8e153f10-e667-4fb0-b08b-45c40de9637e', '9d2df4a9-0085-4838-b8af-d8b46659cb62', 'WITHDRAW', 100001),
('32b1b558-6df8-4e85-8337-cb1791646c97', 'fd8c7c64-12d0-44c3-916c-692dc8c1e624', '9d2df4a9-0085-4838-b8af-d8b46659cb62', 'DEPOSIT', 50000),
--Транзакции второго пользователя
('bc88661e-a230-4f02-af06-b780782c7ee9', '08fe0bfa-8375-4789-98d9-e43e3a9dbecd', 'e809075f-1752-411a-8e0c-de3bae23e1b9', 'DEPOSIT', 1),
('ad175a68-9556-4881-bf42-f1a912661c82', '26dcd78c-a1ad-48da-b9d0-9f4937a0e9d3', 'e809075f-1752-411a-8e0c-de3bae23e1b9', 'DEPOSIT', 1000),
('1cabe140-ba74-4baf-9746-e5d902e969ea', 'f7e0c81d-17c7-47ca-8e26-f8c694052d94', 'e809075f-1752-411a-8e0c-de3bae23e1b9', 'DEPOSIT', 1);

INSERT INTO dynamic_rules (id, product_id, product_name, product_text) VALUES
('d8328cd4-cab8-4ed6-908b-4844023d46b3', '393c337a-8af4-4432-b26a-ddaa9e856257', 'Test product', 'Test description'),
('cd77bfb1-6180-4565-afd3-1f45762c5121', '06ade82b-f568-4e49-8dad-f1ae012e85c4', 'Delete', 'Delete description');

INSERT INTO rule_queries (id, rule_id, query_type, negate) VALUES
('93652d2d-c9a8-432b-8f17-8401e6da3fdc', 'd8328cd4-cab8-4ed6-908b-4844023d46b3', 'USER_OF', FALSE),
('6c1485a4-1d95-4cd3-9fb2-1428da0a01de', 'cd77bfb1-6180-4565-afd3-1f45762c5121', 'ACTIVE_USER_OF', FALSE);

INSERT INTO rule_query_arguments (query_id, argument) VALUES
('93652d2d-c9a8-432b-8f17-8401e6da3fdc', 'DEBIT'),
('6c1485a4-1d95-4cd3-9fb2-1428da0a01de', 'CREDIT');

