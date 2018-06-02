package it.ingsoftw.progetto.common;

public interface IMonitor {
    IPatient[] getHospitalizedPatients();
    IPatient getPatientByRoomNumber(int roomNumber);
}
