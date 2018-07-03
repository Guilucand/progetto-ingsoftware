package it.ingsoftw.progetto.vsmonitor;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.SimpleTheme;
import com.googlecode.lanterna.graphics.Theme;
import com.googlecode.lanterna.gui2.Border;
import com.googlecode.lanterna.gui2.Borders;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.Component;
import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.gui2.dialogs.ActionListDialogBuilder;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiFunction;
import java.util.function.Function;

import it.ingsoftw.progetto.common.AlarmLevel;
import it.ingsoftw.progetto.common.IVSListener;
import it.ingsoftw.progetto.common.MonitorData;
import javafx.util.Pair;

/**
 * Interfaccia grafica da terminale di una macchina di monitoraggio
 */
public class VsGui {

    Panel topPanel;
    Panel mainPanel;

    Panel monitorPanel;
    Panel optionsPanel;

    Label bpmText;
    Label sbpText;
    Label dbpText;
    Label tempText;

    int currentBpm;
    int currentSbp;
    int currentDbp;
    float currentTemp;

    boolean hasChanged;

    WindowBasedTextGUI mainWindow;

    Button alarmSimulateButton;
    Button alarmStopButton;
    Button connectionButton;
    Runnable[] alarmsRunnables;

    boolean connectionStatus;

    BiFunction<String, AlarmLevel, Integer> callback;
    Function<Integer, Boolean> stopCallback;
    Function<Boolean, Boolean> connectionCallback;


    Pair<String, AlarmLevel>[] alarmsTypes = new Pair[]{
            new Pair<>("Aritmia", AlarmLevel.Level1),
            new Pair<>("Tachicardia", AlarmLevel.Level1),
            new Pair<>("Flutter/Fibrillazione", AlarmLevel.Level3),
            new Pair<>("Ipertensione", AlarmLevel.Level1),
            new Pair<>("Ipotensione", AlarmLevel.Level1),
            new Pair<>("Ipertensione", AlarmLevel.Level1),
            new Pair<>("Ipotensione", AlarmLevel.Level1),
    };

    List<Integer> alarms = new ArrayList<>();
    private Function<MonitorData, MonitorData> monitorDataFunction;

    private VsInstance instance;

