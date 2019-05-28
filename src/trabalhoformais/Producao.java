/* Uma produção é uma subclasse da gramática
em que há uma string variável e mais uma lista encadeada com as derivaçoes dela
Ex.: S -> A | Ab | b
*/
package trabalhoformais;

import java.util.ArrayList;


public class Producao {
  
public ArrayList <String> producoes = new ArrayList(); 
private String variavel = new String();

    public Producao() { 
    }

    public ArrayList<String> getProducoes() {
        return producoes;
    }

    public void setProducoes(ArrayList<String> producoes) {
        this.producoes = producoes;
    }

    public String getVariavel() {
        return variavel;
    }

    public void setVariavel(String variavel) {
        this.variavel = variavel;
    }
   
}
