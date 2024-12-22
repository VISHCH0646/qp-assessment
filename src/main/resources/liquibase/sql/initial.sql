CREATE TABLE grocery_items (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price numeric NOT NULL,
    description TEXT,
    stock_quantity INT DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE orders (
    order_id SERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total_amount numeric NOT NULL,
    status VARCHAR(50) DEFAULT 'pending',
    shipping_address TEXT
);

CREATE TABLE order_items (
    order_item_id SERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL,
    grocery_item_id BIGINT NOT NULL,
    quantity INT DEFAULT 1,
    price numeric NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE,
    FOREIGN KEY (grocery_item_id) REFERENCES grocery_items(id) ON DELETE CASCADE
);

INSERT INTO grocery_items (name, price, description, stock_quantity)
VALUES
('Apple', 1.50, 'Fresh red apples, great for snacks or pies', 100),
('Banana', 0.80, 'Ripe yellow bananas, perfect for smoothies', 150),
('Carrot', 0.60, 'Organic carrots, crunchy and healthy', 200),
('Broccoli', 1.20, 'Fresh broccoli, packed with vitamins', 80),
('Milk', 2.50, 'Full-fat milk, 1 liter', 120),
('Eggs', 3.00, 'Fresh eggs, dozen pack', 180),
('Cheese', 4.50, 'Cheddar cheese, 500 grams', 90),
('Bread', 1.80, 'Whole wheat bread, soft and fresh', 150),
('Rice', 2.20, 'Basmati rice, 1 kg', 60),
('Tomato', 1.00, 'Fresh tomatoes, great for salads and sauces', 200);