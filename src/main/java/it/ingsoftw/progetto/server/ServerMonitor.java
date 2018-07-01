package it.ingsoftw.progetto.server;

import it.ingsoftw.progetto.common.IMonitor;
import it.ingsoftw.progetto.common.IPatient;
import it.ingsoftw.progetto.common.IRoom;
import it.ingsoftw.progetto.server.database.IMessageDatabase;
import it.ingsoftw.progetto.server.database.IPatientsDatabase;
import it.ingsoftw.progetto.server.database.IRecoveryDatabase;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

public class ServerMonitor extends UnicastRemoteObject implements IMonitor {

    private ClientStatus status;
    private IRecoveryDatabase recoveryDatabase;
    private IMessageDatabase messageDatabase;
    private IPatientsDatabase patientsDatabase;
    private HashMap<Integer, IRoom> roomInterfaces;

    public ServerMonitor(ClientStatus status,
                         IRecoveryDatabase recoveryDatabase,
                         IMessageDatabase messageDatabase,
                         IPatientsDatabase patientsDatabase) throws RemoteException {
        super(ServerConfig.port);
        this.status = status;

        this.recoveryDatabase = recoveryDatabase;
        this.messageDatabase = messageDatabase;
        this.patientsDatabase = patientsDatabase;
        roomInterfaces = new HashMap<>();
    }

    @Override
    public IRoom getRoomByNumber(int roomNumber) throws RemoteException {
        if (!roomInterfaces.containsKey(roomNumber))
            roomInterfaces.put(roomNumber,
                    new ServerRoom(status,
                                    recoveryDatabase,
                                    patientsDatabase,
                                    messageDatabase,
                                    roomNumber));

        return roomInterfaces.get(roomNumber);
    }
}
