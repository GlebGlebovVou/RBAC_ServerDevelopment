import java.io.Console;
import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

public class CommandRegistry {
    static public void register() {
        registerUserControl();
        registerRoleControl();
        registerAssignmentControl();
        registerPermissionsView();
        registerReportView();
        logCommands();
        systemCommands();
    }

    static public Command user_list() {
        return (scanner,system,args)-> {
            List<User> listOfUsers = system.getUserManager().findAll();
            if(args != null && args.length > 1) {
                String filterName = args[0];
                String filterKey = args[1];
                switch(filterName) {
                    case "username":
                        listOfUsers = system.getUserManager().findByFilter(UserFilters.byUsernameContains(filterKey));
                    case "email":
                        listOfUsers = system.getUserManager().findByFilter(UserFilters.byEmail(filterKey));
                    case "emailDomain":
                        listOfUsers = system.getUserManager().findByFilter(UserFilters.byEmailDomain(filterKey));
                    case "fullname":
                        listOfUsers = system.getUserManager().findByFilter(UserFilters.byFullNameContains(filterKey));
                }
            }
            ArrayList<String[]> users = new ArrayList<String[]>();
            String[] names = {"username","full name", "email"};
            for(User u : listOfUsers) {
                String[] arr = {u.username(),u.fullName(),u.email()};
                users.add(arr);
            }
            IO.println(FormatUtils.formatTable(names,users));
        };
    }

    static public Command user_create() {
        return (scanner,system,args)-> {
            String username = ConsoleUtils.promptString(scanner,"Input username: ",true);
            String fullname = ConsoleUtils.promptString(scanner,"Input full name: ",false);
            String email = ConsoleUtils.promptString(scanner,"Input email: ",true);
            int old_size = system.getUserManager().count();
            User u = User.validate(username,fullname,email);
            if(u == null) {
                IO.println("Validation error");
                return;
            }
            system.getUserManager().add(User.validate(username,fullname,email));
            if(system.getUserManager().count() == old_size) {
                IO.println("This user is probably already contains; try user-update");
            }
        };
    }

    static public Command user_view() {
        return (scanner,system,args)-> {
            String username = ConsoleUtils.promptString(scanner,"Input username: ",true);
            Optional<User> u = system.getUserManager().findById(username);
            if(u.isEmpty()) {
                IO.println("No such user");
                return;
            }
            User user = u.get();
            IO.println(user.format());
            ArrayList<Role> roles = new ArrayList<Role>();
            ArrayList<Permission> permissions = new ArrayList<Permission>();
            for(RoleAssignment o : system.getAssignmentManager().findByUser(user)) {
                if(!roles.contains(o.role()) && o.isActive()) {
                    roles.add(o.role());
                    for(Permission p : o.role().getPermissions()) {
                        if(!permissions.contains(p)) {
                            permissions.add(p);
                        }
                    }
                }
            }
            IO.println("Roles:");
            for(Role r : roles) {
                IO.println(r.format());
            }
            IO.println("Permissions of user:");
            for(Permission p : permissions) {
                IO.println(p.format());
            }
        };
    }


    static public Command user_update() {
        return (scanner,system,args)-> {
            String username = ConsoleUtils.promptString(scanner,"Input username: ",true);
            Optional<User> u = system.getUserManager().findById(username);
            if(u.isEmpty()) {
                IO.println("No such user");
                return;
            }
            User user = u.get();
            IO.println(user.format());
            String fullname = ConsoleUtils.promptString(scanner,"Input new full name: ",false);
            String email = ConsoleUtils.promptString(scanner,"Input new email: ",true);
            system.getUserManager().update(username,fullname,email);
        };
    }

    static public Command user_delete() {
        return (scanner,system,args)-> {
            String username = ConsoleUtils.promptString(scanner,"Input username: ",true);
            Optional<User> u = system.getUserManager().findById(username);
            if(u.isEmpty()) {
                IO.println("No such user");
                return;
            }
            if(!ConsoleUtils.promptYesNo(scanner,"Are you sure? (yes/no): ")) {
                return;
            }
            User user = u.get();
            for(RoleAssignment o : system.getAssignmentManager().findByUser(user)) {
                system.getAssignmentManager().remove(o);
            }
            system.getUserManager().remove(user);
        };
    }

