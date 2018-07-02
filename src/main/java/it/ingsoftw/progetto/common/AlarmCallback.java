package it.ingsoftw.progetto.common;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import it.ingsoftw.progetto.server.ReverseClientSocket;
import it.ingsoftw.progetto.server.ServerConfig;

public abstract class AlarmCallback extends UnicastRemoteObject implements IAlarmCallback {

    public AlarmCallback() throws RemoteException {
        super(ServerConfig.reversePort, ReverseClientSocket.getClientSocketFactory(), ReverseClientSocket.getServerSocketFactory());
    }
}
