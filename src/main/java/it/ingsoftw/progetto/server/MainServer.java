package it.ingsoftw.progetto.server;

import com.sun.security.ntlm.Client;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import it.ingsoftw.progetto.common.IClientConnection;
import it.ingsoftw.progetto.common.User;

public class MainServer {

    static Registry serverRegistry;
    static IClientConnection clientListener;

    public static void main(String[] args) {
        System.out.println("Hello, world11!");
        ClientRmiFactory.testUsersDatabase.addUser(new User("test", "Ciao", "Ciao2", "guilucand@gmail.com", User.UserType.Medic));
        ClientRmiFactory.testUsersDatabase.updatePassword("test", "prova");
        try {
            serverRegistry = LocateRegistry.createRegistry(ServerConfig.port);
            clientListener = new ClientListener();
            serverRegistry.rebind("auth", clientListener);
        }
        catch (Exception ignored) {
            System.out.println("Error! " + ignored.toString());
        }
    }
}
