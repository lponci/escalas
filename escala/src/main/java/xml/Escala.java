package xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@XStreamAlias("Escala")
public class Escala {

    @XStreamAsAttribute
    private String nome;

    @XStreamImplicit(itemFieldName = "Mes")
    private List<Mes> mes = new ArrayList<>();

    private Map<String, String> telefones = new LinkedHashMap<>();

    public Escala() {
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void addMes(Mes mes){
        this.mes.add(mes);
    }
}
