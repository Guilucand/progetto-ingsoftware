package it.ingsoftw.progetto.client;

import it.ingsoftw.progetto.common.ILogin;

public interface IClientGuiCallback {
    void onLoginSuccessful(ILogin.LoginStatus status, String username);
}
