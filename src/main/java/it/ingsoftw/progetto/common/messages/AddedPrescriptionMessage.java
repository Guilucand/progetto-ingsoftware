package it.ingsoftw.progetto.common.messages;

import it.ingsoftw.progetto.common.DrugPrescription;

public class AddedPrescriptionMessage extends MessageObject {

    public static final int CONSTRUCTOR = 0xADD9325;
    private DrugPrescription prescription;

    public AddedPrescriptionMessage(Integer recoveryKey, String roomId, DrugPrescription prescription) {
        super(recoveryKey, roomId);
        this.prescription = prescription;
    }

    @Override
    public int getMessageType() {
        return CONSTRUCTOR;
    }

    @Override
    public String getMessageText() {
        return "Aggiunta prescrizione " + prescription.drug.commercialName + " " +
                prescription.drug.packageDescription + " " + " Dosi giornaliere': " +
                prescription.dailyDoses + " Quantita':" + prescription.qtyPerDose + " Note: " +
                prescription.notes;
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
