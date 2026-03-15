import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class ConsoleUtils {
    public static String promptString(Scanner scanner, String message, boolean required) {
        IO.print(message);
        while(true) {
            String a = scanner.next();
            if(!required || !a.isEmpty()) {
                return a;
            }
            IO.println("Error");
        }
    }
    public static int promptInt(Scanner scanner, String message, int min, int max) {
        while(true) {
            IO.print(message);
            int n = scanner.nextInt();
            if(n >= min && n <= max) {
                return n;
            }
            IO.println("Error");
        }
    }
    public static boolean promptYesNo(Scanner scanner, String message) {
        while(true) {
            IO.print(message);
            String res = scanner.next().toLowerCase(Locale.ROOT);
            List<String> allowed = new ArrayList<String>();
            if(allowed.contains(res)) {
                return res.equals("yes") || res.equals("да");
            }
            IO.println("Error");
        }
    }
    <T> T promptChoice(Scanner scanner, String message, List<T> options) {
        for(int i = 0; i < options.size(); i++) {
            IO.println(String.format("%d. %s",i+1,options.get(i)));
        }
        int n = promptInt(scanner, message, 1, options.size());
        return options.get(n);
    }
}
