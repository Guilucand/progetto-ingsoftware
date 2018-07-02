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

    private Map<String, Set<IMessagesChangedCallback>> callbacks;
    private Map<String, Set<MessageObject>> messages;

    public MessageDatabase() {

        this.callbacks = new HashMap<>();
        this.messages = new HashMap<>();
    }

    @Override
    public List<MessageObject> getMessagesForRecovery(String recoveryId) {

        synchronized (messages) {

            Set<MessageObject> recoveryMessages = messages.get(recoveryId);
            if (recoveryMessages == null)
                return new ArrayList<>();

            return new ArrayList<>(recoveryMessages);
        }
    }

    @Override
    public void addMessage(String recoveryId, String key, MessageObject message) {

        synchronized (messages) {

            if (messages.get(recoveryId) == null)
                messages.put(recoveryId, new HashSet<>());

            messages.get(recoveryId).add(message);
        }

        notifyChanges(recoveryId);
    }

    @Override
    public void completeMessage(String recoveryId, String key) {

        boolean removed;
        synchronized (messages) {
            Set<MessageObject> recoveryMessages = messages.get(recoveryId);
            if (recoveryMessages == null)
                return;

            removed = recoveryMessages.remove(key);
        }
        if (removed)
            notifyChanges(recoveryId);
    }

    @Override
    public void addSingleTimeMessage(String recoveryId, String key, MessageObject message) {
        addMessage(recoveryId, key, message);

        synchronized (messages) {
            Set<MessageObject> recoveryMessages = messages.get(recoveryId);
            if (recoveryMessages == null)
                return;

            recoveryMessages.remove(key);
        }
    }

    private void notifyChanges(String recoveryId) {
        Set<IMessagesChangedCallback> recoveryCallbacks;
        Set<IMessagesChangedCallback> recoveryCallbacksCopy;

        synchronized (callbacks) {

            recoveryCallbacks = callbacks.get(recoveryId);
            if (recoveryCallbacks == null)
                return;
            // Copio l'oggetto per evitare problemi di sincronizzazione
            recoveryCallbacksCopy = new HashSet<>(recoveryCallbacks);
        }

        Set<IMessagesChangedCallback> unresponsive = new HashSet<>();
        for (IMessagesChangedCallback callback : recoveryCallbacksCopy) {
            try {
                callback.messagesChanged();
            } catch (RemoteException e) {
                unresponsive.add(callback);
            }
        }

        synchronized (callbacks) {
            for (IMessagesChangedCallback cb : unresponsive) {
                recoveryCallbacks.remove(cb);
            }
        }
    }

    @Override
    public void addMessagesChangedCallback(String recoveryId, IMessagesChangedCallback callback) {

        synchronized (callbacks) {
            if (callbacks.get(recoveryId) == null)
                callbacks.put(recoveryId, new HashSet<>());

            callbacks.get(recoveryId).add(callback);
        }
    }

    @Override
    public void removeMessagesChangedCallback(String recoveryId, IMessagesChangedCallback callback) {
        synchronized (callbacks) {
            if (callbacks.get(recoveryId) == null)
                return;

            callbacks.get(recoveryId).remove(callback);
        }
    }


}
