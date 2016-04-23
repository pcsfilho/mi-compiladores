package analisador_sintatico;

import java.util.Objects;

/**
 *
 * @author Paulo
 */
public class Item{
    
    private String nome;
    private String tipo;
    private String valor;
    private String escopo;
    
    public Item(String nome,String tipo, String valor){
        this.nome=nome;
        this.tipo=tipo;
        this.valor=valor;
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
    
    @Override
    public boolean equals(Object o){
        System.out.println("Entrou");
        if ((o instanceof Item) && ((Item) o).get_nome().equals(this.get_nome())) {
            return true; 
        }else
            return false;
    }



    @Override
    public int hashCode() {
        System.out.println("Entrou hash");
        return 97 * Objects.hashCode(this.nome);
    }
}
