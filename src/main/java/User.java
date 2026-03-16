import java.util.regex.*;

public record User(String username, String fullName, String email) {
    static public User validate(String username, String fullName, String email) {
        try {
            if(!ValidationUtils.isValidUsername(username))
                throw new IllegalArgumentException("username should have only digits, latin symbols or _ and be longer than 3 and shorter than 20");
            if(!ValidationUtils.isValidEmail(email))
                throw new IllegalArgumentException("email should have @ and . after it");
        }
        catch (Exception e){
            IO.println(e.getMessage());
            return null;
        }
        return new User(username, fullName, email);
    }
    public String format() {
        return String.format("%s (%s) <%s>",this.username(),this.fullName(),this.email());
    }

}
