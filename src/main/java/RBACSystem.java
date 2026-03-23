public class RBACSystem {
    private UserManager uman = new UserManager();
    private RoleManager rman = new RoleManager();
    private AssignmentManager aman = new AssignmentManager();
    private String currentUser;

    public UserManager getUserManager() {
        return uman;
    }

    public RoleManager getRoleManager() {
        return rman;
    }

    public AssignmentManager getAssignmentManager() {
        return aman;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public void initialize() {
        Permission[] permissions = new Permission[3];
        permissions[0] = new Permission("READ","file","read file");
        permissions[1] = new Permission("WRITE","file","write file");
        permissions[2] = new Permission("DELETE","file","delete file");
        Role admin = new Role("Admin","main user");
        Role manager = new Role("Manager","can do something");
        Role viewer = new Role("Viewer","can view");
        rman.add(admin);
        rman.add(manager);
        rman.add(viewer);
        for(Permission i : permissions) {
            admin.addPermission(i);
            if(!i.name().equals("delete")) {
                manager.addPermission(i);
                if(!i.name().equals("write")) {
                    viewer.addPermission(i);
                }
            }
        }
        User testAdmin = User.validate("admin","rootAdmin","ogo@gmail.com");
        uman.add(testAdmin);
        aman.add(new PermanentAssignment(testAdmin,admin,new AssignmentMetadata("admin",
                DateUtils.getCurrentDate(),"asdad")));
    }

    public String generateStatistics() {
        StringBuilder f = new StringBuilder();
        f.append(FormatUtils.formatBox(String.format("%s\n",ReportGenerator.generateUserReport(uman,aman))));
        f.append(FormatUtils.formatBox(String.format("%s\n",ReportGenerator.generateRoleReport(rman,aman))));
        f.append(FormatUtils.formatBox(String.format("amount of assignments: %d",aman.findAll().size())));
        return f.toString();
    }
}
