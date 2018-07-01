package it.ingsoftw.progetto.server.database;

import java.sql.SQLException;
import java.util.List;

import it.ingsoftw.progetto.common.EditablePatientData;
import it.ingsoftw.progetto.common.PatientData;

public interface IPatientsDatabase {
    boolean addPatient(PatientData patient) throws SQLException;
    boolean removePatient(EditablePatientData patient) throws SQLException;

    PatientData getPatientById(String patientId) throws SQLException;
    List<String> searchPatientsById(String patientId) throws SQLException;

    EditablePatientData getEditablePatient(String patientId) throws SQLException;

    boolean updatePatient(EditablePatientData updatedPatientData) throws SQLException;

    List<PatientData> getAllPatients() throws SQLException;
}
