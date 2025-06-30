DROP TABLE IF EXISTS transaction;
DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS supplier;
DROP TABLE IF EXISTS category;


CREATE TABLE category (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    active BOOLEAN DEFAULT TRUE
);

CREATE TABLE supplier (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255),
    phone VARCHAR(255),
    email VARCHAR(255),
    active BOOLEAN DEFAULT TRUE
);


CREATE TABLE product (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255),
    quantity INT,
    price DECIMAL(10, 2),
    vat DECIMAL(5, 2),
    category_id INT,
    barcode VARCHAR(255),
    active BOOLEAN DEFAULT TRUE
);

CREATE TABLE transaction (
    id INT PRIMARY KEY AUTO_INCREMENT,
    product_id INT,
    supplier_id INT,
    type VARCHAR(10),
    quantity INT,
    timestamp TIMESTAMP,
    active BOOLEAN DEFAULT TRUE
);
