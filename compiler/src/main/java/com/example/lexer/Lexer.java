package com.example.lexer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Lexer {

    private BufferedReader stream; // input stream reader
    private Token nextToken;
    private int nextChar;
    private int lineNumber = 1; // current line
    private int columNumber = 1; // current column

    private final static Map<String, TokenType> reservedWords; // reserved words
    private final static Map<Character, TokenType> punctuation;
    private final static Map<String, TokenType> properties; // reserved words
    private final static Map<String, TokenType> attributes; // reserved words

    private int errors; // number of errors

    static {
        reservedWords = new HashMap<>();
        reservedWords.put("linearLayout", TokenType.LINEAR_LAYOUT);
        reservedWords.put("editText", TokenType.EDIT_TEXT);
        reservedWords.put("textView", TokenType.TEXT_VIEW);

        punctuation = new HashMap<>();
        punctuation.put('{', TokenType.OPEN_REGION);
        punctuation.put('}', TokenType.CLOSE_REGION);
        punctuation.put('(', TokenType.OPEN_PROPERTIES);
        punctuation.put(')', TokenType.CLOSE_PROPERTIES);
        punctuation.put('=', TokenType.ASSIGN_VALUE);
        punctuation.put(',', TokenType.COMMA);
        punctuation.put(';', TokenType.SEMI);

        properties = new HashMap<>();
        properties.put("orientation", TokenType.ORIENTATION);
        properties.put("width", TokenType.WIDTH);
        properties.put("height", TokenType.HEIGHT);

        attributes = new HashMap<>();
        attributes.put("text", TokenType.TEXT_VALUE);
        attributes.put("textSize", TokenType.TEXT_SIZE);
    }

    public Lexer(FileReader file) {
        this.stream = new BufferedReader(file);
        nextChar = getChar();
    }

    public int getErrors() {
        return errors;
    }

    // handles I/O for char stream
    private int getChar() {
        try {
            return stream.read();
        } catch (IOException e) {
            System.err.print(e.getMessage());
            System.err.println("IOException in Lexer::getChar()");
            return -1;
        }
    }

    // detect and skip possible '\n', '\r' and '\rn' line breaks
    private boolean skipNewLine() {
        if (nextChar == '\n') {
            lineNumber++;
            columNumber = 1;
            nextChar = getChar();
            return true;
        }

        if (nextChar == '\r') {
            lineNumber++;
            columNumber = 1;
            nextChar = getChar();

            // skip over next char if '\n'
            if (nextChar == '\n')
                nextChar = getChar();
            return true;
        }

        // new line char not found
        return false;
    }

    // return the next token without consuming it
    public Token peek() throws IOException {
        // advance token only if its been reset by getToken()
        if (nextToken == null)
            nextToken = getToken();

        return nextToken;
    }

    // return the next token in the input stream (EOF signals end of input)
    public Token getToken() throws IOException {
        // check if peek() was called
        if (nextToken != null) {
            Token token = nextToken;
            nextToken = null; // allow peek to call for next token
            return token;
        }

        // skip white space character
        while (Character.isWhitespace(nextChar)) {
            // check if whitespace char is a new line
            if (!skipNewLine()) {
                columNumber++;
                nextChar = getChar();
            }

            // offset colNum for tab chars
            if (nextChar == '\t')
                columNumber +=4;
        }

        // identifier, reserved word ([a-zA-z]*) or property ([A-Z_]*)
        if (Character.isLetter(nextChar)) {
            // create new idVal starting with first char or identifier
            String current = Character.toString((char) nextChar);
            columNumber++;
            nextChar = getChar();

            // include remaining sequence of chars that are letters, digits or _
            while (Character.isLetterOrDigit(nextChar) || (Character.toString((char) nextChar).equals("_"))) {
                current += (char) nextChar;
                columNumber++;
                nextChar = getChar();
            }

            // check if identifier is a reserved word
            TokenType type = reservedWords.get(current);

            if (type != null)
                return new Token(type, new TokenAttribute(), lineNumber, columNumber - current.length());

            TokenType prop = properties.get(current);

            if (prop != null)
                return new Token(prop, new TokenAttribute(), lineNumber, columNumber - current.length());

            TokenType attribute = attributes.get(current);

            if (attribute != null)
                return new Token(attribute, new TokenAttribute(), lineNumber, columNumber - current.length());

            if (current.equals("MATCH_PARENT"))
                return new Token(
                        TokenType.MATCH_PARENT,
                        new TokenAttribute("MATCH_PARENT"), lineNumber, columNumber - current.length()
                );
            else if (current.equals("WRAP_CONTENT"))
                return new Token(
                        TokenType.WRAP_CONTENT,
                        new TokenAttribute("WRAP_CONTENT"), lineNumber, columNumber - current.length()
                );
            else if (current.equals("VERTICAL"))
                return new Token(
                        TokenType.VERTICAL,
                        new TokenAttribute("VERTICAL"), lineNumber, columNumber - current.length()
                );
            else if (current.equals("HORIZONTAL"))
                return new Token(
                        TokenType.HORIZONTAL,
                        new TokenAttribute("HORIZONTAL"), lineNumber, columNumber - current.length()
                );

            // token is an identifier
            return new Token(TokenType.ID, new TokenAttribute(current), lineNumber, columNumber - current.length());
        }

        if (Character.isDigit(nextChar)) {
            // create string representation of number
            String numString = Character.toString((char) nextChar);
            columNumber++;
            nextChar = getChar();

            // concatenate remaining sequence of digits
            while (Character.isDigit(nextChar)) {
                numString += (char) nextChar;
                columNumber++;
                nextChar = getChar();
            }

            int digits;
            try {
                digits = Integer.parseInt(numString);
            } catch (Exception e) {
                return new Token(TokenType.UNKNOWN, new TokenAttribute(), lineNumber, columNumber - numString.length() + 1);
            }

            /*if (!Character.isDigit(nextChar)) {
                nextChar = getChar();
                columNumber++;

                while (!Character.isWhitespace(nextChar)) {
                    columNumber++;
                    numString += nextChar;
                    nextChar = getChar();
                }

                return new Token(TokenType.UNKNOWN, new TokenAttribute(), lineNumber, columNumber - numString.length() + 1);
            }*/

            return new Token(
                    TokenType.DIGIT,
                    new TokenAttribute(digits),
                    lineNumber,
                    columNumber - numString.length() + 1
            );
        }

        if (nextChar == '\"') {
            String text = Character.toString((char) nextChar);

            nextChar = getChar();
            columNumber++;

            while (Character.isLetterOrDigit(nextChar) || Character.isWhitespace(nextChar)) {
                text += (char) nextChar;
                columNumber++;
                nextChar = getChar();
            }

            if (nextChar == '\"') {
                text += Character.toString((char) nextChar);
                nextChar = getChar();
                columNumber++;

                return new Token(TokenType.STRING, new TokenAttribute(text), lineNumber, columNumber - text.length() - 1);
            }

            return new Token(TokenType.UNKNOWN, new TokenAttribute(), lineNumber, columNumber - text.length() - 1);
        }

        // EOF reached
        if (nextChar == -1)
            return new Token(TokenType.EOF, new TokenAttribute(), lineNumber, columNumber);

        // check for punctuation
        TokenType type = punctuation.get((char) nextChar);
        columNumber++;
        nextChar = getChar();

        // found punctuation token
        if (type != null)
            return new Token(type, new TokenAttribute(), lineNumber, columNumber - 1);

        // token type is unknown
        return new Token(TokenType.UNKNOWN, new TokenAttribute(), lineNumber, columNumber - 1);
    }
}
