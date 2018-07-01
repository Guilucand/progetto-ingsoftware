

CREATE TABLE IF NOT EXISTS prescription (
    index SERIAL PRIMARY KEY,
    recoveryId INTEGER NOT NULL FOREIGN KEY REFERENCES recovery(idx),
    drug CHAR(9) NOT NULL FOREIGN KEY REFERENCES drug(aicCode),
    dosesPerDay VARCHAR(64),
    quantity    VARCHAR(64),
    notes       VARCHAR(256)
);