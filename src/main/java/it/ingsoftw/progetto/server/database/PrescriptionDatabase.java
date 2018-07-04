package it.ingsoftw.progetto.server.database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.ingsoftw.progetto.common.DrugAdministration;
import it.ingsoftw.progetto.common.DrugPrescription;
import it.ingsoftw.progetto.common.PatientData;
import it.ingsoftw.progetto.common.User;
import it.ingsoftw.progetto.common.messages.AddedAdministrationMessage;
import it.ingsoftw.progetto.common.messages.AddedPrescriptionMessage;

public class PrescriptionDatabase implements IPrescriptionDatabase {

    private final Connection connection;
    private IDrugsDatabase drugsDatabase;
    private IUsersDatabase usersDatabase;
    private IRecoveryDatabase recoveryDatabase;
    private IMessageDatabase messageDatabase;

    public PrescriptionDatabase(Connection connection,
                                IDrugsDatabase drugsDatabase,
                                IUsersDatabase usersDatabase,
                                IRecoveryDatabase recoveryDatabase,
                                IMessageDatabase messageDatabase) {

        this.connection = connection;
        this.drugsDatabase = drugsDatabase;
        this.usersDatabase = usersDatabase;
        this.recoveryDatabase = recoveryDatabase;
        this.messageDatabase = messageDatabase;
        DatabaseUtils.createDatabaseFromSchema(connection, "schema/prescription.sql");
    }

    @Override
    public boolean addPrescription(int recoveryKey, User loggedUser, DrugPrescription prescription) {
        String sql = "INSERT INTO prescription (recoveryKey, drug, date, daysDuration, dosesPerDay, quantity, notes, medic) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?);";

        try {
            drugsDatabase.addDrug(prescription.drug);
            PreparedStatement addPrescription = connection.prepareStatement(sql);
            addPrescription.setInt(1, Integer.valueOf(recoveryKey));
            addPrescription.setString(2, prescription.drug.aic);
            addPrescription.setDate(3, Date.valueOf(prescription.prescriptionDate));

            addPrescription.setString(4, prescription.durationDays);
            addPrescription.setString(5, prescription.dailyDoses);
            addPrescription.setString(6, prescription.qtyPerDose);
            addPrescription.setString(7, prescription.notes);
            addPrescription.setString(8, loggedUser.getId());
            if (addPrescription.executeUpdate() > 0) {
                messageDatabase.addVolatileMessage(new AddedPrescriptionMessage(recoveryKey,
                        recoveryDatabase.mapRecoveryToRoom(recoveryKey),
                        prescription
                ));
                return true;
            }

        } catch (Exception e) {
        }
        return false;
    }

    @Override
    public boolean addAdministration(int recoveryKey, User loggedUser, DrugAdministration administration) {
        String sql = "INSERT INTO prescription (prescription, datetime, quantity, notes, nurse) " +
                "VALUES(?, ?, ?, ?, ?);";

        try {
            PreparedStatement addAdministration = connection.prepareStatement(sql);
            addAdministration.setInt(1, administration.prescriptionKey);
            addAdministration.setTimestamp(2, Timestamp.valueOf(administration.administrationDateTime));
            addAdministration.setString(3, administration.qty);
            addAdministration.setString(4, administration.notes);
            addAdministration.setString(5, loggedUser.getId());
            messageDatabase.addVolatileMessage(new AddedAdministrationMessage(recoveryKey,
                    recoveryDatabase.mapRecoveryToRoom(recoveryKey),
                    administration,
                    getPrescription(administration.prescriptionKey)
            ));
            return addAdministration.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    private DrugPrescription getPrescription(int prescriptionKey) {

        String sql =
                "SELECT key, drug, date, daysDuration, dosesPerDay, quantity, notes, medic FROM prescription " +
                        "WHERE key = ?;";

        try {
            PreparedStatement queryPrescriptions = connection.prepareStatement(sql);
            queryPrescriptions.setInt(1, prescriptionKey);
            ResultSet result = queryPrescriptions.executeQuery();
            ArrayList<DrugPrescription> prescriptions = new ArrayList<>();

            return getPrescriptionFromResultSet(result);
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        return null;
    }

    private DrugPrescription getPrescriptionFromResultSet(ResultSet result) throws SQLException {

        if (!result.next())
            return null;

        return new DrugPrescription(
                result.getInt(1),
                drugsDatabase.getDrug(result.getString(2)),
                result.getDate(3).toLocalDate(),
                result.getString(4),
                result.getString(5),
                result.getString(6),
                result.getString(7),
                usersDatabase.getEditableUser(result.getString(8)));
    }

    @Override
    public List<DrugPrescription> getPrescriptions(int recoveryKey) {

        String sql =
                "SELECT key, drug, date, daysDuration, dosesPerDay, quantity, notes, medic FROM prescription " +
                        "WHERE recoveryKey = ?;";

        try {
            PreparedStatement queryPrescriptions = connection.prepareStatement(sql);
            queryPrescriptions.setInt(1, recoveryKey);
            ResultSet result = queryPrescriptions.executeQuery();
            ArrayList<DrugPrescription> prescriptions = new ArrayList<>();


            DrugPrescription current;
            while ((current = getPrescriptionFromResultSet(result)) != null) {
                prescriptions.add(current);
            }
            return prescriptions;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
