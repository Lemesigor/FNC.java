
package trabalhoformais;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Scanner;

public class TrabalhoFormais {

    public static void main(String[] args) throws IOException {
        
        String continuar = "S";
        do {

            String inicial = new String();

            Gramatica gramatica = new Gramatica();

            Simplifica simple = new Simplifica(gramatica);
            ConfiguraGramatica arquivo = new ConfiguraGramatica(gramatica);
            Chomsky gramaticac = new Chomsky(gramatica);
            arquivo.setNome_arquivo();
            Boolean abriu = arquivo.abrir();
            CYK cyk = new CYK(gramatica); //     CYK
            String relatorio = "relatoriofinal.txt";

            //Laço para abertura do arquivo
            if (!abriu) {
                while (!abriu) {
                    arquivo.setNome_arquivo();
                    abriu = arquivo.abrir();
                }
            }
            arquivo.editor_arquivo();

            //Módulos independentes para fazer as simplificaçoes a partir da gramática inicial
            Gramatica gramatica_vazia = gramatica;
            Gramatica gramatica_unitaria = gramatica;
            Gramatica gramatica_inuteis = gramatica;
            Gramatica gramatica_fnc = gramatica;

            /* Os algoritmos abaixo sempre realizam as mesmas etapas:
        1: cria um objeto para abertura e escrita de aquivo
        2: coloca o título da seção e fecha o arquivo
        3: executa o método referente ao algoritmo (simpl, chomsky, cyk, ...)
        4: ordena as produçoes
        5: grava a "nova" gramática no arquivo
             */
            FileWriter arq = new FileWriter(relatorio, false);
            PrintWriter gravarArq = new PrintWriter(arq);
            gravarArq.println("##################    RELATORIO FINAL ####################\n\n");
            gravarArq.println("\n");
            gravarArq.println("\n                 -ORIGINAL-");
            arq.close();
            Collections.sort(gramatica.getProducoes(), Gramatica.StuNameComparator);
            gramatica.grava_gramatica();

            arq = new FileWriter(relatorio, true);
            gravarArq = new PrintWriter(arq);
            gravarArq.println("\n              -SEM PROD. VAZIAS-");
            arq.close();
            simple.prodVazias();

            Simplifica simples_vazio = new Simplifica(gramatica_vazia);
            simples_vazio.prodVazias();
            Collections.sort(gramatica_vazia.getProducoes(), Gramatica.StuNameComparator);
            gramatica_vazia.grava_gramatica();

            ///
            arq = new FileWriter(relatorio, true);
            gravarArq = new PrintWriter(arq);
            gravarArq.println("\n              -SEM PROD. UNITARIAS-");
            arq.close();
            simple.prodSubstVar();

            Simplifica simples_unitario = new Simplifica(gramatica_unitaria);
            Collections.sort(gramatica_unitaria.getProducoes(), Gramatica.StuNameComparator);
            gramatica_unitaria.grava_gramatica();

            //
            arq = new FileWriter(relatorio, true);
            gravarArq = new PrintWriter(arq);
            gravarArq.println("\n              -SEM SIMBOLOS INUTEIS-");
            arq.close();
            simple.simbInuteis();

            Simplifica simples_inuteis = new Simplifica(gramatica_inuteis);
            Collections.sort(gramatica_inuteis.getProducoes(), Gramatica.StuNameComparator);
            gramatica_inuteis.grava_gramatica();

            /////
            arq = new FileWriter(relatorio, true);
            gravarArq = new PrintWriter(arq);
            gravarArq.println("\n              -Gramatica com as três simplificações aplicadas na ordem-");
            arq.close();
            Collections.sort(gramatica.getProducoes(), Gramatica.StuNameComparator);
            gramatica.grava_gramatica();

            ////
            arq = new FileWriter(relatorio, true);
            gravarArq = new PrintWriter(arq);
            gravarArq.println("\n                    -FNC-");
            arq.close();
            gramaticac.removeVazios();
            gramatica_fnc = gramaticac.converteGramatica();
            Collections.sort(gramatica_fnc.getProducoes(), Gramatica.StuNameComparator);
            arq = new FileWriter(relatorio, true);
            gravarArq = new PrintWriter(arq);
            gravarArq.println("\n                    -FNC Final-");
            arq.close();
            gramatica_fnc.grava_gramatica();

            //Aplica o algoritmo cyk sobre a gramática na FNC
            cyk.Algoritmo();
            
            // Pergunta se o querido professor deseja abrir outros arquivos :)
            Scanner ler = new Scanner(System.in);
            System.out.println("Deseja abrir outro arquivo/gramatica? (S/N)");
            continuar = ler.nextLine().toUpperCase();
        } while (continuar.matches("S")); //Repete o laço enquanto quiser, amiguinho
        System.out.println("relatoriofinal.txt gerado com sucesso :) Até a próxima!");
    }
}
    
    

