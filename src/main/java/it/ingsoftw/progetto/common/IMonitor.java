package it.ingsoftw.progetto.common;

/**
 * Interfaccia che fornisce metodi per il monitoraggio dei pazienti ricoverati
 */
public interface IMonitor {

    /**
     * Ritorna un array dei pazienti ricoverati in questo momento
     * @return la lista di pazienti
     */
    IPatient[] getHospitalizedPatients();

    /**
     * Ritorna il paziente corrispondente al numero di stanza
     * @param roomNumber il numero di stanza
     * @return il paziente o null se la stanza non esiste o e' vuota
     */
    IPatient getPatientByRoomNumber(int roomNumber);
}
