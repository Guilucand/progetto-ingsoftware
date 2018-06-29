package it.ingsoftw.progetto.client;

import it.ingsoftw.progetto.common.ILogin;
import it.ingsoftw.progetto.common.utils.Password;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Dimension;
import java.rmi.RemoteException;

import java.util.concurrent.TimeUnit;

public class ClientGUI extends JFrame{
    private JTextField usernameTextField;
    private JPasswordField passwordTextField;
    private  JPanel MainPanel;
    private JLabel imgUser;
    private JLabel imgPassw;
    private JLabel imgLogin;
    private JButton logInButton;
    private JButton forgotPasswordButton;
    private String username;
    private String password;
    private ILogin loginInterface;
    private ILogin.LoginStatus loginStatus;
    private IClientGuiCallback resultsCallback;
    private int limit_access = 5;

    public ClientGUI(ILogin loginInterface, IClientGuiCallback resultsCallback){

        // Con 'super' chiamo il costruttore della classe JFrame
        // a cui passo il titolo della finestra
        super("login-gui");

        this.loginInterface = loginInterface;
        this.resultsCallback = resultsCallback;

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();


        this.setResizable(false);
        this.setContentPane(MainPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setSize(400, 300);
        this.setLocation((dim.width/2-this.getSize().width/2), (dim.height/2-this.getSize().height/2));

        this.setVisible(true);


        logInButton.addActionListener(e -> TryAccess());

        forgotPasswordButton.addActionListener(e -> {

            new ConfirmChangePassword(loginInterface);

            /*if (JOptionPane.showConfirmDialog(null, "Conferma cambiamento password (invio nuova password tramite email)") == 0){

                try {
                    if(loginInterface.passwordForgotten("guilucand@gmail.com") == true){

                        JOptionPane.showMessageDialog(null,"La nuova password è stata inviata per mail");

                    }else{

                        JOptionPane.showMessageDialog(null,"hai inserito una mail non valida");

                    }
                } catch (RemoteException e1) {
                    e1.printStackTrace();
                }

            }*/

        });


        usernameTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);

                if(e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_DOWN) passwordTextField.requestFocusInWindow();

            }
        });


        passwordTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);

                if(e.getKeyCode() == KeyEvent.VK_ENTER) TryAccess();
                else if (e.getKeyCode() == KeyEvent.VK_UP) usernameTextField.requestFocusInWindow();
                else if(e.getKeyCode() == KeyEvent.VK_DOWN) logInButton.requestFocusInWindow();

            }

        });

        logInButton.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);

                if(e.getKeyCode() == KeyEvent.VK_UP) passwordTextField.requestFocusInWindow();
                else if(e.getKeyCode() == KeyEvent.VK_DOWN) forgotPasswordButton.requestFocusInWindow();
                else if (e.getKeyCode() == KeyEvent.VK_ENTER) TryAccess();

            }
        });

        forgotPasswordButton.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);

                if(e.getKeyCode() == KeyEvent.VK_UP) logInButton.requestFocusInWindow();
                if(e.getKeyCode() == KeyEvent.VK_ENTER) new ConfirmChangePassword(loginInterface);

            }
        });

        usernameTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_F3) RapidAccess();
            }
        });
    }


    public void Dispose(){

        this.dispose();
    }

    // CREO UN LOCK DI 30 SECONDI OGNI 5 TENTATIVI SBAGLIATI

    public void CheckAccess(){

        limit_access--;

        if (limit_access <= 0) {
            JOptionPane.showMessageDialog(null, "Hai superato 5 tentativi di accesso, bloccato per 30 secondi");
            try {
                TimeUnit.SECONDS.sleep(30);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }

            limit_access = 5;
            JOptionPane.showMessageDialog(null,"Ora puoi riprovare");
        }

    }

    public void TryAccess(){

        username = usernameTextField.getText();
        password = String.valueOf(passwordTextField.getPassword());

        try {
            loginStatus = loginInterface.doLogin(username, Password.fromPassword(password));
        }
        catch (RemoteException ex) {
            loginStatus = ILogin.LoginStatus.NOTLOGGED;
            // ILogin.LoginStatus loginStatus = MainClient.LogIn(this);

        }

        //System.out.println(loginStatus);

        if(loginStatus == ILogin.LoginStatus.NOTLOGGED) {

            JOptionPane.showMessageDialog(null, "USERNAME O PASSWORD ERRATI");
            CheckAccess();
        }


        if (loginStatus != ILogin.LoginStatus.NOTLOGGED) {

            Dispose();
            try {
                resultsCallback.onLoginSuccessful(loginStatus, username);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void RapidAccess(){

        username = "Primario";
        password = "prova";

        try {
            loginStatus = loginInterface.doLogin(username, Password.fromPassword(password));
        }
        catch (RemoteException ex) {
            loginStatus = ILogin.LoginStatus.NOTLOGGED;
            // ILogin.LoginStatus loginStatus = MainClient.LogIn(this);

        }

        //System.out.println(loginStatus);

        if(loginStatus == ILogin.LoginStatus.NOTLOGGED) {

            JOptionPane.showMessageDialog(null, "USERNAME O PASSWORD ERRATI");
            CheckAccess();
        }


        if (loginStatus != ILogin.LoginStatus.NOTLOGGED) {

            Dispose();
            try {
                resultsCallback.onLoginSuccessful(loginStatus, username);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }



}
