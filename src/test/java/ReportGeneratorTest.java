import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ReportGeneratorTest {
    public ReportGenerator gen = new ReportGenerator();
    public UserManager uman;
    public RoleManager rman;
    public AssignmentManager aman;

    private User mockUser(String username, String fullName, String email) {
        User v = Mockito.mock(User.class);//new User("qqqa","gfgf","vou@ogo.com");
        Mockito.when(v.email()).thenReturn(email);
        Mockito.when(v.fullName()).thenReturn(fullName);
        Mockito.when(v.username()).thenReturn(username);
        Mockito.when(v.format()).thenReturn(String.format("%s (%s) <%s>",username,fullName,email));
        return v;
    }

    private Role mockRole(String name, String description, String id) {
        Role v = Mockito.mock(Role.class);
        v.name = name;
        v.description = description;
        v.id = id;
        Mockito.when(v.format()).thenReturn(String.format("Role: %s [ID: %s]\nDescription: %s\nPermissions:",name,id,
                description));
        //Mockito.when(v.).thenReturn(name);
        //Mockito.when(v.description).thenReturn(description);
        return v;
    }

    private Permission mockPermission(String name, String resource, String description) {
        Permission v = Mockito.mock(Permission.class);
        Mockito.when(v.resource()).thenReturn(resource);
        Mockito.when(v.name()).thenReturn(name);
        Mockito.when(v.description()).thenReturn(description);
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
        RoleAssignment v = Mockito.mock(RoleAssignment.class);
        Mockito.when(v.role()).thenReturn(r);
        Mockito.when(v.user()).thenReturn(u);
        Mockito.when(v.metadata()).thenReturn(m);
        return v;
    }

    private PermanentAssignment mockPermanentAssignment(User u, Role r, AssignmentMetadata m, boolean revoked,String assignmentId) {
        PermanentAssignment v = Mockito.mock(PermanentAssignment.class);
        Mockito.when(v.role()).thenReturn(r);
        Mockito.when(v.user()).thenReturn(u);
        Mockito.when(v.metadata()).thenReturn(m);
        Mockito.when(v.isRevoked()).thenReturn(revoked);
        Mockito.when(v.assignmentType()).thenReturn("PERMANENT");
        Mockito.when(v.assignmentId()).thenReturn(assignmentId);
        return v;
    }

    private TemporaryAssignment mockTemporaryAssignment(User u, Role r, AssignmentMetadata m, boolean expired, String assignmentId) {
        TemporaryAssignment v = Mockito.mock(TemporaryAssignment.class);
        Mockito.when(v.role()).thenReturn(r);
        Mockito.when(v.user()).thenReturn(u);
        Mockito.when(v.metadata()).thenReturn(m);
        Mockito.when(v.isExpired()).thenReturn(expired);
        Mockito.when(v.isActive()).thenReturn(!expired);
        Mockito.when(v.assignmentType()).thenReturn("TEMPORARY");
        Mockito.when(v.assignmentId()).thenReturn(assignmentId);
        return v;
    }

    private UserManager mockUserManager() {
        UserManager v = Mockito.mock(UserManager.class);
        User u = mockUser("user1","fullname","email");
        Mockito.when(v.findById("user1")).thenReturn(Optional.of(u));
        ArrayList<User> arr = new ArrayList<User>();
        arr.add(u);
        Mockito.when(v.findAll()).thenReturn(arr);
        return v;
    }

    private RoleManager mockRoleManager() {
        RoleManager v = Mockito.mock(RoleManager.class);
        Role r = mockRole("role1","fuldsf", "1");
        Mockito.when(v.findById("1")).thenReturn(Optional.of(r));
        ArrayList<Role> arr = new ArrayList<Role>();
        arr.add(r);
        Mockito.when(v.findAll()).thenReturn(arr);
        return v;
    }

    private AssignmentManager mockAssignmentManager(User u, Role r1) {
        AssignmentManager v = Mockito.mock(AssignmentManager.class);
        PermanentAssignment r = mockPermanentAssignment(u, r1,
                mockAssignmentMetadata("ogo",DateUtils.getCurrentDate(),"give me the reason"),
                false,"1");
        Mockito.when(v.findById(("1"))).thenReturn(Optional.of(r));
        ArrayList<RoleAssignment> uf = new ArrayList<RoleAssignment>();
        uf.add(r);
        Mockito.when(v.findByUser(u)).thenReturn(uf);
        Mockito.when(v.findByRole(r1)).thenReturn(uf);
        Mockito.when(v.findAll()).thenReturn(uf);
        Mockito.when(r.user()).thenReturn(u);
        Mockito.when(r.role()).thenReturn(r1);
        return v;
    }

    @BeforeEach
    public void setUp() {
        this.uman = mockUserManager();
        this.rman = mockRoleManager();
        this.aman = mockAssignmentManager(uman.findById("user1").get(),rman.findById("1").get());
    }

    @Test
    public void reportGenerator_generateUserReport() {
        String res = gen.generateUserReport(uman,aman);
        assertEquals(res,"user1 (fullname) <email>\n"+ "Roles: Role: role1 [ID: 1]\n"+
        "Description: fuldsf\n" +
        "Permissions:\n");
    }

    @Test
    public void reportGenerator_generateUserReportParallel() {
        IO.println("ogo");
        String res = gen.generateUserReportParallel(uman,aman);
        assertEquals(res,"user1 (fullname) <email>\n"+ "Roles: Role: role1 [ID: 1]\n"+
                "Description: fuldsf\n" +
                "Permissions:\n");
    }

    @Test
    public void reportGenerator_generateRoleReport() {
        String res = gen.generateRoleReport(rman,aman);
        assertEquals(res,"Role: role1 [ID: 1]\n"+
        "Description: fuldsf\n" +
        "Permissions:\n"+
        "amount of users: 1\n\n");
    }

    @Test
    public void reportGenerator_generatePermissionMatrix() {
        HashSet<Permission> uf = new HashSet<Permission>();
        Permission p = mockPermission("name","r","d");
        uf.add(p);
        Mockito.when(rman.findById("1").get().getPermissions()).thenReturn(uf);
        String res = gen.generatePermissionMatrix(uman,aman);
        assertEquals(res,"r\t\n"+
                "user1\t+\t");
    }

    @Test
    public void reportGenerator_generatePermissionMatrixParallel() {
        HashSet<Permission> uf = new HashSet<Permission>();
        Permission p = mockPermission("name","r","d");
        uf.add(p);
        Mockito.when(rman.findById("1").get().getPermissions()).thenReturn(uf);
        String res = gen.generatePermissionMatrixParallel(uman,aman);
        assertEquals(res,"r\t\n"+
                "user1\t+\t");
    }
}
