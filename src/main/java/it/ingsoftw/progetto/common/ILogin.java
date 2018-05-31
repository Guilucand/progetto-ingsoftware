package it.ingsoftw.progetto.common;


import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ILogin extends Remote {

    enum LoginStatus {
        NOTLOGGED,
        MEDIC_LOGGED,
        NURSE_LOGGED,
        PRIMARY_LOGGED,
        ADMIN_LOGGED
    }


    LoginStatus doLogin(String userId, String password) throws RemoteException;
    LoginStatus isLogged() throws RemoteException;
    boolean doLogout() throws RemoteException;

    boolean passwordForgotten(String email) throws RemoteException;
    boolean changePassword(String oldPassword, String newPassword) throws RemoteException;
}
