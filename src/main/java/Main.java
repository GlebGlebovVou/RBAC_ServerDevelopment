//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import java.util.List;

public class Main {
    static void testValidation() {
        List<List<String>> testsList = List.of(
                List.of("User","Name Full Of User", "something@gmail.com"),
                List.of("User wrong wrong","Name Full Of User", "something@gmail.com"),
                List.of("User","Name Full Of User", "wrong@gmailcom"),
                List.of("User","Name Full Of User", "somethinggmail.com"),
                List.of("U","Name Full Of User", "something@gmail.com"),
                List.of("UserTooooooLooooooooooooooooong","Name Full Of User", "something@gmail.com")
                );
        int ind = 1;
        for(List<String> curCheck : testsList) {
            IO.println(String.format("%d. user: (usern: %s, full n: %s, em: %s)",ind,curCheck.get(0),curCheck.get(1),curCheck.get(2)));
            try {
                User u = User.validate(curCheck.get(0),curCheck.get(1),curCheck.get(2));
                if(u.getClass().isRecord()) {
                    IO.println("Correct validation");
                }
            }
            catch(Exception e) {
                IO.println(String.format("error happened: %s",e.getMessage()));
            }
            finally {
                ind++;
                IO.println();
            }
        }
    }
    static void main() {
        testValidation();
    }
}
