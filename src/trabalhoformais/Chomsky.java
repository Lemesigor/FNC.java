
package trabalhoformais;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;


public class Chomsky {
    Gramatica gramatica_recebida = new Gramatica();
    Gramatica gramatica_chomsky = new Gramatica (); 
    private String variavel_nova = new String() ; // as novas variáveis ficarão com rótulo de números que serão aumentados de acordo com o seu uso
    private ArrayList <Producao> producoes_novas = new ArrayList();
    private int contador_variavel = 1;  //Serve para colocar como rótulo das variáveis da etapa 3 (método converteN3)
    private String relatorio = "relatoriofinal.txt"; 

    public Chomsky(Gramatica gramatica) {
        this.gramatica_recebida = gramatica; 
        this.gramatica_chomsky = gramatica; 
    }

    public Gramatica getGramatica_recebida() {
        return gramatica_recebida;
    }

    public void setGramatica_recebida(Gramatica gramatica_recebida) {
        this.gramatica_recebida = gramatica_recebida;
    }

    public Gramatica getGramatica_chomsky() {
        return gramatica_chomsky;
    }

    public void setGramatica_chomsky(Gramatica gramatica_chomsky) {
        this.gramatica_chomsky = gramatica_chomsky;
    }

   
     
    //Etapa 1.1
    /*Remove as produções vazias de uma gramática simplificada
    pois a FNC não as vê como parte da linguagem
    */
    public void removeVazios (){
        int i;
        
        for (i = 0; i <getGramatica_recebida().getProducoes().size(); i++ ){
             for (int j = 0; j < getGramatica_recebida().getProducoes().get(i).getProducoes().size(); j++){
            if (getGramatica_recebida().getProducoes().get(i).getProducoes().get(j).equals("V")) // Se uma variável gera a palavra vazia, remove da lista de produçoes
                getGramatica_recebida().getProducoes().remove(i);
                }
        }
    }
    
    /* Etapa 2
    Remove transformaçòes do lado direito de comprimento n >= 2 
    */
    public Gramatica converteGramatica () throws IOException{ 
        int i, tam_producoes;
        Producao producao_buffer; 
        
        for (i = 0; i <getGramatica_recebida().getProducoes().size(); i++ ){
             if( getGramatica_recebida().getProducoes().get(i).getProducoes().size() >= 2){
                 producao_buffer = getGramatica_recebida().getProducoes().get(i);  // Se a variável tiver uma ou mais derivaçoes, passa para o buffer
                 producao_buffer = converteN2(producao_buffer); 
                 getGramatica_chomsky().getProducoes().set(i, producao_buffer); // Substitui a producao com terminal e variáveis pela forma só com variáveis         
            }
        }
        this.gramatica_chomsky.producoes.addAll(producoes_novas); //Passa a lista de novas produções para a gramática de chomsky
        this.gramatica_recebida = this.gramatica_chomsky; // Passar a trabalhar pela gramática já editada
        this.producoes_novas.removeAll(producoes_novas); //Limpa o array de produções
        
        //Grava a gramática após a etapa dois
        FileWriter arq = new FileWriter(relatorio,true);
        PrintWriter gravarArq = new PrintWriter(arq);
        gravarArq.println("\n                 -Etapa 2-");
        arq.close();  
        this.gramatica_recebida.grava_gramatica();
        
        tam_producoes = getGramatica_recebida().getProducoes().size();
        i = 0; 
        
        while (i < tam_producoes){
            if (getGramatica_recebida().getProducoes().get(i).getProducoes().size() > 2){ //Uma produção tiver n > 2 derivações
                producao_buffer = getGramatica_recebida().getProducoes().get(i); // Salva no buffer p fazer as alterações
                getGramatica_chomsky().getProducoes().remove(i); // P3 = P3 - ( A -> B1, B2,..., Bn);  Remove a produção da gramática
                producao_buffer = converteN3(producao_buffer);
                getGramatica_chomsky().getProducoes().add(producao_buffer); //adiciona a nova produção na gramática            
            } 
            else
                i ++; 
        }  
        this.gramatica_chomsky.producoes.addAll(producoes_novas); //Adiciona todas produçoes do tipo Dn -> Bn na gramática
        
        arq = new FileWriter(relatorio,true);
        gravarArq = new PrintWriter(arq);
        gravarArq.println("\n                 -Etapa 3-");
        arq.close();
        this.gramatica_chomsky.grava_gramatica();
        
        return this.gramatica_chomsky; //Retorna a gramática editada
    }
    
    /*Remove terminais e cria novas produçoes a partir de uma variável
    */ 
    public Producao converteN2(Producao producao_buffer){
        
        
        int tam1 = getGramatica_recebida().getTerminais().size();
        int tam2 = producao_buffer.getProducoes().size(); 
        
        for (int i = 0; i < tam1; i++ ){ // Percorre a lista de variáves
            for (int j = 0; j < tam2; j ++){       // e a lista de produçoes em busca de variáveis terminais
                if (getGramatica_recebida().getTerminais().get(i).equals(producao_buffer.getProducoes().get(j))){ // Se há um terminal dentro das derivaçoes de uma 
                    Producao producao_nova = new Producao(); // variável que serve de buffer para a nova producao do tipo C1 -> a
                    this.variavel_nova = "C"+(String) getGramatica_recebida().getTerminais().get(i); //// Adiciona uma nova variável com rótulo "C+terminal"
                    this.gramatica_chomsky.variaveis.add(this.variavel_nova); // Adiciona uma nova variável com rótulo "C+terminal" na lista de variaveis
                     
                    producao_buffer.getProducoes().set(j, variavel_nova); //substitui a produção do terminal pela variável nova
                    
                    producao_nova.setVariavel(variavel_nova); // Adiciona a nova variavel na producao buffer
                    producao_nova.producoes.add((String) getGramatica_recebida().getTerminais().get(i)); // Adiciona o termnal como derivação da nova variavel
                    
                    this.producoes_novas.add(producao_nova); // Adiciona a nova regra de produção e derivação na gramática
                }
            }
        }
        return producao_buffer; 
    }
    
    public Producao converteN3 (Producao producao_buffer){
        
        int i = 1; // i começa em 1 que é o índice do segundo termo da lista de derivaçoes
       while (producao_buffer.getProducoes().size() > 2){// Enquanto não estiver com duas variáveis, repete o láco
           Producao  producao_auxiliar = new Producao(); //cria uma producao auxiliar para criar a nova 
           
           this.variavel_nova = "D" + Integer.toString(this.contador_variavel); //Adiciona no buffer
           this.contador_variavel ++; //incrementa a variável de rótulos; 
           
           this.gramatica_chomsky.getVariaveis().add(this.variavel_nova);// Adiciona a nova variável "D+n" na lsita de variáveis 
           
           producao_auxiliar.setVariavel(variavel_nova); //coloca a nova variável na prod auxiliar
           producao_auxiliar.getProducoes().add(producao_buffer.getProducoes().get(i)); //Adiciona a segunda derivação na lista de produçoes nova
           producao_auxiliar.getProducoes().add(producao_buffer.getProducoes().get(i + 1)); // Adiciona a terceira derivação na lista de producóes nova
           
           
           this.producoes_novas.add(producao_auxiliar); 
           
           producao_buffer.getProducoes().remove(i);// Remove a segunda derivação da lista
           producao_buffer.getProducoes().remove(i); //Remove a terceira
           
           producao_buffer.getProducoes().add(variavel_nova); //Adiciona a nova produção no buffer
             
       } 
       return producao_buffer; //retorna o buffer atualizado com derivaçoes tamanho n = 2; 
           
        }
}
    

