package test.database;

import it.ingsoftw.progetto.common.MonitorData;
import it.ingsoftw.progetto.common.PatientData;
import it.ingsoftw.progetto.common.User;
import it.ingsoftw.progetto.common.messages.IMessage;
import it.ingsoftw.progetto.common.utils.Password;
import it.ingsoftw.progetto.server.database.*;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;

public class TestDataAddition {
    public static void addTestData(IDatabaseConnection connection) throws RemoteException, SQLException {
        IUsersDatabase usersDatabase = connection.getUsersInterface();
        IRecoveryDatabase recoveryDatabase = connection.getRecoveryInterface();
        IMessageDatabase messageDatabase = connection.getMessageInterface();
        IPrescriptionDatabase prescriptionDatabase = connection.getPrescriptionInterface();
        IDrugsDatabase drugsDatabase = connection.getDrugsInterface();
        IPatientsDatabase patientsDatabase = connection.getPatientsInterface();

        usersDatabase.addUser(new User(
                "primario",
                "Luigi",
                "Rossi",
                "luigi.rossi@test.it",
                User.UserType.Primary
        ), Password.fromPassword("password"));

        usersDatabase.addUser(new User(
                "medico",
                "Luisa",
                "Alberti",
                "luisa.alberti@test.it",
                User.UserType.Medic
        ), Password.fromPassword("password"));

        usersDatabase.addUser(new User(
                "infermiere",
                "Gianni",
                "Grolli",
                "gianni.poli@test.it",
                User.UserType.Nurse
        ), Password.fromPassword("password"));

        usersDatabase.addUser(new User(
                "amministratore",
                "Linus",
                "Torvalds",
                "linus.torvalds@test.it",
                User.UserType.Admin
        ), Password.fromPassword("password"));


        patientsDatabase.addPatient(new PatientData(
                "BSFFYP36B57D038G",
                "Filippo",
                "Bassi",
                LocalDate.of(1990, 11, 4),
                "Roma"
        ));

        patientsDatabase.addPatient(new PatientData(
                "GGHQSN70H25I627O",
                "Gino",
                "Quad",
                LocalDate.of(1957, 9, 7),
                "Milano"
        ));

        patientsDatabase.addPatient(new PatientData(
                "JZZSML29A61G713M",
                "John",
                "Jackyl",
                LocalDate.of(1937, 4, 17),
                "Milano"
        ));

        patientsDatabase.addPatient(new PatientData(
                "SLNLBS74H61A546K",
                "Albus",
                "Silente",
                LocalDate.of(1812, 6, 27),
                "Londra"
        ));

        for (int i = 0; i < 10; i++)
            recoveryDatabase.setRoomMachineId(String.valueOf(i), String.valueOf(i));

        Integer r1 = recoveryDatabase.addRecovery(patientsDatabase.getPatientByCode("SLNLBS74H61A546K"), "1");
        if (r1 != null)
            recoveryDatabase.addDiagnosis(r1, "Mal di pancia.");

        Integer r2 = recoveryDatabase.addRecovery(patientsDatabase.getPatientByCode("JZZSML29A61G713M"), "4");
        if (r2 != null)
            recoveryDatabase.addDiagnosis(r2, "Trauma cranico.");

        MonitorData md = new MonitorData(60, 115, 75, 36.5f);
        Random random = new Random();

        if (r1 != null) {
            for (int i = 0; i < 100; i++) {
                ((RecoveryDatabase) recoveryDatabase).addMonitorDataToDatabase(
                        r1,
                        LocalDateTime.now().minusMinutes(i),
                        md
                );
                md = new MonitorData(md.getBpm() + random.nextInt(3) - 1,
                        md.getSbp() + random.nextInt(3) - 1,
                        md.getDbp() + random.nextInt(3) - 1,
                        md.getTemp() + (float) random.nextDouble() * 0.1f);
            }
        }

        Integer r3 = recoveryDatabase.addRecovery(patientsDatabase.getPatientByCode("BSFFYP36B57D038G"), "7");
//        recoveryDatabase.addDiagnosis(r1, "Frattura femore.");



    }
}
