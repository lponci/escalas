package xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.*;

@XStreamAlias("Escala")
public class Escala {

    @XStreamAsAttribute
    private String nome;

    @XStreamImplicit(itemFieldName = "Mes")
    private List<Mes> mes = new ArrayList<>();

    private Telefones telefones;

    public Escala(Telefones telefones) {
        this.telefones = telefones;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void addMes(Mes mes){
        this.mes.add(mes);
    }
}
