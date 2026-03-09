import java.util.List;

public class FormatUtils {
    static String formatTable(String[] headers, List<String[]> rows) {
        StringBuilder f = new StringBuilder();
        int size = 10 * headers.length;
        f.append(String.format("%s\n","-".repeat(Math.max(size,0))));
        for(String i : headers) {
            f.append(String.format("*%s",(padRight(truncate(i,10),10))));
        }
        f.append("*\n|");
        for(String[] i : rows) {
            for(String j : i) {
                f.append(String.format("|%s",padRight(truncate(j,10),10)));
            }
            f.append("|*|");
        }
        return f.toString();
    }
    static String formatBox(String text) {
        return String.format(
                "%s\n|%s|\n%s",
                "-".repeat(text.length() + 2),
                text,
                "-".repeat(text.length() + 2));
    }
    static String formatHeader(String text) {
        return String.format(
                "%s\n*%s*\n%s",
                "*".repeat(text.length() + 2),
                text,
                "*".repeat(text.length() + 2));
    }
    static String truncate(String text, int maxLength) {
        if(text.length() > maxLength) {
            char[] new_text = new char[maxLength];
            text.getChars(0,maxLength-2,new_text,0);
            return String.copyValueOf(new_text).concat("...");
        }
        return text;
    }
    static String padRight(String text, int length) {
        return text.concat(" ".repeat(Math.max(0,length - text.length())));
    }
    static String padLeft(String text, int length) {
        return " ".repeat(Math.max(0,length - text.length())).concat(text);
    }
}
