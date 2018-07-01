package it.ingsoftw.progetto.server.database;

import java.rmi.RemoteException;

public interface IDatabaseConnection {
    IUsersDatabase getUsersInterface() throws RemoteException;
    IPatientsDatabase getPatientsInterface();
    IRecoveryDatabase getRecoveryInterface();
    IDrugsDatabase getDrugsInterface();

    IMessageDatabase getMessageInterface();

    IPrescriptionDatabase getPrescriptionInterface();
}
