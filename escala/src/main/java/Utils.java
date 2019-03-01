import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.extended.NamedMapConverter;


public class Utils {

    public static String generateXml(Object escala, Class[] classes) {
        XStream xstream = new XStream();
        xstream.processAnnotations(classes);
        NamedMapConverter nMapConverter = new NamedMapConverter(xstream.getMapper(),
                "item",
                "id",String.class,
                "nome",String.class,
                true,
                true,
                xstream.getConverterLookup());
        xstream.registerConverter(nMapConverter);
        //        NamedMapConverter namedMapConverter = new NamedMapConverter(xstream.getMapper(),"item","id",String.class,"nomeAndTelefone",String.class,true,true, xstream.getConverterLookup());
        //        xstream.registerConverter(namedMapConverter);

        return xstream.toXML(escala);
    }

}
