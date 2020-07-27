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
    
    TRY("try"),
    CATCH("catch"),
    FINALLY("finally"),
    
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
    
    BITWISE_OR('|'),

    NEW("new"),

    NUMBER(CharTypeInteger.INSTANCE),
    
    DECIMAL_LITERAL(true, string -> matchDecimalString(string, string.length())),
    LONG_DECIMAL_LITERAL(true, string -> matchLong(string, 0, JavaToken::matchDecimalString)),

    HEX_LITERAL(true, string -> matchHexString(string, string.length())),
    LONG_HEX_LITERAL(true, string -> matchLong(string, 2, JavaToken::matchHexString)),

    OCTAL_LITERAL(true, string -> matchOctalString(string, string.length())),
    LONG_OCTAL_LITERAL(true, string -> matchLong(string, 1, JavaToken::matchOctalString)),

    BINARY_LITERAL(true, string -> matchBinaryString(string, string.length())),
    LONG_BINARY_LITERAL(true, string -> matchLong(string, 2, JavaToken::matchBinaryString)),

    IDENTIFIER(false, string -> {
        
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
    private final boolean customEager;

    private JavaToken(TokenType tokenType) {

        Objects.requireNonNull(tokenType);

        this.tokenType = tokenType;
        this.character = 0;
        this.charType = null;
        this.toCharacter = 0;
        this.literal = null;
        this.toLiteral = null;
        this.custom = null;
        this.customEager = false;
    }

    private JavaToken(char character) {
        this.tokenType = TokenType.CHARACTER;
        this.character = character;
        this.charType = null;
        this.toCharacter = 0;
        this.literal = null;
        this.toLiteral = null;
        this.custom = null;
        this.customEager = false;
    }

    private JavaToken(char fromCharacter, char toCharacter) {
        this.tokenType = TokenType.FROM_CHAR_TO_CHAR;
        this.character = fromCharacter;
        this.charType = null;
        this.toCharacter = toCharacter;
        this.literal = null;
        this.toLiteral = null;
        this.custom = null;
        this.customEager = false;
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
        this.customEager = false;
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
        this.customEager = false;
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
        this.customEager = false;
    }

    private JavaToken(boolean customEager, CustomMatcher custom) {

        Objects.requireNonNull(custom);

        this.tokenType = TokenType.CUSTOM;
        this.character = 0;
        this.charType = null;
        this.toCharacter = 0;
        this.literal = null;
        this.toLiteral = null;
        this.custom = custom;
        this.customEager = customEager;
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

    @Override
    public boolean isEager() {

        return tokenType == TokenType.CUSTOM && customEager;
    }

    @FunctionalInterface
    interface CharMatcher {
        boolean matches(char c);
    }
    
    @FunctionalInterface
    interface MatchString {
        
        CustomMatchResult match(CharSequence string, int length);
    }
    
    private static CustomMatchResult matchLong(CharSequence string, int prefixLength, MatchString matchString) {
        
        final int len = string.length();
        
        final CustomMatchResult stringMatches;
        
        if (len <= prefixLength + 1) {
            
            // at most 3 characters, eg. 0x1
            
            final CustomMatchResult matchResult = matchString.match(string, len);
            
            stringMatches = matchResult == CustomMatchResult.MATCH
                    ? CustomMatchResult.POSSIBLE_MATCH
                    : matchResult;
        }
        else {
            final char lastChar = string.charAt(len - 1);
            
            // at least 4 characters, eg. 0x1L so room for 'l' or 'L'
            
            if (lastChar == 'l' || lastChar == 'L') {
                
                // Should be the complete thing
                final int decimalPartLen = len - 1;

                stringMatches = matchString.match(string, decimalPartLen);
                
                // Should always match or not since decimalPartLen > 2, unless ends with '_'
                if (stringMatches == CustomMatchResult.POSSIBLE_MATCH && string.charAt(decimalPartLen - 1) != '_') {
                    throw new IllegalStateException();
                }
            }
            else {
                stringMatches = matchString.match(string, len);
                
                // Should always match or not since decimalPartLen > 2, unless ends with '_'
                if (stringMatches == CustomMatchResult.POSSIBLE_MATCH && string.charAt(len - 1) != '_') {
                    throw new IllegalStateException();
                }
            }
        }
        
        return stringMatches;
    }
    
    private static CustomMatchResult matchDecimalString(CharSequence string, int len) {

        final CustomMatchResult matchResult;
        
        final char c = string.charAt(0);
        
        if (c == '0') {
            // conflict with octal, "0" by itself (len 1) is a decimal while a longer is octal 
            matchResult = len == 1
                    ? CustomMatchResult.MATCH
                    : CustomMatchResult.NO_MATCH;
        }
        else {
            matchResult = matches(string, len, Character::isDigit);
        }
    
        return matchResult;
    }

    private static CustomMatchResult matchOctalString(CharSequence string, int len) {

        final CustomMatchResult matchResult;
        
        switch (len) {
        case 0:
            throw new IllegalStateException();
            
        case 1:
            matchResult = string.charAt(0) == '0'
                    ? CustomMatchResult.POSSIBLE_MATCH
                    : CustomMatchResult.NO_MATCH;
            break;
            
        default:
            matchResult = matches(string, len, StringUtils::isOctalDigit);
        }
        
        return matchResult;
    }

    
    private static CustomMatchResult matchHexString(CharSequence string, int len) {

        return matchPrefixString(string, len, 'x', 'X', StringUtils::isHexDigit);
    }

    private static CustomMatchResult matchBinaryString(CharSequence string, int len) {

        return matchPrefixString(string, len, 'b', 'B', c -> c == '0' || c == '1');
    }

    private static CustomMatchResult matchPrefixString(
            CharSequence string,
            int len,
            char lowerPrefix,
            char upperPrefix,
            CharMatcher charMatcher) {
        
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
            final char charAt1 = string.charAt(1);
            
            matchResult = string.charAt(0) == '0' && (charAt1 == lowerPrefix || charAt1 == upperPrefix)
                ? CustomMatchResult.POSSIBLE_MATCH
                : CustomMatchResult.NO_MATCH;
            break;

        default:
            matchResult = matches(string, 2, len, charMatcher);
            break;
        }

        return matchResult;
    }

    private static CustomMatchResult matches(CharSequence sequence, int length, CharMatcher matcher) {
        
        return matches(sequence, 0, length, matcher);
    }
        
    private static CustomMatchResult matches(CharSequence sequence, int initial, int length, CharMatcher matcher) {

        CustomMatchResult matches = CustomMatchResult.MATCH;
        
        for (int i = initial; i < length; ++ i) {
            
            final char c = sequence.charAt(i);
            
            if (c == '_') {
                
                if (i == initial) {
                    // not allowed to have underscore as initial character
                    matches = CustomMatchResult.NO_MATCH;
                    break;
                }
                else if (i == length - 1) {
                    // might match if given more input
                    matches = CustomMatchResult.POSSIBLE_MATCH;
                    break;
                }
            }
            else {
                if (!matcher.matches(c)) {
                    matches = CustomMatchResult.NO_MATCH;
                    break;
                }
            }
        }
        
        return matches;
    }
}
