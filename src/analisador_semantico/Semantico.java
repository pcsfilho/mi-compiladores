package analisador_semantico;

import analisador_sintatico.Item;
import java.util.ArrayList;
import java.util.Hashtable;

public class Semantico {
    private ArrayList<Item> constantes_tab;//armazena o conjunto primeiro de cada não terminal
    private ArrayList<Item> var_globais_tab;//armazena o conjunto primeiro de cada não terminal
    private Hashtable<String,Escopo> classe_tab;
    private ArrayList<Item> heranca_tab;//armazena o conjunto primeiro de cada não terminal
    private ArrayList<Item> variavel_tab;//armazena o conjunto primeiro de cada não terminal

    public Semantico(){
        constantes_tab=new ArrayList<Item>();
        var_globais_tab=new ArrayList<Item>();
        classe_tab=new Hashtable<>();
        heranca_tab= new ArrayList<Item>();
        variavel_tab= new ArrayList<Item>();
    }
    
    /*
    
    */
    public void add_contantes_tab(Item item, String linha){
        if(!(constantes_tab.contains(item))){
            if((item.get_tipo().equals("int")&&item.get_valor().equals("NUM_I")) ||
                   (item.get_tipo().equals("float")&&item.get_valor().equals("NUM_F"))||
                    (item.get_tipo().equals("char")&&item.get_valor().equals("CAR"))||
                    (item.get_tipo().equals("bool")&&item.get_valor().equals("true"))||
                    (item.get_tipo().equals("bool")&&item.get_valor().equals("false"))){
                constantes_tab.add(item);
            }else{
                System.out.println("Erro semantico, linha "+ linha +", o tipo declarado não corresponde ao valor");
            }
        }else{
            System.out.println("Erro semantico, linha "+ linha +",ja existe constante com este nome");
        }
    }
    /*
    */
    public void add_var_globals_tab(Item item, String linha){
        if(!(constantes_tab.contains(item))){
                constantes_tab.add(item);
        }else{
            System.out.println("Erro semantico, linha "+ linha +",ja existe constante com este nome");
        }
    }
    
    /*
    */
    public void add_classe_tab(String classe, String linha){
        if(!(classe_tab.containsKey(classe))){//Se não contem nenhuma outra classe com este nome
            System.out.println("Adicionou classe");
            classe_tab.put(classe,new Escopo(classe));//Adiciona na tabela
        }else{
            System.out.println("Erro semantico, linha "+ linha +",ja existe classe com o nome "+classe);
        }
    }
    
       public void add_heranca_tab(Item item , String linha){
    if(!(heranca_tab.contains(item))){
                heranca_tab.add(item);
        }else{
            System.out.println("Erro semantico, linha "+ linha +",ja existe uma heranca com este nome");
        }
   }
   // rever essa adição para ser consciente com o valor padrão
   public void add_variavel_tab(Item item , String linha){
    if(!(variavel_tab.contains(item))){
                variavel_tab.add(item);
        }else{
            System.out.println("Erro semantico, linha "+ linha +",ja existe uma variavel com este nome");
        }
   }
    
}
