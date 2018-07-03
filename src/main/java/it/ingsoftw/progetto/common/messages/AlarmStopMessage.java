package it.ingsoftw.progetto.common.messages;

import it.ingsoftw.progetto.common.AlarmData;

public class AlarmStopMessage extends MessageObject {
    public static final int CONSTRUCTOR= 0xA7A33743;
    private AlarmData alarmData;

    public AlarmStopMessage(int recoveryKey, String roomId, AlarmData alarmData) {
        super(recoveryKey, roomId);
        this.alarmData = alarmData;
    }

    @Override
    public int getMessageType() {
        return CONSTRUCTOR;
    }

    @Override
    public String getMessageText() {
        return "Allarme " + alarmData.getDescription() + " fermato";
    }

    @Override
    public boolean loggedMessage() {
        return true;
    }

    @Override
    public boolean visualizedMessage() {
        return false;
    }

    public AlarmData getAlarmData() {
        return alarmData;
    }
}
