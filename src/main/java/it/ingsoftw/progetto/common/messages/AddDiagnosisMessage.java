package it.ingsoftw.progetto.common.messages;

public class AddDiagnosisMessage extends MessageObject {
    public static final int CONSTRUCTOR = 0xADDD1A6;



    @Override
    public int getMessageType() {
        return CONSTRUCTOR;
    }

    @Override
    public String getMessageText() {
        return "Crea lettera di dimissione";
    }

    @Override
    public boolean canCompleteMessage() {
        return false;
    }
}
