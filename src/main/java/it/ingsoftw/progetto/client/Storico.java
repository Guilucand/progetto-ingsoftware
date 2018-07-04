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
    XYChart chartSBP;
    XYChart chartDBP;
    XYChart chartFrequence;
    XYChart chartTemperature;

    JPanel pnlChartSBP;
    JPanel pnlChartDBP;
    JPanel pnlChartFrequence;
    JPanel pnlChartTemperature;

    private ArrayList<XYChart> GraphicList;

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

        chartSBP = new XYChartBuilder().xAxisTitle("time").yAxisTitle("SBP").width(300).height(100).build();
        chartDBP = new XYChartBuilder().xAxisTitle("time").yAxisTitle("DBP").width(300).height(100).build();
        chartFrequence = new XYChartBuilder().xAxisTitle("time").yAxisTitle("Frequence").width(300).height(100).build();
        chartTemperature = new XYChartBuilder().xAxisTitle("time").yAxisTitle("Temperature °C").width(300).height(100).build();

        chartSBP.getStyler().setSeriesColors(new Color[]{new Color(107,187,95)});
        chartDBP.getStyler().setSeriesColors(new Color[]{new Color(17,87,31)});
        chartFrequence.getStyler().setSeriesColors(new Color[]{new Color(187,35,34)});
        chartTemperature.getStyler().setSeriesColors(new Color[]{new Color(70,83,187)});

        updateChart();

        pnlChartSBP = new XChartPanel<>(chartSBP);
        pnlChartDBP = new XChartPanel<>(chartDBP);
        pnlChartFrequence = new XChartPanel<>(chartFrequence);
        pnlChartTemperature = new XChartPanel<>(chartTemperature);

        GraphicList = new ArrayList<>();
        GraphicList.add(chartSBP);
        GraphicList.add(chartDBP);
        GraphicList.add(chartFrequence);
        GraphicList.add(chartTemperature);

        this.SBPgraphicPanel.setLayout(new GridLayout());
        this.SBPgraphicPanel.add(pnlChartSBP);

        this.DBPgraphicPanel.setLayout(new GridLayout());
        this.DBPgraphicPanel.add(pnlChartDBP);

        this.FrequencegraphicPanel.setLayout(new GridLayout());
        this.FrequencegraphicPanel.add(pnlChartFrequence);

        this.temperaturegraphicPanel.setLayout(new GridLayout());
        this.temperaturegraphicPanel.add(pnlChartTemperature);

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

        updateChartTimer = new Timer(1000, e -> updateChart());
        updateChartTimer.start();

        stampaReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                PrintableReport printReport = null;
                try {
                    printReport = new PrintableReport(true, recovery.getKey(), recoveryHistory ,GraphicList);
                } catch (RemoteException e1) {
                    e1.printStackTrace();
                }

                printReport.printToPdf("here");


            }
        });
    }

    private void updateChart() {
        if (!isVisible() && updateChartTimer != null)
            updateChartTimer.stop();

        chartSBP.getStyler().setYAxisMin(20.0);
        chartSBP.getStyler().setYAxisMax(250.0);

        chartDBP.getStyler().setYAxisMin(20.);
        chartDBP.getStyler().setYAxisMax(250.0);

        chartFrequence.getStyler().setYAxisMin(0.0);
        chartFrequence.getStyler().setYAxisMax(250.0);

        chartTemperature.getStyler().setYAxisMin(35.0);
        chartTemperature.getStyler().setYAxisMax(45.0);

        final int minutes = 120;
        final int totSeconds = minutes * 60;

        List<Pair<LocalDateTime, MonitorData>> historyData;
        try {
            historyData = recovery.getLastVsData(minutes * (60 / RecoveryDatabase.SNAPSHOT_SECONDS_INTERVAL));
        } catch (RemoteException e) {
            e.printStackTrace();
            historyData = null;
        }

        if (historyData != null && historyData.size() > 0) {

            List<Integer> yDataSBP = new ArrayList<>();
            List<Integer> yDataDBP = new ArrayList<>();
            List<Integer> yDataFrequence = new ArrayList<>();
            List<Float> yDataTemperature = new ArrayList<>();

            List<Integer> xData = new ArrayList<>();//new Time[historyData.size()];
            Map<Double, Object> xAxisOverrideMap = new HashMap<>();

            long lastValue;
            {
                LocalDateTime lastTime = historyData
                        .get(historyData.size() - 1)
                        .getKey();
                ZonedDateTime lastZonedDateTime = ZonedDateTime.of(lastTime, ZoneId.systemDefault());
                lastValue = lastZonedDateTime.getLong(ChronoField.INSTANT_SECONDS);
            }


            for (int i = 0; i < historyData.size(); i++) {
                LocalDateTime currentTime = historyData
                        .get(i)
                        .getKey();

                ZonedDateTime zonedDateTime = ZonedDateTime.of(currentTime, ZoneId.systemDefault());

                long seconds = zonedDateTime.getLong(ChronoField.INSTANT_SECONDS);

                int secondsValue = (int) (totSeconds + (seconds - lastValue));

                xData.add(secondsValue);

                if (i % 15 == 0) {
                    xAxisOverrideMap.put((double) secondsValue,
                            currentTime.format(DateTimeFormatter.ofPattern("HH:mm")));
                }

                double maxXValue = xData.get(xData.size() - 1);

                chartSBP.getStyler().setXAxisMin(0.0);
                chartSBP.getStyler().setXAxisMax(maxXValue);
                chartDBP.getStyler().setXAxisMin(0.0);
                chartDBP.getStyler().setXAxisMax(maxXValue);
                chartFrequence.getStyler().setXAxisMin(0.0);
                chartFrequence.getStyler().setXAxisMax(maxXValue);
                chartTemperature.getStyler().setXAxisMin(0.0);
                chartTemperature.getStyler().setXAxisMax(maxXValue);

                MonitorData currentData = historyData.get(i).getValue();
                yDataDBP.add(currentData.getDbp());
                yDataSBP.add(currentData.getSbp());
                yDataFrequence.add(currentData.getBpm());
                yDataTemperature.add(currentData.getTemp());
            }

            chartSBP.getSeriesMap().containsKey("SBP");
            chartDBP.removeSeries("DBP");
            chartFrequence.removeSeries("Frequence");
            chartTemperature.removeSeries("Temperature");




            if (!chartSBP.getSeriesMap().containsKey("SBP"))
                chartSBP.addSeries("SBP", xData, yDataSBP).setMarker(SeriesMarkers.NONE);
            else
                chartSBP.updateXYSeries("SBP", xData, yDataSBP, null);

            if (!chartDBP.getSeriesMap().containsKey("DBP"))
                chartDBP.addSeries("DBP", xData, yDataDBP).setMarker(SeriesMarkers.NONE);
            else
                chartDBP.updateXYSeries("DBP", xData, yDataDBP, null);

            if (!chartFrequence.getSeriesMap().containsKey("Frequence"))
                chartFrequence.addSeries("Frequence", xData, yDataFrequence).setMarker(SeriesMarkers.NONE);
            else
                chartFrequence.updateXYSeries("Frequence", xData, yDataFrequence, null);


            if (!chartTemperature.getSeriesMap().containsKey("Temperature"))
                chartTemperature.addSeries("Temperature", xData, yDataTemperature).setMarker(SeriesMarkers.NONE);
            else
                chartTemperature.updateXYSeries("Temperature", xData, yDataTemperature, null);


            chartDBP.setXAxisLabelOverrideMap(xAxisOverrideMap);
            chartSBP.setXAxisLabelOverrideMap(xAxisOverrideMap);
            chartFrequence.setXAxisLabelOverrideMap(xAxisOverrideMap);
            chartTemperature.setXAxisLabelOverrideMap(xAxisOverrideMap);

            if (pnlChartSBP != null) {
                pnlChartSBP.revalidate();
                pnlChartSBP.repaint();
            }
            if (pnlChartDBP != null) {
                pnlChartDBP.revalidate();
                pnlChartDBP.repaint();
            }
            if (pnlChartFrequence != null) {
                pnlChartFrequence.revalidate();
                pnlChartFrequence.repaint();
            }
            if (pnlChartTemperature != null) {
                pnlChartTemperature.revalidate();
                pnlChartTemperature.repaint();
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

    public void close(){this.dispose();}
    private Storico questo(){return this;}
}
