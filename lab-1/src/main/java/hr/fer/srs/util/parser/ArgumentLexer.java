package hr.fer.srs.util.parser;

import hr.fer.srs.util.reader.StringReader;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class ArgumentLexer {
    /**
     * StringReader that is used to split the arguments into tokens
     */
    private final StringReader reader;

    /**
     * Current state of the lexer
     */
    private ArgumentLexerState state;

    public static final char escapeCharacter;

    public static final List<Character> escapableCharacters;

    static {
        escapeCharacter = '\\';

        escapableCharacters = new ArrayList<>();
        escapableCharacters.add('"');
        escapableCharacters.add('\\');
    }

    public ArgumentLexer(char[] args, ArgumentLexerState state) {
        this.state = state;
        this.reader = new StringReader(args, 0, escapeCharacter, escapableCharacters);
    }

    public ArgumentLexer(char[] args) {
        this(args, ArgumentLexerState.BASIC);
    }

    public ArgumentToken nextToken() {
        try {
            if (state == ArgumentLexerState.BASIC) {
                if (!reader.read(c -> c != ' ').equals("")) {
                    return new ArgumentToken(" ", ArgumentTokenType.WHITESPACE);
                }

                if (reader.read(c -> c != '"').equals("\"")) {
                    return new ArgumentToken("\"", ArgumentTokenType.QUOTE);
                }

                return new ArgumentToken(reader.read(c -> c == ' '), ArgumentTokenType.ARGUMENT);
            }

            if (state == ArgumentLexerState.STRING_LITERAL) {
                String rawToken = reader.readAndEscape(c -> c == '"');
                reader.skip(1); //Skip STRING_LITERAL closing "

                return new ArgumentToken(rawToken, ArgumentTokenType.ARGUMENT);
            }
        } catch (NoSuchElementException e) {
            return new ArgumentToken("", ArgumentTokenType.EOF);
        }

        throw new IllegalArgumentException("Unexpected lexer state.");
    }

    public ArgumentLexerState getState() {
        return state;
    }

    public void setState(ArgumentLexerState state) {
        this.state = state;
    }
}
