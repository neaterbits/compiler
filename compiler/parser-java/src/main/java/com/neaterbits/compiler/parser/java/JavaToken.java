package com.neaterbits.compiler.parser.java;

import java.util.Objects;
import java.util.function.Predicate;

import com.neaterbits.util.parse.CharType;
import com.neaterbits.util.parse.CharTypeInteger;
import com.neaterbits.util.parse.CharTypeWS;
import com.neaterbits.util.parse.IToken;
import com.neaterbits.util.parse.TokenType;

public enum JavaToken implements IToken {

    NONE(TokenType.NONE),
    EOF(TokenType.EOF),

    IF("if"),

    FOR("for"),
    WHILE("while"),
    DO("do"),

    SWITCH("switch"),
    CASE("case"),
    DEFAULT("default"),

    BREAK("break"),
    CONTINUE("continue"),

    IMPORT("import"),
    
    ENUM("enum"),
    CLASS("class"),
    INTERFACE("interface"),
    PACKAGE("package"),

    IMPLEMENTS("implements"),
    EXTENDS("extends"),

    FINAL("final"),
    STATIC("static"),

    PUBLIC("public"),
    PROTECTED("protected"),
    PRIVATE("private"),

    LBRACE('{'),
    RBRACE('}'),

    LBRACKET('['),
    RBRACKET(']'),

    LPAREN('('),
    RPAREN(')'),

    PERIOD('.'),
    COMMA(','),
    SEMI(';'),
    COLON(':'),
    ASTERISK('*'),

    DIGIT(CharTypeInteger.INSTANCE),

    IDENTIFIER(string -> {
        
        boolean isIdentifier;

        if (Character.isJavaIdentifierStart(string.charAt(0))) {

            isIdentifier = true;
            
            for (int i = 1; i < string.length(); ++ i) {
                
                if (!Character.isJavaIdentifierPart(string.charAt(i))) {
                    isIdentifier = false;
                    break;
                }
            }
        }
        else {
            isIdentifier = false;
        }
        
        return isIdentifier;
    }),
    
    C_COMMENT("/*", "*/"),
    CPP_COMMENT("//", TokenType.FROM_STRING_TO_EOL),

    WS(CharTypeWS.INSTANCE);

    private final TokenType tokenType;
    private final char character;
    private final char toCharacter;
    private final CharType charType;

    private final String literal;
    private final String toLiteral;
    
    private final Predicate<CharSequence> custom;

    private JavaToken(TokenType tokenType) {

        Objects.requireNonNull(tokenType);

        this.tokenType = tokenType;
        this.character = 0;
        this.charType = null;
        this.toCharacter = 0;
        this.literal = null;
        this.toLiteral = null;
        this.custom = null;
    }

    private JavaToken(char character) {
        this.tokenType = TokenType.CHARACTER;
        this.character = character;
        this.charType = null;
        this.toCharacter = 0;
        this.literal = null;
        this.toLiteral = null;
        this.custom = null;
    }

    private JavaToken(CharType charType) {

        Objects.requireNonNull(charType);

        this.tokenType = TokenType.CHARTYPE;
        this.character = 0;
        this.charType = charType;
        this.toCharacter = 0;
        this.literal = null;
        this.toLiteral = null;
        this.custom = null;
    }

    private JavaToken(String literal, TokenType tokenType) {

        Objects.requireNonNull(literal);
        Objects.requireNonNull(tokenType);

        this.tokenType = tokenType;
        this.character = 0;
        this.charType = null;
        this.toCharacter = 0;
        this.literal = literal;
        this.toLiteral = null;
        this.custom = null;
    }

    private JavaToken(String literal) {
        this(literal, TokenType.CI_LITERAL);
    }

    private JavaToken(String fromLiteral, String toLiteral) {

        Objects.requireNonNull(fromLiteral);
        Objects.requireNonNull(toLiteral);

        this.tokenType = TokenType.FROM_STRING_TO_STRING;
        this.character = 0;
        this.charType = null;
        this.toCharacter = 0;
        this.literal = fromLiteral;
        this.toLiteral = toLiteral;
        this.custom = null;
    }

    private JavaToken(Predicate<CharSequence> custom) {

        Objects.requireNonNull(custom);

        this.tokenType = TokenType.CUSTOM;
        this.character = 0;
        this.charType = null;
        this.toCharacter = 0;
        this.literal = null;
        this.toLiteral = null;
        this.custom = custom;
    }

    @Override
    public TokenType getTokenType() {
        return tokenType;
    }

    @Override
    public char getCharacter() {
        return character;
    }

    @Override
    public char getFromCharacter() {
        return character;
    }

    @Override
    public char getToCharacter() {
        return toCharacter;
    }

    @Override
    public String getLiteral() {
        return literal;
    }

    @Override
    public String getFromLiteral() {
        return literal;
    }

    @Override
    public String getToLiteral() {
        return toLiteral;
    }

    @Override
    public CharType getCharType() {
        return charType;
    }

    @Override
    public Predicate<CharSequence> getCustom() {
        return custom;
    }
}
