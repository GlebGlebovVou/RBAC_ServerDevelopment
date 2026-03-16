import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.Format;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class CommandRegistryTest {
    @Test
    void testThing() {
        CommandRegistry.register();
        assertNotEquals(0, CommandParser.commands.size());
    }
}
