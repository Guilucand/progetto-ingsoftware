package it.ingsoftw.progetto.client;

import it.ingsoftw.progetto.common.IAdmin;
import it.ingsoftw.progetto.common.ILogin;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

public class EmptyPanelAdmin extends JPanel{

    private JButton openAdminPanel;
    private JPanel MainPanel;
    private JButton logOutButton;
    private MonitorGUI Monitor;
    private ILogin.LoginStatus status;
    private IAdmin adminInterface;

    public EmptyPanelAdmin(MonitorGUI mgui , ILogin.LoginStatus status , IAdmin adminInterface){

        this.Monitor=mgui;
        this.status=status;
        this.adminInterface = adminInterface;

        Dimension preferredDimension = new Dimension(400, 200);

        MainPanel.setPreferredSize(preferredDimension);

        MainPanel.remove(openAdminPanel);

        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(JOptionPane.showConfirmDialog(null,"Conferma Log-out") == 0){

                    Chiudi(mgui);

                }

            }
        });
    }


    public EmptyPanelAdmin(ILogin.LoginStatus status,IAdmin adminInterface){

        this.status=status;
        this.adminInterface = adminInterface;

        Dimension preferredDimension = new Dimension(400, 200);

        MainPanel.setPreferredSize(preferredDimension);

        MainPanel.remove(logOutButton);

        openAdminPanel.addActionListener(e -> {
            try {
                new AdminPanel(status,adminInterface);
            } catch (RemoteException e1) {
                e1.printStackTrace();
            }
        });


    }

    public JPanel getPanel(){

        return this.MainPanel;

    }

    public JButton getButton(){

        return openAdminPanel;

    }

    public void Chiudi(MonitorGUI mgui){

        mgui.dispose();

    }

}
