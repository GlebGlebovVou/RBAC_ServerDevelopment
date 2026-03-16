import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CommandParser {
    static Map<String,Command> commands = new HashMap<String,Command>();
    static Map<String,String> commandDescriptions = new HashMap<String,String>();

    static void registerCommand(String name,String description, Command command) {
        commands.put(name,command);
        commandDescriptions.put(name,description);
    }

    static void executeCommand(String commandName, Scanner scanner, RBACSystem system, String[] args) {
        if(commands.containsKey(commandName)) {
            commands.get(commandName).execute(scanner,system,args);
        }
        else {
            IO.println("Error! No such command");
        }
    }

    static void printHelp() {
        IO.println(FormatUtils.formatHeader("Help"));
        for(String name : commands.keySet()) {
            String desc = commandDescriptions.get(name);
            IO.println(FormatUtils.formatBox(String.format("%s: %s",name,desc)));
        }
    }

    static void parseAndExecute(String input, Scanner scanner, RBACSystem system) {
        String[] split = input.split(" ");
        String name = split[0];
        String[] args = (String[]) Arrays.stream(split).skip(1).toArray();
        executeCommand(split[0],scanner,system,args);
    }
}
