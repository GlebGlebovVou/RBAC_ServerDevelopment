import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class RoleManager implements Repository<Role>{
    public Map<String, Role> data;
    public Map<String,Role> dataIndex;
    @Override
    public void add(Role item) {
        if(item != null && !exists(item.name)) {
            data.put(item.id,item);
            dataIndex.put(item.name,item);
        }
    }

    @Override
    public boolean remove(Role item) {
        boolean a = exists(item.name);
        if(a) {
            data.remove(item.id);
            dataIndex.remove(item.name);
        }
        return a;
    }

    @Override
    public Optional<Role> findById(String id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<Role> findAll() {
        return data.values().stream().toList();
    }

    @Override
    public int count() {
        return data.size();
    }

    @Override
    public void clear() {
        data.clear();
    }
    public Optional<Role> findByName(String name) {
        return Optional.ofNullable(dataIndex.get(name));
    }
    public List<Role> findByFilter(RoleFilter filter) {
        return data.values().stream().filter(filter::test).toList();
    }
    public List<Role> findAll(RoleFilter filter, Comparator<Role> sorter) {
        return data.values().stream().filter(filter::test).sorted(sorter).collect(Collectors.toList());
    }
    public boolean exists(String name) {
        return dataIndex.get(name) != null;
    }

    public void addPermissionToRole(String roleName, Permission permission) {
        findByName(roleName).ifPresent(role -> role.addPermission(permission));
    }

    public void removePermissionFromRole(String roleName, Permission permission) {
        findByName(roleName).ifPresent(role -> role.removePermission(permission));
    }

    public List<Role> findRolesWithPermission(String permissionName, String resource) {
        return findByFilter(RoleFilters.hasPermission(permissionName,resource));
    }
}
