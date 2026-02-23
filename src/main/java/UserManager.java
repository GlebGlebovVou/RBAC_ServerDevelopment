import java.util.*;
import java.util.stream.Collectors;

public class UserManager implements Repository<User>{
    public Map<String,User> data = new HashMap<String, User>();

    @Override
    public void add(User item) {
        if(item != null && !exists(item.username()))
        {
            data.put(item.username(),item);
        }
    }

    @Override
    public boolean remove(User item){return data.remove(item.username()) != null;}

    @Override
    public Optional<User> findById(String id){
        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        return data.values().stream().toList();
    }

    public List<User> findAll(UserFilter filter, Comparator<User> sorter) {
        return data.values().stream().filter(filter::test).sorted(sorter).collect(Collectors.toList());
    }

    @Override
    public int count() {return data.size();}

    @Override
    public void clear() {data.clear();}
    public Optional<User> findByUsername(String username) {
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
        return data.values().stream().filter(filter::test).collect(Collectors.toList());
    }

    public boolean exists(String username) {
        return data.get(username) != null;
    }

    public void update(String username, String newFullName, String newEmail) {
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
