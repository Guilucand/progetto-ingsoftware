package it.ingsoftw.progetto.client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
            //JOptionPane.showMessageDialog(null,"effettuo il login");
        });

        forgotPasswordButton.addActionListener(e -> {

            password = String.valueOf(passwordPasswordField.getPassword());
            //JOptionPane.showMessageDialog(null,"ops.... password dimenticata");
        });

        // Inizializzo qui la finestra
        this.setContentPane(MainPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
    }


    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }


    public static void main(String[] args) {

        System.out.println("entro nel main");

        JFrame frame = new clientGUI();
        frame.setVisible(true);
    }


}
