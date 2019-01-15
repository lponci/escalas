import com.thoughtworks.xstream.XStream;
import xml.Dias;
import xml.Escala;
import xml.Mes;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main {

    public static void main(String[] args) {

        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;

        Pessoas pessoas = new Pessoas();
        String[] names = {"Nayra", "Rute", "Ana", "Jessica"};
        pessoas.setNames(Arrays.asList(names));

        Calendar cal = new GregorianCalendar(year, month, 1);
        int fourMonths = cal.get(Calendar.MONTH) + 4;
        Escala escala = new Escala();
        escala.setNome("Escala de Organistas - RJM");
        for (int i = month; i < fourMonths; i++) {
//            List<String> daysMonth = new ArrayList<>();
//            System.out.print(cal.getDisplayName(Calendar.MONTH, 1, Locale.US));
            Mes mes = new Mes();
            mes.setNomeMes(cal.getDisplayName(Calendar.MONTH, 1, Locale.US).toUpperCase() + " / " + (year % 2000));
            Dias dias = new Dias();
            getDays(month, cal, pessoas, dias);
//            System.out.println(daysMonth);
            mes.addDias(dias);
            escala.addMes(mes);
            month++;
        }

        XStream xstream = new XStream();
        xstream.processAnnotations(Escala.class);
        xstream.processAnnotations(Mes.class);
        xstream.processAnnotations(Dias.class);
        String xml = xstream.toXML(escala);
//        System.out.println(xml);
        try {
            stringToDom(xml);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        return xml;

    }

    private static Dias getDays(int month, Calendar cal, Pessoas pessoas, Dias dias) {
        do {
            // get the day of the week for the current day
            int day = cal.get(Calendar.DAY_OF_WEEK);
            // check if it is a Saturday or Sunday
            if (day == Calendar.SUNDAY) {
                // print the day - but you could add them to a list or whatever
                String sunday = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
//                daysMonth.add(sunday + " " + pessoas.getNames().get(pessoas.getSequencia()));
                dias.addDia(sunday);
                dias.addEscalada(pessoas.getNames().get(pessoas.getSequencia()));
                if (pessoas.getSequencia() == pessoas.getNames().size() - 1) {
                    pessoas.setSequencia(0);
                } else {
                    pessoas.setSequencia(pessoas.getSequencia() + 1);
                }
//                    System.out.print(cal.get(Calendar.DAY_OF_MONTH) + " ");
            }
            // advance to the next day
            cal.add(Calendar.DAY_OF_YEAR, 1);
        } while (cal.get(Calendar.MONTH) == month);
        return dias;
    }

    public static void stringToDom(String xmlSource) throws IOException {
        FileWriter fw = new FileWriter("src/main/resources/escala.xml");
        fw.write(xmlSource);
        fw.close();
    }

}
