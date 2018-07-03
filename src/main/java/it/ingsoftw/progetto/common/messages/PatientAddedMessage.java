package it.ingsoftw.progetto.common.messages;

import it.ingsoftw.progetto.common.PatientData;

public class PatientAddedMessage extends MessageObject {
    public static final int CONSTRUCTOR = 0xADDED9A7;
    private PatientData patientData;

    public PatientAddedMessage(Integer recoveryKey, String roomId, PatientData patientData) {
        super(recoveryKey, roomId);
        this.patientData = patientData;
    }

    @Override
    public int getMessageType() {
        return CONSTRUCTOR;
    }

    @Override
    public String getMessageText() {
        return "Aggiunto ricovero del paziente " + patientData.getName() + " " + patientData.getSurname();
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
