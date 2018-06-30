package test.database;

import it.ingsoftw.progetto.common.IAlarmCallback;
import it.ingsoftw.progetto.common.IMonitorDataUpdatedCallback;
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


    Map<String, List<IAlarmCallback>> alarmCallbacks = new HashMap<>();
    Map<String, List<IMonitorDataUpdatedCallback>> dataCallbacks = new HashMap<>();

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

    public void updateMonitorData(String machineId, MonitorData data) {
        this.data.put(machineId, data);
        List<IMonitorDataUpdatedCallback> callbacksList = dataCallbacks.get(mapMachineToRecovery(machineId));
        if (callbacksList == null)
            return;

        for (IMonitorDataUpdatedCallback callback : callbacksList) {
            try {
                callback.monitorDataChanged(data);
            } catch (RemoteException e) {
            }
        }
    }

    @Override
    public MonitorData getCurrentVsData(String recoveryId) {
            return data.get(mapRecoveryToMachine(recoveryId));
    }

    @Override
    public void addAlarmHook(String recoveryId, IAlarmCallback callback) {
        if (!alarmCallbacks.containsKey(recoveryId))
            alarmCallbacks.put(recoveryId, new ArrayList<>());
        alarmCallbacks.get(recoveryId).add(callback);
    }

    @Override
    public void removeAlarmHook(String recoveryId, IAlarmCallback callback) {
        alarmCallbacks.get(recoveryId).remove(callback);
    }

    @Override
    public void addMonitorDataUpdatedCallbackHook(String recoveryId, IMonitorDataUpdatedCallback callback) {
        if (!dataCallbacks.containsKey(recoveryId))
            dataCallbacks.put(recoveryId, new ArrayList<>());
        dataCallbacks.get(recoveryId).add(callback);
    }

    @Override
    public void removeMonitorDataUpdatedCallbackHook(String recoveryId, IMonitorDataUpdatedCallback callback) {
        dataCallbacks.get(recoveryId).remove(callback);
    }

    @Override
    public boolean startAlarm(String machineId, IAlarmCallback.AlarmData data) {
        List<IAlarmCallback> callbacksList = alarmCallbacks.get(mapMachineToRecovery(machineId));
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
        List<IAlarmCallback> callbacksList = alarmCallbacks.get(mapMachineToRecovery(machineId));
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
