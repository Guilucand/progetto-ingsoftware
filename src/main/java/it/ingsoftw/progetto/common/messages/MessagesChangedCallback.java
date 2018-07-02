package it.ingsoftw.progetto.common.messages;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import it.ingsoftw.progetto.server.ReverseClientSocket;
import it.ingsoftw.progetto.server.ServerConfig;

public abstract class MessagesChangedCallback extends UnicastRemoteObject implements IMessagesChangedCallback {
    public MessagesChangedCallback() throws RemoteException {
        super(ServerConfig.reversePort, ReverseClientSocket.getClientSocketFactory(), ReverseClientSocket.getServerSocketFactory());
    }
}