    static public Command user_search() {
        return (scanner,system,args)-> {
            ArrayList<String> filters = new ArrayList<String>();
            filters.add("username");
            filters.add("email");
            filters.add("emailDomain");
            filters.add("fullname");
            String filterName = ConsoleUtils.promptChoice(scanner,"Select search type: ",filters);
            String filterKey = ConsoleUtils.promptString(scanner,"Input key: ",true);
            IO.print(filterName);
            IO.println(filterKey);
            List<User> listOfUsers = switch (filterName) {
                case "username" -> system.getUserManager().findByFilter(UserFilters.byUsernameContains(filterKey));
                case "email" -> system.getUserManager().findByFilter(UserFilters.byEmail(filterKey));
                case "emailDomain" -> system.getUserManager().findByFilter(UserFilters.byEmailDomain(filterKey));
                case "fullname" -> system.getUserManager().findByFilter(UserFilters.byFullNameContains(filterKey));
                default -> null;
            };
            assert listOfUsers != null;
            for(User u : listOfUsers) {
                IO.println(FormatUtils.formatBox(u.format()));
            }
        };
    }

    static public void registerUserControl() {
        CommandParser.registerCommand("user-list","Print list of all users",user_list());
        CommandParser.registerCommand("user-create","Create user via console input",user_create());
        CommandParser.registerCommand("user-view","View all info about user through his username",user_view());
        CommandParser.registerCommand("user-update","Update user's full name and email",user_update());
        CommandParser.registerCommand("user-delete","Delete user from manager's",user_delete());
        CommandParser.registerCommand("user-search","Search users by some filters",user_search());
    }

    static public Command role_list() {
        return (scanner,system,args)-> {
            IO.println(ReportGenerator.generateRoleReport(system.getRoleManager(), system.getAssignmentManager()));
        };
    }

    static public Command role_create() {
        return (scanner,system,args)-> {
            String name = ConsoleUtils.promptString(scanner,"Input name of role: ",true);
            String desc = ConsoleUtils.promptString(scanner,"Input description of role: ",true);
            int old_size = system.getRoleManager().count();
            Role role = new Role(name,desc);
            system.getRoleManager().add(role);
            if(old_size == system.getRoleManager().count()) {
                IO.println("Role wasn't added");
                return;
            }
            while(true) {
                String res = ConsoleUtils.promptString(scanner,"Input permission name: (or empty for skip)",false);
                if(res.isEmpty()) {
                    break;
                }
                String name1 = ConsoleUtils.promptString(scanner,"Input permission name",true);
                String resource = ConsoleUtils.promptString(scanner,"Input permission resource",true);
                String description = ConsoleUtils.promptString(scanner,"Input permission description",true);
                role.addPermission(new Permission(name1,resource,description));
            }
        };
    }

    static public Command role_view() {
        return (scanner,system,args)-> {
            String name = ConsoleUtils.promptString(scanner,"Input role name: ",false);
            Optional<Role> role = system.getRoleManager().findByName(name);
            if(role.isEmpty()) {
                IO.println("No such role");
                return;
            }
            Role r = role.get();
            IO.println(r.format());
        };
    }

    static public Command role_update() {
        return (scanner,system,args)-> {
            String name = ConsoleUtils.promptString(scanner,"Input role name: ",false);
            Optional<Role> role = system.getRoleManager().findByName(name);
            if(role.isEmpty()) {
                IO.println("No such role");
                return;
            }
            Role r = role.get();
            system.getRoleManager().remove(r);
            r.name = ConsoleUtils.promptString(scanner,"Input new role name: ",true);
            r.description = ConsoleUtils.promptString(scanner,"Input new role description: ",true);
            system.getRoleManager().add(r);
        };
    }

    static public Command role_delete() {
        return (scanner,system,args)-> {
            String name = ConsoleUtils.promptString(scanner,"Input role name: ",false);
            Optional<Role> role = system.getRoleManager().findByName(name);
            if(role.isEmpty()) {
                IO.println("No such role");
                return;
            }
            Role r = role.get();
            system.getRoleManager().remove(r);
        };
    }

