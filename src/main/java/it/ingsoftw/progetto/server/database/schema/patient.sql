
CREATE TABLE IF NOT EXISTS patient (
    code VARCHAR(48) PRIMARY KEY,
    name VARCHAR(64),
    surname VARCHAR(64),
    birthdate DATE,
    birthplace VARCHAR(64)
);