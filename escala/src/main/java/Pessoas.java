import java.util.List;

public class Pessoas {

    private int sequencia = 0;
    private  List<String> names;

    public int getSequencia() {
        return sequencia;
    }

    public void setSequencia(int sequencia) {
        this.sequencia = sequencia;
    }

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }

//    String[] names = {"Nayra", "Rute", "Ana", "Jessica"};
//    List<String> pessoas = new ArrayList<>(Arrays.asList(names));
}
