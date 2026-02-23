import java.util.Locale;

public class RoleFilters {
    static RoleFilter byName(String name) {
        return (Role role) -> role.name.equals(name);
    }
    static RoleFilter byNameContains(String substring) {
        return (Role role) -> role.name.toLowerCase(Locale.ROOT).contains(substring.toLowerCase(Locale.ROOT));
    }
    static RoleFilter hasPermission(Permission permission) {
        return (Role role) -> role.hasPermission(permission);
    }
    static RoleFilter hasPermission(String permission, String resource) {
        return (Role role) -> role.hasPermission(permission, resource);
    }
    static RoleFilter hasAtLeastNPermissions(int n) {
        return (Role role) -> role.permissions.size() >= n;
    }
}