    static public Command role_add_permission() {
        return (scanner,system,args)-> {
            String name = ConsoleUtils.promptString(scanner,"Input role name: ",false);
            Optional<Role> role = system.getRoleManager().findByName(name);
            if(role.isEmpty()) {
                IO.println("No such role");
                return;
            }
            Role r = role.get();
            String name1 = ConsoleUtils.promptString(scanner,"Input permission name",true);
            String resource = ConsoleUtils.promptString(scanner,"Input permission resource",true);
            String description = ConsoleUtils.promptString(scanner,"Input permission description",true);
            Permission p = new Permission(name1,resource,description);
            system.getRoleManager().addPermissionToRole(name,p);
        };
    }

    static public Command role_remove_permission() {
        return (scanner,system,args)-> {
            String name = ConsoleUtils.promptString(scanner,"Input role name: ",false);
            Optional<Role> role = system.getRoleManager().findByName(name);
            if(role.isEmpty()) {
                IO.println("No such role");
                return;
            }
            Role r = role.get();
            List<Permission> p = r.getPermissions().stream().toList().stream().sorted((p1, p2) -> p1.name().compareTo(p2.name())).toList();
            //system.getRoleManager().remove(r);
            Permission ans = ConsoleUtils.promptChoice(scanner,"Chose permission: ",p);
            r.removePermission(ans);
        };
    }

    static public Command role_search() {
        return (scanner,system,args)-> {
            List<String> vars = new ArrayList<String>();
            vars.add("nameContains");
            vars.add("permission");
            vars.add("minimalAmountOfPermission");
            List<Role> roles = new ArrayList<Role>();
            String name = ConsoleUtils.promptChoice(scanner,"Choice filter: ",vars);
            String key;
            if(args != null && args.length > 0) {
                key = args[0];
            }
            else {
                key = ConsoleUtils.promptString(scanner,"Input key: ",false);
            }
            roles = switch (name) {
                case "nameContains" -> system.getRoleManager().findByFilter(RoleFilters.byNameContains(key));
                case "permission" -> {
                    String resource = ConsoleUtils.promptString(scanner, "Input key (description): ", false);
                    yield system.getRoleManager().findByFilter(RoleFilters.hasPermission(key, resource));
                }
                case "minimalAmountOfPermission" -> {
                    int key1 = Integer.parseInt(key);
                    yield system.getRoleManager().findByFilter(RoleFilters.hasAtLeastNPermissions(key1));
                }
                default -> roles;
            };
            for(Role r : roles) {
                IO.println(r.format());
            }
        };
    }



    static public void registerRoleControl() {
        CommandParser.registerCommand("role-list","Print all roles",role_list());
        CommandParser.registerCommand("role-create","Create new role",role_create());
        CommandParser.registerCommand("role-view","Display information about role",role_view());
        CommandParser.registerCommand("role-update","Update information about role",role_update());
        CommandParser.registerCommand("role-delete","Delete role",role_delete());
        CommandParser.registerCommand("role-add-permission","Add permission to role",role_add_permission());
        CommandParser.registerCommand("role-remove-permission","Add permission to role",role_remove_permission());
        CommandParser.registerCommand("role-search","Search roles by filters",role_search());
    }

    static public Command assign_role() {
        return (scanner,system,args)-> {
            String username = ConsoleUtils.promptString(scanner,"Input username: ",true);
            Optional<User> userT = system.getUserManager().findById(username);
            if(userT.isEmpty()) {
                IO.println("No such user");
                return;
            }
            User user = userT.get();
            List<Role> roles = system.getRoleManager().findAll();
            Role role = ConsoleUtils.promptChoice(scanner,"Choice role: ",roles);
            List<String> types = new ArrayList<String>();
            RoleAssignment assignment = null;
            types.add("Permanent");
            types.add("Temporary");
            String type = ConsoleUtils.promptChoice(scanner,"Chose type: ",types);
            String reason = ConsoleUtils.promptString(scanner,"Describe reason: ",false);
            AssignmentMetadata metadata = new AssignmentMetadata(system.getCurrentUser(), DateUtils.getCurrentDateTime(),
                    reason);
            assignment = switch (type) {
                case "Permanent" -> new PermanentAssignment(user, role, metadata);
                case "Temporary" -> new TemporaryAssignment(user, role, metadata);
                default -> assignment;
            };
            system.getAssignmentManager().add(assignment);
        };
    }

