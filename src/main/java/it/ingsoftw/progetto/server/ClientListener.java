package it.ingsoftw.progetto.server;

//import java.rmi.server.;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashSet;
import java.util.Set;

import it.ingsoftw.progetto.common.IClientListener;
import it.ingsoftw.progetto.common.IClientRmiFactory;
import it.ingsoftw.progetto.server.database.IDatabaseConnection;

public class ClientListener extends UnicastRemoteObject implements IClientListener {

    Set<IClientRmiFactory> connectedUsers;
    IDatabaseConnection databaseConnection;

    public ClientListener(IDatabaseConnection databaseConnection) throws RemoteException {
        super(ServerConfig.port);
        connectedUsers = new HashSet<>();
        this.databaseConnection = databaseConnection;
    }

    @Override
    public IClientRmiFactory estabilishConnection() throws RemoteException{
        ClientRmiFactory clientFactory = new ClientRmiFactory(databaseConnection);
        connectedUsers.add(clientFactory);
        return clientFactory;
    }
}
