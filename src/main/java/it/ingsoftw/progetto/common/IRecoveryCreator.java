package it.ingsoftw.progetto.common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IRecoveryCreator extends Remote {

    /**
     * Esegue una query sul database per ottenere tutti gli
     * id che iniziano con la stringa specificata
     * @param query il prefisso degli id
     * @return la lista degli id
     * @throws RemoteException
     */
    List<String> queryPatientCode(String query) throws RemoteException;

    /**
     * Ritorna un paziente da un id
     * @param id l'id
     * @return il paziente
     * @throws RemoteException
     */
    PatientData getPatientFromId(String id) throws RemoteException;

    /**
     * Crea un nuovo ricovero nella stanza corrente
     * @param patientData il paziente ricoverato
     * @throws RemoteException
     */
    boolean createRecovery(PatientData patientData) throws RemoteException;


}