    static public Command revoke_role() {
        return (scanner,system,args)-> {
            String username = ConsoleUtils.promptString(scanner,"Input username: ",true);
            Optional<User> userT = system.getUserManager().findById(username);
            if(userT.isEmpty()) {
                IO.println("No such user");
                return;
            }
            User user = userT.get();
            List<RoleAssignment> assignments = system.getAssignmentManager().findByUser(user);
            RoleAssignment assignment = ConsoleUtils.promptChoice(scanner,"Choice assignment",assignments);
            if(assignment instanceof PermanentAssignment perm) {
                perm.revoke();
            }
            else if (assignment instanceof TemporaryAssignment temp) {
                temp.extend(DateUtils.getCurrentDateTime());
            }
        };
    }

    static public Command assignment_list() {
        return (scanner,system,args)-> {
            String[] headers = {"username","role","type","status","assigned at"};
            List<String[]> rows = new ArrayList<String[]>();
            for(RoleAssignment a : system.getAssignmentManager().findAll()) {
                String[] o = {a.user().username(),a.role().name,a.assignmentType(),a.isActive() ? "Active" : "Inactive",a.metadata().assignedAt()};
                rows.add(o);
            }
            IO.println(FormatUtils.formatTable(headers,rows));
        };
    }

    static public Command assignment_list_user() {
        return (scanner,system,args)-> {
            String username = ConsoleUtils.promptString(scanner,"Input username: ",true);
            Optional<User> userT = system.getUserManager().findById(username);
            if(userT.isEmpty()) {
                return;
            }
            User user = userT.get();
            String[] headers = {"username","role","type","status","assigned at"};
            List<String[]> rows = new ArrayList<String[]>();
            for(RoleAssignment a : system.getAssignmentManager().findByUser(user)) {
                String[] o = {a.user().username(),a.role().name,a.assignmentType(),a.isActive() ? "Active" : "Inactive",a.metadata().assignedAt()};
                rows.add(o);
            }
            IO.println(FormatUtils.formatTable(headers,rows));
        };
    }

    static public Command assignment_list_role() {
        return (scanner,system,args)-> {
            String name = ConsoleUtils.promptString(scanner,"Input name of role: ",true);
            Optional<Role> roleT = system.getRoleManager().findByName(name);
            if(roleT.isEmpty()) {
                return;
            }
            Role role = roleT.get();
            List<User> users = new ArrayList<User>();
            List<RoleAssignment> assignments = system.getAssignmentManager().findByRole(role);
            for(RoleAssignment a : assignments) {
                if(!users.contains(a.user())) {
                    users.add(a.user());
                    IO.println(a.user().format());
                }
            }
        };
    }

    static public Command assignment_active() {
        return (scanner,system,args)-> {
            for(RoleAssignment a : system.getAssignmentManager().getActiveAssignments()) {
                if(a instanceof PermanentAssignment) {
                    IO.println(((PermanentAssignment)a).summary());
                }
                else if(a instanceof TemporaryAssignment) {
                    IO.println(((TemporaryAssignment)a).summary());
                }
            }
        };
    }

    static public Command assignment_expired() {
        return (scanner,system,args)-> {
            for(RoleAssignment a : system.getAssignmentManager().getExpiredAssignments()) {
                if(a instanceof PermanentAssignment) {
                    IO.println(((PermanentAssignment)a).summary());
                }
                else if(a instanceof TemporaryAssignment) {
                    IO.println(((TemporaryAssignment)a).summary());
                }
            }
        };
    }

    static public Command assignment_extend() {
        return (scanner,system,args)-> {
            String id = null;
            String username = null,roleName = null;
            RoleAssignment assignment = null;
            if(ConsoleUtils.promptYesNo(scanner,"Input id?(yes/no): ")) {
                id = ConsoleUtils.promptString(scanner,"Input id: ",true);
                Optional<RoleAssignment> assignmentT = system.getAssignmentManager().findById(id);
                if(assignmentT.isEmpty()) {
                    return;
                }
                assignment = assignmentT.get();
            }
            else {
                username = ConsoleUtils.promptString(scanner,"Input username: ",true);
                roleName = ConsoleUtils.promptString(scanner,"Input role name: ",true);
                List<RoleAssignment> assignmentT = system.getAssignmentManager().findByFilter(
                        AssignmentFilters.byRoleName(roleName).and(AssignmentFilters.byUsername(username)));
                if(assignmentT.isEmpty()) {
                    return;
                }
                assignment = assignmentT.getFirst();
            }
            if(assignment instanceof PermanentAssignment) {
                return;
            }
            TemporaryAssignment a = (TemporaryAssignment)assignment;
            a.extend(ConsoleUtils.promptString(scanner,"Input new date: ",true));
        };
    }

