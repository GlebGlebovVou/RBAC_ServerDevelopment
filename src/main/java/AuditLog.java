import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;

public class AuditLog {
    static volatile List<AuditEntry> entries = new CopyOnWriteArrayList<AuditEntry>();
    static BlockingQueue<String[]> queue = new ArrayBlockingQueue<String[]>(5);
    static Runnable handler = () -> {
        while(true) {
            try {
                while (!queue.isEmpty()) {
                    String[] get = queue.take();
                    AuditEntry e = new AuditEntry(LocalDateTime.now().toString(), get[0], get[1], get[2], get[3]);
                    entries.add(e);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    };
    static public Thread queHandler = new Thread(handler);
    static public void initHandler() {
        if(!queHandler.isAlive()) {
            queHandler.start();
        }
    }
    static public void log(String action, String performer, String target, String details) {
        //AuditEntry e = new AuditEntry(LocalDateTime.now().toString(),action,performer,target,details);
        String[] data = new String[]{action, performer, target, details};
        try {
            queue.put(data);
            Thread.sleep(100);
        }
        catch(Exception e) {
            IO.println("error");
        }
    }
    static public List<AuditEntry> getAll() {
        return entries;
    }
    static public List<AuditEntry> getByPerformer(String performer) {
        return entries.stream().filter((e) -> e.performer().equals(performer)).toList();
    }
    static public List<AuditEntry> getByAction(String action) {
        return entries.stream().filter((e) -> e.action().equals(action)).toList();
    }
    static public void printLog() {
        for(AuditEntry x : entries) {
            IO.println(String.format("%s: %s by %s, target: %s, details: %s",x.timestamp(),
                    x.action(),x.performer(),x.target(),x.details()));
        }
    }
    static public void saveToFile(String filename) throws IOException {
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
