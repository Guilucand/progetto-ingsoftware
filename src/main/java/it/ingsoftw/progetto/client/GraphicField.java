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


    public GraphicField(HistoryChartsManager graphicList){

        this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

        this.setPreferredSize(new Dimension(595,900));

        SBPgraphicPanel.setLayout(new BoxLayout(SBPgraphicPanel, BoxLayout.PAGE_AXIS));
        DBPgraphicPanel.setLayout(new BoxLayout(DBPgraphicPanel, BoxLayout.PAGE_AXIS));
        FrequencegraphicPanel.setLayout(new BoxLayout(FrequencegraphicPanel, BoxLayout.PAGE_AXIS));
        temperaturegraphicPanel.setLayout(new BoxLayout(temperaturegraphicPanel, BoxLayout.PAGE_AXIS));

        SBPgraphicPanel.add(graphicList.getPnlChartSBP());
        DBPgraphicPanel.add(graphicList.getPnlChartDBP());
        FrequencegraphicPanel.add(graphicList.getPnlChartFrequence());
        temperaturegraphicPanel.add(graphicList.getPnlChartTemperature());


        this.add(MainPanel);

    }



}
