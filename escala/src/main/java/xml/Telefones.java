package xml;



import java.util.LinkedHashMap;
import java.util.Map;

public class Telefones {

    private Map<Integer, LinkedHashMap<String, String>> telefones = new LinkedHashMap<>();

    public void addTelefone(int seq, String nome, String numero){
        if(telefones.get(seq) == null) {
            telefones.put(seq, new LinkedHashMap<String, String>());
        }
        this.telefones.get(seq).put(nome, numero);
    }

}
