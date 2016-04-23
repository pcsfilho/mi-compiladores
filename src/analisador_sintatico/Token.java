/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analisador_sintatico;

/**
 * @author Paulo
 */
public class Token{
    private String tipo;//Este atributo ira guarda o tipo da variavel(int,char,bool,float)
    private String lexema;//Guarda o conteudo do token
    private String linha;//Guarda a linha da ocorrencia do token    
    private int escopo;//Guarda o escopo do token
    private String padrao;//Guarda a classificação do token(RES,CAD,CAR,NUM_I,NUM_F)
    public Token(){
        tipo="";
        lexema="";
        linha="";
        padrao="";
        escopo=0;
    }
    
    public Token(String token){
        splitToken(token);
    }
    
    public String get_padrao(){
        return this.padrao;
    }
    
    public String get_lexema(){
        return this.lexema;
    }
    
    public String get_linha(){
        return this.linha;
    }
    
    private void splitToken(String token){
        String[] separated = token.split("#");
        this.padrao=separated[1];
        
        this.lexema=separated[2];
        
        this.linha=separated[3];
    }
}
