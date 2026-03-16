import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.Format;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class DateUtilsTest {

    @Test
    void dateUtils_getCurrentDate() {
        assertEquals(DateUtils.getCurrentDate(),LocalDate.now().toString());
        IO.println(LocalDate.now());
    }

    @Test
    void dateUtils_isBefore() {
        assertTrue(DateUtils.isBefore("2020-02-10","2020-02-11"));
        assertFalse(DateUtils.isBefore("2015-02-10","2010-02-10"));
        assertTrue(DateUtils.isBefore("2014-03-09","2014-05-09"));
    }

    @Test
    void dateUtils_isAfter() {
        assertTrue(DateUtils.isAfter("2020-02-16","2020-02-11"));
        assertFalse(DateUtils.isAfter("2009-02-10","2010-02-10"));
        assertTrue(DateUtils.isAfter("2014-09-09","2014-05-09"));
    }

    @Test
    void dateUtils_addDays() {
        assertEquals("2020-05-14",DateUtils.addDays("2020-05-08",6));
        assertEquals("2020-06-05",DateUtils.addDays("2020-05-30",6));
    }

    @Test
    void dateUtils_formatRelativeTime() {
        String res = DateUtils.formatRelativeTime("2020-05-14");
        assertTrue(res.contains("ago"));
        res = DateUtils.formatRelativeTime("9000-05-14");
        assertTrue(res.contains("in"));
    }
}
