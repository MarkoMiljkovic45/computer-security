package hr.fer.zemris.java.hw06.shell.util.parser;

import java.util.ArrayList;
import java.util.List;

/**
 * A utility class with static methods for parsing ShellCommand arguments
 *
 * @author Marko Miljković (miljkovicmarko45@gmail.com)
 */
public class ArgumentParser {
    /**
     * Lexer used to generate ArgumentTokens
     */
    private final ArgumentLexer lexer;

    /**
     * The last processed token
     */
    private ArgumentToken currentToken;

    public ArgumentParser(char[] args) {
        this.lexer = new ArgumentLexer(args);

        this.currentToken = lexer.nextToken();
    }

    public ArgumentParser(String args) {
        this(args.toCharArray());
    }

    public List<String> parse() {
        List<String> args = new ArrayList<>();

        while(currentToken.getType() != ArgumentTokenType.EOF) {

            if (currentToken.getType() == ArgumentTokenType.WHITESPACE) {
                currentToken = lexer.nextToken();
                continue;
            }

            if (currentToken.getType() == ArgumentTokenType.QUOTE) {
                lexer.setState(ArgumentLexerState.STRING_LITERAL);
                currentToken = lexer.nextToken();
                continue;
            }

            if (currentToken.getType() == ArgumentTokenType.ARGUMENT){
                args.add(currentToken.getValue());
                lexer.setState(ArgumentLexerState.BASIC);
                currentToken = lexer.nextToken();
                continue;
            }

            throw new IllegalArgumentException("Unexpected ArgumentToken: " + currentToken);
        }

        return args;
    }
}
