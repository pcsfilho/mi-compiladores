package analisador_lexico;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author Paulo Filho, Rejanio Moraes
 * @version 1.0
*/
public class Analex{
    private FileReader fileR;//atributo de leitura de arquivo
    private BufferedReader readFile;//atributo de leitura de arquivo
    private FileWriter fileW;
    private BufferedWriter writeFile;
    private final String[] reservedsWords;//atributo de vetor de palavras reservadas
    
    /**
     Este mÃ©todo inicializa os atributos da classe Analex. Abre o arquivo de leitura e inicializa
     * o vetor de palavras reservadas para o analisador lÃ©xico.
     * @throws java.io.FileNotFoundException
     */
    public Analex() throws FileNotFoundException,IOException{
        openFiletoRead();
        openFiletoWrite();
        reservedsWords= new String[18];        
        reservedsWords[0]= "class";reservedsWords[1]= "const"; reservedsWords[2]= "else";
        reservedsWords[3]= "if";reservedsWords[4]= "new";reservedsWords[5]= "read";
        reservedsWords[6]= "write";reservedsWords[7]= "return";reservedsWords[8]= "void";
        reservedsWords[9]= "while";reservedsWords[10]= "int";reservedsWords[11]= "float";
        reservedsWords[12]= "bool";reservedsWords[13]= "string";reservedsWords[14]= "char";
        reservedsWords[15]= "true";reservedsWords[16]= "false";reservedsWords[17]= "main";
    }
    
    /**
    Este mÃ©todo abre o arquivo de leitura do analisador lÃ©xico. Caso o arquivo nÃ£o possa
    * ser lido ele apresenta uma mensagem de erro de leitura.
    */
    private void openFiletoRead() throws FileNotFoundException{
        //Abre arquivo para leitura
        if(new File("../analisador_lexico/src/analisador_lexico/programa.txt").canRead()){
            fileR = new FileReader("../analisador_lexico/src/analisador_lexico/programa.txt");
            readFile = new BufferedReader(fileR);
        }else{
            System.out.print("O arquivo de leitura nÃ£o pode ser lido\n");
        }
    }
    /**
    Este mÃ©todo abre o arquivo de leitura do analisador lÃ©xico. Caso o arquivo nÃ£o possa
    * ser lido ele apresenta uma mensagem de erro de leitura.
    */
    private void openFiletoWrite() throws IOException{
        //Abre arquivo para leitura
        //construtor que recebe o objeto do tipo arquivo
        fileW = new FileWriter(new File("../analisador_lexico/src/analisador_lexico/lexOut.txt"));
                
        if(new File("../analisador_lexico/src/analisador_lexico/lexOut.txt").canRead()){
            //construtor recebe como argumento o objeto do tipo FileWriter
            writeFile = new BufferedWriter(fileW);
        }else{
            System.out.print("O arquivo de Escrita nÃ£o pode ser aberto\n");
        }
    }
    /** Metodo que verifica se a entrada eh uma letra
    def ehLetra (self, caracter):
    String com as letras componentes da linguagem (a..z|A..Z
    *@return true if is a letter*/
    private boolean isLetter(char caractere){
        //Testa se o valor ASCII do caracter esta entre 64 e 90(A..Z) ou entre  97 e 122 (a..z)
        return ((((int)caractere)>64 && ((int)caractere)<91) || (((int)caractere)>96 && ((int)caractere)<123));
    }
       
    /**
    * Este mÃ©todo recebe um caracter e verifica se este Ã© um digito.
    * Isto Ã© feito utilizando o mÃ©todo indexOf da String digitos que contem os digitos
    * de 0 Ã  9 e que recebe um caractere como parÃ¢metro e retorna um inteiro maior que 
    * zero caso este caractere esteja na String.
    */
    private boolean isDigit(char caractere){
        String digitos = "0123456789";        
        return (digitos.indexOf(caractere)>=0);
    }    
    /**
    * Este mÃ©todo recebe um caracter e verifica se este Ã© um delimitador.
    * Isto Ã© feito utilizando o mÃ©todo indexOf da String delimitadores que
    * contem os delimitadores {}[];.() e que recebe um caractere como parÃ¢metro
    * e retorna um inteiro maior que zero caso este substring esteja na String.
    */
    private boolean isDelimiter(char caractere){
        String delimitadores = "{}[];(),";        
        return (delimitadores.indexOf(caractere)>=0);
    }
    
