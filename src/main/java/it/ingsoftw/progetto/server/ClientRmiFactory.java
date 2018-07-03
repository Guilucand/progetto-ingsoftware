package it.ingsoftw.progetto.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import it.ingsoftw.progetto.client.MessagesDispatcher;
import it.ingsoftw.progetto.common.IAdmin;
import it.ingsoftw.progetto.common.IClientRmiFactory;
import it.ingsoftw.progetto.common.ILogin;
import it.ingsoftw.progetto.common.IMonitor;
import it.ingsoftw.progetto.common.messages.IMessage;
import it.ingsoftw.progetto.server.database.IDatabaseConnection;

public class ClientRmiFactory extends UnicastRemoteObject implements IClientRmiFactory {

    ILogin loginInterface;
    IMonitor monitorInterface;
    IAdmin adminInterface;
    IMessage messageInterface;
    IDatabaseConnection databaseConnection;
    ClientStatus status;


    protected ClientRmiFactory(IDatabaseConnection databaseConnection) throws RemoteException {
        super(ServerConfig.port);
        this.databaseConnection = databaseConnection;
        this.status = new ClientStatus();
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
            monitorInterface = new ServerMonitor(status,
                    databaseConnection.getRecoveryInterface(),
                    databaseConnection.getMessageInterface(),
                    databaseConnection.getPatientsInterface(),
                    databaseConnection.getPrescriptionInterface());
        }
        return monitorInterface;
    }

    @Override
    public IAdmin getAdminInterface() throws RemoteException {
        if (adminInterface == null) {
            adminInterface = new ServerUsersAdmin(status, databaseConnection.getUsersInterface());
        }
        return adminInterface;
    }

    @Override
    public IMessage getMessageInterface() throws RemoteException {
        if (messageInterface == null) {
            messageInterface = new ServerMessageDispatcher(databaseConnection.getMessageInterface());
        }
        return messageInterface;
    }
}
