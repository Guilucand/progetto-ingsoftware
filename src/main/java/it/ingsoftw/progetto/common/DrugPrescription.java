package it.ingsoftw.progetto.common;

import java.io.Serializable;
import java.time.LocalDate;

public class DrugPrescription implements Serializable {

    public DrugPrescription(Drug drug, LocalDate prescriptionDate,
                            String durationDays, String dailyDoses,
                            String qtyPerDose, String notes,
                            User doctor) {
        this.drug = drug;
        this.prescriptionDate = prescriptionDate;
        this.durationDays = durationDays;
        this.dailyDoses = dailyDoses;
        this.qtyPerDose = qtyPerDose;
        this.notes = notes;
        this.doctor = doctor;
        this.key = 0;
    }

    public DrugPrescription(int key, Drug drug, LocalDate prescriptionDate,
                            String durationDays, String dailyDoses,
                            String qtyPerDose, String notes,
                            User doctor) {
        this.drug = drug;
        this.prescriptionDate = prescriptionDate;
        this.durationDays = durationDays;
        this.dailyDoses = dailyDoses;
        this.qtyPerDose = qtyPerDose;
        this.notes = notes;
        this.doctor = doctor;
        this.key = key;
    }

    public final int key;
    public final Drug drug;
    public final LocalDate prescriptionDate;
    public final String durationDays;
    public final String dailyDoses;
    public final String qtyPerDose;
    public final String notes;

    public final User doctor;
}
