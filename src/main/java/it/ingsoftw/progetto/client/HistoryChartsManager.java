package it.ingsoftw.progetto.client;

import it.ingsoftw.progetto.common.IRecovery;
import it.ingsoftw.progetto.common.IRecoveryHistory;
import it.ingsoftw.progetto.common.MonitorData;
import it.ingsoftw.progetto.server.database.RecoveryDatabase;
import javafx.util.Pair;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.style.markers.SeriesMarkers;

import javax.swing.*;
import java.awt.*;
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

public class HistoryChartsManager {

    XYChart chartSBP;
    XYChart chartDBP;
    XYChart chartFrequence;
    XYChart chartTemperature;

    JPanel pnlChartSBP;
    JPanel pnlChartDBP;
    JPanel pnlChartFrequence;
    JPanel pnlChartTemperature;
    private IRecovery recovery;
    private int minutesCount;
    private int recoveryKey;
    private IRecoveryHistory recoveryHistory;
    private LocalDateTime begin;
    private LocalDateTime end;

    public JPanel getPnlChartSBP() {
        return pnlChartSBP;
    }

    public JPanel getPnlChartDBP() {
        return pnlChartDBP;
    }

    public JPanel getPnlChartFrequence() {
        return pnlChartFrequence;
    }

    public JPanel getPnlChartTemperature() {
        return pnlChartTemperature;
    }

    public HistoryChartsManager(IRecovery recovery, int minutesCount) {
        this();
        this.recovery = recovery;
        this.minutesCount = minutesCount;
        updateChart();
    }

    public HistoryChartsManager(int recoveryKey,
                                IRecoveryHistory recoveryHistory,
                                LocalDateTime begin,
                                LocalDateTime end) {
        this();
        this.recoveryKey = recoveryKey;
        this.recoveryHistory = recoveryHistory;
        this.begin = begin;
        this.end = end;
        updateChart();
    }


    private HistoryChartsManager() {
        chartSBP = new XYChartBuilder().xAxisTitle("time").yAxisTitle("SBP").width(300).height(100).build();
        chartDBP = new XYChartBuilder().xAxisTitle("time").yAxisTitle("DBP").width(300).height(100).build();
        chartFrequence = new XYChartBuilder().xAxisTitle("time").yAxisTitle("Frequence").width(300).height(100).build();
        chartTemperature = new XYChartBuilder().xAxisTitle("time").yAxisTitle("Temperature °C").width(300).height(100).build();

        chartSBP.getStyler().setSeriesColors(new Color[]{new Color(107,187,95)});
        chartDBP.getStyler().setSeriesColors(new Color[]{new Color(17,87,31)});
        chartFrequence.getStyler().setSeriesColors(new Color[]{new Color(187,35,34)});
        chartTemperature.getStyler().setSeriesColors(new Color[]{new Color(70,83,187)});

        pnlChartSBP = new XChartPanel<>(chartSBP);
        pnlChartDBP = new XChartPanel<>(chartDBP);
        pnlChartFrequence = new XChartPanel<>(chartFrequence);
        pnlChartTemperature = new XChartPanel<>(chartTemperature);

    }

    public void updateChart() {


        chartSBP.getStyler().setYAxisMin(20.0);
        chartSBP.getStyler().setYAxisMax(250.0);

        chartDBP.getStyler().setYAxisMin(20.);
        chartDBP.getStyler().setYAxisMax(250.0);

        chartFrequence.getStyler().setYAxisMin(0.0);
        chartFrequence.getStyler().setYAxisMax(250.0);

        chartTemperature.getStyler().setYAxisMin(35.0);
        chartTemperature.getStyler().setYAxisMax(45.0);


        final int totSeconds = minutesCount * 60;

        java.util.List<Pair<LocalDateTime, MonitorData>> historyData = null;
        try {
            if (recovery != null)
                historyData = recovery.getLastVsData(minutesCount * (60 / RecoveryDatabase.SNAPSHOT_SECONDS_INTERVAL));
            else if (recoveryHistory != null) {
                historyData = recoveryHistory.getMonitorsBetween(recoveryKey, begin, end);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        if (historyData != null && historyData.size() > 0) {

            java.util.List<Integer> yDataSBP = new ArrayList<>();
            java.util.List<Integer> yDataDBP = new ArrayList<>();
            java.util.List<Integer> yDataFrequence = new ArrayList<>();
            java.util.List<Float> yDataTemperature = new ArrayList<>();

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
                    if (recovery != null)
                        xAxisOverrideMap.put((double) secondsValue,
                             currentTime.format(DateTimeFormatter.ofPattern("HH:mm")));
                    else
                        xAxisOverrideMap.put((double) secondsValue,
                                currentTime.format(DateTimeFormatter.ofPattern("dd/MM-HH:mm")));

                }

                MonitorData currentData = historyData.get(i).getValue();
                yDataDBP.add(currentData.getDbp());
                yDataSBP.add(currentData.getSbp());
                yDataFrequence.add(currentData.getBpm());
                yDataTemperature.add(currentData.getTemp());
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


            chartSBP.getSeriesMap().containsKey("SBP");
            chartDBP.removeSeries("DBP");
            chartFrequence.removeSeries("BPM");
            chartTemperature.removeSeries("Temperatura");


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
}
