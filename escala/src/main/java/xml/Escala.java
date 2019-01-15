package xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.ArrayList;
import java.util.List;

@XStreamAlias("Escala")
public class Escala {

    @XStreamAsAttribute
    private String nome;

    @XStreamImplicit
    @XStreamAlias("Mes")
    private List<Mes> mes = new ArrayList<>();

    public Escala() {
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void addMes(Mes mes){
        this.mes.add(mes);
    }
}
