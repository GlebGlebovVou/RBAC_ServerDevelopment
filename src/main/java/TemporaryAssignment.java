import java.time.LocalDateTime;

public class TemporaryAssignment extends AbstractRoleAssignment {
    public String expiresAt = LocalDateTime.now().toString();

    public boolean autoRenew;

    TemporaryAssignment(User user, Role role, AssignmentMetadata metadata) {
        super(user, role, metadata);
    }

    @Override
    public boolean isActive() {
        return !isExpired();
    }

    public void extend(String newExpirationDate) {
        expiresAt = newExpirationDate;
    }

    boolean isExpired() {
        return LocalDateTime.now().toString().compareTo(expiresAt) <= 0;
    }

    @Override
    public String assignmentType() {
        return "TEMPORARY";
    }

    @Override
    public String summary() {
        String res = super.summary();
        res += String.format("\nExpires at %s",expiresAt);
        return res;
    }
}
