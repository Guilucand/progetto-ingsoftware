CREATE TYPE privilege AS ENUM ('nurse', 'medic', 'primary');

CREATE TABLE IF NOT EXISTS users {
    username varchar(32) PRIMARY KEY,
    password char(64),
    name varchar(64) NOT NULL,
    surname varchar(64) NOT NULL,
    email varchar(64) NOT NULL,
}