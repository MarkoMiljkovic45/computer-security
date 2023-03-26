package hr.fer.srs.util.parser;

/**
 * Used to determine weather the lexer is reading regular text
 * or a string literal with escape characters
 *
 * @author Marko Miljković (miljkovimarko45@gmail.com)
 */
public enum ArgumentLexerState {
    /** In this state the lexer reads the character sequentially, no special actions will be taken **/
    BASIC,
    /** In this state the lexer will escape te " and \ characters **/
    STRING_LITERAL
}
