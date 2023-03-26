package hr.fer.srs.util.parser;

/**
 * Used to represent argument lexer tokens
 *
 * @author Marko Miljković (miljkovicmarko45@gmail.com)
 */
public class ArgumentToken {
    /**
     * Data stored in the token
     */
    private final String value;

    /**
     * The type of token
     */
    private final ArgumentTokenType type;

    public ArgumentToken(String value, ArgumentTokenType type) {
        this.value = value;
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public ArgumentTokenType getType() {
        return type;
    }
}
