INSERT INTO customer (name) VALUES ('Test customer 1');
INSERT INTO customer (name) VALUES ('Test customer 2');

INSERT INTO account (account_number, balance, customer_id) VALUES ('1234567890', 1000.00, 1);
INSERT INTO account (account_number, balance, customer_id) VALUES ('0987654321', 2000.00, 2);

INSERT INTO transaction (amount, timestamp, type, account_id) VALUES (1000.00, '2025-01-01 00:00:00', 'DEPOSIT', 1);
INSERT INTO transaction (amount, timestamp, type, account_id) VALUES (20.00, '2025-01-02 00:00:00', 'WITHDRAW', 1);
INSERT INTO transaction (amount, timestamp, type, account_id) VALUES (1000.00, '2025-01-03 00:00:00', 'DEPOSIT', 1);
INSERT INTO transaction (amount, timestamp, type, account_id) VALUES (50.00, '2025-01-04 00:00:00', 'WITHDRAW', 1);
INSERT INTO transaction (amount, timestamp, type, account_id) VALUES (200.00, '2025-01-05 00:00:00', 'WITHDRAW', 1);
INSERT INTO transaction (amount, timestamp, type, account_id) VALUES (2000.00, '2025-01-06 00:00:00', 'DEPOSIT', 2);
INSERT INTO transaction (amount, timestamp, type, account_id) VALUES (1000.00, '2025-01-07 00:00:00', 'WITHDRAW', 2);