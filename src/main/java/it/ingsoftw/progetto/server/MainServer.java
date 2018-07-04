package it.ingsoftw.progetto.server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;
import java.time.LocalDate;

import it.ingsoftw.progetto.common.IClientListener;
import it.ingsoftw.progetto.common.IVSListener;
import it.ingsoftw.progetto.common.PatientData;
import it.ingsoftw.progetto.common.User;
import it.ingsoftw.progetto.common.utils.Password;
import it.ingsoftw.progetto.server.database.DatabaseConnection;
import it.ingsoftw.progetto.server.database.IDatabaseConnection;
import it.ingsoftw.progetto.server.database.IPatientsDatabase;
import it.ingsoftw.progetto.server.database.IRecoveryDatabase;
import test.database.TestDataAddition;

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

        System.setProperty("java.rmi.server.hostname", ServerConfig.hostname);

        IDatabaseConnection databaseConnection;
        databaseConnection = new DatabaseConnection();

        try {
            TestDataAddition.addTestData(databaseConnection);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }


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
