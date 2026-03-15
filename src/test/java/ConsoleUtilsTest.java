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
    private InputStream ogo = new ByteArrayInputStream("".getBytes());
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
        System.setIn(ogo);
    }

    @AfterEach
    void finish() {
        System.setOut(orig);
        System.setIn(originalSystemIn);
    }

    @Test
    public void consoleUtils_promptString() {
        writeInput("input nice");
        InputStream ogo = new ByteArrayInputStream("".getBytes());
        try {ogo.read("input nice".getBytes());}
        catch (Exception e) {
            IO.println("some error");
        }
        Scanner scan = new Scanner(ogo);
        writeInput("input nice");
        String res = ConsoleUtils.promptString(scan, "input mes", true);
        assertEquals(res, "input nice");
        assertEquals(uf.toString(),"input mes");
        writeInput("");
        System.out.close();
        res = ConsoleUtils.promptString(scan, "input mes", true);
        assertEquals(res, null);
        assertEquals(uf.toString(),"input mes");
    }

    @Test
    public void consoleUtils_promptInt() {
        writeInput("55");
        Scanner scan = new Scanner(System.in);
        int res = ConsoleUtils.promptInt(scan, "ogo", 1,1000);
        assertEquals(res, 55);
        assertEquals(uf.toString(),"ogo");
        writeInput("10000");
        res = ConsoleUtils.promptInt(scan, "ogo", 1,1000);
    }
}
