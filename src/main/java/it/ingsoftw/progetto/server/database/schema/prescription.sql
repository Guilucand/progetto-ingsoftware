

CREATE TABLE IF NOT EXISTS prescription (
    key SERIAL PRIMARY KEY,
    recoveryKey INTEGER NOT NULL REFERENCES recovery(key),
    drug CHAR(9) NOT NULL REFERENCES drug(aicCode),
    date         DATE,
    daysDuration VARCHAR(32),
    dosesPerDay  VARCHAR(64),
    quantity     VARCHAR(64),
    notes        VARCHAR(256),
    medic        VARCHAR(32) NOT NULL REFERENCES users(username)
);

CREATE TABLE IF NOT EXISTS administration (
    prescription INTEGER NOT NULL REFERENCES prescription(key),
    datetime     TIMESTAMP NOT NULL,
    quantity     VARCHAR(64) NOT NULL,
    notes        VARCHAR(256),
    nurse        VARCHAR(32) NOT NULL REFERENCES users(username),

    PRIMARY KEY(prescription, datetime)
);