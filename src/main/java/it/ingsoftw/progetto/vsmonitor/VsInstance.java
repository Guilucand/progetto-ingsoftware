package it.ingsoftw.progetto.vsmonitor;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.Random;

import it.ingsoftw.progetto.common.IVSListener;
import it.ingsoftw.progetto.common.MonitorData;
import it.ingsoftw.progetto.server.ServerConfig;

/**
 * Classe rappresentante una macchina di monitoraggio
 */
public class VsInstance {

    public final String ID;
    public final VsGui GUI;
    private int key;

    Random r = new Random();

    float genRandomFluctuation(Random rand, float min, float max, float last, float step) {
        double prob = rand.nextDouble();

        float mult = 0;
        if (prob > 0.95) mult = 0.5f;
        else if (prob > 0.99) mult = 1f;
        else if (prob > 0.993) mult = 2f;
        else if (prob > 0.999) mult = 5f;



        float newval = last + ((float)r.nextGaussian()) * step * mult;
        return Math.min(Math.max(newval, min), max);
    }


    public MonitorData generateDistribution(MonitorData last) {

        int bpm = (int)genRandomFluctuation(r, 40, 160, last.getBpm(), 3);
        int sbp = (int)genRandomFluctuation(r, 40, 160, last.getSbp(), 2);
        int dbp = (int)genRandomFluctuation(r, 40, 160, last.getDbp(), 2);
        float temp = genRandomFluctuation(r, 35.0f, 40.0f, last.getTemp(), 0.2f);

        return new MonitorData(bpm, sbp, dbp, temp);

    }

    IVSListener serverListener;


    public void connect() {
        try {
            String url = "//" + ServerConfig.hostname + ":" + ServerConfig.port + "/vsauth";
            serverListener = (IVSListener) Naming.lookup(url);
            key = serverListener.connectVS(ID);
            GUI.setConnectionStatus(key >= 0);
        }
        catch (Exception e) {
            System.out.println("Connection error: " + e.toString());
            GUI.setConnectionStatus(false);
            return;
        }
    }


    public VsInstance(String id) {

        ID = id;
        GUI = new VsGui(id, this);
        GUI.setUpdateFunction(this::generateDistribution);

        connect();

        GUI.setAlarmCallback((desc, level)-> {
            try {
                return serverListener.startAlarm(key, desc, level);
            } catch (RemoteException e) {
                GUI.setConnectionStatus(false);
            }
            return -1;
        });

        GUI.setAlarmStopCallback((alarmId)-> {
            try {
                return serverListener.stopAlarm(key, alarmId);
            } catch (RemoteException e) {
                GUI.setConnectionStatus(false);
            }
            return false;
        });

        GUI.setConnectionCallback((newStatus)-> {
            if (newStatus) {// Connect
                return true;
            }
            return false;
        });
    }

    public void updateMonitorData(MonitorData data) throws RemoteException {
        if (serverListener != null) {
            serverListener.notifyMonitorUpdate(key, data);
        }
    }
}
