public class AssignmentFilters {
    AssignmentFilter byUser(User user) {
        return (RoleAssignment assignment) -> assignment.user().equals(user);
    }
    AssignmentFilter byUsername(String username) {
        return (RoleAssignment assignment) -> assignment.user().username().equals(username);
    }
    AssignmentFilter byRole(Role role) {
        return (RoleAssignment assignment) -> assignment.role().equals(role);
    }
    AssignmentFilter byRoleName(String roleName) {
        return (RoleAssignment assignment) -> assignment.role().name.equals(roleName);
    }
    AssignmentFilter activeOnly() {
        return RoleAssignment::isActive;
    }
    AssignmentFilter inactiveOnly() {
        return (RoleAssignment assignment) -> !assignment.isActive();
    }
    AssignmentFilter byType(String type) {
        return (RoleAssignment assignment) -> assignment.assignmentType().equals(type);
    }
    AssignmentFilter assignedBy(String username) {
        return (RoleAssignment assignment) -> assignment.metadata().assignedBy().equals(username);
    }
    AssignmentFilter assignedAfter(String date) {
        return (RoleAssignment assignment) -> assignment.metadata().assignedAt().compareTo(date) > 0;
    }
    AssignmentFilter expiringBefore(String date) {
        return (RoleAssignment assignment) -> assignment.assignmentType().equals("TEMPORARY") && ((TemporaryAssignment)assignment).expiresAt.compareTo(date) < 0;
    }
}
