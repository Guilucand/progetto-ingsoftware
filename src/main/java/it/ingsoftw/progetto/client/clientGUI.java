package it.ingsoftw.progetto.client;

import it.ingsoftw.progetto.common.ILogin;
import javafx.scene.shape.CubicCurve;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Dimension;

public class clientGUI extends JFrame {
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

    public clientGUI(){
        // Con 'super' chiamo il costruttore della classe JFrame
        // a cui passo il titplo della finestra
        super("login-gui");

        logInButton.addActionListener(e -> {

            username = usernameTextField.getText();
            password = String.valueOf(passwordPasswordField.getPassword());

            ILogin.LoginStatus loginStatus = MainClient.LogIn(this);

            switch (loginStatus){

                case NOTLOGGED: JOptionPane.showMessageDialog(null,"USERNAME O PASSWORD ERRATI");
                    break;
                case MEDIC_LOGGED: JOptionPane.showMessageDialog(null,"LOGGATO COME MEDICO");
                                   MainClient.MedicLogged(this);
                    break;
                case NURSE_LOGGED: JOptionPane.showMessageDialog(null,"LOG-IN COME INFERMIERE");
                    break;
                case PRIMARY_LOGGED: JOptionPane.showMessageDialog(null,"LOG-IN COME PRIMARIO");
                    break;
                case ADMIN_LOGGED: JOptionPane.showMessageDialog(null,"LOG-IN COME AMMINISTRATORE");
                    break;
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


    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }




}
