package it.ingsoftw.progetto.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import it.ingsoftw.progetto.common.IClientRmiFactory;
import it.ingsoftw.progetto.common.ILogin;
import test.database.TestUsersDatabase;

public class ClientRmiFactory extends UnicastRemoteObject implements IClientRmiFactory {

    ILogin loginInterface;

    public static TestUsersDatabase testUsersDatabase = new TestUsersDatabase();

    protected ClientRmiFactory() throws RemoteException {
        super(ServerConfig.port);
    }

    @Override
    public ILogin getLoginInterface() throws RemoteException{
        if (loginInterface == null) {
            loginInterface = new ServerLogin(testUsersDatabase);
        }
        return loginInterface;
    }
}
