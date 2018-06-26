package it.ingsoftw.progetto.client;

import it.ingsoftw.progetto.common.ILogin;

import java.rmi.RemoteException;

public interface IClientGuiCallback {
    void onLoginSuccessful(ILogin.LoginStatus status, String username) throws RemoteException;
}
