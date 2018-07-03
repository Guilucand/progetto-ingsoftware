package it.ingsoftw.progetto.common.messages.persistent;

import it.ingsoftw.progetto.common.AlarmData;
import it.ingsoftw.progetto.common.messages.MessageObject;

import java.util.Objects;

public class AlarmStartMessage extends MessageObject {
    public static final int CONSTRUCTOR = 0x57A77E55;

    private AlarmData alarmData;

    public AlarmStartMessage(int recoveryKey, String roomId, AlarmData alarmData) {
        super(recoveryKey, roomId);
        this.alarmData = alarmData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AlarmStartMessage that = (AlarmStartMessage) o;
        return Objects.equals(alarmData, that.alarmData);
    }

    @Override
    public int hashCode() {

        return Objects.hash(alarmData);
    }

    @Override
    public int getMessageType() {
        return CONSTRUCTOR;
    }

    @Override
    public String getMessageText() {
        return alarmData.getDescription();
    }

    @Override
    public boolean loggedMessage() {
        return true;
    }

    @Override
    public boolean visualizedMessage() {
        return true;
    }

    public AlarmData getAlarmData() {
        return alarmData;
    }
}
