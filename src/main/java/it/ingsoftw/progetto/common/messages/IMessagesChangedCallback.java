package it.ingsoftw.progetto.common.messages;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IMessagesChangedCallback extends Remote {
    void messagesChanged() throws RemoteException;
}
