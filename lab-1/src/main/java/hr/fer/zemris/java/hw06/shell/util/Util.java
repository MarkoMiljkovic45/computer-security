package hr.fer.zemris.java.hw06.shell.util;

import hr.fer.zemris.java.hw06.shell.util.parser.ArgumentParser;

import java.util.List;

/**
 * A utility class with static methods
 *
 * @author Marko Miljković (miljkovicmarko45@gmail.com)
 */
public class Util {

    /**
     * Used for parsing ShellCommand arguments
     * <p>
     * If an argument starts with a quotation character escaping is supported for " and \.
     * Every other situation in which after \ follows anything but " and \ will be copied as two characters
     *
     * @param args String containing all arguments
     * @return String array of individual arguments
     */
    public static List<String> parse(String args) {
        if (args == null || args.equals("")) {
            return null;
        }

        ArgumentParser parser = new ArgumentParser(args);
        return parser.parse();
    }
}
