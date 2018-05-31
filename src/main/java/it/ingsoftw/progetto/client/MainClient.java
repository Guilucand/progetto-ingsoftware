package it.ingsoftw.progetto.client;

import java.rmi.Naming;

import it.ingsoftw.progetto.common.IClientConnection;
import it.ingsoftw.progetto.common.IClientRmiFactory;
import it.ingsoftw.progetto.common.ILogin;

public class MainClient {

    static IClientRmiFactory serverFactory;

    public static void main(String[] args) {
        System.out.println("Hello, world1!");

        try {

            IClientConnection connection;
            String url = "//localhost:8080/auth";
            connection = (IClientConnection) Naming.lookup(url);
            serverFactory = connection.estabilishConnection();

            ILogin loginInterface = serverFactory.getLoginInterface();

            System.out.println("Test wrong login: " + loginInterface.doLogin("test", "test").toString());
            System.out.println("Test correct login: " + loginInterface.doLogin("test", "prova").toString());

            loginInterface.passwordForgotten("guilucand@gmail.com");

            System.out.println("Mail sent!");

        }
        catch (Exception e) {
            System.out.println("Error! " + e.toString());
        }
    }
}
