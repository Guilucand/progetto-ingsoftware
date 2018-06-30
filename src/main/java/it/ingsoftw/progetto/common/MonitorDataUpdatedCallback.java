package it.ingsoftw.progetto.common;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public abstract class MonitorDataUpdatedCallback extends UnicastRemoteObject implements IMonitorDataUpdatedCallback {
    public MonitorDataUpdatedCallback() throws RemoteException {
    }
}
