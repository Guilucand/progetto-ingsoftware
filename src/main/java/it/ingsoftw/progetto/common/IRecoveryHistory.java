package it.ingsoftw.progetto.common;

import javafx.util.Pair;

import java.rmi.Remote;
import java.time.LocalDateTime;
import java.util.List;

public interface IRecoveryHistory extends Remote {

    class RecoveryInfo {

        public RecoveryInfo(int recoveryKey, String patientCode, LocalDateTime beginDate, LocalDateTime endDate, String diagnosis, String dimissionLetter) {
            this.recoveryKey = recoveryKey;
            this.patientCode = patientCode;
            this.beginDate = beginDate;
            this.endDate = endDate;
            this.diagnosis = diagnosis;
            this.dimissionLetter = dimissionLetter;
        }

        public int getRecoveryKey() {
            return recoveryKey;
        }

        public String getPatientCode() {
            return patientCode;
        }

        public LocalDateTime getBeginDate() {
            return beginDate;
        }

        public LocalDateTime getEndDate() {
            return endDate;
        }

        public String getDiagnosis() {
            return diagnosis;
        }

        public String getDimissionLetter() {
            return dimissionLetter;
        }

        int recoveryKey;
        String patientCode;
        LocalDateTime beginDate;
        LocalDateTime endDate;

        String diagnosis;
        String dimissionLetter;
    }

    RecoveryInfo getRecoveriesList(LocalDateTime begin, LocalDateTime end);


    List<Pair<LocalDateTime, String>> getEventsBetween(int recoveryKey, LocalDateTime begin, LocalDateTime end);
    List<Pair<LocalDateTime, MonitorData>> getMonitorsBetween(int recoveryKey, LocalDateTime begin, LocalDateTime end);
}
