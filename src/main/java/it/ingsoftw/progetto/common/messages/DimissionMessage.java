package it.ingsoftw.progetto.common.messages;

/**
 * Messaggio che annuncia la dimissione di un paziente
 */
public class DimissionMessage extends MessageObject {
    public static final int CONSTRUCTOR = 0xD1415510;

    @Override
    public int getMessageType() {
        return CONSTRUCTOR;
    }

    @Override
    public String getMessageText() {
        return null;
    }
}
