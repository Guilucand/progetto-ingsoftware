package it.ingsoftw.progetto.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import it.ingsoftw.progetto.common.AlarmLevel;
import it.ingsoftw.progetto.common.IVSConnection;
import it.ingsoftw.progetto.common.IVSListener;

public class VsListener extends UnicastRemoteObject implements IVSListener {

    Set<Integer> alarms = new HashSet<>();
    int alarmsId = 0;

    Map<String, IVSConnection> connectedMonitors;


    protected VsListener() throws RemoteException {
        super(ServerConfig.port);
        connectedMonitors = new HashMap<>();
    }

    @Override
    public boolean connectVS(String id, IVSConnection connection) {
        System.out.println("Connected vital signs monitor with id " + id);
        connectedMonitors.put(id, connection);
        return true;
    }

    @Override
    public boolean stopAlarm(String id, int alarmId) {
        if (alarms.contains(alarmId)) {
            System.out.println("Stopped alarm " + alarmId + " on monitor " + id);
            return true;
        }
        return false;
    }

    @Override
    public int startAlarm(String id, String description, AlarmLevel level) {
        System.out.println("Alarm from monitor " + id + ": " + description + " " + level.toString());
        int alarmId = alarmsId++;
        alarms.add(alarmId);
        return alarmId;
    }
}
