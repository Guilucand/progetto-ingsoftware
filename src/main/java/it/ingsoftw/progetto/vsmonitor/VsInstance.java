package it.ingsoftw.progetto.vsmonitor;

import java.rmi.Naming;
import java.rmi.RemoteException;

import it.ingsoftw.progetto.common.IVSListener;

/**
 * Classe rappresentante una macchina di monitoraggio
 */
public class VsInstance {

    public final String ID;
    public final VsGui GUI;

    public VsInstance(String id) {
        ID = id;
        GUI = new VsGui(id);
        VsConnection conn;
        IVSListener serverListener;

        try {
            conn = new VsConnection(GUI);
            String url = "//localhost:8080/vsauth";
            serverListener = (IVSListener) Naming.lookup(url);
            serverListener.connectVS(ID, conn);
            GUI.setConnectionStatus(true);
        }
        catch (Exception e) {
            System.out.println("Connection error: " + e.toString());
            GUI.setConnectionStatus(false);
            return;
        }

        GUI.setAlarmCallback((desc, level)-> {
            try {
                return serverListener.startAlarm(ID, desc, level);
            } catch (RemoteException e) {
                GUI.setConnectionStatus(false);
            }
            return -1;
        });

        GUI.setAlarmStopCallback((alarmId)-> {
            try {
                return serverListener.stopAlarm(ID, alarmId);
            } catch (RemoteException e) {
                GUI.setConnectionStatus(false);
            }
            return false;
        });

        GUI.setConnectionCallback((newStatus)-> {
            throw new UnsupportedOperationException(); // Da implementare
        });
    }
}
