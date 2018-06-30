package it.ingsoftw.progetto.server.database;

import java.util.List;

import it.ingsoftw.progetto.common.DrugPrescription;
import it.ingsoftw.progetto.common.User;

public interface IPrescriptionDatabase {
    void addPrescription(String recoveryId, User loggedUser, DrugPrescription prescription);
    List<DrugPrescription> getPrescriptions();
}
