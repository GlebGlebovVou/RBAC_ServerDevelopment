import java.util.Comparator;

public class RoleSorters {
    Comparator<Role> byName() {
        return Comparator.comparing((Role o1) -> o1.name);
    }
    Comparator<Role> byPermissionCount() {
        return Comparator.comparing((Role o1) -> o1.getPermissions().size());
    }
}
