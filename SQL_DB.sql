CREATE TABLE Franchise (
    franchise_id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE Branch (
    branch_id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    franchise_id INT REFERENCES Franchise(franchise_id)
);

CREATE TABLE Product (
    product_id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE Branch_Product (
    branch_id INT REFERENCES Branch(branch_id),
    product_id INT REFERENCES Product(product_id),
    stock INT NOT NULL,
    PRIMARY KEY (branch_id, product_id)
);
