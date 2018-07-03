package it.ingsoftw.progetto.common.messages;

/**
 * Messaggio che annuncia la dimissione di un paziente
 */
public class DimissionMessage extends MessageObject {
    public static final int CONSTRUCTOR = 0xD1415510;
    private String dimissionLetter;

    public DimissionMessage(int recoveryKey, String roomId, String dimissionLetter) {
        super(recoveryKey, roomId);

        this.dimissionLetter = dimissionLetter;
    }


    @Override
    public int getMessageType() {
        return CONSTRUCTOR;
    }

    @Override
    public String getMessageText() {
        return "Dimesso paziente, lettera di dimissioni: " + dimissionLetter;
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
