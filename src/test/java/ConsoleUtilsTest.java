import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConsoleUtilsTest {
    Scanner scan;

    @BeforeEach
    void setUp() {
        scan = Mockito.mock(Scanner.class);
    }
    @Test
    public void consoleUtils_promptString() {
        Scanner scan = new Scanner(System.in);
        Mockito.when(scan.next()).thenReturn("input nice");
        assertEquals(ConsoleUtils.promptString(scan,"input mes",true),"input nice");
    }
}
