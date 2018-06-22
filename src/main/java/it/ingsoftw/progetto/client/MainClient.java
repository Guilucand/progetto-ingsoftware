package it.ingsoftw.progetto.client;

import java.rmi.Naming;

import it.ingsoftw.progetto.common.Drug;
import it.ingsoftw.progetto.common.IClientListener;
import it.ingsoftw.progetto.common.IClientRmiFactory;
import it.ingsoftw.progetto.common.ILogin;
import it.ingsoftw.progetto.common.utils.Password;

import javax.swing.*;

public class MainClient {

    static IClientRmiFactory serverFactory;

    public static void main(String[] args) {
        System.out.println("Hello, world1!");

        clientGUI clientFrame = new clientGUI();

        System.out.println(clientFrame.getUsername());

        DrugsQuery q = new DrugsQuery();
        Drug[] d = q.queryDatabase("Tachip", DrugsQuery.QueryType.ActivePrinciple, true);

        for (Drug dr : d) {
            System.out.println(dr.drugDescription + " " + dr.company);
        }

        try {

            IClientListener connection;
            String url = "//localhost:8080/auth";
            connection = (IClientListener) Naming.lookup(url);
            serverFactory = connection.estabilishConnection();

            ILogin loginInterface = serverFactory.getLoginInterface();

            System.out.println("Test wrong login: " + loginInterface.doLogin("test", Password.fromPassword("test")).toString());
            System.out.println("Test correct login: " + loginInterface.doLogin("test", Password.fromPassword("prova")).toString());

            loginInterface.passwordForgotten("guilucand@gmail.com");

            System.out.println("Mail sent!");

        }
        catch (Exception e) {
            System.out.println("Error! " + e.toString());
        }
    }
}
