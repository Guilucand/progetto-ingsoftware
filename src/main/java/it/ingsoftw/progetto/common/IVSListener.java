package it.ingsoftw.progetto.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IVSListener extends Remote {
    boolean connectVS(String id, IVSConnection connection) throws RemoteException;
    int startAlarm(String id, String description, AlarmLevel level) throws RemoteException;
    boolean stopAlarm(String id, int alarmId) throws RemoteException;
}
