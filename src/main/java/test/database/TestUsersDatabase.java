package test.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import it.ingsoftw.progetto.common.EditableUser;
import it.ingsoftw.progetto.common.User;
import it.ingsoftw.progetto.common.utils.Password;
import it.ingsoftw.progetto.server.database.IUsersDatabase;

public class TestUsersDatabase implements IUsersDatabase {

    HashSet<User> users;
    HashMap<String, String> auth;

    public TestUsersDatabase() {
        users = new HashSet<>();
        auth = new HashMap<>();
    }

    @Override
    public boolean addUser(User user, Password tempPassword) {
        if (getUser(user.getId()) != null)
            return false;

        users.add(user);
        updatePassword(user.getId(), tempPassword);
        return true;
    }

    @Override
    public boolean removeUser(User user) {
        return users.removeIf((u)->u.getId().equals(user.getId()));
    }

    @Override
    public User getUser(String id) {
        for (User u : users) {
            if (u.getId().equals(id))
                return u;
        }
        return null;
    }

    @Override
    public User getUserFromEmail(String email) {
        for (User u : users) {
            if (u.getEmail().equals(email))
                return u;
        }
        return null;
    }

    @Override
    public List<User> getUserList() {
        return new ArrayList<>(users);
    }

    @Override
    public EditableUser getEditableUser(String id) {
        return new EditableUser(getUser(id), id);
    }

    @Override
    public void updateUser(EditableUser updatedUser) {

        users.remove(getUser(updatedUser.getInternalReference()));
        users.add(new User(updatedUser));
    }

    @Override
    public boolean updatePassword(String id, Password newPassword) {
        if (getUser(id) == null)
            return false;

        auth.put(id, newPassword.getPasswordHash());
        return true;
    }

    @Override
    public boolean authenticateUser(String id, Password password) {
        if (password.equals(Password.fromHash(auth.getOrDefault(id, null))))
            return true;
        return false;
    }
}
