USE bankdb;

CREATE TABLE IF NOT EXISTS fixed_deposits (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    account_number VARCHAR(255) NOT NULL,
    principal_amount DECIMAL(15,2) NOT NULL,
    interest_rate DECIMAL(5,2) NOT NULL,
    tenure_months INT NOT NULL,
    maturity_amount DECIMAL(15,2) NOT NULL,
    status VARCHAR(50) NOT NULL,
    start_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    maturity_date DATETIME
);
