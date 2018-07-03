package it.ingsoftw.progetto.common.messages.persistent;


import it.ingsoftw.progetto.common.messages.MessageObject;

import java.util.Objects;

/**
 * Messaggio che richiede
 * l'aggiunta di una diagnosi
 */
public class RequestDiagnosisMessage extends MessageObject {
    public static final int CONSTRUCTOR = 0x2303554;


    public RequestDiagnosisMessage(int recoveryKey, String roomId) {
        super(recoveryKey, roomId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequestDiagnosisMessage that = (RequestDiagnosisMessage) o;
        return getRecoveryKey() == that.getRecoveryKey();
    }

    @Override
    public int hashCode() {

        return Objects.hash(getRecoveryKey());
    }

    @Override
    public int getMessageType() {
        return CONSTRUCTOR;
    }

    @Override
    public String getMessageText() {
        return "Crea lettera di dimissione";
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
