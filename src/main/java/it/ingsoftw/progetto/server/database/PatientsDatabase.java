package it.ingsoftw.progetto.server.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.ingsoftw.progetto.common.EditablePatientData;
import it.ingsoftw.progetto.common.EditableUser;
import it.ingsoftw.progetto.common.PatientData;
import it.ingsoftw.progetto.common.User;

public class PatientsDatabase implements IPatientsDatabase {

    private Connection connection;

    public PatientsDatabase(Connection connection) {
        this.connection = connection;
        DatabaseUtils.createDatabaseFromSchema(connection, "schema/patient.sql");
    }

    @Override
    public boolean addPatient(PatientData patient) throws SQLException {
        String sql =
                "INSERT into patient (code, name, surname, birthdate, birthplace) " +
                        "VALUES (?, ?, ?, ?, ?);";

        PreparedStatement addPatient = connection.prepareStatement(sql);
        addPatient.setString(1, patient.getCode());
        addPatient.setString(2, patient.getName());
        addPatient.setString(3, patient.getSurname());
        addPatient.setString(4, patient.getBirthPlace());
        addPatient.setDate(5, patient.getBirthDate());

        try {
            return addPatient.executeUpdate() > 0;
        }
        catch (SQLException e) {
            String s = e.getSQLState();

            // Violazione di vincoli di integrita'
            if (Integer.parseInt(s)/1000 == 23) {
                return false;
            }
            throw e;
        }
    }

    @Override
    public boolean removePatient(EditablePatientData patient) throws SQLException {
        String sql =
                "DELETE FROM patient " +
                        "WHERE " +
                        "code = ?" +
                        ";";

        PreparedStatement deleteUser = connection.prepareStatement(sql);
        deleteUser.setString(1, patient.getCode());

        return deleteUser.executeUpdate() > 0;
    }

    private PatientData getPatientDataFromResultSet(ResultSet result) throws SQLException {

        if (!result.next())
            return null;

        return new PatientData(
                result.getString(1),
                result.getString(2),
                result.getString(3),
                result.getDate(4),
                result.getString(5));
    }


    @Override
    public PatientData getPatientById(String patientId) throws SQLException {
        String sql =
                "SELECT code, name, surname, birthdate, birthplace FROM patient " +
                        "WHERE " +
                        "code = ?" +
                        ";";

        PreparedStatement queryUser = connection.prepareStatement(sql);
        queryUser.setString(1, patientId);
        ResultSet result = queryUser.executeQuery();

        return getPatientDataFromResultSet(result);
    }

    @Override
    public EditablePatientData getEditablePatient(String patientId) throws SQLException {
        PatientData patientData = getPatientById(patientId);
        if (patientData == null) return null;

        return new EditablePatientData(patientData, patientId);
    }

    @Override
    public boolean updatePatient(EditablePatientData updatedPatientData) throws SQLException {
        String sql =
                "UPDATE patient SET (code, name, surname, birthdate, birthplace)" +
                        "= (?, ?, ?, ?, ?) " +
                        "WHERE code = ?";

        PreparedStatement addPatient = connection.prepareStatement(sql);
        addPatient.setString(1, updatedPatientData.getCode());
        addPatient.setString(2, updatedPatientData.getName());
        addPatient.setString(3, updatedPatientData.getSurname());
        addPatient.setString(4, updatedPatientData.getBirthPlace());
        addPatient.setDate(5, updatedPatientData.getBirthDate());

        // Imposto la chiave primaria del paziente prima della modifica
        addPatient.setString(6, updatedPatientData.getInternalReference());

        try {
            return addPatient.executeUpdate() > 0;
        }
        catch (SQLException e) {
            String s = e.getSQLState();

            // Violazione di vincoli di integrita'
            if (Integer.parseInt(s)/1000 == 23) {
                return false;
            }
            throw e;
        }
    }


    @Override
    public List<PatientData> getAllPatients() throws SQLException {
        String sql =
                "SELECT code, name, surname, birthdate, birthplace FROM patient" +
                        ";";

        PreparedStatement queryPatients = connection.prepareStatement(sql);
        ResultSet result = queryPatients.executeQuery();

        ArrayList<PatientData> patients = new ArrayList<>();

        PatientData current;
        while ((current = getPatientDataFromResultSet(result)) != null) {
            patients.add(current);
        }

        return patients;
    }
}
