
import java.time.LocalDateTime;

public record AssignmentMetadata(String assignedBy, String assignedAt, String reason) {
    static AssignmentMetadata now(String assignedBy, String reason) {
        LocalDateTime date = LocalDateTime.now();
        return new AssignmentMetadata(assignedBy,date.toString(),reason);
    }
    public String format() {
        return String.format("%s by %s: %s",assignedAt,assignedBy,reason);
    }
}
