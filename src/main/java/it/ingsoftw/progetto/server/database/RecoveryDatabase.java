package it.ingsoftw.progetto.server.database;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import it.ingsoftw.progetto.common.IAlarmCallback;
import it.ingsoftw.progetto.common.IMonitorDataUpdatedCallback;
import it.ingsoftw.progetto.common.MonitorData;
import it.ingsoftw.progetto.common.messages.AddDiagnosisMessage;
import javafx.util.Pair;

public class RecoveryDatabase implements IRecoveryDatabase {

    private Connection connection;
    private IMessageDatabase messageDatabase;
    private Map<String, MonitorData> currentMonitorsData;
    private Map<String, Set<IMonitorDataUpdatedCallback>> monitorDataCallbacks;
    private Map<String, Set<IAlarmCallback>> alarmsCallbacks;
    private Map<String, Map<Integer, IAlarmCallback.AlarmData>> activeAlarms;

    private Timer snapshotMonitorsTimer;

    public RecoveryDatabase(Connection connection, IMessageDatabase messageDatabase) {

        this.connection = connection;
        this.messageDatabase = messageDatabase;
        this.currentMonitorsData = new HashMap<>();
        this.monitorDataCallbacks = new HashMap<>();
        this.alarmsCallbacks = new HashMap<>();
        this.activeAlarms = new HashMap<>();
        this.snapshotMonitorsTimer = new Timer();
        DatabaseUtils.createDatabaseFromSchema(connection, "schema/recovery.sql");


        this.snapshotMonitorsTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                minuteSnapshot();
            }
        }, 0, 60 * 1000);
    }

    private List<String> getAllCurrentRecoveries() {
        return getSqlResults("SELECT key FROM recovery WHERE roomId IS NOT NULL;");
    }

    private void minuteSnapshot() {

        LocalDateTime currentTime = LocalDateTime.now();
        for (String recovery : getAllCurrentRecoveries()) {
            MonitorData monitorData = currentMonitorsData.get(mapRecoveryToMachine(recovery));
            addMonitorDataToDatabase(recovery, currentTime, monitorData);
        }
    }

    private boolean addMonitorDataToDatabase(String recoveryId, LocalDateTime dateTime, MonitorData data) {
        if (data == null)
            return false;

        String sql = "INSERT INTO vsdata (recoveryId, dateTime, bpm, sbp, dbp, temp) " +
                "VALUES (?, ?, ?, ?, ?, ?);";
        try {
            PreparedStatement addMonitorData = connection.prepareStatement(sql);
            addMonitorData.setInt(1, Integer.parseInt(recoveryId));
            addMonitorData.setTimestamp(2, Timestamp.valueOf(dateTime));
            addMonitorData.setInt(3, data.getBpm());
            addMonitorData.setInt(4, data.getSbp());
            addMonitorData.setInt(5, data.getDbp());
            addMonitorData.setFloat(6, data.getTemp());
            return addMonitorData.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    private boolean addMachine(String name) {
        String sql =
                "INSERT INTO vsmachine (identifier) " +
                        "VALUES (?);";
        try {
            PreparedStatement addMachine = connection.prepareStatement(sql);
            addMachine.setString(1, name);
            return addMachine.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    private boolean addRoom(String number) {
        String sql =
                "INSERT INTO room (number) " +
                        "VALUES (?);";
        try {
            PreparedStatement addRoom = connection.prepareStatement(sql);
            addRoom.setString(1, number);
            return addRoom.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }


    @Override
    public boolean setRoomMachineId(String roomId, String machineId) {
        addMachine(machineId);
        addRoom(roomId);

        String sql =
                "UPDATE room SET (machine) " +
                        "= (?) " +
                        "WHERE number = ?;";
        try {
            PreparedStatement setRoomMachine = connection.prepareStatement(sql);
            setRoomMachine.setString(1, machineId);
            setRoomMachine.setString(2, roomId);
            return setRoomMachine.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public String addRecovery(String patientId, String roomId) {
        String sql =
                "INSERT INTO recovery (patientCode, roomId) " +
                        "VALUES (?, ?) " +
                        "RETURNING key;";

        try {
            PreparedStatement addMachine = connection.prepareStatement(sql);
            addMachine.setString(1, patientId);
            addMachine.setString(2, roomId);
            ResultSet result = addMachine.executeQuery();
            if (!result.next())
                return null;

            String recoveryId = result.getString(1);

            if (recoveryId != null) {
                messageDatabase.addMessage(recoveryId, "ADD_DIAGNOSIS", new AddDiagnosisMessage());
            }
            return recoveryId;

        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public void updateMonitorData(String machineId, MonitorData data) {
        currentMonitorsData.put(machineId, data);


        Set<IMonitorDataUpdatedCallback> recoveryCallbacks = monitorDataCallbacks
                .get(mapMachineToRecovery(machineId));

        if (recoveryCallbacks == null)
            return;


        Set<IMonitorDataUpdatedCallback> unresponsive = new HashSet<>();

        for (IMonitorDataUpdatedCallback cb : recoveryCallbacks) {
            try {
                cb.monitorDataChanged(data);
            } catch (RemoteException e) {
                unresponsive.add(cb);
            }
        }

        for (IMonitorDataUpdatedCallback cb : unresponsive) {
            recoveryCallbacks.remove(cb);
        }
    }

    @Override
    public MonitorData getCurrentVsData(String recoveryId) {
        return currentMonitorsData.get(mapRecoveryToMachine(recoveryId));
    }

    @Override
    public void addAlarmHook(String recoveryId, IAlarmCallback callback) {
        alarmsCallbacks.computeIfAbsent(recoveryId, k -> new HashSet<>())
                .add(callback);
    }

    @Override
    public void removeAlarmHook(String recoveryId, IAlarmCallback callback) {
//        Set<IAlarmCallback> callbacks = alarmsCallbacks.get(recoveryId);
//        if (callbacks == null)
//            return;
//        callbacks.remove(callback);
    }

    @Override
    public void addMonitorDataUpdatedCallbackHook(String recoveryId, IMonitorDataUpdatedCallback callback) {
        monitorDataCallbacks.computeIfAbsent(recoveryId, k -> new HashSet<>())
                .add(callback);

        try {
            callback.monitorDataChanged(getCurrentVsData(recoveryId));
        } catch (RemoteException ignored) {
        }
    }

    @Override
    public void removeMonitorDataUpdatedCallbackHook(String recoveryId, IMonitorDataUpdatedCallback callback) {
        Set<IMonitorDataUpdatedCallback> callbacks = monitorDataCallbacks.get(recoveryId);
        if (callbacks == null)
            return;
        callbacks.remove(callback);
    }

    @Override
    public boolean startAlarm(String machineId, IAlarmCallback.AlarmData data) {
        activeAlarms.computeIfAbsent(machineId, k -> new HashMap<>())
                .put(data.getAlarmId(), data);


        Set<IAlarmCallback> currentAlarmCallbacks = alarmsCallbacks
                .get(mapMachineToRecovery(machineId));

        if (currentAlarmCallbacks == null)
            return false;


        Set<IAlarmCallback> unresponsive = new HashSet<>();

        boolean alarmReceived = false;
        for (IAlarmCallback cb : currentAlarmCallbacks) {
            try {
                cb.startAlarm(data);
                alarmReceived = true;
            } catch (RemoteException e) {
                unresponsive.add(cb);
            }
        }

        for (IAlarmCallback cb : unresponsive) {
            alarmsCallbacks.remove(cb);
        }
        return alarmReceived;
    }

    @Override
    public boolean stopAlarm(String machineId, int alarmId) {
        if (activeAlarms.computeIfAbsent(machineId, k -> new HashMap<>())
                .remove(alarmId) != null) {


            Set<IAlarmCallback> currentAlarmCallbacks = alarmsCallbacks
                    .get(mapMachineToRecovery(machineId));

            if (currentAlarmCallbacks == null)
                return false;


            Set<IAlarmCallback> unresponsive = new HashSet<>();

            for (IAlarmCallback cb : currentAlarmCallbacks) {
                try {
                    cb.stopAlarm(alarmId);
                } catch (RemoteException e) {
                    unresponsive.add(cb);
                }
            }

            for (IAlarmCallback cb : unresponsive) {
                alarmsCallbacks.remove(cb);
            }
            return true;
        }
        return false;
    }

    private String getSingleArgSqlResult(String sql, String arg1) {

        try {
            PreparedStatement getResult = connection.prepareStatement(sql);
            getResult.setString(1, arg1);
            ResultSet result = getResult.executeQuery();
            if (!result.next())
                return null;
            return result.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getSingleArgSqlResultIntParam(String sql, int arg1) {

        try {
            PreparedStatement getResult = connection.prepareStatement(sql);
            getResult.setInt(1, arg1);
            ResultSet result = getResult.executeQuery();
            if (!result.next())
                return null;
            return result.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<String> getSqlResults(String sql) {
        List<String> results = new ArrayList<>();

        try {
            PreparedStatement getResult = connection.prepareStatement(sql);
            ResultSet result = getResult.executeQuery();
            while (result.next())
                results.add(result.getString(1));
            return results;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }



    @Override
    public String mapRecoveryToRoom(String recoveryId) {
        try {
            return getSingleArgSqlResultIntParam("SELECT roomId FROM recovery WHERE key = ?;",
                    Integer.valueOf(recoveryId));
        }
        catch (Exception e) {
            return null;
        }
    }

    @Override
    public String mapRoomToRecovery(String roomId) {
        return getSingleArgSqlResult("SELECT key FROM recovery WHERE roomId = ?;",
                roomId);
    }

    @Override
    public String mapMachineToRoom(String machineId) {
        return getSingleArgSqlResult("SELECT number FROM room WHERE machine = ?;",
                machineId);
    }

    @Override
    public String mapRoomToMachine(String roomId) {
        return getSingleArgSqlResult("SELECT machine FROM room WHERE number = ?;",
                roomId);
    }

    @Override
    public String mapRecoveryToMachine(String recoveryId) {
        return getSingleArgSqlResultIntParam(
                "SELECT machine FROM recovery, room WHERE key = ? AND recovery.roomId = room.number;",
                Integer.valueOf(recoveryId));
    }

    @Override
    public String mapMachineToRecovery(String machineId) {
        return getSingleArgSqlResult(
                "SELECT key FROM recovery, room WHERE machine = ? AND recovery.roomId = room.number;",
                machineId);
    }

    @Override
    public String mapPatientToRecovery(String patientId) {
        return getSingleArgSqlResult("SELECT key FROM recovery WHERE patientCode = ? AND roomId IS NOT NULL;",
                patientId);
    }

    @Override
    public String mapRecoveryToPatient(String recoveryId) {
        return getSingleArgSqlResultIntParam("SELECT patientCode FROM recovery WHERE key = ?;",
                Integer.valueOf(recoveryId));
    }

    @Override
    public List<Pair<LocalDateTime, MonitorData>> getLastMonitorData(String recoveryId, int maxMinutes) {
        String sql = "SELECT dateTime, bpm, sbp, dbp, temp FROM vsdata " +
                "WHERE recoveryId = ? " +
                "ORDER BY dateTime DESC " +
                "LIMIT ?";

        List<Pair<LocalDateTime, MonitorData>> results = new ArrayList<>();

        try {
            PreparedStatement getResult = connection.prepareStatement(sql);
            getResult.setInt(1, Integer.parseInt(recoveryId));
            getResult.setInt(2, maxMinutes);

            ResultSet result = getResult.executeQuery();
            while (result.next()) {
                results.add(new Pair<>(
                        result.getTimestamp(1).toLocalDateTime(),
                        new MonitorData(
                                result.getInt(2),
                                result.getInt(3),
                                result.getInt(4),
                                result.getFloat(5))));
            }
            Collections.reverse(results);
            return results;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
