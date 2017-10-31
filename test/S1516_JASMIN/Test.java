package S1516_JASMIN;

import java.util.*;

/**
 *
 * @author Anne Brüggemann-Klein
 */

class A {
    private String nameA;
    private int[] secretNumbers;
    public A(String nameA, int size) {
        this.nameA = nameA;
        secretNumbers = new int[size];
        for (int i = 0; i < size; i++) {
            secretNumbers[i] = i * i;
        }
    }
    public void printInfo() {
        System.out.print(nameA + " " + Arrays.toString(secretNumbers));
    }
}
class B extends A {
    private String nameB;
    public B(String nameA, String nameB) {
        super(nameA, 3);
        this.nameB = nameB;
    }
    @Override
    public void printInfo() {
        System.out.print(nameB + " ");
        super.printInfo();
    }
}

public class Test {
    public static void main(String[] args) {
        B varB=new B("AAA","BBB");
        varB.printInfo();
        System.out.println();
    }
}
