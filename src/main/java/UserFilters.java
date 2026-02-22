import java.util.Locale;

public class UserFilters {
    static UserFilter byUsername(String username) {
        return (User user) -> user.username().equals(username);
    }
    static UserFilter byUsernameContains(String substring) {
        return (User user) -> user.username().toLowerCase(Locale.ROOT).contains(substring.toLowerCase(Locale.ROOT));
    }
    static UserFilter byEmail(String email) {
        return (User user) -> user.email().equals(email);
    }
    static UserFilter byEmailDomain(String domain) {
        return (User user) -> user.email().endsWith(domain);
    }
    static UserFilter byFullNameContains(String substring) {
        return (User user) -> user.fullName().contains(substring);
    }
}
