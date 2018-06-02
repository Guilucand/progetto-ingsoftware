package it.ingsoftw.progetto.common;

public class Drug {

    public Drug(String company, String drugDescription, String packageDescription, String drugCode, String packageCode, String atcCode, String atcDescription, String aic, String packageKey) {
        this.company = company;
        this.drugDescription = drugDescription;
        this.packageDescription = packageDescription;
        this.drugCode = drugCode;
        this.packageCode = packageCode;
        this.atcCode = atcCode;
        this.atcDescription = atcDescription;
        this.aic = aic;
        this.packageKey = packageKey;
    }

    public final String company;
    public final String drugDescription;
    public final String packageDescription;

    public final String drugCode;
    public final String packageCode;
    public final String atcCode;


    public final String atcDescription;

    public final String aic;
    public final String packageKey;

}
