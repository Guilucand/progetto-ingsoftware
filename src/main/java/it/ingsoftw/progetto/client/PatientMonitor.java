package it.ingsoftw.progetto.client;

//import com.sun.prism.paint.Color;


import it.ingsoftw.progetto.common.AlarmCallback;
import it.ingsoftw.progetto.common.IMonitor;
import it.ingsoftw.progetto.common.IMonitorDataUpdatedCallback;
import it.ingsoftw.progetto.common.IPatient;
import it.ingsoftw.progetto.common.MonitorData;
import it.ingsoftw.progetto.common.MonitorDataUpdatedCallback;

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

    private final CardLayout cardLayout;

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
    private int roomNumber;
    private IPatient patient;
    private EmptyRoom emptyRoom;

    private JPanel mainPanel;

    final static String EMPTYROOM = "emptyRoomPanel";
    final static String PATIENTROOM = "fullRoomPanel";

    public class EmptyRoom {

        public JPanel panel;
        public JButton assignButton;

        public EmptyRoom() {
            assignButton.addActionListener(e -> addPatient());
        }
    }

    private void addPatient() {

        cardLayout.show(mainPanel, PATIENTROOM);


        patientPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        patientPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(1,1,1,1,Color.black),"Stanza "+ roomNumber,TitledBorder.TOP,TitledBorder.CENTER));
        ((javax.swing.border.TitledBorder) patientPanel.getBorder()).setTitleFont(new Font("Arial", Font.BOLD, 16));

        this.revalidate();
        this.repaint();

    }

    public PatientMonitor(int roomNumber, IPatient patient) throws RemoteException {

        this.roomNumber = roomNumber;
        this.patient = patient;

        this.mainPanel = new JPanel(cardLayout = new CardLayout());


        emptyRoom = new EmptyRoom();
        {

            emptyRoom.panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            emptyRoom.panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.black), "Stanza " + roomNumber, TitledBorder.TOP, TitledBorder.CENTER));
            ((javax.swing.border.TitledBorder) emptyRoom.panel.getBorder()).setTitleFont(new Font("Arial", Font.BOLD, 16));

            emptyRoom.panel.setBackground(new java.awt.Color(255, 255, 255));


            // Impostazione pulsante aggiunta paziente
            setImage("./img/aggiungi1.png", emptyRoom.assignButton);
            emptyRoom.assignButton.setBackground(new java.awt.Color(54, 193, 112));
        }
//        Dimension preferredDimension = new Dimension(325, 200);
//
//        emptyRoom.panel.setPreferredSize(preferredDimension);
//        patientPanel.setPreferredSize(preferredDimension);


        this.mainPanel.add(patientPanel, PATIENTROOM);
        this.mainPanel.add(emptyRoom.panel, EMPTYROOM);
        this.add(mainPanel, 0);

        //____PARAMETRI

        MonitorData data = patient.getCurrentMonitorData();

        IMonitorDataUpdatedCallback updatedCallback = new MonitorDataUpdatedCallback() {
            @Override
            public void monitorDataChanged(MonitorData data) {
                if (data != null) {
                    dbpParameter.setText(String.valueOf(data.getDbp()));
                    sbpParameter.setText(String.valueOf(data.getSbp()));
                    frequenceParameter.setText(String.valueOf(data.getBpm()));
                    temperatureParameter.setText(String.valueOf(data.getTemp()));
                }
            }
        };

        updatedCallback.monitorDataChanged(data);
        patient.setMonitorDataUpdatedCallback(updatedCallback);

        patient.setAlarmCallback(new AlarmCallback() {
            @Override
            public void startAlarm(AlarmData alarmData) throws RemoteException {
                System.out.println("Alarm started!");
            }

            @Override
            public void stopAlarm(int alarmId) throws RemoteException {
                System.out.println("Alarm stopped!");
            }
        });
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

        // Mostra una stanza vuota
        cardLayout.show( mainPanel, EMPTYROOM);
    }

    private void setImage(String pathimg, JButton assignButton) {

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
