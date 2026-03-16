import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserManagerTest {
    private UserManager manager;

    private User mockUser(String username, String fullName, String email) {
        User v = Mockito.mock(User.class);//new User("qqqa","gfgf","vou@ogo.com");
        Mockito.when(v.email()).thenReturn(email);
        Mockito.when(v.fullName()).thenReturn(fullName);
        Mockito.when(v.username()).thenReturn(username);
        return v;
    }

    @BeforeEach
    public void setUp() {
        manager = new UserManager();
        manager.add(mockUser("ogogdgd","test","ogo@vou.com"));
        manager.add(mockUser("test","test2","test@ogo.com"));
        manager.add(mockUser("test1","test1","test@ogo.com"));
    }

    @Test
    public void userManager_findByUsername_Returns() {
        assertTrue(manager.findByUsername("ogogdgd").isPresent());
    }

    @Test
    public void userManager_findByEmail_Returns() {
        assertTrue(manager.findByEmail("ogo@vou.com").isPresent());
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
