package it.ingsoftw.progetto.common.messages;

public class AddedDiagnosisMessage extends MessageObject {
    public static final int CONSTRUCTOR = 0xADDD1A6;

    private String diagnosis;

    public AddedDiagnosisMessage(int recoveryKey, String roomId, String diagnosis) {
        super(recoveryKey, roomId);

        this.diagnosis = diagnosis;
    }

    @Override
    public int getMessageType() {
        return CONSTRUCTOR;
    }

    @Override
    public String getMessageText() {
        return "Aggiunta diagnosi: " + diagnosis;
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
