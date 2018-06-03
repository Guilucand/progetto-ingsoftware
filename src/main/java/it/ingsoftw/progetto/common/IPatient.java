package it.ingsoftw.progetto.common;

/**
 * Interfaccia di monitoraggio di un paziente attualmente ricoverato
 */
public interface IPatient {

    /**
     * Ottiene i dati anagrafici del paziente
     * @return i dati del paziente
     */
    PatientData getPatientData();

}
