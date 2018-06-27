package it.ingsoftw.progetto.server.database;

import it.ingsoftw.progetto.common.MonitorData;

public interface IRecoveryDatabase {

    void setRoomMachineId(String roomId, String machineId);

    String addRecovery(String patientId, String roomId);

    void updateVsMonitor(String machineId, MonitorData data);
    MonitorData getCurrentVsData(String recoveryId);
}
