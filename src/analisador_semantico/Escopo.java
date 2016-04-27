/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analisador_semantico;
import java.io.Serializable;


/**
 *
 * @author Paulo
 */
public class Escopo implements Serializable{
    private String nomeEscopo;
    
    
    public Escopo(String nomeEscopo){
        this.nomeEscopo=nomeEscopo;
    }
    
    public String get_nome(){
        return this.nomeEscopo;
    }
    
    @Override
    public boolean equals(Object o){
        System.out.println("Entrou");
        if ((o instanceof Escopo) && ((Escopo) o).get_nome().equals(this.get_nome())) {
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
