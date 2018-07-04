package it.ingsoftw.progetto.client;

import it.ingsoftw.progetto.common.IRecovery;
import it.ingsoftw.progetto.common.IRoom;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

public class ArchiveStopAlarm extends JFrame{
    private JTextArea activityReportTextArea;
    private JPanel MainPanel;
    private JButton inviaButton;
    private JButton chiudiButton;
    IRecovery recovery;

    public ArchiveStopAlarm(IRecovery recovery) {

        super("Rapporto spegnimento allarme");


        this.recovery = recovery;
        Dimension preferredDimension = new Dimension(750, 500);
        MainPanel.setPreferredSize(preferredDimension);

        this.setContentPane(MainPanel);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.pack();

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((dim.width/2-this.getSize().width/2), (dim.height/2-this.getSize().height/2));

        this.setVisible(true);


        inviaButton.addActionListener(e -> {

            String report = activityReportTextArea.getText();

            //
            dispose();

        });

        chiudiButton.addActionListener(e -> {

            if(JOptionPane.showConfirmDialog(null,"Confermi la chiusura?") == 0){

                dispose();

            }

        });
    }
}
