package it.ingsoftw.progetto.client;

import java.awt.Dimension;
import java.util.Random;

import javax.swing.*;

public class PatientMonitor extends JPanel{
    private JPanel patientPanel;
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

    private EmptyRoom emptyRoom;



    public class EmptyRoom {
        public JPanel panel;
        public JLabel roomNameLabel;
        public JButton assignButton;
    }


    static Random r = new Random();
    public PatientMonitor() {
        emptyRoom = new EmptyRoom();
        modificaButton.addActionListener(e -> EditaPaz());

        Dimension preferredDimension = new Dimension(400, 200);

        emptyRoom.panel.setPreferredSize(preferredDimension);
        patientPanel.setPreferredSize(preferredDimension);

        if (r.nextBoolean())
            this.add(emptyRoom.panel, 0);
        else
            this.add(patientPanel, 0);
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
