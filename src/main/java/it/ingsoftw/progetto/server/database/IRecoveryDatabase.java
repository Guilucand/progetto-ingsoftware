package it.ingsoftw.progetto.server.database;

import it.ingsoftw.progetto.common.IAlarmCallback;
import it.ingsoftw.progetto.common.MonitorData;

public interface IRecoveryDatabase {

    void setRoomMachineId(String roomId, String machineId);

    String addRecovery(String patientId, String roomId);

    void updateVsMonitor(String machineId, MonitorData data);

    MonitorData getCurrentVsData(String recoveryId);
    void addAlarmHook(String recoveryId, IAlarmCallback callback);

    void removeAlarmHook(String recoveryId, IAlarmCallback callback);

    boolean startAlarm(String machineId, IAlarmCallback.AlarmData data);
    boolean stopAlarm(String machineId, int alarmId);


    String mapRecoveryToRoom(String recoveryId);
    String mapRoomToRecovery(String roomId);

    String mapMachineToRoom(String machineId);
    String mapRoomToMachine(String roomId);

    String mapRecoveryToMachine(String recoveryId);
    String mapMachineToRecovery(String machineId);
}
