package it.ingsoftw.progetto.client;

import it.ingsoftw.progetto.common.*;
import it.ingsoftw.progetto.common.IRecovery;
import it.ingsoftw.progetto.server.database.RecoveryDatabase;
import javafx.util.Pair;

import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.Theme;
import org.knowm.xchart.style.markers.Marker;
import org.knowm.xchart.style.markers.SeriesMarkers;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.print.Printable;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Storico extends  JFrame{
    private JPanel MainPanel;
    private JScrollPane scrollMainPanel;
    private JPanel SBPgraphicPanel;
    private JPanel dataPatientPanel;
    private JLabel imageLabel;
    private JLabel sbpParameter;
    private JLabel dbpParameter;
    private JLabel frequenceParameter;
    private JLabel temperatureParameter;
    private JPanel SBPPanel;
    private JPanel DBPPanel;
    private JPanel DBPgraphicPanel;
    private JPanel FrequencePanel;
    private JPanel FrequencegraphicPanel;
    private JScrollPane scrollpaneDBP;
    private JPanel TemperaturePanel;
    private JPanel temperaturegraphicPanel;
    private JLabel nameParameter;
    private JLabel surnameParameter;
    private JLabel cfParameter;
    private JLabel dateParameter;
    private JLabel birthlocationParameter;
    private JButton dimettiButton;
    private JButton esciDalloStoricoButton;
    private JButton stampaReportButton;
    private PatientData patientData;
    private JFrame leavePatientFrame;
    private HistoryChartsManager historyChartsManager;

    Timer updateChartTimer;
    private IRecovery recovery;
    private IRecoveryHistory recoveryHistory;
    private IRecoveryHistory.RecoveryInfo recoveryInfo;

    public void updateVsData(MonitorData data) {
        if (data != null) {
            temperatureParameter.setText(String.valueOf(data.getTemp()));
            sbpParameter.setText(String.valueOf(data.getSbp()));
            dbpParameter.setText(String.valueOf(data.getDbp()));
            frequenceParameter.setText(String.valueOf(data.getBpm()));
        }
    }

    public Storico(IRecovery recovery, IRecoveryHistory recoveryHistory, ILogin.LoginStatus status, String user) throws RemoteException {

        super("Storico");
        this.recovery = recovery;
        this.recoveryHistory = recoveryHistory;
        this.recoveryInfo = recoveryHistory.getRecoveryFromKey(recovery.getKey());
        this.historyChartsManager = new HistoryChartsManager(recovery, 120);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        this.MainPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(1,1,1,1,Color.black),user,TitledBorder.TOP,TitledBorder.CENTER));
        ((javax.swing.border.TitledBorder) this.MainPanel.getBorder()).setTitleFont(new Font("Droid Serif", Font.ITALIC, 14));

        Dimension preferredDimension = new Dimension(760, 800);
        MainPanel.setPreferredSize(preferredDimension);

        this.dataPatientPanel.setPreferredSize( new Dimension(700, 200));
        this.SBPgraphicPanel.setPreferredSize( new Dimension(700, 200));
        this.DBPgraphicPanel.setPreferredSize( new Dimension(700, 200));
        this.FrequencegraphicPanel.setPreferredSize( new Dimension(700, 200));
        this.temperaturegraphicPanel.setPreferredSize( new Dimension(700, 200));

        if(!(status == ILogin.LoginStatus.PRIMARY_LOGGED)) dimettiButton.setVisible(false);

        setImage("./img/image.png",imageLabel);

        patientData = recoveryHistory.getPatientData(recoveryInfo.getPatientCode());

        if (patientData != null) {
            nameParameter.setText(patientData.getName());
            surnameParameter.setText(patientData.getSurname());
            cfParameter.setText(patientData.getCode());
            birthlocationParameter.setText(patientData.getBirthPlace());
            dateParameter.setText(patientData.getBirthDate().toString());
        }

        this.SBPgraphicPanel.setLayout(new GridLayout());
        this.SBPgraphicPanel.add(historyChartsManager.getPnlChartSBP());

        this.DBPgraphicPanel.setLayout(new GridLayout());
        this.DBPgraphicPanel.add(historyChartsManager.getPnlChartDBP());

        this.FrequencegraphicPanel.setLayout(new GridLayout());
        this.FrequencegraphicPanel.add(historyChartsManager.getPnlChartFrequence());

        this.temperaturegraphicPanel.setLayout(new GridLayout());
        this.temperaturegraphicPanel.add(historyChartsManager.getPnlChartTemperature());

        this.setContentPane(MainPanel);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.pack();
        this.setLocation((dim.width/2-this.getSize().width/2), (dim.height/2-this.getSize().height/2));
        this.setVisible(true);

        dimettiButton.addActionListener(e -> leavePatientFrame = new LeavePatient(recovery, user, questo()));
        esciDalloStoricoButton.addActionListener(e -> {
            if (leavePatientFrame == null){
                dispose();
            }
            else if (leavePatientFrame.isShowing()) {

                if(JOptionPane.showConfirmDialog(null,"Chiudere senza salvare la schermata di dimissione?") == 0){

                    dispose();
                    leavePatientFrame.dispose();
                }

            }
            else if (!leavePatientFrame.isActive()){
                dispose();
            }

        });

        updateChartTimer = new Timer(1000, e -> {
            if (!isVisible() && updateChartTimer != null)
                updateChartTimer.stop();

            historyChartsManager.updateChart();
        });
        updateChartTimer.start();


        stampaReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                PrintableReport printReport = null;
                try {
                    printReport = new PrintableReport(true, recovery.getKey(), recoveryHistory, LocalDateTime.now().minusMinutes(600), LocalDateTime.now());
                } catch (RemoteException e1) {
                    e1.printStackTrace();
                }

                printReport.printToPdf("here");


            }
        });
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

    public void close(){this.dispose();}
    private Storico questo(){return this;}
}
