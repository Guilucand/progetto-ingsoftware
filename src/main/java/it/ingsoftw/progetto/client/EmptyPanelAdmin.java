package it.ingsoftw.progetto.client;

import it.ingsoftw.progetto.common.IAdmin;
import it.ingsoftw.progetto.common.ILogin;
import it.ingsoftw.progetto.common.IRecoveryHistory;

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
    private JButton vediReportButton;
    private MonitorGUI Monitor;
    private ILogin.LoginStatus status;
    private IAdmin adminInterface;
    private IRecoveryHistory recoveryHistory;

    public EmptyPanelAdmin(MonitorGUI mgui,
                           ILogin.LoginStatus status,
                           String username,
                           IAdmin adminInterface){

        this.Monitor=mgui;
        this.status=status;
        this.adminInterface = adminInterface;

        Dimension preferredDimension = new Dimension(400, 200);

        MainPanel.setPreferredSize(preferredDimension);

        MainPanel.remove(openAdminPanel);

        logOutButton.addActionListener(e -> {

            if(JOptionPane.showConfirmDialog(null,"Conferma Log-out") == 0){

                Chiudi(mgui);

            }

        });

        vediReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                System.out.println("aperto il frame dei report");
                //new ReportFrame(status,username);
//                new PrintableReport()

            }
        });
    }


    public EmptyPanelAdmin(ILogin.LoginStatus status,
                           IAdmin adminInterface,
                           IRecoveryHistory recoveryHistory,
                           String username){

        this.status=status;
        this.adminInterface = adminInterface;
        this.recoveryHistory = recoveryHistory;

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

        vediReportButton.addActionListener(e -> {

            System.out.println("aperto il frame dei report");
            new ReportFrame(status,
                    username,
                    recoveryHistory);

        });


    }

    public JPanel getPanel(){

        return this.MainPanel;

    }

    public JButton getButton(){

        return openAdminPanel;

    }

    public JButton getButtonreport(){

        return this.vediReportButton;

    }

    public void Chiudi(MonitorGUI mgui){

        System.exit(0);

    }

}
