package test.database;

import javafx.util.Pair;
import it.ingsoftw.progetto.common.MonitorData;
import it.ingsoftw.progetto.server.database.IRecoveryDatabase;

import java.util.HashMap;
import java.util.Map;

public class TestRecoveryDatabase implements IRecoveryDatabase {

    Map<String, MonitorData> data = new HashMap<>();
    Map<String, String> roomMachineMapping = new HashMap<>();

    Map<String, Pair<String, String>> recovery = new HashMap<>();


    @Override
    public void setRoomMachineId(String roomId, String machineId) {
        roomMachineMapping.put(roomId, machineId);
    }

    @Override
    public String addRecovery(String patientId, String roomId) {
        String recoveryId = roomId;

        recovery.put(recoveryId, new Pair<>(patientId, roomId));
        return recoveryId;
    }

    public void updateVsMonitor(String machineId, MonitorData data) {
        this.data.put(machineId, data);
    }

    @Override
    public MonitorData getCurrentVsData(String recoveryId) {
        return data.get(roomMachineMapping.get(recovery.get(recoveryId).getValue()));
    }
}
