package it.ingsoftw.progetto.vsmonitor;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.Random;

import it.ingsoftw.progetto.common.IVSListener;
import it.ingsoftw.progetto.common.MonitorData;

/**
 * Classe rappresentante una macchina di monitoraggio
 */
public class VsInstance {

    public final String ID;
    public final VsGui GUI;

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


    public VsInstance(String id) {
        ID = id;
        GUI = new VsGui(id);
        GUI.setUpdateFunction(this::generateDistribution);
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
