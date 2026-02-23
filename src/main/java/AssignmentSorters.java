import java.util.Comparator;

public class AssignmentSorters {
    static Comparator<RoleAssignment> byUsername() {
        return Comparator.comparing((o1) -> o1.user().username());
    }
    static Comparator<RoleAssignment> byRoleName() {
        return Comparator.comparing((o1) -> o1.role().name);
    }
    static Comparator<RoleAssignment> byAssignmentData() {
        return Comparator.comparing((o1) -> o1.metadata().assignedAt());
    }
}
