/*Adaptado de https://www.geeksforgeeks.org/binary-tree-set-1-introduction/ 
http://www.baeldung.com/java-binary-tree
https://www.geeksforgeeks.org/print-binary-tree-2-dimensions/
em 13/06/18
São basicamente métodos e estrututuras para manipulação de árvores adaptados para o escopo necessário para o trabalho*/
package trabalhoformais;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Queue;

class Node {

    String variavel;
    Node left, right;

    public Node(String variavel) {

        this.variavel = variavel;
        this.left = null;
        this.right = null;
    }

}

public class BinaryTree {

    // Root of Binary Tree
    Node root;
    private String relatorio = "relatoriofinal.txt"; 

    // Constructors
    BinaryTree(String variavel) {
        this.root = new Node(variavel);
    }

    BinaryTree() {
        this.root = null;
    }

    /*private Node addRecursive(Node current, int rotulo, String variavel) {
        if (current == null) {
            return new Node(rotulo, variavel);
        }

        if (rotulo < current.rotulo) {
            current.left = addRecursive(current.left, rotulo, variavel);
        } else if (rotulo > current.rotulo) {
            current.right = addRecursive(current.right, rotulo, variavel);
        } else {
            // value already exists
            return current;
        }

        return current;
    }

    public void add(int rotulo, String variavel) {
        root = addRecursive(root, rotulo, variavel);
    }*/
    public void traverseInOrder(Node node) {
        if (node != null) {
            traverseInOrder(node.left);
            System.out.print(" " + node.variavel);
            traverseInOrder(node.right);
        }
    }
    // Para imprimir por nível
    
    
    public void traverseLevelOrder() {
        if (root == null) {
            return;
        }

        Queue<Node> nodes = new LinkedList<>();
        nodes.add(root);

        while (!nodes.isEmpty()) {
            System.out.println(" ");
            Node node = nodes.remove();

            System.out.print("    " + node.variavel);

            if (node.left != null) {
                nodes.add(node.left);
            }

            if (node.right != null) {
                nodes.add(node.right);
            }

        }
    }
//Para imprimir no formato de árvore
    void print2DUtil(Node root, int space) throws IOException {
        int count = 10;
        // Base case
        if (root == null) {
            return;
        }

        // Increase distance between levels
        space += count;

        // Process right child first
        print2DUtil(root.right, space);

        // Print current node after space
        // count
        FileWriter arq = new FileWriter(this.relatorio,true);
        PrintWriter gravarArq = new PrintWriter(arq);
        
        gravarArq.println("");
        arq.close();
        for (int i = count; i < space; i++) {
           arq = new FileWriter(this.relatorio,true);
           gravarArq = new PrintWriter(arq);
            gravarArq.print(" ");
            arq.close();
        }
        arq = new FileWriter(this.relatorio,true);
        gravarArq = new PrintWriter(arq);
        gravarArq.println("-- " + root.variavel);
        arq.close();
        // Process left child
        print2DUtil(root.left, space);
    
    }

// Wrapper over print2DUtil()
    void print2D(Node root) throws IOException {
        // Pass initial space count as 0
        print2DUtil(root, 0);
    }
}
