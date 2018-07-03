package it.ingsoftw.progetto.server.database;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import it.ingsoftw.progetto.common.messages.DimissionMessage;
import it.ingsoftw.progetto.common.messages.MessageObject;
import it.ingsoftw.progetto.server.ServerMessageDispatcher;
import javafx.util.Pair;

public class MessageDatabase implements IMessageDatabase {

    private Set<MessageObject> persistentMessages;
    private Set<ServerMessageDispatcher> messageDispatchers;
    private Connection databaseConnection;

    public MessageDatabase(Connection databaseConnection) {
        this.databaseConnection = databaseConnection;

        this.persistentMessages = new HashSet<>();
        this.messageDispatchers = new HashSet<>();

        DatabaseUtils.createDatabaseFromSchema(databaseConnection, "schema/message.sql");
    }

    private boolean addMessageToDatabase(MessageObject message) {
        if (message == null
                || !message.loggedMessage()
                || message.getRecoveryKey() == null
                || message.getMessageText() == null)
            return false;

        String sql = "INSERT INTO message (recoveryKey, dateTime, messageText) " +
                "VALUES (?, ?, ?);";

        try {
            PreparedStatement addMessage = databaseConnection.prepareStatement(sql);
            addMessage.setInt(1, message.getRecoveryKey());
            addMessage.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            addMessage.setString(3, message.getMessageText());

            if (addMessage.executeUpdate() > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Pair<LocalDateTime, String>> getMessagesForRecovery(int recoveryKey, LocalDateTime begin, LocalDateTime end) {
        String sql = "SELECT dateTime, messageText " +
                "FROM message " +
                "WHERE (recoveryKey = ?) AND (dateTime BETWEEN ? AND ?);";

        List<Pair<LocalDateTime, String>> messages = new ArrayList<>();

        try {
            PreparedStatement getMessages = databaseConnection.prepareStatement(sql);
            getMessages.setInt(1, recoveryKey);
            getMessages.setTimestamp(2, Timestamp.valueOf(begin));
            getMessages.setTimestamp(3, Timestamp.valueOf(end));

            ResultSet result = getMessages.executeQuery();

            while (result.next()) {
                messages.add(new Pair<>(result.getTimestamp(1).toLocalDateTime(),
                                        result.getString(2)));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    @Override
    public synchronized void addPersistentMessage(MessageObject message) {
        persistentMessages.add(message);
        addMessageToDatabase(message);


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
        addMessageToDatabase(message);

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
