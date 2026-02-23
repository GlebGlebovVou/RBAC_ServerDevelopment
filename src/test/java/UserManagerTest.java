import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserManagerTest {
    private UserManager manager;
    User u = User.validate("ogogdgd","test","ogo@vou.com");
    @BeforeEach
    public void setUp() {
        manager = new UserManager();
        manager.add(u);
        manager.add(User.validate("test","test2","test@ogo.com"));
        manager.add(User.validate("test1","test1","test@ogo.com"));
    }

    @Test
    public void userManager_findByUsername_Returns() {
        assertEquals(manager.findByUsername("ogogdgd").get(),u);
    }

    @Test
    public void userManager_findByEmail_Returns() {
        assertEquals(manager.findByEmail("ogo@vou.com").get(),u);
    }

    @Test
    public void userManager_count_Returns() {
        assertEquals(3,manager.count());
    }

    @Test
    public void userManager_findAll_Returns() {
        List<User> a = manager.findAll();
        assertEquals(3,a.size());
    }

    @Test
    public void userManager_findAllFilter_Returns() {
        List<User> a = manager.findAll(UserFilters.byEmail("test@ogo.com"),UserSorters.byFullName());
        assertEquals(2,a.size());
        assertEquals("test1",a.get(0).fullName());
    }

    @Test
    public void userManager_Exists() {
        assertTrue(manager.exists("ogogdgd"));
    }

    @Test
    public void userManager_Clear() {
        manager.clear();
        assertEquals(0,manager.count());
    }

    @Test
    public void userManager_findByFilter() {
        List<User> a = manager.findByFilter(UserFilters.byEmail("test@ogo.com"));
        assertEquals(2,a.size());
    }

    @Test
    public void userManager_Update() {
        manager.update("ogogdgd", "testogo","vou@ogo.com");
        assertFalse(manager.findByEmail("vou@ogo.com").isEmpty());
    }
}
