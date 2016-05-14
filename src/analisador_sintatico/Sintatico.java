package analisador_sintatico;
import analisador_semantico.Semantico;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Hashtable;
/**
 * @author Paulo Filho, Rejanio Moraes
 * @version 1.0
*/
public class Sintatico{
    private FileReader fileR;//atributo de leitura de arquivo
    private Scanner scanner;//atributo de buffer de leitura de arquivo
    private FileWriter fileW;//atributo de escrita de arquivo
    private BufferedWriter writeFile;//atributo de buffer de escrita de arquivo
    private Token currentToken;//armazena o token atual a ser analisado
    private int position;//armazena a posição atual do token
    private ArrayList<Token> listaTokens;//lista de tokens do analisador lexico;
    private Hashtable<String,ArrayList> conjuntoSeguinte;//armazena o conjunto seguinte de cada não terminal
    private Hashtable<String,ArrayList> conjuntoPrimeiro;//armazena o conjunto primeiro de cada não terminal
    private Semantico sem;
    private boolean error;//armazena um erro sintatico caso ocorra. 
    private String[] reservedsWords;
    private Item currentItem;//item manipulado no momento
    private String escopo;//Guarda escopo atual
    /**
     * Este método inicializa os atributos da classe Sintatico. Abre o arquivo de leitura e inicializa
     * o array de tokens para a analise sintatica. Tambem inicialisa o conjunto seguinte e primeiro.
     * @throws java.io.FileNotFoundException
    */
    public Sintatico(String words[]) throws FileNotFoundException,IOException{
        init_lista_tokens();
        conjuntoSeguinte=new Hashtable<String,ArrayList>();
        conjuntoPrimeiro=new Hashtable<String,ArrayList>();
        sem = new Semantico();
        reservedsWords= words;
        init_seguinte_primeiro();
        error=false;
    }
    /**
    *Le todos os tokens do analex e armazena em uma lista de tokens
    *@throws FileNotFoundException
    *@throws IOException 
    */
    private void init_lista_tokens() throws FileNotFoundException,IOException{
        listaTokens=new ArrayList<Token>();
        position=0;
        scanner = openFiletoRead("../compilador/src/outLex.txt");
        String temp;
        while(scanner.hasNext()){
            String token = scanner.nextLine(); 
            listaTokens.add(new Token(token));//cria um objeto Token para armazenar as informações(tipo, lexema, linha)
        }
        if(listaTokens.size()>0){
            currentToken=listaTokens.get(0);
        }
    }
    /**
    Este metodo abre o arquivo de leitura do analisador lexico. Caso o arquivo nao possa
    *ser lido ele apresenta uma mensagem de erro de leitura.
    *@throws java.io.FileNotFoundException
    */
    private Scanner openFiletoRead(String path) throws FileNotFoundException{
        //Abre arquivo para leitura
        Scanner reader;
        if(new File(path).canRead()){
            fileR = new FileReader(path);
            reader = new Scanner(fileR);
            return reader; 
        }else{
            System.out.print("O arquivo de leitura nao pode ser lido\n");
            return null;
        }
    }
    /**
    Este metodo abre o arquivo para escrita do analisador sintatico. Caso o arquivo nao possa
    * ser lido ele apresenta uma mensagem de erro de leitura.
    */
    private void openFiletoWrite() throws IOException{
        //Abre arquivo para leitura
        //construtor que recebe o objeto do tipo arquivo
        fileW = new FileWriter(new File("../compilador/src/sintOut.txt"));
        if(new File("../compilador/src/sintOut.txt").canRead()){
            //construtor recebe como argumento o objeto do tipo FileWriter
            writeFile = new BufferedWriter(fileW);
        }else{
            System.out.print("O arquivo de Escrita nÃ£o pode ser aberto\n");
        }
    }
    /**
     * Iniciacila uma tabela HASH para os seguintes e uma para os primeiros de cada não terminal a partir da leitura de um arquivo, para
     * seguintes e primeiros.
     */
    private void init_seguinte_primeiro() throws FileNotFoundException{
        Scanner reader=openFiletoRead("../compilador/src/seguinte.txt");
        String temp;
        String[] split;
        ArrayList arrayTemp;
        while(reader.hasNext()){
            temp=reader.nextLine();
            split=temp.split("#");
            arrayTemp = new ArrayList();
            for (int i = 0; i < split.length; i++){
                if(i!=0){
                    arrayTemp.add(split[i]);
                }
            }
            conjuntoSeguinte.put(split[0], arrayTemp);
        }
        //Primeiro
        reader=openFiletoRead("../compilador/src/primeiro.txt");
        while(reader.hasNext()){
            temp=reader.nextLine();
            split=temp.split("#");
            arrayTemp = new ArrayList();
            for (int i = 0; i < split.length; i++){
                if(i!=0){
                    arrayTemp.add(split[i]);
                }
            }
            conjuntoPrimeiro.put(split[0], arrayTemp);
        }
    }
    /**
     * Retorna uma lista do conjunto primeiro de um determinado nao terminal passado como parametro
     * @param nTerminal
     * @return lista de primeiros do @param
     */
    private ArrayList get_primeiro(String nTerminal){
        return conjuntoPrimeiro.get(nTerminal);
    }
    /**
    *Verifica se é uma palavra reservada 
    */
    /**
     Este metodo recebe uma String como parametro e verifica se esta Ã© uma palavra reservada. 
     * Se sim retorna true
     * @param word
     * @return true se Ã© uma palavra reservada
     */
    private boolean isReservedWord(String word){           
        for (int i = 0; i <reservedsWords.length; i++) {
            if(word.equals(reservedsWords[i]))
                return true;
        }          
        return false;
    }
    /**
    * Este método retorna se há um proximo token(true ou false) na lista de tokens
    * para este novo token(Token)
    */
    private boolean has_next_token(){
        if(position+1<listaTokens.size()){//testa se tem um novo token
            return true;
        }
        return false;
    }    
    /**
     * Este método retorna o token atual
     * @return Token
    */
    private Token get_current_token(){
        return currentToken;
    }
    /**
     * Este método retorna o token de acordo com o index somado a posicao atual
     * @return Token
    */
    private Token get_token_from_index(int index){
        if(position+index<listaTokens.size()){
            return listaTokens.get(position+index);
        }else{
            return currentToken;
        }
    }
    /**
     * Este metodo retorna uma posiçao no array de tokens
     */
    private void back_token(){
        position--;
        if(position>=0)
            currentToken=listaTokens.get(position);
    } 
    /**
    * Este metodo avanca uma posiçao no array de tokens
    */
    private void ahead_token(){
        position++;
        if(position<listaTokens.size()){
            currentToken=listaTokens.get(position);
        }
    }
    
