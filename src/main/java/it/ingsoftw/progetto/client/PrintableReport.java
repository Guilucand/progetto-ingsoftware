package it.ingsoftw.progetto.client;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import it.ingsoftw.progetto.common.IRecoveryHistory;
import javafx.util.Pair;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.html.HTMLDocument;
import java.awt.*;
import java.io.FileOutputStream;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PrintableReport extends JPanel {

    private boolean printable;
    private IRecoveryHistory recoveryHistory;
    private IRecoveryHistory.RecoveryInfo recoveryInfo;
    private HistoryChartsManager graphicList;
    private JTextPane logsTextPanel;
    private static final int YLengthPDF = 1200;


    PrintableReport(boolean printable,
                    int recoveryKey,
                    IRecoveryHistory recoveryHistory,
                    LocalDateTime begin,
                    LocalDateTime end) throws RemoteException {

        this.recoveryHistory = recoveryHistory;
        this.recoveryInfo = recoveryHistory.getRecoveryFromKey(recoveryKey);
        this.graphicList = new HistoryChartsManager(recoveryInfo.getRecoveryKey(), recoveryHistory, begin, end);
        this.printable = printable;
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.setBackground(Color.WHITE);
        this.logsTextPanel = new JTextPane();
        this.logsTextPanel.setContentType("text/html");
        this.logsTextPanel.setEditable(false);

        List<Pair<LocalDateTime, String>> events = this.recoveryHistory.getEventsBetween(recoveryKey, begin, end);


        StringBuilder logs = new StringBuilder();
        logs.append("<h1>Registro degli eventi</h1></br>");

        for (Pair<LocalDateTime, String> event : events) {
            logs.append("<p><b>" +event.getKey().format(DateTimeFormatter.ofPattern("dd/mm/yyyy HH:mm:ss")) +
                        ":</b>   " + event.getValue() + "</p>");
        }


        this.logsTextPanel.setText(logs.toString());

        Dimension A4min = new Dimension(595, YLengthPDF);
        Dimension A4max = new Dimension(595, YLengthPDF*10);


//        this.setPreferredSize(A4);
        this.setMinimumSize(A4min);
        this.setMaximumSize(A4max);
//        this.setSize(A4min);

        if (printable)
            this.setBackground(Color.WHITE);

        this.setBorder(new EmptyBorder(40, 30, 30, 30));

        this.add(new InfoField(recoveryInfo, recoveryHistory));

        this.add(new GraphicField(graphicList));

        this.add(logsTextPanel);
    }



    @Deprecated
    public void printToPdfOld(String path) {

        Document document = new Document(new com.itextpdf.text.Rectangle(
                getPreferredSize().width,
                getPreferredSize().height));
        try {

            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("test.pdf"));
            document.open();

            JFrame preRenderer = new JFrame();
            preRenderer.setBackground(Color.WHITE);
            preRenderer.setContentPane(this);
            preRenderer.pack();
//            test.setVisible(true);

            PdfContentByte contentByte = writer.getDirectContent();

            for (int i = 0; i < 2; i++) {
                document.newPage();
                PdfTemplate template = contentByte.createTemplate(this.getWidth(), this.getHeight());
                Graphics2D g2 = template.createGraphics(this.getWidth(), this.getHeight());
                this.print(g2);
                g2.dispose();
                contentByte.addTemplate(template, 0, 0);
             }

        } catch (Exception e1) {
            e1.printStackTrace();
        }
        finally{
            if(document.isOpen()){
                document.close();
            }
        }
    }

    public PdfTemplate getPdfTemplate(PdfContentByte contentByte) {

        JFrame preRenderer = new JFrame();
        preRenderer.setBackground(Color.WHITE);
        preRenderer.setContentPane(this);
        preRenderer.pack();

        PdfTemplate template = contentByte.createTemplate(this.getWidth(), this.getHeight());
        Graphics2D g2 = template.createGraphics(this.getWidth(), this.getHeight());
        this.print(g2);
        g2.dispose();
        return template;
    }

    public static void saveMultipleReports(String path, PrintableReport... reports) {
        if (reports == null || reports.length == 0)
            return;
        Document document = new Document(new com.itextpdf.text.Rectangle(
                reports[0].getPreferredSize().width,
                reports[0].getPreferredSize().height));
        try {

            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(path));
            document.open();

            PdfContentByte contentByte = writer.getDirectContent();

            for (PrintableReport report : reports) {
                document.newPage();
                contentByte.addTemplate(report.getPdfTemplate(contentByte), 0, 0);
            }

        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            if (document.isOpen()) {
                document.close();
            }
        }
    }

    public void printToPdf(String path) {
        saveMultipleReports(path, this);
    }
   /* private class GraphicField extends JPanel {

        public GraphicField(ArrayList<XYChart> graphicList) {

            this.setLayout(new GridLayout(8,1,15,5));


            XYChart SBP = graphicList.get(0);
            XYChart DBP = graphicList.get(1);
            XYChart Frequence = graphicList.get(2);
            XYChart Temperature = graphicList.get(3);

            this.setBackground(Color.WHITE);

            JPanel pnlChartSBP = new XChartPanel<>(SBP);
            JPanel pnlChartDBP = new XChartPanel<>(DBP);
            JPanel pnlChartFrequence = new XChartPanel<>(Frequence);
            JPanel pnlChartTemperature = new XChartPanel<>(Temperature);

            JLabel titlechartsbp = new JLabel("GRAFICO DELLA PRESSIONE SISTOLICA");
            JLabel titlechartdbp = new JLabel("GRAFICO DELLA PRESSIONE DIASTOLICA");
            JLabel titlechartfreq = new JLabel("GRAFICO DELLE FREQUENZE CARDIACHE");
            JLabel titlecharttemp = new JLabel("GRAFICO DELLA TEMPERATURA");

            this.add(titlechartsbp,0);
            this.add(pnlChartSBP,1);
            this.add(titlechartdbp,2);
            this.add(pnlChartDBP,3);
            this.add(titlechartfreq,4);
            this.add(pnlChartFrequence,5);
            this.add(titlecharttemp,6);
            this.add(pnlChartTemperature,7);


        }
    }*/
}
