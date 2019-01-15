package xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.ArrayList;
import java.util.List;

@XStreamAlias("Mes")
public class Mes {

    private String nomeMes;

    @XStreamImplicit
    private List<Dias> dias = new ArrayList<>();

    public void setNomeMes(String nomeMes) {
        this.nomeMes = nomeMes;
    }

    public void addDias(Dias dias){
        this.dias.add(dias);
    }


}
