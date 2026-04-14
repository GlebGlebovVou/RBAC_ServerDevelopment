import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ReportGenerator {
    public static String generateUserReport(UserManager userManager, AssignmentManager assignmentManager) {
        StringBuilder f = new StringBuilder();
        for(User x : userManager.findAll()) {
            f.append(x.format());
            f.append("\nRoles: ");
            List<RoleAssignment> a = assignmentManager.findByUser(x);
            for(RoleAssignment r : a) {
                f.append(r.role().format());
                f.append("\n");
            }
        }
        return f.toString();
    }
    public static String generateRoleReport(RoleManager roleManager, AssignmentManager assignmentManager) {
        StringBuilder f = new StringBuilder();
        for(Role x : roleManager.findAll()) {
            f.append(x.format());
            List<RoleAssignment> b = assignmentManager.findByRole(x);
            List<User> u = new ArrayList<User>();
            int count = 0;
            for(RoleAssignment a : b) {
                if(!u.contains(a.user())) {
                    u.add(a.user());
                    count += 1;
                }
            }
            f.append(String.format("\namount of users: %d\n\n",count));
        }
        return f.toString();
    }
    public static String generatePermissionMatrix(UserManager userManager, AssignmentManager assignmentManager) {
        StringBuilder f = new StringBuilder();
        HashSet<String> resources = new HashSet<String>();
        List<Role> roles = new ArrayList<Role>();
        for(RoleAssignment a : assignmentManager.findAll()) {
            if (!roles.contains(a.role())) {
                for (Permission g : a.role().getPermissions()) {
                    if(!resources.contains(g.resource())) {
                        f.append(String.format("%s\t",g.resource()));
                    }
                    resources.add(g.resource());
                }
                roles.add(a.role());
            }
        }
        for(User i : userManager.findAll()) {
            f.append("\n");
            f.append(i.username()).append("\t");
            HashSet<String> res = new HashSet<String>();
            List<RoleAssignment> r  = assignmentManager.findByUser(i);
            for(RoleAssignment u : r) {
                for(Permission o : u.role().getPermissions()) {
                    res.add(o.resource());
                }
            }
            for(String l : resources) {
                f.append(res.contains(l) ? "+\t" : "-\t");
            }
        }
        return f.toString();
    }
    public static void exportToFile(String report, String filename) throws IOException {
        try(FileWriter w = new FileWriter(filename)) {
            w.write(report);
        }
        catch(IOException e) {
            throw new IOException();
        }
    }
    public static String generateUserReportParallel(UserManager userManager, AssignmentManager assignmentManager) {
        StringBuilder f = new StringBuilder();
        userManager.findAll().parallelStream().forEach((User x) -> {
            f.append(x.format());
            f.append("\nRoles: ");
            List<RoleAssignment> a = assignmentManager.findByUser(x);
            for(RoleAssignment r : a) {
                f.append(r.role().format());
                f.append("\n");
            }
        });
        return f.toString();
    }
    public static String generatePermissionMatrixParallel(UserManager userManager, AssignmentManager assignmentManager) {
        StringBuilder f = new StringBuilder();
        HashSet<String> resources = new HashSet<String>();
        List<Role> roles = new ArrayList<Role>();
        assignmentManager.findAll().parallelStream().forEach((RoleAssignment a) -> {
            if (!roles.contains(a.role())) {
                a.role().getPermissions().parallelStream().forEach((Permission g) -> {
                    if(!resources.contains(g.resource())) {
                        f.append(String.format("%s\t",g.resource()));
                    }
                    resources.add(g.resource());
                });
                roles.add(a.role());
            }
        });
        userManager.findAll().parallelStream().forEach((User i) -> {
            f.append("\n");
            f.append(i.username()).append("\t");
            HashSet<String> res = new HashSet<String>();
            List<RoleAssignment> r  = assignmentManager.findByUser(i);
            r.parallelStream().forEach((RoleAssignment u) -> {
                u.role().getPermissions().parallelStream().forEach((Permission o) -> {
                    res.add(o.resource());
                });
            }
            );
            for(String l : resources) {
                f.append(res.contains(l) ? "+\t" : "-\t");
            }
        });
        return f.toString();
    }
}
