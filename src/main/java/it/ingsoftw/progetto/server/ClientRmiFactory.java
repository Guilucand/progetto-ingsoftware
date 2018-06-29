package it.ingsoftw.progetto.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import it.ingsoftw.progetto.common.IAdmin;
import it.ingsoftw.progetto.common.IClientRmiFactory;
import it.ingsoftw.progetto.common.ILogin;
import it.ingsoftw.progetto.common.IMonitor;
import it.ingsoftw.progetto.server.database.IDatabaseConnection;
import it.ingsoftw.progetto.server.database.IUsersDatabase;
import test.database.TestUsersDatabase;

public class ClientRmiFactory extends UnicastRemoteObject implements IClientRmiFactory {

    ILogin loginInterface;
    IMonitor monitorInterface;
    IAdmin adminInterface;
    IDatabaseConnection databaseConnection;
    ClientStatus status;


    protected ClientRmiFactory(IDatabaseConnection databaseConnection) throws RemoteException {
        super(ServerConfig.port);
        this.databaseConnection = databaseConnection;
    }

    @Override
    public ILogin getLoginInterface() throws RemoteException{
        if (loginInterface == null) {
            loginInterface = new ServerLogin(status, databaseConnection.getUsersInterface());
        }
        return loginInterface;
    }

    @Override
    public IMonitor getMonitorInterface() throws RemoteException {
        if (monitorInterface == null) {
            monitorInterface = new ServerMonitor(status, databaseConnection.getRecoveryInterface());
        }
        return monitorInterface;
    }

    @Override
    public IAdmin getAdminInterface() throws RemoteException {
        if (adminInterface == null) {
//            adminInterface = new
        }
        return adminInterface;
    }
}
