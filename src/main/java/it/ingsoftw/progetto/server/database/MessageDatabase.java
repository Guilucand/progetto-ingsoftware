package it.ingsoftw.progetto.server.database;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.ingsoftw.progetto.common.messages.IMessagesChangedCallback;
import it.ingsoftw.progetto.common.messages.MessageObject;

public class MessageDatabase implements IMessageDatabase {

    private DatabaseConnection connection;
    private Map<String, Set<IMessagesChangedCallback>> callbacks;
    private Map<String, Set<MessageObject>> messages;

    public MessageDatabase(DatabaseConnection connection) {

        this.connection = connection;
        this.callbacks = new HashMap<>();
        this.messages = new HashMap<>();
    }

    @Override
    public synchronized List<MessageObject> getMessagesForRecovery(String recoveryId) {
        Set<MessageObject> recoveryMessages = messages.get(recoveryId);
        if (recoveryMessages == null)
            return new ArrayList<>();

        return new ArrayList<>(recoveryMessages);
    }

    @Override
    public synchronized void addMessage(String recoveryId, String key, MessageObject message) {

        if (messages.get(recoveryId) == null)
            messages.put(recoveryId, new HashSet<>());

        messages.get(recoveryId).add(message);
        notifyChanges(recoveryId);
    }

    @Override
    public synchronized void completeMessage(String recoveryId, String key) {

        Set<MessageObject> recoveryMessages = messages.get(recoveryId);
        if (recoveryMessages == null)
            return;

        if (recoveryMessages.remove(key))
            notifyChanges(recoveryId);
    }

    private synchronized void notifyChanges(String recoveryId) {
        Set<IMessagesChangedCallback> recoveryCallbacks = new HashSet<>();
        if (recoveryCallbacks == null)
            return;

        Set<IMessagesChangedCallback> unresponsive = new HashSet<>();
        for (IMessagesChangedCallback callback : recoveryCallbacks) {
            try {
                callback.messagesChanged();
            } catch (RemoteException e) {
                unresponsive.add(callback);
            }
        }

        for (IMessagesChangedCallback cb : unresponsive) {
            recoveryCallbacks.remove(cb);
        }
    }

    @Override
    public synchronized void addMessagesChangedCallback(String recoveryId, IMessagesChangedCallback callback) {
        if (callbacks.get(recoveryId) == null)
            callbacks.put(recoveryId, new HashSet<>());

        callbacks.get(recoveryId).add(callback);
    }

    @Override
    public synchronized void removeMessagesChangedCallback(String recoveryId, IMessagesChangedCallback callback) {
        if (callbacks.get(recoveryId) == null)
            return;

        callbacks.get(recoveryId).remove(callback);
    }


}
