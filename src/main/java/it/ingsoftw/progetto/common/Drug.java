package it.ingsoftw.progetto.common;

import java.io.Serializable;

/**
 * Classe che descrive un farmaco
 */
public class Drug implements Serializable {

    /**
     * Construttore
     * @param company l'azienda produttrice
     * @param commercialName il nome commerciale del farmaco
     * @param packageDescription la descrizione della confezione
     * @param atcCode codice atc
     * @param activePrinciple principi attivi
     * @param aic codice aic
     */
    public Drug(String company, String commercialName, String packageDescription, String atcCode, String activePrinciple, String aic) {
        this.company = company;
        this.commercialName = commercialName;
        this.packageDescription = packageDescription;
        this.atcCode = atcCode;
        this.activePrinciple = activePrinciple;
        this.aic = aic;
    }

    /**
     * L'azienda produttrice
     */
    public final String company;

    /**
     * Il nome commerciale del farmaco
     */
    public final String commercialName;

    /**
     * La descrizione della confezione
     */
    public final String packageDescription;

    /**
     * Il codice atc
     */
    public final String atcCode;

    /**
     * I principi attivi
     */
    public final String activePrinciple;

    /**
     * Il codice aic
     */
    public final String aic;
}
