CREATE SCHEMA IF NOT EXISTS order_management;

CREATE TYPE order_state AS ENUM ('CREATED', 'PAID', 'IN_FULFILLMENT', 'CLOSED');

CREATE TABLE order_management.items (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price NUMERIC(10, 2) NOT NULL,
    stock INT NOT NULL
);

CREATE TABLE order_management.orders (
    id SERIAL PRIMARY KEY,
    state order_state NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fulfillment_result TEXT
);

CREATE TABLE order_management.basket_items (
    order_id INT NOT NULL REFERENCES order_management.orders(id) ON DELETE CASCADE,
    item_id INT NOT NULL REFERENCES order_management.items(id),
    quantity INT NOT NULL CHECK (quantity > 0),
    PRIMARY KEY (order_id, item_id)
);

-- Inserts iniciales
INSERT INTO order_management.items (id, name, price, stock) VALUES (1, 'Item1', 10.00, 100);
INSERT INTO order_management.items (id, name, price, stock) VALUES (2, 'Item2', 20.00, 50);
