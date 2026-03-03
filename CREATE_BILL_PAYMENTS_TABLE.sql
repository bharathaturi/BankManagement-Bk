USE bankdb;

CREATE TABLE IF NOT EXISTS bill_payments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    account_number VARCHAR(255) NOT NULL,
    biller_category VARCHAR(255) NOT NULL,
    biller_name VARCHAR(255) NOT NULL,
    consumer_number VARCHAR(255) NOT NULL,
    amount DECIMAL(15,2) NOT NULL,
    status VARCHAR(50) NOT NULL,
    payment_date DATETIME DEFAULT CURRENT_TIMESTAMP
);
