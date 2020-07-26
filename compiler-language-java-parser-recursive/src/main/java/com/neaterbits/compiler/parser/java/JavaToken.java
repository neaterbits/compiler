package com.neaterbits.compiler.parser.java;

import java.util.Objects;

import com.neaterbits.util.StringUtils;
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
    
    DECIMAL_LITERAL(string -> {
        
        final CustomMatchResult matchResult;
        
        final char c = string.charAt(0);
        
        if (c == '0') {
            // conflict with octal, "0" by itself is a decimal 
            matchResult = string.length() == 1
                    ? CustomMatchResult.MATCH
                    : CustomMatchResult.NO_MATCH;
        }
        else {
            matchResult = matches(string, Character::isDigit)
                    ? CustomMatchResult.MATCH
                    : CustomMatchResult.NO_MATCH;
        }
    
        return matchResult;
    }),
    LONG_DECIMAL_LITERAL(string -> {
        
        CustomMatchResult matchResult;
        
        final int len = string.length();
        
        switch (len) {
        
        case 0:
            throw new IllegalStateException();

        case 1:
            matchResult = Character.isDigit(string.charAt(0))
                    ? CustomMatchResult.POSSIBLE_MATCH
                    : CustomMatchResult.NO_MATCH;
            break;
            
        default:
            if (string.charAt(0) == '0') {
                matchResult = CustomMatchResult.NO_MATCH;
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
            break;
        }
        
        return matchResult;
    }),

    HEX_LITERAL(string -> matchHex(string, string.length())),
    
    LONG_HEX_LITERAL(string -> {
        
        final int len = string.length();
        
        final CustomMatchResult hexMatches;
        
        if (len <= 3) {
            
            // at most 3 characters, eg. 0x1
            
            final CustomMatchResult matchResult = matchHex(string, len);
            
            hexMatches = matchResult == CustomMatchResult.MATCH
                    ? CustomMatchResult.POSSIBLE_MATCH
                    : matchResult;
        }
        else {
            final char lastChar = string.charAt(len - 1);
            
            // at least 4 characters, eg. 0x1L so room for 'l' or 'L'
            
            if (lastChar == 'l' || lastChar == 'L') {
                
                // Should be the complete thing
                final int decimalPartLen = len - 1;

                hexMatches = matchHex(string, decimalPartLen);
                
                // Should always match or not since decimalPartLen > 2
                if (hexMatches == CustomMatchResult.POSSIBLE_MATCH) {
                    throw new IllegalStateException();
                }
            }
            else {
                final CustomMatchResult matchResult = matchHex(string, len);
                
                // Should always match or not since decimalPartLen > 2
                if (matchResult == CustomMatchResult.POSSIBLE_MATCH) {
                    throw new IllegalStateException();
                }

                // If matches as regular hex, then this is a possible match since not ending with 'l' or 'L'
                hexMatches = matchResult == CustomMatchResult.MATCH
                        ? CustomMatchResult.POSSIBLE_MATCH
                        : CustomMatchResult.NO_MATCH;
            }
        }
        
        return hexMatches;
    }),

    OCTAL_LITERAL(string -> {
        
        final CustomMatchResult matchResult;
        
        final char c = string.charAt(0);
        
        if (c == '0') {
            
            if (string.length() > 1) {
                // conflict with octal, "0" by itself is a decimal 
                matchResult = matches(string, 1, string.length(), StringUtils::isOctalDigit)
                        ? CustomMatchResult.MATCH
                        : CustomMatchResult.NO_MATCH;
            }
            else {
                matchResult = CustomMatchResult.POSSIBLE_MATCH;
            }
        }
        else {
            matchResult = CustomMatchResult.NO_MATCH;
        }
    
        return matchResult;
    }),
    LONG_OCTAL_LITERAL(string -> {
        
        CustomMatchResult matchResult;
        
        final int len = string.length();
        
        // requires at least a 0 prefix, a digit and 'l' or 'L' for a match
        
        switch (len) {
        case 0:
            throw new IllegalStateException();
            
        case 1:
            matchResult = string.charAt(0) == '0'
                    ? CustomMatchResult.POSSIBLE_MATCH
                    : CustomMatchResult.NO_MATCH;
            break;

        case 2:
            matchResult = string.charAt(0) == '0' && StringUtils.isOctalDigit(string.charAt(1))
                ? CustomMatchResult.POSSIBLE_MATCH
                : CustomMatchResult.NO_MATCH;
            break;
            
        default:
            final char lastChar = string.charAt(len - 1);
            
            if (string.charAt(0) != '0') {
                matchResult = CustomMatchResult.NO_MATCH;
            }
            else {
                if (lastChar == 'l' || lastChar == 'L') {
                    
                    final int decimalPartLen = len - 1;
    

                    matchResult = matches(string, 1, decimalPartLen, StringUtils::isOctalDigit)
                            ? CustomMatchResult.MATCH
                            : CustomMatchResult.NO_MATCH;
                }
                else {
                    matchResult = matches(string, 1, len, StringUtils::isOctalDigit)
                            ? CustomMatchResult.POSSIBLE_MATCH
                            : CustomMatchResult.NO_MATCH;
                }
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
    
    private static CustomMatchResult matchHex(CharSequence string, int len) {
        CustomMatchResult matchResult;
        
        switch (len) {
        case 0:
            throw new IllegalStateException();

        case 1:
            matchResult = string.charAt(0) == '0'
                ? CustomMatchResult.POSSIBLE_MATCH
                : CustomMatchResult.NO_MATCH;
            break;

        case 2:
            matchResult = string.charAt(0) == '0' && string.charAt(1) == 'x'
                ? CustomMatchResult.POSSIBLE_MATCH
                : CustomMatchResult.NO_MATCH;
            break;

        default:
                matchResult = matches(string, 2, len, StringUtils::isHexDigit)
                    ? CustomMatchResult.MATCH
                    : CustomMatchResult.NO_MATCH;
                break;
        }

        return matchResult;
    }

    private static boolean matches(CharSequence sequence, CharMatcher matcher) {
        
        return matches(sequence, 0, sequence.length(), matcher);
    }

    private static boolean matches(CharSequence sequence, int length, CharMatcher matcher) {
        
        return matches(sequence, 0, length, matcher);
    }
        
    private static boolean matches(CharSequence sequence, int initial, int length, CharMatcher matcher) {

        boolean matches = true;
        
        for (int i = initial; i < length; ++ i) {
            if (!matcher.matches(sequence.charAt(i))) {
                matches = false;
                break;
            }
        }
        
        return matches;
    }
}
