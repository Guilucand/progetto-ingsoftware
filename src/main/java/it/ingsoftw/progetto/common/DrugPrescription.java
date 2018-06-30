package it.ingsoftw.progetto.common;

import java.io.Serializable;
import java.sql.Date;

public class DrugPrescription implements Serializable {

    public DrugPrescription(Drug drug, Date prescriptionDate,
                            int durationDays, int dailyDoses,
                            String qtyPerDose, String notes,
                            User doctor) {
        this.drug = drug;
        this.prescriptionDate = prescriptionDate;
        this.durationDays = durationDays;
        this.dailyDoses = dailyDoses;
        this.qtyPerDose = qtyPerDose;
        this.notes = notes;
        this.doctor = doctor;
    }

    public final Drug drug;
    public final Date prescriptionDate;
    public final int durationDays;
    public final int dailyDoses;
    public final String qtyPerDose;
    public final String notes;

    public final User doctor;
}
