import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class AuditLogTest {

    @BeforeEach
    public void setUp() {
        AuditLog.entries.clear();
        AuditLog.initHandler();
        AuditLog.log("smth","vou","smth","dsf");
        AuditLog.log("smt11h","vou","sm11th","ds1f");
        AuditLog.log("smth","vou11","sm11th","ds1f11");
    }

    @Test
    public void auditLog_getAll() {
        assertEquals(3, AuditLog.getAll().size());
        assertEquals("smth", AuditLog.getAll().getFirst().action());
    }

    @Test
    public void auditLog_getByPerformer() {
        assertEquals(2, AuditLog.getByPerformer("vou").size());
        assertEquals("dsf", AuditLog.getByPerformer("vou").getFirst().details());
    }

    @Test
    public void auditLog_getByAction() {
        assertEquals(2, AuditLog.getByAction("smth").size());
        assertEquals("smth", AuditLog.getByAction("smth").getFirst().target());
    }

}
