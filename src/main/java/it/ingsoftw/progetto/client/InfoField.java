package it.ingsoftw.progetto.client;

import it.ingsoftw.progetto.common.IRecoveryHistory;
import it.ingsoftw.progetto.common.IRoom;
import it.ingsoftw.progetto.common.PatientData;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;

public class InfoField extends JPanel{
    private JPanel dataPatientPanel;
    private JLabel imageLabel;
    private JLabel nameParameter;
    private JLabel surnameParameter;
    private JLabel cfParameter;
    private JLabel dateParameter;
    private JLabel birthlocationParameter;
    private JPanel MainPanel;

    public InfoField(IRecoveryHistory.RecoveryInfo recoveryInfo, IRecoveryHistory recoveryHistory) throws RemoteException {

        this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

        this.setPreferredSize(new Dimension(595,300));

        PatientData datipaz = recoveryHistory.getPatientData(recoveryInfo.getPatientCode());

        nameParameter.setText(datipaz.getName());
        surnameParameter.setText(datipaz.getSurname());
        dateParameter.setText(String.valueOf(datipaz.getBirthDate()));
        birthlocationParameter.setText(datipaz.getBirthPlace());
        cfParameter.setText(datipaz.getCode());

        this.add(MainPanel);

    }


}
