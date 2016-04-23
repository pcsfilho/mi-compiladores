/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analisador_sintatico;

import analisador_lexico.Analex;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author Paulo
*/
public class Compilador {
    private Analex analex;
    private Sintatico sintatico;
    private String[] reservedsWords;
    public Compilador() throws IOException{
        init_reserveds_words();
        init_analex();
        init_sintatico();
    }
    
    private void init_analex() throws IOException{
        analex= new Analex(this.reservedsWords);
        analex.analiser();
    }
    
    private void init_sintatico() throws IOException{
        sintatico= new Sintatico(this.reservedsWords);
        sintatico.analiser();
    }
    
    private void init_reserveds_words(){
        reservedsWords= new String[18];        
        reservedsWords[0]= "class";reservedsWords[1]= "const"; reservedsWords[2]= "else";
        reservedsWords[3]= "if";reservedsWords[4]= "new";reservedsWords[5]= "read";
        reservedsWords[6]= "write";reservedsWords[7]= "return";reservedsWords[8]= "void";
        reservedsWords[9]= "while";reservedsWords[10]= "int";reservedsWords[11]= "float";
        reservedsWords[12]= "bool";reservedsWords[13]= "string";reservedsWords[14]= "char";
        reservedsWords[15]= "true";reservedsWords[16]= "false";reservedsWords[17]= "main";
    }
    
    /**
    * @param args the command line arguments
    */
    
    public static void main(String[] args){             
        try {
            Compilador compilador=new Compilador();
        } catch (FileNotFoundException e) {
            System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage());
        } catch (IOException e) {
            System.err.printf("Erro na leitura do arquivo: %s.\n", e.getMessage());
        }
        
    }
    
}