    @SuppressWarnings("unchecked")
    public VsGui(String name, VsInstance instance) {
        this.instance = instance;

        topPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        topPanel.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));

        topPanel.addComponent(new Label("Monitor " + name).addStyle(SGR.BOLD).addStyle(SGR.UNDERLINE)
                .setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center)));


        mainPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));

        topPanel.addComponent(mainPanel);

        mainPanel.addComponent(monitorPanel = new Panel());
        mainPanel.addComponent(optionsPanel = new Panel());


        monitorPanel.addComponent(bpmText = new Label(""));
        bpmText.setTheme(new SimpleTheme(TextColor.ANSI.RED, TextColor.ANSI.WHITE));

        monitorPanel.addComponent(sbpText = new Label(""));
        sbpText.setTheme(new SimpleTheme(TextColor.ANSI.MAGENTA, TextColor.ANSI.WHITE));


        monitorPanel.addComponent(dbpText = new Label(""));
        dbpText.setTheme(new SimpleTheme(TextColor.ANSI.BLUE, TextColor.ANSI.WHITE));

        monitorPanel.addComponent(tempText = new Label(""));
        tempText.setTheme(new SimpleTheme(TextColor.ANSI.BLACK, TextColor.ANSI.WHITE));


        optionsPanel.addComponent(alarmSimulateButton = new Button("Simula allarme"));
        optionsPanel.addComponent(alarmStopButton = new Button("Spegni allarme"));
        optionsPanel.addComponent(connectionButton = new Button("Connetti"));


        alarmSimulateButton.addListener((b)->{
            showAlarmsWindow();
//            simulateAlarm("Aritmia", AlarmLevel.Level1);
        });

        alarmStopButton.addListener((b)->{
            stopAlarms();
        });

        connectionButton.addListener((b)->{
            if (connectionCallback != null)
                connectionCallback.apply(!connectionStatus);
        });


        setBPM(60);
        setDbp(70);
        setSbp(95);
        setTemp(36.5f);
        startPulses();
    }

    public Component getPanel() {
        Border border = Borders.singleLine();
        border.setComponent(topPanel);
        return border;
    }

    public void startPulses() {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.execute(this::heartUpdater);
        executorService.execute(this::alarmAndVsDataUpdater);
    }

    MonitorData lastData = new MonitorData(60, 115, 75, 36.5f);

    public void setUpdateFunction(Function<MonitorData, MonitorData> updateFunction) {

        this.monitorDataFunction = updateFunction;
    }

    private void updateValues() {
        if (monitorDataFunction != null)
            lastData = monitorDataFunction.apply(lastData);
        setBPM(lastData.getBpm());
        setDbp(lastData.getDbp());
        setSbp(lastData.getSbp());
        setTemp(lastData.getTemp());
    }

    public void simulateAlarm(String desc, AlarmLevel level) {
        if (callback != null) {
            int alarm = callback.apply(desc, level);
            if (alarm >= 0)
                alarms.add(alarm);
        }
    }

    public void stopAlarms() {
        for (int alarm : alarms) {
            stopCallback.apply(alarm);
        }
        alarms.clear();
    }

    void heartUpdater() {
        boolean hearthShown = false;

        while (true) {
            hearthShown = !hearthShown;
            bpmText.setText((hearthShown ? "❤ " : "  ") + currentBpm);


            try {
                Thread.sleep(60000/currentBpm/2);
            } catch (InterruptedException ignored) {
            }
        }
    }

    void alarmAndVsDataUpdater() {

        boolean alarmShown = false;
        boolean toUpdate = true;

        Theme defaultTheme = alarmStopButton.getTheme();
        Theme alarmTheme = new SimpleTheme(TextColor.ANSI.WHITE, TextColor.ANSI.YELLOW, SGR.BOLD);

        while (true) {
            boolean hasAlarms = alarms.size() > 0;

            if (hasAlarms)
                alarmShown = !alarmShown;
            else
                alarmShown = false;

            alarmStopButton.setTheme(alarmShown ? alarmTheme : defaultTheme);
            alarmStopButton.invalidate();

            if (toUpdate) {
                updateValues();
                try {
                    if (hasChanged) {
                        hasChanged = false;
                        instance.updateMonitorData(new MonitorData(
                                currentBpm,
                                currentSbp,
                                currentDbp,
                                currentTemp));
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                    setConnectionStatus(false);
                }
            }

            toUpdate = !toUpdate;

            try {
                Thread.sleep(500);
            } catch (InterruptedException ignored) {
            }
        }
    }

    void setBPM(int bpm) {
        if (currentBpm != bpm) {
            currentBpm = bpm;
            hasChanged = true;
        }
    }

    int getBpm() {
        return currentBpm;
    }

    public int getSbp() {
        return currentSbp;
    }

    public void setSbp(int sbp) {
        if (currentSbp != sbp) {
            currentSbp = sbp;
            sbpText.setText("▲ " + currentSbp);
            hasChanged = true;
        }
    }

    public int getDbp() {
        return currentDbp;
    }

    public void setDbp(int dbp) {
        if (currentDbp != dbp) {
            currentDbp = dbp;
            dbpText.setText("▼ " + currentDbp);
            hasChanged = true;
        }
    }

    public float getTemp() {
        return currentTemp;
    }

    public void setTemp(float temp) {
        float newTemp = (int)(temp * 10) / 10.0f;
        if (newTemp != currentTemp) {
            currentTemp = newTemp;
            tempText.setText("T " + currentTemp + "°C");
            hasChanged = true;
        }
    }

    public void setAlarmCallback(BiFunction<String, AlarmLevel, Integer> callback) {
        this.callback = callback;
    }
    public void setAlarmStopCallback(Function<Integer, Boolean> callback) {
        this.stopCallback = callback;
    }

    public boolean getConnectionStatus() {
        return connectionStatus;
    }

    public void setConnectionStatus(boolean connectionStatus) {
        this.connectionStatus = connectionStatus;
        connectionButton.setLabel(connectionStatus ? "Disconnetti" : "Connetti");
    }

    public void setConnectionCallback(Function<Boolean, Boolean> connectionCallback) {
        this.connectionCallback = connectionCallback;
    }

    public void setMainWindow(WindowBasedTextGUI mainWindow) {
        this.mainWindow = mainWindow;
    }

    public void showAlarmsWindow() {
        ActionListDialogBuilder builder = new ActionListDialogBuilder()
                .setTitle("Simulazione allarme")
                .setDescription("Scegliere l'allarme");


        for (Pair<String, AlarmLevel> alarm : alarmsTypes) {
            builder.addAction(alarm.getKey() + " " + alarm.getValue(),
                    ()->simulateAlarm(alarm.getKey(), alarm.getValue()));
        }

        builder.build().showDialog(mainWindow);
    }

}
