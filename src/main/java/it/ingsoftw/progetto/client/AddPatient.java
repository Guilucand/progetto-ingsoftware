package it.ingsoftw.progetto.client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddPatient extends JPanel{
    private JPanel MainPanel;
    private JButton addButton;

    public AddPatient(){

        
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                AddPatientMonitor();

            }
        });
    }


    public void AddPatientMonitor(){

        MainPanel.remove(addButton);
        MainPanel.add(new PatientMonitor());

    }

}
