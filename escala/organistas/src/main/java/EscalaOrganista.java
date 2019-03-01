import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import com.itextpdf.text.DocumentException;
import xml.Dias;
import xml.Escala;
import xml.Mes;


public class EscalaOrganista {

    private static final String RESOURCES_DIR = "organistas//src//main//resources//";
    private static final String OUTPUT_DIR = "organistas//src//main//resources//output//";
    private static final String SEGUNDA = "-2";
    private static final String QUINTA = "-5";
    private static final String DOMINGO = "-D";

    public static void main(String[] args){

        String nomeEscala = "Escala de Organistas";
        int qntMes = 4;
        List<String> namesSeq1 = Arrays.asList("Cecilia", "Daniela", "Any",  "Camila", "Ester", "Cibele", "Maria","Cristiane", "Fernanda", "Jessica");
        List<String> namesSeq2 = Arrays.asList("Daniela", "Any", "Cecilia",  "Ester", "Cibele", "Camila", "Maria","Cristiane", "Fernanda", "Jessica");
        List<String> namesSeq3 = Arrays.asList("Any", "Cecilia", "Daniela",  "Cibele", "Camila", "Ester", "Cristiane", "Fernanda", "Jessica");

        List<String> nomes = joinNames(namesSeq1, namesSeq2, namesSeq3);

        Class[] classes = {Escala.class, Mes.class, Dias.class};
        String xml = Utils.generateXml(getEscala(nomeEscala, nomes, qntMes), classes);
        String xsl = RESOURCES_DIR+ "template.xsl";
        ByteArrayOutputStream btOs = PdfGeneration.generatePDF(xml, xsl);

        String fundo = RESOURCES_DIR+ "fundo.pdf";
        String destPDF = OUTPUT_DIR + "newMergedDest.pdf";
        try {
            ApplyTemplate.apply(btOs, fundo, destPDF);
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }
    }

    private static List<String> joinNames(List<String> namesSeq1, List<String> namesSeq2, List<String> namesSeq3) {
        List<String> nomes = new ArrayList<>();
        for (int i = 0; i <= 3; i++ ){
            nomes.addAll(namesSeq1);
            nomes.addAll(namesSeq2);
            nomes.addAll(namesSeq3);
        }
        return nomes;
    }

    private static Escala getEscala(String nomeEscala, List<String> names, int qntMes) {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        Calendar cal = new GregorianCalendar(year, month, 1);

        Nomes nomes = new Nomes(names);
        Escala escala = new Escala();
        escala.setNome(nomeEscala);
        for (int i = 1; i <= qntMes; i++) {
            Mes mes = new Mes();
            Locale ptBR = new Locale("pt","br");
            mes.setNomeMes(cal.getDisplayName(Calendar.MONTH, Calendar.SHORT, ptBR).toUpperCase() + " / " + (year % 2000));

            Dias dias = new Dias();
            setDays(month, cal, nomes, dias);
            mes.addDias(dias);
            escala.addMes(mes);
            month++;
        }
        return escala;
    }

    private static void setDays(int month, Calendar cal, Nomes nomes, Dias dias) {
        do {
            // get the day of the week for the current day

            // check if it is a Saturday or Sunday
            isDay(cal, nomes, dias, Calendar.MONDAY, SEGUNDA);
            isDay(cal, nomes, dias, Calendar.THURSDAY, QUINTA);
            isDay(cal, nomes, dias, Calendar.SUNDAY, DOMINGO);
            // advance to the next day
            cal.add(Calendar.DAY_OF_YEAR, 1);
        } while (cal.get(Calendar.MONTH) == month);
    }

    private static void isDay(Calendar cal, Nomes nomes, Dias dias, int dayOfWeek, String refDayOfWeek) {
        int day = cal.get(Calendar.DAY_OF_WEEK);
        if (day == dayOfWeek) {
            // print the day - but you could add them to a list or whatever
            String dayOfMonth = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
            dias.joinDiaEscalada(dayOfMonth + refDayOfWeek, nomes.getNames().get(nomes.getSequencia()));
            if (nomes.getSequencia() == nomes.getNames().size() - 1) {
                nomes.setSequencia(0);
            } else {
                nomes.setSequencia(nomes.getSequencia() + 1);
            }
        }
    }

    public static String getField(String str, String regex, int field){
        List<String> list = Arrays.asList(str.split(regex));
        return list.get(field);
    }



}
