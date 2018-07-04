package it.ingsoftw.progetto.common.messages;

import it.ingsoftw.progetto.common.AlarmData;

import java.time.format.DateTimeFormatter;
import java.util.Date;

public class AddedStopAlarmMessage extends MessageObject {
    public static final int CONSTRUCTOR = 0x4DDA7E56;
    private String message;
    private AlarmData alarmData;

    public AddedStopAlarmMessage(Integer recoveryKey, String roomId, String message, AlarmData alarmData) {
        super(recoveryKey, roomId);
        this.message = message;
        this.alarmData = alarmData;
    }

    @Override
    public int getMessageType() {
        return CONSTRUCTOR;
    }

    @Override
    public String getMessageText() {
        return "Report allarme " +
                alarmData.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm - ")) +
                alarmData.getDescription() + ": " + message;
    }

    public AlarmData getAlarmData() {
        return alarmData;
    }

    @Override
    public boolean loggedMessage() {
        return true;
    }

    @Override
    public boolean visualizedMessage() {
        return false;
    }
}
