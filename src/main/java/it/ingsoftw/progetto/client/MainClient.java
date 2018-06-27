package it.ingsoftw.progetto.client;

import java.rmi.Naming;
import java.rmi.RemoteException;

import it.ingsoftw.progetto.common.Drug;
import it.ingsoftw.progetto.common.IClientListener;
import it.ingsoftw.progetto.common.IClientRmiFactory;
import it.ingsoftw.progetto.common.ILogin;
import it.ingsoftw.progetto.server.ServerConfig;

public class MainClient {

    private static ILogin.LoginStatus loginstatus = ILogin.LoginStatus.NOTLOGGED;

    static IClientRmiFactory serverFactory;

    public static void main(String[] args) {

        System.setProperty("sun.java2d.uiScale","1");

        try {
            // Connessione al server
            IClientRmiFactory serverConnection = connectToServer();

            // Ottenimento dell'interfaccia di login
            ILogin loginInterface = serverConnection.getLoginInterface();

            // Avvio della GUI di login
            new ClientGUI(loginInterface, (status, username) -> {

                // Callback in caso di login andato a buon fine
                switch (status) {
                    case NOTLOGGED:

                        break;

                    default:
                        Logged(status,username);
                        break;
                }
            });

        }
        catch (RemoteException e) {
            System.out.println("Connessione con il server persa: " + e.getMessage());
        }

        // TEST
        DrugsQuery q = new DrugsQuery();
        Drug[] d = q.queryDatabase("Tachip", DrugsQuery.QueryType.ActivePrinciple, true);

        /*for (Drug dr : d) {
            System.out.println(dr.drugDescription + " " + dr.company);
        }*/


    }

    /**
     * Metodo per connettersi al server
     * @return istanza della factory per richiedere
     * interfacce di connessione al server
     * @throws RemoteException
     */
    public static IClientRmiFactory connectToServer() throws RemoteException {

        IClientListener connection;
        String url = "//" + ServerConfig.hostname + ":" + ServerConfig.port + "/auth";


        try{

            connection=(IClientListener) Naming.lookup(url);

        }catch (Exception e){

            throw new RemoteException();

        }

        return connection.estabilishConnection();


    }

    private static void Logged(ILogin.LoginStatus status, String username) {

        new MonitorGUI(status,username);

    }
}
