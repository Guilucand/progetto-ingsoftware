package it.ingsoftw.progetto.server.database;

import test.database.TestDatabaseConnection;

import java.sql.*;
import java.util.Properties;



public class DatabaseConnection implements IDatabaseConnection {

    private Connection connection;
    private UsersDatabase usersDatabase;
    private PatientsDatabase patientsDatabase;
    private DrugsDatabase drugsDatabase;
    private RecoveryDatabase recoveryDatabase;
    private MessageDatabase messageDatabase;
    private PrescriptionDatabase prescriptionDatabase;

    private static final String DATABASE_HOST = "localhost";

    private static final String DATABASE_USER = "ingsoft";

    private static final String DATABASE_NAME = "ingsoft";

    private static final String DATABASE_PASS = "ingsoft";



    public DatabaseConnection() {
        Properties properties = new Properties();

        properties.setProperty("user", DATABASE_USER);
        properties.setProperty("password", DATABASE_PASS);

        try {
            connection = DriverManager.getConnection("jdbc:postgresql://" + DATABASE_HOST + "/" + DATABASE_NAME, properties);
        } catch (SQLException e) {
            System.out.println("Impossibile stabilire una connessione con il database: " + e.getLocalizedMessage());
        }
    }

    @Override
    public IUsersDatabase getUsersInterface() {
        if (usersDatabase == null) {
            usersDatabase = new UsersDatabase(connection);
        }
        return usersDatabase;
    }

    private TestDatabaseConnection tmp = new TestDatabaseConnection();

    @Override
    public IPatientsDatabase getPatientsInterface() {
        if (patientsDatabase == null) {
            patientsDatabase = new PatientsDatabase(connection);
        }
        return patientsDatabase;
    }

    @Override
    public IRecoveryDatabase getRecoveryInterface() {
        if (recoveryDatabase == null) {
            recoveryDatabase = new RecoveryDatabase(connection, getMessageInterface());
        }
        return recoveryDatabase;
    }

    @Override
    public IDrugsDatabase getDrugsInterface() {
        if (drugsDatabase == null)
            drugsDatabase = new DrugsDatabase(connection);
        return drugsDatabase;
    }

    @Override
    public IMessageDatabase getMessageInterface() {
        if (messageDatabase == null)
            messageDatabase = new MessageDatabase(this);
        return messageDatabase;
    }

    @Override
    public IPrescriptionDatabase getPrescriptionInterface() {
        if (prescriptionDatabase == null)
            prescriptionDatabase = new PrescriptionDatabase(connection, getDrugsInterface(), getUsersInterface());
        return prescriptionDatabase;
    }
}
