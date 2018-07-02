package it.ingsoftw.progetto.server.database;

import java.util.List;

import it.ingsoftw.progetto.common.messages.IMessagesChangedCallback;
import it.ingsoftw.progetto.common.messages.MessageObject;

public interface IMessageDatabase {

    List<MessageObject> getMessagesForRecovery(String recoveryId);

    void addMessage(String recoveryId, String key, MessageObject message);

    void completeMessage(String recoveryId, String key);

    void addSingleTimeMessage(String recoveryId, String key, MessageObject message);

    void addMessagesChangedCallback(String recoveryId, IMessagesChangedCallback callback);

    void removeMessagesChangedCallback(String recoveryId, IMessagesChangedCallback callback);
}
