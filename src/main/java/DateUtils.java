import java.time.LocalDate;
import java.time.LocalDateTime;

public class DateUtils {
    static String getCurrentDate() {
        return LocalDate.now().toString();
    }
    static String getCurrentDateTime() {
        return LocalDateTime.now().toString();
    }
    static boolean isBefore(String date1, String date2) {
        return date1.compareTo(date2) < 0;
    }
    static boolean isAfter(String date1, String date2) {
        return date1.compareTo(date2) > 0;
    }
    static String addDays(String date, int days) {
        return LocalDate.parse(date).plusDays(days).toString();
    }
    static String formatRelativeTime(String date) {
        int days = Integer.parseInt(date.split(" ")[2]) - Integer.parseInt(getCurrentDate().split(" ")[2]);
        if(days < 0) {
            return String.format("%d days ago",-days);
        }
        return String.format("in %d days",days);
    }
}
