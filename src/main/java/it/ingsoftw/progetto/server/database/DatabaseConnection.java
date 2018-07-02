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

        usersDatabase = new UsersDatabase(connection);
        patientsDatabase = new PatientsDatabase(connection);
        drugsDatabase = new DrugsDatabase(connection);

        recoveryDatabase = new RecoveryDatabase(connection, getMessageInterface());

        messageDatabase = new MessageDatabase(this);
        prescriptionDatabase = new PrescriptionDatabase(connection, getDrugsInterface(), getUsersInterface());
    }

    @Override
    public IUsersDatabase getUsersInterface() {
        return usersDatabase;
    }

    private TestDatabaseConnection tmp = new TestDatabaseConnection();

    @Override
    public IPatientsDatabase getPatientsInterface() {
        return patientsDatabase;
    }

    @Override
    public IRecoveryDatabase getRecoveryInterface() {
        return recoveryDatabase;
    }

    @Override
    public IDrugsDatabase getDrugsInterface() {
        return drugsDatabase;
    }

    @Override
    public IMessageDatabase getMessageInterface() {
        return messageDatabase;
    }

    @Override
    public IPrescriptionDatabase getPrescriptionInterface() {
        return prescriptionDatabase;
    }
}
