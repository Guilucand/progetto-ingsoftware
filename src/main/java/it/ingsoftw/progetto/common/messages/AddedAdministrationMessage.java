package it.ingsoftw.progetto.common.messages;

import it.ingsoftw.progetto.common.DrugAdministration;
import it.ingsoftw.progetto.common.DrugPrescription;

import java.time.format.DateTimeFormatter;

public class AddedAdministrationMessage extends MessageObject {
    public static final int CONSTRUCTOR = 0xADDAD357;
    private DrugAdministration administration;
    private final DrugPrescription prescription;

    public AddedAdministrationMessage(Integer recoveryKey,
                                         String roomId,
                                         DrugAdministration administration,
                                         DrugPrescription prescription) {
        super(recoveryKey, roomId);
        this.administration = administration;
        this.prescription = prescription;
    }

    @Override
    public int getMessageType() {
        return CONSTRUCTOR;
    }

    @Override
    public String getMessageText() {
        return "Aggiunta somministrazione ore: " +
                administration.administrationDateTime.format(DateTimeFormatter.ofPattern("HH:mm")) + " " +
                prescription.drug.commercialName + " Quantita':" + administration.qty + " Note: " + administration.notes;
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
