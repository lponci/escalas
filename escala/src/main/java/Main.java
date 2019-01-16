import com.thoughtworks.xstream.XStream;
import org.apache.fop.apps.FOPException;
import org.apache.xmlgraphics.util.MimeConstants;
import xml.Dias;
import xml.Escala;
import xml.Mes;

import javax.xml.transform.TransformerException;
import java.io.*;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class Main {

    public static void main(String[] args) {

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

        XStream xstream = new XStream();
        xstream.processAnnotations(Escala.class);
        xstream.processAnnotations(Mes.class);
        xstream.processAnnotations(Dias.class);
        String xml = xstream.toXML(escala);
        try {
            FOPUtils fopUtils = new FOPUtils();
            OutputStream pdfContent = new FileOutputStream(PdfGeneration.OUTPUT_DIR + "//output.pdf");
            InputStream xsl = new FileInputStream(PdfGeneration.OUTPUT_DIR + "//output.pdf");
            fopUtils.transform(stringToInputStream(xml), xsl, pdfContent, MimeConstants.MIME_PDF);
        } catch (IOException | TransformerException | FOPException e ) {
            e.printStackTrace();
        }
    }

    private static void setDays(int month, Calendar cal, Pessoas pessoas, Dias dias) {
        do {
            // get the day of the week for the current day
            int day = cal.get(Calendar.DAY_OF_WEEK);
            // check if it is a Saturday or Sunday
            if (day == Calendar.SUNDAY) {
                // print the day - but you could add them to a list or whatever
                String sunday = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
                dias.addDia(sunday);
                dias.addEscalada(pessoas.getNames().get(pessoas.getSequencia()));
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

    public static void stringToDom(String xmlSource) throws IOException {
        FileWriter fw = new FileWriter("src/main/resources/escala.xml");
        fw.write(xmlSource);
        fw.close();
    }

    public static InputStream stringToInputStream(String xmlSource){
        return new ByteArrayInputStream(xmlSource.getBytes());
    }

}
