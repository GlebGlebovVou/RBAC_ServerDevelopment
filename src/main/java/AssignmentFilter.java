@FunctionalInterface
public interface AssignmentFilter {
    boolean test(RoleAssignment assignment);
    default AssignmentFilter and(AssignmentFilter other) {
        return (RoleAssignment assignment) -> (this.test(assignment) && other.test(assignment));
    }
    default AssignmentFilter or(AssignmentFilter other) {
        return (RoleAssignment assignment) -> (this.test(assignment) || other.test(assignment));
    }
}
