import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RoleManagerTest {

    private RoleManager manager;
    Role u = new Role("ogo1", "cool");

    @BeforeEach
    public void setUp() {
        manager = new RoleManager();
        manager.add(u);
        Role u1 = new Role("ogo", "cool1");
        Role u2 = new Role("vou", "cool2");
        u1.addPermission(new Permission("nice", "1", "1fd"));
        u2.addPermission(new Permission("nice1", "12", "1fd3"));
        manager.add(u1);
        manager.add(u2);
    }

    @Test
    public void userManager_findByName_Returns() {
        assertEquals(manager.findByName("ogo1").get(), u);
    }


    @Test
    public void userManager_count_Returns() {
        assertEquals(3, manager.count());
    }

    @Test
    public void userManager_findAll_Returns() {
        List<Role> a = manager.findAll();
        assertEquals(3, a.size());
    }

    @Test
    public void userManager_findAllFilter_Returns() {
        List<Role> a = manager.findAll(RoleFilters.byName("ogo"), RoleSorters.byPermissionCount());
        assertEquals(1, a.size());
        assertEquals("ogo", a.get(0).name);
        assertEquals(1, a.get(0).permissions.size());
    }

    @Test
    public void userManager_Exists() {
        assertTrue(manager.exists(u.name));
    }

    @Test
    public void userManager_Clear() {
        manager.clear();
        assertEquals(0, manager.count());
    }

    @Test
    public void userManager_findByFilter() {
        List<Role> a = manager.findByFilter(RoleFilters.byNameContains("og"));
        assertEquals(2, a.size());
    }

    @Test
    public void userManager_findRolesWithPermission() {
        List<Role> a = manager.findRolesWithPermission("nice","1");
        assertEquals(1,a.size());
    }
}