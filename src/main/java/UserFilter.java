@FunctionalInterface
public interface UserFilter {
    boolean test(User user);
    default UserFilter and(UserFilter other) {
        return (User user) -> this.test(user) && other.test(user);
    }
    default UserFilter or(UserFilter other){
        return (User user) -> this.test(user) || other.test(user);
    }
}
