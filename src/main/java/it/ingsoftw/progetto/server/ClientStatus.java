package it.ingsoftw.progetto.server;

import it.ingsoftw.progetto.common.User;

public class ClientStatus {

    public User getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(User loggedUser) {
        this.loggedUser = loggedUser;
    }

    private User loggedUser;
}
