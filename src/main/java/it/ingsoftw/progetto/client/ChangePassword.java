package it.ingsoftw.progetto.client;

import it.ingsoftw.progetto.common.ILogin;
import it.ingsoftw.progetto.common.utils.Password;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

public class ChangePassword extends JFrame{
    private JPasswordField oldPasswordTextField;
    private JPasswordField newPasswordTextField;
    private JPasswordField newPassword1TextField;
    private JButton cambiaPasswordButton;
    private JLabel errorLabel;
    private JPanel MainPanel;

    public ChangePassword(ILogin loginInterface)  {


        super("Cambia-Password");

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        this.setContentPane(MainPanel);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.pack();
        this.setSize(350, 450);
        this.setLocation((dim.width/2-this.getSize().width/2), (dim.height/2-this.getSize().height/2));

        this.setVisible(true);


        cambiaPasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String oldpassword = String.valueOf(oldPasswordTextField.getPassword());

                String newpassword = String.valueOf(newPasswordTextField.getPassword());
                String newpassword1 = String.valueOf(newPassword1TextField.getPassword());

                if(!newpassword.equals(newpassword1)){

                    errorLabel.setText("Errore! La password nuova e la conferma non coincidono");
                    errorLabel.setForeground(Color.RED);

                }else {

                    try {
                        if(!loginInterface.changePassword(Password.fromPassword(oldpassword),Password.fromPassword(newpassword))){

                            errorLabel.setText("Errore! La password vecchia è sbagliata\n o il server non è riuscito a cambiarla");
                            errorLabel.setForeground(Color.RED);

                        }else{

                            JOptionPane.showMessageDialog(null,"Password cambiata con successo");
                            chiudi();

                        }
                    } catch (RemoteException e1) {
                        e1.printStackTrace();
                    }

                }

            }
        });
    }

    public void chiudi(){

        this.dispose();

    }
}
