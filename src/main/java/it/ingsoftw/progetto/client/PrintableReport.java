package it.ingsoftw.progetto.client;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.io.FileOutputStream;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class PrintableReport extends JPanel {

    private boolean printable;

    private class InfoField extends JPanel {
        public InfoField(String title, String value) {
            this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

            JLabel titleLabel = new JLabel(title);
            JLabel valueLabel = new JLabel(value);

            Font baseFont = titleLabel.getFont();

            titleLabel.setFont(baseFont.deriveFont(baseFont.getStyle() | Font.BOLD));
            valueLabel.setFont(baseFont.deriveFont(baseFont.getStyle() & ~Font.BOLD));

            Component separator = Box.createHorizontalStrut(10);
            separator.setMaximumSize(new Dimension(separator.getSize().width, 0));

            this.add(titleLabel);
            this.add(separator);
            this.add(valueLabel);

            if (printable) {
                titleLabel.setBackground(Color.WHITE);
                titleLabel.setBackground(Color.WHITE);
                this.setBackground(Color.WHITE);
            }
        }

    }


    PrintableReport(boolean printable) {
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


        this.add(new InfoField("Nome", "Prova"));
        this.add(new InfoField("Cognome", "Prova1"));
    }

    void printToPdf(String path) {
        Document document = new Document(PageSize.A4);
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("/home/andrea/test.pdf"));
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

}
