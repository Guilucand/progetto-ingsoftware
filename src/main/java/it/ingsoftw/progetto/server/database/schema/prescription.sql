

CREATE TABLE IF NOT EXISTS prescription (
    key SERIAL PRIMARY KEY,
    recoveryId INTEGER NOT NULL FOREIGN KEY REFERENCES recovery(idx),
    drug CHAR(9) NOT NULL FOREIGN KEY REFERENCES drug(aicCode),
    date         DATE,
    daysDuration VARCHAR(32),
    dosesPerDay  VARCHAR(64),
    quantity     VARCHAR(64),
    notes        VARCHAR(256),
    medic        NOT NULL FOREIGN KEY REFERENCES user(id)
);

CREATE TABLE IF NOT EXISTS administration (
    prescription INTEGER NOT NULL FOREIGN KEY REFERENCES prescription(key),
    datetime     TIMESTAMP,
    quantity     VARCHAR(64),
    notes        VARCHAR(256),
    nurse        NOT NULL FOREIGN KEY REFERENCES user(id),

    PRIMARY KEY(prescription, datetime)
);