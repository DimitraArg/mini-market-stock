INSERT INTO category (id, name, active) VALUES (1, 'Default', true);
-- INSERT INTO supplier (id, name, phone, email, active) VALUES (1, 'Default Supplier', '', '', true);
INSERT INTO product ( name, quantity, price, vat, category_id, barcode, active)
VALUES ( 'Milk', 20, 1.00, 13.00, 1,  'MILK01', true),
       ( 'Juice', 15, 1.50, 13.00, 1, 'JUICE01', true);
