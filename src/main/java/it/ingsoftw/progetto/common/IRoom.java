package it.ingsoftw.progetto.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRoom extends Remote {
    IRecoveryCreator addRecovery() throws RemoteException;
    boolean hasPatient() throws RemoteException;
    IRecovery getCurrentPatient() throws RemoteException;
}
