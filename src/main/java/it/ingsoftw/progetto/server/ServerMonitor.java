package it.ingsoftw.progetto.server;

import it.ingsoftw.progetto.common.IMonitor;
import it.ingsoftw.progetto.common.IPatient;
import it.ingsoftw.progetto.server.database.IRecoveryDatabase;

public class ServerMonitor implements IMonitor {

    private IRecoveryDatabase recoveryDatabase;

    public ServerMonitor(IRecoveryDatabase recoveryDatabase) {
        this.recoveryDatabase = recoveryDatabase;
    }

    @Override
    public IPatient[] getHospitalizedPatients() {
        return new IPatient[1];
    }

    @Override
    public IPatient getPatientByRoomNumber(int roomNumber) {
        return new ServerPatient(recoveryDatabase, "1");
    }
}
