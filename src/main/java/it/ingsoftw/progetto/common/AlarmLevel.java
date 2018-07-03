package it.ingsoftw.progetto.common;

/**
 * Enumerazione delle gravita' degli allarmi
 */
public enum AlarmLevel {

    NoAlarm(0),
    /**
     * Livello piu' basso, intervento in 3 minuti
     */
    Level1(1),
    /**
     * Livello intermedio basso, intervento in 2 minuti
     */
    Level2(2),
    /**
     * Livello piu' grave, intervento in 1 minuto
     */
    Level3(3);

    private int gravity;
    AlarmLevel(int gravity){

        this.gravity = gravity;
    }

    public static AlarmLevel getMaximum(AlarmLevel a, AlarmLevel b) {
        return a.gravity > b.gravity ? a : b;
    }

}
