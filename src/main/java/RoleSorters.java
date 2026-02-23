import java.util.Comparator;

public class RoleSorters {
    static Comparator<Role> byName() {
        return Comparator.comparing((Role o1) -> o1.name);
    }
    static Comparator<Role> byPermissionCount() {
        return Comparator.comparing((Role o1) -> o1.getPermissions().size());
    }
}
