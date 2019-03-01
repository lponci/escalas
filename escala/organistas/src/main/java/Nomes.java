import java.util.List;

public class Nomes {

    private int sequencia = 0;
    private  List<String> names;

    public Nomes(List<String> names) {
        this.names = names;
    }

    public int getSequencia() {
        return sequencia;
    }

    public void setSequencia(int sequencia) {
        this.sequencia = sequencia;
    }

    public List<String> getNames() {
        return names;
    }

}