    private Token get_next_token(){
        if(position+1<listaTokens.size()){
            return listaTokens.get(position+1);
        }else{
            return currentToken;
        }
    } 
    /**Este método verifica se um terminal passado como parametro pertence ao conjunto seguinte de um não terminal
    */ 
    private boolean contains_seguinte(String nTerminal,String terminal){
        ArrayList arrayTemp=conjuntoSeguinte.get(nTerminal);
        if(arrayTemp!=null){
            return arrayTemp.contains(terminal);
        }
        return false;
    }  
    /**Este método verifica se um terminal passado como parametro pertence ao conjunto primeiro de um não terminal
    */
    private boolean contains_primeiro(String nTerminal,String terminal){
        ArrayList arrayTemp = get_primeiro(nTerminal);
        if(arrayTemp!=null){
            return arrayTemp.contains(terminal);
        }
        return false;
    }

    /******************************************Nao Terminais************************************************************
     * Cada nao terminal da gramatica possui um metodo para a analise sintatica. Sendo que a verificação dos terminais é 
     * feita a partir de testes de verdadeiro ou falso para um token atual. A cada verificacao verdadeira o token é atualizado 
     * de acordo com a lista de tokens armazenado em list_tokens. Usa-se o metodo has_next_token para verificar se existe um proximo token 
     * a ser verificado. Usa-se o metodo para ahead_token para avancar para o proximo token. 
     * Caso exista um vazio na gramatica este é testado verificando o seguinte do não terminal corrente. Caso o token corrente esteja na lista de
     * seguintes do não terminal então este esta correto. Caso ocorra algum erro sintatico em algum não terminal aninhado dentro de outro não
     * terminal, o atributo error é sinalizado como verdadeiro. Caso um não terminal seja testado, mas este nao pertenca a cadeia requerida error continua como 
     * falso. Isto é feito para garantir que o vazio será tratado corretamente.
     * O metodo analiser chama o não terminal inicial, programa, para a verificacao da cadeia de entrada.
    */
    
    /*
    Este metodo accept verifica se o token atual eh o token esperado. Caso seja o token esperado ele avança para o proximo token.
    Caso contrario ele reporta o erro sintatico.
    */
    private boolean  accept(String expected, String type, String nTerminal){
        //System.out.println("Espera "+expected+" "+get_current_token().get_lexema());
        if(get_current_token().get_lexema().equals(expected) || get_current_token().get_padrao().equals(type)){            
            if(has_next_token()){
                //System.out.println("Aceito "+get_current_token().get_lexema());
                return true;
            }else{
                if(contains_primeiro(nTerminal,"vazio")){
                    return true;
                }else{
                  System.out.println("Escopo incompleto 1");  
                  return false;
                }
            }            
        }else{
            System.out.println("Linha " + get_current_token().get_linha() +": Falta "+expected);
            panic(nTerminal);
            return false;
        }
    }
    /*
        Este metodo accept verifica se o tipo de token atual eh o token esperado. Caso seja o token esperado ele avança para o proximo token.
        Caso contrario ele reporta o erro sintatico.
    */
    private boolean  accept(String type, String nTerminal){
        //System.out.println("Espera "+get_current_token().get_padrao()+ " "+type);
        if(get_current_token().get_padrao().equals(type)){            
            if(has_next_token()){
                //System.out.println("Aceito "+get_current_token().get_lexema());
                return true;
            }else{
                if(contains_primeiro(nTerminal,"vazio")){
                    return true;
                }else{
                  System.out.println("Escopo incompleto");  
                  return false;
                }
            }                        
        }else{
            System.out.println("Linha " + get_current_token().get_linha() +": Falta "+type);
            panic(nTerminal);
            return false;
        }
    }
    
