
import java.io.*;
import java.util.*;

public class GetAllProperties {

    public static void main(String[] args) {
        Properties pro = new Properties();
        System.out.println(System.getProperty("user.dir"));
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter file name for getting all properties : ");
            FileInputStream in = new FileInputStream(br.readLine() + ".properties");
            pro.load(in);
            System.out.println("All keys of the property file : ");
            System.out.println(pro.keySet());
            System.out.println("All values of the property file : ");
            Enumeration em = pro.keys();
            while (em.hasMoreElements()) {
                String str = (String) em.nextElement();
                System.out.println(str + ": " + pro.get(str));
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
