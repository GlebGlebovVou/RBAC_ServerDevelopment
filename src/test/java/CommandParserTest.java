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

    @BeforeEach
    void setUp() {
    }

    @Test
    void commandparser_registerCommand() {
        CommandParser.registerCommand("vou","vou", (scan,ogo,vou) -> {});
        assertTrue(CommandParser.commands.size() == 1);
    }
}
