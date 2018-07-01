

CREATE TABLE IF NOT EXISTS prescription (
    recoveryId INTEGER FOREIGN KEY REFERENCES recovery(idx),
    drug CHAR(9) FOREIGN KEY REFERENCES drug(aicCode),
    dosesPerDay VARCHAR(64),
    quantity    VARCHAR(64),
    notes       VARCHAR(256)
);