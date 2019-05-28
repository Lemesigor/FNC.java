package trabalhoformais;

import java.io.FileWriter;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.IOException;
import java.io.PrintWriter;
public class CYK {
    public Gramatica gramatica; // Objeto gramática onde é guardado a gramática editada 
    private Boolean aceita = false; // Booleano para ao final do cyk saber se aceita ou rejeita a gramática
    private String relatorio = "relatoriofinal.txt";
    private BinaryTree arvore = new BinaryTree(); //Pega a inicial e já coloca como raiz da possível árvore 
    private ArrayList <String>[][] matriz_buffer = new ArrayList[100][100];
    public CYK(Gramatica gramatica) {
        this.gramatica = gramatica;
    }
    
    
    
    public void Algoritmo() throws IOException{
        String continuar;
        do{
        String word;    
        int n;          //quantos terminais tem a sentenca
        int i,j,k,l,x,y;//indices
        int ini=0, fim=1;
        int tam_maior = 1; // variavel que guarda o tamanho do maior terminal possível
        int contador; //variável usada para saber em qual nível está
        
        Scanner ler = new Scanner(System.in);
        System.out.printf("Palavra a ser reconhecida:\n");
        word = ler.nextLine(); // lê do teclado uma sentenca a ser reconhecida (como string de nome word)
        
        ArrayList <String> sentenca = new ArrayList(); // a sentenca deve ser uma lista de strings
        
        word = word.replaceAll(" ", "");//tira os espacos da string recebida
        
        do{ //por causa da possibilidade de terminais com mais de um char, é necessario "quebrar" a entrada em strings                                                                    
                if(gramatica.getTerminais().contains(word.substring(ini, fim))){//testa substrings de "word" se sao terminais da gramatica
                    sentenca.add(word.substring(ini, fim));                     //se for um terminal, essa string e colocada na lista de strings: sentenca 
                    ini=fim;                                                    
                    fim++;                                                      //testa substring de comp 1, se nao for coloca mais um char e testa, se nao for ...
                }                                                               //ini e fim servem para delimitar a substring
                else
                    fim++;
        }while(fim <= word.length());                                           
        
        n = sentenca.size(); //QUANTOS TERMINAIS A SENTENCA TEM
        
        ArrayList <String>[][] matriz = new ArrayList[n][n];  //A matriz do CYK é definida como um array bidimenssional de listas de strings 
        
        
        //PREENCHE A PRIMEIRA LINHA DA MATRIZ
        for(i=0;i < n;i++){                                                                   //percorre as palavras da sentenca
            ArrayList <String> buffer = new ArrayList();
            for(j=0;j < gramatica.getProducoes().size();j++){                                 // Percorre as producoes                
                if(gramatica.getProducoes().get(j).getProducoes().contains(sentenca.get(i)) ) //se uma producao contem a palavra 
                    buffer.add(gramatica.getProducoes().get(j).getVariavel());                //a variavel dessa prod é colocada numa lista buffer
            }
            matriz[0][i] = buffer;                                                            //depois de ver todas as prod. o buffer é colocado na matriz
        }
       
        for(i=1;i < n;i++){        //linha (comeca em 1 pq a primeira ja foi preenchida)
            for(j=0;j < n-i;j++){  //coluna (a cada nova linha tem menos colunas)
                ArrayList <String> buffer = new ArrayList();
                for(k=0;k < i;k++){
                    if(!matriz[k][j].isEmpty() & !matriz[i-1-k][j+1+k].isEmpty()){
                        for(l=0;l < matriz[k][j].size();l++){                       //esses dois lacos fazem o produto por distribuicao dos 
                            for(x=0;x < matriz[i-1-k][j+1+k].size();x++){           //dois "quadrdos" da matriz sendo processados
                                String buff1 = new String();
                                buff1 = buff1.concat(matriz[k][j].get(l));          //o produto é colocado em uma string (concatena um pedaco de cada)
                                buff1 = buff1.concat(matriz[i-1-k][j+1+k].get(x));
                                for(y=0;y < gramatica.getProducoes().size();y++){
                                    String buff2;                                   //o lado direito de cada prod tambem é colocado numa string
                                    buff2 = gramatica.getProducoes().get(y).getProducoes().toString(); 
                                    buff2 = buff2.replaceAll("\\[", "");
                                    buff2 = buff2.replaceAll("\\]", "");//ao transformar lista em string aparecem esses caracteres que precisam ser retirados
                                    buff2 = buff2.replaceAll(", ", "");
                                    if(buff1.matches(buff2) & !buffer.contains(gramatica.getProducoes().get(y).getVariavel()))                               
                                        buffer.add(gramatica.getProducoes().get(y).getVariavel()); //se o produto é uma das producoes de alguma variavel
                                }                                                                  //essa variavel e colocada no buffer (se ja nao esta)
                            }
                        }
                    }
                }
                 matriz[i][j] = buffer; 
            }
        } 
       /* IMPRIME A TABALA CYK
        COM AS PRODUÇOES
      
      /* Teste inicial para detectar
       parcialmente o maior tamanho possível que uma célula pode ter
       */
       String teste_maior1;
       for(i=0;i < n;i++){
            for(j=0;j < n-i;j++){
                teste_maior1 = matriz[i][j].toString();
                teste_maior1 = teste_maior1.replaceAll("\\[", "");
                teste_maior1 = teste_maior1.replaceAll("\\]", "");
                teste_maior1 = teste_maior1.replaceAll(", ", " ");
                if(teste_maior1.length() > tam_maior){
                    tam_maior = teste_maior1.length();
                }
            }
       }
       
            
       
        
        FileWriter arq = new FileWriter(relatorio, true);
        PrintWriter gravarArq = new PrintWriter(arq);

        gravarArq.println("                   *****TABELA CYK*******\n");
        String teste_maior2;
        contador = 0;
        for (i = n - 1; i >= 0; i--) { //percorre da última linha da matriz para cima
            for (x = 0; x <= contador; x++) { // percorre as colunas da matriz
                gravarArq.print("|| ");
                if (matriz[i][x].isEmpty()) {
                    gravarArq.print("*");
                    for (j = 0; j < tam_maior; j++) //imprime os espaços restantes para ficar formatado
                    {
                        gravarArq.print(" ");
                    }
                } 
                else {
                    for (k = 0; k < matriz[i][x].size(); k++) //vê as variáveis dentro da célula da matriz
                    {
                        gravarArq.print(matriz[i][x].get(k) + " ");
                    }

                    teste_maior2 = matriz[i][x].toString();
                    teste_maior2 = teste_maior2.replaceAll("\\[", "");
                    teste_maior2 = teste_maior2.replaceAll("\\]", "");
                    teste_maior2 = teste_maior2.replaceAll(", ", " ");
                    //System.out.println("#"+teste2+"#");

                    if (teste_maior2.length() < tam_maior) { // se o total de produçoes for menor do que o maior número possível em uma célula
                        for (j = teste_maior2.length(); j < tam_maior; j++) //imprime a diferença de tamanho em espaços para ficar bem formatado
                        {
                            gravarArq.print(" ");
                        }
                    }
                }
            }
            gravarArq.print("||");
            contador++; //incrementa contador porque no próximo laço vai aumentaro  número de células 
            gravarArq.println("\n");
        }
       
         //Grava a última linha com a sentença
        gravarArq.println("\n");
        for (k = 0; k < sentenca.size(); k++) {
            gravarArq.print("|| ");
            gravarArq.print(sentenca.get(k) + " ");
      
            teste_maior2 = sentenca.get(k);
            teste_maior2 = teste_maior2.replaceAll("\\[", "");
            teste_maior2 = teste_maior2.replaceAll("\\]", "");
            teste_maior2 = teste_maior2.replaceAll(", ", " ");
 
            if (teste_maior2.length() < tam_maior) { // se o total de produçoes for menor do que o maior número possível em uma célula
              for (j = teste_maior2.length(); j < tam_maior; j++) //imprime a diferença de tamanho em espaços para ficar bem formatado
                  gravarArq.print(" ");   
            }
        }
        gravarArq.print("||");
        arq.close();
        
// VÊ SE A SENTENÇÃO FAZ PARTE DA LINGUAGEM 
       for (k=0; k < matriz[n - 1][0].size();k++)
            if(matriz[n - 1][0].get(k).equals(this.gramatica.getInicial())){
                    //Se achou inicial no topo, aceita, adiciona na raiz da struct arvore
                    this.aceita = true; 
                    //this.arvore.add(10000,this.gramatica.getInicial());
                    this.matriz_buffer = matriz;
                    ArrayList <String> raizes = new ArrayList();
                    raizes.add(matriz[n - 1][0].get(k)); //Cria um array apenas com S para fazer o primeiro laço
                    this.arvore.root = new Node(this.gramatica.getInicial()); // cria o nodo inicial da árvore
                    this.arvore.root =  montaArvore(0,n - 1 ,raizes,this.arvore.root);
                  
            }
       
        if(aceita){
            arq = new FileWriter(this.relatorio,true);
            gravarArq = new PrintWriter(arq);
            gravarArq.println("\n");
            gravarArq.println("\n");
            gravarArq.println("            A sentença foi ACEITA");
            gravarArq.println("");
            System.out.println("A sentença foi ACEITA");
            
            gravarArq.println("          - Arvore de Derivaçao -");
            gravarArq.println("");
            arq.close();
            this.arvore.print2D(this.arvore.root);
        }
        else{
            gravarArq = new PrintWriter(arq);
            gravarArq.println("\n");
            gravarArq.println("\n");
            gravarArq.println("            A sentença foi RECUSADA");
            gravarArq.println("");
            System.out.println("A sentença foi RECUSADA");
        }
        ler = new Scanner(System.in);
            System.out.println("Testar outra sentenca? (S/N)");
            continuar = ler.nextLine().toUpperCase();
        } while (continuar.matches("S")); //Repete o laço enquanto quiser, amiguinho
        
    }
        
