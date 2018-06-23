package it.ingsoftw.progetto.server.database;

public interface IDatabaseConnection {
    IUsersDatabase getUsersInterface();
    IPatientsDatabase getPatientsInterface();
    IRecoveryDatabase getRecoveryInterface();
}
