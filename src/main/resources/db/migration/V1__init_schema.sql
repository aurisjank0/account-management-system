CREATE TABLE customer (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          name VARCHAR(255) NOT NULL
);

CREATE TABLE account (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         account_number VARCHAR(255) NOT NULL,
                         balance DOUBLE NOT NULL,
                         customer_id BIGINT,
                         CONSTRAINT fk_customer FOREIGN KEY (customer_id) REFERENCES customer(id)
);

CREATE TABLE transaction (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             amount DOUBLE NOT NULL,
                             timestamp TIMESTAMP NOT NULL,
                             type VARCHAR(50) NOT NULL,
                             account_id BIGINT,
                             CONSTRAINT fk_account FOREIGN KEY (account_id) REFERENCES account(id)
);