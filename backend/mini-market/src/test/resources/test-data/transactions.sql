INSERT INTO category (id, name, active) VALUES (1, 'Default', true);
INSERT INTO supplier (id, name, phone, email, active) VALUES (1, 'Default', '', '', true);
INSERT INTO product (id, name, quantity, price, vat, category_id, barcode, active)
VALUES (1, 'Item', 100, 5.00, 24.00, 1,  'ITEM01', true);
INSERT INTO transaction ( product_id, supplier_id, type, quantity, timestamp, active)
VALUES ( 1, 1, 'IN', 10, CURRENT_TIMESTAMP, true);
