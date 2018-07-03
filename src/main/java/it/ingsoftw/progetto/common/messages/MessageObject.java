package it.ingsoftw.progetto.common.messages;

import java.io.Serializable;
import java.util.EnumSet;

import it.ingsoftw.progetto.common.ILogin;

/**
 * Interfaccia che rappresenta un
 * messaggio generico, che puo' essere
 * associato ad un determinato paziente
 * Es. compilazione report
 */
public abstract class MessageObject implements Serializable {

    private final Integer recoveryKey;
    private final String roomId;

    protected MessageObject(Integer recoveryKey, String roomId) {
        this.recoveryKey = recoveryKey;
        this.roomId = roomId;
    }

    /**
     * Funzione che ritorna il tipo del messaggio
     */
    public abstract int getMessageType();

    /**
     * Funzione che ritorna il testo del messaggio
     * utilizzata per la visualizzazione o il
     * salvataggio su database dei logs.
     * @return testo del messaggio
     */
    public abstract String getMessageText();

    /**
     * Ritorna la chiave del ricovero a cui
     * e' associato il messaggio
     * @return la chiave o null se non associato
     * ad un ricovero.
     */
    public Integer getRecoveryKey() {
        return recoveryKey;
    }

    /**
     * Ritorna il nome della stanza a cui
     * e' associato il messaggio
     * @return il nome o null se non associato
     * ad una stanza.
     */
    public String getRoom() {
        return roomId;
    }


    /**
     * Ritorna true se questo e' un messaggio
     * importante, che deve rimanere
     * salvato nello storico del ricovero
     */
    public abstract boolean loggedMessage();

    /**
     * Ritorna true se questo messaggio deve essere
     * visualizzato tra la lista dei messaggi di un paziente
     * @return
     */
    public abstract boolean visualizedMessage();

}
