import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class AssignmentManager implements Repository<RoleAssignment>{
    public Map<String, RoleAssignment> data;

    @Override
    public void add(RoleAssignment item) {
        if(item != null)
        {
            data.put(item.assignmentId(),item);
        }
    }

    @Override
    public boolean remove(RoleAssignment item) {
        return data.remove(item.assignmentId()) != null;
    }

    @Override
    public Optional<RoleAssignment> findById(String id){
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<RoleAssignment> findAll() {
        return (List<RoleAssignment>)data.values();
    }

    public List<RoleAssignment> findAll(AssignmentFilter filter, Comparator<RoleAssignment> sorter) {
        return data.values().stream().filter(filter::test).sorted(sorter).collect(Collectors.toList());
    }

    @Override
    public int count() {return data.size();}

    @Override
    public void clear() {data.clear();}

    public List<RoleAssignment> findByUser(User user) {
        return findByFilter(AssignmentFilters.byUser(user));
    }

    public List<RoleAssignment> findByRole(Role role) {
        return findByFilter(AssignmentFilters.byRole(role));
    }

    public List<RoleAssignment> getActiveAssignments() {
        return findByFilter(AssignmentFilters.activeOnly());
    }

    public List<RoleAssignment> getExpiredAssignments() {
        return findByFilter(AssignmentFilters.expiringBefore(LocalDateTime.now().toString()));
    }

    public boolean userHasRole(User user, Role role) {
        List<RoleAssignment> a = findByUser(user) ;
        a.retainAll(findByRole(role));
        return !a.isEmpty();
    }

    public boolean userHasPermission(User user, String permissionName, String resource) {
        List<RoleAssignment> a = findByUser(user);
        for(RoleAssignment b : a) {
            if(b.role().hasPermission(permissionName,resource)) {
                return true;
            }
        }
        return false;
    }

    public Set<Permission> getUserPermissions(User user) {
        Set<Permission> a = new HashSet<Permission>();
        List<RoleAssignment> b = findByUser(user);
        for(RoleAssignment c : b) {
            a.addAll(c.role().getPermissions());
        }
        return a;
    }

    void revokeAssignment(String assignmentId) {
        Optional<RoleAssignment> a = findById(assignmentId);
        a.ifPresent(vou -> {
            if(vou.assignmentType().equals("PERMANENT")) {
                ((PermanentAssignment)vou).revoke();
            }
        });
    }

    void extendTemporaryAssignment(String assignmentId, String newExpirationDate) {
        Optional<RoleAssignment> a = findById(assignmentId);
        a.ifPresent(vou -> {
            if(vou.assignmentType().equals("TEMPORARY")) {
                ((TemporaryAssignment)vou).extend(newExpirationDate);
            }
        });
    }

    public List<RoleAssignment> findByFilter(AssignmentFilter filter) {
        return data.values().stream().filter(filter::test).collect(Collectors.toList());
    }

}
