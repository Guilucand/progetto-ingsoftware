package it.ingsoftw.progetto.server;

import it.ingsoftw.progetto.common.ILogin;
import it.ingsoftw.progetto.common.User;
import it.ingsoftw.progetto.common.utils.Password;
import it.ingsoftw.progetto.common.utils.RandomString;
import it.ingsoftw.progetto.server.database.IUsersDatabase;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;


public class ServerLogin extends UnicastRemoteObject implements ILogin {

    public ServerLogin(ClientStatus status, IUsersDatabase database) throws RemoteException {
        super(ServerConfig.port);
        this.status = status;
        this.database = database;
    }

    private LoginStatus getLoginStatus() {
        if (status.getLoggedUser() == null)
            return LoginStatus.NOTLOGGED;

        switch (status.getLoggedUser().getUserType()) {
            case Medic:
                return LoginStatus.MEDIC_LOGGED;
            case Nurse:
                return LoginStatus.NURSE_LOGGED;
            case Primary:
                return LoginStatus.PRIMARY_LOGGED;
            case Admin:
                return LoginStatus.ADMIN_LOGGED;
        }
        return LoginStatus.NOTLOGGED;
    }

    @Override
    public LoginStatus doLogin(String userId, Password password) throws RemoteException {

        try {
            boolean loginSuccessful = database.authenticateUser(userId, password);

            if (!loginSuccessful)
                return LoginStatus.NOTLOGGED;

            status.setLoggedUser(database.getUser(userId));

            System.out.println("Login with id: " + userId);

            return getLoginStatus();
        }
        catch (SQLException ex) {
            throw new RemoteException(ex.getMessage());
        }
    }

    @Override
    public LoginStatus isLogged() {
        return getLoginStatus();
    }

    @Override
    public User getLoggedUser() {
        return status.getLoggedUser();
    }

    @Override
    public boolean doLogout() {
        status.setLoggedUser(null);
        return true;
    }

    @Override
    public boolean passwordForgotten(String email) throws RemoteException {

        try {
            User user = database.getUserFromEmail(email);

            if (user == null) return false;

            String tempPassword = new RandomString(12).nextString();
            database.updatePassword(user.getId(), Password.fromPassword(tempPassword));
            EmailSender.sendForgotPasswordMail(email, tempPassword);
            return true;
        }
        catch (SQLException ex) {
            throw new RemoteException(ex.getMessage());
        }
    }

    @Override
    public boolean changePassword(Password oldPassword, Password newPassword) {
        try {

            User loggedUser = getLoggedUser();
            if (loggedUser == null)
                return false;

            if (database.authenticateUser(loggedUser.getId(), oldPassword)) {
                database.updatePassword(getLoggedUser().getId(), newPassword);
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private ClientStatus status;
    private IUsersDatabase database;
}
