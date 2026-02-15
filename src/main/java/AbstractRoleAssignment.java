import java.util.Objects;
import java.util.UUID;

abstract public class AbstractRoleAssignment {
    String assignmentId = UUID.randomUUID().toString();
    User user;
    Role role;
    AssignmentMetadata metadata;
    AbstractRoleAssignment(User user,Role role,AssignmentMetadata metadata) {
        this.user = user;
        this.role = role;
        this.metadata = metadata;
    }
    User user() {
        return this.user;
    }
    Role role() {
        return this.role;
    }
    AssignmentMetadata metadata() {
        return this.metadata;
    }
    @Override
    public boolean equals(Object o) {
        if(o == this)
            return true;
        if(o == null)
            return false;
        if(!(o instanceof AbstractRoleAssignment))
            return false;
        return ((AbstractRoleAssignment) o).assignmentId.equals(assignmentId);
    }
    @Override
    public int hashCode() {
        return Objects.hashCode(assignmentId);
    }
    String summary() {
        String out = String.format("[%s] %s assigned to %s by %s at %s",
                assignmentType(),role().name,user().username(),
                metadata().assignedBy(),
                metadata().assignedAt());
        out += String.format("\nReason: %s", metadata().reason());
        out += String.format("\nStatus: %s", isActive() ? "ACTIVE" : "NOT ACTIVE");
        return out;
    }
    abstract boolean isActive();
    abstract String assignmentType();
}
