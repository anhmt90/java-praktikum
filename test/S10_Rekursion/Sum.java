package S10_Rekursion;

/**
 *
 * @author Anne Brüggemann-Klein
 */

// Typische Klausuraufgaben, S. 10
public class Sum {
    // addiere die positiven Werte unter a[left],...a[a.length-1] zu 42
    public static int someSum(int[] a, int left) {
        int sum = 42;
        for (int i=left; i<a.length; i++) {
            if (a[i] > 0) {
                sum = sum + a[i];
            }
        }
        return sum;
    }
    // rekursive Fassung
    public static int someSumRec(int[] a, int left) {
        // Rekursionsanker: Was ist der einfache Fall, der direkt zu lösen ist?
        // left>=a.length
        // Rückgabe 42
        if (left >= a.length) {
            return 42;
        }
        // Rekursionsschritt: Wie können wir die Berechnung auf einen einfacheren
        // Fall zurückführen? Indem wir left um 1 erhöhen
        // Wie bekommen wir die Lösung für den aktuellen Fall? Indem wir zum
        // Ergebnis des einfacheren Falls a[length] addieren, falls der Wert
        // positiv ist.
        if (a[left]>0) {
            return a[left] + someSumRec(a,left+1);
        } else {
            return someSumRec(a,left+1);
        }
    }
    
    public static void main(String[] args) {
        int[] a = {1,2,3,4};
        System.out.println("erwarte 52: "+someSum(a,0));
        System.out.println("erwarte 52: "+someSumRec(a,0));
        System.out.println("erwarte 42: "+someSum(a,7));
        System.out.println("erwarte 42: "+someSumRec(a,7));
        System.out.println("erwarte 42: "+someSum(a,4));
        System.out.println("erwarte 42: "+someSumRec(a,4));
    }
}
