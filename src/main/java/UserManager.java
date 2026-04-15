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
            if(data.putIfAbsent(item.username(),item) != null) {
                IO.println(String.format("%s user is probably already contains; try user-update",item.username()));
            }
            else {
                IO.println(String.format("User %s was added!",item.username()));
            }
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

    public synchronized List<User> findByFilter(UserFilter filter) {
        return data.values().stream().filter(filter::test).collect(Collectors.toList());
    }

    public synchronized List<User> findByFilterParallel(UserFilter filter) {
        return data.values().parallelStream().filter(filter::test).collect(Collectors.toList());
    }

    public synchronized boolean exists(String username) {
        return data.get(username) != null;
    }

    public synchronized void update(String username, String newFullName, String newEmail) {
        if(data.get(username) != null) {
            User u = User.validate(username,newFullName,newEmail);
            User u1 = findById(username).get();
            if(u1 != null && u != null && u.format().compareTo(u1.format()) != 0) {
                data.remove(username);
                add(User.validate(username, newFullName, newEmail));
            }
            else {
                IO.println(String.format("Cannot update user %s",username));
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
