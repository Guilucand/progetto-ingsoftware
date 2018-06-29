package it.ingsoftw.progetto.server;

import it.ingsoftw.progetto.common.EditableUser;
import it.ingsoftw.progetto.common.IAdmin;
import it.ingsoftw.progetto.common.User;
import it.ingsoftw.progetto.common.utils.Password;
import it.ingsoftw.progetto.common.utils.RandomString;
import it.ingsoftw.progetto.server.database.IUsersDatabase;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
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
    public boolean addUser(User newUser) throws RemoteException {
        if (!hasPermission()) return false;

        Password tempPassword = Password.fromPassword(newUser.getName());

        try {
            return database.addUser(newUser, tempPassword);
        } catch (SQLException e) {
            throw new RemoteException(e.getMessage());
        }
    }

    @Override
    public boolean deleteUser(EditableUser userToDelete) throws RemoteException {
        if (!hasPermission()) return false;

        try {
            return database.removeUser(userToDelete);
        } catch (SQLException e) {
            throw new RemoteException((e.getMessage()));
        }
    }

    @Override
    public List<User> getUsers() throws RemoteException {
        if (!hasPermission()) return null;

        try {
            return database.getUserList();
        } catch (SQLException e) {
            throw new RemoteException(e.getMessage());
        }
    }

    @Override
    public EditableUser getEditableUser(String id) throws RemoteException {
        try {
            return database.getEditableUser(id);
        } catch (SQLException e) {
            throw new RemoteException(e.getMessage());
        }
    }

    @Override
    public void commitUserChanges(EditableUser editedUser) throws RemoteException {
        try {
            database.updateUser(editedUser);
        } catch (SQLException e) {
            throw new RemoteException(e.getMessage());
        }
    }

    private ClientStatus status;
    private IUsersDatabase database;

}
