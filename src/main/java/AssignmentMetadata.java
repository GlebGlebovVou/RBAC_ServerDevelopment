
import java.time.LocalDateTime;

public record AssignmentMetadata(String assignedBy, String assignedAt, String reason) {
    static AssignmentMetadata now(String assignedBy, String reason) {
        return new AssignmentMetadata(assignedBy,DateUtils.getCurrentDateTime(),reason);
    }
    public String format() {
        return String.format("%s by %s: %s",assignedAt,assignedBy,reason);
    }
}
