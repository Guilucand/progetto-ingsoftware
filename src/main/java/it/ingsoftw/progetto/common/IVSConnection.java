package it.ingsoftw.progetto.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IVSConnection extends Remote {
    int getBpm() throws RemoteException;
    int getSbp() throws RemoteException;
    int getDbp() throws RemoteException;
    float getTemp() throws RemoteException;

}
