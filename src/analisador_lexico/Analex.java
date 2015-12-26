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
 * @version 1.1.0
*/
public class Analex{
    private FileReader fileR;//atributo de leitura de arquivo
    private BufferedReader readFile;//atributo de buffer de leitura de arquivo
    private FileWriter fileW;//atributo de escrita de arquivo
    private BufferedWriter writeFile;//atributo de buffer de escrita de arquivo
    private final String[] reservedsWords;//atributo de vetor de palavras reservadas
    private boolean occurred_error;//atributo de ocorrencia de algum erro lexico na analise
    
    /**
     Este m�todo inicializa os atributos da classe Analex. Abre o arquivo de leitura e inicializa
     * o vetor de palavras reservadas para o analisador l�xico.
     * @throws java.io.FileNotFoundException
    */
    public Analex() throws FileNotFoundException,IOException{
        openFiletoRead();
        reservedsWords= new String[18];        
        reservedsWords[0]= "class";reservedsWords[1]= "const"; reservedsWords[2]= "else";
        reservedsWords[3]= "if";reservedsWords[4]= "new";reservedsWords[5]= "read";
        reservedsWords[6]= "write";reservedsWords[7]= "return";reservedsWords[8]= "void";
        reservedsWords[9]= "while";reservedsWords[10]= "int";reservedsWords[11]= "float";
        reservedsWords[12]= "bool";reservedsWords[13]= "string";reservedsWords[14]= "char";
        reservedsWords[15]= "true";reservedsWords[16]= "false";reservedsWords[17]= "main";
        occurred_error=false;
    }
    
    /**
    Este m�todo abre o arquivo de leitura do analisador l�xico. Caso o arquivo não possa
    * ser lido ele apresenta uma mensagem de erro de leitura.
    * @throws java.io.FileNotFoundException
    */
    private void openFiletoRead() throws FileNotFoundException{
        //Abre arquivo para leitura
        if(new File("../mi-compiladores/src/analisador_lexico/codigo.txt").canRead()){
            fileR = new FileReader("../mi-compiladores/src/analisador_lexico/codigo.txt");
            readFile = new BufferedReader(fileR);
        }else{
            System.out.print("O arquivo de leitura n�o pode ser lido\n");
        }
    }
    /**
    Este m�todo abre o arquivo de leitura do analisador l�xico. Caso o arquivo não possa
    * ser lido ele apresenta uma mensagem de erro de leitura.
    */
    private void openFiletoWrite() throws IOException{
        //Abre arquivo para leitura
        //construtor que recebe o objeto do tipo arquivo
        fileW = new FileWriter(new File("lexOut.txt"));
                
        if(new File("lexOut.txt").canRead()){
            //construtor recebe como argumento o objeto do tipo FileWriter
            writeFile = new BufferedWriter(fileW);
        }else{
            System.out.print("O arquivo de Escrita n�o pode ser aberto\n");
        }
    }
    /** Metodo que verifica se a entrada eh uma letra. As letras do alfebeto est�o definidos entre 
    * o numero decimal 65 � 90 e de 97 � 122 da tabela ASCII. Se receber um caracter com o numero decimal ascii nesta 
    * margem � dito uma letra.
    *@return true if is a letter*/
    private boolean isLetter(char caractere){
        //Testa se o valor ASCII do caracter esta entre 64 e 90(A..Z) ou entre  97 e 122 (a..z)
        return ((((int)caractere)>64 && ((int)caractere)<91) || (((int)caractere)>96 && ((int)caractere)<123));
    }
       
    /**
    * Este m�todo recebe um caracter e verifica se este � um digito.
    * Isto � feito utilizando o m�todo indexOf da String digitos que contem os digitos
    * de 0 � 9 e que recebe um caractere como par�metro e retorna um inteiro maior que 
    * zero caso este caractere esteja na String.
    */
    private boolean isDigit(char caractere){
        String digitos = "0123456789";        
        return (digitos.indexOf(caractere)>=0);
    }    
    /**
    * Este m�todo recebe um caracter e verifica se este � um delimitador.
    * Isto � feito utilizando o m�todo indexOf da String delimitadores que
    * contem os delimitadores {}[];.() e que recebe um caractere como par�metro
    * e retorna um inteiro maior que zero caso este substring esteja na String.
    */
    private boolean isDelimiter(char caractere){
        String delimitadores = "{}[];(),";        
        return (delimitadores.indexOf(caractere)>=0);
    }
    
