package it.ingsoftw.progetto.client;

import com.googlecode.lanterna.gui2.GridLayout;
import javafx.scene.chart.XYChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

public class Storico extends  JFrame{
    private JPanel MainPanel;
    private JPanel StoricPanel;
    private JLabel immagine1;
    private JLabel sbp;
    private JLabel temperatura;
    private JLabel dbp;
    private JLabel frequenza;
    private JLabel val1;
    private JLabel val2;
    private JLabel val3;
    private JScrollPane ScrollPane;
    private JPanel panelGraph;
    //private JScrollPane scrollpanel1;
    private XChartPanel chartPanel;


    public Storico() {

        super("Storico");

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        this.MainPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(1,1,1,1,Color.black),"nome",TitledBorder.TOP,TitledBorder.CENTER));
        ((javax.swing.border.TitledBorder) this.MainPanel.getBorder()).setTitleFont(new Font("Droid Serif", Font.ITALIC, 14));

        Dimension preferredDimension = new Dimension(600, 800);
        MainPanel.setPreferredSize(preferredDimension);


        this.ScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.ScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        this.ScrollPane.setBounds(50, 30, 300, 50);

        //this.scrollpanel1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        //this.scrollpanel1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        //this.scrollpanel1.setBounds(50, 30, 300, 50);

        org.knowm.xchart.XYChart chartSBP = new XYChartBuilder().xAxisTitle("time").yAxisTitle("SBP").width(300).height(100).build();
        org.knowm.xchart.XYChart chartDBP = new XYChartBuilder().xAxisTitle("time").yAxisTitle("DBP").width(300).height(100).build();
        org.knowm.xchart.XYChart chartFrequence = new XYChartBuilder().xAxisTitle("time").yAxisTitle("Frequence").width(300).height(100).build();
        org.knowm.xchart.XYChart chartTemperature = new XYChartBuilder().xAxisTitle("time").yAxisTitle("Temperature °C").width(300).height(100).build();

        chartSBP.getStyler().setYAxisMin((double) 0);
        chartSBP.getStyler().setYAxisMax((double) 200);
        chartSBP.getStyler().setXAxisMin((double) 0);
        chartSBP.getStyler().setXAxisMax((double) 5);

        chartDBP.getStyler().setYAxisMin((double) 0);
        chartDBP.getStyler().setYAxisMax((double) 120);
        chartDBP.getStyler().setXAxisMin((double) 0);
        chartDBP.getStyler().setXAxisMax((double) 5);

        chartFrequence.getStyler().setYAxisMin((double) 0);
        chartFrequence.getStyler().setYAxisMax((double) 200);
        chartFrequence.getStyler().setXAxisMin((double) 0);
        chartFrequence.getStyler().setXAxisMax((double) 5);

        chartTemperature.getStyler().setYAxisMin((double) 0);
        chartTemperature.getStyler().setYAxisMax((double) 45);
        chartTemperature.getStyler().setXAxisMin((double) 0);
        chartTemperature.getStyler().setXAxisMax((double) 5);

        double[] yData = new double[] { 2.0, 1.0, 0.0 };
        XYSeries serieProva = chartSBP.addSeries("SBP", yData);
        panelGraph.add(chartPanel, GridLayout.createHorizontallyFilledLayoutData(2));

        this.setContentPane(MainPanel);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.pack();
        this.setLocation((dim.width/2-this.getSize().width/2), (dim.height/2-this.getSize().height/2));
        this.setVisible(true);

    }
}
