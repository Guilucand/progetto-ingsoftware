package it.ingsoftw.progetto.common.messages;

import java.io.Serializable;
import java.util.EnumSet;

import it.ingsoftw.progetto.common.ILogin;

/**
 * Interfaccia che rappresenta un
 * messaggio non urgente per un
 * determinato paziente
 * Es. compilazione report
 */
public abstract class MessageObject implements Serializable {

    /**
     * Funzione che ritorna il tipo del messaggio
     */
    public abstract int getMessageType();

    /**
     * Funzione che ritorna il testo del messaggio
     * cambia a seconda del privilegio
     * @return testo del messaggio
     */
    public abstract String getMessageText();

    /**
     * Ritorna true se l'utente
     * corrente ha i privilegi necessari
     * per completare il messaggio
     * @return true se si dispongono dei privilegi necessari
     */
    public abstract boolean canCompleteMessage();

}
