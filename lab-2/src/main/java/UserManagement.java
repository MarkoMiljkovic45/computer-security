public class UserManagement {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Invalid number of arguments.");
            return;
        }

        String command  = args[0];
        String username = args[1];

        switch (command) {
            case "add"       -> add(username);
            case "passwd"    -> passwd(username);
            case "forcepass" -> forcepass(username);
            case "del"       -> del(username);
            default          -> System.out.printf("Command %s not recognised\n", command);
        }
    }

    /**
     * 1. Prompt for password
     * 2. Check il len > 8
     * 3. Retype password and check for match
     * 4. Salt and save the password
     * @param username
     */
    private static void add(String username) {
        //TODO
    }

    private static void passwd(String username) {
        //TODO
    }

    private static void forcepass(String username) {
        //TODO
    }

    private static void del(String username) {
        //TODO
    }
}
