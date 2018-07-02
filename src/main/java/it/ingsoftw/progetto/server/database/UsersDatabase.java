package it.ingsoftw.progetto.server.database;

import it.ingsoftw.progetto.common.EditableUser;
import it.ingsoftw.progetto.common.User;
import it.ingsoftw.progetto.common.utils.Password;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.xml.crypto.Data;

class UsersDatabase implements IUsersDatabase {

    Connection connection;

    public UsersDatabase(Connection connection) {
        this.connection = connection;
        DatabaseUtils.createDatabaseFromSchema(connection, "schema/users.sql");
    }

    @Override
    public boolean addUser(User user, Password tempPassword) throws SQLException{
        String sql =
                "INSERT INTO users (username, password, name, surname, email, usertype) " +
                "VALUES (?, ?, ?, ?, ?, CAST (? AS privilege) );";

        PreparedStatement addUser = connection.prepareStatement(sql);
        addUser.setString(1, user.getId());
        addUser.setString(2, tempPassword.getPasswordHash());
        addUser.setString(3, user.getName());
        addUser.setString(4, user.getSurname());
        addUser.setString(5, user.getEmail());
        addUser.setString(6, user.getUserType().toString());

        try {
            return addUser.executeUpdate() > 0;
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
    public boolean removeUser(User user) throws SQLException {
        String sql =
                "DELETE FROM users " +
                "WHERE " +
                        "username = ?" +
                        ";";

        PreparedStatement deleteUser = connection.prepareStatement(sql);
        deleteUser.setString(1, user.getId());

        return deleteUser.executeUpdate() > 0;
    }

    private User getUserFromResultSet(ResultSet result) throws SQLException {

        if (!result.next())
            return null;

        return new User(
                result.getString(1),
                result.getString(2),
                result.getString(3),
                result.getString(4),
                User.UserType.valueOf(result.getString(5)));
    }

    @Override
    public User getUser(String id) throws SQLException {
        String sql =
                "SELECT username, name, surname, email, usertype FROM users " +
                        "WHERE " +
                        "username = ?" +
                        ";";

        PreparedStatement queryUser = connection.prepareStatement(sql);
        queryUser.setString(1, id);
        ResultSet result = queryUser.executeQuery();

        return getUserFromResultSet(result);
    }

    @Override
    public User getUserFromEmail(String email) throws SQLException {
        String sql =
                "SELECT username, name, surname, email, usertype FROM users" +
                        "WHERE " +
                        "email = ?" +
                        ";";

        PreparedStatement queryUser = connection.prepareStatement(sql);
        queryUser.setString(1, email);
        ResultSet result = queryUser.executeQuery();

        return getUserFromResultSet(result);
    }

    @Override
    public List<User> getUserList() throws SQLException{
        String sql =
                "SELECT username, name, surname, email, usertype FROM users" +
                        ";";

        PreparedStatement queryUsers = connection.prepareStatement(sql);
        ResultSet result = queryUsers.executeQuery();

        ArrayList<User> users = new ArrayList<>();

        User current;
        while ((current = getUserFromResultSet(result)) != null) {
            users.add(current);
        }

        return users;
    }

    @Override
    public EditableUser getEditableUser(String id) throws SQLException {
        User user = getUser(id);
        if (user == null) return null;

        return new EditableUser(user, id);
    }

    @Override
    public boolean updateUser(EditableUser updatedUser) throws SQLException {
        String sql =
                "UPDATE users SET (username, name, surname, email, usertype)" +
                "= (?, ?, ?, ?, CAST (? AS privilege) ) " +
                "WHERE username = ?;";

        PreparedStatement addUser = connection.prepareStatement(sql);
        addUser.setString(1, updatedUser.getId());
        addUser.setString(2, updatedUser.getName());
        addUser.setString(3, updatedUser.getSurname());
        addUser.setString(4, updatedUser.getEmail());
        addUser.setString(5, updatedUser.getUserType().toString());

        // Imposto la chiave primaria dello user prima della modifica
        addUser.setString(6, updatedUser.getInternalReference());

        try {
            return addUser.executeUpdate() > 0;
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
    public boolean updatePassword(String id, Password newPassword) throws SQLException {
        String sql =
                "UPDATE users SET password = ? " +
                        "WHERE username = ?";

        PreparedStatement changePassword = connection.prepareStatement(sql);
        changePassword.setString(1, newPassword.getPasswordHash());
        changePassword.setString(2, id);

        try {
            return changePassword.executeUpdate() > 0;
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
    public boolean authenticateUser(String id, Password password) throws SQLException {
        String sql =
                "SELECT password FROM users " +
                        "WHERE " +
                        "username = ?" +
                        ";";

        PreparedStatement queryUser = connection.prepareStatement(sql);
        queryUser.setString(1, id);
        ResultSet result = queryUser.executeQuery();

        if (!result.next()) return false;

        return Objects.equals(Password.fromHash(result.getString(1)), password);
    }
}
