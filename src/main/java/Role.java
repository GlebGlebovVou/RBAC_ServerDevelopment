import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.io.*;

public class Role {
    public String id;
    public String name;
    public String description;
    Set<Permission> permissions;
    public Role(String name, String description) {
        this.id = "role_" + UUID.randomUUID().toString();
        this.name = name;
        this.description = description;
        this.permissions = new HashSet<Permission>();
    }
    void addPermission(Permission permission) {
        permissions.add(permission);
    }
    void removePermission(Permission permission) {
        permissions.remove(permission);
    }
    boolean hasPermission(Permission permission) {
        return permissions.contains(permission);
    }
    boolean hasPermission(String permissionName, String resource) {
        for(Permission x : permissions) {
            if(x.name().equals(permissionName) &&
                    x.resource().equals(resource)) {
                return true;
            }
        }
        return false;
    }
    Set<Permission> getPermissions() {
        return Set.copyOf(permissions);
    }
    @Override
    public boolean equals(Object u) {
        if(u == this)
            return true;
        if(u == null)
            return false;
        if(!(u instanceof Role o))
            return false;
        return o.name.equals(this.name) && o.description.equals(this.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name,this.description);
    }

    @Override
    public String toString() {
        return String.format("id: %s name: %s",id,name);
    }

    String format() {
        StringBuilder out = new StringBuilder(String.format("Role: %s [ID: %s]\nDescription: %s",
                name, id, description));
        try {
            out.append(String.format("\nPermissions (%d):\n", permissions.size()));
            for (Permission x : permissions) {
                out.append(String.format("- %s\n", x.format()));
            }
        }
        catch (Exception e) {
            IO.println(e.getMessage());
        }
        return out.toString();
    }
}
