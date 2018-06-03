package it.ingsoftw.progetto.common.utils;

import java.security.SecureRandom;
import java.util.Random;

/**
 * Taken from https://stackoverflow.com/questions/7111651/how-to-generate-a-secure-random-alphanumeric-string-in-java-efficiently
 */
public final class RandomString
{

    /* Assign a string that contains the set of characters you allow. */
    private static final String symbols = "ABCDEFGJKLMNPRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+-/*";

    private final Random random = new SecureRandom();

    private final char[] buf;

    /**
     * Inizializza la classe con una lunghezza predefinita
     * @param length la lunghezza della stringa
     */
    public RandomString(int length)
    {
        if (length < 1)
            throw new IllegalArgumentException("length < 1: " + length);
        buf = new char[length];
    }

    /**
     * Ritorna una stringa casuale sicura
     * @return la stringa
     */
    public String nextString()
    {
        for (int idx = 0; idx < buf.length; ++idx)
            buf[idx] = symbols.charAt(random.nextInt(symbols.length()));
        return new String(buf);
    }

}
