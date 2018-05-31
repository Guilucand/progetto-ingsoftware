package it.ingsoftw.progetto.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IClientConnection extends Remote {
    IClientRmiFactory estabilishConnection() throws RemoteException;
}
