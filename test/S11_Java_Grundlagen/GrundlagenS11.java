package S11_Java_Grundlagen;

/**
 *
 * @author Anne Brüggemann-Klein
 */
public class GrundlagenS11 {
    public static int fun() {
        boolean tt=true;
        while (tt) {
            @SuppressWarnings("UnusedAssignment")
            int[] a = {2,1};
            int[] b = {2};
            try {
                a = b;
                int c = a[1];  // Exception wird geworfen, aber gefangen
                tt = false;    // wird nie erreicht
            } catch (Exception e) {
                System.out.println(b[b[0]]);  // Exception ArrayOutOfBounds
                                              // Warum nicht deklariert ?
            }
        }
        return 42;
    }
    public static void main(String[] args) {
        fun();
    }
}
