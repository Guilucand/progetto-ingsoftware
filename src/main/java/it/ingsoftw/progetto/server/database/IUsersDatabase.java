package it.ingsoftw.progetto.server.database;

import it.ingsoftw.progetto.common.EditableUser;
import it.ingsoftw.progetto.common.User;

public interface IUsersDatabase {
    boolean addUser(User user);
    boolean removeUser(User user);
    User getUser(String id);
    User getUserFromEmail(String email);

    EditableUser getEditableUser(String id);
    void updateUser(EditableUser updatedUser);

    boolean updatePassword(String id, String newPassword);
    boolean authenticateUser(String id, String password);
}
