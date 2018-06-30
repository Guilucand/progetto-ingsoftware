

CREATE TABLE IF NOT EXISTS recovery {
    patientCode VARCHAR(48) REFERENCES patient(code),
    diagnosis VARCHAR(4096),

}