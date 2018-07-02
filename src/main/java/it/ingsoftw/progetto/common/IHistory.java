package it.ingsoftw.progetto.common;

import java.rmi.Remote;
import java.time.LocalDateTime;
import java.util.List;

public interface IHistory extends Remote {

    List<IRecovery> getRecoveriesInDateRange(LocalDateTime begin, LocalDateTime end);
    List<IRecovery> getRecoveriesByPatient(String patientCode);

}
