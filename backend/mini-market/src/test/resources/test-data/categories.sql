DELETE FROM category;
ALTER TABLE category ALTER COLUMN id RESTART WITH 1;

INSERT INTO category (name, active) VALUES ('Beverages', true);
INSERT INTO category (name, active) VALUES ('Snacks', true);
INSERT INTO category (name, active) VALUES ('Inactive Category', false);