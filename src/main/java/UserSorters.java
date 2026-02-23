import java.util.Comparator;

public class UserSorters {
    static Comparator<User> byUsername() {
        return Comparator.comparing(User::username);
    }
    static Comparator<User> byFullName() {
        return Comparator.comparing(User::fullName);
    }
    static Comparator<User> byEmail() {
        return Comparator.comparing(User::email);
    }
}
