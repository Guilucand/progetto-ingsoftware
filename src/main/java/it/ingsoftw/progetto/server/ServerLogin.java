package it.ingsoftw.progetto.server;

import it.ingsoftw.progetto.common.ILogin;
import it.ingsoftw.progetto.common.User;
import it.ingsoftw.progetto.common.utils.Password;
import it.ingsoftw.progetto.common.utils.RandomString;
import it.ingsoftw.progetto.server.database.IUsersDatabase;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


public class ServerLogin extends UnicastRemoteObject implements ILogin {

    public ServerLogin(IUsersDatabase usersDbConnection) throws RemoteException {
        super(ServerConfig.port);
        this.usersDbConnection = usersDbConnection;
    }

    private LoginStatus getLoginStatus() {
        if (userLogged == null)
            return LoginStatus.NOTLOGGED;

        switch (userLogged.getUserType()) {
            case Medic:
                return LoginStatus.MEDIC_LOGGED;
            case Nurse:
                return LoginStatus.NURSE_LOGGED;

            case Primary:
                return LoginStatus.PRIMARY_LOGGED;
        }
        return LoginStatus.NOTLOGGED;
    }

    @Override
    public LoginStatus doLogin(String userId, Password password) {

        boolean loginSuccessful = usersDbConnection.authenticateUser(userId, password);

        if (!loginSuccessful)
            return LoginStatus.NOTLOGGED;

        userLogged = usersDbConnection.getUser(userId);

        System.out.println("Login with id: " + userId);

        return getLoginStatus();
    }

    @Override
    public LoginStatus isLogged() {
        return getLoginStatus();
    }

    @Override
    public boolean doLogout() {
        userLogged = null;
        return true;
    }

    @Override
    public boolean passwordForgotten(String email) {

        User user = usersDbConnection.getUserFromEmail(email);

        if (user == null) return false;


        String tempPassword = new RandomString(12).nextString();
        usersDbConnection.updatePassword(user.getId(), Password.fromPassword(tempPassword));
        EmailSender.sendForgotPasswordMail(email, tempPassword);
        return true;
    }

    @Override
    public boolean changePassword(Password oldPassword, Password newPassword) {
        return false;
    }

    private User userLogged;
    private IUsersDatabase usersDbConnection;
}
