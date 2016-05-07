/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analisador_semantico;
import analisador_sintatico.Item;
import java.io.Serializable;
import java.util.ArrayList;


/**
 *
 * @author Paulo
 */
public class Classe implements Serializable{
    private String nomeEscopo;
    private ArrayList<Item> variavel_tab;//armazena o conjunto de variaveis locais
    private ArrayList<Item> funcoes_tab;//armazena o conjunto de variaveis locais
    private String heranca;// se esta classe herda de outra classe armazena o nome da outra classe.
    private String linha;
    
    public Classe(String nomeEscopo,String heranca,String linha){
        this.nomeEscopo=nomeEscopo;
        this.heranca=heranca;
        this.linha=linha;
        variavel_tab= new ArrayList<Item>();
        funcoes_tab=new ArrayList<Item>();
    }
    
    public ArrayList<Item> get_funcoes(){
        return this.funcoes_tab;
    }
    
    public String get_nome(){
        return this.nomeEscopo;
    }
    public String get_heranca(){
        return this.heranca;
    }
    public String get_linha(){
        return this.linha;
    }
    
    public void add_metodo(Item item, String linhaMetodo){
        if(!(funcoes_tab.contains(item))){
                funcoes_tab.add(item);
                System.out.println("Adicionou metodo na linha "+ linhaMetodo);
        }else{
            System.out.println("Erro semantico, linha "+ linhaMetodo +",ja existe um método com este nome");
        }
    }
    
    
    
    @Override
    public boolean equals(Object o){
        System.out.println("Entrou");
        if ((o instanceof Classe) && ((Classe) o).get_nome().equals(this.get_nome())) {
            return true; 
        }else
            return false;
    }



    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }
    
    @Override
    public String toString(){
        return get_nome();
    }

}
