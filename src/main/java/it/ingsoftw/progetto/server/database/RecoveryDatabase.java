package it.ingsoftw.progetto.server.database;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.transform.Result;

import it.ingsoftw.progetto.common.IAlarmCallback;
import it.ingsoftw.progetto.common.IMonitorDataUpdatedCallback;
import it.ingsoftw.progetto.common.MonitorData;
import it.ingsoftw.progetto.common.MonitorDataUpdatedCallback;
import it.ingsoftw.progetto.common.messages.AddDiagnosisMessage;
import it.ingsoftw.progetto.common.messages.IMessagesChangedCallback;

public class RecoveryDatabase implements IRecoveryDatabase {

    private Connection connection;
    private IMessageDatabase messageDatabase;
    private Map<String, MonitorData> currentMonitorsData;
    private Map<String, Set<IMonitorDataUpdatedCallback>> monitorDataCallbacks;
    private Map<String, Set<IAlarmCallback>> alarmsCallbacks;
    private Map<String, Map<Integer, IAlarmCallback.AlarmData>> activeAlarms;


    public RecoveryDatabase(Connection connection, IMessageDatabase messageDatabase) {

        this.connection = connection;
        this.messageDatabase = messageDatabase;
        this.currentMonitorsData = new HashMap<>();
        this.monitorDataCallbacks = new HashMap<>();
        this.alarmsCallbacks = new HashMap<>();
        this.activeAlarms = new HashMap<>();
        DatabaseUtils.createDatabaseFromSchema(connection, "schema/recovery.sql");
    }

    private boolean addMachine(String name) {
        String sql =
                "INSERT into vsmachine (identifier) " +
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
                "INSERT into room (number) " +
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
        Set<IAlarmCallback> callbacks = alarmsCallbacks.get(recoveryId);
        if (callbacks == null)
            return;
        callbacks.remove(callback);
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
}
