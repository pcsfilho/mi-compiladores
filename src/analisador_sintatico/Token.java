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
    private String tipo;
    private String lexema;
    private String linha;    
    
    public Token(){
        tipo="";
        lexema="";
        linha="";
    }
    
    public Token(String token){
        splitToken(token);
    }
    
    public String get_tipo(){
        return this.tipo;
    }
    
    public String get_lexema(){
        return this.lexema;
    }
    
    public String get_linha(){
        return this.linha;
    }
    
    private void splitToken(String token){
        String[] separated = token.split("#");
        this.tipo=separated[1];
        
        this.lexema=separated[2];
        
        this.linha=separated[3];
        
    }
}
