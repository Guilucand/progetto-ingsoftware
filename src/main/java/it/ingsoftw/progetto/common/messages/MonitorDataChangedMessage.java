package it.ingsoftw.progetto.common.messages;

import it.ingsoftw.progetto.common.MonitorData;

public class MonitorDataChangedMessage extends MessageObject {
    public static final int CONSTRUCTOR= 0x38463D35;

    private int recoveryKey;
    private MonitorData data;

    public MonitorDataChangedMessage(int recoveryKey, String roomId, MonitorData data) {
        super(recoveryKey, roomId);

        this.recoveryKey = recoveryKey;
        this.data = data;
    }

    @Override
    public int getMessageType() {
        return CONSTRUCTOR;
    }

    @Override
    public String getMessageText() {
        return null;
    }

    @Override
    public Integer getRecoveryKey() {
        return recoveryKey;
    }

    @Override
    public boolean loggedMessage() {
        return false;
    }

    @Override
    public boolean visualizedMessage() {
        return false;
    }

    public MonitorData getData() {
        return data;
    }
}
