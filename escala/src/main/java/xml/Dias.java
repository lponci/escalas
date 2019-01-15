package xml;



import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.ArrayList;
import java.util.List;

@XStreamAlias("Dias")
public class Dias {

    @XStreamAlias(value = "dia")
    private List<String> dia = new ArrayList<>();
    private List<String> escalada = new ArrayList<>();

    public void addDia(String dia){
        this.dia.add(dia);
    }

    public void addEscalada(String escalada){
        this.escalada.add(escalada);
    }

}
