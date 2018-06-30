package it.ingsoftw.progetto.server;

import it.ingsoftw.progetto.common.IMonitor;
import it.ingsoftw.progetto.common.IPatient;
import it.ingsoftw.progetto.common.IRoom;
import it.ingsoftw.progetto.server.database.IRecoveryDatabase;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

public class ServerMonitor extends UnicastRemoteObject implements IMonitor {

    private ClientStatus status;
    private IRecoveryDatabase recoveryDatabase;
    private HashMap<Integer, IRoom> roomInterfaces;

    public ServerMonitor(ClientStatus status, IRecoveryDatabase recoveryDatabase) throws RemoteException {
        super(ServerConfig.port);
        this.status = status;

        this.recoveryDatabase = recoveryDatabase;
        roomInterfaces = new HashMap<>();
    }

    @Override
    public IRoom getRoomByNumber(int roomNumber) throws RemoteException {
        if (!roomInterfaces.containsKey(roomNumber))
            roomInterfaces.put(roomNumber, new ServerRoom(status, recoveryDatabase, roomNumber));

        return roomInterfaces.get(roomNumber);
    }
}
