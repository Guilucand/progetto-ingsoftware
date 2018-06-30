package it.ingsoftw.progetto.common;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;

public interface IAlarmCallback extends Remote {

    class AlarmData implements Serializable {

        public AlarmData(AlarmLevel level, String description, Date startTime, int alarmId) {
            this.level = level;
            this.description = description;
            this.startTime = startTime;
            this.alarmId = alarmId;
        }

        public AlarmLevel getLevel() {
            return level;
        }

        public String getDescription() {
            return description;
        }

        public Date getStartTime() {
            return startTime;
        }

        public int getAlarmId() {
            return alarmId;
        }

        AlarmLevel level;
        String description;
        Date startTime;
        int alarmId;
    }

    void startAlarm(AlarmData alarmData) throws RemoteException;
    void stopAlarm(int alarmId) throws RemoteException;


}
