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
        assertEquals(3, log.getAll().size());
        assertEquals("smth", log.getAll().getFirst().action());
    }

    @Test
    public void auditLog_getByPerformer() {
        assertEquals(2, log.getByPerformer("vou").size());
        assertEquals("dsf", log.getByPerformer("vou").getFirst().details());
    }

    @Test
    public void auditLog_getByAction() {
        assertEquals(2, log.getByAction("smth").size());
        assertEquals("smth", log.getByAction("smth").getFirst().target());
    }

}