    static public Command assignment_search() {
        return (scanner,system,args)-> {
            List<String> ogo = new ArrayList<String>();
            ogo.add("user");
            ogo.add("role");
            ogo.add("type");
            ogo.add("status");
            ogo.add("assigned after");
            ogo.add("expiring before");
            String choice = ConsoleUtils.promptChoice(scanner,"Choice filter: ",ogo);
            String key = ConsoleUtils.promptString(scanner,"Input key: ",true);
            List<RoleAssignment> assignments = null;
            assignments = switch(choice) {
                case "user" -> {
                    User u = system.getUserManager().findById(key).get();
                    yield system.getAssignmentManager().findByUser(u);
                }
                case "role" -> {
                    Role r = system.getRoleManager().findByName(key).get();
                    yield system.getAssignmentManager().findByRole(r);
                }
                case "type" ->
                    system.getAssignmentManager().findByFilter(AssignmentFilters.byType(key.toUpperCase(Locale.ROOT)));

                case "status" -> {
                    if(key.toLowerCase(Locale.ROOT).equals("active")) {
                        yield system.getAssignmentManager().findByFilter(AssignmentFilters.activeOnly());
                    }
                    else {
                        yield system.getAssignmentManager().findByFilter(AssignmentFilters.inactiveOnly());
                    }
                }
                case "assigned after" ->
                    system.getAssignmentManager().findByFilter(AssignmentFilters.assignedAfter(key));

                case "expiring before" ->
                    system.getAssignmentManager().findByFilter(AssignmentFilters.expiringBefore(key));
                default -> assignments;
            };
            assert assignments != null;
            for(RoleAssignment a : assignments) {
                if(a instanceof PermanentAssignment) {
                    IO.println(((PermanentAssignment)a).summary());
                }
                else if(a instanceof TemporaryAssignment) {
                    IO.println(((TemporaryAssignment)a).summary());
                }
            }
        };
    }

    static public void registerAssignmentControl() {
        CommandParser.registerCommand("assign-role","Assign role to user",assign_role());
        CommandParser.registerCommand("revoke-role","Revoke role assignment for user",revoke_role());
        CommandParser.registerCommand("assignment-list","Print table of all assignments",assignment_list());
        CommandParser.registerCommand("assignment-list-user","Print table of all assignments from some user",assignment_list_user());
        CommandParser.registerCommand("assignment-list-role","Print all users with role",assignment_list_role());
        CommandParser.registerCommand("assignment-active","Print all active assignments",assignment_active());
        CommandParser.registerCommand("assignment-expired","Print all expired assignments",assignment_expired());
        CommandParser.registerCommand("assignment-extend","Extend assignment",assignment_extend());
        CommandParser.registerCommand("assignment-search","Search assignments by filter",assignment_search());
    }

    static public Command permissions_user() {
        return (scanner,system,args)-> {
            String username = ConsoleUtils.promptString(scanner,"Input username: ",true);
            Optional<User> uT = system.getUserManager().findById(username);
            if(uT.isEmpty()) {
                return;
            }
            User user = uT.get();
            HashMap<String, List<Permission>> permissions = new HashMap<String, List<Permission>>();
            for(RoleAssignment a : system.getAssignmentManager().findByUser(user)) {
                List<Permission> permissions1 = a.role().getPermissions().stream().sorted((p1,p2) -> p1.name().compareTo(p2.name())).toList();
                for(Permission p : permissions1) {
                    if(!permissions.containsKey(p.resource())) {
                        ArrayList<Permission> permissions2 = new ArrayList<Permission>();
                        permissions2.add(p);
                        permissions.put(p.resource(),permissions2);
                    }
                    else {
                        permissions.get(p.resource()).add(p);
                    }
                }
            }
            for(String resource : permissions.keySet()) {
                IO.println(resource);
                for(Permission p : permissions.get(resource)) {
                    IO.println(p.format());
                }
            }

        };
    }

