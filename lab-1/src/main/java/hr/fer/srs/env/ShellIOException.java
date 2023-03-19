package hr.fer.srs.env;

/**
 * This exception is thrown if there is an exception when reading from or writing to the shell environment.
 * <p>
 * When shell catches ShellIOException it must terminate since no communication is possible with the user.
 *
 * @author Marko Miljković (miljkovicmarko45@gmail.com)
 */
public class ShellIOException extends RuntimeException {

    public ShellIOException() {
    }

    public ShellIOException(String message) {
        super(message);
    }
}
