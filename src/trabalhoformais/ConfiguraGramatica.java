
package trabalhoformais;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.ArrayList;


public class ConfiguraGramatica {
    private String nome_arquivo;  //Variável que guarda o nome do arquivo txt
    private String linha;  //String que serve de buffer para a linha lida do arquivo 
    private FileReader arq; 
    private BufferedReader lerArq;
    public Gramatica gramatica; // Objeto gramática onde é guardado a gramática editada
    

    public ConfiguraGramatica(Gramatica gramatica) {
        this.gramatica = gramatica;
    }

    
    public String getNome_arquivo() {
        return nome_arquivo;
    }

    public void setNome_arquivo() {
        Scanner ler = new Scanner(System.in);
        System.out.printf("Informe o nome de arquivo texto:\n");
        this.nome_arquivo = ler.nextLine();
    }

    public String getLinha() {
        return linha;
    }

    public void setLinha(String linha) {
        this.linha = linha;
    }
           
    public FileReader getArq() {
        return arq;
    }

    public void setArq(FileReader arq) {
        this.arq = arq;
    }

    public BufferedReader getLerArq() {
        return lerArq;
    }

    public void setLerArq(BufferedReader lerArq) {
        this.lerArq = lerArq;
    }

    public Gramatica getGramatica() {
        return gramatica;
    }

    public void setGramatica(Gramatica gramatica) {
        this.gramatica = gramatica;
    }


    //  MÉTODOS
    
    public Boolean abrir() {    //Função que testa se o nome informado está correto
        try {
            setArq(new FileReader(nome_arquivo));
            setLerArq(new BufferedReader(arq));
            System.out.println("Arquivo aberto com sucesso");
            return true;
        } 
        catch (IOException e) {
            System.err.printf("Erro na abertura do arquivo: %s.\n",
                    e.getMessage());
            return false; 
        }

    }
    
      public void exibir() throws IOException { // Método auxiliar para exibir o arquivo de texto lido
          
            setLinha (getLerArq().readLine());
        while (getLinha() != null) {
            System.out.printf("%s\n", getLinha());

            setLinha (getLerArq().readLine());  // lê da segunda até a última linha
        }

        this.arq.close();
    }
      
  
   
      public void editor_arquivo() throws IOException {
        Boolean variavel, inicial, terminal, regra;
        
        

        setLinha (getLerArq().readLine());  //Pega a primeira linha do arquivo
        while (getLinha() != null) {     //Laço para percorrer todo o arquivo
           
            //Testes para ir colocando as strings nas suas respectivas estruturas da classe Gramatica
            if (getLinha().matches("(.*)#Terminais(.*)"))  
                edita_terminais();
            
            if (getLinha().matches("(.*)#Variaveis(.*)"))
                edita_variaveis();
            
            if(getLinha().matches("(.*)#Inicial(.*)"))
                edita_inicial();
            
            if(getLinha().matches("(.*)#Regras(.*)")){
                edita_producoes();
            }
                
           
            setLinha (getLerArq().readLine());  
           
        }

        this.arq.close();
    }

    private void edita_producoes() throws IOException{
        String[] strBuffer, strBuffer2; // Array que será usado como buffer 
        int n = 0; 
          
        setLinha (getLerArq().readLine()); // Avança para a próxima linha  
        while (getLinha() != null  ) { // Laço até achar ono fim das produçoes
            Producao buffer_producao = new Producao (); // Buffer para salvar uma producao que depoi será alocada em uma lista zerado a cada laço
            setLinha(getLinha().replaceAll(" ", "")); //Remove espaços
            setLinha(getLinha().replaceAll("\t", "")); // Remove tabs
            setLinha(getLinha().split("#")[0]); // Remove os comentários 
            
            strBuffer = getLinha().split(">"); // Divide a string na variavel e suas produçoes
            buffer_producao.setVariavel(strBuffer[0].replaceAll("\\[", "").replaceAll("\\]","")); //Define a inicial da produção sem os []
            
            strBuffer[1] = strBuffer[1].replaceAll("\\[", ""); // Remove todos [ das produçoes
            strBuffer2 = strBuffer[1].split("\\]"); // Vai separar todas as producoes sem os cochetes 
            for(int i = 0; i < strBuffer2.length; i++) //Laço que vai formar a lista de produçoes do buffer a partir do array
                buffer_producao.getProducoes().add(strBuffer2[i]); 
            
            
            gramatica.getProducoes().add(buffer_producao); //adiciona a produção na lista da producoes da gramatica
            
            setLinha (getLerArq().readLine()); // Avança para a próxima linha 
        }
    }  
    private void edita_terminais() throws IOException{
        String[] strBuffer; // Array que será usado como buffer 
        int n = 0; 
          
        setLinha (getLerArq().readLine()); // Avança para a próxima linha  
        while (!(getLinha().matches("(.*)#Variaveis(.*)")) && getLinha() != null  ) { // Laço até achar o começo de Variaveis ou acabar o arquivo
              strBuffer = getLinha().split("]"); // Separa a linha para ficar apenas com o terminal
              strBuffer[0] = strBuffer[0].replaceAll("\\s", ""); // Apaga espaço
              strBuffer[0] = strBuffer[0].replaceAll("\\[", ""); // Apaga colchete inicial 
              
              gramatica.getTerminais().add(strBuffer[0]);
              n++;
              setLinha (getLerArq().readLine()); // Avança para a próxima linha
            
            }

           
        }
    
    private void edita_variaveis () throws IOException{
        String[] strBuffer; // Array usado como buffer do split
        int i = 0;
        
        setLinha (getLerArq().readLine()); // Avança para a próxima linha 
        while (!(getLinha().matches("(.*)#Inicial(.*)")) && getLinha() != null){
            strBuffer = getLinha().split("]"); // Separa a linha para ficar apenas com o terminal
            strBuffer[0] = strBuffer[0].replaceAll("\\s", ""); // Apaga espaço
            strBuffer[0] = strBuffer[0].replaceAll("\\[", ""); // Apaga colchete inicial
            
            gramatica.getVariaveis().add(strBuffer[0]); // A parte importante fica no 0 do split
            i++;
            setLinha (getLerArq().readLine()); // Avança para a próxima linha
            
            
        }
    }    
        
    private void edita_inicial () throws IOException{
        String [] strBuffer;
        
        setLinha (getLerArq().readLine()); // Avança para a próxima linha 
        strBuffer = getLinha().split("]"); // Separa a linha para ficar apenas com o terminal
        strBuffer[0] = strBuffer[0].replaceAll("\\s", ""); // Apaga espaço
        strBuffer[0] = strBuffer[0].replaceAll("\\[", ""); // Apaga colchete inicial
        gramatica.setInicial(strBuffer[0]);
        setLinha (getLerArq().readLine()); // Avança para a próxima linha
    }
    
    
        
}
            
