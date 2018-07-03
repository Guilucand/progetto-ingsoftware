package it.ingsoftw.progetto.client;

import it.ingsoftw.progetto.common.IRecovery;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

public class AddDiagnosis extends JFrame{
    private JTextArea diagnosisTextArea;
    private JButton inviaButton;
    private JPanel mainPanel;
    private JButton chiudiButton;


    public AddDiagnosis(IRecovery paziente ) {


        super("Diagnosi ingresso");


        Dimension preferredDimension = new Dimension(600, 400);
        mainPanel.setPreferredSize(preferredDimension);

        this.setContentPane(mainPanel);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.pack();

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((dim.width/2-this.getSize().width/2), (dim.height/2-this.getSize().height/2));

        this.setVisible(true);

        inviaButton.addActionListener(e -> {

            try {
                paziente.addDiagnosis(diagnosisTextArea.getText());
            } catch (RemoteException e1) {
                e1.printStackTrace();
            }
            dispose();

        });

        chiudiButton.addActionListener(e -> {

            if(JOptionPane.showConfirmDialog(null,"Confermi la chiusura?") == 0){

                dispose();

            }

        });
    }
}
