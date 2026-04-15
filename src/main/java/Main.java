//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import java.util.List;
import java.util.Scanner;

public class Main {
    static void testValidation() {
        List<List<String>> testsList = List.of(
                List.of("User","Name Full Of User", "something@gmail.com"),
                List.of("User wrong wrong","Name Full Of User", "something@gmail.com"),
                List.of("User","Name Full Of User", "wrong@gmailcom"),
                List.of("User","Name Full Of User", "somethinggmail.com"),
                List.of("U","Name Full Of User", "something@gmail.com"),
                List.of("UserTooooooLooooooooooooooooong","Name Full Of User", "something@gmail.com")
                );
        int ind = 1;
        for(List<String> curCheck : testsList) {
            IO.println(String.format("%d. user: (usern: %s, full n: %s, em: %s)", ind, curCheck.get(0), curCheck.get(1), curCheck.get(2)));
            User u = User.validate(curCheck.get(0), curCheck.get(1), curCheck.get(2));
            if (u != null) {
                IO.println("Correct validation");
            }
            ind++;
            IO.println();
        }
    }
    static void testSomething() {
        User us = User.validate("goal","cool","fdfd@gfg.");
        Permission p = new Permission("ogo","users","klluta");
        Permission vou = new Permission("nice","users","sdfjjksdfhk");
        Role r = new Role("User","hihiha");
        r.addPermission(p);
        r.addPermission(vou);
        PermanentAssignment a = new PermanentAssignment(us,r,AssignmentMetadata.now("user","fdfdf"));
        a.revoke();
        IO.println(a.summary());
        TemporaryAssignment b = new TemporaryAssignment(us,r,AssignmentMetadata.now("user","sadasd"));
        IO.println(b.summary());
    }
    static void testNagruz() {
        RBACSystem system = new RBACSystem();
        Scanner s = new Scanner(System.in);
        system.initialize();
        class MyRunnable implements Runnable {
            public int id;
            public RBACSystem system;
            public Scanner s;
            public int phase = 0;
            public String fullname;
            public MyRunnable(int id, RBACSystem system, Scanner s) {
                this.id = id;
                this.system = system;
                this.s = s;
                this.fullname = "Fullname";
            }

            @Override
            public void run() {
                while(true) {
                    IO.println(String.format("%d thread: ", this.id));
                    switch(phase) {
                        case 0:
                            CommandParser.executeCommand("user-create", this.s, this.system,
                                    new String[]{"username", fullname, "ogo@gmail.com"});
                            IO.println(this.system.getUserManager().count());
                            break;
                        case 1:
                            this.fullname += "a";
                            CommandParser.executeCommand("user-update", this.s, this.system,
                                    new String[]{"username", fullname, "ogo@gmail.com"});
                            IO.println(this.system.getUserManager().count());
                            break;
                        case 2:
                            CommandParser.executeCommand("user-list",this.s,this.system,null);
                            break;
                        case 3:
                            
                    }
                    phase = (phase + 1) % 4;
                    try {
                        Thread.sleep(3000);
                    } catch (Exception e) {
                        IO.println("error");
                    }
                }
            }
        }
        int size = 5;
        Thread[] uf = new Thread[size];
        for(int i = 0; i < size; i++) {
            uf[i] = new Thread(new MyRunnable(i+1,system,s));
            uf[i].start();
        }
        for(int i = 0; i < size; i++) {
            try{uf[i].join();}
            catch(Exception e) {IO.println("error");}
        }
    }
    static void main() {
        //testValidation();
        //testSomething();
        testNagruz();
    }
}
