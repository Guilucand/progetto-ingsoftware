package it.ingsoftw.progetto.server.database;

import test.database.TestDatabaseConnection;
import test.database.TestRecoveryDatabase;

import java.rmi.RemoteException;
import java.sql.*;
import java.util.Properties;



public class DatabaseConnection implements IDatabaseConnection {

    private Connection connection;
    private UsersDatabase usersDatabase;
    private PatientsDatabase patientsDatabase;


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
    public IUsersDatabase getUsersInterface() throws RemoteException {
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
        return tmp.getRecoveryInterface();
    }
}
