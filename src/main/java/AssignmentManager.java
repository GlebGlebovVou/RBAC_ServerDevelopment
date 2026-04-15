import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

public class AssignmentManager implements Repository<RoleAssignment>{
    public ConcurrentMap<String, RoleAssignment> data = new ConcurrentHashMap<String, RoleAssignment>();
    private final Object obj = new Object();

    @Override
    public void add(RoleAssignment item) {
        RoleAssignment prev = data.putIfAbsent(item.assignmentId(),item);
        if(prev != null)
        {
            throw new IllegalArgumentException("Item with that id exists");
        }
        AuditLog.log("ASSIGNMENT_ADD",item.metadata().assignedBy(),item.user().username(),item.metadata().reason());
    }

    @Override
    public boolean remove(RoleAssignment item) {
        if(this.data.remove(item.assignmentId()) != null) {
            AuditLog.log("ASSIGNMENT_REMOVE",item.metadata().assignedBy(),item.user().username(),item.metadata().reason());
            return true;
        }
        return false;
    }

    @Override
    public Optional<RoleAssignment> findById(String id){
        return Optional.ofNullable(this.data.get(id));
    }

    @Override
    public synchronized List<RoleAssignment> findAll() {
        return data.values().stream().toList();
    }

    public synchronized List<RoleAssignment> findAll(AssignmentFilter filter, Comparator<RoleAssignment> sorter) {
        return data.values().stream().filter(filter::test).sorted(sorter).collect(Collectors.toList());
    }

    @Override
    public int count() {return data.size();}

    @Override
    public synchronized void clear() {data.clear();}

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

    public synchronized void revokeAssignment(String assignmentId) {
        Optional<RoleAssignment> a = findById(assignmentId);
        a.ifPresent(vou -> {
            if(vou.assignmentType().equals("PERMANENT")) {
                ((PermanentAssignment)vou).revoke();
            }
        });
    }

    public synchronized void extendTemporaryAssignment(String assignmentId, String newExpirationDate) {
        Optional<RoleAssignment> a = findById(assignmentId);
        a.ifPresent(vou -> {
            if(vou.assignmentType().equals("TEMPORARY")) {
                ((TemporaryAssignment)vou).extend(newExpirationDate);
            }
        });
    }

    public synchronized List<RoleAssignment> findByFilter(AssignmentFilter filter) {
        return data.values().stream().filter(filter::test).collect(Collectors.toList());
    }

    public synchronized List<RoleAssignment> findByFilterParallel(AssignmentFilter filter) {
        return data.values().parallelStream().filter(filter::test).collect(Collectors.toList());
    }

}
