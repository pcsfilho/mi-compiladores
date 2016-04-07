/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analisador_sintatico;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;
/**
 *
 * @author Paulo
 */
public class FirstAndFollow {
    private FileReader fileR;//atributo de leitura de arquivo
    private Scanner scanner;//atributo de buffer de leitura de arquivo
    private FileWriter fileW;//atributo de escrita de arquivo
    private BufferedWriter writeFile;//atributo de buffer de escrita de arquivo
    private final String[] terminais;
    private ArrayList<String> nTerminais;
    private Hashtable<String,ArrayList> producoes;
    private Hashtable<String,ArrayList> conjuntoPrimeiro;
    private Hashtable<String,ArrayList> conjuntoSeguinte;
    
    public FirstAndFollow() throws FileNotFoundException{
        nTerminais= new ArrayList();
        producoes= new Hashtable();
        conjuntoPrimeiro= new Hashtable();
        conjuntoSeguinte= new Hashtable();
        openFiletoRead();
        terminais= new String[43];        
        terminais[0]= "class";terminais[1]= "const"; terminais[2]= "else";
        terminais[3]= "if";terminais[4]= "new";terminais[5]= "read";
        terminais[6]= "write";terminais[7]= "return";terminais[8]= "void";
        terminais[9]= "while";terminais[10]= "int";terminais[11]= "float";
        terminais[12]= "bool";terminais[13]= "string";terminais[14]= "char";
        terminais[15]= "true";terminais[16]= "false";terminais[17]= "main";
        terminais[18]= "id";terminais[19]= "{";terminais[20]= "}";
        terminais[21]= "|";terminais[22]= "||";terminais[23]= "[";
        terminais[24]= "]";terminais[25]= "-";terminais[26]= "--";
        terminais[27]= "++";terminais[28]= ";";terminais[29]= "&";
        terminais[30]= "&&";terminais[31]= "/";terminais[32]= "%";
        terminais[33]= "<";terminais[34]= ".";terminais[35]= ",";
        terminais[36]= ">=";terminais[37]= "<="; terminais[39]= "*";
        terminais[40]= "+"; terminais[41]= "="; terminais[42]= "==";
    }
    
    /**
    Este método abre o arquivo de leitura do analisador léxico. Caso o arquivo nÃ£o possa
    * ser lido ele apresenta uma mensagem de erro de leitura.
    * @throws java.io.FileNotFoundException
    */
    private void openFiletoRead() throws FileNotFoundException{
        //Abre arquivo para leitura
        if(new File("../analisador_sintatico/src/producoes.txt").canRead()){
            fileR = new FileReader("../analisador_sintatico/src/producoes.txt");
            scanner = new Scanner(fileR);
        }else{
            System.out.print("O arquivo de leitura não pode ser lido\n");
        }
    }
    
    public void nTerminais(){
        String temp[];
        while(scanner.hasNext()){//testa se tem um novo token
            temp = scanner.nextLine().split("#");
            ArrayList arrayTemp=new ArrayList();
            nTerminais.add(temp[0]);
            
            for (int i = 0; i < temp.length; i++) {                
                if(i!=0){
                 arrayTemp.add(temp[i]);
                }
                if(arrayTemp.size()>0)
                    System.out.println(temp[0]+" => "+temp[i]);
                
            }
            
            producoes.put(temp[0], arrayTemp);
        }
        
    }
    /**
     * Fun��o Primeiro
     * @param nTerminal 
     */
    void first(String nTerminal){
      ArrayList a=producoes.get(nTerminal);
      
      if(a.contains("vazio")){
          //System.out.println(nTerminal+" Vazio");
      }
        String temp;
        String[] split;
        for (int i = 0; i < a.size(); i++) {
            temp =(String)a.get(i);
            
            if(!temp.startsWith("<")){
                split=temp.split(" ");
          //      if(!split[0].equals("vazio"))
        //        System.out.println(nTerminal+" => "+split[0]);
            }else if(temp.startsWith("<")){
                
            }
            
        }
      
    }
    
    void follow(String nTerminal){
        
    }
    
    
    public static void main(String[] args){             
        try {
            FirstAndFollow t= new FirstAndFollow();
            t.nTerminais();
        } catch (FileNotFoundException e) {
            System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage());
        }
    }    
}
