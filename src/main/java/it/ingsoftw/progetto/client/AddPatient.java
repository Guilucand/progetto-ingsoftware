package it.ingsoftw.progetto.client;

import org.jdatepicker.JDatePicker;
import org.jdatepicker.SqlDateModel;

import it.ingsoftw.progetto.common.IRecoveryCreator;
import it.ingsoftw.progetto.common.PatientData;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AddPatient extends JFrame {
    private JPanel MainPanel;
    private JTextField cfTextField;
    private JTextField nameTextField;
    private JTextField locationTextField;
    private JTextField surnameTextField;
    private JButton aggiungiRicoveroButton;
    private JPanel panelDate;
    private IRecoveryCreator recoveryCreator;
    private IAddPatientCallback addPatientCallback;
    private JPanel Panel1;
    private JDatePicker birthDatePicker;
    private AutocompleteJTextFrame autocompleteCF;
    private final String dateFormatString = "dd/MM/yyyy";


    interface IAddPatientCallback {
        void patientAdded() throws RemoteException;
    }

    private void setPatientData(PatientData data) {
        if (data == null)
            return;
        cfTextField.setText(data.getCode());
        nameTextField.setText(data.getName());
        surnameTextField.setText(data.getSurname());
        locationTextField.setText(data.getBirthPlace());

        LocalDate birthDate = data.getBirthDate();

        birthDatePicker.getFormattedTextField().setText(birthDate.format(DateTimeFormatter.ofPattern(dateFormatString)));
    }

    public AddPatient(IRecoveryCreator recoveryCreator, IAddPatientCallback addPatientCallback) {

        super("Aggiunta ricovero");

        this.setResizable(false);

        this.recoveryCreator = recoveryCreator;
        this.addPatientCallback = addPatientCallback;
        this.setContentPane(MainPanel);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        Dimension preferredDimension = new Dimension(600, 400);
        this.MainPanel.setPreferredSize(preferredDimension);


        autocompleteCF = new AutocompleteJTextFrame(cfTextField, new AutocompleteJTextFrame.Searchable() {
            @Override
            public List<String> query(String s) {
                try {
                    if (s.length() == 0)
                        return new ArrayList<>();

                    return recoveryCreator.queryPatientCode(s);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                return new ArrayList<>();
            }

            @Override
            public void choose(String s) {

                try {
                    PatientData patientData = recoveryCreator.getPatientFromId(s);
                    setPatientData(patientData);
                } catch (RemoteException e) {
                    e.printStackTrace();
                    return;
                }
            }
        });


        this.pack();

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((dim.width/2-this.getSize().width/2), (dim.height/2-this.getSize().height/2));

        this.setVisible(true);

        aggiungiRicoveroButton.addActionListener(e -> {

            String name = nameTextField.getText();
            String surname = surnameTextField.getText();
            String patientCode = cfTextField.getText();
            String birthPlace = locationTextField.getText();
            LocalDate birthDate = LocalDate.of(
                    birthDatePicker.getModel().getYear(),
                    birthDatePicker.getModel().getMonth(),
                    birthDatePicker.getModel().getDay());

            try {
                if (recoveryCreator.createRecovery(new PatientData(patientCode,
                        name,
                        surname,
                        birthDate,
                        birthPlace))) {
                    this.dispose();
                    addPatientCallback.patientAdded();
                }
            } catch (RemoteException e1) {
                e1.printStackTrace();
            }
            //iMonitor.


        });
    }

    private void createUIComponents() {
        birthDatePicker = new JDatePicker(new SqlDateModel(), dateFormatString);
    }
}
