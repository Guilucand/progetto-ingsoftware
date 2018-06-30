package it.ingsoftw.progetto.server.database;

import java.sql.SQLException;

import it.ingsoftw.progetto.common.Drug;

public interface IDrugsDatabase {
    boolean addDrug(Drug drug) throws SQLException;
    boolean deleteDrug(Drug drug) throws SQLException;
    Drug getDrug(String aicCode) throws SQLException;
}