    /**
    * Este mÃ©todo recebe uma String e verifica se este Ã© um operador.
    * Isto Ã© feito comparando a String parÃ¢metro com os operadores presente na condicional.
    */
    private boolean isOperator(String caracteres){
        if(caracteres.equals("+") || caracteres.equals("++") || caracteres.equals(".")
                || caracteres.equals("-")||  caracteres.equals("--") || caracteres.equals("*")
                || caracteres.equals("/")|| caracteres.equals("=") || caracteres.equals("==")
                || caracteres.equals("!=") || caracteres.equals(">") || caracteres.equals("<")
                || caracteres.equals(">=") || caracteres.equals("<=") || caracteres.equals("&&")
                || caracteres.equals("||")){
            return true;
        }
        return false;
    }
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
    
    private void getNumeral(){
        
    }
    /**
    Este Ã© o mÃ©todo principal da classe Analex, pois Ã© este que recebe um arquivo fonte para anÃ¡lise lÃ©xica e 
    * e gera um arquivo de saÃ­da com os tokens e lexemas das cadeias de entrada. Este mÃ©todo possui dois laÃ§os
    * principais aninhados. Um laÃ§o lÃª o arquivo linha a linha atÃ© encontrar o fim de arquivo. Outro laÃ§o lÃª 
    * caractere a caractere para verificar os padrÃµes contidos nas condicionais. Verificado cada padrÃ£o este mÃ©todo 
    * escreve em um arquivo de saÃ­da os determinados tokens e lexemas.
    */
    private void analiser() throws IOException{     
            //Armazena uma linha do arquivo
            String linha;
            //salva o numero atual da linha
            int num_linha=0;
            // guarda a posiÃ§Ã£o do caracter atual na linha a ser analisado
            int iterador_caracteres;
            // guarda o caracter atual e proximo caracter a serem analisados
            char current_char,next_char;            
            //Este Loop faz a leitura uma a uma de cada linha do arquivo
            while((linha = readFile.readLine()) != null){
                current_char=next_char=' ';
                iterador_caracteres=0;
                num_linha++;
                //pega tamanho da linha para a iteraÃ§Ã£o de caracteres
                int row_size=linha.length();
                while(iterador_caracteres<row_size){
                    current_char=linha.charAt(iterador_caracteres);//ler o primeiro caracter                    
                    //LÃ³gica para ignorar comentarios
                    //*******************************************************************
                    //Verifa se existe um proximo caracter para ser lido na linha atual e o armazena. 
                    if((iterador_caracteres+1)<row_size){
                        next_char=linha.charAt(iterador_caracteres+1);
                    }                    
                    //Consumindo e pulando comentarios de linha: //
                    if(current_char=='/' && next_char=='/'){
                        /*Aqui a variavel iteradora se torna igual ao tamanho da linha, 
                        forÃ§ando a saida do laÃ§o de leitura de linha e pulando uma linha de comentario*/
                        iterador_caracteres=row_size;
                    /*Consmindo e pulando comentarios com mais de uma linha;\/**\/.
                        A logica utilizada nesta condicional Ã© a de verficar se o caracter atual e o proximo formam
                        a cadeia /*. Caso esta cadeia todos os caracteres posteriores sÃ£o consumidos atÃ© encontrar 
                        a sequencia *\/*/
                    }else if(current_char=='/' && next_char=='*'){
                        //Esta variavel armazena o valor da linha que iniciou o comentario caso ocorra um erro
                        int num_row_coment=num_linha;
                        //Este loop consome todos os caracteres dentro dos delimitadores de comentario de mais de uma linha
                        while(!(current_char=='*' && next_char=='/')){
                            if((iterador_caracteres+2)<row_size){                                
                                iterador_caracteres++;
                                current_char=linha.charAt(iterador_caracteres);//ler o caracter apÃ³s caracter /                    
                                next_char=linha.charAt(iterador_caracteres+1);//ler o proximo caracter                                   
                            //Caso nÃ£o existe um proximo caracter a ser lido    
                            }else{
                                linha=readFile.readLine();//uma nova linha Ã© lida
                                iterador_caracteres=0;//coloca o iterador no inicio da linha
                                //Enquanto hÃ¡ linha a ser lida
                                if(linha!=null){                                    
                                    row_size=linha.length();//pega o tamnho da linha para iteraÃ§Ã£o dos caracteres
                                    num_linha++;
                                    if((iterador_caracteres+1)<row_size){
                                        current_char=linha.charAt(iterador_caracteres);                    
                                        next_char=linha.charAt(iterador_caracteres+1);
                                    }
                                }else{                                   
                                    //Se nÃ£o hÃ¡ mais linhas a serem lidas e ainda nÃ£o encontrou a sequencia /*
                                    writeFile.write("Erro Lexico  Comentario nÃ£o foi fechado  Linha "+num_linha+"\n");
                                    System.out.printf("\n Erro Lexico na linha %d. Comentario nÃ£o foi fechado\n",num_row_coment); 
                                    row_size = iterador_caracteres=0;
                                    break;
                                }
                            }   
                        } 
                        /*Como foi usada a lÃ³gica de pegar o caractere corrente e o proximo este incrementador serve para ignorar 
                        o proximo caracter ao fim da analise de comentario, no caso o '/'*/
                        iterador_caracteres++;                      
                    }
                    //LÃ³gica para palavras reservadas e identificadores
                    //*******************************************************************
                    /*IF para verificar se Ã© uma palavra reservada ou um identificador
                    Inicialmente verifica se o primeiro caractere Ã© uma letra*/
                    else if(isLetter(current_char)){
                        /*Apos verificar que o primeiro caractere da palavra sendo uma letra percorre-se a sequÃªncia de
                        caracteres atÃ© encontrar um caractere que nÃ£o faz parte do padrÃ£o ou o caractere de fim de linha.
                        A variavel iterador comeÃ§a a partir do prÃ³ximo caracter da linha.
                        A variavel erro guarda um true se ocorrer algum erro de padrÃ£o na sequencia de caracteres para
                        identificador.
                        */ 
                        iterador_caracteres++;
                        boolean erro=false;//caso ocorra algum erro
                        String temp_caracters=""+current_char;//armazena o lexema temporario
                        //Este loop vai iterar caracter a caracter atÃ© o fim da linha
                        while(iterador_caracteres<row_size){
                            current_char=linha.charAt(iterador_caracteres);//ler o caracter a ser analisado agora
                            //verifica se hÃ¡ um proximo caracter a ser lido para a analise de operadores com 2 caracteres
                            if(iterador_caracteres+1<row_size){
                                next_char=linha.charAt(iterador_caracteres+1);//ler o caracter a ser analisado agora
                            }                            
                            //Aninhamento de IFs para o teste analise lexica para um identificador
                            //Caracteres vÃ¡lidos de um identificador ou palavra reservada
                            if(isLetter(current_char) || isDigit(current_char) || current_char=='_'){
                                temp_caracters= temp_caracters+current_char;
                            /*Caso no meio da analise de um identificador ou palavra reservada exista um caracte espaÃ§o 
                                um operador ou delimitador este tipo de analise Ã© encerrada e volta-se uma posiÃ§Ã£o para
                                garantir a anÃ¡lise do novo padrÃ£o.*/    
                            }else if(((int)current_char)==32 || isOperator(""+current_char) || isOperator(""+next_char+current_char) || isDelimiter(current_char)){
                                //Se for alguma das opÃ§Ãµes acima volta uma posiÃ§Ã£o para analisar o proximo padrÃ£o e sai deste loop.                               
                                iterador_caracteres--;
                                break;
                            }else if(current_char!='\n'){
                                //Se ocorrer algum outro simbolo que nÃ£o esteja nas verificaÃ§Ãµes acima Ã© considerado  um erro lexico
                                erro=true;//Faz erro verdadeiro
                                writeFile.write("Erro Lexico Identificador com caracter Invalido "+current_char+" Linha "+num_linha+"\n");
                                System.out.printf("\nErro Lexico - Identificador com caracter invalido: %c.Linha: %d\n",current_char, num_linha);
                                break;
                            }
                            iterador_caracteres++;
                        }
                        if(erro){
                            /*JÃ¡ que ocorreu algum erro este loop percorre e consome o resto da sequencia 
                            de caracteres apÃ³s o erro lexico. Se outra palavra for iniciada haverÃ¡ a verificaÃ§Ã£o de outro padrÃ£o, pois
                            este loop e o seu loop pai serÃ£o pulados*/
                            while(iterador_caracteres+1<row_size){
                                    iterador_caracteres++;
                                    current_char = linha.charAt(iterador_caracteres);//ler o caracter a ser analisado agora
                                    if(((int)current_char==32) || isDelimiter(current_char) || isOperator(""+current_char) || ((int)current_char==13) || ((int)current_char ==32)){
                                        //Se for alguma das opÃ§Ãµes acima volta uma posiÃ§Ã£o e sai deste loop para verificar outro padrÃ£o.
                                        iterador_caracteres--;
                                        break;
                                    }
                            }
                            erro=false;
                        }else{
                                //Se nÃ£o houver nenhum erro lexico imprime no arquivo
                                //Neste IF verifica-se se a palavra Ã© reservada
                                if(isReservedWord(temp_caracters)){
                                    //TOKEN_RES_palavra_NumeroDaLinha
                                    //escreve o conteÃºdo no arquivo
                                    writeFile.write("TOKEN#RES#"+temp_caracters+"#"+num_linha+"\n");
                                    System.out.printf("TOKEN#RES#"+temp_caracters+"#"+num_linha+"\n");
                                }else {
                                   //TOKEN_ID_palavra_NumeroDaLinha
                                    writeFile.write("TOKEN#ID#"+temp_caracters+"#"+num_linha+"\n");
                                    System.out.printf("TOKEN#ID#"+temp_caracters+"#"+num_linha+"\n");
                                }                                  
                            }
                    }    
                    //LÃ³gica para Operadores
                    //*******************************************************************
                    //Este verifica se o caracter Ã© um operador duplo e imprime no arquivo.Pega o atual e o proximo caractere
                    else if((int)next_char!=32 &&  isOperator(""+current_char+next_char)){
                        writeFile.write("TOKEN#OP#"+""+current_char+next_char+"#"+num_linha+"\n");                        
                        System.out.printf("TOKEN#OP#"+""+current_char+next_char+"#"+num_linha+"\n");
                        iterador_caracteres++;
                    }
                    //Este verifica se o caracter Ã© um operador unÃ¡rio e imprime no arquivo. Pega apenas o caracter atual.
                    else if(isOperator(""+current_char)){
                        writeFile.write("TOKEN#OP#"+""+current_char+"#"+num_linha+"\n");
                        System.out.printf("TOKEN#OP#"+""+current_char+"#"+num_linha+"\n");
                    }                    
                    //LÃ³gica para palavras reservadas e identificadores
                    //*******************************************************************
                    //Este verifica se o caracter Ã© um delimitador e imprime no arquivo.
                    else if(isDelimiter(current_char)){
                        writeFile.write("TOKEN#DEL#"+current_char+"#"+num_linha+"\n");
                        System.out.printf("TOKEN#DEL#"+current_char+"#"+num_linha+"\n");
                    }
                    
                    ///// verificando a parte de cadeia e caracter constantes
            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                      
                    
             // lÃ³gica para cadeia constante      
             // código com a lógica do da cadeia constante. 
             else if(current_char == '"'){
                //string que vai armazenar a cadeia constante e exibir para o usuÃ¡rio   
                String cadeia=""+current_char;
                System.out.printf("Char: %c\n", current_char);
                System.out.println("Cadeia "+cadeia);
                
                if((iterador_caracteres+1)<row_size){
                    current_char=linha.charAt(iterador_caracteres+1);
                }
                cadeia = cadeia+current_char;
                iterador_caracteres++;
                boolean erro=false;
                 // percorrendo toda a cadeia constante
                    while(current_char !='"'){
                        System.out.printf("\n entrou no while Char: %c\n", current_char);
                        
                        if((iterador_caracteres+2)<=row_size){
                            System.out.printf("Cahar if: %c\n", linha.charAt(iterador_caracteres+1));
                            current_char = linha.charAt(iterador_caracteres+1);//ler o primeiro caracter   
                        }else{
                            erro=true;
                            break;
                        }                        
                        cadeia = cadeia+current_char;
                        System.out.println("Cadeia "+cadeia);
                        System.out.printf("\n final do while\n");
                        iterador_caracteres++;
                    }
                    if(erro){
                        System.out.printf("\n Erro Lexico - NÃ£o fechou \" na linha: "+num_linha+"\n");
                    }else{
                        System.out.printf("\n TOKEN#Cadeia Constante#"+cadeia+ ":"+num_linha+"\n");
                    }
                    
                    //           
             
            } 
            /** 
             * Else if que trata de caracter constante
             * */
                
      else if(current_char==39){
                    String caracter=""+current_char;
                //// vendo se o iterador +1 é em branco e tratando isso com o else
                if((int)(iterador_caracteres+1)!=32){
                    
                // verificando se estão na faixa de parametro aceita pela estrutura lexica
                if(isDigit(linha.charAt(iterador_caracteres+1))||isLetter(linha.charAt(iterador_caracteres+1))){
                        //verificando se o iterador +2 é diferente de espaço em branco 
                        if((int)(iterador_caracteres+2)!=32){
                           
                            //veirificando se o próximo caracter é uma aspas simples ,dessa forma checando se o usuário fechou a constante
                            if(linha.charAt(iterador_caracteres+2)==39){
                                 // reconhecendo um caracter constante   
                                 caracter=caracter+linha.charAt(iterador_caracteres+1);
                                 caracter=caracter+linha.charAt(iterador_caracteres+2);
                                System.out.printf("\n TOKEN#Caracter constante#"+caracter+"#"+num_linha+"\n");
                                iterador_caracteres=+2;
                            }
                            else{
                                // mostando a mensagem de erro na construção de caracter constante
                                caracter=caracter+linha.charAt(iterador_caracteres+1);
                                System.out.printf("\n Erro Lexico - o terceiro caracter não é uma aspas simples : %s Linha: %d\n",caracter, num_linha);
                                iterador_caracteres=+2;
                                break;
                            }
                            
                        }
                        //erro o terceiro caracter é um espaço em branco o else trata isso e passa a mensagem para o usuário
                        else{
                        //mostrar para o usuário até que ponto tava a construnção do token 
                        caracter=caracter+linha.charAt(iterador_caracteres+1);
                            System.out.printf("\n Erro Lexico - o terceiro caracter eh um espaco branco (vc não fechou o caracter constante): %s Linha: %d\n",caracter, num_linha);
                        
                        iterador_caracteres=+2;
                        }
                
                }
                else{
                    ///quando tem caracter não aceito pela estrutura de caracter constante
                    System.out.printf("\n Erro Lexico - contém caracter não aceito : %s Linha: %d\n",caracter, num_linha);
                    iterador_caracteres=+2;
                    break;
                }
                }else{
                    System.out.printf("\n Erro Lexico - o segundo caracter eh um espaco branco : %s Linha: %d\n",caracter, num_linha);
                    iterador_caracteres=+2;
                }
                        
                    }
                         
            ///////////////////////////////////////////////////////////////////////////////////////////     
                    
                    
                    
                    
            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

                    
                    
                    
                    
                    
                    
                    
                    
                    
                    
                    
                    //LÃ³gica para numeros
                    //*******************************************************************
                    /*Este verifica se o caracter Ã© um numero inteiro ou real e imprime no arquivo.
                    Caso ocorra um erro do tipo 34edf ou 1. ou 1.2er ou 1.a ou 2.2.2 Ã© dito um erro lÃ©xico 
                    e a sequÃªncia de erro Ã© impressa junto a mensagem de erro e a linha.
                    */
                    else if(isDigit(current_char)){
                        String temp=""+current_char;//armazena primeiro digito 
                        //Se existe algum caracter antes de acabar a linha 
                        if(iterador_caracteres+1<row_size){
                            iterador_caracteres++;//pula para o proximo
                            current_char=linha.charAt(iterador_caracteres);//armazena
                        }
                        //Percorre parte inteira do nÃºmero e armazena
                        while(isDigit(current_char) && iterador_caracteres+1<row_size){
                            temp=temp+current_char;
                            iterador_caracteres++;
                            current_char= current_char=linha.charAt(iterador_caracteres);
                        }
                        
                        if(iterador_caracteres+1>=row_size){
                            iterador_caracteres=row_size;
                             temp=temp+current_char;
                        }
                        //Testa se hÃ¡ a parte fracionaria
                        if((int)current_char==46){
                            int cont_frac=0;//Conta quantos numeros tem depois do ponto flutuante, se tiver.
                            temp=temp+current_char;//armazena o ponto fracionario
                            //Testa se hÃ¡ mais caracteres na linha
                            if(iterador_caracteres+1<row_size){                                                               
                                iterador_caracteres++;//pula para o proximo
                                current_char=linha.charAt(iterador_caracteres);//pega o caractere atual
                                //Enquanto for digito e tiver mais caracteres na linha
                                while(isDigit(current_char) && iterador_caracteres+1<row_size){
                                    cont_frac++;//incrementa parte fracionaria
                                    temp=temp+current_char;
                                    iterador_caracteres++;//proxima iteraÃ§Ã£o de caractere                
                                    current_char= current_char=linha.charAt(iterador_caracteres);//atualiza o caracter atual
                                }                                
                                //Se o ultimo caractere iterado for um digito
                                if(isDigit(current_char)){
                                    temp=temp+current_char;
                                //Se nÃ£o Ã© um caractere invalido
                                }else if(!(isDelimiter(current_char) || (int)current_char==32 || isOperator(""+current_char)) || (int)current_char==46){
                                    cont_frac=0;
                                    int cont_err=0;//conta quantas vezes houve erro
                                    while((!(isDelimiter(current_char) || (int)current_char==32 || isOperator(""+current_char)) || (int)current_char==46) && iterador_caracteres+1<row_size){
                                        temp=temp+current_char;
                                        iterador_caracteres++;
                                        current_char=linha.charAt(iterador_caracteres);
                                        //Testa se a linha acabou, caso sim pega o ultimo caractere concatena 
                                        //ao temp e iguala o iterador ao tamanho da linha para poder ir para o proxima linha
                                        if(iterador_caracteres+1>=row_size && (!(isDelimiter(current_char) || (int)current_char==32 || isOperator(""+current_char))|| (int)current_char==46)){
                                           iterador_caracteres=row_size; 
                                           temp=temp+current_char;
                                        }
                                        cont_err++;
                                    }                                    
                                    if(cont_err==0){
                                        temp=temp+current_char;//concatena ultimo caractere errado ao temp
                                        iterador_caracteres++;
                                    }
                                }
                            }else{
                                iterador_caracteres=row_size; //Como foi o ultimo caractere isso Ã© feito para pular a linha na proxima iteraÃ§Ã£o
                            }                         
                            if(cont_frac>0){
                                System.out.printf("TOKEN#NUM_F#"+temp+"#"+num_linha+"\n");
                                writeFile.write("TOKEN#NUM_F#"+temp+"#"+num_linha+"\n");
                                iterador_caracteres--;         
                            }else{
                                System.out.printf("Erro Lexico Numero mal formado %s Linha : %d\n",temp, num_linha);
                                writeFile.write("Erro Lexico Numero mal formado com caracter Invalido "+temp+" Linha "+num_linha+"\n");
                                iterador_caracteres--; 
                            }
                        //ELSE para numero inteiro
                        }else{
                            //Testa se Ã© um numero inteiro vÃ¡lido verificando se o proximo caractere Ã© o fim de um inteiro: espaÃ§o delimitador ou operador
                            if(isOperator(""+current_char) || isDelimiter(current_char) || (int)current_char==32 || iterador_caracteres==row_size){
                                System.out.printf("TOKEN#NUM_I#"+temp+"#"+num_linha+"\n");
                                writeFile.write("TOKEN#NUM_I#"+temp+"#"+num_linha+"\n");
                                iterador_caracteres--;//volta uma posiÃ§Ã£o para pegar o espaÃ§o delimitador ou operador na prÃ³xima verificaÃ§Ã£o
                            }else{
                                    /*Percorre a linha apÃ³s a partir do ponto colocado errado atÃ© consumir 
                                    todos caracteres pertencentes ao numero errado*/ 
                                    temp=temp+current_char;
                                    while(iterador_caracteres+1<row_size){
                                        iterador_caracteres++;
                                        current_char=linha.charAt(iterador_caracteres);//pega o caractere atual
                                        
                                        if(isDelimiter(current_char) || (int)current_char==32 || isOperator(""+current_char)){
                                            iterador_caracteres--; 
                                            break;
                                        }
                                        temp=temp+current_char;
                                    }
                                    System.out.printf("Erro Lexico - NÃºmero mal formado: %s - linha: %d\n",temp,num_linha);
                                    writeFile.write("Erro Lexico Numero mal formado com caracter invalido "+temp+" Linha "+num_linha+"\n");
                            }                            
                        }                        
                    }                    
                    /*
                    Os caracteres espaÃ§o, \t, \r e \n nÃ£o sÃ£o considerados caracteres invalidos, no entanto caso ocorra algo que nÃ£o
                    esteja dentro dos padrÃµes analisados anteriormente o analisador entrara neste else if como um erro lexico, pois 
                    estarÃ¡ fora dos padrÃµes analisados.
                    */
                    else if(((int)current_char!=32)){                        
                        writeFile.write("Erro Lexico Caracter Invalido "+current_char+" Linha "+num_linha+"\n");
                        System.out.printf("Erro Lexico - Caracter Invalido: %c - linha: %d\n",current_char,num_linha);
                    }
                    next_char=' ';//reinicializa o proximo caractere para a analise anterior nÃ£o ser usada na prÃ³xima.
                    iterador_caracteres++;//incrementa caracteres de linha
                }//fecha loop de caracteres               
            }//fecha loop de linha
            //fecha os recursos de stream
            fileR.close();
            readFile.close();
            writeFile.close();
            fileW.close();
    }        
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args){             
        try {
            Analex analex;
            analex = new Analex();
            analex.analiser();
        } catch (FileNotFoundException e) {
            System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage());
        } catch (IOException e) {
            System.err.printf("Erro na leitura do arquivo: %s.\n", e.getMessage());
        }        
    }    
}
