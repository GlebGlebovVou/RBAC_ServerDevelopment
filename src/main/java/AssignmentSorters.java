import java.util.Comparator;

public class AssignmentSorters {
    Comparator<RoleAssignment> byUsername() {
        return Comparator.comparing((o1) -> o1.user().username());
    }
    Comparator<RoleAssignment> byRoleName() {
        return Comparator.comparing((o1) -> o1.role().name);
    }
    Comparator<RoleAssignment> byAssignmentData() {
        return Comparator.comparing((o1) -> o1.metadata().assignedAt());
    }
}
