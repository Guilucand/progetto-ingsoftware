package it.ingsoftw.progetto.common.utils;

import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Consente la trasmissione e lo storage sicuri delle password
 * Effettua un hash della password per offuscarla
 */
public class Password implements Serializable {

    /**
     * Stringa concatenata alla password per evitare i piu'
     * comuni attacchi dizionario
     */
    private static String salt = "cyAUbMtU2bvL6johv$";

    /**
     * Creazione dell'istanza di hashing
     */
    static {
        try {
            sha256Digest = MessageDigest.getInstance("SHA-256");
        }
        catch (NoSuchAlgorithmException ignored) {
        }
    }

    private static MessageDigest sha256Digest;

    /**
     * Crea un'istanza a partire da una password in chiaro
     * @param password la password
     * @return l'istanza della classe Password
     */
    public static Password fromPassword(String password) {
        return new Password(sha256Digest.digest((salt+password).getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * Crea un'istanza a partire da un hash
     * @param hash l'hash
     * @return l'istanza della classe Password
     */
    public static Password fromHash(String hash) {
        if (hash == null)
            return null;
        return new Password(HexBin.decode(hash));
    }

    /**
     * Comparatore per stabilire l'equivalenza delle password
     * @param o l'altra password
     * @return true se le password coincidono
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Password password = (Password) o;
        return Arrays.equals(passHash, password.passHash);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(passHash);
    }

    /**
     * Costruttore privato che accetta un hash in bytes
     * @param hash l'hash
     */
    private Password(byte[] hash) {
        passHash = hash;
    }

    public String getPasswordHash() {
        return HexBin.encode(passHash);
    }

    /**
     * L'hash della password
     */
    private byte[] passHash;
}
