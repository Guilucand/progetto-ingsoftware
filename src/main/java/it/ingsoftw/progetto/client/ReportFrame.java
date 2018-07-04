package it.ingsoftw.progetto.client;

import it.ingsoftw.progetto.common.DrugPrescription;
import it.ingsoftw.progetto.common.ILogin;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import it.ingsoftw.progetto.common.IRecoveryHistory;

import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReportFrame extends JFrame{
    private final DefaultListModel<String> recoveryListModel;
    private JList recoveryList;
    private JPanel MainPanel;
    private JScrollPane scrollMainPanel;
    private JButton stampaReportButton;
    private JPanel reportRightPanel;
    private JPanel reportMainPanel;
    private IRecoveryHistory recoveryHistory;
    private List<PrintableReport> printableReports;

    public ReportFrame(ILogin.LoginStatus status,
                       String username,
                       IRecoveryHistory recoveryHistory) {

        super("Pannello dei Report");
        this.recoveryHistory = recoveryHistory;
        this.printableReports = new ArrayList<>();
        this.reportMainPanel.setLayout(new BoxLayout(reportMainPanel, BoxLayout.PAGE_AXIS));

        this.setContentPane(MainPanel);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        if(!(status == ILogin.LoginStatus.PRIMARY_LOGGED)) stampaReportButton.setVisible(false);

        Dimension preferredDimension = new Dimension(1200, 800);
        this.MainPanel.setPreferredSize(preferredDimension);

        this.pack();

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((dim.width/2-this.getSize().width/2), (dim.height/2-this.getSize().height/2));

        List<IRecoveryHistory.RecoveryInfo> recoveryInfoList;

        this.recoveryListModel = new DefaultListModel<>();
        this.recoveryList.setModel(recoveryListModel);

        LocalDateTime now = LocalDateTime.now();

        try {
            recoveryInfoList = recoveryHistory.getRecoveriesList(now.minusWeeks(1), now);
        } catch (RemoteException e) {
            e.printStackTrace();
            return;
        }

        if (recoveryInfoList == null)
            return;

        for (int i = 0; i < recoveryInfoList.size(); i++)
            recoveryListModel.add(i, recoveryInfoList.get(i).getBeginDate().format(Date));


        for (IRecoveryHistory.RecoveryInfo recoveryInfo : recoveryInfoList) {

            try {
                this.printableReports.add(new PrintableReport(true,
                        recoveryInfo.getRecoveryKey(),
                        recoveryHistory,
                        now.minusWeeks(1),
                        now
                        ));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < printableReports.size(); i++) {
            reportMainPanel.add(printableReports.get(i), i);
        }

        this.setVisible(true);


        //Aggiungi la lista dei ricoveri;


        stampaReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                PrintableReport.saveMultipleReports("reportSettimana.pdf", printableReports);
                System.out.println("Stampa il report");


            }
        });


        /*
        Element root = new Element("message");
        Document document = new Document(root);

        Document document = new Document();
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("C:\\temp\\test.pdf"));
            document.open();
            PdfContentByte contentByte = writer.getDirectContent();
            PdfTemplate template = contentByte.createTemplate(500, 500);
            Graphics2D g2 = template.createGraphics(500, 500);
            panel.print(g2);
            g2.dispose();
            contentByte.addTemplate(template, 30, 300);
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            if(document.isOpen()){
                document.close();
            }
        }*/

    }


    public class recoveryListRenderer extends DefaultListCellRenderer {


        public Component getListCellRendererComponent(
                JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
        {

            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            DrugPrescription farmaco = (DrugPrescription)value;

            setText(farmaco.drug.commercialName+" "+farmaco.drug.packageDescription);

            return this;
        }

    }

    /*public void PrintFrameToPDF(File file) {
        try {
            Document d = new Document();
            PdfWriter writer = PdfWriter.getInstance(d, new FileOutputStream(file));
            d.open();

            PdfContentByte cb = writer.getDirectContent();
            PdfTemplate template = cb.createTemplate(PageSize.A4.getWidth(),PageSize.A4.getHeight());
            cb.addTemplate(template, 0, 0);

            Graphics2D g2d = template.createGraphics(PageSize.A4.getWidth(),PageSize.A4.getHeight());
            g2d.scale(0.4, 0.4);

            for(int i=0; i< this.getContentPane().getComponents().length; i++){
                Component c = this.getContentPane().getComponent(i);
                if(c instanceof JLabel || c instanceof JScrollPane){
                    g2d.translate(c.getBounds().x,c.getBounds().y);
                    if(c instanceof JScrollPane){c.setBounds(0,0,(int)PageSize.A4.getWidth()*2,(int)PageSize.A4.getHeight()*2);}
                    c.paintAll(g2d);
                    c.addNotify();
                }
            }


            g2d.dispose();

            d.close();
        } catch (Exception e) {
            System.out.println("ERROR: " + e.toString());
        }
    }*/

}
