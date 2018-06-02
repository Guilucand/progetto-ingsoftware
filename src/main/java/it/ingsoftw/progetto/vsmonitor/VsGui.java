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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiFunction;
import java.util.function.Function;

import it.ingsoftw.progetto.common.AlarmLevel;
import javafx.util.Pair;

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

    @SuppressWarnings("unchecked")
    public VsGui(String name) {

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
        executorService.execute(this::alarmUpdater);
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

    void alarmUpdater() {

        boolean alarmShown = false;

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

            try {
                Thread.sleep(500);
            } catch (InterruptedException ignored) {
            }
        }
    }

    void setBPM(int bpm) {
        currentBpm = bpm;
    }

    int getBpm() {
        return currentBpm;
    }

    public int getSbp() {
        return currentSbp;
    }

    public void setSbp(int sbp) {
        this.currentSbp = sbp;
        sbpText.setText("▲ " + currentSbp);
    }

    public int getDbp() {
        return currentDbp;
    }

    public void setDbp(int dbp) {
        this.currentDbp = dbp;
        dbpText.setText("▼ " + currentDbp);
    }

    public float getTemp() {
        return currentTemp;
    }

    public void setTemp(float temp) {
        this.currentTemp = temp;
        tempText.setText("T " + currentTemp + "°C");
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
