package it.ingsoftw.progetto.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import it.ingsoftw.progetto.common.IPatient;
import it.ingsoftw.progetto.common.IRecoveryCreator;
import it.ingsoftw.progetto.common.IRoom;
import it.ingsoftw.progetto.common.PatientData;
import it.ingsoftw.progetto.server.database.IRecoveryDatabase;

public class ServerRoom extends UnicastRemoteObject implements IRoom {

    private ClientStatus status;
    private IRecoveryDatabase recoveryDatabase;
    private int roomId;
    private IPatient currentPatient;

    protected ServerRoom(ClientStatus status, IRecoveryDatabase recoveryDatabase, int roomId) throws RemoteException {
        super(ServerConfig.port);
        this.status = status;
        this.recoveryDatabase = recoveryDatabase;
        this.roomId = roomId;
    }

    @Override
    public IRecoveryCreator addRecovery() throws RemoteException {
        return null;
    }

    @Override
    public boolean hasPatient() throws RemoteException {
        return recoveryDatabase.mapRoomToRecovery(String.valueOf(roomId)) != null;
    }

    @Override
    public IPatient getCurrentPatient() throws RemoteException {
        if (currentPatient == null)
            currentPatient = new ServerPatient(status, recoveryDatabase, recoveryDatabase.mapRoomToRecovery(String.valueOf(roomId)));

        return currentPatient;
    }
}
