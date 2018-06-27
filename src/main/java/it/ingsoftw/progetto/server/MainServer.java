package it.ingsoftw.progetto.server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import it.ingsoftw.progetto.common.IClientListener;
import it.ingsoftw.progetto.common.IVSListener;
import it.ingsoftw.progetto.common.User;
import it.ingsoftw.progetto.common.utils.Password;
import it.ingsoftw.progetto.server.database.DatabaseConnection;
import it.ingsoftw.progetto.server.database.IDatabaseConnection;
import it.ingsoftw.progetto.server.database.IRecoveryDatabase;
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
        databaseConnection.getUsersInterface().addUser(new User("test", "Cracco", "Ciao2", "guilucand@gmail.com", User.UserType.Primary));
        databaseConnection.getUsersInterface().addUser(new User("test1", "Simo", "Ciao2", "simo@gmail.com", User.UserType.Admin));
        databaseConnection.getUsersInterface().updatePassword("test", Password.fromPassword("prova"));
        databaseConnection.getUsersInterface().updatePassword("test1", Password.fromPassword("prova1"));

        IRecoveryDatabase recoveryDatabase = databaseConnection.getRecoveryInterface();

        for (int i = 0; i < 10; i++)
            recoveryDatabase.setRoomMachineId(String.valueOf(i+1), String.valueOf(i+1));

        recoveryDatabase.addRecovery("Luigi", "1");


        // Registrazione delle interfacce iniziali di comunicazione con client e macchine di monitoraggio
        try {
            serverRegistry = LocateRegistry.createRegistry(ServerConfig.port);

            clientListener = new ClientListener(databaseConnection);
            vsListener = new VsListener(databaseConnection.getRecoveryInterface());

            serverRegistry.rebind("auth", clientListener);
            serverRegistry.rebind("vsauth", vsListener);
        }
        catch (Exception e) {
            System.out.println("Connection error: " + e.toString());
        }

    }
}
