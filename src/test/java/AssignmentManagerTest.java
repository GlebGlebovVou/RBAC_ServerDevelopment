import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AssignmentManagerTest {

    private AssignmentManager manager;
    User v = new User("qqqq","gfgf","vou@ogo.com");
    Role r = new Role("fqfq","fgfg");
    AssignmentMetadata m = new AssignmentMetadata("qqqq", LocalDateTime.now().toString(),"dfdf");
    RoleAssignment u = new TemporaryAssignment(v,r,m);

    private User mockUser(String username, String fullName, String email) {
        User v = Mockito.mock(User.class);//new User("qqqa","gfgf","vou@ogo.com");
        Mockito.when(v.email()).thenReturn(email);
        Mockito.when(v.fullName()).thenReturn(fullName);
        Mockito.when(v.username()).thenReturn(username);
        return v;
    }

    private Role mockRole(String name, String description) {
        Role v = Mockito.mock(Role.class);
        Mockito.when(v.name).thenReturn(name);
        Mockito.when(v.description).thenReturn(description);
        return v;
    }

    private AssignmentMetadata mockAssignmentMetadata(String assignedBy, String assignedAt, String reason) {
        AssignmentMetadata v = Mockito.mock(AssignmentMetadata.class);
        Mockito.when(v.assignedAt()).thenReturn(assignedAt);
        Mockito.when(v.assignedBy()).thenReturn(assignedBy);
        Mockito.when(v.reason()).thenReturn(reason);
        return v;
    }

    private RoleAssignment mockAssignment(User u, Role r, AssignmentMetadata m) {
        RoleAssignment v = Mockito.mock(PermanentAssignment.class);
        Mockito.when(v.role()).thenReturn(r);
        Mockito.when(v.user()).thenReturn(u);
        Mockito.when(v.metadata()).thenReturn(m);
        return v;
    }

    private PermanentAssignment mockPermanentAssignment(User u, Role r, AssignmentMetadata m, boolean revoked) {
        PermanentAssignment v = (PermanentAssignment) mockAssignment(u,r,m);
        Mockito.when(v.user).thenReturn(u);
        Mockito.when(v.metadata).thenReturn(m);
        Mockito.when(v.revoked).thenReturn(revoked);
        Mockito.when(v.isRevoked()).thenReturn(revoked);
        return v;
    }

    private TemporaryAssignment mockTemporaryAssignment(User u, Role r, AssignmentMetadata m, boolean expired) {
        TemporaryAssignment v = (TemporaryAssignment) mockAssignment(u,r,m);;
        Mockito.when(v.isExpired()).thenReturn(expired);
        return v;
    }

    @BeforeEach
    public void setUp() {
        /*
        User v = new User("qqqq","gfgf","vou@ogo.com");
        Role r = new Role("fqfq","fgfg");
        AssignmentMetadata m = new AssignmentMetadata("qqqq", LocalDateTime.now().toString(),"dfdf");
        RoleAssignment u = new TemporaryAssignment(v,r,m);
         */
        Mockito.when(DateUtils.getCurrentDate()).thenReturn("2020-06-30");
        manager = new AssignmentManager();
        User v = mockUser("qqqq","qfqf","vou@ogo.com");
        Role r = mockRole("fqfq","fqfq");
        AssignmentMetadata m = mockAssignmentMetadata("qqqq",DateUtils.getCurrentDate(),"dfdf");
        manager.add(mockTemporaryAssignment(v,r,m,false));
        User v1 = mockUser("qqqa","gfgf","vou@ogo.com");
        Role r1 = mockRole("fqfq","fgfg");
        AssignmentMetadata m1 = mockAssignmentMetadata("qqqq", DateUtils.getCurrentDate(),"dfdf");
        manager.add(mockTemporaryAssignment(v,r,m,false));
        User v2 = mockUser("gggq","gfgf","vou@ogo.com");
        Role r2 = mockRole("ogo11","fgfgq");
        AssignmentMetadata m2 = mockAssignmentMetadata("qqqq", DateUtils.getCurrentDate(),"dfdf");
        manager.add(mockPermanentAssignment(v2,r2,m2,true));
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
        assertEquals(2,manager.getActiveAssignments().size());
    }

    @Test
    public void userManager_getExpiredAssignments() {
        assertEquals(0,manager.getExpiredAssignments().size());
    }

}