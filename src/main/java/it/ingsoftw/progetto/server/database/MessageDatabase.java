package it.ingsoftw.progetto.server.database;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import it.ingsoftw.progetto.common.messages.MessageObject;
import it.ingsoftw.progetto.server.ServerMessageDispatcher;

public class MessageDatabase implements IMessageDatabase {

    private Set<MessageObject> persistentMessages;
    private Set<ServerMessageDispatcher> messageDispatchers;

    public MessageDatabase() {

        this.persistentMessages = new HashSet<>();
        this.messageDispatchers = new HashSet<>();
    }

    @Override
    public synchronized void addPersistentMessage(MessageObject message) {
        persistentMessages.add(message);
        for (ServerMessageDispatcher dispatcher : messageDispatchers) {
            dispatcher.addMessage(message);
        }
    }

    @Override
    public synchronized void removePersistentMessage(MessageObject message) {
        persistentMessages.remove(message);
    }

    @Override
    public synchronized void addVolatileMessage(MessageObject message) {
        for (ServerMessageDispatcher dispatcher : messageDispatchers) {
            dispatcher.addMessage(message);
        }
    }

    @Override
    public synchronized List<MessageObject> getPersistentMessagesList() {
        return new ArrayList<>(persistentMessages);
    }

    @Override
    public void registerDispatcher(ServerMessageDispatcher dispatcher) {
        messageDispatchers.add(dispatcher);
        for (MessageObject message : persistentMessages) {
            dispatcher.addMessage(message);
        }
    }
}
