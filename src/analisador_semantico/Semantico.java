package analisador_semantico;
import analisador_sintatico.Item;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;
public class Semantico {
    private ArrayList<Item> constantes_tab;//armazena o conjunto primeiro de cada n�o terminal
    private ArrayList<Item> var_globais_tab;//armazena o conjunto primeiro de cada n�o terminal
    private Hashtable<String,Classe> classe_tab;   
    private String funcao_current;//Armazena o nome da fun��o armazenada no momento   
    public Semantico(){
        constantes_tab=new ArrayList<Item>();
        var_globais_tab=new ArrayList<Item>();
        classe_tab=new Hashtable<>();
        funcao_current="";
    }
    /*
    Esta fun��o adiciona as constantes em uma lista ordenada. Caso o identificador seja unico e o valor seja compativel a constante � adicionada
    na lista. Caso contrario � relatado um erro sem�ntico.
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
                System.out.println("Erro semantico, linha "+ linha +", o tipo declarado n�o corresponde ao valor");
            }
        }else{
            System.out.println("Erro semantico, linha "+ linha +",ja existe constante com este nome");
        }
    }
    /*
    Esta fun��o adiciona as variaveis globais em uma lista ordenada. Caso o identificador seja unico a variavel � adicionada
    na lista. Caso contrario � relatado um erro sem�ntico.
    */
    public void add_var_globals_tab(Item item, String linha){
        if(!(var_globais_tab.contains(item))){
                var_globais_tab.add(item);
                System.out.println("Add variavel global "+item.get_nome()+" "+item.getEscopo());
        }else{
            System.out.println("Erro semantico, linha "+ linha +",ja existe variavel global com este nome");
        }
    }
    /*
    Esta fun��o adiciona as classes em uma tabela. Caso o identificador da classe seja unico a classe � adicionada
    na tabela. Caso contrario � relatado um erro sem�ntico. A chave de cada elemento � o nome da propria classe. 
    */
    public void add_classe_tab(String classe, String linha,String heranca){
        if(!(classe_tab.containsKey(classe))){//Se n�o contem nenhuma outra classe com este nome
            classe_tab.put(classe,new Classe(classe,heranca,linha));//Adiciona na tabela
        }else{
            System.out.println("Erro semantico, linha "+ linha +",ja existe classe com o nome "+classe);
        }
    }
    /*
    Verifica ao final da analise sem�ntica se as classes declaradas como heranca existem na tabela de classes.
    */
    public void verifica_heranca(){
        Set<String> nomes = classe_tab.keySet();//Conjunto de chaves da tabela de classes.
        for (String nome : nomes){
            System.out.println("Nome Classe: "+classe_tab.get(nome).get_nome());
            for(int i=0;i<classe_tab.get(nome).get_funcoes().size();i++){
                System.out.println("Nome Fun��o: "+classe_tab.get(nome).get_funcoes().get(i).get_nome());
                if(classe_tab.get(nome).get_funcoes().get(i).getParametros()!=null){
                    for(int j=0;j<classe_tab.get(nome).get_funcoes().get(i).getParametros().size();j++){
                        System.out.println("Nome parametro: "+classe_tab.get(nome).get_funcoes().get(i).getParametros().get(j).get_nome());
                    }
                }                                    
            }
            
            if(!(classe_tab.get(nome).get_heranca().equals(""))){//Se existe heran�a
                String heranca=classe_tab.get(nome).get_heranca();
                String linha=classe_tab.get(nome).get_linha();
                boolean contem = classe_tab.containsKey(heranca);//verifica se a classe heran�a existe
                if(!contem){
                    System.out.println("Erro semantico, linha "+ linha +",N�o existe uma Classe "+ heranca+" para ser herdada");
                }
            }
        }
   }
   // rever essa adi��o para ser consciente com o valor padr�o
   public void add_variavel_tab(Item item , String linha, String escopo){
       String temp;
       String[] split;
       split=escopo.split("#");
       if(!(var_globais_tab.contains(item) && constantes_tab.contains(item))){
           System.out.println("Add variavel no escopo: "+item.getEscopo());
           classe_tab.get(split[0]).add_variavel(split[1],item,linha);
       }else{
           System.out.println("Erro semantico, linha "+ linha +",ja existe uma variavel global ou constante com este nome");
       }
       
   }
   
    /*
    Esta fun��o adiciona os metodos de uma classe em uma lista ordenada. Caso o identificador da classe seja unico o metodo eh adicionado
    na lista. Caso contrario � relatado um erro sem�ntico.
    */
   public void add_metodo(String escopo, Item item,String linha){
       classe_tab.get(escopo).add_metodo(item, linha);
   }    
   /*
   Esta fun��o adiciona os parametros de um metodo em uma lista ordenada. Caso o identificador de parametro seja unico o parametro eh adicionado
   na lista. Caso contrario � relatado um erro sem�ntico.
   */
   public void add_parametro_metodo(Item metodo, Item item,String linha){
       if(!(metodo.getParametros().contains(item))){
          metodo.getParametros().add(item);
          System.out.println("Adicionou parametro "+item.get_nome());
       }else{
            System.out.println("Erro semantico, linha "+ linha +",ja existe parametro com este nome");
       }
   } 
   
   public void verifica_retorno(Item metodo, String tipo, String linha){
       System.out.println(metodo.get_tipo()+" "+tipo);
        if(!((metodo.get_tipo().equals("char") && tipo.equals("CAR"))||(metodo.get_tipo().equals("string") && tipo.equals("CAD"))||
                (metodo.get_tipo().equals("int") && tipo.equals("NUM_I"))||
                (metodo.get_tipo().equals("float") && tipo.equals("NUM_F"))||
                (metodo.get_tipo().equals("bool") && (tipo.equals("true")||tipo.equals("false"))))){
            System.out.println("Erro semantico, linha "+ linha +",o tipo de retorno n�o corresponde ao tipo da fun��o");
        }
   }
}