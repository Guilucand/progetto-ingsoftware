package it.ingsoftw.progetto.client;

import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import it.ingsoftw.progetto.common.ILogin;

import javax.swing.*;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import com.itextpdf.text.pdf.PdfWriter;

public class ReportFrame extends JFrame{
    private JList recoveryList;
    private JPanel MainPanel;
    private JScrollPane scrollMainPanel;
    private JPanel dataPatientPanel;
    private JLabel imageLabel;
    private JLabel nameParameter;
    private JLabel sbpParameter;
    private JLabel surnameParameter;
    private JLabel dbpParameter;
    private JLabel cfParameter;
    private JLabel frequenceParameter;
    private JLabel dateParameter;
    private JLabel temperatureParameter;
    private JLabel birthlocationParameter;
    private JPanel SBPPanel;
    private JPanel SBPgraphicPanel;
    private JPanel DBPPanel;
    private JScrollPane scrollpaneDBP;
    private JPanel DBPgraphicPanel;
    private JPanel FrequencePanel;
    private JPanel FrequencegraphicPanel;
    private JPanel TemperaturePanel;
    private JPanel temperaturegraphicPanel;
    private JButton stampaReportButton;

    public ReportFrame(ILogin.LoginStatus status, String username) {

        super("Pannello dei Report");

        this.setContentPane(MainPanel);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        if(!(status == ILogin.LoginStatus.PRIMARY_LOGGED)) stampaReportButton.setVisible(false);

        Dimension preferredDimension = new Dimension(1200, 800);
        this.MainPanel.setPreferredSize(preferredDimension);

        this.pack();

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((dim.width/2-this.getSize().width/2), (dim.height/2-this.getSize().height/2));

        this.setVisible(true);


        //Aggiungi la lista dei ricoveri;


        stampaReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                System.out.println("Stampa il report");

            }
        });



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
