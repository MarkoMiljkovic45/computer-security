package hr.fer.srs.util.parser;

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
