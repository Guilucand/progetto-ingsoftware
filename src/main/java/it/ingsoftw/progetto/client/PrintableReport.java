package it.ingsoftw.progetto.client;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import it.ingsoftw.progetto.common.IRoom;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;

import java.awt.*;
import java.io.FileOutputStream;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class PrintableReport extends JPanel {

    private boolean printable;
    private IRoom room;
    private ArrayList<XYChart> graphicList;


    PrintableReport(boolean printable, IRoom room, ArrayList<XYChart> graphicList ) {

        this.graphicList = graphicList;
        this.printable = printable;
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        Dimension A4 = new Dimension(595, 842);


        this.setPreferredSize(A4);
        this.setMinimumSize(A4);
        this.setMaximumSize(A4);
        this.setSize(A4);

        if (printable)
            this.setBackground(Color.WHITE);

        this.setBorder(new EmptyBorder(40, 30, 30, 30));


        this.add(new InfoField("Nome    :", "Prova"));
        this.add(new InfoField("Cognome     :", "Prova1"));
        this.add(new GraphicField(graphicList));

    }

    private class InfoField extends JPanel {

        public InfoField(String title, String value) {

            this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

            JLabel titleLabel = new JLabel(title);
            JLabel valueLabel = new JLabel(value);

            Font baseFont = titleLabel.getFont();

            titleLabel.setFont(baseFont.deriveFont(baseFont.getStyle() | Font.BOLD));
            valueLabel.setFont(baseFont.deriveFont(baseFont.getStyle() & ~Font.BOLD));


            this.add(titleLabel);

            this.add(valueLabel);

            if (printable) {
                titleLabel.setBackground(Color.WHITE);
                titleLabel.setBackground(Color.WHITE);
                this.setBackground(Color.WHITE);
            }
        }

    }

    public void printToPdf(String path) {

        Document document = new Document(PageSize.A4);
        try {

            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("test.pdf"));
            document.open();

            JFrame test = new JFrame();

            test.setContentPane(this);


            test.pack();
            test.setVisible(true);

            PdfContentByte contentByte = writer.getDirectContent();

            PdfTemplate template = contentByte.createTemplate(this.getWidth(), this.getHeight());
            Graphics2D g2 = template.createGraphics(this.getWidth(), this.getHeight());
            this.print(g2);
            g2.dispose();
            contentByte.addTemplate(template, 0, 0);

        } catch (Exception e1) {
            e1.printStackTrace();
        }
        finally{
            if(document.isOpen()){
                document.close();
            }
        }
    }

    private class GraphicField extends JPanel {

        public GraphicField(ArrayList<XYChart> graphicList) {

            this.setLayout(new GridLayout(8,1,15,5));


            XYChart SBP = graphicList.get(0);
            XYChart DBP = graphicList.get(1);
            XYChart Frequence = graphicList.get(2);
            XYChart Temperature = graphicList.get(3);


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
    }
}
