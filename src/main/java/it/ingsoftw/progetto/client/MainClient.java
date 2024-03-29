package it.ingsoftw.progetto.client;

import java.rmi.Naming;
import java.rmi.RemoteException;

import it.ingsoftw.progetto.common.IClientListener;
import it.ingsoftw.progetto.common.IClientRmiFactory;
import it.ingsoftw.progetto.common.ILogin;
import it.ingsoftw.progetto.server.ServerConfig;

public class MainClient {

    private static ILogin.LoginStatus loginstatus = ILogin.LoginStatus.NOTLOGGED;

    static IClientRmiFactory serverFactory;
    static MessagesDispatcher messagesDispatcher;


    public static void main(String[] args) throws RemoteException {

        // TEST
//        DrugsQuery q = new DrugsQuery();
//        Drug[] d = q.queryDatabase("Va", DrugsQuery.QueryType.Drug, true);
//
//        for (Drug dr : d) {
//            System.out.println(dr.commercialName + " -> " + dr.company + " : " + dr.activePrinciple);
//        }

//        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
//        GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

//        double ratio = dim.height / 1080.0;
        System.setProperty("sun.java2d.uiScale", "1");//String.valueOf((int)ratio));

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
            new LoginGUI(loginInterface, (status, username) -> {

                // Callback in caso di login andato a buon fine
                switch (status) {
                    case NOTLOGGED:

                        break;

                    default:
                        Logged(username, status);
                        break;
                }
            });

        }
        catch (RemoteException e) {
            System.out.println("Connessione con il server persa: " + e.getMessage());
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

    private static void Logged(String username, ILogin.LoginStatus status) throws RemoteException {

        MonitorGUI monitorGUI = new MonitorGUI(username, status, serverFactory);

        if (messagesDispatcher == null) {
            messagesDispatcher = new MessagesDispatcher(serverFactory.getMessageInterface(), monitorGUI);
        }

    }
}
