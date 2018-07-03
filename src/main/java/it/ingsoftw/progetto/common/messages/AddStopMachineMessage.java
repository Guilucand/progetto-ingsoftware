package it.ingsoftw.progetto.common.messages;

/**
 * Messaggio che richiede
 * l'aggiunta di un piccolo report su come l'allarme scattato su di un paziente è stato risolto ripristinando così i parametri vitali ai valori normali
 */

public class AddStopMachineMessage extends MessageObject {

    public static final int CONSTRUCTOR = 0xADDD1A6;

    @Override
    public int getMessageType() {
        return CONSTRUCTOR;
    }

    @Override
    public String getMessageText() {
        return "Crea lettera di dimissione";
    }



}
