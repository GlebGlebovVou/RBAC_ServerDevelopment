@FunctionalInterface
public interface RoleFilter {
    boolean test(Role role);
    default RoleFilter and(RoleFilter another) {
        return (Role role) -> this.test(role) && another.test(role);
    }
    default RoleFilter or(RoleFilter another) {
        return (Role role) -> this.test(role) || another.test(role);
    }
}
