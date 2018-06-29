package it.ingsoftw.progetto.server.database;

import it.ingsoftw.progetto.common.EditableUser;
import it.ingsoftw.progetto.common.User;
import it.ingsoftw.progetto.common.utils.Password;

import java.sql.Connection;
import java.util.List;

class UsersDatabase implements IUsersDatabase {

    Connection connection;

    public UsersDatabase(Connection connection) {
        this.connection = connection;
    }

    private void createDatabase() {

    }


    @Override
    public boolean addUser(User user) {
        return false;
    }

    @Override
    public boolean removeUser(User user) {
        return false;
    }

    @Override
    public User getUser(String id) {
        return null;
    }

    @Override
    public User getUserFromEmail(String email) {
        return null;
    }

    @Override
    public List<User> getUserList() {
        return null;
    }

    @Override
    public EditableUser getEditableUser(String id) {
        return null;
    }

    @Override
    public void updateUser(EditableUser updatedUser) {

    }

    @Override
    public boolean updatePassword(String id, Password newPassword) {
        return false;
    }

    @Override
    public boolean authenticateUser(String id, Password password) {
        return false;
    }
}
