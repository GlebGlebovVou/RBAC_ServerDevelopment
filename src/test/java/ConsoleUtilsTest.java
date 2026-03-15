import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConsoleUtilsTest {
    Scanner scan;
    private ByteArrayOutputStream uf = new ByteArrayOutputStream();
    private final PrintStream orig = System.out;
    private final InputStream originalSystemIn = System.in;

    @BeforeEach
    void setUp() {
        scan = Mockito.mock(Scanner.class);
        System.setOut(new PrintStream(uf));
    }

    @AfterEach
    void finish() {
        System.setOut(orig);
        System.setIn(originalSystemIn);
    }

    @Test
    public void consoleUtils_promptString() {
        InputStream ogo = new ByteArrayInputStream("input nice".getBytes());
        InputStream ogo1 = new ByteArrayInputStream("ogo".getBytes());
        try {
            System.setIn(ogo);
            Scanner scan = new Scanner(System.in);
            String res = ConsoleUtils.promptString(scan, "input mes", true);
            assertEquals(res, "input nice");
            assertEquals(uf.toString(),"input mes");
            System.setIn(ogo1);
            Scanner scan1 = new Scanner(ogo1);
            String res1 = ConsoleUtils.promptString(scan1, "input mes", true);
            assertEquals(res1, null);
            assertEquals(uf.toString(),"input mes");
        }
        finally {
            System.setIn(originalSystemIn);
        }
    }

    @Test
    public void consoleUtils_promptInt() {
        InputStream originalSystemIn = System.in;
        InputStream ogo = new ByteArrayInputStream("input nice".getBytes());
        try {
            System.setIn(ogo);
            Scanner scan = new Scanner(System.in);
            String res = ConsoleUtils.promptString(scan, "input mes", true);
            assertEquals(res, "input nice");
            assertEquals(uf.toString(),"input mes");
        }
        finally {
            System.setIn(originalSystemIn);
        }
    }
}
