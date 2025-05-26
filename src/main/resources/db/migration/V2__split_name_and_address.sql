-- === 1. Добавляем новые поля ===

ALTER TABLE customers ADD COLUMN first_name VARCHAR(100);
ALTER TABLE customers ADD COLUMN middle_name VARCHAR(100);
ALTER TABLE customers ADD COLUMN last_name VARCHAR(100);

ALTER TABLE customers ADD COLUMN country VARCHAR(100);
ALTER TABLE customers ADD COLUMN state VARCHAR(100);
ALTER TABLE customers ADD COLUMN city VARCHAR(100);
ALTER TABLE customers ADD COLUMN street VARCHAR(100);
ALTER TABLE customers ADD COLUMN house_number VARCHAR(20);
ALTER TABLE customers ADD COLUMN apartment VARCHAR(20);
ALTER TABLE customers ADD COLUMN postal_code VARCHAR(20);
ALTER TABLE customers ADD COLUMN full_address VARCHAR(255);

-- === 2. Копируем данные из name и address в новые поля ===
UPDATE customers
SET
    first_name = split_part(name, ' ', 1),
    middle_name = CASE
        WHEN array_length(string_to_array(name, ' '), 1) = 3 THEN split_part(name, ' ', 2)
        ELSE NULL
    END,
    last_name = CASE
        WHEN array_length(string_to_array(name, ' '), 1) = 3 THEN split_part(name, ' ', 3)
        ELSE split_part(name, ' ', 2)
    END;

-- Разбор address:
UPDATE customers
SET
    country = split_part(address, ',', 1),
    state = trim(split_part(address, ',', 2)),
    city = trim(split_part(address, ',', 3)),
    street = trim(split_part(address, ',', 4)),
    house_number = trim(split_part(address, ',', 5)),
    apartment = trim(split_part(address, ',', 6)),
    postal_code = trim(split_part(address, ',', 7));

-- === 3. Собираем full_address из новых полей ===
UPDATE customers
SET full_address = 
    concat_ws(', ',
        country,
        state,
        city,
        concat_ws(' ', street, house_number),
        apartment,
        postal_code
    );

-- === 4. Удаляем старые поля ===
ALTER TABLE customers DROP COLUMN name;
ALTER TABLE customers DROP COLUMN address;