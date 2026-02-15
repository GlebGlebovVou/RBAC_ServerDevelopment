public class PermanentAssignment extends AbstractRoleAssignment {
PermanentAssignment(User user,Role role,AssignmentMetadata metadata) {
    super(user,role,metadata);
}
    boolean revoked = false;
    void revoke() {
        revoked = true;
    }
    boolean isRevoked() {
        return revoked;
    }

    @Override
    public boolean isActive() {
        return revoked;
    }

    @Override
    public String assignmentType() {
        return "PERMANENT";
    }
}
