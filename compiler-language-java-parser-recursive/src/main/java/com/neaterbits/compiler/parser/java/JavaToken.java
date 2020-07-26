package com.neaterbits.compiler.parser.java;

import java.util.Objects;

import com.neaterbits.util.parse.CharType;
import com.neaterbits.util.parse.CharTypeInteger;
import com.neaterbits.util.parse.CharTypeWS;
import com.neaterbits.util.parse.IToken;
import com.neaterbits.util.parse.TokenType;

public enum JavaToken implements IToken {

    NONE(TokenType.NONE),
    EOF(TokenType.EOF),

    IF("if"),
    ELSE("else"),

    FOR("for"),
    WHILE("while"),
    DO("do"),

    SWITCH("switch"),
    CASE("case"),
    DEFAULT("default"),

    BREAK("break"),
    CONTINUE("continue"),
    
    RETURN("return"),
    THROW("throw"),
    
    THROWS("throws"),

    IMPORT("import"),
    
    ENUM("enum"),
    CLASS("class"),
    INTERFACE("interface"),
    PACKAGE("package"),
    
    TRUE("true"),
    FALSE("false"),
    
    VOID("void"),
    
    BYTE("byte"),
    SHORT("short"),
    INT("int"),
    LONG("long"),
    FLOAT("float"),
    DOUBLE("double"),
    CHAR("char"),
    BOOLEAN("boolean"),

    IMPLEMENTS("implements"),
    EXTENDS("extends"),

    SUPER("super"),
    
    FINAL("final"),
    ABSTRACT("abstract"),
    
    STATIC("static"),

    PUBLIC("public"),
    PROTECTED("protected"),
    PRIVATE("private"),
    
    VARARGS("..."),

    STRING_LITERAL('"', '"'),
    CHARACTER_LITERAL('\'', '\''),
    
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
    ASSIGN('='),
    
    EXCLAMATION('!'),
    
    AT('@'),
    QUESTION_MARK('?'),
    
    EQUALS("=="),
    NOT_EQUALS("!="),
    LT('<'),
    GT('>'),
    LTE("<="),
    GTE(">="),
    
    LOGICAL_AND("&&"),
    LOGICAL_OR("||"),
    
    INCREMENT("++"),
    DECREMENT("--"),
    
    PLUS('+'),
    MINUS('-'),
    MUL('*'),
    DIV('/'),
    MOD('%'),

    NEW("new"),

    NUMBER(CharTypeInteger.INSTANCE),
    
    LONG_NUMBER(string -> {
        
        CustomMatchResult matchResult;
        
        final int len = string.length();
        
        if (len < 2) { // requires at least a digit and 'l' or 'L'
            matchResult = CustomMatchResult.POSSIBLE_MATCH;
        }
        else {
            final char lastChar = string.charAt(len - 1);
            
            if (lastChar == 'l' || lastChar == 'L') {
                
                final int decimalPartLen = len - 1;

                matchResult = matches(string, decimalPartLen, Character::isDigit)
                        ? CustomMatchResult.MATCH
                        : CustomMatchResult.NO_MATCH;
            }
            else {
                matchResult = matches(string, len, Character::isDigit)
                        ? CustomMatchResult.POSSIBLE_MATCH
                        : CustomMatchResult.NO_MATCH;
            }
        }
        
        return matchResult;
    }),
    
    IDENTIFIER(string -> {
        
        CustomMatchResult matchResult;

        if (Character.isJavaIdentifierStart(string.charAt(0))) {

            matchResult = CustomMatchResult.MATCH;
            
            for (int i = 1; i < string.length(); ++ i) {
                
                if (!Character.isJavaIdentifierPart(string.charAt(i))) {
                    matchResult = CustomMatchResult.NO_MATCH;
                    break;
                }
            }
        }
        else {
            matchResult = CustomMatchResult.NO_MATCH;
        }
        
        return matchResult;
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
    
    private final CustomMatcher custom;

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

    private JavaToken(char fromCharacter, char toCharacter) {
        this.tokenType = TokenType.FROM_CHAR_TO_CHAR;
        this.character = fromCharacter;
        this.charType = null;
        this.toCharacter = toCharacter;
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

    private JavaToken(CustomMatcher custom) {

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
    public CustomMatcher getCustom() {
        return custom;
    }

    @FunctionalInterface
    interface CharMatcher {
        boolean matches(char c);
    }
    
    private static boolean matches(CharSequence sequence, int length, CharMatcher matcher) {

        boolean matches = true;
        
        for (int i = 0; i < length; ++ i) {
            if (!matcher.matches(sequence.charAt(i))) {
                matches = false;
                break;
            }
        }
        
        return matches;
    }
}
