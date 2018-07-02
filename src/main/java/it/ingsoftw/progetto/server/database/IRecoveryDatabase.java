package it.ingsoftw.progetto.server.database;

import java.time.LocalDateTime;
import java.util.List;

import it.ingsoftw.progetto.common.IAlarmCallback;
import it.ingsoftw.progetto.common.IMonitorDataUpdatedCallback;
import it.ingsoftw.progetto.common.MonitorData;
import javafx.util.Pair;

public interface IRecoveryDatabase {

    boolean addDiagnosis(String recoveryId, String diagnosis);

    boolean leaveRecovery(String recoveryId, String dimissionLetter);

    boolean setRoomMachineId(String roomId, String machineId);

    String addRecovery(String patientCode, String roomId);

    void updateMonitorData(String machineId, MonitorData data);

    MonitorData getCurrentVsData(String recoveryId);
    void addAlarmHook(String recoveryId, IAlarmCallback callback);
    void removeAlarmHook(String recoveryId, IAlarmCallback callback);

    void addMonitorDataUpdatedCallbackHook(String recoveryId, IMonitorDataUpdatedCallback callback);
    void removeMonitorDataUpdatedCallbackHook(String recoveryId, IMonitorDataUpdatedCallback callback);

    boolean startAlarm(String machineId, IAlarmCallback.AlarmData data);
    boolean stopAlarm(String machineId, int alarmId);


    String mapRecoveryToRoom(String recoveryId);
    String mapRoomToRecovery(String roomId);

    String mapMachineToRoom(String machineId);
    String mapRoomToMachine(String roomId);

    String mapRecoveryToMachine(String recoveryId);
    String mapMachineToRecovery(String machineId);

    String mapPatientToRecovery(String patientCode);

    String mapRecoveryToPatient(String recoveryId);

    List<Pair<LocalDateTime, MonitorData>> getLastMonitorData(String recoveryId, int maxMinutes);
}
