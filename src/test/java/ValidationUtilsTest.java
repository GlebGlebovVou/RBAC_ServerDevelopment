import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ValidationUtilsTest {
    @BeforeEach
    public void setUp() {

    }

    @Test
    public void validationUtils_isValidUsername() {
        assertTrue(ValidationUtils.isValidUsername("correctName"));
        assertFalse(ValidationUtils.isValidUsername("c"));
        assertFalse(ValidationUtils.isValidUsername("correctName1111111111111111111"));
        assertFalse(ValidationUtils.isValidUsername("cor  rec   tName  111111111"));
    }

    @Test
    public void validationUtils_isValidEmail() {
        assertTrue(ValidationUtils.isValidEmail("correct@ogo.com"));
        assertFalse(ValidationUtils.isValidEmail("takoe@ogocom"));
        assertFalse(ValidationUtils.isValidEmail("takoeogo.com"));
    }

    @Test
    public void validationUtils_isValidDate() {
        assertTrue(ValidationUtils.isValidDate("55-55-12T2515:21525:21515.12312"));
        assertFalse(ValidationUtils.isValidDate("55-55-12T2515:21525:2151512312"));
        assertFalse(ValidationUtils.isValidDate("55-55-122515:21525:21515.12312"));
    }

    @Test
    public void validationUtils_normalizeString() {
        assertEquals(ValidationUtils.normalizeString("TEXT TEST"),"texttest");
    }
}
