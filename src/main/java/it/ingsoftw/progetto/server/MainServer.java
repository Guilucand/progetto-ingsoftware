package it.ingsoftw.progetto.server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import it.ingsoftw.progetto.common.IClientListener;
import it.ingsoftw.progetto.common.IVSListener;
import it.ingsoftw.progetto.common.User;
import it.ingsoftw.progetto.common.utils.Password;
import it.ingsoftw.progetto.server.database.IDatabaseConnection;
import test.database.TestDatabaseConnection;

public class MainServer {

    /**
     * Il registro RMI
     */
    static Registry serverRegistry;

    /**
     * L'interfaccia di comunicazione dei client
     */
    static IClientListener clientListener;

    /**
     * L'interfaccia di comunicazione delle macchine di monitoraggio
     */
    static IVSListener vsListener;

    public static void main(String[] args) {

        IDatabaseConnection databaseConnection;

        // TEST
        databaseConnection = new TestDatabaseConnection();
        databaseConnection.getUsersInterface().addUser(new User("test", "Ciao", "Ciao2", "guilucand@gmail.com", User.UserType.Medic));
        databaseConnection.getUsersInterface().updatePassword("test", Password.fromPassword("prova"));

        // Registrazione delle interfacce iniziali di comunicazione con client e macchine di monitoraggio
        try {
            serverRegistry = LocateRegistry.createRegistry(ServerConfig.port);

            clientListener = new ClientListener(databaseConnection);
            vsListener = new VsListener();

            serverRegistry.rebind("auth", clientListener);
            serverRegistry.rebind("vsauth", vsListener);
        }
        catch (Exception e) {
            System.out.println("Connection error: " + e.toString());
        }

    }
}
