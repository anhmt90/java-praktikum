/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package S1_Zeichenkunst;

/**
 *
 * @author TuanAnh
 */
public class Test {
    private static int b;
    private static int h;
    private static char[][] matrix;

    public static void main(String[] args) {
        init(4);
        fülleMatrix();
        print();
    }

    private static void print() {
        System.out.println();
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < b; j++) {
                System.out.print(matrix[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }

    // Ihre weiteren Methoden hier
    // befülle die Matrix mit Sternen

    private static void fülleMatrix() {
        // befülle zeilenweise
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < 2*i+1; j++) {
                matrix[i][h-i+j]='*';
            }
        }
    }

    private static void init(int h) {
        // initialisiere Klassenvariablen h, b, matrix
        Test.h = h;
        b = 2 * h + 1;
        matrix = new char[h][b];
    }

}
