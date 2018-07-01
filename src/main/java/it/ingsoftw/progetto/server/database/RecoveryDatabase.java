package it.ingsoftw.progetto.server.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.xml.transform.Result;

import it.ingsoftw.progetto.common.IAlarmCallback;
import it.ingsoftw.progetto.common.IMonitorDataUpdatedCallback;
import it.ingsoftw.progetto.common.MonitorData;
import it.ingsoftw.progetto.common.messages.AddDiagnosisMessage;

public class RecoveryDatabase implements IRecoveryDatabase {

    private Connection connection;
    private IMessageDatabase messageDatabase;

    public RecoveryDatabase(Connection connection, IMessageDatabase messageDatabase) {

        this.connection = connection;
        this.messageDatabase = messageDatabase;
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
                        "= ROW(?) " +
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

    }

    @Override
    public MonitorData getCurrentVsData(String recoveryId) {
        return null;
    }

    @Override
    public void addAlarmHook(String recoveryId, IAlarmCallback callback) {

    }

    @Override
    public void removeAlarmHook(String recoveryId, IAlarmCallback callback) {

    }

    @Override
    public void addMonitorDataUpdatedCallbackHook(String recoveryId, IMonitorDataUpdatedCallback callback) {

    }

    @Override
    public void removeMonitorDataUpdatedCallbackHook(String recoveryId, IMonitorDataUpdatedCallback callback) {

    }

    @Override
    public boolean startAlarm(String machineId, IAlarmCallback.AlarmData data) {
        return false;
    }

    @Override
    public boolean stopAlarm(String machineId, int alarmId) {
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


    @Override
    public String mapRecoveryToRoom(String recoveryId) {
        return getSingleArgSqlResult("SELECT roomId FROM recovery WHERE key = ?;",
                recoveryId);
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
        return getSingleArgSqlResult(
                "SELECT machine FROM recovery, room WHERE key = ? AND recovery.roomId = room.number;",
                recoveryId);
    }

    @Override
    public String mapMachineToRecovery(String machineId) {
        return getSingleArgSqlResult(
                "SELECT key FROM recovery, room WHERE machine = ? AND recovery.roomId = room.number;",
                machineId);
    }
}
