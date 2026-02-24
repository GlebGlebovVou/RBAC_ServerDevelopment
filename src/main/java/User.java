import java.util.regex.*;
import java.io.*;

public record User(String username, String fullName, String email) {
    static public User validate(String username, String fullName, String email) {
        try {
            Pattern usernamePat = Pattern.compile("\\W+");
            Matcher usernameMat =  usernamePat.matcher(username);
            if(usernameMat.find())
                throw new IllegalArgumentException("username should have only digits, latin symbols and _");
            if (username.length() < 3 || username.length() > 20)
                throw new IllegalArgumentException("username should be longer than 3 and shorter than 20");
            Pattern emailPat = Pattern.compile("\\w+@\\w*[.]\\w*");
            Matcher emailMat = emailPat.matcher(email);
            if(!emailMat.find())
                throw new IllegalArgumentException("email should have @ and . after it");

        }
        catch (Exception e){
            IO.println(e.getMessage());
            return null;
        }
        return new User(username, fullName, email);
    }
    String format() {
        return String.format("%s (%s) <%s>",this.username,this.fullName,this.email);
    }

}
