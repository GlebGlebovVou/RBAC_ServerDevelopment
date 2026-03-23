import java.util.*;

public class ConsoleUtils {
    public static String promptString(Scanner scanner, String message, boolean required) {
        IO.print(message);
        while(true) {
            String a;
            try {
                a = scanner.nextLine();
            }
            catch (NoSuchElementException e) {
                if(required)
                    continue;
                return "";
            }
            catch(Exception e) {
                continue;
            }
            return a;
        }
    }
    public static int promptInt(Scanner scanner, String message, int min, int max) {
        while(true) {
            IO.print(message);
            int n;
            try {
                n = scanner.nextInt();
            }
            catch(Exception e) {
                continue;
            }
            if(n >= min && n <= max) {
                scanner.nextLine();
                return n;
            }
            IO.println("Error");
        }
    }
    public static boolean promptYesNo(Scanner scanner, String message) {
        while(true) {
            IO.print(message);
            String res;
            try {
                res = scanner.nextLine().toLowerCase(Locale.ROOT);
                if(!res.equals("no") && !res.equals("yes")) {
                    throw new Exception();
                }
            }
            catch (Exception e) {
                continue;
            }
            return res.equals("yes") || res.equals("да");
        }
    }
    public static <T> T promptChoice(Scanner scanner, String message, List<T> options) {
        for(int i = 0; i < options.size(); i++) {
            IO.println(String.format("%d. %s",i+1,options.get(i)));
        }
        int n = promptInt(scanner, message, 1, options.size());
        return options.get(n-1);
    }
}
