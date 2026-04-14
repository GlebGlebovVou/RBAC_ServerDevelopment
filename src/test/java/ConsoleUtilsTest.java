import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ConsoleUtilsTest {
    Scanner scan;
    private ByteArrayOutputStream uf = new ByteArrayOutputStream();
    private final PrintStream orig = System.out;
    private final InputStream originalSystemIn = System.in;

    private void writeInput(String input) {
        try {System.in.read(input.getBytes());}
        catch (Exception e) {
            IO.println("some error");
        }
    }

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(uf));
    }

    @AfterEach
    void finish() {
        System.setOut(orig);
        System.setIn(originalSystemIn);
    }

    @Test
    public void consoleUtils_promptString() throws IOException {
        InputStream ogo = new ByteArrayInputStream("input nice".getBytes());
        System.setIn(ogo);
        Scanner scan = new Scanner(ogo);
        String res = ConsoleUtils.promptString(scan, "input mes", false);
        assertEquals("input nice", res);
        assertEquals("input mes", uf.toString());
        System.out.close();
        res = ConsoleUtils.promptString(scan, "input mes", false);
        assertEquals("", res);
        assertEquals("input mes", uf.toString());
    }

    @Test
    public void consoleUtils_promptInt() {
        ByteArrayInputStream ogo = new ByteArrayInputStream("55\n".getBytes());
        System.setIn(ogo);
        Scanner scan = new Scanner(ogo);
        int res = ConsoleUtils.promptInt(scan, "ogo", 1,1000);
        assertEquals(55, res);
        assertEquals("ogo", uf.toString());
    }

    @Test
    public void consoleUtils_promptYesNo() {
        ByteArrayInputStream ogo = new ByteArrayInputStream("yes".getBytes());
        System.setIn(ogo);
        Scanner scan = new Scanner(ogo);
        boolean res = ConsoleUtils.promptYesNo(scan, "m");
        assertTrue(res);
        assertEquals("m", uf.toString());
    }

    @Test
    public void consoleUtils_promptChoice() {
        ByteArrayInputStream ogo = new ByteArrayInputStream("1\n".getBytes());
        System.setIn(ogo);
        Scanner scan = new Scanner(ogo);
        List<String> list = new ArrayList<String>();
        list.add("1");
        list.add("2");
        String res = ConsoleUtils.promptChoice(scan,"",list);
        assertEquals("1", res);
    }
}
