package it.ingsoftw.progetto.client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PatientMonitor extends JPanel{
    private JPanel MainPanel;
    private JLabel image;
    private JLabel nomePaziente;
    private JLabel cognomePaziente;
    private JLabel cognome;
    private JLabel nome;
    private JLabel sbp;
    private JLabel dbp;
    private JLabel sbpParameter;
    private JLabel dbpParameter;
    private JLabel frequenceParameter;
    private JLabel tempParameter;
    private JLabel frequenza;
    private JLabel temperatura;
    private JButton modificaButton;


    public PatientMonitor() {

        modificaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                EditaPaz();

            }
        });
    }


    private void EditaPaz(){

        new EditPatient(this);

    }

    public void setName(String modname){

        this.nomePaziente.setText(modname);

    }

    public void setSurname(String modsurname){

        this.cognomePaziente.setText(modsurname);

    }

    public void setPath(String modpath){

        ImageIcon image = new ImageIcon(getClass().getResource(modpath));
        this.image.setIcon(image);

    }


}
