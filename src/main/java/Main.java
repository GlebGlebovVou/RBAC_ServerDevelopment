//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.

public class Main {
    static void main() {
        User u = User.validate("vou","v","nic@f.d");
        IO.println(u.format());
    }
}
