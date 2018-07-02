package it.ingsoftw.progetto.server.database;

import java.sql.SQLException;
import java.util.List;

import it.ingsoftw.progetto.common.EditablePatientData;
import it.ingsoftw.progetto.common.PatientData;

public interface IPatientsDatabase {
    boolean addPatient(PatientData patient) throws SQLException;
    boolean removePatient(EditablePatientData patient) throws SQLException;

    PatientData getPatientByCode(String patientCode) throws SQLException;
    List<String> searchPatientsByCode(String patientCode) throws SQLException;

    EditablePatientData getEditablePatient(String patientCode) throws SQLException;

    boolean updatePatient(EditablePatientData updatedPatientData) throws SQLException;

    List<PatientData> getAllPatients() throws SQLException;
}
