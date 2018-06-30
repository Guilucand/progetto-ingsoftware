package it.ingsoftw.progetto.client;

import it.ingsoftw.progetto.common.IMonitor;
import it.ingsoftw.progetto.common.IPatient;
import it.ingsoftw.progetto.common.IRecoveryCreator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddPatient extends JFrame{
    private JPanel MainPanel;
    private JFormattedTextField cfTextField;
    private JTextField nameTextField;
    private JTextField locationTextField;
    private JTextField surnameTextField;
    private JTextField dateTextField;
    private JButton aggiungiRicoveroButton;
    private JPanel panelDate;
    private IRecoveryCreator recoveryCreator;

    public AddPatient(IRecoveryCreator recoveryCreator) {
        this.recoveryCreator = recoveryCreator;


        /*JDateComponentFactory jdcf = new JDateComponentFactory();

        JDatePicker jdp = jdcf.createJDatePicker();

        panelDate.add((Component) jdp,1);
        */





        aggiungiRicoveroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //recoveryCreator.

            }
        });
    }
}
