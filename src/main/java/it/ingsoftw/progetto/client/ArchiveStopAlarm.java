package it.ingsoftw.progetto.client;

import it.ingsoftw.progetto.common.IRoom;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ArchiveStopAlarm extends JFrame{
    private JTextArea activityReportTextArea;
    private JButton archiveActivity;
    private JPanel MainPanel;

    public ArchiveStopAlarm(IRoom room, String user) {

        super("Rapporto spegnimento allarme");

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        this.MainPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(1,1,1,1,Color.black),user,TitledBorder.TOP,TitledBorder.CENTER));
        ((javax.swing.border.TitledBorder) this.MainPanel.getBorder()).setTitleFont(new Font("Droid Serif", Font.ITALIC, 14));

        Dimension preferredDimension = new Dimension(400, 600);
        MainPanel.setPreferredSize(preferredDimension);

        this.setContentPane(MainPanel);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);



        archiveActivity.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String activityReport = "";

                activityReport = activityReportTextArea.getText();

                //Archivia report sulle attività fatte al paziente per farlo tornare allo stato normale

            }
        });
    }
}
