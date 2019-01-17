import com.itextpdf.text.DocumentException;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.extended.NamedMapConverter;
import org.apache.xmlgraphics.util.MimeConstants;
import xml.Dias;
import xml.Escala;
import xml.Mes;

import java.io.*;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class Main {

    private static final String RESOURCES_DIR = "src//main//resources//";

    public static void main(String[] args){
        Escala escala = getEscala();
        String xml = generateXml(escala);
        ByteArrayOutputStream btOs = generatePDF(xml);
        try {
            ApplyTemplate.apply(btOs);
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }
    }

    public static ByteArrayOutputStream generatePDF(String xml) {
        ByteArrayOutputStream btOs = new ByteArrayOutputStream();
        try {
            PdfGeneration pdfGeneration = new PdfGeneration();
            InputStream xslInput = new FileInputStream(RESOURCES_DIR+ "//template.xsl");
            InputStream xmlInput = new ByteArrayInputStream(xml.getBytes());
            btOs.writeTo(pdfGeneration.generate(xmlInput, xslInput, btOs, MimeConstants.MIME_PDF));
        } catch (IOException e ) {
            e.printStackTrace();
        }
        return btOs;
    }

    private static Escala getEscala() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;

        String[] names = {"Nayra", "Rute", "Ana", "Jessica"};
        Pessoas pessoas = new Pessoas(Arrays.asList(names));

        int qntMes = 6;

        Calendar cal = new GregorianCalendar(year, month, 1);
        Escala escala = new Escala();
        escala.setNome("Escala de Organistas - RJM");
        for (int i = month; i <= qntMes; i++) {
            Mes mes = new Mes();
            Locale ptBR = new Locale("pt","br");
            mes.setNomeMes(cal.getDisplayName(Calendar.MONTH, Calendar.SHORT, ptBR).toUpperCase() + " / " + (year % 2000));

            Dias dias = new Dias();
            setDays(month, cal, pessoas, dias);
            mes.addDias(dias);
            escala.addMes(mes);
            month++;
        }
        return escala;
    }

    private static String generateXml(Escala escala) {
        XStream xstream = new XStream();
        xstream.processAnnotations(Escala.class);
        xstream.processAnnotations(Mes.class);
        xstream.processAnnotations(Dias.class);
        NamedMapConverter namedMapConverter = new NamedMapConverter(xstream.getMapper(),"item","dia",String.class,"nome",String.class,true,true, xstream.getConverterLookup());
        xstream.registerConverter(namedMapConverter);

        return xstream.toXML(escala);
    }

    private static void setDays(int month, Calendar cal, Pessoas pessoas, Dias dias) {
        do {
            // get the day of the week for the current day
            int day = cal.get(Calendar.DAY_OF_WEEK);
            // check if it is a Saturday or Sunday
            if (day == Calendar.SUNDAY) {
                // print the day - but you could add them to a list or whatever
                String sunday = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
                dias.joinDiaEscalada(sunday, pessoas.getNames().get(pessoas.getSequencia()));
                if (pessoas.getSequencia() == pessoas.getNames().size() - 1) {
                    pessoas.setSequencia(0);
                } else {
                    pessoas.setSequencia(pessoas.getSequencia() + 1);
                }
            }
            // advance to the next day
            cal.add(Calendar.DAY_OF_YEAR, 1);
        } while (cal.get(Calendar.MONTH) == month);
    }

}
