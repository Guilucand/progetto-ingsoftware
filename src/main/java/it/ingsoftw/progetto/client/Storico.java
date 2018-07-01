package it.ingsoftw.progetto.client;

import it.ingsoftw.progetto.common.ILogin;
import it.ingsoftw.progetto.common.IPatient;
import it.ingsoftw.progetto.common.IRoom;
import it.ingsoftw.progetto.common.PatientData;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.internal.style.SeriesColorMarkerLineStyle;
import org.knowm.xchart.style.XYStyler;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;

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
    private IPatient ipatient;
    private PatientData patientData;
    private JFrame leavePatientFrame;

    public Storico(IRoom stanza, ILogin.LoginStatus status, String user) throws RemoteException {

        super("Storico");

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

        ipatient = stanza.getCurrentPatient();
        setImage("./img/image.png",imageLabel);

        temperatureParameter.setText("36");
        sbpParameter.setText("80");
        dbpParameter.setText("90");
        frequenceParameter.setText("100");

        patientData = ipatient.getPatientData();

        /*nameParameter.setText(patientData.getName());
        surnameParameter.setText(patientData.getSurname());
        cfParameter.setText(patientData.getCode());
        birthlocationParameter.setText(patientData.getBirthPlace());
        dateParameter.setText(patientData.getBirthDate().toString());
*/

        org.knowm.xchart.XYChart chartSBP = new XYChartBuilder().xAxisTitle("time").yAxisTitle("SBP").width(300).height(100).build();
        org.knowm.xchart.XYChart chartDBP = new XYChartBuilder().xAxisTitle("time").yAxisTitle("DBP").width(300).height(100).build();
        org.knowm.xchart.XYChart chartFrequence = new XYChartBuilder().xAxisTitle("time").yAxisTitle("Frequence").width(300).height(100).build();
        org.knowm.xchart.XYChart chartTemperature = new XYChartBuilder().xAxisTitle("time").yAxisTitle("Temperature °C").width(300).height(100).build();

        chartSBP.getStyler().setSeriesColors(new Color[]{new Color(107,187,95)});
        chartDBP.getStyler().setSeriesColors(new Color[]{new Color(17,87,31)});
        chartFrequence.getStyler().setSeriesColors(new Color[]{new Color(187,35,34)});
        chartTemperature.getStyler().setSeriesColors(new Color[]{new Color(70,83,187)});


        chartSBP.getStyler().setYAxisMin((double) 0);
        chartSBP.getStyler().setYAxisMax((double) 250);
        chartSBP.getStyler().setXAxisMin((double) 0);
        chartSBP.getStyler().setXAxisMax((double) 20);

        chartDBP.getStyler().setYAxisMin((double) 0);
        chartDBP.getStyler().setYAxisMax((double) 250);
        chartDBP.getStyler().setXAxisMin((double) 0);
        chartDBP.getStyler().setXAxisMax((double) 20);

        chartFrequence.getStyler().setYAxisMin((double) 0);
        chartFrequence.getStyler().setYAxisMax((double) 250);
        chartFrequence.getStyler().setXAxisMin((double) 0);
        chartFrequence.getStyler().setXAxisMax((double) 20);

        chartTemperature.getStyler().setYAxisMin((double) 0);
        chartTemperature.getStyler().setYAxisMax((double) 45);
        chartTemperature.getStyler().setXAxisMin((double) 0);
        chartTemperature.getStyler().setXAxisMax((double) 20);

        double[] yDataSBP = new double[] { 70.0, 75.0, 73.0 , 80.0 , 90.0 , 80 , 70 , 73.0 , 80.0 , 90.0 , 73.0 , 80.0 , 90.0 };
        double[] yDataDBP = new double[] { 70.0, 75.0, 73.0 , 80.0 , 90.0 , 80 , 70 , 73.0 , 80.0 , 90.0 , 73.0 , 80.0 , 90.0 };
        double[] yDataFrequence = new double[] { 70.0, 75.0, 73.0 , 80.0 , 90.0 , 80 , 70 , 73.0 , 80.0 , 90.0 , 73.0 , 80.0 , 90.0 };
        double[] yDataTemperature = new double[] { 36.5, 36.5 , 36.6 , 36.7 , 37 ,37.1 , 37 ,37.1, 37 ,37.1, 37 ,37.1, 37 ,37.1 };

        XYSeries serieProvaSBP = chartSBP.addSeries("SBP", yDataSBP);
        XYSeries serieProvaDBP = chartDBP.addSeries("DBP", yDataDBP);
        XYSeries serieProvaFrequence = chartFrequence.addSeries("Frequence", yDataFrequence);
        XYSeries serieProvaTemperature = chartTemperature.addSeries("Temperature", yDataTemperature);

        JPanel pnlChartSBP = new XChartPanel<>(chartSBP);
        JPanel pnlChartDBP = new XChartPanel<>(chartDBP);
        JPanel pnlChartFrequence = new XChartPanel<>(chartFrequence);
        JPanel pnlChartTemperature = new XChartPanel<>(chartTemperature);

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

        dimettiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                leavePatientFrame = new LeavePatient(stanza,user);

            }
        });
        esciDalloStoricoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                if(leavePatientFrame == null){dispose();}
                else if(leavePatientFrame.isActive()){

                    if(JOptionPane.showConfirmDialog(null,"Sei sicuro di voler uscire nonostante ci sia una schermata di dimisisone aperta?") == 0){

                        dispose();

                    }

                }else if (!leavePatientFrame.isActive()){dispose();}

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
}
