package it.ingsoftw.progetto.common;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Objects;

public class AlarmData implements Serializable {

    public AlarmData(AlarmLevel level, String description, LocalDateTime startTime, int alarmId) {
        this.level = level;
        this.description = description;
        this.startTime = startTime;
        this.alarmId = alarmId;

        switch (this.level) {
            case Level1:
                requiredTime = 60;
                break;
            case Level2:
                requiredTime = 120;
                break;
            case Level3:
                requiredTime = 180;
                break;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AlarmData alarmData = (AlarmData) o;
        return alarmId == alarmData.alarmId;
    }

    @Override
    public int hashCode() {

        return Objects.hash(alarmId);
    }

    public AlarmLevel getLevel() {
        return level;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public int getAlarmId() {
        return alarmId;
    }

    public int getSecondsLeft() {
        return (int)LocalDateTime.now().until(startTime.plusSeconds(requiredTime), ChronoUnit.SECONDS);
    }


    AlarmLevel level;
    String description;
    LocalDateTime startTime;
    int alarmId;
    int requiredTime;
}