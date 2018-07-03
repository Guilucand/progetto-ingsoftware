package it.ingsoftw.progetto.server.database;

import java.time.LocalDateTime;
import java.util.List;

import it.ingsoftw.progetto.common.AlarmData;
import it.ingsoftw.progetto.common.MonitorData;
import javafx.util.Pair;

public interface IRecoveryDatabase {

    boolean addDiagnosis(int recoveryKey, String diagnosis);

    boolean leaveRecovery(int recoveryKey, String dimissionLetter);

    boolean setRoomMachineId(String roomId, String machineId);

    Integer addRecovery(String patientCode, String roomId);

    void updateMonitorData(String machineId, MonitorData data);

    MonitorData getCurrentVsData(int recoveryKey);

    boolean startAlarm(String machineId, AlarmData data);
    boolean stopAlarm(String machineId, int alarmId);


    String mapRecoveryToRoom(int recoveryKey);
    Integer mapRoomToRecovery(String roomId);

    String mapMachineToRoom(String machineId);
    String mapRoomToMachine(String roomId);

    String mapRecoveryToMachine(int recoveryKey);
    Integer mapMachineToRecovery(String machineId);

    Integer mapPatientToRecovery(String patientCode);

    String mapRecoveryToPatient(int recoveryKey);

    List<Pair<LocalDateTime, MonitorData>> getLastMonitorData(int recoveryKey, int maxMinutes);
}
