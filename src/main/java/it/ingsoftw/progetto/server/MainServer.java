package it.ingsoftw.progetto.server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.Date;
import java.sql.SQLException;

import it.ingsoftw.progetto.client.DrugsQuery;
import it.ingsoftw.progetto.common.Drug;
import it.ingsoftw.progetto.common.IClientListener;
import it.ingsoftw.progetto.common.IVSListener;
import it.ingsoftw.progetto.common.PatientData;
import it.ingsoftw.progetto.common.User;
import it.ingsoftw.progetto.common.utils.Password;
import it.ingsoftw.progetto.server.database.DatabaseConnection;
import it.ingsoftw.progetto.server.database.IDatabaseConnection;
import it.ingsoftw.progetto.server.database.IPatientsDatabase;
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
        databaseConnection = new DatabaseConnection();


        IRecoveryDatabase recoveryDatabase = databaseConnection.getRecoveryInterface();
        IPatientsDatabase patientsDatabase = databaseConnection.getPatientsInterface();

        DrugsQuery q = new DrugsQuery();
        Drug[] d = q.queryDatabase("Va", DrugsQuery.QueryType.Drug, true);

        for (Drug dr : d) {
            System.out.println(dr.commercialName + " -> " + dr.company + " : " + dr.activePrinciple);
            try {
                databaseConnection.getDrugsDatabase().deleteDrug(dr);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }



        // TEST

        try {
            databaseConnection.getUsersInterface().addUser(
                    new User("user", "Cracco", "Ciao2", "guilucand@gmail.com", User.UserType.Primary),
                    Password.fromPassword("pass"));
            databaseConnection.getUsersInterface().addUser(
                    new User("test", "Simo", "Ciao2", "simo@gmail.com", User.UserType.Admin),
                    Password.fromPassword("prova"));

            patientsDatabase.addPatient(new PatientData(
                    "CRCNDR96T05A703M",
                    "Andrea",
                    "Cracco",
                    new Date(1996, 12, 5),
                    "Bassano del Grappa"));
        }
        catch (Exception e) {
            e.printStackTrace();
        }


        for (int i = 1; i <= 10; i++)
            recoveryDatabase.addRecovery("PATIENT" + String.valueOf(i), String.valueOf(i));
//            recoveryDatabase.setRoomMachineId(String.valueOf(i+1), String.valueOf(i+1));


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
