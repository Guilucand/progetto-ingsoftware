package it.ingsoftw.progetto.client;

import it.ingsoftw.progetto.common.AlarmData;

import javax.sound.sampled.LineUnavailableException;
import java.util.HashSet;

/**
 * Classe che controlla il
 * suono degli allarmi
 */
public class AlarmSound {

    private static AlarmSound alarmSoundInstance = new AlarmSound();

    public static AlarmSound getInstance() {
        return alarmSoundInstance;
    }

    private AlarmSound() {
        sound = new Thread();
    }

    private Thread sound;
    private HashSet<Integer> activeAlarms = new HashSet<>();

    public synchronized void startAlarmSound(AlarmData alarmData) {

        activeAlarms.add(alarmData.getAlarmId());

        if (sound == null || !sound.isAlive()) {

            sound = new Thread(() -> {
                SoundUtils alarmtone = new SoundUtils();
                try {

                    while (activeAlarms.size() > 0) {

                        // Suono dell'allarme
                        alarmtone.tone(500, 500);
                        alarmtone.tone(1000, 500);
                        alarmtone.tone(0, 500);

                    }
                } catch (LineUnavailableException e1) {
                    e1.printStackTrace();
                }
            });

            sound.start();
        }

    }

    public synchronized void stopAlarmSound(int alarmId) {
        activeAlarms.remove(alarmId);
    }
}
