import java.time.LocalDate;
import java.time.LocalDateTime;

public class DateUtils {
    public static String getCurrentDate() {
        return LocalDate.now().toString();
    }
    public static String getCurrentDateTime() {
        return LocalDateTime.now().toString();
    }
    public static boolean isBefore(String date1, String date2) {
        return date1.compareTo(date2) < 0;
    }
    public static boolean isAfter(String date1, String date2) {
        return date1.compareTo(date2) > 0;
    }
    public static String addDays(String date, int days) {
        return LocalDate.parse(date).plusDays(days).toString();
    }
    public static String formatRelativeTime(String date) {
        LocalDate date1 = LocalDate.parse(date);
        LocalDate date2 = LocalDate.parse(getCurrentDate());
        long days = (date2.getYear() * 365L + date2.getDayOfYear()) - (date1.getYear() * 365L + date1.getDayOfYear());
        if(days > 0) {
            return String.format("%d days ago",-days);
        }
        return String.format("in %d days",days);
    }
}
