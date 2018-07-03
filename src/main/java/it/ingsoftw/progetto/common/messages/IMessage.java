package it.ingsoftw.progetto.common.messages;

import it.ingsoftw.progetto.common.messages.MessageObject;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Classe che riceve i messaggi
 * dal server in modo bloccante.
 * E' utilizzata su un thread del client
 * che chiama la funzione getNextMessage.
 */
public interface IMessage extends Remote {
    MessageObject getNextMessage() throws RemoteException;
}
