package it.ingsoftw.progetto.client;

//import com.sun.prism.paint.Color;


import it.ingsoftw.progetto.common.IMonitor;
import it.ingsoftw.progetto.common.IPatient;
import it.ingsoftw.progetto.common.MonitorData;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;

public class PatientMonitor extends JPanel{

    public void putSbpParameter(int sbpParameter) {this.sbpParameter.setText(String.valueOf(sbpParameter));}


    public void putDbpParameter(int dbpParameter) {this.dbpParameter.setText(String.valueOf(dbpParameter));}

    public void putFrequenceParameter(int frequenceParameter) {this.frequenceParameter.setText(String.valueOf(frequenceParameter));}

    public void putTempParameter(float tempParameter) {this.temperatureParameter.setText(String.valueOf(tempParameter));}

    private JPanel patientPanel;
    private JLabel image;
    private JLabel nomePaziente;
    private JLabel cognomePaziente;
    private JLabel cognome;
    private JLabel nome;
    private JLabel sbpLabel;
    private JLabel dbpLabel;
    private JLabel sbpParameter;
    private JLabel dbpParameter;
    private JLabel frequenceParameter;
    private JLabel temperatureParameter;
    private JLabel freqLabel;
    private JLabel temperatureLabel;
    private JButton modificaButton;
    private JLabel alarmLabel;
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

    public PatientMonitor(int room_numer, IMonitor iMonitorInterface) throws RemoteException {

        this.room_numer = room_numer;
        emptyRoom = new EmptyRoom();

        emptyRoom.panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        emptyRoom.panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(1,1,1,1,Color.black),"Stanza "+room_numer,TitledBorder.TOP,TitledBorder.CENTER));
        ((javax.swing.border.TitledBorder) emptyRoom.panel.getBorder()).setTitleFont(new Font("Arial", Font.BOLD, 16));

        emptyRoom.panel.setBackground(new java.awt.Color(255,255,255));


        SetImage("./img/aggiungi1.png",emptyRoom.assignButton);

        emptyRoom.assignButton.setBackground(new java.awt.Color(54,193,112));

        Dimension preferredDimension = new Dimension(400, 200);

        emptyRoom.panel.setPreferredSize(preferredDimension);
        patientPanel.setPreferredSize(preferredDimension);


        this.add(emptyRoom.panel, 0);

        //____PARAMETRI

        IPatient Paziente = iMonitorInterface.getPatientByRoomNumber(room_numer);
        MonitorData Data = Paziente.getCurrentMonitorData();

        this.dbpParameter.setText(String.valueOf(Data.getDbp()));
        this.sbpParameter.setText(String.valueOf(Data.getSbp()));
        this.frequenceParameter.setText(String.valueOf(Data.getBpm()));
        this.temperatureParameter.setText(String.valueOf(Data.getTemp()));


        //____LISTENER

        modificaButton.addActionListener(e -> new EditPatient(this));


        image.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                if (e.getButton() == MouseEvent.BUTTON1){
                    new Storico();
                }
            }
        });
    }

    private void SetImage(String pathimg, JButton assignButton) {

        InputStream imgStream = PatientMonitor.class.getResourceAsStream(pathimg);

        BufferedImage myImg=null;

        try {
            myImg = ImageIO.read(imgStream);

        } catch (IOException e) {

            e.printStackTrace();
        }

        ImageIcon icon = new ImageIcon(myImg);


        assignButton.setIcon(icon);

    }


    public void setName(String modname){

        this.nomePaziente.setText(modname);

    }

    public void setSurname(String modsurname){

        this.cognomePaziente.setText(modsurname);

    }


}
