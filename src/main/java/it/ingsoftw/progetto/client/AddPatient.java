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
    private JButton aggiungiRicoveroButton;
    private JPanel panelDate;
    private IRecoveryCreator recoveryCreator;
    private JPanel Panel1;

    public AddPatient(IRecoveryCreator recoveryCreator) {

        super("Aggiunta-ricovero");

        this.recoveryCreator = recoveryCreator;

        this.setContentPane(MainPanel);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


        Dimension preferredDimension = new Dimension(600, 400);
        this.MainPanel.setPreferredSize(preferredDimension);

        this.pack();

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((dim.width/2-this.getSize().width/2), (dim.height/2-this.getSize().height/2));

        this.setVisible(true);


        /*JDateComponentFactory jdcf = new JDateComponentFactory();

        JDatePicker jdp = jdcf.createJDatePicker();

        panelDate.add((Component) jdp,1);
        */





        aggiungiRicoveroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //recoveryCreator.

                String NomePaziente = nameTextField.getText();
                String CognomePaziente = surnameTextField.getText();
                String CodiceFiscale = cfTextField.getText();
                String LuogoNascita = locationTextField.getName();

                System.out.println(NomePaziente+" "+CognomePaziente+" "+CodiceFiscale+" "+LuogoNascita);

                //iMonitor.


            }
        });
    }
}
