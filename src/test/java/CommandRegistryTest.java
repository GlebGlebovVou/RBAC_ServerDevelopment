import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.Mockito;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.Format;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class CommandRegistryTest {
    private ByteArrayOutputStream uf = new ByteArrayOutputStream();
    private final Scanner scanner = new Scanner(System.in);
    private final RBACSystem system = new RBACSystem();
    private final PrintStream origOut = System.out;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(uf));
        system.initialize();
    }

    @AfterEach
    void finish() {
        System.setOut(origOut);
    }

    @Test
    void commandregistry_register() {
        CommandRegistry.register();
        assertNotEquals(0, CommandParser.commands.size());
    }

    @Test
    void commandregistry_user_list() {
        CommandRegistry.user_list().execute(scanner,system,null);
        assertTrue(uf.toString().contains("*************************************************\n" +
                        "*     usernam...*     full na...*     email     *\n" +
                        "*************************************************\n" +
                        "*     admin     *     rootAdm...*     ogo@gma...*\n" +
                        "*************************************************\n"));
    }

    @Test
    void commandregistry_user_create() {
        ByteArrayInputStream ogo = new ByteArrayInputStream("userna\nname\nuf@u.com".getBytes());
        System.setIn(ogo);
        CommandRegistry.user_create().execute(new Scanner(System.in),system,null);
        assertTrue(uf.toString().contains("Input username: Input full name: Input email: "));
        assertEquals(2,system.getUserManager().count());
        ByteArrayInputStream ogo1 = new ByteArrayInputStream("u\nname\nuf@u.com".getBytes());
        uf.reset();
        System.setIn(ogo1);
        CommandRegistry.user_create().execute(new Scanner(System.in),system,null);
        assertTrue(uf.toString().contains("Input username: Input full name: Input email: username should have only digits, latin symbols or _ and be longer than 3 and shorter than 20"));
        assertTrue(uf.toString().contains("Validation error"));
        assertEquals(2,system.getUserManager().count());
    }

    @Test
    void commandregistry_user_view() {
        ByteArrayInputStream ogo = new ByteArrayInputStream("admin1".getBytes());
        System.setIn(ogo);
        CommandRegistry.user_view().execute(new Scanner(System.in),system,null);
        assertTrue(uf.toString().contains("Input username: No such user"));
        uf.reset();
        ByteArrayInputStream ogo1 = new ByteArrayInputStream("admin".getBytes());
        System.setIn(ogo1);
        CommandRegistry.user_view().execute(new Scanner(System.in),system,null);
        assertFalse(uf.toString().contains("Input username: No such user"));
        assertTrue(uf.toString().contains("Permissions (3)"));
        assertTrue(uf.toString().contains("Description: main user"));
        assertTrue(uf.toString().contains("Input username: admin (rootAdmin)"));
    }

    @Test
    void commandregistry_user_update() {
        ByteArrayInputStream ogo = new ByteArrayInputStream("admin1\nnice\ntest@g.com".getBytes());
        System.setIn(ogo);
        CommandRegistry.user_update().execute(new Scanner(System.in),system,null);
        assertTrue(uf.toString().contains("Input username: No such user"));
        uf.reset();
        ByteArrayInputStream ogo1 = new ByteArrayInputStream("admin\nnice\ntest@g.com".getBytes());
        System.setIn(ogo1);
        CommandRegistry.user_update().execute(new Scanner(System.in),system,null);
        assertFalse(uf.toString().contains("Input username: No such user"));
        assertTrue(system.getUserManager().findByUsername("admin").isPresent());
        assertEquals("admin (nice) <test@g.com>",system.getUserManager().findByUsername("admin").get().format());
    }

    @Test
    void commandregistry_user_delete() {
        ByteArrayInputStream ogo = new ByteArrayInputStream("admin1\nyes".getBytes());
        System.setIn(ogo);
        CommandRegistry.user_delete().execute(new Scanner(System.in),system,null);
        assertTrue(uf.toString().contains("Input username: No such user"));
        uf.reset();
        ByteArrayInputStream ogo1 = new ByteArrayInputStream("admin\nno".getBytes());
        System.setIn(ogo1);
        CommandRegistry.user_delete().execute(new Scanner(System.in),system,null);
        assertTrue(system.getUserManager().findById("admin").isPresent());
        ByteArrayInputStream ogo2 = new ByteArrayInputStream("admin\nyes".getBytes());
        System.setIn(ogo2);
        CommandRegistry.user_delete().execute(new Scanner(System.in),system,null);
        assertFalse(uf.toString().contains("Input username: No such user"));
        assertFalse(system.getUserManager().findByUsername("admin").isPresent());
    }

    @Test
    void commandregistry_user_search() {
        ByteArrayInputStream ogo = new ByteArrayInputStream("1\nadmin".getBytes());
        System.setIn(ogo);
        CommandRegistry.user_search().execute(new Scanner(System.in),system,null);
        assertTrue(uf.toString().contains("admin (rootAdmin) <ogo@gmail.com>"));
        assertTrue(uf.toString().contains("1. username"));
        assertTrue(uf.toString().contains("4. fullname"));
        uf.reset();
        ByteArrayInputStream ogo1 = new ByteArrayInputStream("3\nmmmmmm".getBytes());
        System.setIn(ogo1);
        CommandRegistry.user_search().execute(new Scanner(System.in),system,null);
        assertFalse(uf.toString().contains("admin (rootAdmin) <ogo@gmail.com>"));
        assertTrue(uf.toString().contains("1. username"));
        assertTrue(uf.toString().contains("4. fullname"));
    }

    @Test
    void commandregistry_role_list() {
        CommandRegistry.role_list().execute(new Scanner(System.in),system,null);
        assertTrue(uf.toString().contains("Role: Manager"));
        assertTrue(uf.toString().contains("Role: Admin"));
        assertTrue(uf.toString().contains("Role: Viewer"));
        assertTrue(uf.toString().contains("Description: can view"));
        assertTrue(uf.toString().contains("amount of users: 1"));
    }

    @Test
    void commandregistry_role_create() {
        ByteArrayInputStream ogo = new ByteArrayInputStream("someRole\ndescription\n".getBytes());
        System.setIn(ogo);
        CommandRegistry.role_create().execute(new Scanner(System.in),system,null);
        assertEquals(4,system.getRoleManager().count());
        assertTrue(system.getRoleManager().findByName("someRole").isPresent());
    }

    @Test
    void commandregistry_role_view() {
        ByteArrayInputStream ogo = new ByteArrayInputStream("admin1".getBytes());
        System.setIn(ogo);
        CommandRegistry.role_view().execute(new Scanner(System.in),system,null);
        assertTrue(uf.toString().contains("Input role name: No such role"));
        uf.reset();
        ByteArrayInputStream ogo1 = new ByteArrayInputStream("Admin".getBytes());
        System.setIn(ogo1);
        CommandRegistry.role_view().execute(new Scanner(System.in),system,null);
        assertTrue(uf.toString().contains("Permissions (3)"));
        assertTrue(uf.toString().contains("read on file: read file"));
        assertTrue(uf.toString().contains("Description: main user"));
    }

    @Test
    void commandregistry_role_update() {
        ByteArrayInputStream ogo = new ByteArrayInputStream("admin1\nname\ndesc".getBytes());
        System.setIn(ogo);
        CommandRegistry.role_update().execute(new Scanner(System.in),system,null);
        assertTrue(uf.toString().contains("Input role name: No such role"));
        uf.reset();
        ByteArrayInputStream ogo1 = new ByteArrayInputStream("Admin\nname\ndesc".getBytes());
        System.setIn(ogo1);
        CommandRegistry.role_update().execute(new Scanner(System.in),system,null);
        assertFalse(uf.toString().contains("Input role name: No such role"));
        assertTrue(system.getRoleManager().findByName("name").isPresent());
        assertFalse(system.getRoleManager().findByName("Admin").isPresent());
    }

    @Test
    void commandregistry_role_add_permission() {
        ByteArrayInputStream ogo = new ByteArrayInputStream("admin1".getBytes());
        System.setIn(ogo);
        CommandRegistry.role_add_permission().execute(new Scanner(System.in),system,null);
        assertTrue(uf.toString().contains("Input role name: No such role"));
        uf.reset();
        ByteArrayInputStream ogo1 = new ByteArrayInputStream("Admin\nperm\nres\ndesc".getBytes());
        System.setIn(ogo1);
        CommandRegistry.role_add_permission().execute(new Scanner(System.in),system,null);
        assertFalse(uf.toString().contains("Input role name: No such role"));
        assertTrue(system.getRoleManager().findByName("Admin").isPresent());
        assertEquals(4,system.getRoleManager().findByName("Admin").get().getPermissions().size());
        assertTrue(system.getRoleManager().findByName("Admin").get().hasPermission("perm","res"));
    }

    @Test
    void commandregistry_role_remove_permission() {
        ByteArrayInputStream ogo = new ByteArrayInputStream("admin1".getBytes());
        System.setIn(ogo);
        CommandRegistry.role_remove_permission().execute(new Scanner(System.in),system,null);
        assertTrue(uf.toString().contains("Input role name: No such role"));
        uf.reset();
        ByteArrayInputStream ogo1 = new ByteArrayInputStream("Admin\n1\n".getBytes());
        System.setIn(ogo1);
        assertTrue(system.getRoleManager().findByName("Admin").get().hasPermission("delete","file"));
        CommandRegistry.role_remove_permission().execute(new Scanner(System.in),system,null);
        assertFalse(uf.toString().contains("Input role name: No such role"));
        assertTrue(system.getRoleManager().findByName("Admin").isPresent());
        assertEquals(2,system.getRoleManager().findByName("Admin").get().getPermissions().size());
        assertFalse(system.getRoleManager().findByName("Admin").get().hasPermission("delete","file"));
    }

    @Test
    void commandregistry_role_search() {
        ByteArrayInputStream ogo = new ByteArrayInputStream("1\nad".getBytes());
        System.setIn(ogo);
        CommandRegistry.role_search().execute(new Scanner(System.in),system,null);
        assertTrue(uf.toString().contains("Role: Admin"));
        assertFalse(uf.toString().contains("Role: Viewer"));
        assertFalse(uf.toString().contains("Role: Manager"));
        ByteArrayInputStream ogo1 = new ByteArrayInputStream("2\ndelete\nfile".getBytes());
        System.setIn(ogo1);
        CommandRegistry.role_search().execute(new Scanner(System.in),system,null);
        assertTrue(uf.toString().contains("Role: Admin"));
        assertFalse(uf.toString().contains("Role: Viewer"));
        assertFalse(uf.toString().contains("Role: Manager"));
        ByteArrayInputStream ogo2 = new ByteArrayInputStream("3\n2\n".getBytes());
        System.setIn(ogo2);
        CommandRegistry.role_search().execute(new Scanner(System.in),system,null);
        assertTrue(uf.toString().contains("Role: Admin"));
        assertFalse(uf.toString().contains("Role: Viewer"));
        assertTrue(uf.toString().contains("Role: Manager"));
    }

    @Test
    void commandregistry_assign_role() {
        Role r = system.getRoleManager().findByName("Manager").get();
        User u = system.getUserManager().findById("admin").get();
        ByteArrayInputStream ogo = new ByteArrayInputStream("admin1\n".getBytes());
        System.setIn(ogo);
        CommandRegistry.assign_role().execute(new Scanner(System.in),system,null);
        assertTrue(uf.toString().contains("Input username: No such user"));
        ByteArrayInputStream ogo1 = new ByteArrayInputStream("admin\n2\n1\nwant".getBytes());
        System.setIn(ogo1);
        assertEquals(1,system.getAssignmentManager().findByUser(u).size());
        CommandRegistry.assign_role().execute(new Scanner(System.in),system,null);
        assertEquals(2,system.getAssignmentManager().findByUser(u).size());
    }

    @Test
    void commandregistry_revoke_role() {
        Role r = system.getRoleManager().findByName("Manager").get();
        User u = system.getUserManager().findById("admin").get();
        ByteArrayInputStream ogo = new ByteArrayInputStream("admin1\n".getBytes());
        System.setIn(ogo);
        CommandRegistry.assign_role().execute(new Scanner(System.in),system,null);
        assertTrue(uf.toString().contains("Input username: No such user"));
        ByteArrayInputStream ogo1 = new ByteArrayInputStream("admin\n1\n".getBytes());
        System.setIn(ogo1);
        assertEquals(1,system.getAssignmentManager().findByFilter(AssignmentFilters.activeOnly()).size());
        CommandRegistry.revoke_role().execute(new Scanner(System.in),system,null);
        assertEquals(0,system.getAssignmentManager().findByFilter(AssignmentFilters.activeOnly()).size());
    }

    @Test
    void commandregistry_assignment_list() {
        CommandRegistry.assignment_list().execute(scanner,system,null);
        assertTrue(uf.toString().contains("*     admin     *     Admin     *     PERMANE...*     Active    *"));
    }

    @Test
    void commandregistry_assignment_list_role() {
        ByteArrayInputStream ogo = new ByteArrayInputStream("Admin\n".getBytes());
        System.setIn(ogo);
        CommandRegistry.assignment_list_role().execute(new Scanner(System.in),system,null);
        assertTrue(uf.toString().contains("admin (rootAdmin) <ogo@gmail.com>"));
        uf.reset();
        ByteArrayInputStream ogo1 = new ByteArrayInputStream("Viewer\n".getBytes());
        System.setIn(ogo1);
        CommandRegistry.assignment_list_role().execute(new Scanner(System.in),system,null);
        assertFalse(uf.toString().contains("admin (rootAdmin) <ogo@gmail.com>"));
    }

    @Test
    void commandregistry_assignment_active() {
        CommandRegistry.assignment_active().execute(new Scanner(System.in),system,null);
        assertTrue(uf.toString().contains("[PERMANENT] Admin assigned to admin by admin at"));
    }

    @Test
    void commandregistry_assignment_expired() {
        CommandRegistry.assignment_expired().execute(new Scanner(System.in),system,null);
        assertFalse(uf.toString().contains("[PERMANENT] Admin assigned to admin by admin at"));
    }

    @Test
    void commandregistry_assignment_extend() {
        Role r = system.getRoleManager().findByName("Manager").get();
        User u = system.getUserManager().findById("admin").get();
        ByteArrayInputStream ogo = new ByteArrayInputStream("no\nadmin\nManager\n2050-05-05".getBytes());
        System.setIn(ogo);
        system.getAssignmentManager().add(new TemporaryAssignment(u,r,new AssignmentMetadata(u.username(),"1000-05-05","aaaa")));
        CommandRegistry.assignment_expired().execute(new Scanner(System.in),system,null);
        assertEquals(2,system.getAssignmentManager().findByFilter(AssignmentFilters.activeOnly()).size());
        assertTrue(system.getAssignmentManager().findByRole(r).getFirst().isActive());
    }

    @Test
    void commandregistry_assignment_search() {
        Role r = system.getRoleManager().findByName("Manager").get();
        User u = system.getUserManager().findById("admin").get();
        ByteArrayInputStream ogo = new ByteArrayInputStream("1\nadmin\n".getBytes());
        System.setIn(ogo);
        CommandRegistry.assignment_search().execute(new Scanner(System.in),system,null);
        assertTrue(uf.toString().contains("[PERMANENT] Admin assigned to admin by admin at"));
        uf.reset();
        ByteArrayInputStream ogo1 = new ByteArrayInputStream("2\nViewer\n".getBytes());
        System.setIn(ogo1);
        CommandRegistry.assignment_search().execute(new Scanner(System.in),system,null);
        assertFalse(uf.toString().contains("[PERMANENT] Admin assigned to admin by admin at"));
        uf.reset();
        ByteArrayInputStream ogo2 = new ByteArrayInputStream("3\nTemporary\n".getBytes());
        System.setIn(ogo2);
        CommandRegistry.assignment_search().execute(new Scanner(System.in),system,null);
        assertFalse(uf.toString().contains("[PERMANENT] Admin assigned to admin by admin at"));
        ByteArrayInputStream ogo3 = new ByteArrayInputStream("3\nPermanent\n".getBytes());
        System.setIn(ogo3);
        CommandRegistry.assignment_search().execute(new Scanner(System.in),system,null);
        assertTrue(uf.toString().contains("[PERMANENT] Admin assigned to admin by admin at"));
    }

    @Test
    void commandregistry_permissions_user() {
        ByteArrayInputStream ogo = new ByteArrayInputStream("admin\n".getBytes());
        System.setIn(ogo);
        CommandRegistry.permissions_user().execute(new Scanner(System.in),system,null);
        assertTrue(uf.toString().contains("Input username: file"));
        assertTrue(uf.toString().contains("delete on file: delete file"));
        assertTrue(uf.toString().contains("read on file: read file"));
        assertTrue(uf.toString().contains("write on file: write file"));
    }

    @Test
    void commandregistry_permissions_check() {
        ByteArrayInputStream ogo = new ByteArrayInputStream("admin\nwrite\nfile".getBytes());
        System.setIn(ogo);
        CommandRegistry.permissions_check().execute(new Scanner(System.in),system,null);
        assertFalse(uf.toString().contains("No permission"));
        ByteArrayInputStream ogo1 = new ByteArrayInputStream("admin\nwrit12e\n11".getBytes());
        System.setIn(ogo1);
        CommandRegistry.permissions_check().execute(new Scanner(System.in),system,null);
        assertTrue(uf.toString().contains("No permission"));
    }

    @Test
    void commandregistry_help() {
        CommandRegistry.help().execute(new Scanner(System.in),system,null);
        assertTrue(uf.toString().contains("Help"));
    }

    @Test
    void commandregistry_stats() {
        CommandRegistry.stats().execute(new Scanner(System.in),system,null);
        assertTrue(uf.toString().contains("amount of users: 1"));
        assertTrue(uf.toString().contains("amount of roles: 3"));
        assertTrue(uf.toString().contains("amount of assignments: 1/1/0"));
    }

    @Test
    void commandregistry_audit_log() {
        CommandRegistry.stats().execute(new Scanner(System.in),system,null);
        assertTrue(uf.toString().contains("admin (rootAdmin) <ogo@gmail.com>"));
        assertTrue(uf.toString().contains("Role: Admin [ID:"));
        assertTrue(uf.toString().contains("Role: Viewer ["));
    }
}
