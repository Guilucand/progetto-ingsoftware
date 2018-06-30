package it.ingsoftw.progetto.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IMonitorDataUpdatedCallback extends Remote {
    void monitorDataChanged(MonitorData data) throws RemoteException;
}
