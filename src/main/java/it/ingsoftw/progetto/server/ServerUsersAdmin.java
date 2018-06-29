package it.ingsoftw.progetto.server;

import it.ingsoftw.progetto.common.EditableUser;
import it.ingsoftw.progetto.common.IAdmin;
import it.ingsoftw.progetto.common.User;
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

        return database.addUser(newUser);
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
        return null;
    }

    @Override
    public boolean commitUserChanges(EditableUser editedUser) {
        return false;
    }

    private ClientStatus status;
    private IUsersDatabase database;

}
