DOCUMENTAÇÃO DO PROJETO
=======================
Este repositório contém um projeto java implementado na Netbeans IDE 8.1. A versão java utilizada para a implementação do projeto
foi utilizado a versão 1.8 do JAVA.

Autor: Paulo Cezar dos Santos Filho(paulo.ecomp@gmail.com)

Autor: rejânio Moraes Filho.(rejaniomoraes@gmail.com)

Execução do código fonte
------------------------

Para executar o analisador léxico é necessario ter instalado em sua máquina no minimo o ambiente de execução JAVA. Caso queira
fazer alguma alteração no código presente na pasta src deve-se instalar o ambiente de desenvolvimento JAVA para imlementação e simulação.
Para executar o .jar do projeto deve-se acessar a pasta do projeto analisador_lexico utilizando o shell e executar o comando:

```bash
 java -jar analisador_lexico.jar
```

Arquivos presentes
-----------------------------------
* codigo.txt - Este arquivo contém o código fonte a ser analisado pelo analisador léxico. Caso queira testar outros códigos deve-se inserir-los neste arquivo.

* Analex.java - Este arquivo contem a classe java resposavel pela análise léxica do código fonte.

* lexOut.txt - Este arquivo contém o resultado da análise léxica. Ele é criado ao executar o analisador lexico.


* Analisador Léxico
-------------------

  1. A implementação do analisador léxico para a linguagem de programação JAVA foi definida de acordo com as expressões regulares definidas na tabela abaixo. 
  
| Palavra Token                        | Expressão regular correspondente     |
|--------------------------------------|--------------------------------------|
| Palavras reservadas                  | class, const, else, if, new, read, write, return, void, while, int, float, bool, string, char, true, false, main |
| Identificadores                      | ```Letra(Letra|Dígito|_)*```                          |
| Número                               | (-)?Dígito+(.Dígito+)?                              |
| Letra                                | ```(a..z|A..Z)```                                 |
| Dígito                               | 0..9                                        |
| Símbolo                              | ASCII de 33 a 126                               |
| Cadeia Constante                     | ```"(Letra|Dígito|Símbolo (exceto 34))*"```            |
| Caractere Constante                  | ```'(Letra|Dígito)'```           |
| Operadores                           | ```. + - * / ++ -- == != > >= < <= && || =```         |
| Delimitadores                        | ;,(){}[]                                |
| Comentários de Linha                 | /* Isto é um comentário de bloco */```             |
| Comentários de Bloco                 | // Isto é um comentário de linha```                |

* A execução do analisador léxico resulta no arquivo lexOut.txt que contem em cada linha um token representando os padrões analisados. Estes contem o tipo de token, o lexema e a linha correspondente a ocorrência do símbolo. Abaixo é apresentada a formação de cada tipo de token possível para a linguagem.
 
| TOKEN#RES - Palavra reservada            |
 ----------------------------
| TOKEN#RES#palavra_reservada#numero_linha |
  
| TOKEN#ID# - Identificador                |
 --------------------------
| TOKEN#ID#identificador#numero_linha      |

| TOKEN#NUM# - Número                      |
 ---------------
| TOKEN#NUM_I# - Inteiro                   |
| TOKEN#NUM_F# - Real                      |

| TOKEN#CAD# - Cadeia Constante            |
 --------------------------------

| TOKEN#CAD# - Caractere Constante         |
 --------------------------------

| TOKEN#OP# - Operador                     |
 -------------------
| TOKEN#OP#operador#numero_linha           |
 
| TOKEN#DEL# - DEL                         |
 -------------------
| TOKEN#DEL#delimitador#numero_linha       |

