USE bankdb;

CREATE TABLE IF NOT EXISTS investments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    account_number VARCHAR(255) NOT NULL,
    investment_type VARCHAR(255) NOT NULL,
    symbol VARCHAR(50) NOT NULL,
    amount_invested DECIMAL(15,2) NOT NULL,
    quantity INT,
    current_return DECIMAL(15,2),
    status VARCHAR(50) NOT NULL,
    purchase_date DATETIME DEFAULT CURRENT_TIMESTAMP
);
