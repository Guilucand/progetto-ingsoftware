package it.ingsoftw.progetto.server.database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseUtils {

    public static boolean createDatabaseFromSchema(Connection connection, String schemaResourceUri) {
        try {
            String schema = new String(DatabaseUtils.class.getResourceAsStream(schemaResourceUri).readAllBytes());
            Statement dbCreateStatement = connection.createStatement();
            dbCreateStatement.executeUpdate(schema);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
