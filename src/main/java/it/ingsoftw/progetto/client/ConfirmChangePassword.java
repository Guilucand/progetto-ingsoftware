package it.ingsoftw.progetto.client;

import it.ingsoftw.progetto.common.ILogin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.rmi.RemoteException;

public class ConfirmChangePassword extends JFrame{

    private JPanel MainPanel;
    private JButton cancelConfirm;
    private JButton Confirm;
    private JLabel confirmLabel;
    private JTextField MailTextField;
    private JLabel mailLabel;
    private ILogin loginInterface;


    public ConfirmChangePassword(ILogin loginInterface) {

        this.loginInterface=loginInterface;



        this.setContentPane(MainPanel);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.pack();

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((dim.width/2-this.getSize().width/2), (dim.height/2-this.getSize().height/2));

        this.setVisible(true);

        cancelConfirm.addActionListener(e ->this.dispose());
        Confirm.addActionListener(e -> TryChange());


        MailTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if(e.getKeyCode() == KeyEvent.VK_ENTER) Confirm.requestFocusInWindow();
                else if (e.getKeyCode() == KeyEvent.VK_DOWN) cancelConfirm.requestFocusInWindow();
            }
        });
        Confirm.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if(e.getKeyCode() == KeyEvent.VK_ENTER ) TryChange();
                else if (e.getKeyCode() == KeyEvent.VK_LEFT) cancelConfirm.requestFocusInWindow();
                else if (e.getKeyCode() == KeyEvent.VK_UP) MailTextField.requestFocusInWindow();
            }
        });

        cancelConfirm.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if(e.getKeyCode() == KeyEvent.VK_RIGHT ) Confirm.requestFocusInWindow();
                else if (e.getKeyCode() == KeyEvent.VK_UP) MailTextField.requestFocusInWindow();
                else if (e.getKeyCode() == KeyEvent.VK_ENTER) Chiudi();
            }
        });


    }

    private void Chiudi() {

        this.dispose();

    }

    private void TryChange(){

        String email = MailTextField.getText();

        try {
            if(this.loginInterface.passwordForgotten(email) == true){

                JOptionPane.showMessageDialog(null,"La nuova password le è stata inviata per mail");
                Chiudi();

            }else{

                JOptionPane.showMessageDialog(null,"mail non valida, o non associata ad alcun account.  Riprovare");

            }
        } catch (RemoteException e1) {
            e1.printStackTrace();
        }


    }


}
