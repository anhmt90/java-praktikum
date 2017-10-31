package S1_Zeichenkunst;

/**
 *
 * @author Anne Brüggemann-Klein
 */

public class Dreieck {
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
        for (int i=0; i<h; i++) {
            fülleZeile(i);
        }
    }
    // befülle Zeile i der Matrix mit Sternen
    private static void fülleZeile(int i) {
        // setze Sterne an Positionen h-1-i bis h-1+i
        for (int j=h-1-i; j<h+i; j++) {
            matrix[i][j]='*';
        }
    }
    private static void init(int h) {
        // initialisiere Klassenvariablen h, b, matrix
        Dreieck.h = h;
        b = 2*h-1;
        matrix = new char[h][b];
    }     
        
}


