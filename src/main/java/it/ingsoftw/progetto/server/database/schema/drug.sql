

CREATE TABLE IF NOT EXISTS drug (
    company                 VARCHAR(128),
    commercialName          VARCHAR(128),
    packageDescription      VARCHAR(128),
    atcCode                 CHAR(7),
    activePrinciple         VARCHAR(128),
    aicCode                 CHAR(9) PRIMARY KEY
)