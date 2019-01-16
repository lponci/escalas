package xml;



import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.LinkedHashMap;
import java.util.Map;

@XStreamAlias("Dias")
public class Dias {

    private Map<String, String> diaToEscalada = new LinkedHashMap<>();

    public void joinDiaEscalada(String dia, String escalada){
        this.diaToEscalada.put(dia, escalada);
    }

}
