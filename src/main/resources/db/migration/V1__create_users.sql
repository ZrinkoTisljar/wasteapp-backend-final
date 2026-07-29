CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    user_type VARCHAR(20) NOT NULL,
    full_name VARCHAR(255),
    company_name VARCHAR(255),
    oib VARCHAR(32),
    address VARCHAR(255) NOT NULL,
    phone VARCHAR(64),
    is_approved TINYINT(1) NOT NULL DEFAULT 0,
    created_at DATETIME NOT NULL
);