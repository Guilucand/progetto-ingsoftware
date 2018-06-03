package it.ingsoftw.progetto.common;

/**
 * Classe che descrive un farmaco
 */
public class Drug {

    /**
     * Construttore
     * @param company l'azienda produttrice
     * @param drugDescription il nome commerciale del farmaco
     * @param packageDescription la descrizione della confezione
     * @param drugCode codice del farmaco
     * @param packageCode codice della confezione
     * @param atcCode codice atc
     * @param atcDescription principi attivi
     * @param aic codice aic
     * @param packageKey chiave della confezione
     */
    public Drug(String company, String drugDescription, String packageDescription, String drugCode, String packageCode, String atcCode, String atcDescription, String aic, String packageKey) {
        this.company = company;
        this.drugDescription = drugDescription;
        this.packageDescription = packageDescription;
        this.drugCode = drugCode;
        this.packageCode = packageCode;
        this.atcCode = atcCode;
        this.atcDescription = atcDescription;
        this.aic = aic;
        this.packageKey = packageKey;
    }

    /**
     * L'azienda produttrice
     */
    public final String company;

    /**
     * Il nome commerciale del farmaco
     */
    public final String drugDescription;

    /**
     * La descrizione della confezione
     */
    public final String packageDescription;

    public final String drugCode;

    /**
     * Il codice del farmaco
     */
    public final String packageCode;

    /**
     * Il codice atc
     */
    public final String atcCode;

    /**
     * I principi attivi
     */
    public final String atcDescription;

    /**
     * Il codice aic
     */
    public final String aic;

    /**
     * La chiave della confezione
     */
    public final String packageKey;

}
