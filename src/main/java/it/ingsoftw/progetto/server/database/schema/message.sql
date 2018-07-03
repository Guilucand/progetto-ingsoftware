
CREATE TABLE IF NOT EXISTS message (
  key         SERIAL PRIMARY KEY,
  recoveryKey INTEGER NOT NULL REFERENCES recovery(key),
  dateTime    TIMESTAMP NOT NULL,
  messageText VARCHAR(1024) NOT NULL
);

CREATE INDEX IF NOT EXISTS recovery_idx ON message(recoveryKey);
CREATE INDEX IF NOT EXISTS date_idx ON message(dateTime);