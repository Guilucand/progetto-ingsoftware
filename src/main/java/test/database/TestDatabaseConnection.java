package test.database;

import it.ingsoftw.progetto.server.database.IDatabaseConnection;
import it.ingsoftw.progetto.server.database.IDrugsDatabase;
import it.ingsoftw.progetto.server.database.IMessageDatabase;
import it.ingsoftw.progetto.server.database.IPatientsDatabase;
import it.ingsoftw.progetto.server.database.IPrescriptionDatabase;
import it.ingsoftw.progetto.server.database.IRecoveryDatabase;
import it.ingsoftw.progetto.server.database.IUsersDatabase;

public class TestDatabaseConnection implements IDatabaseConnection {

    private IUsersDatabase usersDatabase = new TestUsersDatabase();
    private IRecoveryDatabase recoveryDatabase = new TestRecoveryDatabase();

    @Override
    public IUsersDatabase getUsersInterface() {
        return usersDatabase;
    }

    @Override
    public IPatientsDatabase getPatientsInterface() {
        return null;
    }

    @Override
    public IRecoveryDatabase getRecoveryInterface() {
        return recoveryDatabase;
    }

    @Override
    public IDrugsDatabase getDrugsInterface() {
        return null;
    }

    @Override
    public IMessageDatabase getMessageInterface() {
        return null;
    }

    @Override
    public IPrescriptionDatabase getPrescriptionInterface() {
        return null;
    }
}
