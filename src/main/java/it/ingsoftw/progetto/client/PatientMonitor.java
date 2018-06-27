package it.ingsoftw.progetto.client;

//import com.sun.prism.paint.Color;


import java.awt.*;
import java.awt.color.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

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
    private int room_numer;
    private EmptyRoom emptyRoom;


    public class EmptyRoom {

        public JPanel panel;
        public JButton assignButton;

        public EmptyRoom() {
            assignButton.addActionListener(e -> AddPatient());
        }
    }

    private void AddPatient() {

        this.remove(0);
        this.add(patientPanel);

        patientPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        patientPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(1,1,1,1,Color.black),"Stanza "+room_numer,TitledBorder.TOP,TitledBorder.CENTER));
        ((javax.swing.border.TitledBorder) patientPanel.getBorder()).setTitleFont(new Font("Arial", Font.BOLD, 16));

        this.revalidate();
        this.repaint();

    }


    //static Random r = new Random();

    public PatientMonitor(int room_numer) {

        this.room_numer = room_numer;
        emptyRoom = new EmptyRoom();

        emptyRoom.panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        emptyRoom.panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(1,1,1,1,Color.black),"Stanza "+room_numer,TitledBorder.TOP,TitledBorder.CENTER));
        ((javax.swing.border.TitledBorder) emptyRoom.panel.getBorder()).setTitleFont(new Font("Arial", Font.BOLD, 16));


        //emptyRoom.panel.setBackground(new java.awt.Color(255,255,255));

        String imagepath="./img/aggiungi1.png";
        InputStream imgStream = PatientMonitor.class.getResourceAsStream(imagepath);

        BufferedImage myImg=null;

        try {
            myImg = ImageIO.read(imgStream);

        } catch (IOException e) {

            e.printStackTrace();
        }

        ImageIcon icon = new ImageIcon(myImg);

        emptyRoom.assignButton.setIcon(icon);
        emptyRoom.assignButton.setBackground(new java.awt.Color(54,193,112));

        Dimension preferredDimension = new Dimension(400, 200);

        emptyRoom.panel.setPreferredSize(preferredDimension);
        patientPanel.setPreferredSize(preferredDimension);


        this.add(emptyRoom.panel, 0);

        modificaButton.addActionListener(e -> new EditPatient(this));


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
