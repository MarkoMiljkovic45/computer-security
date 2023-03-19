package hr.fer.zemris.java.hw06.shell.util.parser;

/**
 * Used to describe argument lexer tokens
 *
 * @author Marko Miljković (miljkovicmarko45@gmail.com)
 */
public enum ArgumentTokenType {
    /** Represents a single argument **/
    ARGUMENT,
    /** Represents ' ' whitespace character **/
    WHITESPACE,
    /** Represents " character **/
    QUOTE,
    /** End of file token **/
    EOF
}
