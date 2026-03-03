USE bankdb;

CREATE TABLE IF NOT EXISTS emis (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    account_number VARCHAR(255) NOT NULL,
    loan_name VARCHAR(255) NOT NULL,
    total_amount DECIMAL(15,2) NOT NULL,
    emi_amount DECIMAL(15,2) NOT NULL,
    total_months INT NOT NULL,
    months_paid INT NOT NULL DEFAULT 0,
    next_due_date DATETIME NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE'
);

-- Pre-populate some dummy user data for user 1234567890 
-- (Assuming the user tests with exactly this standard mock account initially, if not they can change the account_number string securely)
INSERT INTO emis (account_number, loan_name, total_amount, emi_amount, total_months, months_paid, next_due_date, status)
VALUES 
('1234567890', 'iPhone 15 Pro Max', 150000.00, 12500.00, 12, 3, DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 5 DAY), 'ACTIVE'),
('1234567890', 'Sony Bravia 65" TV', 85000.00, 7083.33, 12, 11, DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 2 DAY), 'ACTIVE'),
('1234567890', 'MacBook Air M3', 115000.00, 9583.33, 12, 12, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 10 DAY), 'COMPLETED');
