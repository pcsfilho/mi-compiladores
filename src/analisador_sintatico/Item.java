package analisador_sintatico;
import java.util.Objects;
import java.io.Serializable;
import java.util.ArrayList;
/**
 * @author Paulo
*/
public class Item implements Serializable{
    
    private String nome;
    private String tipo;
    private String valor;
    private String escopo;//Guarda escopo ao qual pertence o item.
    private ArrayList<Item> parametros;//armazena lista de parametros se o item for uma função
    
    //Constantes e variaveis globais
    public Item(String nome,String tipo, String valor, String escopo){
        this.nome=nome;
        this.tipo=tipo;
        this.valor=valor;
        this.escopo=escopo;
    }
    
    //Variaveis e metodos
    public Item(String nome,String tipo,String escopo){
        this.nome=nome;
        this.tipo=tipo;
    }
    
    public Item(String nome,String tipo){
        this.nome=nome;
        this.tipo=tipo;
    }
    
    
    public String get_tipo(){
        return this.tipo;
    }
    
    public String get_valor(){
        return this.valor;
    }
    
    public String get_nome(){
        return this.nome;
    }
    
    public ArrayList<Item> getParametros(){
        return this.parametros;
    }
    
    //Cria uma lista de parametros
    public void criaListaParametro(){
        this.parametros=new ArrayList<Item>();
    }
    
    @Override
    public boolean equals(Object o){
        if ((o instanceof Item) && ((Item) o).get_nome().equals(this.get_nome())) {
            return true; 
        }else
            return false;
    }

    @Override
    public int hashCode() {
        return 97 * Objects.hashCode(this.nome);
    }
}
