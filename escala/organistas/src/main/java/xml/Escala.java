package xml;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("Escala")
public class Escala {

    @XStreamAsAttribute
    private String nome;

    @XStreamImplicit(itemFieldName = "Mes")
    private List<Mes> mes = new ArrayList<>();

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void addMes(Mes mes){
        this.mes.add(mes);
    }
}
