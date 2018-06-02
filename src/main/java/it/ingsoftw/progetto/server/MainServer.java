package it.ingsoftw.progetto.server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import it.ingsoftw.progetto.common.IClientListener;
import it.ingsoftw.progetto.common.IVSListener;
import it.ingsoftw.progetto.common.User;

public class MainServer {

    static Registry serverRegistry;
    static IClientListener clientListener;
    static IVSListener vsListener;

    public static void main(String[] args) {
        ClientRmiFactory.testUsersDatabase.addUser(new User("test", "Ciao", "Ciao2", "guilucand@gmail.com", User.UserType.Medic));
        ClientRmiFactory.testUsersDatabase.updatePassword("test", "prova");
        try {
            serverRegistry = LocateRegistry.createRegistry(ServerConfig.port);

            clientListener = new ClientListener();
            vsListener = new VsListener();

            serverRegistry.rebind("auth", clientListener);
            serverRegistry.rebind("vsauth", vsListener);

        }
        catch (Exception ignored) {
            System.out.println("Error! " + ignored.toString());
        }
    }
}
