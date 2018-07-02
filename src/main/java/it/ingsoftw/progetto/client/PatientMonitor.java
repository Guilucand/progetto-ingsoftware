package it.ingsoftw.progetto.client;

//import com.sun.prism.paint.Color;


import it.ingsoftw.progetto.common.*;
import it.ingsoftw.progetto.common.messages.DimissionMessage;
import it.ingsoftw.progetto.common.messages.MessageObject;
import it.ingsoftw.progetto.common.messages.MessagesChangedCallback;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.sound.sampled.LineUnavailableException;
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
    private JLabel timerLabel;
    private JButton prescriviButton;
    private int roomNumber;
    private final ILogin loginInterface;
    private IRecovery patient;
    private EmptyRoom emptyRoom;
    private IAlarmCallback.AlarmData alarmData;
    private HashMap<IAlarmCallback.AlarmData,Timer> alarmList;
    private JPopupMenu pop;
    private JPanel mainPanel;
    private Thread sound;
    private IRoom room;
    private boolean showpop = true;
    private int temporimanente;

    private Storico historyWindow;

    final static String EMPTYROOM = "emptyRoomPanel";
    final static String PATIENTROOM = "fullRoomPanel";

    public class EmptyRoom {

        public JPanel panel;
        public JButton assignButton;

        public EmptyRoom() {

            assignButton.addActionListener(e -> {
                try {
                    new AddPatient(room.addRecovery(), ()-> {
                        if (room.hasRecovery()) {
                            patient = room.getCurrentRecovery();
                            setupPatient();
                        }
                    });
                } catch (RemoteException e1) {
                    e1.printStackTrace();
                }
            });
        }
    }

    private void setupPatient() throws RemoteException {

        //____PARAMETRI VITALI IN DIRETTA

        MonitorData data = patient.getCurrentMonitorData();


        // classe di observer
        IMonitorDataUpdatedCallback updatedCallback = new MonitorDataUpdatedCallback() {
            @Override
            public void monitorDataChanged(MonitorData data) {
                if (data != null) {
                    dbpParameter.setText(String.valueOf(data.getDbp()));
                    sbpParameter.setText(String.valueOf(data.getSbp()));
                    frequenceParameter.setText(String.valueOf(data.getBpm()));
                    temperatureParameter.setText(String.valueOf(data.getTemp()));
                    if (historyWindow != null && historyWindow.isVisible()) {
                        historyWindow.updateVsData(data);
                    }
                }
            }
        };

        updatedCallback.monitorDataChanged(data);
        patient.setMonitorDataUpdatedCallback(updatedCallback);


        //OBSERVER

        patient.setAlarmCallback(new AlarmCallback() {
            @Override
            public synchronized void startAlarm(AlarmData alarmData) throws RemoteException {

                System.out.println("Alarm started!");

                int count = 0;

                if(alarmData.getLevel() == AlarmLevel.Level1) count = 180;
                else if (alarmData.getLevel() == AlarmLevel.Level2) count = 120;
                else if (alarmData.getLevel() == AlarmLevel.Level3) count = 60;

                TimeClass tc = new TimeClass(count);

                Timer t = new Timer(1000,tc);

                t.start();

                if(alarmList.isEmpty()){

                    sound = new Thread(new Runnable(){

                        public void run(){
                            SoundUtils alarmtone = new SoundUtils();
                            try {

                                while(alarmList.size() > 0) {

                                    alarmtone.tone(500, 500);
                                    alarmtone.tone(1000, 500);
                                    alarmtone.tone(0, 500);

                                }
                            } catch (LineUnavailableException e1) {
                                e1.printStackTrace();
                            }
                        }

                    });

                    sound.start();
                }

                alarmList.put(alarmData,t);

                Set<AlarmData> keyset = alarmList.keySet();

                boolean lvl1 = false;
                boolean lvl2 = false;
                boolean lvl3 = false;

                for(AlarmData ad : keyset){

                    if (ad.getLevel() == AlarmLevel.Level1){lvl1 = true;}
                    else if (ad.getLevel() == AlarmLevel.Level2){lvl2 = true;}
                    else if (ad.getLevel() == AlarmLevel.Level3){lvl3 = true;}


                }

                if(lvl3) setImage("./img/alertPatientRed.png",alarmLabel);
                else if(lvl2) setImage("./img/alertPatientOrange.png",alarmLabel);
                else if(lvl1) setImage("./img/alertPatientGreen.png",alarmLabel);

            }

            @Override
            public synchronized void stopAlarm(int alarmId) throws RemoteException {


                Set<AlarmData> keyset = alarmList.keySet();

                for(AlarmData ad : keyset){

                    if (ad.getAlarmId() == alarmId ) {

                        alarmList.get(ad).stop();
                        alarmList.remove(ad);

                        temporimanente=Integer.parseInt(timerLabel.getText());
                        timerLabel.setText("");

                        //Aggiungere alla lista delle cose da fare un parametro che è completare lo spegnimento da parte del medico

                        break;

                    }

                }

                setImage("./img/alarmPaper.png",alarmLabel);
                timerLabel.setText("");
                timerLabel.setForeground(Color.black);

            }
        });



        // MESSAGES

        patient.setMessagesChangedCallback(new MessagesChangedCallback() {
            @Override
            public void messagesChanged() throws RemoteException {
                System.out.println("Current messages: " + roomNumber);
                for (MessageObject obj : patient.getMessages()) {
                    System.out.println(obj.getMessageType());

                    switch (obj.getMessageType()) {
                        case DimissionMessage
                                .CONSTRUCTOR:
                            updatePatient();

                    }

                }
            }
        });



        cardLayout.show(mainPanel, PATIENTROOM);


        patientPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        patientPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(1,1,1,1,Color.black),"Stanza "+ roomNumber,TitledBorder.TOP,TitledBorder.CENTER));
        ((javax.swing.border.TitledBorder) patientPanel.getBorder()).setTitleFont(new Font("Arial", Font.BOLD, 16));

        // DATA
        PatientData patientData = patient.getPatientData();

        if (patientData != null) {
            nome.setText(patientData.getName());
            cognome.setText(patientData.getSurname());
        }

        this.revalidate();
        this.repaint();
    }

    public PatientMonitor(int roomNumber, IRoom room, ILogin loginInterface, String user) throws RemoteException {

        this.roomNumber = roomNumber;
        this.loginInterface = loginInterface;

        this.room = room;

        this.alarmList = new HashMap<>();

        this.mainPanel = new JPanel(cardLayout = new CardLayout());

        this.pop = new JPopupMenu();

        if(loginInterface.isLogged() == ILogin.LoginStatus.NURSE_LOGGED) {

            prescriviButton.setText("Somministra");

        }

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

        //____LISTENER

        modificaButton.addActionListener(e -> new EditPatient(this));


        image.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                if (e.getButton() == MouseEvent.BUTTON1){
                    try {
                        historyWindow = new Storico(room, loginInterface.isLogged(), user);
                        historyWindow.updateVsData(patient.getCurrentMonitorData());
                    } catch (RemoteException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

        updatePatient();

        boolean b = true;

        alarmLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);

                System.out.println("mouse entrato");

                if(showpop){

                    addPopup();
                    pop.show(alarmLabel,alarmLabel.getWidth()-(alarmLabel.getWidth()/2),alarmLabel.getHeight()-(alarmLabel.getHeight()/2));
                    showpop = false;

                }
            }
        });

        alarmLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);

                if(!showpop) {
                    pop.setVisible(false);
                    showpop = true;
                }
            }
        });
        prescriviButton.addActionListener(e -> {

            ILogin.LoginStatus status = ILogin.LoginStatus.NOTLOGGED;
            try {
                status = loginInterface.isLogged();
            } catch (RemoteException e1) {
                e1.printStackTrace();
            }

            switch (status) {
                case NURSE_LOGGED:
                    try {
                        new DoseDrug(patient,loginInterface.getLoggedUser());
                    } catch (RemoteException e1) {
                        e1.printStackTrace();
                    }
                    break;

                case MEDIC_LOGGED:
                case ADMIN_LOGGED:
                case PRIMARY_LOGGED:
                    try {
                        new PrescriveDrug(loginInterface.getLoggedUser(), patient);
                    } catch (RemoteException e1) {
                        e1.printStackTrace();
                    }
                    break;
            }
        });
    }

    private void updatePatient() {

        try {
            if (room.hasRecovery())
                this.patient = room.getCurrentRecovery();
            else
                this.patient = null;

        // Mostra la stanza vuota o il paziente a seconda dello stato
        if (patient == null)
            cardLayout.show( mainPanel, EMPTYROOM);
        else
            setupPatient();

        } catch (RemoteException e) {
            this.patient = null;
        }
    }


    private synchronized void addPopup(){

        Set<IAlarmCallback.AlarmData> keyset = alarmList.keySet();

        pop.removeAll();

        for(IAlarmCallback.AlarmData ad : keyset){

            System.out.println("aggiungo "+ad.getLevel()+" a pop" );
            JLabel elemento = new JLabel(""+ad.getLevel());
            pop.add(elemento);

        }



    }

    public class TimeClass implements ActionListener {

        int counter;

        public TimeClass(int counter){

            this.counter = counter;

        }

        public int getCounter(){

            return this.counter;

        }

        @Override
        public synchronized void actionPerformed(ActionEvent tc) {

            counter--;

            /*int mincounter;

            Set<IAlarmCallback.AlarmData> keyset = alarmList.keySet();

            IAlarmCallback.AlarmData ad_min = null;

            for(IAlarmCallback.AlarmData ad : keyset){

                if(ad_min == null){ad_min = ad;}
                else if(ad.getStartTime().getNano() < ad_min.getStartTime().getNano()){ad_min = ad;}

            }


            ActionListener al[] = alarmList.get(ad_min).getActionListeners();

            TimeClass tc1 = (TimeClass)al[0];

            mincounter = tc1.getCounter();
            */

            if(counter > 0){

                timerLabel.setText(""+counter);
                timerLabel.setForeground(Color.BLUE);

            }else{


                timerLabel.setText(""+counter);
                timerLabel.setForeground(Color.RED);

            }

        }
    }

    private void setImage(String pathimg, Object componente) {

        InputStream imgStream = PatientMonitor.class.getResourceAsStream(pathimg);

        BufferedImage myImg=null;

        try {
            myImg = ImageIO.read(imgStream);

        } catch (IOException e) {

            e.printStackTrace();
        }

        ImageIcon icon = new ImageIcon(myImg);

        if(componente instanceof JLabel)((JLabel)componente).setIcon(icon);
        if(componente instanceof JButton)((JButton)componente).setIcon(icon);
    }

    public void setName(String modname){
        this.nomePaziente.setText(modname);
    }

    public void setSurname(String modsurname){
        this.cognomePaziente.setText(modsurname);
    }
}
