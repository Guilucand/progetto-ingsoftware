package it.ingsoftw.progetto.common;

public class MonitorData {

    public MonitorData(int bpm, int sbp, int dbp, float temp) {
        this.bpm = bpm;
        this.sbp = sbp;
        this.dbp = dbp;
        this.temp = temp;
    }

    public int getBpm() {
        return bpm;
    }

    public int getSbp() {
        return sbp;
    }

    public int getDbp() {
        return dbp;
    }

    public float getTemp() {
        return temp;
    }

    int bpm;
    int sbp;
    int dbp;
    float temp;
}
