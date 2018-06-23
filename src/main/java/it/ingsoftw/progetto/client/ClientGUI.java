package it.ingsoftw.progetto.client;

import it.ingsoftw.progetto.common.ILogin;
import it.ingsoftw.progetto.common.utils.Password;
import javafx.scene.shape.CubicCurve;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Dimension;
import java.rmi.RemoteException;

public class ClientGUI extends JFrame {
    private JTextField usernameTextField;
    private JPasswordField passwordPasswordField;
    private JPanel MainPanel;
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

    public ClientGUI(ILogin loginInterface, IClientGuiCallback resultsCallback){
        // Con 'super' chiamo il costruttore della classe JFrame
        // a cui passo il titolo della finestra
        super("login-gui");

        this.loginInterface = loginInterface;
        this.resultsCallback = resultsCallback;

        logInButton.addActionListener(e -> {

            username = usernameTextField.getText();
            password = String.valueOf(passwordPasswordField.getPassword());

            try {
                loginStatus = loginInterface.doLogin(username, Password.fromPassword(password));
            }
            catch (RemoteException ex) {
                loginStatus = ILogin.LoginStatus.NOTLOGGED;
            }

            if (loginStatus != ILogin.LoginStatus.NOTLOGGED) {
                this.dispose();
                resultsCallback.onLoginSuccessful(loginStatus, username);
            }

            //JOptionPane.showMessageDialog(null,"effettuo il login");
        });

        forgotPasswordButton.addActionListener(e -> {

            //password = String.valueOf(passwordPasswordField.getPassword());

            //JOptionPane.showMessageDialog(null,"ops.... password dimenticata");
        });

        /*logInButton.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);

            }
        });*/

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((dim.width/2-this.getSize().width/2), (dim.height/2-this.getSize().height/2));

        this.setResizable(false);
        this.setContentPane(MainPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setSize(400, 300);
        this.setVisible(true);
    }
}
