import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import com.itextpdf.text.DocumentException;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.extended.NamedMapConverter;
import xml.Dias;
import xml.Escala;
import xml.Mes;
import xml.Telefones;

public class EscalaRJM {

    private static final String RESOURCES_DIR = "rjm//src//main//resources//";
    private static final String OUTPUT_DIR = "rjm//src//main//resources//output//";

    public static void main(String[] args){

        String[] names = {"Nayra = 4188-7416", "Rute = 4146-3506", "Ana = 95051-1129", "Jessica = 96289-4626"};
        int qntMes = 8;

        String xml = generateXml(getEscala(names, qntMes));
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

    private static Escala getEscala(String[] names, int qntMes) {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        Calendar cal = new GregorianCalendar(year, month, 1);

        Pessoas pessoas = new Pessoas(Arrays.asList(names));
        Telefones telefones = getTelefones(names);
        Escala escala = new Escala(telefones);
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

    private static Telefones getTelefones(String[] names) {
        Telefones telefones = new Telefones();
        int seq = 0;
        for (String name : names){
            String nome = getField(name, "=", 0);
            String numero = getField(name, "=", 1);
            telefones.addTelefone(seq, nome, numero);
            seq++;
        }
        return telefones;
    }

    private static String generateXml(Escala escala) {
        XStream xstream = new XStream();
        xstream.processAnnotations(Escala.class);
        xstream.processAnnotations(Mes.class);
        xstream.processAnnotations(Dias.class);
        NamedMapConverter nMapConverter = new NamedMapConverter(xstream.getMapper(),"item","id",String.class,"nome",String.class, true,true, xstream.getConverterLookup());
        xstream.registerConverter(nMapConverter);

//        NamedMapConverter namedMapConverter = new NamedMapConverter(xstream.getMapper(),"item","id",String.class,"nomeAndTelefone",String.class,true,true, xstream.getConverterLookup());
//        xstream.registerConverter(namedMapConverter);

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

    public static String getField(String str, String regex, int field){
        List<String> list = Arrays.asList(str.split(regex));
        return list.get(field);
    }

}
