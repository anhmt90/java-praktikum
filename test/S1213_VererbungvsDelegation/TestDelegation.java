

/**
 *
 * @author Anne Brüggemann-Klein
 */

class Drucker {
    void drucke(String doc) {
        System.out.println("Aus dem Drucker kommt:\n"+doc);
    }
}

// Diese Lösung mit Delegation ist vorzuziehen.
// Gerät ist immer noch frei bezüglich der Einordnung in die
// Vererbungshierarchie. Der Drucker ist gekapselt (wir könnten
// ihn mit "private final" noch besser kapseln.
abstract class Gerät {
    
    Drucker systemDrucker = new Drucker();
    
    abstract String anleitung();
    
    void druckeAnleitung() {
        systemDrucker.drucke(anleitung());
    }
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

public class TestDelegation {
    public static void main(String[] args) {
        Wecker testWecker = new Wecker();
        testWecker.druckeAnleitung();
        Toaster testToaster = new Toaster();
        testToaster.druckeAnleitung();
    }
}
