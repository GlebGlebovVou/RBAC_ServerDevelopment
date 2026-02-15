public record AssignmentMetadata(String assignedBy, String assignedAt, String reason) {
    static AssignmentMetadata now(String assignedBy, String reason) {
        return new AssignmentMetadata(assignedBy,"",reason);
    }
    public String format() {
        return String.format("%s by %s: %s",assignedAt,assignedBy,reason);
    }
}
