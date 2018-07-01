package it.ingsoftw.progetto.server.database;

import java.util.List;

import it.ingsoftw.progetto.common.DrugAdministration;
import it.ingsoftw.progetto.common.DrugPrescription;
import it.ingsoftw.progetto.common.User;

public interface IPrescriptionDatabase {
    boolean addPrescription(String recoveryId, User loggedUser, DrugPrescription prescription);

    boolean addAdministration(String recoveryId, User loggedUser, DrugAdministration administration);

    List<DrugPrescription> getPrescriptions(String recoveryId);
}
