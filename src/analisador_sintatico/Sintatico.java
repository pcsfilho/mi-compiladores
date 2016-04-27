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
        return arrayTemp.contains(terminal);
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
            System.out.println("Passou variavel "+get_current_token().get_lexema());
            //variavel();
            //programa();
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
            if(accept("const","","<constante>")){//Testa se o token contem a palavra reservada const
                ahead_token();
                if(accept("{","","<constante>")){//avança um token
                    ahead_token();
                    if(declaracao_contante()){//armazena as constantes
                        if(accept("}","","<constante>")){
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
                                Item temp=new Item(nome,tipo,valor);
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
            System.out.println("Passou class");
            ahead_token();
            if(accept("ID","<mainclass>")){
                System.out.println("Passou ID");
                sem.add_classe_tab(get_current_token().get_lexema(),
                        get_current_token().get_linha());
                ahead_token();
                if(heranca()){
                    //System.out.println("Passou herança "+get_current_token().get_lexema());
                    ahead_token();
                    if(accept("{","","<mainclass>")){
                        ahead_token();
                        if(metodoMain()){
                            ahead_token();
                            //System.out.println("Saiu main "+get_current_token().get_lexema()+" "+get_current_token().get_linha());
                            if(cg1()){
                                //System.out.println("Passou CG1 "+get_current_token().get_lexema()+" "+get_current_token().get_linha());
                                ahead_token();                                
                                if(accept("}","DEL","<mainclass>")) {
                                    System.out.println("Passou mainclass");
                                    ahead_token();
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    /*
    <classe> ::= class id <heranca> '{' <cg1> '}' <classe> |
    */
    private boolean classe(){
        System.out.println("Entrou classe "+get_current_token().get_lexema()+" "+get_current_token().get_linha());
        if(get_current_token().get_lexema().equals("class")){
            ahead_token();
            if(accept("ID","<classe>")){
                sem.add_classe_tab(get_current_token().get_lexema(),
                        get_current_token().get_linha());
                ahead_token();
                if(heranca()){
                    System.out.println("Passou herança "+get_current_token().get_lexema()+" "+get_current_token().get_linha());
                    ahead_token();
                    if(accept("{","","<classe>")){
                        ahead_token();
                        if(cg1()){
                            System.out.println("Passou CG1 "+get_current_token().get_lexema()+" "+get_current_token().get_linha());
                            ahead_token();
                            if(accept("}","","<classe>")){
                                ahead_token();
                                if(classe()){
                                    System.out.println("Passou classe");
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
            return false;
        }else if(acceptVazio("<classe>")){
            System.out.println("Passou classe");
            return true;
        }else{
            System.out.println("Não Passou classe");
            return false;
        }
    }
    /*
    <main> ::= void main '(' ')' '{' <cg2> '}'
    */
    private boolean metodoMain(){
        System.out.println("Entrando main "+get_current_token().get_lexema()+" "+get_current_token().get_linha());
        if(accept("void","","<main>")){
            ahead_token();
            if(accept("main","","<main>")){
                ahead_token();
                if(accept("(","","<main>")){
                    ahead_token();
                    if(accept(")","","<main>")){
                        ahead_token();
                        if(accept("{","","<main>")){
                            ahead_token();
                            if(cg2()){
                                ahead_token();
                                if(accept("}","DEL","<main>")){
                                    System.out.println("Passou main "+get_current_token().get_lexema()+" "+get_current_token().get_linha());                              
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        System.out.println("Não Passou main");
        return false;
    }
    /**
     * <heranca> ::= '(' id ')' |
     * @return 
    */
    private boolean heranca(){
        System.out.println("Entrou herança "+get_current_token().get_lexema()+" "+get_current_token().get_linha());
        if(get_current_token().get_lexema().equals("(")){
            ahead_token();
            if(accept("ID","<heranca>")){
                ahead_token();
                if(accept(")","","<heranca>")){
                    ahead_token();
                    //uma heranca deve ser aceita se uma classe ja existir
                    System.out.println("Passou herança");
                    return true;
                }
            }
            return false;
        }else if(acceptVazio("<heranca>")){
            System.out.println("Passou herança "+get_current_token().get_lexema()+" "+get_current_token().get_linha());
            return true;
        }else{
            System.out.println("Não Passou herança");
            return false;
        }        
        
    }
    /*
    <metodo> ::= <tipo> id '(' <parametro> ')''{' <cg2> <return> '}' | void id '(' <parametro> ')''{'
    <cg2> '}'
    */
    private boolean metodo(){
        if(tipo()){
            ahead_token();
            if(accept("ID","<metodo>")){
                ahead_token();
                if(accept("(","DEL","<metodo>")){
                    ahead_token();
                    if(parametro()){
                        ahead_token();
                        if(accept(")","DEL","<metodo>")){
                            ahead_token();
                            if(accept("{","DEL","<metodo>")){
                                ahead_token();
                                if(cg2()){
                                    ahead_token();
                                    if(retorno()){
                                        ahead_token();
                                        if(accept("}","DEL","<metodo>")){
                                            ahead_token();
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
            if(accept("ID","<metodo>")){
                ahead_token();
                if(accept("(", "DEL","<metodo>")){
                    ahead_token();
                    if(parametro()){
                        ahead_token();
                        if(accept(")", "DEL","<metodo>")){
                            ahead_token();
                            if(cg2()){
                                ahead_token();
                            }
                        }
                    }
                }
            }
            return false;
        }else{
            return false;
        }
    }
    /**
     * ProduÃ§Ã£o de variavel 
     * <variavel> ::= <tipo> id <variavelinha> | <declara_vetor>
     * @return 
     */
    
    private boolean variavel(){
        if(contains_primeiro("<tipo>",get_current_token().get_lexema())){
            if(tipo()){
                String tipo=get_current_token().get_lexema();//guarda o nome da constante
                ahead_token();
                if(accept("ID","<variavel>")){
                    String nome = get_current_token().get_lexema();
                    String linha = get_current_token().get_linha();
                    ahead_token();
                    if(variavelLinha(tipo,nome,linha)){
                        
                    }
                }
            }
        }
        else if(contains_primeiro("<declara_vetor>",get_current_token().get_lexema())){
            return true;
        }
        return false;
    }
    
    /*
    <parametro> ::= <tipo> id <parametro2> |
    */
    private boolean parametro() {
        if(tipo()){
            ahead_token();
            if(accept("ID","<parametro>")){
                ahead_token();
                if(parametro2()){
                    ahead_token();
                    return true;
                }
            }
            return false;
        }else if(acceptVazio("<parametro>")){
            return true;
        }else{
            return false;
        }
    }
    
    /*
    <parametro2> ::= ',' <tipo> id <parametro2> |
    */
    private boolean parametro2(){
        if(accept(",","DEL","<parametro2>")){
            ahead_token();
            if(tipo()){
                ahead_token();
                if(accept("ID","<parametro>")){
                    ahead_token();
                    if(parametro2()){
                        ahead_token();
                        return true;
                    }
                }
            }
            return false;
        }else if(acceptVazio("<parametro2>")){
            return true;
        }else{
            return false;
        }
    }
        
    /*
    <cg1> ::= <variavel><cg1> | <metodo><cg1> | !<atribuicao><cg1> |
    */
    private boolean cg1(){
        System.out.println("Entrou cg1 " + get_current_token().get_lexema());
        if(tipo()){
            String tipo=get_current_token().get_lexema();//guarda o nome da constante
            ahead_token();
            if(accept("ID","<cg1>")){
                String nome = get_current_token().get_lexema();
                String linha = get_current_token().get_linha();
                ahead_token();
                if(accept("(","DEL","cg1")){
                    if(metodo()){
                        ahead_token();
                        if(cg1()){
                            System.out.println("Passou cg1");
                            return true;
                        }
                    }
                }else if(variavelLinha(nome,tipo,linha)){
                    System.out.println("Não Passou cg1");
                    return false;
                }else{
                    System.out.println("Não Passou cg1");
                    return false;
                }
            }
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
    private boolean cg2() {
        if(false){
            return false;
        }else if(acceptVazio("<cg2>")){
            System.out.println("Passou Vazio CG2 "+get_current_token().get_lexema());
            return true;
        }else{
            return false;
        }
    }
    /*
    <return> ::= return <returnSufixo> '?' |
    */
    private boolean retorno(){
        if(accept("return","RES","<return>")){
            if(returnSufixo()){
                ahead_token();
                if(accept(";","DEL","<return>")){
                    ahead_token();
                    return true;
                }
            }
            return false;
        }else if(acceptVazio("<return>")){
            return true;
        }else{
            return false;
        }
    }
    /*
    <return> ::= return <returnSufixo> '?' |
    */
    private boolean returnSufixo(){
        if(accept("return","RES","<return>")){
            ahead_token();
            if(returnSufixo()){
                ahead_token();
                if(accept(";","DEL","<returnSufixo>")){
                    ahead_token();
                    return true;
                }
            }
            return false;
        }else if(acceptVazio("<returnSufixo>")){
            return true;
        }else{
            return false;
        }
    }    

    private boolean variavelLinha(String tipo, String nome, String linha) {
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
    }

}