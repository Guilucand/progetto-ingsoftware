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


        logInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                username = usernameTextField.getText();
                //JOptionPane.showMessageDialog(null,"effettuo il login");

            }
        });

        forgotPasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                password = String.valueOf(passwordPasswordField.getPassword());
                //JOptionPane.showMessageDialog(null,"ops.... password dimenticata");
            }
        });
        
    }


    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }


    public static void main(String[] args) {

        System.out.println("entro nel main");

        JFrame frame = new JFrame("Log-in");

        frame.setContentPane(new clientGUI().MainPanel);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.pack();

        frame.setVisible(true);



    }


}
