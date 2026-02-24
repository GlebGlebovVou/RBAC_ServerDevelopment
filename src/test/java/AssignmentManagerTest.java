import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AssignmentManagerTest {

    private AssignmentManager manager;
    User v = new User("qqqq","gfgf","vou@ogo.com");
    Role r = new Role("fqfq","fgfg");
    AssignmentMetadata m = new AssignmentMetadata("qqqq", LocalDateTime.now().toString(),"dfdf");
    TemporaryAssignment u = new TemporaryAssignment(v,r,m);

    @BeforeEach
    public void setUp() {
        manager = new AssignmentManager();
        u.extend(LocalDateTime.now().plusHours(5).toString());
        manager.add(u);
        User v = new User("qqqa","gfgf","vou@ogo.com");
        Role r = new Role("fqfq","fgfg");
        AssignmentMetadata m2 = new AssignmentMetadata("qqqq", LocalDateTime.now().toString(),"dfdf");
        RoleAssignment u1 = new TemporaryAssignment(v,r,m2);
        manager.add(u1);
        User v1 = new User("gggq","gfgf","vou@ogo.com");
        Role r1 = new Role("ogo11","fgfgq");
        AssignmentMetadata m1 = new AssignmentMetadata("qqqq", LocalDateTime.now().toString(),"dfdf");
        PermanentAssignment u2 = new PermanentAssignment(v1,r1,m1);
        manager.add(u2);
        u2.revoke();
    }

    @Test
    public void userManager_findByUser_Returns() {
        List<RoleAssignment> ogo = manager.findByUser(v);
        assertEquals(1,ogo.size());
        assertEquals(manager.findByUser(v).get(0).user(), v);
    }


    @Test
    public void userManager_count_Returns() {
        assertEquals(3, manager.count());
    }

    @Test
    public void userManager_findAll_Returns() {
        List<RoleAssignment> a = manager.findAll();
        assertEquals(3, a.size());
    }

    @Test
    public void userManager_findAllFilter_Returns() {
        List<RoleAssignment> a = manager.findAll(AssignmentFilters.byRoleName("fqfq"), AssignmentSorters.byUsername());
        assertEquals(2, a.size());
        assertEquals("qqqa", a.get(0).user().username());
        assertEquals("qqqq", a.get(1).user().username());
    }

    @Test
    public void userManager_Clear() {
        manager.clear();
        assertEquals(0, manager.count());
    }

    @Test
    public void userManager_getActiveAssignments() {
        assertEquals(1,manager.getActiveAssignments().size());
    }

    @Test
    public void userManager_getExpiredAssignments() {
         assertEquals(1,manager.getExpiredAssignments().size());
    }

}