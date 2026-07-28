CREATE TABLE waste_manifests (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    manifest_number VARCHAR(50) NOT NULL UNIQUE,
    work_order_id BIGINT NOT NULL UNIQUE,
    issued_at DATETIME NOT NULL,
    note VARCHAR(500),
    created_at DATETIME NOT NULL,

    CONSTRAINT fk_waste_manifests_work_order
        FOREIGN KEY (work_order_id) REFERENCES work_orders(id)
);

CREATE INDEX idx_waste_manifests_issued_at ON waste_manifests(issued_at);
