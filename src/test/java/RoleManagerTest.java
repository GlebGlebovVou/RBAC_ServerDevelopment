import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class RoleManagerTest {

    private RoleManager manager;

    private Role mockRole(String name, String description, String id) {
        Role v = Mockito.mock(Role.class);
        v.name = name;
        v.description = description;
        v.id = id;
        //Mockito.when(v.).thenReturn(name);
        //Mockito.when(v.description).thenReturn(description);
        return v;
    }

    private Permission mockPermission(String name, String resource, String description) {
        Permission v = Mockito.mock(Permission.class);
        Mockito.when(v.description()).thenReturn(description);
        Mockito.when(v.name()).thenReturn(name);
        Mockito.when(v.resource()).thenReturn(resource);
        return v;
    }

    @BeforeEach
    public void setUp() {
        manager = new RoleManager();
        Role u = mockRole("ogo1", "cool","1");
        manager.add(u);

        Role u1 = mockRole("ogo", "cool1","2");
        Role u2 = mockRole("vou", "cool2","3");
        Permission v1 = mockPermission("nice", "1", "1fd");
        Permission v2 = mockPermission("nice1", "12", "1fd3");
        HashSet<Permission> k1 = new HashSet<Permission>();
        k1.add(v1);
        Mockito.when(u1.hasPermission("nice","1")).thenReturn(true);
        Mockito.when(u1.getPermissions()).thenReturn(k1);
        HashSet<Permission> k2 = new HashSet<Permission>();
        k2.add(v2);
        Mockito.when(u1.hasPermission("nice1","12")).thenReturn(true);
        Mockito.when(u2.getPermissions()).thenReturn(k2);
        manager.add(u1);
        manager.add(u2);
    }

    @Test
    public void userManager_findByName_Returns() {
        assertEquals(manager.findByName("ogo1").get(), manager.findById("1").get());
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
        assertEquals(1, a.get(0).getPermissions().size());
    }

    @Test
    public void userManager_Exists() {
        assertTrue(manager.exists(manager.findById("1").get().name));
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