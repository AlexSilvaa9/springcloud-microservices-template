DROP TABLE IF EXISTS products;

CREATE TABLE products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(1000),
    price DECIMAL(10,2) NOT NULL,
    stock INT NOT NULL,
    category VARCHAR(255),
    image_url VARCHAR(500),
    active BOOLEAN DEFAULT TRUE
);
