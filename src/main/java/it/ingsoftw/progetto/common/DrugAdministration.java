package it.ingsoftw.progetto.common;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class DrugAdministration implements Serializable {
    public DrugAdministration(int prescriptionKey,
                              LocalDateTime administrationDateTime,
                              String qty, String notes,
                              User nurse) {

        this.prescriptionKey = prescriptionKey;
        this.administrationDateTime = administrationDateTime;
        this.qty = qty;
        this.notes = notes;
        this.nurse = nurse;
    }

    public final int prescriptionKey;
    public final LocalDateTime administrationDateTime;
    public final String qty;
    public final String notes;

    public final User nurse;
}
