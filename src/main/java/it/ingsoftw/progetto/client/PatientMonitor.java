package it.ingsoftw.progetto.client;

import it.ingsoftw.progetto.common.*;
import it.ingsoftw.progetto.common.messages.*;
import it.ingsoftw.progetto.common.messages.persistent.AlarmStartMessage;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.HashMap;

public class PatientMonitor extends JPanel {

    private final CardLayout cardLayout;

    private JPanel patientPanel;
    private JLabel image;
    private JLabel nomePaziente;
    private JLabel cognomePaziente;
    private JLabel cognome;
    private JLabel nome;
    private JLabel sbpParameter;
    private JLabel dbpParameter;
    private JLabel frequenceParameter;
    private JLabel temperatureParameter;

    private JLabel sbpLabel;
    private JLabel dbpLabel;
    private JLabel freqLabel;
    private JLabel temperatureLabel;


    private JButton modificaButton;
    private JLabel alarmLabel;
    private JLabel timerLabel;
    private JButton prescriviButton;
    private int roomNumber;
    private final ILogin.LoginStatus loginStatus;
    private IRecovery recovery;
    private EmptyRoom emptyRoom;
    private HashMap<Integer, AlarmStartMessage> alarmList;
    private JPopupMenu pop;
    private JPopupMenu alarmPopMenu;
    private JPanel mainPanel;
    private IRoom room;
    private Timer coutdownTimer;
    private boolean showpop;

    private Storico historyWindow;

    final static String EMPTYROOM = "emptyRoomPanel";
    final static String PATIENTROOM = "fullRoomPanel";

    public class EmptyRoom {

        public JPanel panel;
        public JButton assignButton;

        public EmptyRoom() {

            assignButton.addActionListener(e -> {
                try {
                    new AddPatient(room.addRecovery());
                } catch (RemoteException e1) {
                    e1.printStackTrace();
                }
            });
        }
    }

    private void updateAlarmImage() {
        Collection<AlarmStartMessage> alarms = alarmList.values();

        AlarmLevel maxAlarmLevel = AlarmLevel.NoAlarm;

        for (AlarmStartMessage alarmDesc : alarms) {
            maxAlarmLevel = AlarmLevel.getMaximum(maxAlarmLevel, alarmDesc.getAlarmData().getLevel());
        }

        switch (maxAlarmLevel) {
            case Level1:
                setImage("./img/alertPatientGreen.png", alarmLabel);
                break;
            case Level2:
                setImage("./img/alertPatientOrange.png", alarmLabel);
                break;
            case Level3:
                setImage("./img/alertPatientRed.png", alarmLabel);
                break;
            case NoAlarm:
                setImage("./img/alarmPaper.png", alarmLabel);
        }
    }

    private void updateVsData(MonitorData data) {

        // Aggiornamento parametri vitali
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

    private void startAlarm(AlarmStartMessage alarmData) {

        System.out.println("Alarm started!");

        AlarmSound.getInstance().startAlarmSound(alarmData.getAlarmData());

        alarmList.put(alarmData.getAlarmData().getAlarmId(), alarmData);

        updateAlarmImage();
    }


    private void stopAlarm(AlarmStopMessage alarmData) {
        AlarmSound.getInstance().stopAlarmSound(alarmData.getAlarmData().getAlarmId());
        alarmList.remove(alarmData.getAlarmData().getAlarmId());
        updateAlarmImage();
    }

    public void processMessage(MessageObject message) {
        System.out.println(message.getMessageType());

        switch (message.getMessageType()) {
            case DimissionMessage
                    .CONSTRUCTOR:
                updatePatient();
                break;

            case MonitorDataChangedMessage
                    .CONSTRUCTOR:
                updateVsData(((MonitorDataChangedMessage) message).getData());
                break;

            case AlarmStartMessage
                    .CONSTRUCTOR:
                startAlarm(((AlarmStartMessage) message));
                break;

            case AlarmStopMessage
                    .CONSTRUCTOR:
                stopAlarm(((AlarmStopMessage) message));
            break;

            case PatientAddedMessage
                    .CONSTRUCTOR:

                try {
                    if (room.hasRecovery()) {
                        recovery = room.getCurrentRecovery();
                        setupPatient();
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

                break;
        }
    }

    private void setupPatient() throws RemoteException {

//                        case AddDiagnosisMessage.CONSTRUCTOR: setImage("./img/diagnosis.png",alarmLabel);; break;
//                        case AddStopMachineMessage.CONSTRUCTOR :setImage("./img/alarmPaper.png",alarmLabel);;break;

        MonitorData data = recovery.getCurrentMonitorData();

        cardLayout.show(mainPanel, PATIENTROOM);


        patientPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        patientPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.black), "Stanza " + roomNumber, TitledBorder.TOP, TitledBorder.CENTER));
        ((javax.swing.border.TitledBorder) patientPanel.getBorder()).setTitleFont(new Font("Arial", Font.BOLD, 16));

        // Imposto inizialmente i dati di monitoraggio
        updateVsData(recovery.getCurrentMonitorData());

        // DATA
        PatientData patientData = recovery.getPatientData();

        if (patientData != null) {
            nome.setText(patientData.getName());
            cognome.setText(patientData.getSurname());
        }

        this.revalidate();
        this.repaint();
    }

