package it.ingsoftw.progetto.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import it.ingsoftw.progetto.common.AlarmData;
import it.ingsoftw.progetto.common.AlarmLevel;
import it.ingsoftw.progetto.common.IVSListener;
import it.ingsoftw.progetto.common.MonitorData;
import it.ingsoftw.progetto.server.database.IRecoveryDatabase;

public class VsListener extends UnicastRemoteObject implements IVSListener {

    Set<Integer> alarms = new HashSet<>();
    int alarmsId = 0;

    int monitorsKey = 0;

    Map<Integer, String> connectedMonitors;
    IRecoveryDatabase database;


    protected VsListener(IRecoveryDatabase database) throws RemoteException {
        super(ServerConfig.port);
        this.database = database;
        connectedMonitors = new HashMap<>();
    }

    @Override
    public synchronized int connectVS(String id) throws RemoteException {
        System.out.println("Connected vital signs monitor with id " + id);
        if (connectedMonitors.containsKey(id))
            return -1;

        connectedMonitors.put(monitorsKey, id);
        return monitorsKey++;
    }

//    @Override
//    public boolean disconnectVS(String id, IVSConnection connection) throws RemoteException {
//        return false;
//    }

    @Override
    public boolean stopAlarm(int key, int alarmId) {
        String monitorId = connectedMonitors.get(key);
        if (monitorId == null) return false;

        if (alarms.contains(alarmId)) {
            alarms.remove(alarmId);
            database.stopAlarm(monitorId, alarmId);
            System.out.println("Stopped alarm " + alarmId + " on monitor " + monitorId);
            return true;
        }
        return false;
    }

    @Override
    public void notifyMonitorUpdate(int key, MonitorData monitorData) throws RemoteException {
        String monitorId = connectedMonitors.get(key);
        if (monitorId == null) return;

        database.updateMonitorData(monitorId, monitorData);
    }

    @Override
    public int startAlarm(int key, String description, AlarmLevel level) {
        String monitorId = connectedMonitors.get(key);
        if (monitorId == null) return -1;

        System.out.println("Alarm from monitor " + monitorId + ": " + description + " " + level.toString());
        int alarmId = alarmsId++;
        alarms.add(alarmId);
        database.startAlarm(monitorId, new AlarmData(level, description, LocalDateTime.now(), alarmId));
        return alarmId;
    }
}
