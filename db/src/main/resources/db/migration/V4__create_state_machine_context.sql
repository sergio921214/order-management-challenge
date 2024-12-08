CREATE TABLE state_machine_context (
    machine_id VARCHAR(255) PRIMARY KEY,
    state VARCHAR(255) NOT NULL,
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
