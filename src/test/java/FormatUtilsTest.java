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

public class FormatUtilsTest {
    String[] headers = new String[2];
    List<String[]> rows = new ArrayList<String[]>();

    @BeforeEach
    void setUp() {
        headers = new String[2];
        headers[0] = "test1";
        headers[1] = "test445552";
        rows = new ArrayList<String[]>();
        String[] values1 = new String[2];
        values1[0] = "value1"; values1[1] = "value2";
        String[] values2 = new String[2];
        values2[0] = "value2111111"; values2[1] = "value22";
        rows.add(values1);
        rows.add(values2);
    }

    @Test
    public void formatUtils_formatTable() {
        String res = FormatUtils.formatTable(headers,rows);
        assertEquals("*********************************\n" +
                "*     test1     *     test445...*\n" +
                "*********************************\n" +
                "*     value1    *     value2    *\n" +
                "*     value21...*     value22   *\n" +
                "*********************************\n",res);
    }

    @Test
    public void formatUtils_formatBox() {
        String res = FormatUtils.formatBox(headers[1]);
        assertEquals("------------\n" +
                "|test445552|\n" +
                "------------\n",res);
    }

    @Test
    public void formatUtils_formatHeader() {
        String res = FormatUtils.formatHeader(headers[1]);
        assertEquals("************\n" +
                "*test445552*\n" +
                "************\n",res);
    }

    @Test
    public void formatUtils_truncate() {
        String res = FormatUtils.truncate(rows.get(1)[0],12);
        assertEquals("value2111...",res);
        res = FormatUtils.truncate(rows.get(1)[0],5);
        assertEquals("va...",res);
        res = FormatUtils.truncate(rows.get(1)[1],10);
        assertEquals("value22",res);
        IO.println(res);
    }

    @Test
    public void formatUtils_padRight() {
        String res = FormatUtils.padRight(headers[0],12);
        assertEquals("test1       ",res);
        res = FormatUtils.padRight(headers[0],2);
        assertEquals("test1",res);
        res = FormatUtils.padRight(headers[0],6);
        assertEquals("test1 ",res);
    }

    @Test
    public void formatUtils_padLeft() {
        String res = FormatUtils.padLeft(headers[0],12);
        assertEquals("       test1",res);
        res = FormatUtils.padLeft(headers[0],2);
        assertEquals("test1",res);
        res = FormatUtils.padLeft(headers[0],6);
        assertEquals(" test1",res);
    }
 }
