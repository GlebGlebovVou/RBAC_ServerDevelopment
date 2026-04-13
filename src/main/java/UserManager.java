import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

public class UserManager implements Repository<User>{

    public ConcurrentMap<String,User> data = new ConcurrentHashMap<String, User>();
    private final Object obj = new Object();

    @Override
    public void add(User item) {
        if(item != null)
        {
            data.putIfAbsent(item.username(),item);
        }
    }

    @Override
    public boolean remove(User item){
        if (item != null) {
            synchronized (obj) {
                return data.remove(item.username()) != null;
            }
        }
        return false;
    }

    @Override
    public synchronized Optional<User> findById(String id){
        return Optional.ofNullable(this.data.get(id));
    }

    @Override
    public synchronized List<User> findAll() {
        return data.values().stream().toList();
    }

    public synchronized List<User> findAll(UserFilter filter, Comparator<User> sorter) {
        return data.values().stream().filter(filter::test).sorted(sorter).collect(Collectors.toList());
    }

    @Override
    public int count() {return data.size();}

    @Override
    public synchronized void clear() {data.clear();}
    public synchronized Optional<User> findByUsername(String username) {
        return Optional.ofNullable(data.get(username));
    }

    public Optional<User> findByEmail(String email) {
        for(User u : data.values()) {
            if(u.email().equals(email)) {
                return Optional.of(u);
            }
        }
        return Optional.empty();
    }

    public List<User> findByFilter(UserFilter filter) {
        IO.println(data.values().stream().filter(filter::test).count());
        return data.values().stream().filter(filter::test).collect(Collectors.toList());
    }

    public synchronized boolean exists(String username) {
        return data.get(username) != null;
    }

    public synchronized void update(String username, String newFullName, String newEmail) {
        if(data.get(username) != null) {
            User u = User.validate(username,newFullName,newEmail);
            if(u != null) {
                data.remove(username);
                add(User.validate(username, newFullName, newEmail));
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if(o == null) {
            return false;
        }
        if(!(o instanceof UserManager)) {
            return false;
        }
        return this.data.equals(((UserManager)o).data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data);
    }
}
