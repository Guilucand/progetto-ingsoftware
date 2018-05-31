package it.ingsoftw.progetto.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IClientRmiFactory extends Remote {
    ILogin getLoginInterface() throws RemoteException;
}
