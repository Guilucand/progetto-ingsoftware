package it.ingsoftw.progetto.common;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Classe contenente i dati anagrafici di un paziente
 */
public class PatientData implements Serializable {

    public PatientData(String code, String name, String surname, LocalDate birthDate, String birthPlace) {
        this.code = code;
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.birthPlace = birthPlace;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    String code;
    String name;
    String surname;
    LocalDate birthDate;
    String birthPlace;
}
