import java.util.List;

public class FormatUtils {
    public static String formatTable(String[] headers, List<String[]> rows) {
        StringBuilder f = new StringBuilder();
        int size = (15+1) * (headers.length);
        String format = String.format("%s*\n", "*".repeat(Math.max(size, 0)));
        f.append(format);
        for(String i : headers) {
            f.append(String.format("*%s",padLeft(padRight(truncate(i,10),10),15)));
        }
        f.append("*\n");
        f.append(format);
        for(String[] i : rows) {
            for(String j : i) {
                f.append(String.format("*%s",padLeft(padRight(truncate(j,10),10),15)));
            }
            f.append("*\n");
        }
        f.append(format);
        return f.toString();
    }
    public static String formatBox(String text) {
        return String.format(
                "%s\n|%s|\n%s\n",
                "-".repeat(text.length() + 2),
                text,
                "-".repeat(text.length() + 2));
    }
    public static String formatHeader(String text) {
        return String.format(
                "%s\n*%s*\n%s\n",
                "*".repeat(text.length() + 2),
                text,
                "*".repeat(text.length() + 2));
    }
    public static String truncate(String text, int maxLength) {
        maxLength -= 3;
        if(text.length() > maxLength) {
            char[] new_text = new char[maxLength];
            text.getChars(0,maxLength,new_text,0);
            return String.copyValueOf(new_text).concat("...");
        }
        return text;
    }
    public static String padRight(String text, int length) {
        return text.concat(" ".repeat(Math.max(0,length - text.length())));
    }
    public static String padLeft(String text, int length) {
        return " ".repeat(Math.max(0,length - text.length())).concat(text);
    }
}
