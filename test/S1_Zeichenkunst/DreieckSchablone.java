package S1_Zeichenkunst;

/**
 *
 * @author Anne Brüggemann-Klein
 */

public class DreieckSchablone {
    private static int b;
    private static int h;
    private static char[][] matrix;
    public static void main(String[] args) {
      init(4);
      // Ihre Methodenaufrufe hier
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
    private static void init(int h) {
        // initialisiere Klassenvariablen h, b, matrix
        DreieckSchablone.h = h;
        b = 2*h-1;
        matrix = new char[h][b];
    }     
        
}


