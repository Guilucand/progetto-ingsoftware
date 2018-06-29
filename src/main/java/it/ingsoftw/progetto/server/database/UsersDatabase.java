package it.ingsoftw.progetto.server.database;

import it.ingsoftw.progetto.common.EditableUser;
import it.ingsoftw.progetto.common.User;
import it.ingsoftw.progetto.common.utils.Password;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

class UsersDatabase implements IUsersDatabase {

    Connection connection;

    public UsersDatabase(Connection connection) throws SQLException {
        this.connection = connection;
        createDatabase();
    }

    private void createDatabase() throws SQLException {

        String schema = "";
        try {
            schema = new String(getClass().getResourceAsStream("schema/users.sql").readAllBytes());
        } catch (IOException ignored) {
        }
        Statement dbCreateStatement = connection.createStatement();
        dbCreateStatement.executeUpdate(schema);
    }


    @Override
    public boolean addUser(User user, Password tempPassword) throws SQLException{
        String sql =
                "INSERT into users (username, password, name, surname, email, usertype)" +
                "VALUES (?, ?, ?, ?, ?, ?);";

        PreparedStatement addUser = connection.prepareStatement(sql);
        addUser.setString(1, user.getName());
        addUser.setString(2, tempPassword.getPasswordHash());
        addUser.setString(3, user.getName());
        addUser.setString(4, user.getSurname());
        addUser.setString(5, user.getEmail());
        addUser.setString(6, user.getUserType().toString());

        String test = user.getUserType().toString();

        return addUser.executeUpdate() > 0;
    }

    @Override
    public boolean removeUser(User user) throws SQLException {
        String sql =
                "DELETE FROM users" +
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
                "SELECT username, name, surname, email, usertype FROM users" +
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
    public void updateUser(EditableUser updatedUser) throws SQLException {

    }

    @Override
    public boolean updatePassword(String id, Password newPassword) throws SQLException {
        return false;
    }

    @Override
    public boolean authenticateUser(String id, Password password) throws SQLException {
        return false;
    }
}
