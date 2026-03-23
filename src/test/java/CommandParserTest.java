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

public class CommandParserTest {

    private ByteArrayOutputStream uf = new ByteArrayOutputStream();
    private final PrintStream orig = System.out;
    private final Scanner scanner = new Scanner(System.in);
    private final RBACSystem system = new RBACSystem();
    @BeforeEach
    void setUp() {
        CommandParser.registerCommand("vou","vou11", (scan,ogo,vou) -> {IO.print("test");});
        CommandParser.registerCommand("vou1","vou11", (scan,ogo,vou) -> {IO.print(vou[0]);});
        System.setOut(new PrintStream(uf));
    }

    @AfterEach
    void finish() {
        CommandParser.commands.clear();
        CommandParser.commandDescriptions.clear();
        System.setOut(orig);
    }

    @Test
    void commandparser_registerCommand() {
        assertEquals(2, CommandParser.commands.size());
    }

    @Test
    void commandparser_executeCommand() {
        CommandParser.executeCommand("vou", scanner, system, null);
        assertEquals("test", uf.toString());
    }

    @Test
    void commandparser_printHelp() {
        CommandParser.printHelp();

        assertEquals("""
                ******
                *Help*
                ******
                \r
                -------------
                |vou1: vou11|
                -------------
                \r
                ------------
                |vou: vou11|
                ------------
                \r
                """, uf.toString());
    }

    @Test
    void commandparser_parseAndExecute() {
        CommandParser.parseAndExecute("vou1 cool",scanner,system);
        assertEquals("cool",uf.toString());
    }
}
