package it.ingsoftw.progetto.common;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import it.ingsoftw.progetto.server.ReverseClientSocket;
import it.ingsoftw.progetto.server.ServerConfig;

public abstract class MonitorDataUpdatedCallback extends UnicastRemoteObject implements IMonitorDataUpdatedCallback {
    public MonitorDataUpdatedCallback() throws RemoteException {
        super(ServerConfig.reversePort, ReverseClientSocket.getClientSocketFactory(), ReverseClientSocket.getServerSocketFactory());
    }
}
