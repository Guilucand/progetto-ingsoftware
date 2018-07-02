package it.ingsoftw.progetto.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.List;

import it.ingsoftw.progetto.common.EditablePatientData;
import it.ingsoftw.progetto.common.IRecoveryCreator;
import it.ingsoftw.progetto.common.PatientData;
import it.ingsoftw.progetto.server.database.IPatientsDatabase;
import it.ingsoftw.progetto.server.database.IRecoveryDatabase;

public class ServerRecoveryCreator extends UnicastRemoteObject implements IRecoveryCreator {

    private ClientStatus status;
    private IPatientsDatabase patientsDatabase;
    private IRecoveryDatabase recoveryDatabase;
    private int roomId;

    public ServerRecoveryCreator(ClientStatus status,
                                 IPatientsDatabase patientsDatabase,
                                 IRecoveryDatabase recoveryDatabase, int roomId) throws RemoteException {
        super(ServerConfig.port);
        this.status = status;
        this.patientsDatabase = patientsDatabase;
        this.recoveryDatabase = recoveryDatabase;
        this.roomId = roomId;
    }


    @Override
    public List<String> queryPatientCode(String query) throws RemoteException {
        try {
            return patientsDatabase.searchPatientsByCode(query);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RemoteException(e.getMessage());
        }
    }

    @Override
    public PatientData getPatientFromId(String id) throws RemoteException {
        try {
            return patientsDatabase.getPatientByCode(id);
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public boolean createRecovery(PatientData patientData) throws RemoteException {
        try {
            if (!patientsDatabase.addPatient(patientData)) {
                if (!patientsDatabase.updatePatient(
                        new EditablePatientData(patientData, patientData.getCode())))
                    return false;
            }
        } catch (SQLException e) {
            return false;
        }
        return recoveryDatabase.addRecovery(patientData.getCode(), String.valueOf(roomId)) != null;
    }
}
