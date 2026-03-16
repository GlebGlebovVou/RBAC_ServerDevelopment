import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationUtils {
    static boolean isValidUsername(String username) {
        Pattern pat = Pattern.compile("\\S{3,20}");
        return pat.matcher(username).find() && !Pattern.compile("\\W+").matcher(username).find() &&
                !Pattern.compile("\\S{21,}").matcher(username).find();
    }
    static boolean isValidEmail(String email) {
        Pattern pat = Pattern.compile("\\w+@\\w*[.]\\w*");
        return pat.matcher(email).find();
    }
    static boolean isValidDate(String date) {
        Pattern pat = Pattern.compile("\\d+-\\d+-\\d+T\\d+:\\d+:\\d+[.]\\d+");
        return pat.matcher(date).find();
    }
    static String normalizeString(String input) {
        input = input.replace(" ","").toLowerCase(Locale.ROOT);
        return input;
    }
    static void requireNonEmpty(String value, String fieldName) throws Exception {
        if(value.isEmpty()) {
            throw new Exception();
        }
    }
}
