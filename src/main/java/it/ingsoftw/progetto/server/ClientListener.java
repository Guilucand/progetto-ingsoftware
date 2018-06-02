package it.ingsoftw.progetto.server;

//import java.rmi.server.;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashSet;
import java.util.Set;

import it.ingsoftw.progetto.common.IClientListener;
import it.ingsoftw.progetto.common.IClientRmiFactory;

public class ClientListener extends UnicastRemoteObject implements IClientListener {

    Set<IClientRmiFactory> connectedUsers;

    public ClientListener() throws RemoteException {
        super(ServerConfig.port);
        connectedUsers = new HashSet<>();
    }

    @Override
    public IClientRmiFactory estabilishConnection() throws RemoteException{
        ClientRmiFactory clientFactory = new ClientRmiFactory();
        connectedUsers.add(clientFactory);
        return clientFactory;
    }
}
