CREATE TABLE work_orders (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    waste_type_id BIGINT NOT NULL,
    quantity DECIMAL(12,3) NOT NULL,
    unit VARCHAR(10) NOT NULL,
    status VARCHAR(20) NOT NULL,
    pickup_address VARCHAR(255) NOT NULL,
    requested_at DATETIME NOT NULL,
    scheduled_for DATETIME,
    completed_at DATETIME,
    note VARCHAR(500),
    created_at DATETIME NOT NULL,

    CONSTRAINT fk_work_orders_user
        FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_work_orders_waste_type
        FOREIGN KEY (waste_type_id) REFERENCES waste_types(id)
);

CREATE INDEX idx_work_orders_user_id ON work_orders(user_id);
CREATE INDEX idx_work_orders_status ON work_orders(status);
CREATE INDEX idx_work_orders_requested_at ON work_orders(requested_at);
