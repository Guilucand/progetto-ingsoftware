package it.ingsoftw.progetto.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import it.ingsoftw.progetto.common.IRecovery;
import it.ingsoftw.progetto.common.IRecoveryCreator;
import it.ingsoftw.progetto.common.IRoom;
import it.ingsoftw.progetto.server.database.IMessageDatabase;
import it.ingsoftw.progetto.server.database.IPatientsDatabase;
import it.ingsoftw.progetto.server.database.IPrescriptionDatabase;
import it.ingsoftw.progetto.server.database.IRecoveryDatabase;

public class ServerRoom extends UnicastRemoteObject implements IRoom {

    private ClientStatus status;
    private IRecoveryDatabase recoveryDatabase;
    private IPatientsDatabase patientsDatabase;
    private IMessageDatabase messageDatabase;
    private IPrescriptionDatabase prescriptionDatabase;
    private int roomId;
    private IRecovery currentRecovery;
    private String recoveryId;

    protected ServerRoom(ClientStatus status,
                         IRecoveryDatabase recoveryDatabase,
                         IPatientsDatabase patientsDatabase,
                         IMessageDatabase messageDatabase,
                         IPrescriptionDatabase prescriptionDatabase,
                         int roomId) throws RemoteException {
        super(ServerConfig.port);
        this.status = status;
        this.recoveryDatabase = recoveryDatabase;
        this.patientsDatabase = patientsDatabase;
        this.messageDatabase = messageDatabase;
        this.prescriptionDatabase = prescriptionDatabase;
        this.roomId = roomId;
    }

    @Override
    public IRecoveryCreator addRecovery() throws RemoteException {
        return new ServerRecoveryCreator(status, patientsDatabase, recoveryDatabase, roomId);
    }

    @Override
    public boolean hasRecovery() throws RemoteException {
        return recoveryDatabase.mapRoomToRecovery(String.valueOf(roomId)) != null;
    }

    @Override
    public IRecovery getCurrentRecovery() throws RemoteException {
        String recoveryId = recoveryDatabase.mapRoomToRecovery(String.valueOf(roomId));
        if (recoveryId == null) {
            return currentRecovery = null;
        }

        if (currentRecovery == null || (recoveryId != this.recoveryId))
            this.recoveryId = recoveryId;
            currentRecovery = new ServerRecovery(status,
                    recoveryDatabase,
                    messageDatabase,
                    patientsDatabase,
                    prescriptionDatabase,
                    recoveryId);

        return currentRecovery;
    }
}
