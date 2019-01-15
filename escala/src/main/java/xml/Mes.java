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

//    public List<Dias> getDias() {
//        return dias;
//    }
//
//    public void setDias(List<Dias> dias) {
//        this.dias = dias;
//    }
//
//    public String getNomeMes() {
//        return nomeMes;
//    }

    public void setNomeMes(String nomeMes) {
        this.nomeMes = nomeMes;
    }

    public void addDias(Dias dias){
        this.dias.add(dias);
    }


}
