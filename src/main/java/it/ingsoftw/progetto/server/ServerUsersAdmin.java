package it.ingsoftw.progetto.server;

import it.ingsoftw.progetto.common.EditableUser;
import it.ingsoftw.progetto.common.IAdmin;
import it.ingsoftw.progetto.common.User;
import it.ingsoftw.progetto.common.utils.Password;
import it.ingsoftw.progetto.common.utils.RandomString;
import it.ingsoftw.progetto.server.database.IUsersDatabase;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class ServerUsersAdmin extends UnicastRemoteObject implements IAdmin {

    protected ServerUsersAdmin(ClientStatus status, IUsersDatabase database) throws RemoteException {
        super(ServerConfig.port);
        this.status = status;
        this.database = database;
    }

    private boolean hasPermission() {
        User loggedUser = status.getLoggedUser();

        return loggedUser != null &&
                (loggedUser.getUserType() == User.UserType.Admin ||
                 loggedUser.getUserType() == User.UserType.Primary);
    }

    @Override
    public boolean addUser(User newUser) {
        if (!hasPermission()) return false;


        if (database.addUser(newUser)) {
//            String tempPassword = new RandomString(12).nextString();
            String tempPassword = newUser.getName();
            database.updatePassword(newUser.getId(), Password.fromPassword(tempPassword));
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteUser(EditableUser userToDelete) {
        if (!hasPermission()) return false;

        return database.removeUser(userToDelete);
    }

    @Override
    public List<User> getUsers() {
        if (!hasPermission()) return null;

        return database.getUserList();
    }

    @Override
    public EditableUser getEditableUser(String id) {
        return database.getEditableUser(id);
    }

    @Override
    public void commitUserChanges(EditableUser editedUser) {
        database.updateUser(editedUser);
    }

    private ClientStatus status;
    private IUsersDatabase database;

}
