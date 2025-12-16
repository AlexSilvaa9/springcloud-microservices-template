-- Vaciar tabla antes de insertar
DELETE FROM products;

-- Insertar productos existentes y nuevos
INSERT INTO products (name, description, price, stock, category, image_url, active) VALUES
('Laptop Dell XPS 13', 'Ultrabook potente y ligero con pantalla 13.3 pulgadas', 1299.99, 10, 'Electronics', 'https://example.com/laptop.jpg', true),
('iPhone 14 Pro', 'Smartphone premium con cámara profesional', 999.99, 15, 'Electronics', 'https://example.com/iphone.jpg', true),
('Auriculares Sony WH-1000XM4', 'Auriculares inalámbricos con cancelación de ruido', 349.99, 20, 'Electronics', 'https://example.com/headphones.jpg', true),
('Camiseta Nike Dri-FIT', 'Camiseta deportiva de alta calidad', 29.99, 50, 'Clothing', 'https://example.com/tshirt.jpg', true),
('Zapatillas Adidas Ultraboost', 'Zapatillas de running profesionales', 179.99, 30, 'Footwear', 'https://example.com/shoes.jpg', true),
('Libro "Clean Code"', 'Guía esencial para desarrolladores de software', 39.99, 25, 'Books', 'https://example.com/book.jpg', true),
('Cafetera Nespresso', 'Cafetera automática para café espresso', 199.99, 12, 'Home', 'https://example.com/coffee.jpg', true),
('Monitor Samsung 27"', 'Monitor 4K para gaming y productividad', 399.99, 8, 'Electronics', 'https://example.com/monitor.jpg', true),
('Teclado Mecánico Corsair', 'Teclado gaming con switches mecánicos', 149.99, 18, 'Electronics', 'https://example.com/keyboard.jpg', true),
('Mochila de Viaje', 'Mochila resistente para aventuras', 79.99, 35, 'Travel', 'https://example.com/backpack.jpg', true),
('Reloj Inteligente Apple Watch', 'Reloj inteligente con monitor de salud', 399.99, 20, 'Electronics', 'https://example.com/watch.jpg', true),
('Silla Ergonómica de Oficina', 'Silla cómoda para largas jornadas', 249.99, 15, 'Furniture', 'https://example.com/chair.jpg', true),
('Smart TV LG 55"', 'Televisor 4K con HDR y conectividad', 699.99, 7, 'Electronics', 'https://example.com/tv.jpg', true),
('Tablet Samsung Galaxy Tab', 'Tablet versátil para trabajo y entretenimiento', 499.99, 10, 'Electronics', 'https://example.com/tablet.jpg', true),
('Botella Reutilizable Hydro Flask', 'Botella de acero inoxidable 750ml', 39.99, 40, 'Home', 'https://example.com/bottle.jpg', true);