    /*O algoritmo é responsável por gerar a árvore de derivação da tabela
    Ele recebe um nodo de uma estrutura árvore e vai refazendo o percurso do algoritmo CYK. Cada vez que ele encontra uma subarvore
    ele passa o primeiro elemento da sua derivação para o nodo esquerdo do nodo atual e o segundo para o direito. 
    Em seguida faz a mesma coisa, mas recebendo como parâmatros a posição na matriz do elemento de índice maior do que 0 para encontrar a próxima
    subárvore e assim ir montando recursivamente. 
    Os argumentos col e lin são respectivamente a posição do elemento atual na matriz
    Raizes serve como buffer dos elementos que estão na célula e que servirão para testar as suas derivações
    Node é o nodo da árvore que vai recursivamente sendo preenchido para montar a árvorezinha
    
    */
    
    private Node montaArvore(int col, int lin,ArrayList <String> raizes, Node atual){
        
        // TAM_MATRIZ RECEBE O INDICE ATUAL DA LINHA
        
       // for(i=1;i < n;i++){        //linha (comeca em 1 pq a primeira ja foi preenchida)
           // for(int j=0;j < 1;j++){  //coluna (a cada nova linha tem menos colunas)
        ArrayList<String> buffer = new ArrayList();
        int j = col;
        int k = 0, i = 0, x = 0, y = 0, z=0;
        Boolean achou = false;
        String variaveis_nodo[];
   
        /* k percorre a as linhas da matriz
        j a coluna atual
        i percorre a primeira célula que será comparada com a célula do index x
        y percorre as produçoes da gramática                                                                                                                                           
        z percorre o arraylist da célula que está servido de raiz na recursão atual
        achou faz sair de todos os laços a retornar as raízes da recursão*/
        
        while (!achou && k < lin) { //
            i = 0;
            if (!this.matriz_buffer[k][j].isEmpty() & !this.matriz_buffer[lin - 1 - k][j + 1 + k].isEmpty()) { //se as duas células não estão vazias
                while (!achou && i < this.matriz_buffer[k][j].size()) {
                    x = 0;   // zera auxiliar
                    while (!achou && x < this.matriz_buffer[lin - 1 - k][j + 1 + k].size()) {           //dois "quadrdos" da matriz sendo processados
                        String buff1 = new String();
                        buff1 = buff1.concat(this.matriz_buffer[k][j].get(i));          //o produto é colocado em uma string (concatena um pedaco de cada)
                        buff1 = buff1.concat(this.matriz_buffer[lin - 1 - k][j + 1 + k].get(x));
                        y = 0;  //zera auxiliar
                        while (!achou && y < gramatica.getProducoes().size()) {
                            String buff2;                                   //o lado direito de cada prod tambem é colocado numa string
                            z=0;
                            while (!achou && z < raizes.size()) { //Controla a quantidade de "possíveis raízes" da subárvore sendo testada no momento
                                if (this.gramatica.getProducoes().get(y).getVariavel().matches(raizes.get(z))) {//pega as produçoes da raiz que está sendo analisada
                                    buff2 = gramatica.getProducoes().get(y).getProducoes().toString();
                                    buff2 = buff2.replaceAll("\\[", "");
                                    buff2 = buff2.replaceAll("\\]", "");//ao transformar lista em string aparecem esses caracteres que precisam ser retirados
                                    
                                    variaveis_nodo = buff2.split(", "); // Gera a esquerda 0 e direita do nodos
                                    buff2 = buff2.replaceAll(", ", "");
                                    
                                    if (buff1.matches(buff2)) { //Se a produção da raiz é igual à concatenação de duas células, é uma sub árvore
                                        atual.left = new Node (variaveis_nodo[0]);   //Coloca o nodo da esqueda 
                                        atual.right = new Node (variaveis_nodo[1]); 

                                        if (k != 0) //Se o atual filho esquedo não for já uma folha, passa ele como subárvore para achar seus filhos
                                           montaArvore(j, k, this.matriz_buffer[k][j],atual.left);
                                        
                                        if (lin - 1 - k != 0)  //Se o atual filho da direita não for já uma folha, passa ele como subárvore para achar seus filhos
                                            montaArvore(j + 1 + k, lin - 1 - k, this.matriz_buffer[lin-1-k][j+1+k],atual.right);
                                        
                                        achou = true; // Quando chegar aqui, é que já fez todas recursos que precisa e pode sair do laço 
                                    }
                                }

                                z++;
                            }
                            y++;
                        }
                        x++;
                    }
                    i++;
                }

            }
            // }
            k++;
        }
    return atual; //sempre vai retornando o nodo atual para encadear as recursões da árvore
    }

}
        
  
    
    

    
    
    
    

