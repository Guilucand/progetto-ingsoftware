package it.ingsoftw.progetto.common.messages;


/**
 * Messaggio che richiede
 * l'aggiunta di una diagnosi
 */
public class AddDiagnosisMessage extends MessageObject {

    public static final int CONSTRUCTOR = 0xABDF1A6;

    @Override
    public int getMessageType() {
        return CONSTRUCTOR;
    }

    @Override
    public String getMessageText() {
        return "Compila report di fine allarme";
    }
}