    public PatientMonitor(int roomNumber, IRoom room, ILogin.LoginStatus loginStatus, String user) throws RemoteException {

        this.roomNumber = roomNumber;
        this.loginStatus = loginStatus;

        this.room = room;

        this.alarmList = new HashMap<>();

        this.mainPanel = new JPanel(cardLayout = new CardLayout());

        this.alarmPopMenu = new JPopupMenu();

        this.coutdownTimer = new Timer(200, (e) -> {

            int minimumTimer = Integer.MAX_VALUE;
            for (AlarmStartMessage alarms : alarmList.values()) {
                minimumTimer = Math.min(minimumTimer, alarms.getAlarmData().getSecondsLeft());
            }

            if (minimumTimer == Integer.MAX_VALUE) {
                timerLabel.setText("");
                timerLabel.setForeground(Color.black);
            } else {
                timerLabel.setText(String.valueOf(minimumTimer));
                if (minimumTimer > 0)
                    timerLabel.setForeground(Color.BLUE);
                else
                    timerLabel.setForeground(Color.RED);
            }

        });

        this.coutdownTimer.start();

        if (loginStatus == ILogin.LoginStatus.NURSE_LOGGED) {
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
        Dimension preferredDimension = new Dimension(400, 250);

        emptyRoom.panel.setPreferredSize(preferredDimension);
        patientPanel.setPreferredSize(preferredDimension);



        this.mainPanel.add(patientPanel, PATIENTROOM);
        this.mainPanel.add(emptyRoom.panel, EMPTYROOM);
        this.add(mainPanel, 0);

        //____LISTENER

        modificaButton.addActionListener(e -> new EditPatient(this));


        image.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                if (e.getButton() == MouseEvent.BUTTON1) {
                    try {
                        historyWindow = new Storico(room, loginStatus, user);
                        historyWindow.updateVsData(recovery.getCurrentMonitorData());
                    } catch (RemoteException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

        updatePatient();

        prescriviButton.addActionListener(e -> {

            switch (loginStatus) {
                case NURSE_LOGGED:
                    new DoseDrug(recovery);
                    break;

                case MEDIC_LOGGED:
                case ADMIN_LOGGED:
                case PRIMARY_LOGGED:
                    new PrescriveDrug(recovery);
                    break;
            }
        });


        alarmLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                System.out.println("lista allarm vuota? " + alarmList.isEmpty() + " tipo utente loggato " + loginStatus);

                if (e.getButton() == MouseEvent.BUTTON1 && alarmList.isEmpty() && loginStatus != ILogin.LoginStatus.NURSE_LOGGED) {

                    buildPopup();
                    alarmPopMenu.show(alarmLabel, alarmLabel.getWidth() - (alarmLabel.getWidth() / 2), alarmLabel.getHeight() - (alarmLabel.getHeight() / 2));

                }
            }
        });
    }

    private void updatePatient() {

        try {
            if (room.hasRecovery())
                this.recovery = room.getCurrentRecovery();
            else
                this.recovery = null;

            // Mostra la stanza vuota o il paziente a seconda dello stato
            if (recovery == null)
                cardLayout.show(mainPanel, EMPTYROOM);
            else
                setupPatient();

        } catch (RemoteException e) {
            this.recovery = null;
        }
    }


    private void buildPopup() {

//        pop.removeAll();
//        pop.add(new JMenuItem("Ciao"));

        alarmPopMenu.removeAll();
        for (AlarmStartMessage alarmMessage : alarmList.values()) {
            JMenuItem nlabel = new JMenuItem(alarmMessage.getMessageText());
            alarmPopMenu.add(nlabel);
        }

//
//                case DimissionMessage.CONSTRUCTOR:
//
//                    System.out.println("constructor");
//
//                    break;
//
//                case AddDiagnosisMessage.CONSTRUCTOR:
//
//
//                    nlabel.addActionListener(new ActionListener() {
//                    @Override
//                    public void actionPerformed(ActionEvent e) {
//
//                        new AddDiagnosis(patient);
//
//                    }
//                });
//            System.out.println("Aggiungo " + ad.getLevel() + " a pop");
//            JLabel element = new JLabel(String.valueOf(ad.getLevel()));
//            pop.add(element);

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
