package it.ingsoftw.progetto.server.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import it.ingsoftw.progetto.common.Drug;

public class DrugsDatabase implements IDrugsDatabase {
    private Connection connection;

    public DrugsDatabase(Connection connection) {

        this.connection = connection;
        DatabaseUtils.createDatabaseFromSchema(connection, "schema/drug.sql");
    }

    @Override
    public boolean addDrug(Drug drug) throws SQLException {
        String sql =
                "INSERT into drug (company, commercialName, packageDescription, atcCode, activePrinciple, aicCode) " +
                        "VALUES (?, ?, ?, ?, ?, ?);";

        PreparedStatement addDrug = connection.prepareStatement(sql);
        addDrug.setString(1, drug.company);
        addDrug.setString(2, drug.commercialName);
        addDrug.setString(3, drug.packageDescription);
        addDrug.setString(4, drug.atcCode);
        addDrug.setString(5, drug.activePrinciple);
        addDrug.setString(6, drug.aic);

        try {
            return addDrug.executeUpdate() > 0;
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
    public boolean deleteDrug(Drug drug) throws SQLException {
        String sql =
                "DELETE FROM drug "  +
                    "WHERE aicCode = ?";

        PreparedStatement deleteDrug = connection.prepareStatement(sql);
        deleteDrug.setString(1, drug.aic);

        return deleteDrug.executeUpdate() > 0;
    }

    private Drug getDrugFromResultSet(ResultSet result) throws SQLException {

        if (!result.next())
            return null;

        return new Drug(
                result.getString(1),
                result.getString(2),
                result.getString(3),
                result.getString(4),
                result.getString(5),
                result.getString(6)
                );
    }


    @Override
    public Drug getDrug(String aicCode) throws SQLException {
        String sql =
                "SELECT company, commercialName, packageDescription, atcCode, activePrinciple, aicCode " +
                        "FROM drug " +
                        "WHERE aicCode = ?";

        PreparedStatement queryDrug = connection.prepareStatement(sql);
        queryDrug.setString(1, aicCode);

        ResultSet result = queryDrug.executeQuery();
        return getDrugFromResultSet(result);
    }


}