    /*
      Este metodo acceptVazio verifica se o nao terminal contem vazio. 
    */
    private boolean  acceptVazio(String nTerminal){
        if(contains_primeiro(nTerminal,"vazio")){
            back_token();
            System.out.println("Dentro do Vazio "+get_current_token().get_lexema());
            return true;
        }else{
            panic(nTerminal);
            return false;   
        }
    }
    /*
    Executa o metodo do panico.Percorre uma lista de tokens até encontrar o token de sincronizacao. utiliza os seguintes do
    nao terminal passado como parametro.
    */
    private void panic(String nTerminal){
        while(!(contains_seguinte(nTerminal,get_current_token().get_lexema()) || 
                    contains_seguinte(nTerminal,get_current_token().get_padrao()) ||
                    get_current_token().equals(nTerminal)||
                    get_current_token().equals(";") || get_current_token().equals("{")||
                    get_current_token().equals("}")) && has_next_token()){
                System.out.println(get_current_token().get_lexema());
                ahead_token();
        }
        if(has_next_token()&& get_current_token().get_padrao().equals("DEL")){
            ahead_token();
        }        
    }
    
    //<programa> ::= <constante> <programa> | <variavel> <programa> | <mainclass> <classe>
    private void programa() throws IOException{
        
        //System.out.println("iniciando programa "+get_current_token().get_lexema());
        //<constante> <programa> | <variavel> <programa>
        if(contains_primeiro("<constante>",get_current_token().get_lexema())){// avalia constantes ou <variavel>
            System.out.println("Passou constante "+get_current_token().get_lexema());
            constantes();
            //programa();
            //System.out.println("Passou programa");
            //Olhar tambem o proximo token
        }else if(contains_primeiro("<variavel>", get_current_token().get_lexema())){
            System.out.println("Passou para variavel "+get_current_token().get_lexema());
            escopo=null;
            variavel();
            programa();
        }else if(contains_primeiro("<mainclass>", get_current_token().get_lexema())){
            System.out.println("Passou mainclass "+get_current_token().get_lexema());
            mainClass();
            classe();
        }else{
            System.out.println("Erro programa");
        }
    }    
    /**
     * Declaração de constantes
     * Este método testa um bloco de constantes
     * <constante> ::= const '{' <DC> '}'
    */
    private void constantes() throws IOException{
            if(accept("const","RES","<constante>")){//Testa se o token contem a palavra reservada const
                ahead_token();
                if(accept("{","DEL","<constante>")){//avança um token
                    ahead_token();
                    if(declaracao_contante()){//armazena as constantes
                        if(accept("}","DEL","<constante>")){
                            System.out.println("Aceitou constante");
                            ahead_token();    
                        }
                    }
                }
            }
    }
    /**
     * Declaração Constante
     * <DC> ::= <tipo> id '=' <valor> ';' <DC> | 
    */
    private boolean declaracao_contante() throws IOException{
        if(contains_primeiro("<tipo>",get_current_token().get_lexema())){//escolhe a primeira produção
            if(tipo()){
                String tipo=get_current_token().get_lexema();//guarda o tipo de declaracao da constante
                String linha=get_current_token().get_linha();//guarda a linha
                ahead_token();
                if(accept("ID","<DC>")){
                    String nome=get_current_token().get_lexema();//guarda o nome da constante
                    ahead_token();
                    if(accept("=","","<DC>")){
                        ahead_token();
                        if(valor()){
                            String valor;
                            if(tipo.equals("bool")){
                                valor=get_current_token().get_lexema();//guarda o tipo de valor da constante
                            }else{
                                valor=get_current_token().get_padrao();//guarda o tipo de valor da constante
                            }                            
                            ahead_token();
                            if(accept(";","","<DC>")){
                                ahead_token();
                                Item temp=new Item(nome,tipo,valor,"global");
                                sem.add_contantes_tab(temp,linha);
                                if(declaracao_contante()){
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
            return false;
        }else if(acceptVazio("<DC>")){
            System.out.println("DC vazio");
            return true;
        }else{
            return false;
        }
    }
    /**
     * Testa se algum tipo é aceito
     * <tipo> ::= int | float | bool | char 
     */
    private boolean tipo(){
        return ((get_current_token().get_lexema().equals("int")) || (get_current_token().get_lexema().equals("float")) ||
                (get_current_token().get_lexema().equals("bool")) || (get_current_token().get_lexema().equals("char")));
    }
    
    /**
     *<valorN> ::= numero | pontoflutuante
     */
    private boolean valor_numero(){
        return ((get_current_token().get_padrao().equals("NUM_I")) || (get_current_token().get_padrao().equals("NUM_F")));
    }
    
    /**
     *<valorB> ::= true | false
     */
    private boolean valor_bool(){
        return ((get_current_token().get_lexema().equals("true")) || (get_current_token().get_lexema().equals("false")));
    }
    
    /**
     *<valorL> ::= cadeiaconstante | caractere
    */
    private boolean valor_literal(){
        return ((get_current_token().get_padrao().equals("CAD")) || (get_current_token().get_padrao().equals("CAR")));
    }
        
    /**
     * Testa se os valores são aceitos
    *<valor> ::= <valorN> | <valorB> | <valorL>
    *<valorL> ::= cadeiaconstante | caractere 
    * @return valor 
    */
    private boolean valor(){
        return ((valor_numero())|| (valor_bool()) || (valor_literal()));
    }
    /*
    <mainclass> ::= class id <heranca> '{' <main> <cg1>'}'
    */
    private void mainClass(){
        if(accept("class","","<mainclass>")){
            ahead_token();
            System.out.println("Passou classMain "+get_current_token().get_lexema());
            if(accept("ID","<mainclass>")){
                String nome=get_current_token().get_lexema();
                String linha=get_current_token().get_linha();
                escopo=nome;//guarda o nome da classe como o escopo atual
                ahead_token();
                System.out.println("Passou ID "+get_current_token().get_lexema());
                if(heranca(nome,linha)){
                    //System.out.println("Passou herança "+get_current_token().get_lexema());
                    ahead_token();
                    System.out.println("Passou heranca "+get_current_token().get_lexema());
                    if(accept("{","","<mainclass>")){
                        ahead_token();
                        System.out.println("Passou colchete "+get_current_token().get_lexema());
                        if(metodoMain()){
                            ahead_token();
                            System.out.println("Saiu main "+get_current_token().get_lexema()+" "+get_current_token().get_linha());
                            if(cg1()){
                                System.out.println("Passou CG1 "+get_current_token().get_lexema()+" "+get_current_token().get_linha());
                                ahead_token();                            
                                if(accept("}","DEL","<mainclass>")) {
                                    System.out.println("Passou mainclass");
                                    ahead_token();
                                    escopo=null;//reinicia o escopo
                                }
                            }
                        }
                    }
                }
            }
        }
        System.out.println("N Passou classMain ");
    }
    /*
    <classe> ::= class id <heranca> '{' <cg1> '}' <classe> |
    */
    private boolean classe(){
        System.out.println("Entrou classe "+get_current_token().get_lexema());
        if(get_current_token().get_lexema().equals("class")){
            ahead_token();
            System.out.println("Passou class "+get_current_token().get_lexema());
            if(accept("ID","<classe>")){
                String nome=get_current_token().get_lexema();
                String linha=get_current_token().get_linha();
                ahead_token();
                System.out.println("Passou ID "+get_current_token().get_lexema());
                if(heranca(nome,linha)){
                    ahead_token();
                    System.out.println("Passou heranca "+get_current_token().get_lexema());
                    if(accept("{","","<classe>")){
                        ahead_token();
                        System.out.println("Passou { "+get_current_token().get_lexema());
                        if(cg1()){
                            ahead_token();
                            System.out.println("Passou cg1 "+get_current_token().get_lexema());
                            if(accept("}","","<classe>")){
                                ahead_token();
                                System.out.println("Passou } "+get_current_token().get_lexema());
                                if(classe()){
                                    System.out.println("Passou classe "+get_current_token().get_lexema());
                                    escopo=null;//reinicia o escopo
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
            System.out.println("Não Passou classe "+get_current_token().get_lexema());
            return false;
        }else if(acceptVazio("<classe>")){
            System.out.println("Passou vazio classe "+get_current_token().get_lexema());
            return true;
        }else{
            System.out.println("Não Passou classe "+get_current_token().get_lexema());
            return false;
        }
    }
    /*
    <main> ::= void main '(' ')' '{' <cg2> '}'
    */
    private boolean metodoMain(){
        if(accept("void","","<main>")){
            ahead_token();
            System.out.println("Passou void "+ get_current_token().get_lexema());
            if(accept("main","","<main>")){
                String nome = get_current_token().get_lexema();
                currentItem=new Item("main","void",escopo);//cria um item metodo que contem o nome,tipo e escopo deste metodo
                sem.add_metodo(escopo,currentItem,get_current_token().get_linha());
                escopo=escopo+"#"+nome;//guarda o nome da classe mais o nome do metodo, por exemplo: escopo=nomeClasse.main
                ahead_token();
                System.out.println("Passou main "+ get_current_token().get_lexema());
                if(accept("(","","<main>")){
                    ahead_token();
                    System.out.println("Passou ( "+ get_current_token().get_lexema());
                    if(accept(")","","<main>")){
                        ahead_token();
                        System.out.println("Passou ) "+ get_current_token().get_lexema());
                        if(accept("{","","<main>")){
                            ahead_token();
                            System.out.println("Passou { "+ get_current_token().get_lexema());
                            if(cg2()){
                                ahead_token();
                                System.out.println("Passou CG2 "+ get_current_token().get_lexema());
                                if(get_current_token().get_lexema().equals("}")){
                                    System.out.println("Passou metodoMain "+ get_current_token().get_lexema());
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        System.out.println("N Passou metodoMain "+ get_current_token().get_lexema());
        return false;
    }
    /**
     * <heranca> ::= '(' id ')' |
     * @return 
    */
    private boolean heranca(String nomeClasse, String linha){
        System.out.println("Entrou herança "+get_current_token().get_lexema()+" "+get_current_token().get_linha());
        if(get_current_token().get_lexema().equals("(")){
            ahead_token();
            System.out.println("Passou ( "+get_current_token().get_padrao());
            if(accept("ID","<heranca>")){
                sem.add_classe_tab(nomeClasse,linha,
                        get_current_token().get_lexema());//passa o nome da classe, a linha e a herança
                ahead_token();
                System.out.println("Passou ID "+get_current_token().get_lexema());
                if(accept(")","","<heranca>")){
                    //uma heranca deve ser aceita se uma classe ja existir
                    System.out.println("Passou ) "+get_current_token().get_lexema());
                    return true;
                }
            }
            return false;
        }else if(acceptVazio("<heranca>")){
            System.out.println("Passou herança "+get_current_token().get_lexema()+" "+get_current_token().get_linha());
            sem.add_classe_tab(nomeClasse,linha,"");//passa o nome da classe, a linha e a herança
            return true;
        }else{
            System.out.println("Não Passou herança");
            return false;
        }        
        
    }
    /*
    <metodo> ::= <tipo> id '(' <parametro> ')''{' <cg2> <return> '}' | void id '(' <parametro> ')''{'
    <cg2>'}'
    */
    private boolean metodo(){
        System.out.println("Entrou Metodo "+get_current_token().get_lexema());
        if(tipo()){
            String tipo = get_current_token().get_lexema();
            
            ahead_token();
            System.out.println("Passou tipo "+get_current_token().get_padrao());
            if(accept("ID","<metodo>")){
                String nome = get_current_token().get_lexema();
                currentItem=new Item(nome,tipo,escopo);//cria um item metodo que contem o nome,tipo e escopo deste metodo
                sem.add_metodo(escopo,currentItem,get_current_token().get_linha());
                escopo=escopo+"#"+nome;//atualiza escopo: classe.nomeMetodo
                ahead_token();
                System.out.println("Passou ID "+get_current_token().get_lexema());
                if(accept("(","DEL","<metodo>")){
                    ahead_token();
                    System.out.println("Passou ( "+get_current_token().get_lexema());
                    if(parametro()){
                        ahead_token();
                        System.out.println("Passou parametro "+get_current_token().get_lexema());
                        if(accept(")","DEL","<metodo>")){
                            ahead_token();
                            System.out.println("Passou ) "+get_current_token().get_lexema());
                            if(accept("{","DEL","<metodo>")){
                                ahead_token();
                                System.out.println("Passou { "+get_current_token().get_lexema());
                                if(cg2()){
                                    ahead_token();
                                    System.out.println("Passou cg2 "+get_current_token().get_lexema());
                                    if(retorno()){
                                        ahead_token();
                                        System.out.println("Passou retorno "+get_current_token().get_lexema());
                                        if(accept("}","DEL","<metodo>")){
                                            System.out.println("Passou } "+get_current_token().get_lexema());
                                            return true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return false;
        }else if(accept("void","RES","<metodo>")){
            ahead_token();
            System.out.println("Passou void "+get_current_token().get_lexema());
            if(accept("ID","<metodo>")){
                ahead_token();
                System.out.println("Passou ID "+get_current_token().get_lexema());
                if(accept("(", "DEL","<metodo>")){
                    ahead_token();
                    System.out.println("Passou ( "+get_current_token().get_lexema());
                    if(parametro()){
                        ahead_token();
                        System.out.println("Passou parametro "+get_current_token().get_lexema());
                        if(accept(")", "DEL","<metodo>")){
                            ahead_token();
                            System.out.println("Passou ) "+get_current_token().get_lexema());
                            if(accept("{", "DEL","<metodo>")){
                                ahead_token();
                                System.out.println("Passou { "+get_current_token().get_lexema());
                                if(cg2()){
                                    ahead_token();
                                    System.out.println("Passou cg2 "+get_current_token().get_lexema());
                                    if(accept("}", "DEL","<metodo>")){
                                        System.out.println("Passou } "+get_current_token().get_lexema());
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            System.out.println("N Passou metodo "+get_current_token().get_lexema());
            return false;
        }else{
            System.out.println("N Passou metodo "+get_current_token().get_lexema());
            return false;
        }
    }
    /**
     * ProduÃ§Ã£o de variavel 
     * <variavel> ::= <tipo> id <variavelinha> | <declara_vetor>
     * @return 
     */
    
    private boolean variavel(){
        //<tipo> id <variavelinha>
        if(contains_primeiro("<tipo>",get_current_token().get_lexema())){
            System.out.println("Indo para tipo");
            if(tipo()){
                String tipo=get_current_token().get_lexema();//guarda o nome da constante
                ahead_token();
                System.out.println("Passou tipo "+get_current_token().get_padrao());
                if(accept("ID","<variavel>")){
                    String nome = get_current_token().get_lexema();
                    String linha = get_current_token().get_linha();
                    ahead_token();
                    System.out.println("Passou ID "+get_current_token().get_lexema());
                    if(variavelLinha(tipo,nome,linha)){
                        ahead_token();
                        System.out.println("Passou Variavel "+get_current_token().get_lexema());
                        return true;
                    }
                }
            }
            System.out.println("N passou variavel "+get_current_token().get_lexema());
            return false;
        }
        //<declara_vetor>
        else if(contains_primeiro("<declara_vetor>",get_current_token().get_lexema())){
            System.out.println("Passou para declaraVetor "+get_current_token().get_lexema());
            return true;
        }else{
            System.out.println("N passou variavel "+get_current_token().get_lexema());
            return false;
        }
        
    }
    
    /*
    <parametro> ::= <tipo> id <parametro2> |
    */
    private boolean parametro(){
        System.out.println("Entrou parametro "+get_current_token().get_lexema());
        if(tipo()){
            String tipo=get_current_token().get_lexema();
            ahead_token();
            System.out.println("Passou tipo "+get_current_token().get_padrao());
            if(accept("ID","<parametro>")){
                if(currentItem.getParametros()==null){//verifica se a lista de parametros foi criada
                   currentItem.criaListaParametro();
                }
                String nome=get_current_token().get_lexema();
                sem.add_parametro_metodo(currentItem, new Item(nome, tipo),get_current_token().get_linha());//passa parametro pra lista de parametros do metodo
                ahead_token();
                System.out.println("Passou ID "+get_current_token().get_lexema());
                if(parametro2()){
                    System.out.println("Passou parametro2 "+get_current_token().get_lexema());
                    return true;
                }
            }
            System.out.println("N Passou parametro "+get_current_token().get_lexema());
            return false;
        }else if(acceptVazio("<parametro>")){
            System.out.println("Passou vazio "+get_current_token().get_lexema());
            return true;
        }else{
            System.out.println("N Passou parametro "+get_current_token().get_lexema());
            return false;
        }
    }
    /*
    <parametro2> ::= ',' <tipo> id <parametro2> |
    */
    private boolean parametro2(){
        System.out.println("Entrou parametro2 "+get_current_token().get_lexema());
        if(get_current_token().get_lexema().equals(",")){
            ahead_token();
            System.out.println("Passou , "+get_current_token().get_lexema());
            if(tipo()){
                String tipo=get_current_token().get_lexema();
                ahead_token();
                System.out.println("Passou tipo "+get_current_token().get_padrao());
                if(accept("ID","<parametro>")){
                    String nome=get_current_token().get_lexema();
                    sem.add_parametro_metodo(currentItem, new Item(nome, tipo),get_current_token().get_linha());//passa parametro pra lista de parametros do metodo
                    ahead_token();
                    System.out.println("Passou ID "+get_current_token().get_lexema());
                    if(parametro2()){
                        System.out.println("Passou parametro2 "+get_current_token().get_lexema());
                        return true;
                    }
                }
            }
            System.out.println("N Passou parametro2 "+get_current_token().get_lexema());
            return false;
        }else if(acceptVazio("<parametro2>")){
            System.out.println("Passou parametro2 "+get_current_token().get_lexema());
            return true;
        }else{
            System.out.println("N Passou parametro2 "+get_current_token().get_lexema());
            return false;
        }
    }
        
    /*
    <cg1> ::= <variavel><cg1> | <metodo><cg1> | !<atribuicao><cg1> |
    Tem como parametro o nome da classe(escopo) ao qual faz parte
    */
    private boolean cg1(){
        System.out.println("Entrou cg1 " + get_current_token().get_lexema());
        if((tipo() && get_token_from_index(1).get_padrao().equals("ID") && 
                get_token_from_index(2).get_lexema().equals(",") || get_token_from_index(2).get_lexema().equals(";"))){//escolhe a prrimeira producao. <variavel><cg1>
            System.out.println("Escolheu variavel "+get_current_token().get_lexema());
            if (variavel()){
                ahead_token();
                System.out.println("Passou variavel "+get_current_token().get_lexema());
                if(cg1()){
                    System.out.println("Passou cg1 "+get_current_token().get_lexema());
                    return true;
                }
            }            
            return false;
        }else if((tipo()|| get_current_token().get_lexema().equals("void")) && (get_token_from_index(1).get_padrao().equals("ID") && 
                get_token_from_index(2).get_lexema().equals("("))){//escolhe a segunda producao. <metodo><cg1>
            System.out.println("Escolheu metodo "+get_current_token().get_lexema());
            if(metodo()){
                ahead_token();
                System.out.println("Passou metodo "+get_current_token().get_lexema());
                if(cg1()){
                    System.out.println("Passou cg1 "+get_current_token().get_lexema());
                    return true;
                }
            }
            System.out.println("N Passou cg1 "+get_current_token().get_lexema());
            return false;
        }else if(acceptVazio("<cg1>")){
            System.out.println("Passou Vazio cg1 " + get_current_token().get_lexema()+" "+get_current_token().get_linha());
            return true;
        }else{
            System.out.println("Não Passou cg1");
            return false;
        }
    }
    /*
    <cg2> ::= <atribuicao><cg2> | <variavel><cg2> | <comando><cg2> | <exp> '?' <cg2> |
    <chamarMetodo> '?' <cg2> | <instanciar> '?' <cg2> |
    */
    private boolean cg2(){
        System.out.println("Entrou cg2 "+get_current_token().get_lexema());
        //<variavel><cg2>
        if(contains_primeiro("<tipo>",get_current_token().get_lexema())){
            System.out.println("Entrou para variavel "+get_current_token().get_lexema());
            if(variavel()){
                ahead_token();
                System.out.println("Passou variavel "+get_current_token().get_lexema());
                if(cg2()){
                    return true;
                }
            }
            System.out.println("N passou cg2 "+get_current_token().get_lexema());
            return false;
        }else if(acceptVazio("<cg2>")){
            System.out.println("Passou Vazio CG2 "+get_current_token().get_lexema());
            return true;
        }else{
            System.out.println("N Passou cg2 "+get_current_token().get_lexema());
            return false;
        }
    }
    /*
    <return> ::= return <returnSufixo> ; |
    */
    private boolean retorno(){
        System.out.println("Entrou return "+get_current_token().get_lexema());
        if(get_current_token().get_lexema().equals("return")){
                ahead_token();
                System.out.println("Passou return "+get_current_token().get_lexema());
                if(returnSufixo()){
                    ahead_token();
                    System.out.println("Passou returnSufixo "+get_current_token().get_lexema());
                    if(accept(";","DEL","<return>")){
                        System.out.println("Passou ; "+get_current_token().get_lexema());
                        return true;
                    }
                } 
            System.out.println("Não passou return "+get_current_token().get_lexema());
            return false;
        }else if(acceptVazio("<return>")){
            System.out.println("Passou return "+get_current_token().get_lexema());
            return true;
        }else{
            System.out.println("Não passou return "+get_current_token().get_lexema());
            return false;
        }
    }
    /*
    <returnSufixo> ::= <exp> | <valorL>
    */
    private boolean returnSufixo(){
        System.out.println("Entrou returnSufixo "+get_current_token().get_padrao());
        if(contains_primeiro("<exp>", get_current_token().get_lexema()) || contains_primeiro("<exp>", get_current_token().get_padrao())){//caso contenha o primeiro de <exp>
            System.out.println("Escolheu exp "+get_current_token().get_lexema());
            if(expressao()){
                System.out.println("Passou exp "+get_current_token().get_lexema());
                return true;
            }            
            return false;
        }else if(contains_primeiro("<valorL>", get_current_token().get_padrao())){//caso contenha o primeiro de <ValorL>
            System.out.println("Escolheu valorL "+get_current_token().get_lexema());
            if(valor_literal()){
                System.out.println("Passou valorL "+get_current_token().get_lexema());
                sem.verifica_retorno(currentItem, get_current_token().get_padrao(),get_current_token().get_linha());//verifica se o tipo de retorno esta correto.
                return true;
            }            
            return false;
        }else{
            System.out.println("Não passou returnSufixo "+get_current_token().get_lexema());
            return false;
        }
    }    
    
    //<variavelinha> ::= ',' id <variavelinha> | '?'
    private boolean variavelLinha(String tipo, String nome, String linha){
        System.out.println("Entrou variaveLinha");
        if(escopo==null){
            sem.add_var_globals_tab(new Item(nome, tipo), linha);
        }else{
            System.out.println("Nome escopo variavel: "+escopo);
            sem.add_variavel_tab(new Item(nome, tipo, escopo), linha, escopo);
        }
        if(get_current_token().get_lexema().equals(",")){
            ahead_token();
            System.out.println("Passou virgula "+get_current_token().get_padrao());
            if(accept("ID","<variavelinha>")){
                nome=get_current_token().get_lexema();    
                linha=get_current_token().get_linha();    
                ahead_token();
                System.out.println("Passou ID "+get_current_token().get_lexema());
                if(variavelLinha(tipo, nome, linha)){
                    return true;
                }
            }
            return false;
        }else if(get_current_token().get_lexema().equals(";")){
            return true;
        }else{        
            return false;
        }
    }
    /*
    <argumentos> ::= <exp> <argumentos2> | <valorL> <argumentos2> |
    */
    private boolean argumentos(){
        if(contains_primeiro("<exp>",get_current_token().get_lexema()) || contains_primeiro("<exp>",get_current_token().get_padrao())){
            if(expressao()){
                ahead_token();
                if(argumentos2()){
                    ahead_token();
                    return true;
                }
            }
            return false;
        }else if(valor_literal()){
            if(argumentos2()){
                ahead_token();
                return true;
            }
            return false;
        }else if(acceptVazio("<argumentos>")){
            return true;
        }else{
            return false;
        }
    }
    /*
    <argumentos2> ::= ',' <argumentos> |
    */
    private boolean argumentos2(){
        if(get_current_token().get_lexema().equals(",")){
            ahead_token();
            if(argumentos()){
                return true;
            }
            return false;
        }else if(acceptVazio("<argumentos2>")){
            return true;
        }else{
            return false;
        }
    }
    /*
    <argumentosVar> ::= id <argumentosVar2>
    */
    private boolean argumentosVar(){
        if(accept("ID","<argumentosVar>")){
            ahead_token();
            if(argumentosVar2()){
                return true;
            }
            return false;
        }else{
            return false;
        }
    }
    /*
    <argumentosVar2> ::= ',' <argumentosVar> |
    */
    private boolean argumentosVar2(){
        if(get_current_token().get_lexema().equals(",")){
            ahead_token();
            if(argumentosVar()){
                ahead_token();
                return true;
            }
            return false;
        }else if(acceptVazio("<argumentosVar2>")){
            return true;
        }else{
            return false;
        }
    }
    /*
    <instanciar> ::= new id '(' <argumentos> ')'
    */
    private boolean instanciar(){
        if(accept("new","RES","<instanciar>")){
           ahead_token();
           if(accept("ID","<instanciar>")){
               ahead_token();
               if(accept("(","DEL","<intanciar>")){
                   ahead_token();
                   if(argumentos()){
                       ahead_token();
                       if(accept(")","DEL","<intanciar>")){
                           return true;
                       }
                   }
               }
           }
           return false;
        }else{
            return false;
        }
    }
    /*
    <chamarMetodo> ::= id '.' <chamarMetodo2> | <chamarMetodo2>
    */
    private boolean chamarMetodo(){
        if(contains_primeiro("<chamarMetodo>",get_current_token().get_lexema()) ||contains_primeiro("<chamarMetodo>",get_current_token().get_padrao())){
            ahead_token();
            if(get_current_token().get_lexema().equals(".")){
                ahead_token();
                if(chamarMetodo2()){
                   return true;     
                }
            }else if(get_current_token().get_lexema().equals("(")){
                ahead_token();
                if(argumentos()){
                    ahead_token();
                    if(get_current_token().get_lexema().equals(")")){
                        ahead_token();
                        if(chamarMetodoEncadeado()){
                            return true;
                        }
                    }
                }
            }
            return false;
        }
        return false;
    }
    /*
    <chamarMetodo2> ::= id '('<argumentos>')' <chamarMetodoEncadeado>
    */
    private boolean chamarMetodo2(){
        if(accept("ID","<chamarMetodo2>")){
            ahead_token();
            if(get_current_token().get_lexema().equals("(")){
                ahead_token();
                if(argumentos()){
                    ahead_token();
                    if(get_current_token().get_lexema().equals(")")){
                        ahead_token();
                        if(chamarMetodoEncadeado()){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    /*
    <chamarMetodoEncadeado> ::= '.' <chamarMetodo2> |
    */
    private boolean chamarMetodoEncadeado(){
        if(get_current_token().get_lexema().equals(".")){
            ahead_token();
            if(chamarMetodo2()){
                return true;
            }
            return false;
        }else if(acceptVazio("<chamarMetodoEncadeado>")){
            return true;
        }else{
            return false;
        }
    }
    /*
    */
    private boolean expressao() {
        return false;
    }

    /**
     * Este metodo chama o não terminal inicial <programa> da gramatica para a analise sintatica.
     * @throws IOException 
    */
    public void analiser() throws IOException{     
        //abre arquivo para escrita
        openFiletoWrite();
        programa();
        
        //Verifica se as classes declarada como herança existem
        sem.verifica_heranca();
    }

}