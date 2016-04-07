package analisador_sintatico;
import analisador_lexico.Analex;
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
    private Token currentToken;
    private Hashtable<String,ArrayList> conjuntoSeguinte;
    private Hashtable<String,ArrayList> conjuntoPrimeiro;
    /**
     * Este método inicializa os atributos da classe Analex. Abre o arquivo de leitura e inicializa
     * o vetor de palavras reservadas para o analisador léxico.
     * @throws java.io.FileNotFoundException
    */
    public Sintatico() throws FileNotFoundException,IOException{
        scanner=openFiletoRead("../analisador_sintatico/src/outLex.txt");
        currentToken = new Token();
        conjuntoSeguinte=new Hashtable<String,ArrayList>();
        conjuntoPrimeiro=new Hashtable<String,ArrayList>();
        init_seguinte_primeiro();
    }
    
    /**
    Este método abre o arquivo de leitura do analisador léxico. Caso o arquivo nÃ£o possa
    * ser lido ele apresenta uma mensagem de erro de leitura.
    * @throws java.io.FileNotFoundException
    */
    private Scanner openFiletoRead(String path) throws FileNotFoundException{
        //Abre arquivo para leitura
        Scanner reader;
        if(new File(path).canRead()){
            fileR = new FileReader(path);
            reader = new Scanner(fileR);
            return reader; 
        }else{
            System.out.print("O arquivo de leitura não pode ser lido\n");
            return null;
        }
    }
    /**
    Este método abre o arquivo de leitura do analisador léxico. Caso o arquivo nÃ£o possa
    * ser lido ele apresenta uma mensagem de erro de leitura.
    */
    private void openFiletoWrite() throws IOException{
        //Abre arquivo para leitura
        //construtor que recebe o objeto do tipo arquivo
        fileW = new FileWriter(new File("../analisador_sintatico/src/sintOut.txt"));
                
        if(new File("../analisador_sintatico/src/sintOut.txt").canRead()){
            //construtor recebe como argumento o objeto do tipo FileWriter
            writeFile = new BufferedWriter(fileW);
        }else{
            System.out.print("O arquivo de Escrita não pode ser aberto\n");
        }
    }
    
    /**
     * retorna os seguintes de um n�o terminal
     */
    private void init_seguinte_primeiro() throws FileNotFoundException{
        Scanner reader=openFiletoRead("../analisador_sintatico/src/seguinte.txt");
        
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
                //if(arrayTemp.size()>0)
                    //System.out.println(split[0] +"=>"+arrayTemp.get(i-1));
            }
            conjuntoSeguinte.put(split[0], arrayTemp);
        }
        //Primeiro
        reader=openFiletoRead("../analisador_sintatico/src/primeiro.txt");
        
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
     * Retorna uma lista do conjunto seguinte do nao terminal passado como parametro
     * @param nTerminal
     * @return 
     */
    private ArrayList get_seguinte(String nTerminal){
        return conjuntoSeguinte.get(nTerminal);
    }
    
    /**
     * Retorna uma lista do conjunto primeiro do nao terminal passado como parametro
     * @param nTerminal
     * @return 
     */
    private ArrayList get_primeiro(String nTerminal){
        return conjuntoPrimeiro.get(nTerminal);
    }
    
    
    /**
    * Este m�todo retorna se h� um proximo token(true ou false) e cria o objeto 
    * para este novo token(Token)
    */
    private boolean next_token(){
        if(scanner.hasNext()){//testa se tem um novo token
            String token = scanner.nextLine();            
            currentToken = new Token(token);//cria um objeto Token para armazenar as informa��es(tipo, lexema, linha)
            return true;
        }
        return false;
    }    
    /**
     * Este m�todo retorna o token atual
     * @return Token
    */
    private Token get_token(){
        return currentToken;
    }
    
    //Fun��o para ser implementada no futuro e tratar vazios com o conjunto seguinte
    private boolean contains_seguinte(String nTerminal,String terminal){
        ArrayList arrayTemp=get_seguinte(nTerminal);
        return arrayTemp.contains(terminal);
    }
    
    //Fun��o para ser implementada no futuro e tratar vazios com o conjunto seguinte
    private boolean contains_primeiro(String nTerminal,String terminal){
        ArrayList arrayTemp=get_primeiro(nTerminal);
        return arrayTemp.contains(terminal);
    }
    /**
     * Declara��o de constantes
     * Este m�todo testa um bloco de constantes
     * <constante> ::= const '{' <DC> '}'
     */
    private boolean constantes(){
            if(get_token().get_lexema().equals("const")){//Testa se o token contem a palavra reservada const
                if(next_token()){//avan�a um token
                    if(get_token().get_lexema().equals("{")){//testa se { � o proximo caractere
                        if(next_token()){
                            if(declaracao_contante()){// vai para o n�o terminal <DC>. Caso aceite <DC> da match
                                return true;
                            }
                        }
                    }
                }
                return false;
            }            
        return false;
    }
    
    /**
     * Declara��o Constante
     * <DC> ::= <tipo> id '=' <valor> ';' <DC> | 
     */
    private boolean declaracao_contante(){
            if(tipo()){//testa o n�o terminal <tipo>
                if(next_token()){
                    if(get_token().get_tipo().equals("ID")){// testa se h� um identificador
                        if(next_token()){
                            if(get_token().get_lexema().equals("=")){//testa se tem um operador de atribui��o
                                if(next_token()){
                                    if(valor()){//testa n�o terminal <valor>
                                        if(next_token()){
                                            if(get_token().get_lexema().equals(";")){//testa delimitador ;
                                                if(next_token()){
                                                    if(get_token().get_lexema().equals("}")){// testa delimitador de fim de bloco }
                                                        return true;
                                                    }else{// ou se h� mais constantes a serem declaradas
                                                        if(declaracao_contante()){
                                                            return true;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                return false;
            }
        return false;
    }
    
    
    
    
    
    /**
     * <declara_vetor> ::= <tipo> id '[' <expN> ']' <col> '?'
     * <col> ::= '[' <expN> ']' <col> |
     * Declara��o de vetor 
     */
    
    private boolean declaracaoVetor(){
        if(tipo()){
            next_token();
            //quando o id 
            if(get_token().get_tipo().equals("ID")){
                next_token();
                    // verifica��o se tem [
                    if(get_token().get_lexema().equals("[")){
                    /////////////////////////
                  //  falta terminar o declara vetor
                    next_token();
                    if(this.expN()){
                       // verificar se precisa pular mesmo essa linha 
                        next_token();
                        // se deu tudo certo para a leitura do ] 
                        if(get_token().get_lexema().equals("]")){
                             next_token();
                            // caso a declara��o de vetor esteja correta
                             if(this.col()){
                                 return true;
                             }
                             // se a declara��o de vetor n�o t� correta
                             else{
                                   System.out.printf(" A declara��o de vetor t� errada -- em rela�� a coluna  ");
                                    return false;
                             }
                             
                         }
                        // caso n�o tenha ]
                        else{
                        System.out.printf(" N�o tem o ] ");
                        return false;
                        }
                            
                    }
                    // quando a express�o for errada
                    else{
                        System.out.printf(" A express�o t� errada ");
                        return false;
                    }
                    
                    }
                    // quando n�o tiver o [
                    else{
                        System.out.printf(" N�o tem o [ ");
                        return false;
                        
                    }
            }
            // caso n�o tenha ID
            else{
                System.out.printf("N�o tem id");
                return false;
            }
        }
        return false;
    } 
    
    
        /**
     * //terminar
     * Declara��o de vari�vel
     * // colocar o m�todo de chamar declara��o de vetor 
     * <variavel> ::= <tipo> id <variavelinha> | <declara_vetor>
     */
        // voltar e ver essa l�gica aqjui com essa classe token 
    private boolean variavel( ){
  
        // verifica a condi��o de ter um n�o terminal <tipo>
        if(tipo() ){
            next_token(); 
            // verificando se ter um id 
            if(get_token().get_tipo().equals("ID")){
                next_token();
                // chamada de m�todo para variavel linha
                // resolver isso daqui 
               
                if(this.variavelLinha()== true){
                 System.out.printf("passou por aqui ");
                 return true;
                }
                else{
                    return false;
                }
              
            }
            else{
                System.out.printf("N�o cont�m a estutura necess�ria , t� faltando o id" );
                return false;
            }
        }
        else{
                System.out.printf("N�o cont�m a estutura necess�ria , t� faltando o tipo" );
                return false;
        }
      //  return true;
    }
    
    
    
    /**
      //duvidas token vai armazena s� o tipo que ele tem ou o conte�do?
      Declara��o do n�o terminal <v�riavelinha>
      * <variavelinha> ::= ',' id <variavelinha> | ';'
      * */
      private boolean variavelLinha(){
            // quando a vari�vel acabou 
            if(get_token().get_lexema().equals(";")){
                 //System.out.printf("passou pelo primeiro variavel linha ");
                next_token();
                return true;
         }
           
            
            else{
                // passando na virgula 
                    if(get_token().get_lexema().equals (",")){
                        next_token();
                        System.out.printf("passou pela ,\n");
                                // passando no ID
                            if(get_token().get_tipo().equals("ID")){
                            next_token();
                            System.out.printf("passou pelo ID \n");
                                if(get_token().get_lexema().equals (";")){
                                    next_token();
                                    System.out.printf("passou pelo ;\n");
                                    return true;
                                    
                                }
                                // caso n�o acabe logo -- recurs�o
                                else{
                                        variavelLinha();
                                        if(get_token().get_lexema().equals (";")){
                                            next_token();
                                            //return true;
                                        }
                                        else{
                                            System.out.printf("deu merda na recurs�o");
                                            //next_token();
                                            return false;
                                            
                                        }
                                }
                            }
                            //caso n�o tenha um id
                            else{
                                System.out.printf("T� sem o id ");
                                next_token();
                                return false;
                                
                            }
                    }
                    //acho desnecess�rio
                    else{
                        System.out.printf("T� sem a ,  ");
                       // next_token();
                        return false;
                        
                    }
                
                
                
                return false;
            }
      }
    
    
    
    
     /**
     * Declara��o de express�o natural
     * <expN> ::= <termo><restoExp>
     * @return 
     */
    private boolean expN(){
     
        if(this.termo()){
            if(this.restoExp()){
                return true;
            }
        }
        
        return false;
        
    }
    
    /**
     * Produ��o de <termo>
     * <termo> ::= <fator><restoTermo>
     */
    public boolean termo(){
    return true;
    }
    /**
     * Produ��o de <restoExp> referente ao resto da express�o
     * <restoExp> ::= '+'<termo><restoExp> | ''<termo><restoExp> |
     */
    // tem que ver quando vai sair dessa exoress�o
    public boolean restoExp(){
        // verificar primeiro se � tem o termo correto
        if(get_token().get_lexema().equals("+") || get_token().get_lexema().equals("-")){
            if(this.termo()){
                this.restoExp();
                
            }
        }
    return false;
    }
    
    /**
     * Produ��o de <fator> 
     * <fator> ::= <valorN> | id<D>| '%' | <vetor> | <chamarMetodo> | '('<expN>')'
     */
    
    public boolean fator(){
        return true;
    }
    
     /**
         * M�todo para averificar a coluna 
         * <col> ::= '[' <expN> ']' <col> |
         */
    private boolean col (){
        // caso acabe com a declara��o de coluna
       // como fazer para parar essa recurs�o ? 
        
    // caso tenha o  [
        if(get_token().get_lexema().equals("[")){
            next_token();
            // caso o expx seja verdadeiro
            if(this.expN()){
                
                //caso tenha o ]
                if(get_token().get_lexema().equals("]")){
                next_token();
                return true;
                    // caso tenha acabe a express�o de coluna
                  // this.col();
                    
                    
                }
                // caso n�o tenha o ]
                else{
                    System.out.printf(" N�o tem o ] ");
                        return false;
                }
            }
            // caso a express�o expn esteja errada 
            else {
                System.out.printf(" Caso a express�o Expn esteja errada ");
                        return false;
            }
            
    } 
        // n�o tem o [
        else{
         System.out.printf(" N�o tem o [ ");
                        return false;
        }
        
    //lembrar que no final deve ter isso    
    //next_token();    
        //return true;
        // return false;
    }
    
    
    
    
    /**
     * Testa se algum tipo � aceito
     * <tipo> ::= int | float | bool | char 
     */
    private boolean tipo(){
        return ((get_token().get_lexema().equals("int")) || (get_token().get_lexema().equals("float")) ||
                (get_token().get_lexema().equals("bool")) || (get_token().get_lexema().equals("char")));
    }
    
    /**
     *<valorN> ::= numero | pontoflutuante
     */
    private boolean valor_numero(){
        return ((get_token().get_tipo().equals("NUM_I")) || (get_token().get_tipo().equals("NUM_F")));
    }
    
    /**
     *<valorB> ::= true | false
     */
    private boolean valor_bool(){
        return ((get_token().get_lexema().equals("true")) || (get_token().get_lexema().equals("false")));
    }
    /**
     *<valorB> ::= true | false
    */
    private boolean valor_literal(){
        return ((get_token().get_tipo().equals("CAD")) || (get_token().get_tipo().equals("CAR")));
    }
        
    /**
     * Testa se os valores s�o aceitos
    *<valor> ::= <valorN> | <valorB> | <valorL>
    *<valorL> ::= cadeiaconstante | caractere 
    * @return valor 
    */
    private boolean valor(){
        return ((valor_numero())|| (valor_bool()) || (valor_literal()));
    }
    /**
     * Testa se h� uma express�o v�lida
     * <exp> ::= <exp_valor><F> | '!' <exp>
     * @return 
     */
    private boolean expressao(){
        //<exp_valor><F> 
        System.out.println("Iniciando em expressao "+get_token().get_lexema());
        if(expressao_valor()){
            System.out.println("Passou expressao valor "+ get_token().get_lexema());
            if(next_token()){
                System.out.println("Indo para F"+get_token().get_lexema());
                if(F()){
                    System.out.println("Passou F"+ get_token().get_lexema());
                    return true;
                }
            //Caso n�o tenha nenhum token verifica se o proximo pode ser vazio
            }else if(contains_primeiro("<F>","vazio")){
                System.out.println("Vazio F");
                return true;
            }
            return false;
        }else
        // '!' <exp>
        if(get_token().get_lexema().equals("!")){
            System.out.println("Passou !"+ get_token().get_lexema());
            if(next_token()){
                if(expressao()){
                    System.out.println("Passou expressao em expressao"+ get_token().get_lexema());
                    return true;
                }
            }
            return false;
        }else{        
            return false;
        }
    }
    /**
     * <F> ::= <exp_sufixo> |
    */
    private boolean F(){
        System.out.println("Iniciando F: "+get_token().get_lexema());
        if(expressao_sufixo()){
            System.out.println("Passou Expressao Sufixo: "+get_token().get_lexema());
            return true;
        }else 
        //Verificar conjunto seguinte(F)
        if(contains_seguinte("<F>",get_token().get_lexema())){
            System.out.println("Passou Conjunto Seguinte de F: "+get_token().get_lexema());
            return true;    
        }else{
            return false;
        }        
    }
    /**
     * <exp_sufixo> ::= '<' <T> <exp> | '>' <T> <exp> | '==' <exp> | '!=' <exp> |  '&&' | <exp> | '||'
     * | '+' <exp> | '�' <exp> | '*' <exp> | '/' <exp> | '%' <exp>
     */
    private boolean expressao_sufixo(){
        //'<' <T> <exp> | '>' <T> <exp>
        System.out.println("Iniciando expressao sufixo: "+get_token().get_lexema());
        if((get_token().get_lexema().equals("<")) || (get_token().get_lexema().equals(">"))){
            System.out.println("Passou para "+ get_token().get_lexema());
            if(next_token()){
                //<T> pode ser '=' ou vazio. se for vazio o proximo de <T> � expressao.
                if(T()){//Expressao � o seguinte caso ocorra um vazio
                    System.out.println("Passou T "+ get_token().get_lexema());
                    return true;
                }else if(expressao()){
                    System.out.println("Passou expressao em expressao sufixo"+ get_token().get_lexema());
                    return true;
                }
            }
            return false;
        }else
        //'==' <exp> | '!=' <exp>
        if((get_token().get_lexema().equals("==")) || (get_token().get_lexema().equals("!="))){
            System.out.println("Passou para "+ get_token().get_lexema());
            if(next_token()){
                if(expressao()){
                    System.out.println("Passou Expressao em sufixo"+ get_token().get_lexema());
                    return true;
                }
            }
            return false;
        }else
        //'&&'| '||'
        if((get_token().get_lexema().equals("&&")) || (get_token().get_lexema().equals("||"))){
            System.out.println("Passou para "+ get_token().get_lexema());
            return true;
        }else
        //<exp>
        if(expressao()){
            System.out.println("Passou expressao sufixo2 "+ get_token().get_lexema());
            return true;
        }else
        //| '+' <exp> | '�' <exp> | '*' <exp> | '/' <exp> | '%' <exp>    
        if((get_token().get_lexema().equals("+")) || (get_token().get_lexema().equals("-")) ||
                (get_token().get_lexema().equals("*")) || (get_token().get_lexema().equals("/")) ||
                (get_token().get_lexema().equals("%"))){
            System.out.println("Passou para "+ get_token().get_lexema());
            if(next_token()){
                System.out.println("Indo para expressao ");
                if (expressao()) {
                    System.out.println("Passou Expressao em sufix3");
                    return true;
                }
            }
            return false;
        }else{
            return false;
        }
    }
    /**
     *<T> ::= '=' | 
    */
    private boolean T(){
        System.out.println("Iniciando T "+ get_token().get_lexema());
        if(get_token().get_lexema().equals("=")){
            System.out.println("Passou para "+ get_token().get_lexema());
            return true;
        }else 
        //Verificar conjunto seguinte(T)
        if(contains_seguinte("<T>",get_token().get_lexema())){
            System.out.println("Passou para "+ get_token().get_lexema());
            return true;
        }else{
            System.out.println("N�o passou <T>");
            return false;
        }
        
    }
    /**
     * Testa se h� uma express�o v�lida
     * <exp_valor> ::= <valorB> | <valorN> | id <G> | ( <exp> ) | <chamarMetodo> | <vetor>
     * @return 
    */
    private boolean expressao_valor(){
        System.out.println("Iniciando expressao valor: "+get_token().get_tipo());
        //<valorB> | <valorN>
        if(valor_bool() || valor_numero()){
            System.out.println("<valorB> | <valorN> Passou para "+get_token().get_lexema());
            return true;
        }else
        //id <G>
        if(get_token().get_tipo().equals("ID")){
            System.out.println("Passou para "+ get_token().get_lexema());
            if(next_token()){
                System.out.println("Indo para G"+ get_token().get_lexema());
                if(G()){
                    System.out.println("Passou de G ");
                    return true;
                }
            }
            return false;
        }else
        //( <exp> )
        if(get_token().get_lexema().equals("(")){
            System.out.println("Passou para "+ get_token().get_lexema());
            if(next_token()){
                System.out.println("Indo para "+ get_token().get_lexema());
                if(expressao()){
                    System.out.println("Passou Expressao em expressao valor "+ get_token().get_lexema());
                    if(next_token()){
                        if(get_token().get_lexema().equals(")")){
                            System.out.println("Passou para "+ get_token().get_lexema());
                            return true;
                        }
                    }
                }
            }
            return false;
        }else 
        //<chamarMetodo>
        if(chamar_metodo()){
            System.out.println("Passou chamar metodo ");
            return true;
        }else 
        //<vetor>
        if(vetor()){
            System.out.println("Passou vetor");
            return true;
        }else{
            return false;
        }
    }
    
    /**
     * <G>::= '.' id |
     */
    private boolean G(){ 
        System.out.println("Iniciando <G>");
        if(get_token().get_lexema().equals(".")){
            System.out.println("Passou para "+get_token().get_lexema());
            if(next_token()){
                if(get_token().get_tipo().equals("ID")){
                    System.out.println("Passou para "+get_token().get_lexema());
                    return true;
                }
            }
            return false;
        }else 
        //Fazer um else if para a Fun��o seguinte(G) para o vazio
        if(contains_seguinte("<G>",get_token().get_lexema())){
            System.out.println("Passou seguinte(G) ");
            return true;
        }else{
            return false;
        }
    }
    /**
     * 
     * @return 
     */
    private boolean vetor(){    
        return false;
    }
    /**
     * 
     * @return 
     */
    private boolean chamar_metodo(){
        
        return false;
    }
    /**
     * <heranca> ::= (id) |
     * @return 
     */
    private boolean heranca(){
            if(get_token().get_lexema().equals("(")){
                if(next_token())
                    if(get_token().get_tipo().equals("ID")){
                        if(get_token().get_lexema().equals(")")){
                            return true;
                        }
                    }
                return false;
            }else
            //Sen�o vazio
            if(contains_seguinte("<heranca>",get_token().get_lexema())){
                return true;
            }
            else{
                return false;
            }
    }
    
    public void analiser() throws IOException{     
        //abre arquivo para escrita
        openFiletoWrite();
        if(next_token()){
          if(constantes()){
              if(next_token()){
                System.out.println("Passou Constantes");
                if(expressao()){
                  System.out.println("Passou Expressao");
                }else{
                    System.out.println("N�o Passou Expressao");
                }
              }
          }else{
              System.out.println("N�o Passou Constantes");
          }
        }
    }
        /**
     * @param args the command line arguments
     */
    public static void main(String[] args){             
        try {
            Analex analex;
            analex= new Analex();
            analex.analiser();
            Sintatico sint;
            sint = new Sintatico();
            sint.analiser();

        } catch (FileNotFoundException e) {
            System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage());
        } catch (IOException e) {
            System.err.printf("Erro na leitura do arquivo: %s.\n", e.getMessage());
        }
        
    }    
}