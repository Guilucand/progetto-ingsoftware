package it.ingsoftw.progetto.common.messages;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public abstract class MessagesChangedCallback extends UnicastRemoteObject implements IMessagesChangedCallback {
    public MessagesChangedCallback() throws RemoteException {
    }
}
