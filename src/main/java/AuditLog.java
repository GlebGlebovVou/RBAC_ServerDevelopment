import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AuditLog {
    List<AuditEntry> entries = new ArrayList<AuditEntry>();
    void log(String action, String performer, String target, String details) {
        AuditEntry e = new AuditEntry(LocalDateTime.now().toString(),action,performer,target,details);
        entries.add(e);
    }
    List<AuditEntry> getAll() {
        return entries;
    }
    List<AuditEntry> getByPerformer(String performer) {
        return entries.stream().filter((e) -> e.performer().equals(performer)).toList();
    }
    List<AuditEntry> getByAction(String action) {
        return entries.stream().filter((e) -> e.action().equals(action)).toList();
    }
    void printLog() {
        for(AuditEntry x : entries) {
            IO.println(String.format("%s: %s by %s, target: %s, details: %s",x.timestamp(),
                    x.action(),x.performer(),x.target(),x.details()));
        }
    }
    void saveToFile(String filename) throws IOException {
        try (FileWriter w = new FileWriter(filename)) {
            for(AuditEntry x : entries) {
                w.write(String.format("%s: %s by %s, target: %s, details: %s\n",x.timestamp(),
                    x.performer(),x.action(),x.target(),x.details()));
            }
        }
        catch (IOException e){
            throw new IOException();
        }
    }
}
