import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AuditLogTest {

    public AuditLog log = new AuditLog();

    @BeforeEach
    public void setUp() {
        log.entries.clear();
        log.log("smth","vou","smth","dsf");
        log.log("smt11h","vou","sm11th","ds1f");
        log.log("smth","vou11","sm11th","ds1f11");
    }

    @Test
    public void auditLog_getAll() {
        assertEquals(log.getAll().size(),3);
        assertEquals(log.getAll().getFirst().action(),"smth");
    }

    @Test
    public void auditLog_getByPerformer() {
        assertEquals(log.getByPerformer("vou").size(),2);
        assertEquals(log.getByPerformer("vou").getFirst().details(),"dsf");
    }

    @Test
    public void auditLog_getByAction() {
        assertEquals(log.getByAction("smth").size(),2);
        assertEquals(log.getByAction("smth").getFirst().target(),"smth");
    }

}
