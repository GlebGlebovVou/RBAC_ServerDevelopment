import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

public class RoleManager implements Repository<Role>{

    public ConcurrentMap<String, Role> data = new ConcurrentHashMap<String, Role>();
    public ConcurrentMap<String,Role> dataIndex = new ConcurrentHashMap<String, Role>();
    private final Object obj = new Object();

    @Override
    public void add(Role item) {
        if(item != null) {
            Role ogo = data.putIfAbsent(item.id,item);
            dataIndex.putIfAbsent(item.name,item);
            if(ogo != null) {
                throw new IllegalArgumentException("Role already added");
            }
        }
    }

    @Override
    public boolean remove(Role item) {
        boolean a = exists(item.name);
        if(a) {
            synchronized(obj) {
                data.remove(item.id);
                dataIndex.remove(item.name);
            }
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
    public synchronized void clear() {
        data.clear();
    }
    public synchronized Optional<Role> findByName(String name) {
        return Optional.ofNullable(dataIndex.get(name));
    }
    public synchronized List<Role> findByFilter(RoleFilter filter) {
        return data.values().stream().filter(filter::test).toList();
    }
    public synchronized List<Role> findByFilterParallel(RoleFilter filter) {
        return data.values().parallelStream().filter(filter::test).toList();
    }
    public synchronized List<Role> findAll(RoleFilter filter, Comparator<Role> sorter) {
        return data.values().stream().filter(filter::test).sorted(sorter).collect(Collectors.toList());
    }
    public synchronized boolean exists(String name) {
        return dataIndex.get(name) != null;
    }

    public synchronized void addPermissionToRole(String roleName, Permission permission) {
        findByName(roleName).ifPresent(role -> role.addPermission(permission));
    }

    public synchronized void removePermissionFromRole(String roleName, Permission permission) {
        findByName(roleName).ifPresent(role -> role.removePermission(permission));
    }

    public List<Role> findRolesWithPermission(String permissionName, String resource) {
        return findByFilter(RoleFilters.hasPermission(permissionName,resource));
    }
}