    static public Command permissions_check() {
        return (scanner,system,args)-> {
            String username = ConsoleUtils.promptString(scanner,"Input username: ",true);
            Optional<User> uT = system.getUserManager().findById(username);
            if(uT.isEmpty()) {
                return;
            }
            User user = uT.get();
            String name = ConsoleUtils.promptString(scanner,"Input permission name: ",true);
            String resource = ConsoleUtils.promptString(scanner,"Input permission resource: ",true);
            if(system.getAssignmentManager().userHasPermission(user,name,resource)) {
                List<RoleAssignment> uf = system.getAssignmentManager().findByUser(user);
                for(RoleAssignment u : uf) {
                    if(u.role().hasPermission(name,resource)) {
                        if(u instanceof PermanentAssignment) {
                            IO.println(((PermanentAssignment)u).summary());
                        }
                        else if(u instanceof TemporaryAssignment) {
                            IO.println(((TemporaryAssignment)u).summary());
                        }
                    }
                }
            }
            else {
                IO.println("No permission");
            }

        };
    }

    static public void registerPermissionsView() {
        CommandParser.registerCommand("permissions-user","Print all permissions from user",permissions_user());
        CommandParser.registerCommand("permissions-check","Check if user have permission",permissions_check());
    }

    static public Command help() {
        return (scanner,system,args)-> {
            CommandParser.printHelp();
        };
    }

    static public Command stats() {
        return (scanner,system,args)-> {
            String res = system.generateStatistics();
            IO.println(res);
            IO.println("----------");
            IO.println(String.format("amount of users: %d",system.getUserManager().count()));
            IO.println(String.format("amount of roles: %d",system.getRoleManager().count()));
            IO.println(String.format("amount of assignments: %d/%d/%d",system.getAssignmentManager().count(),
                    system.getAssignmentManager().getActiveAssignments().size(),
                    system.getAssignmentManager().getExpiredAssignments().size()));
            IO.println(String.format("medium amount of roles on user: %d",
                    system.getRoleManager().count()/system.getUserManager().count()));
            LinkedHashMap<Role, Integer> stat = new LinkedHashMap<Role,Integer>();
            for(User u : system.getUserManager().findAll()) {
                List<RoleAssignment> uf = system.getAssignmentManager().findByUser(u);
                for(RoleAssignment r : uf) {
                    if(stat.containsKey(r.role())) {
                        stat.put(r.role(),stat.get(r.role())+1);
                    }
                    else {
                        stat.put(r.role(), 1);
                    }
                }
            }
            LinkedHashMap<Role, Integer> sortedMap = stat.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue()).limit(3)
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            (oldValue, newValue) -> oldValue,
                            LinkedHashMap::new
                    ));
            int ind = 0;
            for(Role r : sortedMap.keySet()) {
                IO.println(String.format("%d. %s",ind+1,r.format()));
                ind += 1;
            }
        };
    }

    static public Command clear() {
        return (scanner,system,args)-> {
            System.out.print("\033[H\033[2J");
            System.out.flush();
        };
    }

    static public Command exit() {
        return (scanner,system,args)-> {
            if(ConsoleUtils.promptYesNo(scanner,"Are you sure?(yes/no): ")) {
                System.exit(0);
            }
        };
    }

    static public void systemCommands() {
        CommandParser.registerCommand("help","Print all commands",help());
        CommandParser.registerCommand("stats","Print main statistics",stats());
        CommandParser.registerCommand("clear","Clear screen",clear());
        CommandParser.registerCommand("exit","Finish program",exit());
    }

    static public Command audit_log() {
        return (scanner, system, args) -> {
            AuditLog.printLog();
        };
    }

    static public void logCommands() {
        CommandParser.registerCommand("audit-log", "Print audio-log", audit_log());
    }

    static public Command report_users() {
        return (scanner, system, args) -> {
            IO.println(ReportGenerator.generateUserReport(system.getUserManager(),system.getAssignmentManager()));
        };
    }

    static public Command report_roles() {
        return (scanner, system, args) -> {
            IO.println(ReportGenerator.generateRoleReport(system.getRoleManager(),system.getAssignmentManager()));
        };
    }

    static public Command report_matrix() {
        return (scanner, system, args) -> {
            IO.println(ReportGenerator.generatePermissionMatrix(system.getUserManager(),system.getAssignmentManager()));
        };
    }

    static public void registerReportView() {
        CommandParser.registerCommand("report-users","Create reports of users",report_users());
        CommandParser.registerCommand("report-users","Create reports of roles",report_roles());
        CommandParser.registerCommand("report-users","Create reports of users",report_matrix());
    }
}
