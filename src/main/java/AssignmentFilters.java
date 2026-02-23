public class AssignmentFilters {
    static AssignmentFilter byUser(User user) {
        return (RoleAssignment assignment) -> assignment.user().equals(user);
    }
    static AssignmentFilter byUsername(String username) {
        return (RoleAssignment assignment) -> assignment.user().username().equals(username);
    }
    static AssignmentFilter byRole(Role role) {
        return (RoleAssignment assignment) -> assignment.role().equals(role);
    }
    static AssignmentFilter byRoleName(String roleName) {
        return (RoleAssignment assignment) -> assignment.role().name.equals(roleName);
    }
    static AssignmentFilter activeOnly() {
        return RoleAssignment::isActive;
    }
    static AssignmentFilter inactiveOnly() {
        return (RoleAssignment assignment) -> !assignment.isActive();
    }
    static AssignmentFilter byType(String type) {
        return (RoleAssignment assignment) -> assignment.assignmentType().equals(type);
    }
    static AssignmentFilter assignedBy(String username) {
        return (RoleAssignment assignment) -> assignment.metadata().assignedBy().equals(username);
    }
    static AssignmentFilter assignedAfter(String date) {
        return (RoleAssignment assignment) -> assignment.metadata().assignedAt().compareTo(date) > 0;
    }
    static AssignmentFilter expiringBefore(String date) {
        return (RoleAssignment assignment) -> assignment.assignmentType().equals("TEMPORARY") && ((TemporaryAssignment)assignment).isExpired();
    }
}