    /**
    * Este m�todo recebe uma String e verifica se este � um operador.
    * Isto � feito comparando a String par�metro com os operadores presente na condicional.
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
     Este metodo recebe uma String como parametro e verifica se esta � uma palavra reservada. 
     * Se sim retorna true
     * @param word
     * @return true se � uma palavra reservada
     */
    private boolean isReservedWord(String word){           
        for (int i = 0; i <reservedsWords.length; i++) {
            if(word.equals(reservedsWords[i]))
                return true;
        }          
        return false;
    }   
    /**
    Este � o m�todo principal da classe Analex, pois � este que recebe um arquivo fonte para an�lise l�xica e 
    * e gera um arquivo de sa�da com os tokens e lexemas das cadeias de entrada. Este m�todo possui dois la�os
    * principais aninhados. Um la�o l� o arquivo linha a linha at� encontrar o fim de arquivo. Outro la�o l� 
    * caractere a caractere para verificar os padr�es contidos nas condicionais. Verificado cada padr�o este m�todo 
    * escreve em um arquivo de sa�da os determinados tokens e lexemas.
    */
    private void analiser() throws IOException{     
            //abre arquivo para escrita
            openFiletoWrite();
            //Armazena uma linha do arquivo
            String linha;
            //salva o numero atual da linha
            int num_linha=0;
            // guarda a posição do caracter atual na linha a ser analisado
            int iterador_caracteres;
            // guarda o caracter atual e proximo caracter a serem analisados
            char current_char,next_char;            
            //Este Loop faz a leitura uma a uma de cada linha do arquivo
            while((linha = readFile.readLine()) != null){
                current_char=next_char=' ';
                iterador_caracteres=0;
                num_linha++;
                //pega tamanho da linha para a iteração de caracteres
                int row_size=linha.length();
                while(iterador_caracteres<row_size){
                    //ler o primeiro caracter
                    current_char=linha.charAt(iterador_caracteres);                    

//L�gica para ignorar comentarios
//***************************************************************************************************
                    //Verifa se existe um proximo caracter para ser lido na linha atual e o armazena. 
                    if((iterador_caracteres+1)<row_size){
                        next_char=linha.charAt(iterador_caracteres+1);
                    }
                    
                    //Consumindo e pulando comentarios de linha: //
                    if(current_char=='/' && next_char=='/'){
                        /*Aqui a variavel iteradora se torna igual ao tamanho da linha, 
                        for�ando a saida do la�o de leitura de linha e pulando uma linha de comentario*/
                        iterador_caracteres=row_size;
                    /*Consmindo e pulando comentarios com mais de uma linha;\/**\/.
                        A logica utilizada nesta condicional � a de verficar se o caracter atual e o proximo formam
                        a cadeia /*. Caso esta cadeia todos os caracteres posteriores são consumidos at� encontrar 
                        a sequencia *\/*/
                    }else if(current_char=='/' && next_char=='*'){
                        //Esta variavel armazena o valor da linha que iniciou o comentario caso ocorra um erro
                        int num_row_coment=num_linha;
                        //Este loop consome todos os caracteres dentro dos delimitadores de comentario de mais de uma linha
                        while(!(current_char=='*' && next_char=='/')){
                            if((iterador_caracteres+2)<row_size){                                
                                iterador_caracteres++;
                                current_char=linha.charAt(iterador_caracteres);//ler o caracter ap�s caracter /                    
                                next_char=linha.charAt(iterador_caracteres+1);//ler o proximo caracter                                   
                            //Caso n�o existe um proximo caracter a ser lido    
                            }else{
                                linha=readFile.readLine();//uma nova linha � lida
                                iterador_caracteres=0;//coloca o iterador no inicio da linha
                                //Enquanto h� linha a ser lida
                                if(linha!=null){                                    
                                    row_size=linha.length();//pega o tamnho da linha para itera��o dos caracteres
                                    num_linha++;
                                    if((iterador_caracteres+1)<row_size){
                                        current_char=linha.charAt(iterador_caracteres);                    
                                        next_char=linha.charAt(iterador_caracteres+1);
                                    }
                                }else{                                   
                                    //Se n�o h� mais linhas a serem lidas e ainda n�o encontrou a sequencia /*
                                    occurred_error=true;//ocorreu um erro lexico.
                                    writeFile.write("Erro Lexico  Comentario n�o foi fechado  Linha "+num_linha+"\n");
                                    System.out.printf("Erro Lexico na linha %d Comentario n�o foi fechado\n",num_row_coment); 
                                    row_size = iterador_caracteres=0;
                                    break;
                                }
                            }   
                        } 
                        /*Como foi usada a l�gica de pegar o caractere corrente e o proximo este incrementador serve para ignorar 
                        o proximo caracter ao fim da analise de comentario, no caso o '/'*/
                        iterador_caracteres++;                      
                    }
//L�gica para palavras reservadas e identificadores
//***************************************************************************************************
                    /*IF para verificar se � uma palavra reservada ou um identificador
                    Inicialmente verifica se o primeiro caractere � uma letra*/
                    else if(isLetter(current_char)){
                        /*Apos verificar que o primeiro caractere da palavra sendo uma letra percorre-se a sequ�ncia de
                        caracteres at� encontrar um caractere que n�o faz parte do padr�o ou o caractere de fim de linha.
                        A variavel iterador come�a a partir do pr�ximo caracter da linha.
                        A variavel erro guarda um true se ocorrer algum erro de padr�o na sequencia de caracteres para
                        identificador.
                        */  
                        iterador_caracteres++;
                        boolean erro=false;//caso ocorra algum erro
                        String temp_caracters=""+current_char;//armazena o lexema temporario
                        //Este loop vai iterar caracter a caracter at� o fim da linha
                        while(iterador_caracteres<row_size){
                            current_char=linha.charAt(iterador_caracteres);//ler o caracter a ser analisado agora
                            //verifica se h� um proximo caracter a ser lido para a analise de operadores com 2 caracteres
                            if(iterador_caracteres+1<row_size){
                                next_char=linha.charAt(iterador_caracteres+1);//ler o caracter a ser analisado agora
                            }                            
                            //Aninhamento de IFs para o teste analise lexica para um identificador
                            //Caracteres v�lidos de um identificador ou palavra reservada
                            if(isLetter(current_char) || isDigit(current_char) || current_char=='_'){
                                temp_caracters= temp_caracters+current_char;
                            /*Caso no meio da analise de um identificador ou palavra reservada exista um caracte espa�o 
                                um operador ou delimitador este tipo de analise � encerrada e volta-se uma posi��o para
                                garantir a an�lise do novo padr�o. !(((int)current_char==32) || ((int)current_char==9) || ((int)current_char==11) || ((int)current_char==13))*/    
                            }else if(((int)current_char)==32 || isOperator(""+current_char) || isOperator(""+next_char+current_char) || isDelimiter(current_char)
                                    ||  ((int)current_char==9) || ((int)current_char==11) || ((int)current_char==13)){
                                //Se for alguma das op��es acima volta uma posi��o para analisar o proximo padr�o e sai deste loop.                               
                                iterador_caracteres--;
                                break;
                            }else if(current_char!='\n'){
                                //Se ocorrer algum outro simbolo que n�o esteja nas verifica��es acima s�o considerado  um erro lexico
                                erro=true;//Faz erro verdadeiro
                                occurred_error=true;//ocorreu um erro lexico.
                                writeFile.write("Erro Lexico Identificador com caracter inv�lido "+current_char+" Linha "+num_linha+"\n");
                                System.out.printf("Erro Lexico Identificador com caracter invalido: %c Linha %d\n",current_char, num_linha);
                                break;
                            }
                            iterador_caracteres++;
                        }
                        if(erro){
                            /*J� que ocorreu algum erro este loop percorre e consome o resto da sequencia 
                            de caracteres ap�s o erro lexico. Se outra palavra for iniciada haver� a verifica��o de outro padr�o, pois
                            este loop e o seu loop pai ser�o pulados*/
                            while(iterador_caracteres+1<row_size){
                                    iterador_caracteres++;
                                    current_char = linha.charAt(iterador_caracteres);//ler o caracter a ser analisado agora
                                    if(((int)current_char==32) || isDelimiter(current_char) || isOperator(""+current_char) || ((int)current_char==13) || ((int)current_char ==32)){
                                        //Se for alguma das opções acima volta uma posição e sai deste loop para verificar outro padrão.
                                        iterador_caracteres--;
                                        break;
                                    }
                            }
                            erro=false;
                        }else{
                            //Se n�o houver nenhum erro lexico imprime no arquivo
                            //Neste IF verifica-se se a palavra � reservada
                            if(isReservedWord(temp_caracters)){
                                //TOKEN_RES_palavra_NumeroDaLinha
                                //escreve o conteudo no arquivo
                                writeFile.write("TOKEN#RES#"+temp_caracters+"#"+num_linha+"\n");
                                System.out.printf("TOKEN#RES#"+temp_caracters+"#"+num_linha+"\n");
                            }else{
                                //TOKEN_ID_palavra_NumeroDaLinha
                                writeFile.write("TOKEN#ID#"+temp_caracters+"#"+num_linha+"\n");
                                System.out.printf("TOKEN#ID#"+temp_caracters+"#"+num_linha+"\n");
                            }                                  
                        }
                    }    
//L�gica para  verificar n�meros negativos
//***************************************************************************************************                    
                    //verifica se atual � um - e o pr�ximo � um n�mero para entrar na l�gica
                    else if(isDigit(next_char) && (int)current_char == 45  ){
                        String numero;
                        numero=""+current_char;
                        current_char=next_char;
                      
                        iterador_caracteres++;
                       //percorrendo os caracteres
                        while(isDigit(current_char) && iterador_caracteres+1<row_size){
                            numero=numero+current_char;
                            
                            iterador_caracteres++;
                            current_char=linha.charAt(iterador_caracteres);
                           }
                        //parte fracionario
                        if ((int)current_char == 46) {
                            numero = numero + current_char;
                            //verifica pra n�o dar erro de espa�o em branco
                            if (iterador_caracteres + 1 < row_size) {
                                //  verificar se � n�mero
                                iterador_caracteres++;
                                current_char = linha.charAt(iterador_caracteres);
                                //System.out.printf("valor de current" + current_char + "\n");
                                while (isDigit(current_char) && iterador_caracteres + 1 < row_size) {  
                                    numero = numero + current_char;                             
                                    iterador_caracteres++;
                                    current_char = linha.charAt(iterador_caracteres);
                                }
                                //adicionando o ultimo numero q n�o entra na contagem                                
                                //Se o atual caracter for um n�mero adicionar na vari�vel n�mero(token)
                                if(isDigit(current_char)){
                                    numero = numero + current_char;
                                    System.out.printf("TOKEN#NUM_F#"+""+numero+"#"+num_linha+"\n");
                                    writeFile.write("TOKEN#NUM_F#"+numero+"#"+num_linha+"\n");
                                    iterador_caracteres++;
                                }
                                // quando o atual for em branco 
                                else if ((int)current_char == 32){
                                    System.out.printf("TOKEN#NUM_F#"+""+numero+"#"+num_linha+"\n");
                                    writeFile.write("TOKEN#NUM_F#"+numero+"#"+num_linha+"\n");
                                }
                              //quando entrar algo errado no n�mero.
                              else if(!isDigit(current_char)){
                                //registrando o erro no n�mero 
                                numero=numero+current_char;
                                //percorrendo a string errada at� terminar 
                                while((int)current_char !=32  && iterador_caracteres +1 < row_size){
                                    iterador_caracteres++;
                                    current_char = linha.charAt(iterador_caracteres);
                                    numero = numero + current_char;
                                }
                                occurred_error=true;//ocorreu um erro lexico.
                                 System.out.printf("Erro Lexico Numero mal formado %s Linha %d\n",numero, num_linha);
                                 writeFile.write("Erro Lexico Numero mal formado "+numero+" Linha "+num_linha+"\n");
                            }
                            
                            }
                        }
                        //quando n�o tiver a parte de fra��o 
                        else{
                            // quando o �ltimo caracter n�o for n�mero
                            if( (int)current_char == 32 ){
                                 System.out.printf("TOKEN#NUM_F#"+numero+"#"+num_linha+"\n");
                                 writeFile.write("TOKEN#NUM_F#"+numero+"#"+num_linha+"\n");
                            }
                            //passando quando o atual � um n�mero e t� na estrutura correta
                            else if(isDigit(current_char)){
                                numero = numero + current_char;
                                System.out.printf("TOKEN#NUM_F#"+numero+"#"+num_linha+"\n");
                                writeFile.write("TOKEN#NUM_F#"+numero+"#"+num_linha+"\n");
                                iterador_caracteres++;
                            }
                         //quando o atual n�o � um d�gito , ou seja, o n�mero n�o estar na estrutura correta
                            else if(!isDigit(current_char)){
                                //registrando o erro no n�mero 
                                numero=numero+current_char;
                                //percorrendo a string errada at� terminar 
                                while((int)current_char !=32  && iterador_caracteres +1 < row_size){
                                    iterador_caracteres++;
                                    current_char = linha.charAt(iterador_caracteres);
                                    numero = numero + current_char;
                                }
                                occurred_error=true;//ocorreu um erro lexico.
                                 System.out.printf("Erro Lexico Numero mal formado %s Linha %d\n",numero, num_linha);
                                 writeFile.write("Erro Lexico Numero mal formado "+numero+" Linha "+num_linha+"\n");
                            }    
                      }
                    }
// l�gica para cadeia constante      
//***************************************************************************************************                                        
                    // verifica se tem as aspas duplas para poder verificar os pr�ximos caracteres 
                    else if(current_char == '"'){
                        //string que vai armazenar a cadeia constante e exibir para o usu�rio   
                        String cadeia=""+current_char;
                        if((iterador_caracteres+1)<row_size){
                            current_char=linha.charAt(iterador_caracteres+1);
                        }
                        cadeia = cadeia+current_char;
                        iterador_caracteres++;
                        boolean erro=false;
                        // percorrendo toda a cadeia constante at� achar o delimitador 
                        while(current_char !='"'){
                            if((iterador_caracteres+2)<=row_size){
                               current_char = linha.charAt(iterador_caracteres+1);//ler o primeiro caracter   
                            }else{
                                erro=true;
                                break;
                            }                        
                            cadeia = cadeia+current_char;
                            iterador_caracteres++;
                        }
                        if(erro){
                            occurred_error=true;//ocorreu um erro lexico.
                            System.out.printf("Erro Cadeia constante n�o fechou Linha "+num_linha+"\n");
                            writeFile.write("Erro Cadeia constante n�o fechou "+cadeia+" Linha "+num_linha+"\n");
                        }else{
                            System.out.printf("TOKEN#CAD#"+cadeia+"#"+num_linha+"\n");
                            writeFile.write("TOKEN#CAD#"+cadeia+"#"+num_linha+"\n");
                        }                   
                    }
// l�gica para caractere constante                          
//***************************************************************************************************                                        
                    else if(current_char==39){
                        String caracter=""+current_char;
                        //// vendo se o iterador +1 � em branco e tratando isso com o else
                        if((int)(iterador_caracteres+1)!=32){
                          // verificando se est�o na faixa de parametro aceita pela estrutura lexica
                          if(isDigit(linha.charAt(iterador_caracteres+1))||isLetter(linha.charAt(iterador_caracteres+1))){
                                   //verificando se o iterador +2 � diferente de espa�o em branco 
                                    if((int)(iterador_caracteres+2)!=32){
                                      //veirificando se o pr�ximo caracter � uma aspas simples ,dessa forma checando se o usu�rio fechou a constante
                                      if(linha.charAt(iterador_caracteres+2)==39){
                                           // reconhecendo um caracter constante   
                                           caracter=caracter+linha.charAt(iterador_caracteres+1);
                                           caracter=caracter+linha.charAt(iterador_caracteres+2);
                                          System.out.printf("TOKEN#CAR#"+caracter+"#"+num_linha+"\n");
                                          writeFile.write("TOKEN#CAR#"+""+caracter+"#"+num_linha+"\n");
                                          iterador_caracteres=+2;
                                      }
                                      else{
                                          // mostando a mensagem de erro na constru��o de caracter constante
                                          caracter=caracter+linha.charAt(iterador_caracteres+1);
                                          occurred_error=true;//ocorreu um erro lexico.
                                          System.out.printf("Erro Lexico o Terceiro caracter n�o � uma aspas simples %s Linha %d\n",caracter, num_linha);
                                          writeFile.write("Erro Lexico o Terceiro caracter n�o � uma aspas simples "+caracter+" Linha "+num_linha+"\n");
                                          iterador_caracteres=+2;
                                          break;
                                      }
                                }
                                //erro o terceiro caracter � um espa�o em branco o else trata isso e passa a mensagem para o usu�rio
                                else{
                                  //mostrar para o usu�rio at� que ponto tava a construn��o do token 
                                      caracter=caracter+linha.charAt(iterador_caracteres+1);
                                      occurred_error=true;//ocorreu um erro lexico.
                                      System.out.printf("Erro Lexico n�o fechou o caracter %s Linha %d\n",caracter, num_linha);
                                      writeFile.write("Erro Lexico n�o fechou o caracter "+caracter+" Linha "+num_linha+"\n");
                                      iterador_caracteres=+2;
                                }
                          }
                          else{
                              ///quando tem caracter n�o aceito pela estrutura de caracter constante
                               occurred_error=true;//ocorreu um erro lexico.
                              System.out.printf("Erro Lexico cont�m caracter n�o aceito %s Linha %d\n",caracter, num_linha);
                              writeFile.write("Erro Lexico cont�m caracter n�o aceito "+caracter+" Linha "+num_linha+"\n");
                              iterador_caracteres=+2;
                              break;
                          }
                          }else{
                              // quando o segundo caracter � um espa�o em branco 
                               occurred_error=true;//ocorreu um erro lexico.
                               System.out.printf("Erro Lexico cont�m caracter n�o aceito %s Linha %d\n",caracter, num_linha);
                               writeFile.write("Erro Lexico cont�m caracter n�o aceito "+caracter+" Linha "+num_linha+"\n");
                              iterador_caracteres=+2;
                          }
                    }
//L�gica para Operadores
//******************************************************************************************************
                    //Este verifica se o caracter � um operador duplo e imprime no arquivo.Pega o atual e o proximo caractere
                    else if((int)next_char!=32 &&  isOperator(""+current_char+next_char)){
                        writeFile.write("TOKEN#OP#"+""+current_char+next_char+"#"+num_linha+"\n");                        
                        System.out.printf("TOKEN#OP#"+""+current_char+next_char+"#"+num_linha+"\n");
                        iterador_caracteres++;
                    }
                    //Este verifica se o caracter � um operador un�rio e imprime no arquivo. Pega apenas o caracter atual.
                    else if(isOperator(""+current_char)){
                        writeFile.write("TOKEN#OP#"+""+current_char+"#"+num_linha+"\n");
                        System.out.printf("TOKEN#OP#"+""+current_char+"#"+num_linha+"\n");
                    }    
                 
//L�gica para delimitadores
//******************************************************************************************************
                    //Este verifica se o caracter � um delimitador e imprime no arquivo.
                    else if(isDelimiter(current_char)){
                        writeFile.write("TOKEN#DEL#"+current_char+"#"+num_linha+"\n");
                        System.out.printf("TOKEN#DEL#"+current_char+"#"+num_linha+"\n");
                    }
//L�gica para numeros reais e inteiros positivos
//******************************************************************************************************
                    /*Este verifica se o caracter � um numero inteiro ou real e imprime no arquivo.
                    Caso ocorra um erro do tipo 34edf ou 1. ou 1.2er ou 1.a ou 2.2.2 � dito um erro l�xico 
                    e a sequ�ncia de erro é impressa junto a mensagem de erro e a linha.
                    */
                    else if(isDigit(current_char)){
                        String temp="";//armazena primeiro digito 
                        //Percorre parte inteira do n�mero e armazena
                        while(isDigit(current_char)){
                            if(iterador_caracteres+1<row_size){
                                temp=temp+current_char;
                                iterador_caracteres++;
                                current_char= current_char=linha.charAt(iterador_caracteres);                                
                            }else{
                                iterador_caracteres=row_size;
                                break;
                            }                                                         
                        }                        
                        //Pega o ultimo caractere caso seja digito
                        if(isDigit(current_char)){
                            temp=temp+current_char;
                        }
                        //Testa se h� a parte fracionaria
                        if((int)current_char==46){
                            int cont_frac=0;//Conta quantos numeros tem depois do ponto flutuante, se tiver.
                            temp=temp+current_char;//armazena o ponto fracionario
                            //Testa se h� mais caracteres na linha
                            if(iterador_caracteres+1<row_size){
                                iterador_caracteres++;//pula para o proximo
                                current_char=linha.charAt(iterador_caracteres);//pega o caractere atual
                                //Enquanto for digito e tiver mais caracteres na linha
                                while(isDigit(current_char)){
                                    if(iterador_caracteres+1<row_size){
                                        cont_frac++;//incrementa parte fracionaria
                                        temp=temp+current_char;
                                        iterador_caracteres++;//proxima itera��o de caractere                
                                        current_char= current_char=linha.charAt(iterador_caracteres);//atualiza o caracter atual
                                    }else{
                                        iterador_caracteres=row_size;
                                        break;
                                    }
                                }                                
                                //Se o ultimo caractere iterado for um digito
                                if(isDigit(current_char)){
                                    temp=temp+current_char;
                                    cont_frac++;
                                //Se n�o � um caractere invalido
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
                                iterador_caracteres=row_size; //Como foi o ultimo caractere isso � feito para pular a linha na proxima itera��o
                            }                         
                            if(cont_frac>0){
                                System.out.printf("TOKEN#NUM_F#"+temp+"#"+num_linha+"\n");
                                writeFile.write("TOKEN#NUM_F#"+temp+"#"+num_linha+"\n");
                                iterador_caracteres--;         
                            }else{
                                occurred_error=true;//ocorreu um erro lexico.
                                System.out.printf("Erro Lexico Numero mal formado %s Linha %d\n",temp, num_linha);
                                writeFile.write("Erro Lexico Numero mal formado com caracter Invalido "+temp+" Linha "+num_linha+"\n");
                                iterador_caracteres--; 
                            }
                        //ELSE para numero inteiro
                        }else{
                            //Testa se � um numero inteiro v�lido verificando se o proximo caractere � o fim de um inteiro: espa�o delimitador ou operador
                            if(isOperator(""+current_char) || isDelimiter(current_char) || (int)current_char==32 || iterador_caracteres==row_size){
                                System.out.printf("TOKEN#NUM_I#"+temp+"#"+num_linha+"\n");
                                writeFile.write("TOKEN#NUM_I#"+temp+"#"+num_linha+"\n");
                                iterador_caracteres--;//volta uma posi��o para pegar o espa�o delimitador ou operador na pr�xima verifica��o
                            }else{
                                    /*Percorre a linha ap�s a partir do ponto colocado errado at� consumir 
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
                                    occurred_error=true;//ocorreu um erro lexico.
                                    System.out.printf("Erro Lexico N�mero mal formado: %s - linha: %d\n",temp,num_linha);
                                    writeFile.write("Erro Lexico Numero mal formado com caracter invalido "+temp+" Linha "+num_linha+"\n");
                            }                            
                        }                        
                    }              
                    /*
                    Os caracteres espa�os, \t, \r e \n (ASCII: 32,9,11,13) n�o s�o considerados caracteres invalidos, no entanto caso ocorra algo que n�o
                    esteja dentro dos padr�es analisados anteriormente o analisador entrara neste else if como um erro lexico, pois 
                    estar�o fora dos padr�es analisados.
                    */
                    else if(!(((int)current_char==32) || ((int)current_char==9) || ((int)current_char==11) || ((int)current_char==13))){                        
                        occurred_error=true;//ocorreu um erro lexico.
                        writeFile.write("Erro Lexico Caracter Invalido "+current_char+" Linha "+num_linha+"\n");
                        System.out.printf("Erro Lexico Caracter Invalido %c Linha: %d\n",current_char,num_linha);
                    }
                    next_char=' ';//reinicializa o proximo caractere para a analise anterior n�o ser usada na pr�xima.
                    iterador_caracteres++;//incrementa caracteres de linha
                }//fecha loop de caracteres               
            }//fecha loop de linha
            //Se n�o ocorrer nenhum erro l�xico exibe mensagem de Sucesso no fim do arquivo.
            if(!occurred_error){
                writeFile.write("Sucesso\n");
                System.out.printf("Sucesso\n");
            }
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
