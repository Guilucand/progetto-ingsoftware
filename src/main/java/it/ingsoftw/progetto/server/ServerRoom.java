package it.ingsoftw.progetto.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import it.ingsoftw.progetto.common.IPatient;
import it.ingsoftw.progetto.common.IRecoveryCreator;
import it.ingsoftw.progetto.common.IRoom;
import it.ingsoftw.progetto.server.database.IMessageDatabase;
import it.ingsoftw.progetto.server.database.IPatientsDatabase;
import it.ingsoftw.progetto.server.database.IRecoveryDatabase;

public class ServerRoom extends UnicastRemoteObject implements IRoom {

    private ClientStatus status;
    private IRecoveryDatabase recoveryDatabase;
    private IPatientsDatabase patientsDatabase;
    private IMessageDatabase messageDatabase;
    private int roomId;
    private IPatient currentPatient;

    protected ServerRoom(ClientStatus status,
                         IRecoveryDatabase recoveryDatabase,
                         IPatientsDatabase patientsDatabase,
                         IMessageDatabase messageDatabase,
                         int roomId) throws RemoteException {
        super(ServerConfig.port);
        this.status = status;
        this.recoveryDatabase = recoveryDatabase;
        this.patientsDatabase = patientsDatabase;
        this.messageDatabase = messageDatabase;
        this.roomId = roomId;
    }

    @Override
    public IRecoveryCreator addRecovery() throws RemoteException {
        return new ServerRecoveryCreator(status, patientsDatabase, recoveryDatabase, roomId);
    }

    @Override
    public boolean hasPatient() throws RemoteException {
        return recoveryDatabase.mapRoomToRecovery(String.valueOf(roomId)) != null;
    }

    @Override
    public IPatient getCurrentPatient() throws RemoteException {
        if (currentPatient == null)
            currentPatient = new ServerRecovery(status,
                    recoveryDatabase,
                    messageDatabase,
                    recoveryDatabase.mapRoomToRecovery(String.valueOf(roomId)));

        return currentPatient;
    }
}
