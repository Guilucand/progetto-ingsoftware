package it.ingsoftw.progetto.server.database;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import it.ingsoftw.progetto.common.AlarmData;
import it.ingsoftw.progetto.common.IRecoveryHistory;
import it.ingsoftw.progetto.common.MonitorData;
import it.ingsoftw.progetto.common.PatientData;
import it.ingsoftw.progetto.common.messages.*;
import it.ingsoftw.progetto.common.messages.persistent.AlarmStartMessage;
import it.ingsoftw.progetto.common.messages.persistent.RequestDiagnosisMessage;
import it.ingsoftw.progetto.common.messages.persistent.RequestStoppedAlarmReportMessage;
import javafx.util.Pair;

public class RecoveryDatabase implements IRecoveryDatabase {

    private Connection connection;
    private IMessageDatabase messageDatabase;
    private Map<String, MonitorData> currentMonitorsData;
    private Map<String, Map<Integer, AlarmData>> activeAlarms;

    public static final int SNAPSHOT_SECONDS_INTERVAL = 60;

    private Timer snapshotMonitorsTimer;

    public RecoveryDatabase(Connection connection, IMessageDatabase messageDatabase) {

        this.connection = connection;
        this.messageDatabase = messageDatabase;
        this.currentMonitorsData = new HashMap<>();
        this.activeAlarms = new HashMap<>();
        this.snapshotMonitorsTimer = new Timer();
        DatabaseUtils.createDatabaseFromSchema(connection, "schema/recovery.sql");


        this.snapshotMonitorsTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                minuteSnapshot();
            }
        }, 5000, SNAPSHOT_SECONDS_INTERVAL * 1000);

        this.generateMessages();
    }

    private void generateMessages() {
        for (Integer recoveryId : getMissingDiagnosisLetter()) {
            messageDatabase.addPersistentMessage(new RequestDiagnosisMessage(recoveryId, mapRecoveryToRoom(recoveryId)));
        }
    }

    @Override
    public boolean addDiagnosis(int recoveryKey, String diagnosis) {
        String sql = "UPDATE recovery SET diagnosis = ?";

        try {
            PreparedStatement addDiagnosis = connection.prepareStatement(sql);
            addDiagnosis.setString(1, diagnosis);
            addDiagnosis.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        messageDatabase.removePersistentMessage(new RequestDiagnosisMessage(recoveryKey, mapRecoveryToRoom(recoveryKey)));
        messageDatabase.addVolatileMessage(new AddedDiagnosisMessage(recoveryKey, mapRecoveryToRoom(recoveryKey), diagnosis));

        return true;
    }

    @Override
    public boolean leaveRecovery(int recoveryKey, String dimissionLetter) {
        String room = mapRecoveryToRoom(recoveryKey);

        String sql = "UPDATE recovery SET (dimissionLetter, roomId) = (?, NULL) " +
                "WHERE key = ?";

        try {
            PreparedStatement addDiagnosis = connection.prepareStatement(sql);
            addDiagnosis.setString(1, dimissionLetter);
            addDiagnosis.setInt(2, Integer.valueOf(recoveryKey));

            if (addDiagnosis.executeUpdate() > 0) {
                messageDatabase.addVolatileMessage(new DimissionMessage(recoveryKey,
                        room,
                        dimissionLetter));
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private List<Integer> getMissingDiagnosisLetter() {
        return getSqlIntResults("SELECT key FROM recovery WHERE diagnosis IS NULL;");
    }

    private List<Integer> getAllCurrentRecoveries() {
        return getSqlIntResults("SELECT key FROM recovery WHERE roomId IS NOT NULL;");
    }

    private void minuteSnapshot() {

        LocalDateTime currentTime = LocalDateTime.now();
        for (Integer recovery : getAllCurrentRecoveries()) {
            MonitorData monitorData = currentMonitorsData.get(mapRecoveryToMachine(recovery));
            addMonitorDataToDatabase(recovery, currentTime, monitorData);
        }
    }

    private boolean addMonitorDataToDatabase(int recoveryKey, LocalDateTime dateTime, MonitorData data) {
        if (data == null)
            return false;

        String sql = "INSERT INTO vsdata (recoveryKey, dateTime, bpm, sbp, dbp, temp) " +
                "VALUES (?, ?, ?, ?, ?, ?);";
        try {
            PreparedStatement addMonitorData = connection.prepareStatement(sql);
            addMonitorData.setInt(1, recoveryKey);
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
    public Integer addRecovery(PatientData patient, String roomId) {
        String sql =
                "INSERT INTO recovery (patientCode, roomId, startDate) " +
                        "VALUES (?, ?, ?) " +
                        "RETURNING key;";

        try {
            PreparedStatement addRecovery = connection.prepareStatement(sql);
            addRecovery.setString(1, patient.getCode());
            addRecovery.setString(2, roomId);
            addRecovery.setDate(3, Date.valueOf(LocalDate.now()));

            ResultSet result = addRecovery.executeQuery();
            if (!result.next())
                return null;

            int recoveryKey = result.getInt(1);

            messageDatabase.addVolatileMessage(new PatientAddedMessage(recoveryKey, roomId, patient));
            messageDatabase.addPersistentMessage(new RequestDiagnosisMessage(recoveryKey, roomId));
            return recoveryKey;

        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public void updateMonitorData(String machineId, MonitorData data) {
        currentMonitorsData.put(machineId, data);
        Integer recoveryKey = mapMachineToRecovery(machineId);
        if (recoveryKey != null) {
            messageDatabase.addVolatileMessage(new MonitorDataChangedMessage(recoveryKey,
                    mapRecoveryToRoom(recoveryKey),
                    data));
        }
    }

    @Override
    public MonitorData getCurrentVsData(int recoveryKey) {
        return currentMonitorsData.get(mapRecoveryToMachine(recoveryKey));
    }

    @Override
    public boolean startAlarm(String machineId, AlarmData data) {
        activeAlarms.computeIfAbsent(machineId, k -> new HashMap<>())
                .put(data.getAlarmId(), data);

        Integer recoveryKey = mapMachineToRecovery(machineId);
        if (recoveryKey != null) {
            messageDatabase.addPersistentMessage(new AlarmStartMessage(recoveryKey, mapRecoveryToRoom(recoveryKey), data));
            return true;
        }
        return false;
    }

    @Override
    public boolean stopAlarm(String machineId, int alarmId) {
        AlarmData alarmData;
        if ((alarmData = activeAlarms.computeIfAbsent(machineId, k -> new HashMap<>())
                .remove(alarmId)) != null) {
            Integer recoveryKey = mapMachineToRecovery(machineId);
            if (recoveryKey != null) {
                String roomId = mapRecoveryToRoom(recoveryKey);

                messageDatabase.removePersistentMessage(new AlarmStartMessage(recoveryKey, roomId, alarmData));
                messageDatabase.addVolatileMessage(new AlarmStopMessage(recoveryKey, roomId, alarmData));
                messageDatabase.addPersistentMessage(new RequestStoppedAlarmReportMessage(recoveryKey, roomId, alarmData));
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

    private Integer getSingleIntArgSqlResult(String sql, String arg1) {

        try {
            PreparedStatement getResult = connection.prepareStatement(sql);
            getResult.setString(1, arg1);
            ResultSet result = getResult.executeQuery();
            if (!result.next())
                return null;
            return result.getInt(1);
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

    private List<Integer> getSqlIntResults(String sql) {
        List<Integer> results = new ArrayList<>();

        try {
            PreparedStatement getResult = connection.prepareStatement(sql);
            ResultSet result = getResult.executeQuery();
            while (result.next())
                results.add(result.getInt(1));
            return results;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public String mapRecoveryToRoom(int recoveryKey) {
        try {
            return getSingleArgSqlResultIntParam("SELECT roomId FROM recovery WHERE key = ?;",
                    Integer.valueOf(recoveryKey));
        }
        catch (Exception e) {
            return null;
        }
    }

    @Override
    public Integer mapRoomToRecovery(String roomId) {
        return getSingleIntArgSqlResult("SELECT key FROM recovery WHERE roomId = ?;",
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
    public String mapRecoveryToMachine(int recoveryKey) {
        return getSingleArgSqlResultIntParam(
                "SELECT machine FROM recovery, room WHERE key = ? AND recovery.roomId = room.number;",
                Integer.valueOf(recoveryKey));
    }

    @Override
    public Integer mapMachineToRecovery(String machineId) {
        return getSingleIntArgSqlResult(
                "SELECT key FROM recovery, room WHERE machine = ? AND recovery.roomId = room.number;",
                machineId);
    }

    @Override
    public Integer mapPatientToRecovery(String patientCode) {
        return getSingleIntArgSqlResult("SELECT key FROM recovery WHERE patientCode = ? AND roomId IS NOT NULL;",
                patientCode);
    }

    @Override
    public String mapRecoveryToPatient(int recoveryKey) {
        return getSingleArgSqlResultIntParam("SELECT patientCode FROM recovery WHERE key = ?;",
                Integer.valueOf(recoveryKey));
    }

    @Override
    public List<Pair<LocalDateTime, MonitorData>> getMonitorData(int recoveryKey, LocalDateTime begin, LocalDateTime end) {
        String sql = "SELECT dateTime, bpm, sbp, dbp, temp FROM vsdata " +
                "WHERE recoveryKey = ? AND (dateTime BETWEEN ? AND ?) " +
                "ORDER BY dateTime DESC ";

        List<Pair<LocalDateTime, MonitorData>> results = new ArrayList<>();

        try {
            PreparedStatement getResult = connection.prepareStatement(sql);
            getResult.setInt(1, recoveryKey);
            getResult.setTimestamp(2, Timestamp.valueOf(begin));
            getResult.setTimestamp(3, Timestamp.valueOf(end));

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

    @Override
    public List<IRecoveryHistory.RecoveryInfo> getRecoveryInfos(LocalDateTime begin, LocalDateTime end) {
        String sql = "SELECT key, patientCode, startDate, endDate, diagnosis, dimissionLetter " +
                "FROM recovery " +
                "WHERE startDate BETWEEN ? AND ?;";

        List<IRecoveryHistory.RecoveryInfo> results = new ArrayList<>();

        try {
            PreparedStatement getRecoveries = connection.prepareStatement(sql);
            getRecoveries.setTimestamp(1, Timestamp.valueOf(begin));
            getRecoveries.setTimestamp(2, Timestamp.valueOf(end));

            ResultSet result = getRecoveries.executeQuery();
            while (result.next()) {
                results.add(new IRecoveryHistory.RecoveryInfo(
                        result.getInt(1),
                        result.getString(2),
                        result.getTimestamp(3).toLocalDateTime(),
                        result.getTimestamp(4).toLocalDateTime(),
                        result.getString(5),
                        result.getString(6)));
            }
            return results;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public IRecoveryHistory.RecoveryInfo getRecoveryInfoFromKey(int recoveryKey) {
        String sql = "SELECT key, patientCode, startDate, endDate, diagnosis, dimissionLetter " +
                "FROM recovery " +
                "WHERE key = ?;";


        try {
            PreparedStatement getRecovery = connection.prepareStatement(sql);
            getRecovery.setInt(1, recoveryKey);

            ResultSet result = getRecovery.executeQuery();
            if (result.next()) {
                Timestamp endTimestamp = result.getTimestamp(4);


                return new IRecoveryHistory.RecoveryInfo(
                        result.getInt(1),
                        result.getString(2),
                        result.getTimestamp(3).toLocalDateTime(),
                        (endTimestamp != null) ? endTimestamp.toLocalDateTime() : null,
                        result.getString(5),
                        result.getString(6));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
