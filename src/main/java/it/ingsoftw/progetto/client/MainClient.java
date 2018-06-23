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

        clientGUI clientlogin = CreaLoginGUI();

        DrugsQuery q = new DrugsQuery();
        Drug[] d = q.queryDatabase("Tachip", DrugsQuery.QueryType.ActivePrinciple, true);

        for (Drug dr : d) {
            System.out.println(dr.drugDescription + " " + dr.company);
        }


    }

    public static ILogin.LoginStatus LogIn(clientGUI clientlogin) {

        String userID = clientlogin.getUsername();
        String password = clientlogin.getPassword();
        ILogin.LoginStatus loginstatus = ILogin.LoginStatus.NOTLOGGED;


        try {

            IClientListener connection;
            String url = "//localhost:8080/auth";
            connection = (IClientListener) Naming.lookup(url);
            serverFactory = connection.estabilishConnection();

            ILogin loginInterface = serverFactory.getLoginInterface();

            System.out.println("tento il login con userID = "+userID+" e password = "+password);

            System.out.println("Test wrong login: " + loginInterface.doLogin("test", Password.fromPassword("test")).toString());
            System.out.println("Test correct login: " + loginInterface.doLogin(userID, Password.fromPassword(password)).toString());

            loginstatus = loginInterface.doLogin(userID, Password.fromPassword(password));


        }
        catch (Exception e) {
            System.out.println("Error! " + e.toString());
        }

        return loginstatus;


    }


    public static void MedicLogged(clientGUI clientlogin){

        clientlogin.dispose();
        MonitorGUI monitor = CreaMonitorGUI();


        monitor.AddPatient();

    }


    public static void PasswordForgotten( clientGUI clintepswforgotten){

        try {

            IClientListener connection;
            String url = "//localhost:8080/auth";
            connection = (IClientListener) Naming.lookup(url);
            serverFactory = connection.estabilishConnection();

            ILogin loginInterface = serverFactory.getLoginInterface();

            loginInterface.passwordForgotten("guilucand@gmail.com");

            System.out.println("Mail sent!");



        }catch (Exception e) {
            System.out.println("Error! " + e.toString());
        }

    }


    public static clientGUI CreaLoginGUI(){

        return new clientGUI();

    }

    public static MonitorGUI CreaMonitorGUI(){

        return new MonitorGUI();

    }


}
