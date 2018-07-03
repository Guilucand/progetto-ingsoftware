package it.ingsoftw.progetto.server;

import it.ingsoftw.progetto.common.messages.IMessage;
import it.ingsoftw.progetto.common.messages.MessageObject;
import it.ingsoftw.progetto.server.database.IMessageDatabase;
import it.ingsoftw.progetto.server.database.MessageDatabase;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ServerMessageDispatcher extends UnicastRemoteObject implements IMessage {
    private IMessageDatabase messageDatabase;

    private BlockingQueue<MessageObject> messageQueue;

    public void addMessage(MessageObject message) {
        try {
            messageQueue.add(message);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ServerMessageDispatcher(IMessageDatabase messageDatabase) throws RemoteException {
        super(ServerConfig.port);
        this.messageDatabase = messageDatabase;
        this.messageQueue = new ArrayBlockingQueue<>(1024);
        messageDatabase.registerDispatcher(this);
    }

    @Override
    public MessageObject getNextMessage() {
        while (true) {
            try {
                return messageQueue.take();
            } catch (InterruptedException ignored) {
            }
        }
    }
}
