
/**
 *
 * @author Anne Brüggemann-Klein
 */
class Drucker {
    void drucke(String doc) {
        System.out.println("Aus dem Drucker kommt:\n"+doc);
    }
}

// Diese Lösung ist fragwürdig, denn ein Gerät ist kein Spezialfall
// von Drucker (eher umgekehrt).
// Außerdem ist Gerät jetzt in der Vererbungshierarchie festgelegt.
abstract class Gerät extends Drucker {
    abstract String anleitung();
    void druckeAnleitung() {
        drucke(anleitung());
    };
}

class Wecker extends Gerät {
    @Override
    String anleitung() {
        return "Anleitung für einen Wecker ...";
    }
}

class Toaster extends Gerät {
    @Override
    String anleitung() {
        return "Anleitung für einen Toaster ...";
    }
}

public class TestVererbung {
    public static void main(String[] args) {
        Wecker testWecker = new Wecker();
        testWecker.druckeAnleitung();
        Toaster testToaster = new Toaster();
        testToaster.druckeAnleitung();
    }
}
