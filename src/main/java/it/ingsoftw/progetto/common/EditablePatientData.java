package it.ingsoftw.progetto.common;



/**
 * Classe decoratrice che abilita la modifica di un paziente
 */
public class EditablePatientData extends PatientData {

    private String reference;

    public EditablePatientData(PatientData patientData, String reference) {
        super(patientData.code,
                patientData.name,
                patientData.surname,
                patientData.birthDate,
                patientData.birthPlace);
        this.reference = reference;
    }

    public String getInternalReference() {
        return reference;
    }
}
