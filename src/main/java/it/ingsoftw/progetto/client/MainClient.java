package it.ingsoftw.progetto.client;

import java.rmi.Naming;
import java.rmi.RemoteException;

import it.ingsoftw.progetto.common.*;
import it.ingsoftw.progetto.server.ServerConfig;

public class MainClient {

    private static ILogin.LoginStatus loginstatus = ILogin.LoginStatus.NOTLOGGED;

    static IClientRmiFactory serverFactory;

    public static void main(String[] args) throws RemoteException {

        System.setProperty("sun.java2d.uiScale","1");

        try {
            // Connessione al server
            for (int i = 0; i < 200; i++) {
                try {
                    serverFactory = connectToServer();
                } catch (RemoteException e) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e1) {
                        continue;
                    }
                    continue;
                }
                break;
            }

            // Ottenimento dell'interfaccia di login
            ILogin loginInterface = serverFactory.getLoginInterface();

            //new AdminPanel(ILogin.LoginStatus.PRIMARY_LOGGED,serverFactory.getAdminInterface());

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

        for (Drug dr : d) {
            System.out.println(dr.drugDescription + " " + dr.company);
        }


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

    private static void Logged(ILogin.LoginStatus status, String username) throws RemoteException {

        new MonitorGUI(status,username,serverFactory);

    }
}
