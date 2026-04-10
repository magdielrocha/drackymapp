CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255),
    provider VARCHAR(50) NOT NULL,
    provider_id VARCHAR(255),
    role VARCHAR(50) NOT NULL
);