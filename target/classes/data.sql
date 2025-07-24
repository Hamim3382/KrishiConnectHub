-- Preload Users (2 Farmers, 2 Buyers with Bangladeshi names)
-- IDs are omitted to allow the database to generate them automatically.
INSERT INTO users (name, email, password, role) VALUES
('Karim Ahmed', 'karim.a@example.com', 'password123', 'FARMER'),
('Fatima Begum', 'fatima.b@example.com', 'password123', 'FARMER'),
('Anis Rahman', 'anis.r@example.com', 'password123', 'BUYER'),
('Sharmin Chowdhury', 'sharmin.c@example.com', 'password123', 'BUYER');

-- Preload Products from the farmers with Bangladeshi context
-- Note: The farmer_id values (1, 2) correspond to the order of user insertions (Karim=1, Fatima=2).
-- Prices are adjusted to be more representative of BDT values.
INSERT INTO product (name, category, price, quantity, location, farmer_id) VALUES
('Hilsha Fish (Ilish Maach)', 'Fish', 1200.00, 50, 'Chandpur, Chittagong', 1),
('Jute (Golden Fibre)', 'Fibre Crop', 3000.00, 200, 'Faridpur, Dhaka', 2),
('Rajshahi Mangoes (Fazli)', 'Fruit', 800.00, 100, 'Rajshahi, Rajshahi', 1),
('Red Lentils (Masoor Dal)', 'Pulses', 130.00, 500, 'Pabna, Rajshahi', 2);

-- Preload one past Order for a buyer
-- The buyer_id=3 and product_id=2 correspond to Anis Rahman and Jute.
INSERT INTO product_order (quantity, order_date, status, buyer_id, product_id) VALUES
(10, '2025-07-24 11:30:00', 'DELIVERED', 3, 2);