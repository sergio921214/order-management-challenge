CREATE TYPE order_state AS ENUM ('CREATED', 'PAID', 'IN_FULFILLMENT', 'CLOSED');

CREATE TABLE orders (
    id SERIAL PRIMARY KEY,
    state order_state NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fulfillment_result TEXT
);
