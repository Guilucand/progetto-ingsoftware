package test.database;

import it.ingsoftw.progetto.common.IAlarmCallback;
import javafx.util.Pair;
import it.ingsoftw.progetto.common.MonitorData;
import it.ingsoftw.progetto.server.database.IRecoveryDatabase;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class TestRecoveryDatabase implements IRecoveryDatabase {

    Map<String, MonitorData> data = new HashMap<>();
//    Map<String, String> roomMachineMapping = new HashMap<>();

    Map<String, Pair<String, String>> recovery = new HashMap<>();


    Map<String, List<IAlarmCallback>> callbacks = new HashMap<>();

    @Override
    public void setRoomMachineId(String roomId, String machineId) {
//        roomMachineMapping.put(roomId, machineId);
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
            return data.get(mapRecoveryToMachine(recoveryId));
    }

    @Override
    public void addAlarmHook(String recoveryId, IAlarmCallback callback) {
        if (!callbacks.containsKey(recoveryId))
            callbacks.put(recoveryId, new ArrayList<>());
        callbacks.get(recoveryId).add(callback);
    }

    @Override
    public void removeAlarmHook(String recoveryId, IAlarmCallback callback) {
        callbacks.get(recoveryId).remove(callback);
    }

    @Override
    public boolean startAlarm(String machineId, IAlarmCallback.AlarmData data) {
        List<IAlarmCallback> callbacksList = callbacks.get(mapMachineToRecovery(machineId));
        if (callbacksList == null)
            return false;

        boolean received = false;
        for (IAlarmCallback callback : callbacksList) {
            try {
                callback.startAlarm(data);
                received = true;

            } catch (RemoteException e) {
            }
        }
        return received;
    }

    @Override
    public boolean stopAlarm(String machineId, int alarmId) {
        List<IAlarmCallback> callbacksList = callbacks.get(mapMachineToRecovery(machineId));
        if (callbacksList == null)
            return false;

        boolean received = false;
        for (IAlarmCallback callback : callbacksList) {
            try {
                callback.stopAlarm(alarmId);
                received = true;

            } catch (RemoteException e) {
            }
        }
        return received;
    }


    @Override
    public String mapRecoveryToRoom(String recoveryId) {
        return recovery.get(recoveryId).getValue();
    }

    @Override
    public String mapRoomToRecovery(String roomId) {
        for (Map.Entry<String, Pair<String, String>> entry : recovery.entrySet()) {
            if (Objects.equals(entry.getValue().getValue(), roomId))
                return entry.getKey();
        }
        return null;
    }

    @Override
    public String mapMachineToRoom(String machineId) {
        return machineId;
    }

    @Override
    public String mapRoomToMachine(String roomId) {
        return roomId;
    }

    @Override
    public String mapRecoveryToMachine(String recoveryId) {
        return mapRoomToMachine(mapRecoveryToRoom(recoveryId));
    }

    @Override
    public String mapMachineToRecovery(String machineId) {
        return mapRoomToRecovery(mapMachineToRoom(machineId));
    }
}
