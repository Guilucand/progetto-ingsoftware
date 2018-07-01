
CREATE TABLE IF NOT EXISTS vsmachine (
    identifier VARCHAR(32) PRIMARY KEY
);


CREATE TABLE IF NOT EXISTS room (
    number VARCHAR(32) PRIMARY KEY,
    machine VARCHAR(32) UNIQUE REFERENCES vsmachine(identifier)
);


CREATE TABLE IF NOT EXISTS recovery (
    key SERIAL PRIMARY KEY,
    patientCode VARCHAR(48) NOT NULL REFERENCES patient(code),
    roomId VARCHAR(32) UNIQUE REFERENCES room(number),

    diagnosis VARCHAR(4096),
    dimissionLetter VARCHAR(4096)
);

CREATE UNIQUE INDEX IF NOT EXISTS patientsUnique ON recovery(patientCode) WHERE (roomId IS NOT NULL);
