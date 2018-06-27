package it.ingsoftw.progetto.server.database;

import java.sql.*;
import java.util.Properties;



public class DatabaseConnection implements IDatabaseConnection {
    private Connection connection;

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
        return null;
    }

    @Override
    public IPatientsDatabase getPatientsInterface() {
        return null;
    }

    @Override
    public IRecoveryDatabase getRecoveryInterface() {
        return null;
    }
}
