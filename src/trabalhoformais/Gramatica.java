/* Um objeto gramática é composto por uma lista de tamanho desconhecido de terminais
uma lista de tam = ? de variáveis
uma lista de lista de produçoes que são as suas derivaçoes
e uma string representando a variável inicial
*/
package trabalhoformais;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;


public class Gramatica {
    public ArrayList <String> terminais = new ArrayList(); 
    public ArrayList <String> variaveis = new ArrayList(); 
    public ArrayList <Producao> producoes = new ArrayList();
    private String inicial = new String();
    private String relatorio = "relatoriofinal.txt";


    public Gramatica() {
    }

    public ArrayList getTerminais() {
        return terminais;
    }

    public void setTerminais(ArrayList terminais) {
        this.terminais = terminais;
    }

    public ArrayList getVariaveis() {
        return variaveis;
    }

    public void setVariaveis(ArrayList variaveis) {
        this.variaveis = variaveis;
    }

    public String getInicial() {
        return inicial;
    }

    public ArrayList<Producao> getProducoes() {
        return producoes;
    }

    public void setProducoes(ArrayList<Producao> producoes) {
        this.producoes = producoes;
    }

    public void setInicial(String inicial) {
        this.inicial = inicial;
    }
    
    public void imprime_gramatica(){
        int i,j;

        System.out.println("                ||GRAMATICA||");
        System.out.print("- Terminais: { ");
        for(i = 0; i < this.terminais.size(); i++)
            System.out.print(this.terminais.get(i)+ "| ");
        System.out.println(" }.");
        
        System.out.print("- Variaveis: { ");
        for(i = 0; i < this.variaveis.size(); i++)
            System.out.print(this.variaveis.get(i)+ "| ");
        System.out.println(" }.");
        
        System.out.println("- Inicial: { " + this.inicial + " }.");
        
        System.out.println("- Regras: { ");
        for(i = 0; i < this.producoes.size(); i++){
            System.out.print(this.producoes.get(i).getVariavel() + " -> ");
            for( j = 0; j < this.producoes.get(i).getProducoes().size(); j++)
                System.out.print(this.producoes.get(i).getProducoes().get(j) + " ");
            System.out.println(";");
        }
        System.out.println("}\n--------------------------------------------------------------------------");
            
    }
    
      public void grava_gramatica() throws IOException{
        int i,j;
        
        FileWriter arq = new FileWriter(this.relatorio,true);
        PrintWriter gravarArq = new PrintWriter(arq);
        
        
        gravarArq.println("                ||GRAMATICA||");
        gravarArq.print("- Terminais: { ");
        for(i = 0; i < this.terminais.size(); i++)
            gravarArq.print(this.terminais.get(i)+ "| ");
        gravarArq.println(" }.");
        
        gravarArq.print("- Variaveis: { ");
        for(i = 0; i < this.variaveis.size(); i++)
            gravarArq.print(this.variaveis.get(i)+ "| ");
        gravarArq.println(" }.");
        
        gravarArq.println("- Inicial: { " + this.inicial + " }.");
        
        gravarArq.println("- Regras: { ");
        for(i = 0; i < this.producoes.size(); i++){
            gravarArq.print(this.producoes.get(i).getVariavel() + " -> ");
            for( j = 0; j < this.producoes.get(i).getProducoes().size(); j++)
                gravarArq.print(this.producoes.get(i).getProducoes().get(j) + " ");
            gravarArq.println(";");
        }
        gravarArq.println("}");
        gravarArq.println("--------------------------------------------------------------------------");
        arq.close();
        
            
    }
    
    
    
    
    /* Classe auxiliar responsável por servir de Comparator para
    deixar ordenadas as producoes
    */

    public static Comparator<Producao> StuNameComparator = new Comparator<Producao>() {

	public int compare(Producao g1, Producao g2) {
	   String ProducaoName1 = g1.getVariavel().toUpperCase();
	   String ProducaoName2 = g2.getVariavel().toUpperCase();

	   //ascending order
	   return ProducaoName1.compareTo( ProducaoName2);

	   //descending order
	   //return StudentName2.compareTo(StudentName1);
    }};


}