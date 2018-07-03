package it.ingsoftw.progetto.common.messages.persistent;

import it.ingsoftw.progetto.common.AlarmData;
import it.ingsoftw.progetto.common.messages.MessageObject;

import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Messaggio che richiede
 * l'aggiunta di un piccolo report su come l'allarme scattato
 * su un paziente è stato risolto ripristinando
 * così i parametri vitali ai valori normali
 */
public class RequestStoppedAlarmReportMessage extends MessageObject {

    public static final int CONSTRUCTOR = 0x5A355A6E;
    private final AlarmData alarmData;

    public RequestStoppedAlarmReportMessage(Integer recoveryKey, String roomId, AlarmData alarmData) {
        super(recoveryKey, roomId);
        this.alarmData = alarmData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RequestStoppedAlarmReportMessage)) return false;
        RequestStoppedAlarmReportMessage that = (RequestStoppedAlarmReportMessage) o;
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
        return "Crea report allarme  ore " +
                alarmData.getStartTime().format(
                        DateTimeFormatter.ofPattern("HH:mm")) +
                        ": " + alarmData.getDescription();
    }

    @Override
    public boolean loggedMessage() {
        return false;
    }

    @Override
    public boolean visualizedMessage() {
        return true;
    }


}
