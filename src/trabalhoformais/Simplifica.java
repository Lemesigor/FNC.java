
package trabalhoformais;
import java.util.ArrayList;

public class Simplifica {
    
    public Gramatica gramatica; // Objeto gramática onde é guardado a gramática editada
    

    public Simplifica(Gramatica gramatica) {
        this.gramatica = gramatica;
    }


    public Gramatica getGramatica() {
        return gramatica;
    }

    public void setGramatica(Gramatica gramatica) {
        this.gramatica = gramatica;
    }
    
    
     public void prodVazias(){
        int i, k, j;
         ArrayList <String> var1 = new ArrayList(); 
         ArrayList <Producao> prod1 = new ArrayList();
        
        //#####  PRIMEIRA ETAPA  #####
       
        //CRIA o grupo de variaveis que produzem vazio diretamente
        for(i=0; i < gramatica.getProducoes().size(); i++){                      
            if( (gramatica.getProducoes().get(i).getProducoes().contains("V"))
                    && !(var1.contains(gramatica.getProducoes().get(i).getVariavel())) ) //se uma variave produz vazio diretamente e nao esta no grupo, adiciona
                var1.add(gramatica.getProducoes().get(i).getVariavel());
        } 
        
         //Completa o grupo com as variaveis que produzem vazio indiretamente
        for (i=0; i < gramatica.getProducoes().size(); i++)                      
            for (j=0; j < var1.size(); j++)
                if( (gramatica.getProducoes().get(i).getProducoes().contains(var1.get(j)))   //se a variave produz uma das variaveis que produzem vazio 
                        && !(var1.contains(gramatica.getProducoes().get(i).getVariavel()))){ //e nao esta no grupo, adiciona
                    var1.add(gramatica.getProducoes().get(i).getVariavel());
                    i = 0;//toda a vez que atualizar o var1, recomeça TODA a iteração 
                }
               
        //#####  SEGUNDA ETAPA #####
        
        //Cria o grupo P1 com as producoes que nao geram vazio
        for(i=0; i < gramatica.getProducoes().size(); i++){                      
            if( !(gramatica.getProducoes().get(i).getProducoes().contains("V")) //Se a producao nao pruduz vazio, é adicionada a prod1
                    && !(prod1.contains(gramatica.getProducoes().get(i))) )
                prod1.add(gramatica.getProducoes().get(i));
        }
           
        //Simula as prod. vazias em forma de novas producoes
        for (i=0; i < var1.size(); i++){       //i percorre as variaveis que produzem vazio
            for (j=0; j < prod1.size(); j++){  //j percorre as producoes que nao geram vazio diretamente
                if( (prod1.get(j).getProducoes().contains(var1.get(i)))
                        && (prod1.get(j).getProducoes().size() > 1) ){ //se a producao contem a variavel i e nao é unitaria:
                    Producao buffer_producao = new Producao ();
                    ArrayList <String> bufferList = new ArrayList();
                    bufferList.addAll(prod1.get(j).getProducoes());     //é criada uma nova producao igual a j, porem sem a var. i
                    
                    buffer_producao.setProducoes(bufferList);
                    buffer_producao.setVariavel(prod1.get(j).getVariavel());
                    buffer_producao.getProducoes().remove(var1.get(i));
                    if(!(prod1.contains(buffer_producao))){             //se essa prod. ja nao foi colocada em prod1, entao a adiciona a prod1
                        prod1.add(buffer_producao);
                    }
                }
                    
            }
        }
        
        //######  TERCEIRA ETAPA ###### NAO É NECESSARIO
        
        //Adiciona a palavra vazia se necessario
        if(var1.contains("S")){
            Producao buffer_producao = new Producao ();
            ArrayList <String> bufferList = new ArrayList();
            bufferList.add("V");
            
            buffer_producao.setProducoes(bufferList);
            buffer_producao.setVariavel("S");
            
            prod1.add(buffer_producao);
        }
            
        //substitui o conj. de prod. original pelo novo (prod1)
        gramatica.setProducoes(prod1);
        
    }              
         
     
     public void prodSubstVar (){
         int i, j,k;         
         ArrayList <Producao> fechos = new ArrayList();
         
        
         //cria os fechos da variaveis (fecho é uma producao em que a var é o rotulo e as prod. sao o conteudo)
         for(i=0;i < gramatica.getVariaveis().size();i++){
             String buffer ;
             Producao buffer_producao = new Producao ();
             buffer = gramatica.getVariaveis().get(i).toString();
             buffer_producao.setVariavel(buffer);
             
             for(j=0;j < gramatica.getProducoes().size(); j++){
                 if( (gramatica.getProducoes().get(j).getProducoes().size() == 1)                       //se a prod. e unitaria
                         && !(gramatica.getTerminais().contains(gramatica.getProducoes().get(j).getProducoes().get(0))) //se a prod. nao e um terminal
                         && (gramatica.getProducoes().get(j).getVariavel().matches(buffer))           //se é a var do fecho que esta gerando essa prod   
                         && !(gramatica.getProducoes().get(j).getProducoes().contains("V")) ){        //se nao e vazio
                     buffer_producao.getProducoes().addAll(gramatica.getProducoes().get(j).getProducoes());
                 }                
             }
             fechos.add(buffer_producao);
         }
         
         //tira as producoes unitarias da gramatica
         for(i=0;i < gramatica.getProducoes().size(); i++){
             if ((gramatica.getProducoes().get(i).getProducoes().size() == 1)                       //se a prod. e unitaria
                    && !(gramatica.getTerminais().contains(gramatica.getProducoes().get(i).getProducoes().get(0)))
                    &&  !(gramatica.getProducoes().get(i).getProducoes().contains("V"))){ //e nao é terminal
                 gramatica.getProducoes().remove(i);
                 i=0;
               
             }
             
         }
         
         //cria as novas producoes q substituem as unitarias
         ArrayList <Producao> prodNovas = new ArrayList();
         for(i=0;i < fechos.size();i++){ //percorre os fechos das variaveis
             for(j=0;j < fechos.get(i).getProducoes().size();j++){ //percorre o conteudo dos fechos
                 for(k=0;k < gramatica.getProducoes().size(); k++){ //percorre as producoes
                     if(gramatica.getProducoes().get(k).getVariavel().matches(fechos.get(i).getProducoes().get(j))){//se a var atual do conteudo do fecho e a var da prod atual 
                         Producao buffer_producao = new Producao ();
                         buffer_producao.setVariavel(fechos.get(i).getVariavel()); //cria uma prod com o rotulo do fecho como variavel 
                         buffer_producao.setProducoes(gramatica.getProducoes().get(k).getProducoes());//e a prod do fecho como prod
                         prodNovas.add(buffer_producao); //todas as novas prod ficam numa lista sem alterar a lista de prod original
                     }
                         
                 }
                 
             }
         }
         gramatica.getProducoes().addAll(prodNovas); //as novas prod sao add ao grupo de prod original
          
     }
     
     
     public void simbInuteis(){ 
         ArrayList <String> var1 = new ArrayList();
         ArrayList <String> antiVar1 = new ArrayList();
         ArrayList <Producao> prod1 = new ArrayList();
         int i, j, k;
                  
         //cria grupo de variaveis que produzem terminais diretamente
         for(i=0;i < gramatica.getProducoes().size();i++){//percorre as producoes
             j=0;
             while( (j < gramatica.getProducoes().get(i).getProducoes().size())
                     && (!gramatica.getTerminais().contains(gramatica.getProducoes().get(i).getProducoes().get(j))) )//percorre o lado direito das producoes
                 j++; //encontra o indice que tem terminal
             
             if(j<= gramatica.getProducoes().get(i).getProducoes().size()-1)           //se j nao passou do tamanho              
                if( !(var1.contains(gramatica.getProducoes().get(i).getVariavel()))    //e a variavel nao esta no grupo ainda
                        & (gramatica.getTerminais().contains(gramatica.getProducoes().get(i).getProducoes().get(j))) )//e se realmente produz um terminal
                     var1.add(gramatica.getProducoes().get(i).getVariavel());         //coloca no grupo                                   
         }

         
         //coloca no grupo as variaveis que produzem terminais indiretamente
         for (i=0; i < gramatica.getProducoes().size(); i++)                      
            for (j=0; j < var1.size(); j++)
                if( (gramatica.getProducoes().get(i).getProducoes().contains(var1.get(j)))   //se a variave produz uma das variaveis que produzem terminais 
                        && !(var1.contains(gramatica.getProducoes().get(i).getVariavel()))){ //e nao esta no grupo, adiciona
                    var1.add(gramatica.getProducoes().get(i).getVariavel());
                    i = 0;//toda a vez que atualizar o var1, recomeça TODA a iteração 
                }
         
         //cria um grupo somente com as prod cujas variaveis(lado esquerdo) estao em var1
         for(i=0;i < gramatica.getProducoes().size();i++){
             if(var1.contains(gramatica.getProducoes().get(i).getVariavel()))
                 prod1.add(gramatica.getProducoes().get(i));
         }
         
         
        //cria um grupo de variaveis que nao sao geradoras
        for(i=0;i < gramatica.getVariaveis().size();i++){
            if(!var1.contains(gramatica.getVariaveis().get(i))){
                 antiVar1.add(gramatica.getVariaveis().get(i).toString());
            }               
        }
         
         //retira as producoes que tem do lado direito alguma var. nao geradora
         for(i=0;i < prod1.size();i++)
             for(j=0;j < antiVar1.size();j++)
                 if(prod1.get(i).getProducoes().contains(antiVar1.get(j))){
                     prod1.remove(prod1.get(i));
                     i=0;
                 }
             
         
        //tirar inalcancaveis
        ArrayList <String> var2 = new ArrayList();
        ArrayList <String> term = new ArrayList();
        ArrayList <Producao> prod2 = new ArrayList();

        boolean novas;
        var2.add("S");
        
        //cria duas listas, de variaveis e de terminais, que podem ser alcancados pela inicial 
        for(i=0;i < var2.size();i++){
            novas = false;
            for(j=0;j < prod1.size();j++){
                if(prod1.get(j).getVariavel().matches(var2.get(i)))
                    for(k=0;k < prod1.get(j).getProducoes().size();k++){
                        if(gramatica.getTerminais().contains(prod1.get(j).getProducoes().get(k))
                                && !(term.contains(prod1.get(j).getProducoes().get(k)))){
                            term.add(prod1.get(j).getProducoes().get(k));
                        }
                        if(gramatica.getVariaveis().contains(prod1.get(j).getProducoes().get(k))
                                && !(var2.contains(prod1.get(j).getProducoes().get(k)))){
                            var2.add(prod1.get(j).getProducoes().get(k));  
                            novas = true;
                        } 
                    }
            }
            if(novas){
                i=0;
            }
        }

        
        for(i=0; i < prod1.size();i++){
            if(var2.contains(prod1.get(i).getVariavel()))
                prod2.add(prod1.get(i));
        }
        
        gramatica.setProducoes(prod2);
        gramatica.setTerminais(term);
        gramatica.setVariaveis(var2);
     }
     
     
}                   
    

