package it.ingsoftw.progetto.vsmonitor;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import it.ingsoftw.progetto.common.IVSConnection;
import it.ingsoftw.progetto.server.ReverseClientSocket;
import it.ingsoftw.progetto.server.ServerConfig;

public class VsConnection extends UnicastRemoteObject implements IVSConnection {

    VsGui gui;

    public VsConnection(VsGui gui) throws RemoteException {
        super(ServerConfig.reversePort, ReverseClientSocket.getClientSocketFactory(), ReverseClientSocket.getServerSocketFactory());
        this.gui = gui;
    }

    @Override
    public int getBpm() {
        return gui.getBpm();
    }

    @Override
    public int getSbp() {
        return gui.getSbp();
    }

    @Override
    public int getDbp() {
        return gui.getDbp();
    }

    @Override
    public float getTemp() {
        return gui.getTemp();
    }
}
