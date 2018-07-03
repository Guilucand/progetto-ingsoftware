package it.ingsoftw.progetto.client;

import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GraphicField extends JPanel{
    private JPanel SBPPanel;
    private JPanel SBPgraphicPanel;
    private JPanel MainPanel;
    private JPanel DBPPanel;
    private JScrollPane scrollpaneDBP;
    private JPanel DBPgraphicPanel;
    private JPanel FrequencePanel;
    private JPanel FrequencegraphicPanel;
    private JPanel TemperaturePanel;
    private JPanel temperaturegraphicPanel;


    public GraphicField(ArrayList<XYChart> graphicList){

        this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

        this.setPreferredSize(new Dimension(595,900));

        XYChart SBP = graphicList.get(0);
        XYChart DBP = graphicList.get(1);
        XYChart Frequence = graphicList.get(2);
        XYChart Temperature = graphicList.get(3);

        this.setBackground(Color.WHITE);

        SBPgraphicPanel = new XChartPanel<>(SBP);
        DBPgraphicPanel = new XChartPanel<>(DBP);
        FrequencegraphicPanel = new XChartPanel<>(Frequence);
        temperaturegraphicPanel = new XChartPanel<>(Temperature);


        this.add(MainPanel);

    }



}
