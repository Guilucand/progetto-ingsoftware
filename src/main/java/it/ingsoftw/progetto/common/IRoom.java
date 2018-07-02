package it.ingsoftw.progetto.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRoom extends Remote {
    IRecoveryCreator addRecovery() throws RemoteException;
    boolean hasRecovery() throws RemoteException;
    IRecovery getCurrentRecovery() throws RemoteException;
}
