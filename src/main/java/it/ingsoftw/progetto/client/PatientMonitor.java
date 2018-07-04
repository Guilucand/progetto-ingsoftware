package it.ingsoftw.progetto.client;

import it.ingsoftw.progetto.common.*;
import it.ingsoftw.progetto.common.messages.*;
import it.ingsoftw.progetto.common.messages.persistent.AlarmStartMessage;
import it.ingsoftw.progetto.common.messages.persistent.RequestDiagnosisMessage;
import it.ingsoftw.progetto.common.messages.persistent.RequestStoppedAlarmReportMessage;

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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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


    private JLabel alarmLabel;
    private JLabel timerLabel;
    private JButton prescriviButton;
    private int roomNumber;
    private final ILogin.LoginStatus loginStatus;
    private IRecovery recovery;
    private EmptyRoom emptyRoom;
    private HashMap<Integer, AlarmStartMessage> alarmList;
    private JPopupMenu alarmPopMenu;
    private JPopupMenu messagePopMenu;
    private JPanel mainPanel;
    private IRoom room;
    private Timer coutdownTimer;
    private boolean showpop = false;

    private Storico historyWindow;
    private IRecoveryHistory recoveryHistory;
    private static JMenuItem diagnosisMsg;
    private Map<Integer, JMenuItem> stopMachineMessage;


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

            case DimissionMessage.CONSTRUCTOR:
                updatePatient();
                break;

            case MonitorDataChangedMessage.CONSTRUCTOR:
                updateVsData(((MonitorDataChangedMessage) message).getData());
                break;

            case AlarmStartMessage.CONSTRUCTOR:
                startAlarm(((AlarmStartMessage) message));
                break;

            case AlarmStopMessage.CONSTRUCTOR:
                stopAlarm(((AlarmStopMessage) message));
                break;

            case PatientAddedMessage.CONSTRUCTOR:

                try {
                    if (room.hasRecovery()) {
                        recovery = room.getCurrentRecovery();
                        setupPatient();
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

                break;

            case RequestDiagnosisMessage.CONSTRUCTOR:

                addToPopup(message);
                updateImagealarmLabel();

                break;

            case AddedDiagnosisMessage.CONSTRUCTOR:

                removeFromPopup(message);
                updateImagealarmLabel();
                break;

            case RequestStoppedAlarmReportMessage.CONSTRUCTOR:

                addToPopup(message);
                updateImagealarmLabel();

                break;

            case AddedStopAlarmMessage.CONSTRUCTOR:

                removeFromPopup(message);
                updateImagealarmLabel();

                break;
        }
    }

    private void removeFromPopup(MessageObject message) {

        switch (message.getMessageType()) {
            case AddedDiagnosisMessage.CONSTRUCTOR:
                messagePopMenu.remove(diagnosisMsg);
                diagnosisMsg = null;
                break;
            case AddedStopAlarmMessage.CONSTRUCTOR:
                Integer alarmId = ((AddedStopAlarmMessage) message).getAlarmData().getAlarmId();
                messagePopMenu.remove(stopMachineMessage.get(alarmId));
                stopMachineMessage.remove(alarmId);
        }


    }

    private void updateImagealarmLabel() {

        if (diagnosisMsg != null)
            setImage("./img/diagnosis.png", alarmLabel);
        else if (stopMachineMessage.size() > 0)
            setImage("./img/alarmPaper.png", alarmLabel);
        else
            setImage(null, alarmLabel);
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

    public PatientMonitor(int roomNumber, IRoom room, IRecoveryHistory recoveryHistory, String user, ILogin.LoginStatus loginStatus) throws RemoteException {

        this.roomNumber = roomNumber;
        this.recoveryHistory = recoveryHistory;
        this.loginStatus = loginStatus;

        this.stopMachineMessage = new HashMap<>();

        this.room = room;

        this.alarmList = new HashMap<>();

        this.mainPanel = new JPanel(cardLayout = new CardLayout());

        this.alarmPopMenu = new JPopupMenu();
        this.messagePopMenu = new JPopupMenu();

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


        image.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                if (e.getButton() == MouseEvent.BUTTON1) {
                    try {
                        historyWindow = new Storico(recovery, PatientMonitor.this.recoveryHistory, loginStatus, user);
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

                if (e.getButton() == MouseEvent.BUTTON1 && alarmList.isEmpty() && loginStatus != ILogin.LoginStatus.NURSE_LOGGED) {

                    messagePopMenu.show(alarmLabel, alarmLabel.getWidth() - (alarmLabel.getWidth() / 2), alarmLabel.getHeight() - (alarmLabel.getHeight() / 2));

                }
            }
        });

        alarmLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);


                if(!alarmList.isEmpty()){

                    if(showpop){buildAlarmPopup();showpop = false;}
                    alarmPopMenu.show(alarmLabel, alarmLabel.getWidth() - (alarmLabel.getWidth() / 2), alarmLabel.getHeight() - (alarmLabel.getHeight() / 2));

                }


            }
        });


        alarmLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);

                if(!showpop){showpop=true;}

                 alarmPopMenu.setVisible(false);

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


    private void buildAlarmPopup() {


        alarmPopMenu.removeAll();
        for (AlarmStartMessage alarmMessage : alarmList.values()) {
            JMenuItem nlabel = new JMenuItem(alarmMessage.getMessageText());
            alarmPopMenu.add(nlabel);
        }

    }



    private void addToPopup(MessageObject messaggio){


        JMenuItem new_message = new JMenuItem(messaggio.getMessageText());

        messagePopMenu.add(new_message);


        switch (messaggio.getMessageType()){

            case RequestDiagnosisMessage.CONSTRUCTOR:

                diagnosisMsg = new_message;

                new_message.addActionListener(e -> {
                    new AddDiagnosis(recovery);
                });

                break;

            case RequestStoppedAlarmReportMessage.CONSTRUCTOR:

                AlarmData alarmData = ((RequestStoppedAlarmReportMessage)messaggio).getAlarmData();
                stopMachineMessage.put(
                            alarmData.getAlarmId(),
                                new_message);

                new_message.addActionListener(e -> {
                    new ArchiveStopAlarm(recovery, alarmData);
                });
        }
    }

    private void setImage(String pathimg, Object componente) {

        ImageIcon icon = null;

        if (pathimg != null) {
            InputStream imgStream = PatientMonitor.class.getResourceAsStream(pathimg);

            BufferedImage myImg=null;

            try {
                myImg = ImageIO.read(imgStream);

            } catch (IOException e) {

                e.printStackTrace();
            }

            icon = new ImageIcon(myImg);
        }

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
