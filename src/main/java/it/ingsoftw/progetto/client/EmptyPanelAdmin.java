package it.ingsoftw.progetto.client;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EmptyPanelAdmin extends JPanel{

    private JButton openAdminPanel;
    private JPanel MainPanel;
    private JButton logOutButton;

    public EmptyPanelAdmin( MonitorGUI mgui){

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


    public EmptyPanelAdmin(){

        Dimension preferredDimension = new Dimension(400, 200);

        MainPanel.setPreferredSize(preferredDimension);

        MainPanel.remove(logOutButton);

        openAdminPanel.addActionListener(e -> new AdminPanel());


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
