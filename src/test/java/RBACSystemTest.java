import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.Format;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RBACSystemTest {
    RBACSystem system = new RBACSystem();

    @BeforeEach
    void setUp() {
        system.initialize();
    }

    @Test
    void rbacsystem_generateStatistics() {
        String res = system.generateStatistics();
        assertTrue(res.contains("Admin"));
        assertTrue(res.contains("Viewer"));
        assertTrue(res.contains("Manager"));
    }
}
