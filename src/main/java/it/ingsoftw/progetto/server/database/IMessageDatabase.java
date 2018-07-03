package it.ingsoftw.progetto.server.database;

import java.util.List;

import it.ingsoftw.progetto.common.messages.MessageObject;
import it.ingsoftw.progetto.server.ServerMessageDispatcher;

public interface IMessageDatabase {

    /**
     * Aggiunge un messaggio persistente
     * al database.
     * Questo messaggio e' inviato ad ogni client
     * che tenta di connettersi
     * Questi messaggi rimangono anche a
     * successivi riavvii del server
     * @param message il messaggio
     * @return
     */
    void addPersistentMessage(MessageObject message);

    /**
     * Rimuove il messaggio persistente
     * @param message
     */
    void removePersistentMessage(MessageObject message);

    /**
     * Aggiunge un messaggio di notifica ai client
     * riguardante avvenimenti temporanei (es. aggiornamento
     * dei monitor)
     * @param message
     */
    void addVolatileMessage(MessageObject message);

    /**
     * Returns the list of all current active
     * persistent messages
     * @return list of messages
     */
    List<MessageObject> getPersistentMessagesList();

    /**
     * Retrieves the next message from the volatile queue
     * and sends it.
     * @return
     */
    void registerDispatcher(ServerMessageDispatcher dispatcher);
}
